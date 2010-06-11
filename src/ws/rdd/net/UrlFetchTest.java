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
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
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
		HttpParams httpParams = new BasicHttpParams();
		
		org.apache.http.conn.ClientConnectionManager connectionManager = null;
			//?new RrdGraphCmd():new RrdSvgCmd();
			if (!RrdCommander.isGAE()){
				SchemeRegistry schreg = new SchemeRegistry();
				schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				schreg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));				
				connectionManager = new ThreadSafeClientConnManager(httpParams, schreg); 
			}	else{
				connectionManager = new GAEConnectionManager();
			}
		HttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);

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
		 int sizeTmp = Math.max(  (int)eTmp.getContentLength(), contentTmp.available());
		 byte buf[] = new byte[sizeTmp];
		 int readedTmp = contentTmp.read(buf);
		 return new String( buf,0,readedTmp ) ; 
	}
	
}


 