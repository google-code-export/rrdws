package ws.rrd.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection; 
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
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.vietspider.html.HTMLDocument;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.util.HyperLinkUtil;

import ws.rdd.net.UrlFetchTest; 

@SuppressWarnings("serial")
public class LServlet extends HttpServlet {

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
 

	  private static void testCreateFullLink(HTMLNode node, String swapServletUrl2, URL home){
		    handler.createFullNormalLink(node,  swapServletUrl2,  home);
		    List<String> list  = handler.scanSiteLink(node);
		    for(String ele : list)
		      System.out.println(ele);
		  }
		  
	  private static void testCreateScriptLink(HTMLNode node, String swapServletUrl2, URL home){
		    handler.createScriptLink( node,  swapServletUrl2,  home);
		    //List<String> list  = handler.scanScriptLink(node );
		    //for(String ele : list)
		    //  System.out.println(ele);
		  }
		  
	  private static void testCreateMetaLink(HTMLNode node, String swapServletUrl2, URL home){
		    handler.createMetaLink(node,  swapServletUrl2,  home);
		    List<String> list  = handler.scanSiteLink(node);
		    for(String ele : list)
		      System.out.println(ele);
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
			String rurlTmp = ""+req.getRequestURL();
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
			System.out.println(urlStr);
			targetUrl = new StringBuilder(urlStr);

			if ((targetUrl.length() > 0) && (req.getQueryString() != null)
					&& (req.getQueryString().length() > 1)) {
				targetUrl.append(String.format("?%s", req.getQueryString()));
				urlStr = targetUrl.toString();
			}
			
			String[][] headsToResend = calcRequestHeaders(req);
			 
			urlStr  = urlStr.replace(" ", "%20");
			// http://it-ru.de/forum/viewtopic.php?t=182374&amp;postdays=0&amp;postorder=asc&amp;start=15
			urlStr  = urlStr.replace("&amp;", "&");
			HttpResponse xRespTmp = new UrlFetchTest().fetchResp(urlStr, headsToResend);
			HttpEntity entity = xRespTmp.getEntity();
			contextTypeStr = ""+entity.getContentType();
			String contextEncStr =  ""+entity.getContentEncoding() ;
			contextEncStr = "null".equals(contextEncStr)?getXEnc(xRespTmp):contextEncStr;
			if ("null" .equals( ""+contextEncStr ) &&  contextTypeStr.toLowerCase().startsWith("content-type: text/html")){
				int encPos = contextTypeStr.toLowerCase().indexOf(CHARSET_PREFIX);
				if (encPos>0){
					
					contextEncStr = contextTypeStr.substring(encPos+CHARSET_PREFIX.length());
					contextEncStr = contextEncStr .toUpperCase();
					log.warning(contextEncStr + "  }} ENC :=  {  "+contextTypeStr+" } ::: enc ::: "+contextEncStr +"["+urlStr+"]");

				}else{
					
					Header[] contextEncHeaders = xRespTmp.getHeaders("Content-Encoding");
					try{
						contextEncStr =  contextEncHeaders[0].getValue();
					}catch(Throwable e){}
					log.warning("Content-Encoding[0]::=={"+ contextEncStr + "  }" );
					 
				}
			}else{
				log.warning("nonull::::"+ contextEncStr + " ::::: "+ contextTypeStr );
			}
			
			if (					
				"Content-Type: text/css".equalsIgnoreCase( contextTypeStr) 
				)
			{
				log.warning("CSS contextTypeStr / contextEncStr:{"+contextTypeStr+" / "+contextEncStr +"}, url== ["+urlStr+"]");
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
				.replace("URL (/l.gif?", "url(/l.gif?")
				;
				resp.setContentType("text/css");
				outTmp = resp.getOutputStream();
				outTmp.write(xCSS.getBytes());
				outTmp.flush();
				return;
			}else
			if (	
					"null".equalsIgnoreCase( contextTypeStr)||
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
					"Content-Type: application/x-shockwave-flash".equalsIgnoreCase( contextTypeStr) 
				
					
			){
				if (! "null".equals( contextTypeStr )){
					String contypeTmp = contextTypeStr.substring("Content-Type:".length());
					resp.setContentType(contypeTmp);
					setupResponseProperty( resp,  xRespTmp);
				}
				log.warning("HTML contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
				outTmp = resp.getOutputStream();
				entity.writeTo(outTmp) ;
				outTmp.flush();
				return;
			}else if (
					 
 					"Content-Type: application/x-javascript".equalsIgnoreCase( contextTypeStr) ||
					//"content-type: text/html; charset=ISO8859-1".equalsIgnoreCase( contextTypeStr) ||
					"content-type: text/javascript; charset=UTF-8".equalsIgnoreCase( contextTypeStr)  
 
					){
				log.warning("JS contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
				
				ByteArrayOutputStream oaos = new ByteArrayOutputStream();
				entity.writeTo(oaos) ;				
				
				outTmp = resp.getOutputStream();
				oaos.writeTo(outTmp) ;
				outTmp.flush();
				return;
				
			}	else{
			 
				String xEncTmp = getXEnc(xRespTmp);
				
				log.warning("x---HTML---x  contextTypeStr/contextEncStr:"+contextTypeStr+" : :  enc : : "+contextEncStr +"["+urlStr+"]   XXX::"+xEncTmp);
				System.out.println("=====!!!======"+contextTypeStr +"::::"+contextEncStr);
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
			String xCSS = null;
			if ("null".equals(  ""+contextEncStr )){
				xCSS = oaos.toString();
			}else{
				xCSS = oaos.toString(contextEncStr);//xCSS.toUpperCase().substring( 12430)
			}
			String data = xCSS;// data = new UrlFetchTest().testFetchUrl( urlStr ); 
			if (data.toLowerCase().indexOf("content=\"text/html; charset=")>0)try{
				String contextText = "content=\"text/html; charset=";
				int lenTmp = contextText.length();
				int posTmp = data.toLowerCase().indexOf(contextText);
				int beginIndex = posTmp +lenTmp;
				int endIndex = beginIndex + data.toLowerCase().substring(beginIndex).indexOf("\"");
				contextEncStr = data.toLowerCase().substring(beginIndex,endIndex );
				contextEncStr = contextEncStr.toUpperCase();
				data = oaos.toString(contextEncStr);
			}catch(Throwable e){}
			if ("null".equals(""+contextEncStr)){
				dataBuf = data.trim().getBytes();// "ISO-8859-1"
 			} else {
				dataBuf = data.trim().getBytes(contextEncStr);// "utf-8"
			}
			HTMLParser2 parser2 = new HTMLParser2();
			try{
				documentTmp = parser2.createDocument(dataBuf, contextEncStr );// "utf-8"
			}catch(Exception e){
				log.warning("createDocument EXCEPTION!" +e.getMessage()+" contextTypeStr||contextEncStr:["+contextTypeStr+"||"+contextEncStr+"]  URL =:["+urlStr+"]");
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
	    	String textValue  = null;
	    	//String textValue = new String(documentTmp.getTextValue().getBytes("ISO-8859-1"), contextEncStr);// "windows-1251" textValue.toUpperCase().substring( 12430)
	    	if ("KOI8-R".equals(contextEncStr)) {
	    		textValue = documentTmp.getTextValue();//
	    	}else{
	    		textValue = documentTmp.getTextValue();//
	    	}
	    	//PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	    	String string1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML  4.01 Transitional//EN\"><HtMl>\n\t<!-- contextEncStr="+contextEncStr+" -->";
	    	String string2 = "</HtMl>";
			//outTmp.write(string1.getBytes(contextEncStr));
	    	if (!"null".equals(""+contextEncStr)){
	    		outTmp.write((string1 + textValue + string2).getBytes(contextEncStr));//)
	    	}else{
	    		outTmp.write((string1 + textValue + string2).getBytes());//)
	    	}
			//outTmp.write(string2.getBytes(contextEncStr));
			//outTmp.flush();
		} catch (java.lang.NoClassDefFoundError e) {
	    	System.out.println(contextTypeStr +" ===============  "+e.getMessage());e.printStackTrace();
	    	System.out.println(documentTmp);
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
				toBrowser = toBrowser.replace(magik,"" );// SwapServletUrl
				outTmp.write(toBrowser.getBytes());
				
				outTmp.write("<pre>".getBytes());
				outTmp.write((""+e.getMessage()+"\n\n\n\n"+e.getStackTrace()).getBytes());
				e.printStackTrace(new PrintWriter(outTmp, true));
				//outTmp.flush();
				//ExceptionUtils.swapFailedException(resp, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}  
	}



	private String getXEnc(HttpResponse respTmp) {
		 String retval = null;
		 try{
			 retval = respTmp.getHeaders("Content-Encoding")[0].getValue();
		 }catch (Exception e) {
			// TODO: handle exception
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
			"Accept-Charset",
			"Accept-Language",
			//"Accept-Encoding",
			"Referer", 
			"Cookie",
			"Cache-Control",
			"User-Agent"
			
	};	
	protected static void setupResponseProperty(HttpServletResponse resp,
			HttpResponse respTmp) throws IOException {
		for (String headerName :headersToSet)
		for (Header next: respTmp.getHeaders(headerName) )
			resp.setHeader(next.getName(), next.getValue());
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
