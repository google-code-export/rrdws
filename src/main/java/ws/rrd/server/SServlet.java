package ws.rrd.server;  
import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.PrintStream;

import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
 
import org.mozilla.javascript.tools.shell.Main;

import ws.rrd.mem.ScriptItem;
import ws.rrd.mem.ScriptStore; 
public class SServlet extends HttpServlet{ /* SCRIPT-mastering servlet*/
	private static final long serialVersionUID = -5308225516841490806L; 
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream out = resp.getOutputStream();
		String uriTmp =   req.getRequestURL().toString() ;
		System.out.println("sendback "+uriTmp+" ...");
		ScriptItem scriptTmp = ScriptStore.getInstanse().getByURL(uriTmp);
		resp.setContentType("text/javascript");
		String scriptValue = "";
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		ByteArrayOutputStream baErr = new ByteArrayOutputStream();
		PrintStream myOut = new PrintStream(baOut);
		PrintStream myErr = new PrintStream(baErr);
		if (!"yes".equals( req.getParameter("skip") )){
   			final String scriptPath = this.getClass().getClassLoader(). getResource("beautifyALL.js").toExternalForm();
			String[] args = new String[]{scriptPath, "-i", "1", uriTmp+ "?skip=yes"};
						
			Main.setOut( myOut );
			Main.setErr( myErr );
			
			try{
				Main.main(args );
			}catch(java.security.AccessControlException e){				
				e.printStackTrace(myErr);
			}catch(java.lang.SecurityException e){
				myOut.append("\n<br><pre><!-- //*"+e.getMessage()+":::"+uriTmp+"*// --></pre>\n");
				e.printStackTrace(myErr);
			}catch(Throwable e){
				e.printStackTrace(myErr);
			}
			if (myErr .toString().length() > 0){
				System.out.println(myErr .toString());
				scriptValue = scriptTmp.getValue();
			}else{
				scriptValue = new String(baOut.toByteArray());
			}
		}else{
			scriptValue = scriptTmp.getValue();
		}
		String linesTmp[] = scriptValue.split("\n");
		if (linesTmp[0].toLowerCase().trim().startsWith("<script"))
		if (linesTmp[linesTmp.length-1].toLowerCase().trim().startsWith("</script")){
			linesTmp[0] = "/*" +linesTmp[0] + "*/";
			linesTmp[linesTmp.length-1] = "/*" +linesTmp[linesTmp.length-1] + "*/";
			scriptValue = "";
			for (String nextLine:linesTmp)
				scriptValue += nextLine+"\n";
		}
		// http://stackoverflow.com/questions/18985/javascript-beautifier
		
		 
		out.write(scriptValue.getBytes());
	} 
	

	private String readRes(String namePar) throws IOException {
		String scriptSource;
		InputStream in = this.getClass().getResourceAsStream(namePar);
		byte[] buf =  new byte[in.available()] ;
		in.read(buf);
		scriptSource = new String(buf);
		return scriptSource;
	}
}


 