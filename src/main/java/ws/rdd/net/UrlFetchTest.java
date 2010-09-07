package ws.rdd.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.StringTokenizer; 

import  org.htmlparser.http.Cookie; 
//import javax.servlet.http.Cookie; - setMaxAge >8E
//import org.apache.commons.httpclient.Cookie;
//import org.apache.http.cookie.Cookie;
import javax.servlet.http.HttpSession;

import net.sf.jsr107cache.Cache; 

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody; 
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.esxx.js.protocol.GAEConnectionManager;
import org.jrobin.cmd.RrdCommander;

import com.no10x.cache.Manager;
import com.no10x.cache.MemoryFileItem;

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 *         <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 * Creation: 08.04.2010::12:07:00<br>
 */
public class UrlFetchTest {
	private static final String COOKIES_STORE = "COOKIES_STORE";

	private HttpSession session;

	/**
	 * Cookie expiry date format for parsing.
	 */
	static protected SimpleDateFormat mFormat = new SimpleDateFormat(
			"EEE, dd-MMM-yy kk:mm:ss z");

	public UrlFetchTest(HttpSession sessionTmp) {
		this.session = sessionTmp;
	}

	public UrlFetchTest() {
		// TODO Auto-generated constructor stub
	}

	public String testFetchUrl(String toFetchStr)
			throws ClientProtocolException, IOException {
		HttpResponse respTmp = fetchGetResp(toFetchStr);
		System.out.println(respTmp);// s.getAllHeaders()
		HttpEntity eTmp = ((BasicHttpResponse) respTmp).getEntity();
		InputStream contentTmp = eTmp.getContent();
		int sizeTmp = Math.max((int) eTmp.getContentLength(), contentTmp
				.available());
		byte buf[] = new byte[sizeTmp];
		int readedTmp = contentTmp.read(buf);
		return new String(buf, 0, readedTmp);
	}

	public HttpResponse fetchGetResp(String toFetchStr) throws IOException,
			ClientProtocolException {
		return fetchGetResp(toFetchStr, new String[][]{});
	}
	public HttpResponse fetchGetResp(String toFetchStr, String headers[][])
			throws IOException, ClientProtocolException {
		HttpClient httpClient = makeHTTPClient();

		String schemes[] = {"https", "http"};
		for (String scheme : schemes) {
			String proxHostTmp = System.getProperty(scheme + ".proxyHost");// System.getProperties();
			String proxyPortTmp = System.getProperty(scheme + ".proxyPort");// System.setProperty("http.proxyHost","localhost");
			if (("" + proxHostTmp + proxyPortTmp).indexOf("null") == -1) {
				org.apache.http.HttpHost proxyTmp = new org.apache.http.HttpHost(
						proxHostTmp, Integer.parseInt(proxyPortTmp), scheme);
				httpClient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, proxyTmp);
			}
		}

		String fetchUrl = null == toFetchStr
				? "http://www.fiducia.de/service/suchergebnis.html?searchTerm=java"
				: toFetchStr;
		HttpUriRequest m = new HttpGet(fetchUrl);
		for (String[] nextHeader : headers)
			m.addHeader(nextHeader[0], nextHeader[1]);
		addCookies(m);
		m.addHeader("Host", m.getURI().getHost() );
		
		HttpResponse respTmp = httpClient.execute(m);
		StatusLine statusLine = respTmp.getStatusLine();
		String statusTmp = statusLine.toString();
		if (statusTmp.indexOf("200 OK") > 0) {
			System.out.println("resp.:" + statusLine);
		} else if ("HTTP/1.1 401 Unauthorized".equals(statusTmp)) {
			Cache cacheAuth = Manager.getCache(CACHE_NAME);
			String basicAuth = (String) cacheAuth.get(toFetchStr);
			if (basicAuth != null) {// go forward with cached
				m.addHeader("Authorization", "Basic " + basicAuth);
				respTmp = httpClient.execute(m);
			} else { // request auth from real user...
				respTmp.addHeader("WWW-Authenticate",
						"Basic realm=\"Tomcat Manager Application\"");
				respTmp.setStatusCode(401);
				respTmp.setStatusLine(statusLine);
			}

		}
		this.parseCookies(m, respTmp);
		return respTmp;
	}
	public static final String CACHE_NAME = UrlFetchTest.class.getName()
			+ ":Authorization";

	static {
		Cache cacheAuth = Manager.getCache(CACHE_NAME);
		String valueTmp = new String(Base64Coder.encode(("iboserviceuser" + ":"
				+ "iboserviceuser").getBytes()));
		cacheAuth.put(
				"https://pegasus.peras.fiducia.de/WebUrlaub27/Login.aspx",
				valueTmp);
	}

	public HttpClient makeHTTPClient() {
		HttpParams httpParams = new BasicHttpParams();

		org.apache.http.conn.ClientConnectionManager connectionManager = null;
		// ?new RrdGraphCmd():new RrdSvgCmd();
		if (!RrdCommander.isGAE()) {
			SchemeRegistry schreg = new SchemeRegistry();
			PlainSocketFactory socketFactory = PlainSocketFactory .getSocketFactory();
			schreg.register(new Scheme("http", socketFactory, 80));
			SSLSocketFactory socketFactory2 = SSLSocketFactory .getSocketFactory();
			schreg.register(new Scheme("https", socketFactory2, 443));
			connectionManager = new ThreadSafeClientConnManager(httpParams,
					schreg);
		} else {
			connectionManager = new GAEConnectionManager();
		}
		HttpClient httpClient = new DefaultHttpClient(connectionManager,
				httpParams);
		return httpClient;
	}

	public HttpResponse fetchResp(String toFetchStr, String[][] headers,
			Map parameterMap) throws ClientProtocolException, IOException {
		return fetchResp(toFetchStr, headers, parameterMap, null);
	}
	public HttpResponse fetchResp(String toFetchStr, String[][] headers,
			Map parameterMap, java.util.List<MemoryFileItem> items)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = makeHTTPClient();
		String schemes[] = {"https", "http", "ftp"};
		for (String scheme : schemes) {
			String proxHostTmp = System.getProperty(scheme + ".proxyHost");// System.getProperties();
			String proxyPortTmp = System.getProperty(scheme + ".proxyPort");// System.setProperty("http.proxyHost","localhost");
			if (("" + proxHostTmp + proxyPortTmp).indexOf("null") == -1) {
				org.apache.http.HttpHost proxyTmp = new org.apache.http.HttpHost(
						proxHostTmp, Integer.parseInt(proxyPortTmp), scheme);
				httpClient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, proxyTmp);
			}
		}

		String fetchUrl = null == toFetchStr
				? "http://www.fiducia.de/service/suchergebnis.html?searchTerm=java"
				: toFetchStr;
		HttpPost m = new HttpPost(fetchUrl);
		for (String[] nextHeader : headers)
			m.addHeader(nextHeader[0], nextHeader[1]);
		addCookies(m);
		m.addHeader("Host", m.getURI().getHost() );

		if (items != null) {// Multipart
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (final com.no10x.cache.MemoryFileItem item : items) {
				final ContentBody contentBody = new InputStreamBody(item
						.getInputStream(), item.getName());
				String name = item.getName();
				// For File parameters
				entity.addPart(name, contentBody);
			}
			m.setEntity(entity);

		}
		for (Object nextParName : parameterMap.keySet()) {
			String valueTmp = ""
					+ (((String[]) parameterMap.get(nextParName))[0]);
			HttpParams arg0 = httpClient.getParams();
			m.setParams(arg0.setParameter("" + nextParName, valueTmp));
		}
		HttpResponse respTmp = httpClient.execute(m);
		if (respTmp.getStatusLine().toString().indexOf("200 OK") > 0) {
			System.out.println("resp.:" + respTmp.getStatusLine());
		} else {
			m.addHeader("Authorization", "Basic "
					+ new String(Base64Coder.encode(("iboserviceuser" + ":"
							+ "iboserviceuser").getBytes())));
			respTmp = httpClient.execute(m);
		}
		this.parseCookies(m, respTmp);
		return respTmp;
	}

	/**
	 * Check for cookie and parse into cookie jar.
	 * 
	 * @param request
	 * @param connection
	 *            The connection to extract cookie information from.
	 */
	public void parseCookies(HttpUriRequest request, HttpResponse response/*
																	 * URLConnection
																	 * connection
																	 */) {
		Header[] setCookieHeaders;
		List<Cookie> cookies;

		String token;
		int index;

		String key;
		String value;
		Cookie cookie;

		setCookieHeaders = response.getHeaders("Set-Cookie");
		for (Header setCookieHeader : setCookieHeaders) {
			// set-cookie = "Set-Cookie:" cookies
			// cookies = 1#cookie
			// cookie = NAME "=" VALUE *(";" cookie-av)
			// NAME = attr
			// VALUE = value
			// cookie-av = "Comment" "=" value
			// | "Domain" "=" value
			// | "Max-Age" "=" value
			// | "Path" "=" value
			// | "Secure"
			// | "Version" "=" 1*DIGIT
			cookies = new ArrayList<Cookie>();
			String strCookieHeader = setCookieHeader.toString();
			strCookieHeader = strCookieHeader.substring(strCookieHeader.indexOf( "Set-Cookie: ") +"Set-Cookie: ".length());
			System.out.println("\"Set-Cookie: "+strCookieHeader +"\"");
			StringTokenizer tokenizer = new StringTokenizer(strCookieHeader,
					";,", true);
			cookie = null;
			String name;
			while (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken().trim();
				if (token.equals(";"))
					continue;
				else if (token.equals(",")) {
					cookie = null;
					continue;
				}

				index = token.indexOf('=');
				if (-1 == index) {
					if (null == cookie) { // an unnamed cookie
						name = "";
						value = token;
						key = name;
					} else {
						name = token;
						value = null;
						key = name.toLowerCase();
					}
				} else {
					name = token.substring(0, index);
					value = token.substring(index + 1);
					key = name.toLowerCase();
				}

				if (null == cookie) {
					try {
						cookie = new Cookie(name, value);
						cookies.add(cookie);
					} catch (IllegalArgumentException iae) {
						// should print a warning
						// for now just bail
						iae.printStackTrace();
						break;
					}
				} else {
					if (key.equals("expires")) // Wdy, DD-Mon-YY HH:MM:SS GMT
					{
						String comma = tokenizer.nextToken();
						String rest = tokenizer.nextToken();
						try {
							Date date = mFormat.parse(value + comma + rest);
							// http://download.oracle.com/javaee/1.4/api/javax/servlet/http/Cookie.html#setMaxAge(int)
							//cookie.setMaxAge((int) (date.getTime() - System .currentTimeMillis()) / 1000);
							cookie.setExpiryDate(date);
						} catch (Exception pe)// catch (ParseException pe)
						{
							// ok now set it to 1 day!
							//cookie.setMaxAge(24 * 60 * 60);
							cookie.setExpiryDate(new Date(System.currentTimeMillis()+1000*24*60*60));
						}
					} else if (key.equals("domain"))
						cookie.setDomain(value);
					else if (key.equals("path"))
						cookie.setPath(value);
					else if (key.equals("paath"))
						cookie.setPath(value);
					else if (key.equals("secure"))
						cookie.setSecure(true);
					else if (key.equals("comment"))
						cookie.setComment(value);
					else if (key.equals("version"))
						cookie.setVersion(Integer.parseInt(value));
					else if (key.equals("max-age")) {
						Date date = new Date();
						long then = date.getTime() + Integer.parseInt(value) * 1000;
						date.setTime(then);
						cookie.setExpiryDate(date);
						// cookie.setMaxAge (date);
						//cookie.setMaxAge((int) (date.getTime() - System .currentTimeMillis()) / 1000);
					} else
						// error,? unknown attribute,
						// maybe just another cookie
						// not separated by a comma
						try {
							cookie = new Cookie(name, value);
							cookies.add(cookie);
						} catch (IllegalArgumentException iae) {
							// should print a warning
							// for now just bail
							break;
						}
				}
			}
			if (0 != cookies.size())
				saveCookies(cookies, request);
		}
	}

	/**
	 * Adds a cookie to the cookie jar.
	 * 
	 * @param cookie
	 *            The cookie to add.
	 * @param domain
	 *            The domain to use in case the cookie has no domain attribute.
	 */
	public void setCookie(Cookie cookie, String domain) {
		String path;
		Cookie probe;
		boolean found; // flag if a cookie with current name is already there

		if (null != cookie.getDomain())
			domain = cookie.getDomain();
		path = cookie.getPath();
		Map<String, List<Cookie>> mCookieJar = getOrCreateStore();
		List<Cookie> cookies = mCookieJar.get(domain);
		if (null != cookies) {
			found = false;
			for (int j = 0; j < cookies.size(); j++) {
				probe = (Cookie) cookies.get(j);
				if (probe.getName().equalsIgnoreCase(cookie.getName())) {
					// we keep paths sorted most specific to least
					if (probe.getPath().equals(path)) {
						cookies.set(j, cookie); // replace
						found = true; // cookie found, set flag
						break;
					} else if (path.startsWith(probe.getPath())) {
						cookies.add(cookie);
						found = true; // cookie found, set flag
						break;
					}
				}
			}
			if (!found)
				// there's no cookie with the current name, therefore it's added
				// at the end of the list (faster then inserting at the front)
				cookies.add(cookie);
		} else { // new cookie list needed
			cookies = new ArrayList<Cookie>();
			cookies.add(cookie);
			mCookieJar.put(domain, cookies);
		}
	}

	/**
	 * @author vipup
	 * @return
	 */
	private Map<String, List<Cookie>> getOrCreateStore() {
		Map<String, List<Cookie>> mCookieJar = (Map<String, List<Cookie>>) session
				.getAttribute(COOKIES_STORE);
		if (null == mCookieJar) {
			mCookieJar = new HashMap<String, List<Cookie>>(); // turn on
																// cookie
																// processing
			session.setAttribute(COOKIES_STORE, mCookieJar);
		}
		return mCookieJar;
	}

	/**
	 * Generate a HTTP cookie header value string from the cookie jar.
	 * 
	 * <pre>
	 *   The syntax for the header is:
	 * 
	 *    cookie          =       &quot;Cookie:&quot; cookie-version
	 *                            1*((&quot;;&quot; | &quot;,&quot;) cookie-value)
	 *    cookie-value    =       NAME &quot;=&quot; VALUE [&quot;;&quot; path] [&quot;;&quot; domain]
	 *    cookie-version  =       &quot;$Version&quot; &quot;=&quot; value
	 *    NAME            =       attr
	 *    VALUE           =       value
	 *    path            =       &quot;$Path&quot; &quot;=&quot; value
	 *    domain          =       &quot;$Domain&quot; &quot;=&quot; value
	 * 
	 * 
	 * </pre>
	 * 
	 * @param connection
	 *            The connection being accessed.
	 * @see <a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a>
	 * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
	 */
	public void addCookies(HttpUriRequest request /* URLConnection connection */) {
		List<Cookie> list;
		URI url;
		String host;
		String path;
		String domain;

		Map<String, List<Cookie>> mCookieJar = getOrCreateStore();
		if (null != mCookieJar) {
			list = null;
			// get the site from the URL
			url = request.getURI();
			host = url.getHost();
			path = url.getPath();
			if (0 == path.length())
				path = "/";
			if (null != host) { // http://www.objectsdevelopment.com/portal/modules/freecontent/content/javawebserver.html
				List<Cookie> cookListTmp = mCookieJar.get (host);
				list = mergeCookies (cookListTmp, path, list);
				domain = getDomain(host);
				String keyCook = null;
				if (null != domain)
					//list = addCookies(  mCookieJar.get(domain), path, list);
					keyCook = domain;
				else
					// maybe it is the domain we're accessing
					//list = addCookies(  mCookieJar.get("." + host), path, list);
					keyCook = "." + host;
				cookListTmp = mCookieJar.get( keyCook );
				list = mergeCookies(  cookListTmp, path, list );
			}
			if (null != list) {
				String generateCookieProperty = generateCookieProperty(list);
				generateCookieProperty  = generateCookieProperty .replace( "; HttpOnly=null" , "");
				request.addHeader("Cookie", generateCookieProperty);
			}
		}
	}

	/**
	 * Save the cookies received in the response header.
	 * 
	 * @param list
	 *            The list of cookies extracted from the response header.
	 * @param connection
	 *            The connection (used when a cookie has no domain).
	 */
	protected void saveCookies(List<Cookie> list, HttpUriRequest request) {
		for (Cookie cookie : list) {

			String domain = cookie.getDomain();
			if (null == domain)
				domain = request.getURI().getHost();
			setCookie(cookie, domain);
		}
	}

	/**
	 * Get the domain from a host.
	 * 
	 * @param host
	 *            The supposed host name.
	 * @return The domain (with the leading dot), or null if the domain cannot
	 *         be determined.
	 */
	protected String getDomain(String host) {
		StringTokenizer tokenizer;
		int count;
		String server;
		int length;
		boolean ok;
		char c;
		String ret;

		ret = null;

		tokenizer = new StringTokenizer(host, ".");
		count = tokenizer.countTokens();
		if (3 <= count) {
			// have at least two dots,
			// check if we were handed an IP address by mistake
			length = host.length();
			ok = false;
			for (int i = 0; i < length && !ok; i++) {
				c = host.charAt(i);
				if (!(Character.isDigit(c) || (c == '.')))
					ok = true;
			}
			if (ok) {
				// so take everything  !
				server = tokenizer.nextToken();
				length = server.length();
				ret = host.substring(0);
			}
		}

		return (ret);
	}

	/**
	 * Creates the cookie request property value from the list of valid cookies
	 * for the domain.
	 * 
	 * @param cookies
	 *            The list of valid cookies to be encoded in the request.
	 * @return A string suitable for inclusion as the value of the "Cookie:"
	 *         request property.
	 */
	protected String generateCookieProperty(List<Cookie> cookies) {
		int version;
		StringBuffer buffer;
		String ret;

		ret = null;

		buffer = new StringBuffer();
		version = 0;
		// 1st: search for max
		for (Cookie cTmp : cookies) {
			version = Math.max(cTmp.getVersion(), version);
		}

		if (0 != version) {
			buffer.append("$Version=\"");
			buffer.append(version);
			buffer.append("\"");
		}
		// / 2nd: search for max
		for (Cookie cookie : cookies) {
			{

				if (0 != buffer.length())
					buffer.append("; ");
				buffer.append(cookie.getName());
				buffer.append(cookie.getName().equals("") ? "" : "=");
				if (0 != version)
					buffer.append("\"");
				buffer.append(cookie.getValue());
				if (0 != version)
					buffer.append("\"");
				if (0 != version) {
					if ((null != cookie.getPath())
							&& (0 != cookie.getPath().length())) {
						buffer.append("; $Path=\"");
						buffer.append(cookie.getPath());
						buffer.append("\"");
					}
					if ((null != cookie.getDomain())
							&& (0 != cookie.getDomain().length())) {
						buffer.append("; $Domain=\"");
						buffer.append(cookie.getDomain());
						buffer.append("\"");
					}
				}
			}
			if (0 != buffer.length())
				ret = buffer.toString();
		}
		return (ret);
		
	}

	/**
	 * Add qualified cookies from cookies into list.
	 * @param cookies The list of cookies to check (may be null).
	 * @param path The path being accessed.
	 * @param list The list of qualified cookies.
	 * @return The list of qualified cookies.
	 */
	protected List<Cookie> mergeCookies (List<Cookie> cookies, String path, List<Cookie> list)
	{
		List<Cookie> copyOfCookies = new ArrayList<Cookie> ();
		if (cookies!=null)
			copyOfCookies .addAll(cookies);
	    Date expires;
	    Date now;
	
	    if (null != cookies)
	    {
	        now = new Date ();
	        for (Cookie cookie :copyOfCookies)
	        {
	            
	            expires = cookie.getExpiryDate ();
	            if ((null != expires) && expires.before (now))
	            {
	                // clean original List from exired values
	            	cookies.remove (cookie);
	                //i--; // dick with the loop variable
	            }
	            else
	                if (path.startsWith (cookie.getPath ()))
	                {
	                    if (null == list)
	                        list = new ArrayList<Cookie> ();
	                    if (list .indexOf(cookie)==-1)
	                    	list.add(cookie);
	                }
	        }
	    }
	    
	    return (list);
	}

}
