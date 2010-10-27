package ws.rrd.server;   
import java.io.IOException;  
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
  

import cc.co.llabor.cache.js.Item;
import cc.co.llabor.cache.js.JSStore; 
import cc.co.llabor.cache.replace.ReplaceStore;

public class SServlet extends HttpServlet{ /* SCRIPT-mastering servlet*/
	private static final long serialVersionUID = -5308225516841490806L; 
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream out = resp.getOutputStream();
		String uriTmp =   req.getRequestURL().toString() ;
		System.out.println("sendback "+uriTmp+" ...");
		
		resp.setContentType("text/javascript");
		String scriptValue = "";

		// assumes already cached
		final JSStore instanse = JSStore.getInstanse();//http://ejohn.org/blog/bringing-the-browser-to-the-server/
		Item scriptTmp = instanse.getByURL(uriTmp); // http://www.snible.org/java2/uni2java.html
		try{
			scriptValue = scriptTmp.getValue() ;//http://realcode.ru/regexptester/ 
			ReplaceStore replacerTmp = ReplaceStore.getInstanse();//replacerTmp.putOrCreate(cacheKey, value)
			String newValue = replacerTmp.replaceByRules(uriTmp,scriptValue);
		
			out.write(newValue.getBytes()); 
			out.flush();
			String refTmp = ""+req.getHeaders("referer").nextElement();
			instanse.putOrCreate(uriTmp, scriptValue, refTmp   );
		}catch(Exception e){
			System.out.println("NOSCRIPT in the store! URL=["+uriTmp+"]");
			e.printStackTrace();
		}
	}

	 
	
 
}


 