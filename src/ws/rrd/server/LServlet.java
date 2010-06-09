package ws.rrd.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.apache.http.HttpResponse;
import org.vietspider.html.HTMLDocument;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.util.HyperLinkUtil;

import ws.rdd.net.UrlFetchTest;

//import org.lzy.fwswaper.swaper.SwaperFactory;
//import org.lzy.fwswaper.util.Base64Coder;
//import org.lzy.fwswaper.util.ExceptionUtils;

@SuppressWarnings("serial")
public class LServlet extends HttpServlet {

	// TODO: Create a config class to dynamic load settings from system.
	// propertiesin appengine-web.xml.
	//	public static String SwapServletUrl = "http://localhost:8080/swap/";		// dev.
	public static String SwapServletUrl = "local".equals(System
			.getProperty("myenviroment"))
			? "http://localhost:8888/l/"
			: "https://rrdws.appspot.com/l/"; // prod

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

	  private static void testGetLink(HTMLNode node){
	    List<String> list  = handler.scanSiteLink(node);
	    for(String ele : list)
	      System.out.println(ele);
	  }

	  private static void testCreateFullLink(HTMLNode node, URL home){
	    handler.createFullNormalLink(node, home);
	    List<String> list  = handler.scanSiteLink(node);
	    for(String ele : list)
	      System.out.println(ele);
	  }

	  private static void testCreateImageLink(HTMLNode node, URL home){
		    handler.createFullImageLink(node, home);
		    List<String> list  = handler.scanImageLink(node);
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
		try {
			StringBuffer requestURL = req.getRequestURL();
			String decodedUrl = requestURL.substring( SwapServletUrl.length());
			char[] charArray = decodedUrl.toCharArray();
			String urlStr = new String(Base64Coder.decode(charArray));
			System.out.println(urlStr);
			targetUrl = new StringBuilder(urlStr);

			if ((targetUrl.length() > 0) && (req.getQueryString() != null)
					&& (req.getQueryString().length() > 1)) {
				targetUrl.append(String.format("?%s", req.getQueryString()));
			}
			HttpResponse xRespTmp = new UrlFetchTest().fetchResp(urlStr);
			contextTypeStr = xRespTmp.getEntity().getContentType().toString();
			
			if (
					"Content-Type: image/jpeg".equals(contextTypeStr) ||
					"Content-Type: text/css".equals(contextTypeStr)					
			){
				resp.setContentType(contextTypeStr);
				xRespTmp.getEntity().writeTo(resp.getOutputStream()) ;
				return;
			}else{
				System.out.println("=====!!!======"+contextTypeStr);
			}
			 
			String data = new UrlFetchTest().testFetchUrl( urlStr ); 
			dataBuf = data.trim().getBytes();
			HTMLParser2 parser2 = new HTMLParser2();
			documentTmp = parser2.createDocument(dataBuf, "utf-8");
	    	URL realURL = new URL(urlStr);
	    	String suffix = realURL.getProtocol()+ "://"+realURL.getHost() ;
	    	suffix  = new String ( Base64Coder.encode(  suffix.getBytes() )  );
	    	URL url = new URL(SwapServletUrl+ suffix);
	    	testCreateFullLink(documentTmp.getRoot(), url);
	    	testCreateImageLink(documentTmp.getRoot(), url);	
	    	outTmp = resp.getOutputStream();
	    	String textValue = documentTmp.getTextValue();
			outTmp.write(textValue.getBytes("UTF-8"));
		} catch (java.lang.NoClassDefFoundError e) {
	    	System.out.println(contextTypeStr +" ===============  "+e.getMessage());e.printStackTrace();
	    	System.out.println(documentTmp);
		} catch (Exception e) {
			
			
			if (!"".equals(""+targetUrl  ) && targetUrl != null)
				ExceptionUtils.swapFailedException(targetUrl.toString(), resp,
						e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			else
				outTmp = resp.getOutputStream();
				InputStream in = this.getClass().getClassLoader().getResourceAsStream("index.html");
				byte buf[] = new byte[in.available()];
				String magik = "l11010101010000101010100101lIll1l0O0l10ll1001l1l01ll001/";
				int readRetVal = in.read(buf);
				String toBrowser = new String (buf);
				toBrowser = toBrowser.replace(magik,"" );// SwapServletUrl
				outTmp.write(toBrowser.getBytes());
				outTmp.flush();
				//ExceptionUtils.swapFailedException(resp, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}  
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

	protected static void setupResponseProperty(HttpServletResponse resp,
			HttpURLConnection swaperConn) throws IOException {
		for (String name : swaperConn.getHeaderFields().keySet())
			resp.setHeader(name, swaperConn.getHeaderField(name));
	}

	protected static void markFwswaperTagInResponseHead(HttpServletResponse resp) {
		resp.setHeader("fwswaper", String.format("com.lzy.fwswaper.%d",
				FWSwaperAppVersion));
	}
}
