package ws.rdd.net;

import java.io.IOException;
import java.io.InputStream;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.esxx.js.protocol.GAEConnectionManager;

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
		HttpParams httpParams = new BasicHttpParams();
		org.apache.http.conn.ClientConnectionManager connectionManager = new GAEConnectionManager();
		HttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
		//		System.setProperty("http.proxyHost", "webcache.mydomain.com");
		// System.setPropery("http.proxyPort", "8080");
		String schemes[] =  {"htttps","http"};
		for (String scheme: schemes)
		if ((""+System.getProperty(scheme+".proxyHost")+System.getProperty(scheme+".proxyPort")).indexOf("null")==-1){
			org.apache.http.HttpHost proxy = new org.apache.http.HttpHost("proxy", 8080, scheme);
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
  
		String fetchUrl = null == toFetchStr? "http://www.fiducia.de/service/suchergebnis.html?searchTerm=zeit":toFetchStr;
		HttpUriRequest m =  new HttpGet(fetchUrl );;
		 HttpResponse s = httpClient.execute(m );
		 System.out.println(s);//s.getAllHeaders()
		 HttpEntity eTmp = ((BasicHttpResponse )s).getEntity();
		 InputStream contentTmp = eTmp.getContent();
		 int sizeTmp = contentTmp.available();
		 byte buf[] = new byte[sizeTmp];
		 int readedTmp = contentTmp.read(buf);
		 return new String( buf,0,readedTmp ) ; 
	}
	
}


 