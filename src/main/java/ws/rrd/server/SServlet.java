package ws.rrd.server;  
import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.PrintStream;

import javax.script.ScriptException;
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
 
import org.mozilla.javascript.tools.shell.Main;

import cc.co.llabor.script.Beauty;

import ws.rrd.mem.ScriptItem;
import ws.rrd.mem.ScriptStore; 
public class SServlet extends HttpServlet{ /* SCRIPT-mastering servlet*/
	private static final long serialVersionUID = -5308225516841490806L; 
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream out = resp.getOutputStream();
		String uriTmp =   req.getRequestURL().toString() ;
		System.out.println("sendback "+uriTmp+" ...");
		
		resp.setContentType("text/javascript");
		String scriptValue = "";

		// assumes already cached
		final ScriptStore instanse = ScriptStore.getInstanse();
		ScriptItem scriptTmp = instanse.getByURL(uriTmp);
		scriptValue = scriptTmp.getValue() ;
		out.write(scriptValue.getBytes()); 
		out.flush();
		instanse.putOrCreate(uriTmp, scriptValue, scriptTmp.getRefs().toArray(new String[]{})[0] );
	}

	 
	
	public static String performFormatJS(String uriTmp, String scriptValue) throws IOException  {//, <-- ScriptItem scriptTmp
		 
		Beauty b = new Beauty(); 
		String formattedJS = scriptValue;
		try{
			formattedJS = b.fire(scriptValue);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
 
 		return formattedJS;
	} 
 
}


 