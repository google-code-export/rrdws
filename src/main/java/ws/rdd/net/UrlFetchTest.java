package ws.rdd.net;

import java.io.IOException;
import java.io.InputStream; 
import java.util.Map;
import java.util.Properties;
  
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

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
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.james.mime4j.message.Entity;
import org.esxx.js.protocol.GAEConnectionManager;
import org.jrobin.cmd.RrdCommander;
 

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  08.04.2010::12:07:00<br> 
 */
public class UrlFetchTest {
	public String testFetchUrl(String toFetchStr) throws ClientProtocolException, IOException{
		HttpResponse respTmp = fetchResp(toFetchStr);
		 System.out.println(respTmp);//s.getAllHeaders()
		 HttpEntity eTmp = ((BasicHttpResponse )respTmp).getEntity();
		 InputStream contentTmp = eTmp.getContent();
		 int sizeTmp = Math.max(  (int)eTmp.getContentLength(), contentTmp.available());
		 byte buf[] = new byte[sizeTmp];
		 int readedTmp = contentTmp.read(buf);
		 return new String( buf,0,readedTmp ) ; 
	}

	public HttpResponse fetchResp(String toFetchStr) throws IOException, ClientProtocolException {
		return fetchResp(toFetchStr, new String[][]{});
	}
	public HttpResponse fetchResp(String toFetchStr, String headers[][]) throws IOException, ClientProtocolException {
		HttpClient httpClient = makeHTTPClient();

		String schemes[] = {"https", "http" };
		for (String scheme : schemes) {
			String proxHostTmp = System.getProperty(scheme + ".proxyHost");//System.getProperties();
			String proxyPortTmp = System.getProperty(scheme + ".proxyPort");//System.setProperty("http.proxyHost","localhost");
			if (("" + proxHostTmp + proxyPortTmp).indexOf("null") == -1) {
				org.apache.http.HttpHost proxyTmp = new org.apache.http.HttpHost(
						proxHostTmp, Integer.parseInt( proxyPortTmp ), scheme);
				httpClient.getParams().setParameter( ConnRoutePNames.DEFAULT_PROXY, proxyTmp);
			}
		}

		String fetchUrl = null == toFetchStr
				? "http://www.fiducia.de/service/suchergebnis.html?searchTerm=java"
				: toFetchStr;
		HttpUriRequest m = new HttpGet(fetchUrl);
		for (String []nextHeader :headers)
			m.addHeader(nextHeader[0], nextHeader[1]);
		HttpResponse respTmp = httpClient.execute(m);
		StatusLine statusLine = respTmp.getStatusLine();
		String statusTmp = statusLine.toString();
		if (statusTmp.indexOf("200 OK")>0){
			System.out.println("resp.:"+statusLine);
		}else if ("HTTP/1.1 401 Unauthorized".equals( statusTmp )){ 
			Cache cacheAuth = CacheManager.getInstance().getCache(this.getClass().getName()+":Authorization");
			String basicAuth = (String) cacheAuth.get(toFetchStr);
			if (basicAuth != null){// go forward with cached 
				m .addHeader("Authorization", "Basic " +  basicAuth );
				respTmp = httpClient.execute(m);
			}else{ // request auth from real user...
				respTmp.addHeader("WWW-Authenticate" , "Basic realm=\"Tomcat Manager Application\"");
				respTmp.setStatusCode(401);
				respTmp.setStatusLine( statusLine );
			}
			
		}
		return respTmp;
	}
	public static final String cacheName = UrlFetchTest.class.getName()+":Authorization";
	
	static{
		CacheManager cmInstance = CacheManager.getInstance();
		
		Cache cacheAuth = cmInstance.getCache(cacheName);
		if (cacheAuth == null){
			Map env = new Properties();
			CacheFactory cacheFactory;
			try {
				cacheFactory = cmInstance.getCacheFactory();
				cacheAuth = cacheFactory.createCache(env );
				cmInstance.registerCache(cacheName, cacheAuth);
			} catch (CacheException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		String valueTmp = new String(Base64Coder.encode(("iboserviceuser"+":"+"iboserviceuser").getBytes() )) ;
		cacheAuth.put("https://pegasus.peras.fiducia.de/WebUrlaub27/Login.aspx", valueTmp);
		
	}

	public HttpClient makeHTTPClient() {
		HttpParams httpParams = new BasicHttpParams();

		org.apache.http.conn.ClientConnectionManager connectionManager = null;
		// ?new RrdGraphCmd():new RrdSvgCmd();
		if (!RrdCommander.isGAE()) {
			SchemeRegistry schreg = new SchemeRegistry();
			schreg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schreg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			connectionManager = new ThreadSafeClientConnManager(httpParams,
					schreg);
		} else {
			connectionManager = new GAEConnectionManager();
		}
		HttpClient httpClient = new DefaultHttpClient(connectionManager,
				httpParams);
		return httpClient;
	}

	public HttpResponse fetchResp(String toFetchStr, String[][] headers, 	Map parameterMap) throws ClientProtocolException, IOException {
		return fetchResp(toFetchStr,  headers,   parameterMap, null);
	}
	public HttpResponse fetchResp(String toFetchStr, String[][] headers, 	Map parameterMap,  java.util.List<ws.rrd.mem.MemoryFileItem> items) throws ClientProtocolException, IOException {
		HttpClient httpClient = makeHTTPClient();
		String schemes[] = {"https", "http", "ftp"};
		for (String scheme : schemes) {
			String proxHostTmp = System.getProperty(scheme + ".proxyHost");//System.getProperties();
			String proxyPortTmp = System.getProperty(scheme + ".proxyPort");//System.setProperty("http.proxyHost","localhost");
			if (("" + proxHostTmp + proxyPortTmp).indexOf("null") == -1) {
				org.apache.http.HttpHost proxyTmp = new org.apache.http.HttpHost(
						proxHostTmp, Integer.parseInt( proxyPortTmp ), scheme);
				httpClient.getParams().setParameter( ConnRoutePNames.DEFAULT_PROXY, proxyTmp);
			}
		}

		String fetchUrl = null == toFetchStr
				? "http://www.fiducia.de/service/suchergebnis.html?searchTerm=java"
				: toFetchStr;
		HttpPost m = new HttpPost(fetchUrl);
		for (String []nextHeader :headers)
			m.addHeader(nextHeader[0], nextHeader[1]); 

		if (items !=null){// Multipart
			MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
			for (final ws.rrd.mem.MemoryFileItem item: items){
				final ContentBody contentBody =  new InputStreamBody (item.getInputStream(), item.getName());
				String name = item.getName() ;
				// For File parameters
				entity.addPart(name , contentBody)  ;
			}
			m.setEntity( entity );
			
		}
		for(Object nextParName:parameterMap.keySet()){
			String valueTmp =  ""+(((String[])parameterMap.get(nextParName))[0]);
			HttpParams arg0 = httpClient.getParams();
			m.setParams(arg0.setParameter(""+nextParName, valueTmp));
		}
		HttpResponse respTmp = httpClient.execute(m);
		if (respTmp.getStatusLine().toString().indexOf("200 OK")>0){
			System.out.println("resp.:"+respTmp.getStatusLine());
		}else{ 
			m .addHeader("Authorization", "Basic " + new String(Base64Coder.encode(("iboserviceuser"+":"+"iboserviceuser").getBytes() )) );
			respTmp = httpClient.execute(m);
		}		
		return respTmp;
	}
	
}


 