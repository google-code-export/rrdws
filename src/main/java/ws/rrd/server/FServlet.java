package ws.rrd.server; 
import java.io.IOException; 
import java.net.URL; 
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import org.vietspider.html.util.HyperLinkUtil; 
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
		resp.setStatus(301);
		resp.setHeader( "Location", "http://www.new-url.com/" );
		resp.setHeader( "Connection", "close" );
 
	} 
}


 