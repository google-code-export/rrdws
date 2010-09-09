package ws.rrd.server;   
import java.io.IOException;  
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
  

import cc.co.llabor.cache.css.CSStore; 
import cc.co.llabor.cache.css.Item;

public class CSServlet extends HttpServlet{ /* CSS-mastering servlet*/
	private static final long serialVersionUID = -5308225516841490806L; 
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream out = resp.getOutputStream();
		String uriTmp =   req.getRequestURL().toString() ;
		System.out.println("sendback "+uriTmp+" ...");
		
		resp.setContentType("text/css");
		String cssVal = "";

		// assumes already cached
		final CSStore instanse = CSStore.getInstanse();
		Item cssTmp = instanse.getByURL(uriTmp);
		cssVal = cssTmp.getValue() ;
		out.write(cssVal.getBytes()); 
		out.flush();
		instanse.putOrCreate(uriTmp, cssVal, cssTmp.getRefs().toArray(new String[]{})[0] );
	}

	 
	
 
}


 