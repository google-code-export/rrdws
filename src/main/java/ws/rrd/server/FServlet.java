package ws.rrd.server; 
import java.io.IOException; 
import java.net.URL; 
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.vietspider.html.util.HyperLinkUtil; 

import ws.rdd.net.UrlFetchTest;
public class FServlet extends HttpServlet{ /* FORWARD-mastering servlet*/
	private static final long serialVersionUID = -5308225516841490806L; 
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream out = resp.getOutputStream();
		String uriTmp =   req.getRequestURL().toString() ;
		uriTmp = uriTmp.substring(uriTmp.indexOf("/F/" )+3);
		uriTmp = uriTmp.replace("h_t_t_p_://", "http://");
		uriTmp = uriTmp.replace("h_t_t_p_s_://", "https://");
		System.out.println("sendback "+uriTmp+"  ... ");
		
		 
		uriTmp = HyperLinkUtil.encodeLink(new URL(uriTmp), uriTmp);
		HttpSession sessionTmp = req.getSession();
		
		UrlFetchTest urlFetcherTmp = (UrlFetchTest) sessionTmp.getAttribute("UrlFetcher");
		if (urlFetcherTmp == null){
			urlFetcherTmp = new UrlFetchTest(sessionTmp);
			sessionTmp.setAttribute("UrlFetcher",urlFetcherTmp);
		}
		
		HttpResponse xRespTmp = urlFetcherTmp.fetchGetResp(uriTmp);
    	LServlet. setupResponseProperty( resp,  xRespTmp); 
    	ServletOutputStream outTmp = resp.getOutputStream();	
    	HttpEntity entity = xRespTmp.getEntity();
    	entity.writeTo(  outTmp );
 
	} 
}


 