package ws.rrd.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.PrintWriter; 
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection; 
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
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
import org.vietspider.html.HTMLDocument;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.util.HyperLinkUtil;

import cc.co.llabor.system.ExitTrappedException;

import com.no10x.cache.MemoryFileItem;
import com.no10x.cache.MemoryFileItemFactory;

import ws.rdd.net.UrlFetchTest;  
import ws.rrd.mem.ScriptItem;
import ws.rrd.mem.ScriptStore;

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

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.doGetPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.doGetPost(req, resp);
	}

	  private static HyperLinkUtil handler  = new HyperLinkUtil();

	public static boolean TRACE = false;
 

	  public static void testCreateFullLink(HTMLNode node, String swapServletUrl2, URL home){
		    handler.createFullNormalLink(node,  swapServletUrl2,  home);
		    List<String> list  = handler.scanSiteLink(node);
		    for(String ele : list)
		    	if (TRACE) System.out.println(ele);
		  }
		  
	  public static void testCreateScriptLink(HTMLNode node, String swapServletUrl2, URL home){
		    handler.createScriptLink( node,  swapServletUrl2,  home);
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
			SwapServletUrl  = rurlTmp.substring(0, rurlTmp.indexOf(req.getServletPath()+"/") )+req.getServletPath()+"/";
			String decodedUrl = requestURL.substring( SwapServletUrl.length());
			
			try{
				urlStr = HyperLinkUtil.decode(decodedUrl);
			}catch(Throwable e){
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
				xRespTmp = urlFetcherTmp.fetchResp(urlStr, headsToResend,	parameterMap, items);
			}				
			else{
				xRespTmp = urlFetcherTmp.fetchGetResp(urlStr, headsToResend);
			}
			if (xRespTmp.getStatusLine().getStatusCode() == 401){
				resp.setStatus(401);
				resp.setHeader( "WWW-Authenticate", xRespTmp.getHeaders("WWW-Authenticate")[0].getValue());
				return;
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
				if (TRACE) log.warning("x---HTML---x  contextTypeStr/contextEncStr:"+contextTypeStr+" : :  enc : : "+contextEncStr +"["+urlStr+"]   XXX::"+xEncTmp);
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
			testCreateFullLink(rootTmp, SwapServletUrl, realURL);
//	    	testCreateImageLink(documentTmp.getRoot(), SwapServletUrl, realURL);
	    	
	    	testCreateMetaLink(rootTmp, SwapServletUrl, realURL);
	    	
	    	testCreateScriptLink(rootTmp, SwapServletUrl, realURL);	    	
	    	
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
	    		textValue = documentTmp.getTextValue();//
	    	}else{
	    		textValue = documentTmp.getTextValue();//
	    	}
	    	//PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	    	String string1 = "<!DOCTYPE html><html>\n<!-- contextEncStr="+contextEncStr+" -->\n";
	    	string1 += "";
	    	String string2 = "</html>";
			//outTmp.write(string1.getBytes(contextEncStr));
	    	if (!"null".equals(""+contextEncStr)){
	    		outTmp.write((string1 + textValue + string2).getBytes(contextEncStr));//)
	    	}else{
	    		outTmp.write((string1 + textValue + string2).getBytes());//)
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
				String toBrowser = new String (buf);
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

	private ServletOutputStream performCSS(HttpServletResponse resp,
			String contextTypeStr, String urlStr, HttpResponse xRespTmp,
			HttpEntity entity, String contextEncStr) throws IOException {
		ServletOutputStream outTmp;
		if (TRACE) log.warning("CSS contextTypeStr / contextEncStr:{"+contextTypeStr+" / "+contextEncStr +"}, url== ["+urlStr+"]");
		ByteArrayOutputStream oaos = new ByteArrayOutputStream();
		entity.writeTo(oaos) ;
		if (isGZip(xRespTmp)){
			oaos = deZip(oaos);
		    //contextEncStr  = "ISO-8859-1";
		}				
		String xCSS = oaos.toString().replace("url(/", "URL (/l.gif?")
		.replace("url (/", "URL (/l.gif?")
		.replace("URL(/", "URL (/l.gif?")
		.replace("Url(/", "URL (/l.gif?")
		.replace("url ( /", "URL (/l.gif?")
		
		.replace("url(", "url(  "+SwapServletUrl.replace("/l/",undescoredProtocol(urlStr))+stripFileName(  stripProtocol(urlStr)))
		
		.replace("URL (/l.gif?", "url(/l.gif?")
		// url(http://maps.gstatic.com
		.replace("url(http://", "url(hTtP://"+SwapServletUrl.replace("/l/","/F/h_t_t_p_://"))
		.replace("url(http://", "url(hTtP://"+SwapServletUrl.replace("/l/","/F/h_t_t_p_://"))
		.replace("url(https://", "url(hTtPs://"+SwapServletUrl.replace("/l/","/F/h_t_t_p_s_://"))
		
		;
		resp.setContentType("text/css");
		outTmp = resp.getOutputStream();
		outTmp.write(xCSS.getBytes());
		outTmp.flush();
		return outTmp;
	}

	private String undescoredProtocol(String urlStr) {
		return urlStr.startsWith("https://")? "/F/h_t_t_p_s_://":"/F/h_t_t_p_://";
	}
	private String stripProtocol(String urlStr) {
		return urlStr.startsWith("https://")? urlStr.substring("https://".length()):urlStr.substring("http://".length());
	}
	private String stripFileName(String urlStr) {
		return urlStr.endsWith("/")? urlStr :urlStr.substring(0, urlStr.lastIndexOf("/"))+"/";
	}

	private ServletOutputStream performBinary(HttpServletResponse resp,
			String contextTypeStr, String urlStr, HttpResponse xRespTmp,
			HttpEntity entity, String contextEncStr) throws IOException {
		ServletOutputStream outTmp;
		if (! "null".equals( contextTypeStr )){
			String contypeTmp = contextTypeStr.substring("Content-Type:".length());
			resp.setContentType(contypeTmp);
			//setupResponseProperty( resp,  xRespTmp);
		}
		//log.warning("HTML contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
		outTmp = resp.getOutputStream();
		entity.writeTo(outTmp) ;
		outTmp.flush();
		outTmp.close();
		return outTmp;
	}

	private ServletOutputStream performScript(HttpServletResponse resp,
			String contextTypeStr, String urlStr, HttpEntity entity,
			String contextEncStr) throws IOException {
		ServletOutputStream outTmp;
		if (TRACE) log.warning("JS contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
		
		ScriptStore ssTmp = ScriptStore.getInstanse();
		ScriptItem scriptTmp = ssTmp.getByURL( urlStr);
		if (scriptTmp == null){
			ByteArrayOutputStream oaos = new ByteArrayOutputStream();
			entity.writeTo(oaos) ;				
			String jsToWrap = oaos.toString("UTF-8");
			// 						(new Element('li', { 'class': 'fav', 'html': ((empty && i==0) ? '' : ', ') 
			//+ '<a href="hTTp://rrdsaas.appspot.com/F/h_t_t_p_://rrdsaas.appspot.com/l//HtTp/' + temp.user.login + '.' + temp.base_short + '/favorites/tag/' + tag + '">' + tag + '</a>'}
			String FServletURL = SwapServletUrl.replace("/l/", "/F/");
			jsToWrap = SServlet.performFormatJS(urlStr, jsToWrap ) ;
			jsToWrap = 
				jsToWrap
					.replace("http://",   FServletURL +  "h_t_t_p_://" )
					.replace("HTTP://",   FServletURL +  "h_t_t_p_://" )
					.replace("HTTPS://",   FServletURL +  "h_t_t_p_s_://" )
					.replace("https://",   FServletURL +  "h_t_t_p_s_://" )
				;
			
			scriptTmp = ssTmp.putOrCreate(urlStr, jsToWrap, urlStr);
		} else{
			String jsToWrap = scriptTmp.getValue();
			String jsToWrapTmp = SServlet.performFormatJS(urlStr, jsToWrap ) ;
			if (jsToWrapTmp .length() > jsToWrap.length()){
				ssTmp.putOrCreate(urlStr, jsToWrapTmp, urlStr);
			}else{
				//System.out.println(jsToWrapTmp);
			}
		}
		resp.setContentType("application/x-javascript; charset=utf-8");
		outTmp = resp.getOutputStream();
		outTmp.write(scriptTmp.getValue().getBytes("UTF-8")) ;
		outTmp.flush();
		return outTmp;
	}

	private boolean isCSS(String contextTypeStr) {
		return "Content-Type: text/css".equalsIgnoreCase( contextTypeStr);
	}

	private boolean isBinary(String contextTypeStr) {
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

	private boolean isScript(String contextTypeStr) {
		return
		
		"Content-Type: text/javascript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/javascript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-javascript".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/javascript; charset=utf-8".equalsIgnoreCase( contextTypeStr) ||
		"Content-Type: application/x-javascript; charset=utf-8".equalsIgnoreCase( contextTypeStr) ||
		//"content-type: text/html; charset=ISO8859-1".equalsIgnoreCase( contextTypeStr) ||
		"content-type: text/javascript; charset=UTF-8".equalsIgnoreCase( contextTypeStr) || 
		(""+contextTypeStr).toLowerCase().indexOf("text/javascript")>=0;
	}

	private boolean isRootReq(HttpServletRequest req) {
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
	
	private byte[] getResourceAsBA(String namePar) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(namePar); 
		byte[] b = new byte[in.available()] ;
		in.read(b);
		return b;
	}
	
	

	private String getXEnc(HttpResponse respTmp) {
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

	private boolean isGZip(HttpResponse xRespTmp) {
		boolean retval = false;
		try{
			retval = "gzip".equals(xRespTmp.getHeaders("Content-Encoding")[0].getValue());
		}catch(Throwable e){}
		return  retval;
	}

	private ByteArrayOutputStream deZip(ByteArrayOutputStream oaos)
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
			//"Content-Type",
			"Content-Language",
			//"Content-Encoding",
			"Date",
			"Last-Modified" ,
			"Accept",
			"Accept-Charset",
			"Accept-Language",
			//"Accept-Encoding",
			"Referer", 
//			"Cookie",
			"Cache-Control",
			"User-Agent",
			"Cookie2",
			"Cookie2",
			"Expires",
			"TE",
			"Server",
			//"Set-Cookie",
			"Keep-Alive",
			"Authorization"
			
	};	
	protected static void setupResponseProperty(HttpServletResponse resp,
			HttpResponse respTmp) throws IOException {
		for (String headerName :headersToSet)
		for (Header next: respTmp.getHeaders(headerName) )
			resp.setHeader(next.getName(), next.getValue());
		
		// cookies
		for(Header setCookieHeader: respTmp.getHeaders("Set-Cookie") ){
			// Set-Cookie: itrude_data=s%3A0%3A%22%22%3B; expires=Tue, 19-Jul-2011 19:20:20 GMT; path=/; domain=it-ru.de, itrude_sid=1167d5639e1181bc58dda31856a7225f; path=/; domain=it-ru.de
            // Parse cookie
            String[] fields = setCookieHeader.getValue().split(";\\s*");
            String cookieValue = fields[0];
            String cookTmp = "C00C";
            String nameTmp =  cookTmp+ cookieValue;
            String expires = null;
            String path = null;
            String domain = null;
            boolean secure = false;

            // Parse each field
            for (int j=1; j<fields.length; j++) {
                if ("secure".equalsIgnoreCase(fields[j])) {
                    secure = true;
                } else if (fields[j].indexOf('=') > 0) {
                    String[] f = fields[j].split("=");
                    if ("expires".equalsIgnoreCase(f[0])) {
                        expires = f[1];
                    } else if ("domain".equalsIgnoreCase(f[0])) {
                        domain = "localhost";  cookieValue += f[1];
                    } else if ("path".equalsIgnoreCase(f[0])) {
                        path = f[1];
                    }
                }

                if (1==1){
                    // Save the cookie...
                    try{
                        nameTmp = nameTmp.split("=")[0]+cookieValue.hashCode() ;
                        nameTmp = nameTmp.replace(".", "_").replace("-", "_").replace(":", "_");
                        Cookie newCookie = new Cookie(nameTmp,new String ( Base64Coder.encode(   cookieValue.getBytes() ) ) );
	                    newCookie.setSecure(secure); 
	                    newCookie.setDomain(new URL(LServlet.SwapServletUrl).getHost());
	                    newCookie.setPath( HyperLinkUtil.encodeLink(  new URL("http://www.google.de") , path) );
                    	// Tue, 19-Jul-2011 20:00:53 GMT
                    	SimpleDateFormat sf = new SimpleDateFormat ("EEE, dd-MMM-yyyy hh:mm:ss z");
                    	if (expires != null){
                    		long nowTmp = (System.currentTimeMillis());
                    		long endTmp = sf.parse(expires).getTime();
                    		int expiresInSecTmp = (int)(endTmp-nowTmp)/1000;
                    		newCookie.setMaxAge( expiresInSecTmp);
                    	}else{
                    		newCookie.setMaxAge( 1111);
                    	}
                        resp.addCookie(newCookie);
                        if (TRACE) System.out.println("NEW COOKIE {"+
                        		newCookie.getName()+ "::"+
                        		newCookie.getValue()+ "::"+
                        		newCookie.getVersion()+ "::"+
                        		newCookie.getSecure()+ "::"+
                        		newCookie.getDomain()+ "::"+
                        		newCookie.getPath() + "::"+
                        		newCookie.getClass() + "::"+
                        		"}"
                        		);
                    }catch(Throwable e){}
                	
                }
                
            }

		}
		
	}
	
	protected  String[][] calcRequestHeaders(
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

	private Map<String, String> calcRequestHeadersAsMap(HttpServletRequest req, String[] headersToSetPar) {
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

	protected static void markFwswaperTagInResponseHead(HttpServletResponse resp) {
		resp.setHeader("l-swapper", String.format("com.lzy.fwswaper.%d",
				FWSwaperAppVersion));
	}
}
