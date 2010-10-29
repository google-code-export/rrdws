package ws.rrd.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.PrintWriter; 
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection; 
import java.nio.charset.Charset; 
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List; 
import java.util.Map; 
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
 
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity; 
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.vietspider.html.HTMLDocument;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.util.HyperLinkUtil;

import cc.co.llabor.cache.css.CSStore;
import cc.co.llabor.cache.js.Item;
import cc.co.llabor.cache.js.JSStore;
import cc.co.llabor.system.ExitTrappedException;

import com.no10x.cache.MemoryFileItem;
import com.no10x.cache.MemoryFileItemFactory;

import ws.rdd.net.UrlFetchTest;  

@SuppressWarnings("serial")
public class LServlet extends HttpServlet {

	public static final String _U_R_L_ = "_u_r_l_";
	
	public static final String SYSTEM_EXIT_RESETED = ExitTrappedException.isReseted()? "YES":"no";

	private static final String CHARSET_PREFIX = "charset=";

	// TODO: Create a config class to dynamic load settings from system.
	// propertiesin appengine-web.xml.
	//	public static String SwapServletUrl = "http://localhost:8080/swap/";		// dev.
	public static String SwapServletUrl = "local".equals(System
			.getProperty("myenviroment"))
			? "http://localhost:8888/l/"
			: "https://rrdsaas.appspot.com/l/"; // prod

	public static int SwaperConnTimeoutMS = 30000;
	public static int SwaperReadTimeoutMS = 30000;

	public static short FWSwaperAppVersion = 1;

	private static final Logger log = Logger.getLogger(LServlet.class
			.getName());

	public static final String BEAUTIFY = "BEAUTIFY";
	public static final String REPLACER = "REPLACER";
	public static final String TRACER = "TRACER";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.doGetPost(req, resp);
	}

	  private static HyperLinkUtil handler  = new HyperLinkUtil();

	public static boolean TRACE = true;
 

	  public static void testCreateFullLink(HTMLNode node, String swapServletUrl2, URL home){
	    handler.createFullNormalLink(node,  swapServletUrl2,  home);
	    List<String> list  = handler.scanSiteLink(node);
	    if (TRACE)
	    	for(String ele : list)
	    		System.out.println(ele);
	  }
		  
	  public static void testCreateScriptLink(HTMLNode node, String swapServletUrl2, URL home){
	    handler.createScriptLink( node,  swapServletUrl2,  home);
	    if (TRACE){
	    	//List<String> list  = handler.scanScriptLink(node );
	    	//for(String ele : list)
	    	//	System.out.println(ele);
	    }
	  }	  
	  public static void testCreateStyleLink(HTMLNode node, String swapServletUrl2, URL home){
	    handler.createStyleLink( node,  swapServletUrl2,  home);
	    if (TRACE){
	    	//List<String> list  = handler.scanScriptLink(node );
	    	//for(String ele : list)
	    	//	System.out.println(ele);
	    }
	  }
		  
	  public static void testCreateMetaLink(HTMLNode node, String swapServletUrl2, URL home){
	    handler.createMetaLink(node,  swapServletUrl2,  home);
	    if (TRACE){
		    List<String> list  = handler.scanSiteLink(node);
		    for(String ele : list)
		    	if (TRACE) System.out.println(ele);
	    }
	  }	  
 
	
	public void doGetPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		StringBuilder targetUrl = null;
 
		ServletOutputStream outTmp = null;
		String contextTypeStr = null ;
		byte[] dataBuf =null;
		HTMLDocument documentTmp = null;
		String urlStr = null;
		try {
			StringBuffer requestURL = req.getRequestURL();
			String rurlTmp = ""+req.getRequestURL()+""; 
			String baseURL =  System .getProperty("l.baseURL");
			String decodedUrl = rurlTmp;
			if (baseURL == null){
				SwapServletUrl  = rurlTmp.substring(0, rurlTmp.indexOf(req.getServletPath()+"/") )+req.getServletPath()+"/";
				decodedUrl = requestURL.substring( SwapServletUrl.length());
			}else{
				SwapServletUrl  = baseURL;
				decodedUrl = requestURL.substring( requestURL.lastIndexOf("/l/")+3 );
				
			}
				
			 
			
			try{
				if (TRACE){System.out.println("DECODE :"+decodedUrl);}
				urlStr = HyperLinkUtil.decode(decodedUrl);
			}catch(Throwable e){
				if (TRACE){e.printStackTrace();}
				outTmp = resp.getOutputStream();
				PrintWriter pw = new PrintWriter(outTmp, true);
				pw.println( requestURL);
				pw.println( contextTypeStr);
				pw.println( SwapServletUrl);
				pw.println( decodedUrl);
								
				e.printStackTrace(pw);
			}
			// fix ROOT-Panel-Request
			if (isRootReq(req) ){ 
				urlStr = req.getParameter(_U_R_L_) ; 
			}
			
			// normalize non-protocol-ADDRESS
			urlStr = (""+urlStr ).startsWith("http")? urlStr:"http://"+urlStr;			
			if (TRACE) System.out.println(_U_R_L_ + " := "+ urlStr);
			targetUrl = new StringBuilder(urlStr);

			if ((targetUrl.length() > 0) && (req.getQueryString() != null)
					&& (req.getQueryString().length() > 1)) {
				if (targetUrl.toString().endsWith("?"))
					targetUrl.append(String.format("%s", req.getQueryString()));
				else
					targetUrl.append(String.format("?%s", req.getQueryString()));
				
				urlStr = targetUrl.toString();
			}
			
			String[][] headsToResend = calcRequestHeaders(req);
			if (!urlStr.startsWith("http")) {
				// fix via REFeRER
				try{
				 URL refURL = new URL ( HyperLinkUtil.decode( req.getHeader("Referer").substring(SwapServletUrl.length()) ) );
				 urlStr = refURL.getProtocol() + "://"+refURL.getHost() + "/"+(urlStr.startsWith("/")?"":refURL.getPath()+"/../")+urlStr;
				}catch(Throwable e){}
			}
			urlStr  = urlStr.replace(" ", "%20").replace("\t", "%090");
			// http://it-ru.de/forum/viewtopic.php?t=182374&amp;postdays=0&amp;postorder=asc&amp;start=15
			urlStr  = urlStr.replace("&amp;", "&");
			HttpSession sessionTmp = req.getSession();
			
			UrlFetchTest urlFetcherTmp = (UrlFetchTest) sessionTmp.getAttribute("UrlFetcher");
			if (urlFetcherTmp == null){
				urlFetcherTmp = new UrlFetchTest(sessionTmp);
				sessionTmp.setAttribute("UrlFetcher",urlFetcherTmp);
			}
			
			
			HttpResponse xRespTmp = null ;

			if ("POST".equals( req.getMethod() ) && ! isRootReq(req) ){
				List<MemoryFileItem> items  = null;
			    if(org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent(req)){
		            
		            MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
		            org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
		            upload.setSizeMax(4*1024*1024); // 4 MB
		  
		            // Parse the request
		            items = upload.parseRequest(req);
			    }				
				Map parameterMap = req.getParameterMap();
				xRespTmp = urlFetcherTmp.fetchPostResp(urlStr, headsToResend,	parameterMap, items);
				// HOTFIX for Login-redirect
				if (xRespTmp.getLastHeader("X-MOVED")!=null){
					urlStr = ""+xRespTmp.getLastHeader("X-MOVED").getValue();
				}
			}				
			else{ // GET
				urlStr = checkGOTO(urlStr);
				
				
				xRespTmp = urlFetcherTmp.fetchGetResp(urlStr, headsToResend);
			}
			final StatusLine statusLine = xRespTmp.getStatusLine();
			try{
				if (statusLine.getStatusCode() == 401){
					resp.setStatus(401);
					resp.setHeader( "WWW-Authenticate", xRespTmp.getHeaders("WWW-Authenticate")[0].getValue());
					return;
				}else if (statusLine.getStatusCode() == 301){
					resp.setStatus(301);
					resp.setHeader( "WWW-Authenticate", xRespTmp.getHeaders("WWW-Authenticate")[0].getValue());
					return;
				}else if (statusLine.getStatusCode() == 302){
					resp.setStatus(302);//xRespTmp.getAllHeaders()
					resp.setHeader(  "Location", requestURL.toString()  );
					return;
				}else if (statusLine.getStatusCode() == 303){
					resp.setStatus(303);
					resp.setHeader( "WWW-Authenticate", xRespTmp.getHeaders("WWW-Authenticate")[0].getValue());
					return;
				}else if (statusLine.getStatusCode() == 304){
					resp.setStatus(304);
					resp.setHeader( "WWW-Authenticate", xRespTmp.getHeaders("WWW-Authenticate")[0].getValue());
					return;
				}else if (statusLine.getStatusCode() == 305){
					resp.setStatus(305);
					resp.setHeader( "WWW-Authenticate", xRespTmp.getHeaders("WWW-Authenticate")[0].getValue());
					return;
				}
			}catch(Exception e){
				if (TRACE)e.printStackTrace();
			}
			HttpEntity entity = xRespTmp.getEntity();
			contextTypeStr = ""+entity.getContentType();
			String contextEncStr =  ""+entity.getContentEncoding() ;
			contextEncStr = "null".equals(contextEncStr)?getXEnc(xRespTmp):contextEncStr;
			if ("null" .equals( ""+contextEncStr ) &&  contextTypeStr.toLowerCase().startsWith("content-type: text/html")){
				int encPos = contextTypeStr.toLowerCase().indexOf(CHARSET_PREFIX);
				if (encPos>0){
					
					contextEncStr = contextTypeStr.substring(encPos+CHARSET_PREFIX.length());
					contextEncStr = contextEncStr .toUpperCase();
					if (TRACE) log.warning(contextEncStr + "  }} ENC :=  {  "+contextTypeStr+" } ::: enc ::: "+contextEncStr +"["+urlStr+"]");

				}else{
					
					Header[] contextEncHeaders = xRespTmp.getHeaders("Content-Encoding");
					try{
						contextEncStr =  contextEncHeaders[0].getValue();
					}catch(Throwable e){}
					if (TRACE) log.warning("Content-Encoding[0]::== {"+ contextEncStr + "  }" );
					 
				}
			}else{
				if (TRACE) log.warning("nonull::::"+ contextEncStr + " ::::: "+ contextTypeStr );
			}
			
			if ( isCSS(contextTypeStr)  )
			{
				outTmp = performCSS(resp, contextTypeStr, urlStr, xRespTmp, entity, contextEncStr);
				return;
			}else if ( isBinary(contextTypeStr) ){
				outTmp = performBinary(resp, contextTypeStr, urlStr, xRespTmp, entity, contextEncStr);
				return;
			}else if (  isScript(contextTypeStr) ){
				outTmp = performScript(resp, contextTypeStr, urlStr, entity, contextEncStr);
				return;
				
			}	else{
			 
				String xEncTmp = getXEnc(xRespTmp); 
				if (TRACE) log.warning("x---HTML--- x  contextTypeStr/contextEncStr:"+contextTypeStr+" : :  enc : : "+contextEncStr +"["+urlStr+"]   XXX::"+xEncTmp);
				if (TRACE) System.out.println("=====!!!======"+contextTypeStr +"::::"+contextEncStr);
			}
			 
			ByteArrayOutputStream oaos = new ByteArrayOutputStream();
			entity.writeTo(oaos) ;
			if ("gzip".equals(contextEncStr)  || isGZip(xRespTmp) ){
				oaos = deZip(oaos); 
		        
				int beginIndex = 0;
				String charSetHeader = req.getHeader("Accept-Charset ");
				int endIndex = charSetHeader.indexOf(",");
				contextEncStr  = charSetHeader.substring(beginIndex , endIndex);//"ISO-8859-1";
			}
			String data = null;
			try{
				data = oaos.toString(contextEncStr);//xCSS.toUpperCase().substring( 12430)
			}catch(Exception e){
				data = oaos.toString();
			} //data.substring( data.indexOf("&lt;") -100, data.indexOf("&lt;") +20);
			 
			if ("null".equals(  ""+contextEncStr ) &&  data.toLowerCase().indexOf("content=\"text/html")>0)try{
				String contextText = "charset=";
				int lenTmp = contextText.length();
				int posTmp = data.toLowerCase().indexOf(contextText);
				int beginIndex = posTmp +lenTmp;
				int endIndex = beginIndex + data.toLowerCase().substring(beginIndex).indexOf("\"");
				contextEncStr = data.toLowerCase().substring(beginIndex,endIndex );
				contextEncStr = contextEncStr.toUpperCase();
				data = oaos.toString(contextEncStr);
			}catch(Throwable e){}
			try{
				dataBuf = data.trim().getBytes(contextEncStr);// "utf-8"
 			} catch(Exception e) {
 				contextEncStr = null;
 				dataBuf = data.trim().getBytes();// "ISO-8859-1"
			}
			HTMLParser2 parser2 = new HTMLParser2();
			try{
				documentTmp = parser2.createDocument(dataBuf, contextEncStr );// "utf-8"
			}catch(Exception e){
				if (TRACE) log.warning("createDocument EXCEPTION!" +e.getMessage()+" contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
				documentTmp = parser2.createDocument(dataBuf, null );// "utf-8"
			}
			 
	    	URL realURL = new URL(urlStr); // new String( oaos.toString(contextEncStr).getBytes(), contextEncStr)
	    	 
	    	HTMLNode rootTmp = documentTmp.getRoot();
	    	if ("true".equals(  sessionTmp.getAttribute(BEAUTIFY) )){
	    		rootTmp.setBeautify(true);
	    	}else{
	    		rootTmp.setBeautify(false);
	    	}
	    	
			testCreateFullLink(rootTmp, SwapServletUrl, realURL);
//	    	testCreateImageLink(documentTmp.getRoot(), SwapServletUrl, realURL);
	    	
	    	testCreateMetaLink(rootTmp, SwapServletUrl, realURL);
	    	
	    	testCreateScriptLink(rootTmp, SwapServletUrl, realURL);	    	
	    	testCreateStyleLink(rootTmp, SwapServletUrl, realURL);	    	
	    	
	    	int beginIndex = contextTypeStr.toUpperCase().indexOf(" ")+1;

	    	setupResponseProperty( resp,  xRespTmp);
	    	resp.setContentType(contextTypeStr.substring(beginIndex));
	    	if (!"null".equals(""+contextEncStr)){
	    		resp.setCharacterEncoding(contextEncStr);
	    	}
	    	
	    	outTmp = resp.getOutputStream();
	    	
	    	String textValue = null;
	    	
	    	try{ // TODO see hg hi 
	    		HTMLNode bodyTmp = documentTmp.getRoot().getChild(1);
				String strTmp = "<body><div  name=toolbar>"+ new String(getResourceAsBA("L.jspX") ) +"</div></body>";
				strTmp = strTmp.replace(
						"B8b8B8Bbbb888B", 
						calcBase() 
						); 				
				HTMLDocument htmlTmp = parser2.createDocument(strTmp);
				HTMLNode myIFrame = htmlTmp.getRoot().getChild(1).getChild(0);
				bodyTmp.addChild(myIFrame);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		include(resp, "L.jspX");
	    	}
	    	//new String(documentTmp.getTextValue().getBytes("ISO-8859-1"), contextEncStr);// "windows-1251" textValue.toUpperCase().substring( 12430)

	    	if ("KOI8-R".equals(contextEncStr)) {
	    		textValue = documentTmp.getTextValue(); 
	    	}else{
	    		HTMLNode doctype = documentTmp.getDoctype();
	    		String sDoctype = (doctype==null?"":doctype.getTextValue());
	    		textValue = sDoctype  + documentTmp.getRoot().getTextValue();
	    		 
	    	} 
	    	if (!"null".equals(""+contextEncStr)){
	    		outTmp.write(textValue.getBytes(contextEncStr)); 
	    	}else{
	    		outTmp.write(textValue.getBytes()); 
	    	}
			//outTmp.write(string2.getBytes(contextEncStr));
			//outTmp.flush();
		} catch (java.lang.NoClassDefFoundError e) {
			if (TRACE) System.out.println(contextTypeStr +" ===============  "+e.getMessage());e.printStackTrace();
			if (TRACE) System.out.println(documentTmp);
		} catch (Exception e) {
			
			
			if (!"".equals(""+targetUrl  ) && targetUrl != null){
				ExceptionUtils.swapFailedException(targetUrl.toString(), resp,
						e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				outTmp = resp.getOutputStream();
				e.printStackTrace(new PrintWriter(outTmp, true));
				 
			}
			else
				outTmp = resp.getOutputStream();
			
				InputStream in = this.getClass().getClassLoader().getResourceAsStream("index.html");
				byte buf[] = new byte[in.available()];
				String magik = "l11010101010000101010100101lIll1l0O0l10ll1001l1l01ll001/";
				int readRetVal = in.read(buf);
				String toBrowser = new String (buf,0, readRetVal );
				toBrowser = toBrowser.replace(magik,SwapServletUrl );// SwapServletUrl
				toBrowser = toBrowser.replace("B8b8B8Bbbb888B", SwapServletUrl.subSequence(0, SwapServletUrl.length()-2) );//				
				outTmp.write(toBrowser.getBytes());
				
				outTmp.write("<pre>".getBytes());
				outTmp.write((""+e.getMessage()+"\n\n\n\n"+e.getStackTrace()).getBytes());
				e.printStackTrace(new PrintWriter(outTmp, true));
				//outTmp.flush();
				//ExceptionUtils.swapFailedException(resp, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}  
	}

	/**
	 * @author vipup
	 * @param urlPar
	 * @return
	 * @throws MalformedURLException
	 */
	private static final String checkGOTO(String urlPar)
			throws MalformedURLException {
		// f.ex.
		// https://www.ccc.de/Wxby7/Lswdn.ipx?goto=../Mdfsus/Rsdfrts.usbx
		String GOTO = "?goto=";
		int gotoPos = urlPar.indexOf(GOTO);
		if (gotoPos > 0) {
			String prefixTmp = urlPar.substring(0, gotoPos);
			URL urlTmp = new URL(prefixTmp);
			String suffTmp = urlPar.substring(gotoPos + GOTO.length());
			urlPar = HyperLinkUtil.prepareLinkValue(urlTmp, suffTmp);
		}
		return urlPar;
	}

	static ServletOutputStream performCSS(HttpServletResponse resp,
			String contextTypeStr, String urlStr, HttpResponse xRespTmp,
			HttpEntity entity, String contextEncPar) throws IOException {
		ServletOutputStream outTmp;
		
		
		
		if (TRACE) log.warning("CSS contextTypeStr / contextEncStr:{"+contextTypeStr+" / "+contextEncPar +"}, url== ["+urlStr+"]");
		CSStore store = CSStore.getInstanse();
		cc.co.llabor.cache.css.Item itemTmp =  store.getByURL(urlStr);
		String xCSS = null;
		if (itemTmp == null){
			
			ByteArrayOutputStream oaos = new ByteArrayOutputStream();
			entity.writeTo(oaos) ;
			if (isGZip(xRespTmp)){
				oaos = deZip(oaos);
			    //contextEncStr  = "ISO-8859-1";
			}				
			String lBAK_GIF = "URL (/l.gif?";
			String FSservletURL = "url ( hTtP://"+SwapServletUrl.replace("/l/","/F/h_t_t_p_://");
			String FSSservletURL = "url  ( hTtPs://"+SwapServletUrl.replace("/l/","/F/h_t_t_p_s_://");
			xCSS = oaos.toString()
			// ROOT OF SERVER
			.replace("url(/", lBAK_GIF)
			.replace("url (/", lBAK_GIF)
			.replace("URL(/", lBAK_GIF)
			.replace("Url(/", lBAK_GIF)
			.replace("url ( /", lBAK_GIF)
			.replace(lBAK_GIF, FSservletURL ) //.replace(lBAK_GIF, "url(/l.gif?")
			// ABSOLUTE-ref like: 
			// url(http://maps.gstatic.com
			.replace("url(http://", FSservletURL)
			.replace("url(https://", FSSservletURL)
			// 	rel-ref from './'	
			.replace("url(", "url  (    "+SwapServletUrl.replace("/l/",undescoredProtocol(urlStr))+stripFileName(  stripProtocol(urlStr)))
			; 
			xCSS = xCSS.replace(" url  (    http", "url(http");
			String refPar = urlStr;
			try{
				xRespTmp.getHeaders("Refferer")[0].getValue();
			}catch(Throwable e){}
			store.putOrCreate(urlStr, xCSS, refPar );
		}else{
			xCSS = itemTmp.getValue();
		}
		
		resp.setContentType("text/css");
		outTmp = resp.getOutputStream();
		outTmp.write(xCSS.getBytes());
		outTmp.flush();
		//store.putOrCreate(urlStr, xCSS, urlStr);
		return outTmp;
	}

	private static String undescoredProtocol(String urlStr) {
		return urlStr.startsWith("https://")? "/F/h_t_t_p_s_://":"/F/h_t_t_p_://";
	}
	private static String stripProtocol(String urlStr) {
		return urlStr.startsWith("https://")? urlStr.substring("https://".length()):urlStr.substring("http://".length());
	}
	private static String stripFileName(String urlStr) {
		return urlStr.endsWith("/")? urlStr :urlStr.substring(0, urlStr.lastIndexOf("/"))+"/";
	}

	static ServletOutputStream performBinary(HttpServletResponse resp,
			String contextTypeStr, String urlStr, HttpResponse xRespTmp,
			HttpEntity entity, String contextEncPar) throws IOException {
		ServletOutputStream outTmp;
		if (! "null".equals( contextTypeStr )){
			String contypeTmp = contextTypeStr.substring("Content-Type:".length());
			resp.setContentType(contypeTmp);
			setupResponseProperty( resp,  xRespTmp);
		}
		//log.warning("HTML contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
		outTmp = resp.getOutputStream();
		entity.writeTo(outTmp) ;
		outTmp.flush();
		outTmp.close();
		return outTmp;
	}

	static ServletOutputStream performScript(HttpServletResponse resp,
			String contextTypeStr, String urlStr, HttpEntity entity,
			String contextEncPar) throws IOException {
		ServletOutputStream outTmp;
		if (TRACE) log.warning("JS contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncPar+"]  URL =:["+urlStr+"]");
		
		JSStore ssTmp = JSStore.getInstanse();
		Item scriptTmp = ssTmp.getByURL( urlStr);
		if (scriptTmp == null){
			ByteArrayOutputStream oaos = new ByteArrayOutputStream();
			entity.writeTo(oaos) ;				
			String jsToWrap = oaos.toString("UTF-8");
			// 						(new Element('li', { 'class': 'fav', 'html': ((empty && i==0) ? '' : ', ') 
			//+ '<a href="hTTp://rrdsaas.appspot.com/F/h_t_t_p_://rrdsaas.appspot.com/l//HtTp/' + temp.user.login + '.' + temp.base_short + '/favorites/tag/' + tag + '">' + tag + '</a>'}
			String FServletURL = SwapServletUrl.replace("/l/", "/F/");
			jsToWrap = JSStore.performFormatJS(urlStr, jsToWrap ) ;
			jsToWrap = 
				jsToWrap
					.replace("http://",   FServletURL +  "h_t_t_p_://" )
					.replace("HTTP://",   FServletURL +  "h_t_t_p_://" )
					.replace("HTTPS://",   FServletURL +  "h_t_t_p_s_://" )
					.replace("https://",   FServletURL +  "h_t_t_p_s_://" )
				;
			
			scriptTmp = ssTmp.putOrCreate(urlStr, jsToWrap, urlStr);
		}  
		resp.setContentType("application/javascript; charset=utf-8");
		outTmp = resp.getOutputStream();
		String scriptValueTmp = scriptTmp.getValue();
		outTmp.write(scriptValueTmp.getBytes("UTF-8")) ;
		outTmp.flush();
		return outTmp;
	}

	static boolean isCSS(String contextTypeStr) {
		return "Content-Type: text/css".equalsIgnoreCase( contextTypeStr)||
		(""+contextTypeStr).toLowerCase().indexOf( "text/css")>0;
	}

	static boolean isBinary(String contextTypeStr) {
		return "null".equalsIgnoreCase( contextTypeStr)||
		"Content-Type: image/jpeg".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: image/png".equalsIgnoreCase( contextTypeStr) ||	
		"Content-Type: image/x-icon".equalsIgnoreCase( contextTypeStr) ||	
		"Content-Type: text/xml".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: image/gif".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/pdf".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-shockwave-flash".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/postscript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/octet-stream".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-msexcel".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: image/tiff".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: image/ief".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: image/g3fax".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-shockwave-flash".equalsIgnoreCase( contextTypeStr);
	}

	static boolean isScript(String contextTypeStr) {
		return
		
		"Content-Type: text/javascript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/javascript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-javascript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/javascript; charset=utf-8".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-javascript; charset=utf-8".equalsIgnoreCase( contextTypeStr) ||
		//"content-type: text/html; charset=ISO8859-1".equalsIgnoreCase( contextTypeStr) ||
		"content-type: text/javascript; charset=UTF-8".equalsIgnoreCase( contextTypeStr) || 
		(""+contextTypeStr).toLowerCase().indexOf("text/javascript")>=0 ||
		(""+contextTypeStr).toLowerCase().indexOf("application/javascript")>=0 ||
		(""+contextTypeStr).toLowerCase().indexOf("application/x-javascript")>=0 
		
		;
	}

	private static boolean isRootReq(HttpServletRequest req) {
		return  req.getParameter(_U_R_L_) != null;
	}



	private void include(HttpServletResponse resp, String resourceName) {
		try {
			ServletOutputStream out;
			out = resp.getOutputStream();			
			byte[] b = getResourceAsBA(resourceName);
			String newVal = new String(b);
			newVal  = newVal .replace( 
					"l11010101010000101010100101lIll1l0O0l10ll1001l1l01ll001",
					SwapServletUrl
					);	
			newVal = newVal.replace(
					"B8b8B8Bbbb888B", 
					calcBase() 
					); 
			b = newVal.getBytes();
			out.write(b );
		} catch (IOException e) { 
			e.printStackTrace();
		}  	
	}

	public static final String calcBase(){
		return SwapServletUrl.substring(0, SwapServletUrl.length()-2);
	}
	
	public static byte[] getResourceAsBA(String namePar) throws IOException {
		InputStream in = LServlet.class.getClassLoader().getResourceAsStream(namePar); 
		byte[] b = new byte[in.available()] ;
		in.read(b);
		return b;
	}
	
	

	private static String getXEnc(HttpResponse respTmp) {
		 String retval = null;
		 try{
			 retval = respTmp.getHeaders("Content-Encoding")[0].getValue();
		 }catch (Exception e) {
			 try{
				 String retvalTmp = respTmp.getHeaders("Content-Type")[0].getValue();
				 // for ex. [Content-Type: text/html; charset=windows-1251]
				 int beginIndex =  retvalTmp.toLowerCase().indexOf("charset=")+"charset=".length();
 
				 retval  = ""+Charset.availableCharsets().get(retvalTmp.substring(beginIndex).toUpperCase()).displayName() ;
			 }catch (Exception e2) {
				// TODO: handle exception
			}
		}
		 return retval;
	}

	private static boolean isGZip(HttpResponse xRespTmp) {
		boolean retval = false;
		try{
			retval = "gzip".equals(xRespTmp.getHeaders("Content-Encoding")[0].getValue());
		}catch(Throwable e){}
		return  retval;
	}

	private static ByteArrayOutputStream deZip(ByteArrayOutputStream oaos)
			throws IOException {
		ByteArrayInputStream gzippeddata = new ByteArrayInputStream(oaos.toByteArray());
		GZIPInputStream zipin = new GZIPInputStream(gzippeddata);
		byte[] buf = new byte[1024];  //size can be  
		int len;
		oaos = new ByteArrayOutputStream();
		while ((len = zipin.read(buf)) > 0) {
			oaos.write(buf, 0, len);
		}
		return oaos;
	}

	@SuppressWarnings("unchecked")
	protected static void setupSwaperConnProperty(HttpURLConnection swaperConn,
			HttpServletRequest req) throws ProtocolException {

		swaperConn.setConnectTimeout(SwaperConnTimeoutMS);
		swaperConn.setReadTimeout(SwaperReadTimeoutMS);

		// TODO: PoC: "java.io.IOException: http method POST against".
		swaperConn.setRequestMethod(req.getMethod());

		// http redirecting cookies (response code 3xx).
		swaperConn.setInstanceFollowRedirects(true);
		swaperConn.setUseCaches(false);

		Enumeration<String> e = (Enumeration<String>) req.getHeaderNames();
		String name = null;

		while (e.hasMoreElements()) {
			name = e.nextElement();
			swaperConn.setRequestProperty(name, req.getHeader(name));
		}
	}

	// TODO: Fix "Cookie rejected" warnning. domain must start with a dot.
	// WARNING: Cookie rejected:
	// "$Version=0; _javaeye3_session_=BAh7BiIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNoSGFzaHsABjoKQHVzZWR7AA%3D%3D--d983012383f33595e8b4015c6235ad6e21fa81cf; $Path=/; $Domain=javaeye.com".
	// Domain attribute "javaeye.com" violates RFC 2109: domain must start with a dot

	static final String headersToSet []= {
//			"Content-Type",
			"Content-Language",
//			"Content-Encoding",
			"Content-Disposition",// : attachment; filename=Personalakte.pdf
			"Date",
			"Last-Modified" ,
			"Accept",
			"Accept-Charset",
			"Accept-Language",
//			"Accept-Encoding",
			"Referer", 
//			"Cookie",
			"Cache-Control",
			"User-Agent",
			"Cookie2",
//			"X-Powered-By", //: ASP.NET
//			"X-AspNet-Version",//: 2.0.50727
			"Expires",
			"TE",
			"Server",
//			"Set-Cookie",
			"Keep-Alive",
			"Authorization"
			
	};	
	/**
	 * copy headers FROM:bPar TO: aPar
	 * 
	 * @author vipup
	 * @param aPar
	 * @param bPar
	 * @throws IOException
	 */
	protected static void setupResponseProperty(HttpServletResponse aPar,
			HttpResponse bPar) throws IOException {
		for (String headerName :headersToSet)
		for (Header next: bPar.getHeaders(headerName) )
			aPar.setHeader(next.getName(), next.getValue()); 
		
	}
	
	protected  static String[][] calcRequestHeaders(
			HttpServletRequest req) {
		Map<String, String> headersTmp = calcRequestHeadersAsMap(req, headersToSet);
		
		String [][]retval = new String[headersTmp.size()][2];
		int i=0;
		for(String  nextKey:headersTmp.keySet()){
			retval[i][0] = nextKey;
			retval[i][1] = headersTmp.get(nextKey);
			i++;
		}
		return   retval;
	}

	private static Map<String, String> calcRequestHeadersAsMap(HttpServletRequest req, String[] headersToSetPar) {
		Map<String, String> headersTmp = new HashMap<String, String>();
		for (String headerName :headersToSetPar){
			String nextVal =  req.getHeader(headerName);
			if ("Referer".equals(headerName) ){
				if ((""+nextVal).startsWith(SwapServletUrl)){
					nextVal   = HyperLinkUtil.decode(nextVal.substring(SwapServletUrl.length()));
				}
			}
			
			if (nextVal != null)
			headersTmp.put( headerName  , nextVal );
		}
		return headersTmp;
	}
 
}
