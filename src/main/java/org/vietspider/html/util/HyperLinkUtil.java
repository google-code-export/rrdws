/***************************************************************************
 * Copyright 2004-2006 The VietSpider Studio All rights reserved.  *
 **************************************************************************/
package org.vietspider.html.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vietspider.chars.TextVerifier;
import org.vietspider.chars.URLUtils;
import org.vietspider.chars.ValueVerifier;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.Name;
import org.vietspider.html.NodeIterator;
import org.vietspider.html.parser.NodeImpl;
import org.vietspider.token.attribute.Attribute;
import org.vietspider.token.attribute.Attributes;

import ws.rrd.server.Base64Coder;

/** 
 * Author : Thuannd
 *         nhudinhthuan@yahoo.com
 * Apr 21, 2006
 */
public class HyperLinkUtil {   
  
  private URLUtils urlCreator;
  private SiteLinkVerifier siteLinkVerifier;
  private ImageLinkVerifier imageLinkVerifier;
  private NormalLinkVerifier normalLinkVerifier;
  private JavaScriptVerifier jsVerifier;
  
  private final static Map<String, String> linkAttributeMap = new HashMap<String, String>(4); 
  private final static Map<String, String> linkAttributeFullMap = new HashMap<String, String>(5);
  private final static Map<String, String> pageAttributeFullMap = new HashMap<String, String>(5);
  private final static Map<String, List<String>> scriptAttributeFullMap = new HashMap<String, List<String>>(5);
  
  public HyperLinkUtil() {
    urlCreator  = new URLUtils();
    siteLinkVerifier = new SiteLinkVerifier();
    imageLinkVerifier  = new ImageLinkVerifier();
    normalLinkVerifier = new NormalLinkVerifier();
    jsVerifier = new JavaScriptVerifier();

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
  
	  //Nach JavaScript (Netscape) erlaubt in folgenden HTML-Tags:
  private final static String tagList [] ={ "<body>","<frameset>","<input>","<layer>","<select>","<textarea>","<a>","<area>","<input>","<textarea>", 

  //Nach HTML 4.0 erlaubt in folgenden HTML-Tags:
   "<a>","<abbr>","<acronym>","<address>","<area>","<b>","<big>","<blockquote>","<body>","<button>","<caption>","<center>","<cite>","<code>","<col>","<colgroup>","<dd>","<del>","<dfn>","<dir>","<div>","<dl>","<dt>","<em>","<fieldset>","<form>","<h1>","<h2>","<h3>","<h4>","<h5>","<h6>","<hr>","<i>","<img>","<input>","<ins>","<kbd>","<label>","<legend>","<li>","<link>","<map>","<menu>","<noframes>","<noscript>","<object>","<ol>","<optgroup>","<option>","<p>","<pre>","<q>","<s>","<samp>","<select>","<small>","<span>","<strike>","<strong>","<sub>","<sup>","<table>","<tbody>","<td>","<textarea>","<tfoot>","<th>","<thead>","<tr>","<tt>","<u>","<ul>","<var>"
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
  		" javascript:"/* (bei Verweisen)*/
			};  
  //http://de.selfhtml.org/javascript/sprache/eventhandler.htm
  private static void initSA(){ 
		  for(String nextTag:tagList)
			  for (String nextEvent:eventList){
				  String rTag = nextTag.substring(1);
				  rTag = rTag.substring(0,rTag.length()-1);
				  String rEvent = nextEvent.substring(1);
				  List events = scriptAttributeFullMap.get(rTag);
				  events = events ==null?new ArrayList<String>(0):events;
				  scriptAttributeFullMap.put(rTag, events);
				  events.add(rEvent);
			  }
		  System.out.println(scriptAttributeFullMap);
  }
  
  public List<String> scanScriptLink(List<String> values, HTMLNode root) {
    IdentifierAttributeHandler handler = new IdentifierAttributeHandler(values, null,  "*", "onclick");
    handler.handle(root);
    
    List<String> list = handler.getValues();
    
    handler = new IdentifierAttributeHandler(list, jsVerifier, "a", "href");
    handler.handle(root);
    
    NodeIterator iterator = root.iterator();
    while(iterator.hasNext()) {
      HTMLNode n = iterator.next();
      if(!n.isNode(Name.SCRIPT)) continue;
      if(n.getChildren().size() < 1) continue;
      list.add(n.getChild(0).getTextValue());
    }
    return list;
  }
 
  @Deprecated()
  public List<String> scanScriptLink(List<NodeImpl> tokens) {
    IdentifierAttributeHandler handler = new IdentifierAttributeHandler(null, null,  "*", "onclick");
    for(int i = 0; i < tokens.size(); i++) {
      NodeImpl token = tokens.get(i);
      handler.handle(token);
    }
    
    List<String> list = handler.getValues();
    handler = new IdentifierAttributeHandler(list, jsVerifier, "a", "href");
    
    for(int i = 0; i < tokens.size(); i++) {
      NodeImpl token = tokens.get(i);
      handler.handle(token);
    }
    
    return list;
  }
  
  public synchronized List<String> scanSiteLink(HTMLNode node) {
    return scanSiteLink(null, node);
  }
  
  public synchronized List<String> scanSiteLink(List<String> values, HTMLNode node) {
    LinkAttributeHandler handler = new LinkAttributeHandler(values, siteLinkVerifier, linkAttributeMap);
    handler.handle(node);
    return handler.getValues();
  }
  
  public synchronized List<String> scanSiteLink(List<NodeImpl> tokens) {
    LinkAttributeHandler handler = new LinkAttributeHandler(null, siteLinkVerifier, linkAttributeMap);
//    MapAttributeHandler handler = new MapAttributeHandler(null, siteLinkVerifier, linkAttributeMap);
    for(int i = 0; i < tokens.size(); i++) {
      NodeImpl token = tokens.get(i);
      handler.handle(token);
    }
    return handler.getValues();
  }
  
  public synchronized List<String> scanHomepageLinks(HTMLNode node) {
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
  
  public String scanSingleImageLink(HTMLNode node) { 
    IdentifierAttributeHandler handler =
      new IdentifierAttributeHandler(null, imageLinkVerifier, "img", "src");
    return handler.getAttributeValue(node);
  }   
     
  public  synchronized void createFullNormalLink(HTMLNode node, String swapServletUrl2, URL home) {   
	    //createFullLink(node, linkAttributeFullMap,    swapServletUrl2, home, normalLinkVerifier);
	  	Map mTmp = new HashMap();
	  	
	  	mTmp.putAll(linkAttributeFullMap);
	  	mTmp.putAll(pageAttributeFullMap);
	  	 
	  	
	    createFullLink(node, mTmp,    swapServletUrl2, home, normalLinkVerifier);  
} 

  public  synchronized void createScriptLink(HTMLNode node, String swapServletUrl2, URL home) {
	  for(String nextTag:tagList)
		  for (String nextEvent:eventList){
			  String rTag = nextTag.substring(1);
			  rTag = rTag.substring(0,rTag.length()-1);
			  String rEvent = nextEvent.substring(1);
			  createFullLink(node, rTag, rEvent,   swapServletUrl2, home, normalLinkVerifier);//jsVerifier  
		  }
	   
} 

  
  public  synchronized void createMetaLink(HTMLNode node, String swapServletUrl2, URL home) {   
	    //createFullLink(node, linkAttributeFullMap,    swapServletUrl2, home, normalLinkVerifier);
	  	Map mTmp = new HashMap();
	  	
	  	mTmp.put("META", "CONTENT");
	  	
	    //createFullLink(node, mTmp,    swapServletUrl2, home, normalLinkVerifier);
	  	 if(node == null) return;
	     NodeIterator iterator = node.iterator();
	     while(iterator.hasNext()) {
	       HTMLNode n = iterator.next();
	       if(n.isTag()) createMetaLink(n, mTmp, swapServletUrl2, home, normalLinkVerifier); 
	     }	  	
}   
  
  
  
   
  public synchronized void createFullLink(HTMLNode node, 
      Map<String, String> map, String swapServletUrl2, URL home, ValueVerifier verifier) {
    if(node == null) return;
    NodeIterator iterator = node.iterator();
    while(iterator.hasNext()) {
      HTMLNode n = iterator.next();
      if(n.isTag()) createFullSingleLink(n, map, swapServletUrl2, home, verifier); 
    }
  }  
  
  private void createFullSingleLink(HTMLNode node, Map<String, String> map,
                                    String swapServletUrl2, URL home, ValueVerifier verifier)   {
    Set<String> keys = map.keySet();
    Iterator<String> iter = keys.iterator();
    String key =null;
    try{
	    while(iter.hasNext()){
	      key = iter.next();
	      if(node.isNode(key) || (key.length() == 1 && key.charAt(0) == '*')){
	        
	        
	        String aKey = map.get(key);
	        createFullSingleLink(node,key,aKey,swapServletUrl2,home,verifier);
 	      }
	    }
    }catch(java.lang.ClassCastException e){
    	Object aKey =   map.get(key);
    	//System.out.println("<"+key  + "attr =="+ aKey +" mess = "+e.getMessage()); //e.printStackTrace();
    	List<String> attrsTmp = (List<String>)aKey;
    	for (String nextAKey:attrsTmp ){
    		createFullSingleLink( node, key, nextAKey,swapServletUrl2,  home,  verifier);
    	}
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
				Attribute attr = attrs.get(map.get(key));
				if (attr == null)
					continue;
				String value = attr.getValue();
				if (value == null)
					continue;
				if (verifier != null && !verifier.verify(value))
					continue;
 
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
    
	if (( "script".equals(nodeName) ||  "noscript".equals(nodeName) )
			&& node.isNode(nodeName)){
		final String scriptValue = node.getTextValue();
		System.out.println(scriptValue);
		if (
				scriptValue .toLowerCase().indexOf("document.")>=0 ||
				scriptValue .toLowerCase().indexOf("eval")>=0 ||
				scriptValue .toLowerCase().indexOf("window.")>=0 
				
			){
			node.clearChildren();//setChild(0, new HTMLNode(){})getChildren().clear()setValue("/* 8-X */".toCharArray());
			final String scriptAliasTmp = ("SCRIPT SRC=\"/l/l"+scriptValue.hashCode()+".js\"");
			node.setValue(scriptAliasTmp.toCharArray());
			System.out.println(""+node.getTextValue());
		}
		System.out.println();
	}
	  
    if(node.isNode(nodeName) || (nodeName.length() == 1 && nodeName.charAt(0) == '*')) {
        Attributes attrs = node.getAttributes();  
		Attribute attr = attrs.get(attrName);
        if(attr == null)  return;
        String value = attr.getValue();
        if(value == null) return;
        if(verifier != null && !verifier.verify(value)) return;  
          String homeStr = ""+home;
          if ( value.startsWith("/")){ 
        	  String fileNameTmp =home.getFile() ;
        	  String sBaseTmp = (""+home);
			if("/".equals( fileNameTmp )) // assums home will be always with "/" at the end
        		  homeStr = sBaseTmp;
			else {
				int indexOfFileName = sBaseTmp.indexOf( fileNameTmp );
				if (indexOfFileName>0)
					homeStr = sBaseTmp.substring(0, indexOfFileName ) +"/";
				else
					homeStr = sBaseTmp +"/";
			}
        	value = (homeStr + value.substring(1,value.length())); // createFullSingleLink #2
          }else{
        	  homeStr = homeStr.substring(0,homeStr.lastIndexOf("/")+1); 
        	  if (value.indexOf(homeStr)== 0 ) value = value;
        	  else if (value.indexOf("http://")==0  ||value.indexOf("https://")==0  ) value = value;
        	  else value =  homeStr + value;  
          }
          value = encode(swapServletUrl2, value);
          
          System.out.println("NEW VAL for "+nodeName+".."+attrName+" : ["+attr.getValue()+"]=>"+value);
          attr.setValue(value);       
          attrs.set(attr);
        }
  }

	public static  String encode(String swapServletUrl2, String value) {
		value  = swapServletUrl2 + new String (Base64Coder.encode(  value.getBytes() ));
		return value;
	}
	
	public static String decode(String decodedUrl) {
		String urlStr;
		char[] charArray = decodedUrl.toCharArray();
		urlStr = new String(Base64Coder.decode(charArray));
		return urlStr;
	}
  
  public static class SiteLinkVerifier extends TextVerifier implements ValueVerifier{
    public boolean verify(String link){
      if(link == null) return false;
      link = link.toLowerCase();    
      String start[]={"mailto", "javascript", "window", "history", "#"};
      String end[]={"css", "js", "jpg", "png", "gif", "jpeg", "bmp", "dat", "exe", "txt", 
                    "java", "pdf", "doc", "xls", "rm", "ram", "wma", "wmv", "mp3", "swf", "zip", "jar", "rar"};
      String exist[] ={"img(\"", "image", ":sendim"} ;
      return !startOrEndOrExist(link, start, end, exist); 
    }
  }
  
  public static class ImageLinkVerifier extends TextVerifier implements ValueVerifier{
    public boolean verify(String link){
      link = link.toLowerCase();    
      String exist[] = {"img", "image"};
      String end[]={"jpg", "gif", "jpeg", "png", "ico", "svg", "bmp", "dib"};
      return existIn(link, exist) || endWith(link, end);
    }
  }
  
  public static class JavaScriptVerifier extends TextVerifier implements ValueVerifier{
    public boolean verify(String link){
      return link.toLowerCase().startsWith("javascript");    
    }
  }
  
  public static class NormalLinkVerifier extends TextVerifier implements ValueVerifier{
    public boolean verify(String link){
      link = link.toLowerCase();    
      String start[]={"mailto", "javascript", "window", "history"};    
      String exist[] ={"javascript", "#"} ;
      String end[]={};
      return !startOrEndOrExist(link, start, end, exist); 
    }
  }

  public ImageLinkVerifier getImageLinkVerifier() { return imageLinkVerifier; }
  
}

