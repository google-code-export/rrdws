package org.vietspider.html.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.vietspider.chars.TextVerifier; 
import org.vietspider.chars.ValueVerifier; 
import org.vietspider.common.util.Queue;
import org.vietspider.common.util.Stack;
import org.vietspider.html.HTMLNode; 
import org.vietspider.html.Name;
import org.vietspider.html.NodeIterator;
import org.vietspider.html.Tag;
import org.vietspider.html.parser.NodeImpl; 
import org.vietspider.token.TypeToken;
import org.vietspider.token.attribute.Attribute;
import org.vietspider.token.attribute.Attributes;

import cc.co.llabor.cache.css.CSStore;
import cc.co.llabor.cache.js.Item;
import cc.co.llabor.cache.js.JSStore;
 
import ws.rrd.server.Base64Coder;
import ws.rrd.server.LServlet;
import ws.rrd.server.SServlet;

 
public class HyperLinkUtil {   
   
  private SiteLinkVerifier siteLinkVerifier; 
  private NormalLinkVerifier normalLinkVerifier;
  private MetaLinkVerifier metaLinkVerifier;
  private JavaScriptVerifier jsVerifier;
  private StyleVerifier cssVerifier;
  
  private final static Map<String, String> linkAttributeMap = new HashMap<String, String>(4); 
  private final static Map<String, String> linkAttributeFullMap = new HashMap<String, String>(5);
  private final static Map<String, String> pageAttributeFullMap = new HashMap<String, String>(5);
  private final static Map<String, List<String>> scriptAttributeFullMap = new HashMap<String, List<String>>(5);
  
  public HyperLinkUtil() { 
    siteLinkVerifier = new SiteLinkVerifier(); 
    normalLinkVerifier = new NormalLinkVerifier();
    metaLinkVerifier = new MetaLinkVerifier();
    jsVerifier = new JavaScriptVerifier();
    cssVerifier = new StyleVerifier();

    linkAttributeMap.put("a", "href");
    linkAttributeMap.put("iframe", "src");
    linkAttributeMap.put("frame", "src");
    linkAttributeMap.put("meta", "url");

    linkAttributeFullMap.put("a", "href");
    linkAttributeFullMap.put("iframe", "src");
    linkAttributeFullMap.put("frame", "src");
    linkAttributeFullMap.put("meta", "url");
    linkAttributeFullMap.put("link", "href");
    linkAttributeFullMap.put("embed", "src");
    
    pageAttributeFullMap.put("a", "href");
    pageAttributeFullMap.put("meta", "url");
    pageAttributeFullMap.put("link", "href");
    pageAttributeFullMap.put("form", "action");
    pageAttributeFullMap.put("input", "src");
    pageAttributeFullMap.put("iframe", "src");
    pageAttributeFullMap.put("frame", "src");
    pageAttributeFullMap.put("embed", "src");
    pageAttributeFullMap.put("img", "src");
    pageAttributeFullMap.put("script", "src");
    pageAttributeFullMap.put("body", "background"); 
    pageAttributeFullMap.put("table", "background"); 
    pageAttributeFullMap.put("td", "background"); 
    pageAttributeFullMap.put("tr", "background"); 
    pageAttributeFullMap.put("th", "background"); 
    
    
    initSA();
    
  
  }
  
	  // Nach JavaScript (Netscape) erlaubt in folgenden HTML-Tags:
  private final static String tagList [] ={ "noscript","script","body","frameset","input","layer","select","textarea","a","area","input","textarea", 

  // Nach HTML 4.0 erlaubt in folgenden HTML-Tags:
   "a","abbr","acronym","address","area","b","big","blockquote","body","button","caption","center","cite","code","col","colgroup","dd","del","dfn","dir","div","dl","dt","em","fieldset","form","h1","h2","h3","h4","h5","h6","hr","i","img","input","ins","kbd","label","legend","li","link","map","menu","noframes","noscript","object","ol","optgroup","option","p","pre","q","s","samp","select","small","span","strike","strong","sub","sup","table","tbody","td","textarea","tfoot","th","thead","tr","tt","u","ul","var"
  };

  private final static String eventList[] = {
	    " onabort"/* (bei Abbruch) */  ,
  		" onblur"/* (beim Verlassen) */  ,
  		" onchange"/* (bei erfolgter �nderung) */  ,
  		" onclick"/* (beim Anklicken) */  ,
  		" ondblclick"/* (bei doppeltem Anklicken) */  ,
  		" onerror"/* (im Fehlerfall) */  ,
  		" onfocus"/* (beim Aktivieren) */  ,
  		" onkeydown"/* (bei gedr�ckter Taste) */  ,
  		" onkeypress"/* (bei gedr�ckt gehaltener Taste) */  ,
  		" onkeyup"/* (bei losgelassener Taste) */  ,
  		" onload"/* (beim Laden einer Datei) */  ,
  		" onmousedown"/* (bei gedr�ckter Maustaste) */  ,
  		" onmousemove"/* (bei weiterbewegter Maus) */  ,
  		" onmouseout"/* (beim Verlassen des Elements mit der Maus) */  ,
  		" onmouseover"/* (beim �berfahren des Elements mit der Maus) */  ,
  		" onmouseup"/* (bei losgelassener Maustaste) */  ,
  		" onreset"/* (beim Zur�cksetzen des Formulars) */  ,
  		" onselect"/* (beim Selektieren von Text) */  ,
  		" onsubmit"/* (beim Absenden des Formulars) */  ,
  		" onunload"/* (beim Verlassen der Datei) */  ,
  		" javascript:"/* (bei Verweisen) */
			};
private static final boolean TRACE = false;  
  // http://de.selfhtml.org/javascript/sprache/eventhandler.htm
  private static void initSA(){ 
		  for(String nextTag:tagList)
			  for (String nextEvent:eventList){
				   
				  List<String> events = scriptAttributeFullMap.get(nextTag);
				  events = events ==null?new ArrayList<String>(0):events;
				  scriptAttributeFullMap.put(nextTag, events);
				  events.add(nextEvent);
			  }
		  if(LServlet.TRACE)System.out.println(scriptAttributeFullMap);
  }
  
 
  
  public  List<String> scanSiteLink(HTMLNode node) {
    return scanSiteLink(null, node);
  }
  
  public List<String> scanSiteLink(List<String> values, HTMLNode node) {
    LinkAttributeHandler handler = new LinkAttributeHandler(values, siteLinkVerifier, linkAttributeMap);
    handler.handle(node);
    return handler.getValues();
  }
  
  public  List<String> scanSiteLink(List<NodeImpl> tokens) {
    LinkAttributeHandler handler = new LinkAttributeHandler(null, siteLinkVerifier, linkAttributeMap);
    for(int i = 0; i < tokens.size(); i++) {
      NodeImpl token = tokens.get(i);
      handler.handle(token);
    }
    return handler.getValues();
  }
  
  public  List<String> scanHomepageLinks(HTMLNode node) {
    Map<String, String> map = new HashMap<String, String>(4); 
    map.put("frame", "src");
    MapAttributeHandler handler = new MapAttributeHandler(null, siteLinkVerifier, map);
    handler.handle(node);
    return handler.getValues();
  }
  
  public List<String> scanImageLink(HTMLNode node) {
    IdentifierAttributeHandler handler = new IdentifierAttributeHandler(null, null, "img", "src");
    handler.handle(node);
    return handler.getValues();
  }
  
 
     
  public void createFullNormalLink(HTMLNode node, String swapServletUrl2, URL home) {    
	  	Map<String,String> mTmp = new HashMap<String, String>(); 
	  	mTmp.putAll(linkAttributeFullMap);
	  	mTmp.putAll(pageAttributeFullMap); 
	    createFullLink(node, mTmp,    swapServletUrl2, home, normalLinkVerifier);  
} 

  public  void createScriptLink(HTMLNode node, String swapServletUrl2, URL home) {

	  JavaScriptVerifier verifier = jsVerifier;
	  if (node == null)
		  return;	  
	  NodeIterator iterator = node.iterator();
	  while (iterator.hasNext()) {
		  HTMLNode n = iterator.next(); 
			  //System.out.println(n.getName());
		      if(verifier != null &&  verifier.verify(n)){ 
				//String  value = verifier.eval (n, null, null);
		        //value = prepareLinkValue(home , value);
		        //value = encode(swapServletUrl2, value);
		        verifier.modi(n, swapServletUrl2, home.toString(),home.toExternalForm()  ); 
		      } 
	  } 
  } 
  
  public  void createStyleLink(HTMLNode node, String swapServletUrl2, URL home) {

	  StyleVerifier verifier = cssVerifier;
	  if (node == null)
		  return;	  
	  NodeIterator iterator = node.iterator();
	  while (iterator.hasNext()) {
		  HTMLNode n = iterator.next(); 
			  //System.out.println(n.getName());
		      if(verifier != null &&  verifier.verify(n)){ 
				//String  value = verifier.eval (n, null, null);
		        //value = prepareLinkValue(home , value);
		        //value = encode(swapServletUrl2, value);
		        verifier.modi(n, swapServletUrl2, home.toString(),home.toExternalForm()  ); 
		      } 
	  } 
  } 
    
  
  public void createMetaLink(HTMLNode node,
			String swapServletUrl2, URL home) {
		Map<String, String> mTmp = new HashMap<String, String>();
		mTmp.put("META", "CONTENT");
		if (node == null)
			return;
		NodeIterator iterator = node.iterator();
		while (iterator.hasNext()) {
			HTMLNode n = iterator.next();
			if (n.isTag())
				createMetaLink(n, mTmp, swapServletUrl2, home, metaLinkVerifier);
		}
	}
  {
//		if (typeTmp == TypeToken.CLOSE  ){
//			level --;
//			lastParent = lastParent .getParent();
//			isClosed = true;
//		}else if (lastParent == null || n.getParent() == lastParent ){
//			if (!  (n.getConfig().end() == Tag.FORBIDDEN ) ){
//				lastParent = n;
//				level ++; 
//			}
//		}
	  
  }
  int level = 0;
  HTMLNode lastParent = null;
  
	private void createFullLink(HTMLNode node,
			Map<String, String> map, String swapServletUrl2, URL home,
			ValueVerifier verifier) {
		if (node == null)
			return;
		NodeIterator iterator = node.iterator();
		java.util.Stack<String> toCloseStack = new java.util.Stack<String>();
		HTMLNode n = null;
		while (iterator.hasNext()) {
			n = iterator.next();
			if (TRACE){
				Name nameTmp = n.getName();
				boolean isClosed = false;
				int typeTmp = ((NodeImpl)n).getType();
				if (lastParent == null ) {
					lastParent = n;
					level = 0;
				}else{
					HTMLNode myParent = n.getParent();
					String myParentName  = myParent.getName().toString();
					String myName  = n.getName().toString();
					String lastParentName  = lastParent.getName().toString();String a =myName+ myParentName +lastParentName;
					if (myParent == lastParent ){
						level +=  1 ; // next level
						lastParent = n;
					}else if (myParent == lastParent.getParent()){
						level += 0; // same level 
						lastParent = n;
					}else {
						// ../
						while(myParent.getParent() != lastParent){
							String spacesTmp = "                                                                                                                     ";
							toCloseStack .push("T"+typeTmp+":l:"+level+spacesTmp.substring(0, level*3)+"</"+lastParent.getName() +">");
							lastParent = lastParent.getParent();
							level -= 1; // low level
						}
						// cd <myParent>
						lastParent = n.getParent();
						level ++;
						//toCloseStack .pop();
						// cd <me>
						lastParent = n;
						level ++;
						//toCloseStack .pop();
					}
				}
				
				if (toCloseStack.size()>0){
					java.util.Queue<String> reverseOrderToCloseStack = new ConcurrentLinkedQueue<String>(toCloseStack.subList(0, toCloseStack.size() -1 ));
					toCloseStack.clear();
					for (String toClose : reverseOrderToCloseStack ){
						System.out.println(toClose);
					}
				}
				
				else
				
				{
					System.out.print("t"+typeTmp+":L:"+level);
					for (int l=0;l<level;l++){
						System.out.print("   ");
					}
					isClosed = typeTmp == TypeToken.CLOSE;
					if (!isClosed)	System.out.println("<"+nameTmp+">" +n);
					//else			System.out.println("</"+nameTmp+">" +n);
				}
			}
				
			if (n.isTag()){ 
				createFullSingleLink(n, map, swapServletUrl2, home, verifier);
			}
		}
		if (TRACE)System.out.println("<!--- "+node.getTextValue()+" --->");
	}  
  
  private void createFullSingleLink(HTMLNode node, Map<String, String> map,
                                    String swapServletUrl2, URL home, ValueVerifier verifier)   {
     
    String key =node.name().toLowerCase(); 
	String aKey = map.get(key);
	if ( null != aKey){
		createFullSingleLink(node,key,aKey,swapServletUrl2,home,verifier);
	}
  }
  
  
  private void createMetaLink(HTMLNode node, Map<String, String> map,
			String swapServletUrl2, URL home, ValueVerifier verifier) {
		Set<String> keys = map.keySet();
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (node.isNode(key) || (key.length() == 1 && key.charAt(0) == '*')) {
				Attributes attrs = node.getAttributes();
				String attrName = map.get(key);
				Attribute attr = attrs.get(attrName);
				if (attr == null)
					continue;
				String value = attr.getValue();
				if (value == null)
					continue;
				if (verifier != null && !verifier.verify(node,key, attrName )) continue;
 
				if (value.toUpperCase().indexOf("URL=HTTP")<0)continue;
				int urlStrinPos = value.toUpperCase().indexOf("HTTP");
				String urlTmp = value.substring(urlStrinPos);
				value = value.substring(0, urlStrinPos);
				value += swapServletUrl2 // createMetaLink #3
						+ new String(Base64Coder.encode(urlTmp.getBytes()));
			 
				 
				attr.setValue(value);
				attrs.set(attr);
			}
		}
	}  
  
  public void createFullLink(HTMLNode node, 
    String nodeName, String attrName, String swapServletUrl2, URL home, ValueVerifier verifier) {
    NodeIterator iterator = node.iterator();
    while(iterator.hasNext()) {
      HTMLNode n = iterator.next();
      if(n.isTag()){
    	  createFullSingleLink(n, nodeName, attrName,  swapServletUrl2, home, verifier);
      }
    }
  } 
  
  private void createFullSingleLink(HTMLNode node, 
      String nodeName, String attrName, String swapServletUrl2, URL home, ValueVerifier verifier)   {
  
        if(verifier != null && !verifier.verify(node, nodeName, attrName)) return;  
        String  value = verifier.eval (node, nodeName, attrName);
        value = encodeLink(swapServletUrl2, home, value);
        
        verifier.modi(node, nodeName, attrName, value);
 
  }



/**
 * @author vipup
 * @param swapServletUrl2
 * @param homeLinkPar
 * @param linkPar
 * @return
 */
  public static String encodeLink( URL homeLinkPar, String linkPar) {
	  return encodeLink(LServlet.SwapServletUrl ,homeLinkPar, linkPar);
  }
  public static String encodeLink(String swapServletUrl2, URL homeLinkPar, String linkPar) {
	linkPar = prepareLinkValue(homeLinkPar , linkPar);
	linkPar = encode(swapServletUrl2, linkPar);
	return linkPar;
}

	private static String prepareLinkValue(URL home, String value) {
		String homeStr = "" + home +"";
		if (value.startsWith("//")) {
			value = home.getProtocol()+":" + value;
		}else if (value.startsWith("/")) {
			String fileNameTmp = home.getFile();
			String sBaseTmp = ("" + home);
			if ("/".equals(fileNameTmp)) // assums home will be always with
											// "/"
				// at the end
				homeStr = sBaseTmp;
			else {
				int indexOfFileName = sBaseTmp.indexOf(fileNameTmp);
				if (indexOfFileName > 0)
					homeStr = sBaseTmp.substring(0, indexOfFileName) + "/";
				else
					homeStr = sBaseTmp + "/";
			}
			value = (homeStr + value.substring(1, value.length())); // createFullSingleLink
			// #3
		} else if (value.startsWith(".."))  {
			String uri = home.toString(); 
			int srvUrlLen = (home.getProtocol().length()+home.getHost().length());
			if (uri.length() > srvUrlLen )uri = uri.substring(0, uri.lastIndexOf("/"));
			//if (uri.length() > srvUrlLen )uri = uri.substring(0, uri.lastIndexOf("/") );
			uri += value.substring(2);
			uri += "";
			value = uri;
			// #2
		} else {
			if (homeStr.lastIndexOf("/")>7)
				homeStr = homeStr.substring(0, homeStr.lastIndexOf("/") + 1); //extract domain + full path
			else
				homeStr += "/"; // put '/' to end of url
			if (value.indexOf(homeStr) == 0)
				value = value;
			else if (value.indexOf("http://") == 0
					|| value.indexOf("https://") == 0)
				value = value;
			else
				value = homeStr + value;
		}
		return value;
	}

	public static  String encode(String swapServletUrl2, String value) {
		if (value.startsWith(swapServletUrl2)) return value;
		value  = swapServletUrl2 + new String (Base64Coder.encode(  value.getBytes() ));
		return value;
	}
	
	public static String decode(String decodedUrl) {
		String urlStr = decodedUrl;
		char[] charArray = decodedUrl.toCharArray();
		try{
			final byte[] decodedTmp = Base64Coder.decode(charArray);
			urlStr = ""+ new String(decodedTmp)+"";
		}catch(Throwable e){
			try{
				// aHR0cDovL2l0LXJ1LmRlL2ZvcnVtL3ZpZXdmb3J1bS5waHA/viewforum.php
				final int indexOfSLASH = decodedUrl.indexOf("/");
				charArray = decodedUrl.substring(0, indexOfSLASH +1 ).toCharArray();
				final byte[] decodedTmp = Base64Coder.decode(charArray);
				urlStr = ""+ new String(decodedTmp);//+decodedUrl.substring(indexOfSLASH+1);
			}catch (Throwable e1 ){ 
				e1.printStackTrace();
			}
		}
		return urlStr;
	}
  
  public static class SiteLinkVerifier extends TextVerifier implements ValueVerifier{
      String start[]={"mailto", "javascript", "window", "history", "#"};
      String end[]={"css", "js", "jpg", "png", "gif", "jpeg", "bmp", "dat", "exe", "txt", 
                    "java", "pdf", "doc", "xls", "rm", "ram", "wma", "wmv", "mp3", "swf", "zip", "jar", "rar"};
      String exist[] ={"img(\"", "image", ":sendim"} ;	  
    protected boolean verify(String link){
      if(link == null) return false;
      link = link.toLowerCase();    
      return !startOrEndOrExist(link, start, end, exist); 
    }
	@Override
	public void modi(HTMLNode node, String nodeName, String attrName, String newValue) {
        Attributes attrs = node.getAttributes();  
		Attribute attr = attrs.get(attrName);		
		String value = attr.getValue();
		if(LServlet.TRACE)System.out.println("NEW VAL for "+nodeName+".."+attrName+" : ["+value+"]=>"+newValue);
        attr.setValue( newValue);       
        attrs.set(attr);
	}    
  }
  
  public static class ImageLinkVerifier extends TextVerifier implements ValueVerifier{
      String exist[] = {"img", "image"};
      String end[]={"jpg", "gif", "jpeg", "png", "ico", "svg", "bmp", "dib"};	  
     protected boolean verify(String link){
      link = link.toLowerCase();    

      return existIn(link, exist) || endWith(link, end);
    }
 	@Override
	public void modi(HTMLNode node, String nodeName, String attrName, String newValue) {
        Attributes attrs = node.getAttributes();  
		Attribute attr = attrs.get(attrName);		
		String value = attr.getValue();
		 
		if(LServlet.TRACE)System.out.println("NEW VAL for "+nodeName+".."+attrName+" : ["+value+"]=>"+newValue);
        attr.setValue( newValue);       
        attrs.set(attr);
	}     
  }
  
  public static class JavaScriptVerifier extends TextVerifier implements ValueVerifier {
		String start[] = {  "javascript", "window", "history"};
		String exist[] = {"document.", "eval","navigator.","window.", ".location", "this"};
		String end[] = {};
		protected boolean verify(String strScriptPar) {
			strScriptPar = strScriptPar.toLowerCase();
			return startOrEndOrExist(strScriptPar.toLowerCase(), start, end, exist);
		}	  
 
		public boolean verify(HTMLNode node) { 
			boolean retval = false;
			String nodeName = node.getName().toString();
			List<String> events = scriptAttributeFullMap.get(nodeName.toLowerCase());
			if ( events == null) return false;
			for (String nextAttr:events){
				Attribute scriptOnEventAttr = node.getAttributes().get(nextAttr);
				if ( scriptOnEventAttr == null) continue;
				String scriptOnEvent = scriptOnEventAttr.getValue().toString();
				if ( scriptOnEvent == null) continue;
				// ATTRibute check
				if (verify(scriptOnEvent)){
					Attribute a1 = new Attribute("_attrVale", scriptOnEvent);
					Attribute a2 = new Attribute("_attrName", nextAttr);
					node.getAttributes().add(a1 );
					node.getAttributes().add(a2 ); 
					retval = true;
				} 
			} 
			// SCRIPT body-check
			if ("SCRIPT".equals( node.getName().toString())){
				String scriptOnBody = node.getTextValue(); 
				if (verify(scriptOnBody)){
					retval = true;
				}
			}
			return retval;
		}
		
		private String  clearBody(HTMLNode node, final String scriptValue, String xRef) {
			node.clearChildren();// setChild(0, new
			String retval= LServlet.calcBase()+"S/"+"l"+ xRef.hashCode() + ".js";
			String stringTmp = "SCRIPT SRC=\""+retval +"\"";
			
			// HTMLNode(){})getChildren().clear()setValue("/*
			// 8-X */".toCharArray());
			final String scriptAliasTmp = (stringTmp);
			// replace Script-Content by link to cached value
			//ScriptItem siTmp = ssTmp.getByValue();
			node.setValue(scriptAliasTmp.toCharArray());
			//System.out.println("" + node.getTextValue());
			return retval;
			
		}
		@Override
		public void modi(HTMLNode node, String rootServletPar, String refPar, String refPar2) {
	        Attributes attrs = node.getAttributes();  
			String newValue = null; 	
			String value =  node.getTextValue() ;
			if (verify(value)){
				String cacheKey = clearBody(node, value, refPar);//clearBody(node, scriptOnBody);

				JSStore ssTmp = JSStore.getInstanse();
				Item scriptTmp =  ssTmp .putOrCreate(cacheKey, value ,refPar);
				if (1==2) System.out.println(scriptTmp);
				
				String newLink = cacheKey;
				newValue = newLink;
				//node.setValue(  newLink.toCharArray() ); 
				if(LServlet.TRACE)System.out.println("NEW VAL for SCRIPT   : ["+value+"]=>"+newValue);
			}else{
				if(LServlet.TRACE)System.out.println(" no changes for "+node.getName());
			}
	        
	        
		}		
	}
  
  
  public static class StyleVerifier extends TextVerifier implements ValueVerifier {
		String start[] = {  "style" };
		String exist[] = {"url", "import", "script", "onclick", "img","background-image"};
		String end[] = {};
		protected boolean verify(String strScriptPar) {
			String lowerCase = strScriptPar.toLowerCase();
			strScriptPar = lowerCase;
			return startOrEndOrExist(lowerCase, start, end, exist) ||
			startOrEndOrExist(lowerCase, start,  end, eventList);
		}	  

		public boolean verify(HTMLNode node) { 
			boolean retval = false;
			String nodeName = node.getName().toString();
			if (1==2)System.out.println(nodeName);
			// CSS body-check
			if ("STYLE".equals( node.getName().toString())){ 
					retval = true; 
			}
			return retval;
		}
		
		private String  clearBody(HTMLNode node, final String scriptValue, String xRefUrl) {
			node.clearChildren();// setChild(0, new
			String retval= LServlet.calcBase()+"C/"+"0"+ xRefUrl.hashCode() + ".css";
			// <LINK href="mystyle.css" title="compact" rel="stylesheet" type="text/css">
			String stringTmp = "<LINK href=\""+retval+ "\" title=\""+retval+"\" rel=\"stylesheet\" type=\"text/css\">";
			
			// HTMLNode(){})getChildren().clear()setValue("/*
			// 8-X */".toCharArray());
			final String scriptAliasTmp = (stringTmp);
			// replace Script-Content by link to cached value
			//ScriptItem siTmp = ssTmp.getByValue();
			node.setValue(scriptAliasTmp.toCharArray());
			//System.out.println("" + node.getTextValue());
			return retval;
			
		}
		@Override
		public void modi(HTMLNode node, String rootServletPar, String refPar, String refPar2) {
	        Attributes attrs = node.getAttributes();  
			String newValue = null; 	
			String value =  node.getTextValue() ;//CSStore.getInstanse().getByURL(refPar)
			if (verify(value)){
				String cacheKey = clearBody(node, value, refPar);//clearBody(node, scriptOnBody);

				CSStore ssTmp = CSStore.getInstanse();
				cc.co.llabor.cache.css.Item scriptTmp =  ssTmp .putOrCreate(cacheKey, value ,refPar);
				if (1==1) System.out.println(scriptTmp);
				
				String newLink = cacheKey;
				newValue = newLink;
				//node.setValue(  newLink.toCharArray() ); 
				if(!LServlet.TRACE)System.out.println("NEW VAL for CSS   : ["+value+"]=>"+newValue);
			}else{
				if(!LServlet.TRACE)System.out.println(" no changes for "+node.getName());
			}
	        
	        
		}		
	}  
  public static class NormalLinkVerifier extends TextVerifier implements ValueVerifier{
      
	      String start[]={"mailto", "javascript", "window", "history"};    
	      String exist[] ={"javascript", "#"} ;
	      String end[]={};	
	      
	    protected boolean verify(String link){ 
	          
	      return true;//!startOrEndOrExist(link, start, end, exist); 
	    } 
		@Override
		public void modi(HTMLNode node, String nodeName, String attrName, String newValue) {
	        Attributes attrs = node.getAttributes();  
			Attribute attr = attrs.get(attrName);		
			String value = attr.getValue();
			if(LServlet.TRACE)System.out.println("NEW VAL for "+nodeName+".."+attrName+" : ["+value+"]=>"+newValue + " := "+nodeName+"["+attrName+"]");
	        attr.setValue(  newValue);       
	        attrs.set(attr);
		}
	   
	  }
  
  public static class MetaLinkVerifier extends TextVerifier
			implements
				ValueVerifier {
		String start[] = {"mailto", "javascript", "window", "history"};
		String exist[] = {"javascript", "#"};
		String end[] = {};
		protected boolean verify(String link) {
			link = link.toLowerCase();
			return !startOrEndOrExist(link, start, end, exist);
		}
		@Override
		public void modi(HTMLNode node, String nodeName, String attrName,
				String newValue) {
			Attributes attrs = node.getAttributes();
			Attribute attr = attrs.get(attrName);
			String value = attr.getValue();
			if(LServlet.TRACE)System.out.println("NEW VAL for " + nodeName + ".." + attrName
					+ " : [" + value + "]=>" + newValue);
			attr.setValue(newValue);
			attrs.set(attr);
		}

	}
 
}
  
