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
		
		resp.setContentType("text/javascript");
		String scriptValue = "";

		ScriptItem scriptTmp = ScriptStore.getInstanse().getByURL(uriTmp);
		scriptValue = scriptTmp.getValue() ;
		String hashTmp = ""+req.getParameter("hash");
		
			if (!"yes".equals( req.getParameter("skip") )){ 
				synchronized(hashTmp){
					System.out.println("LEVEL == ["+level +"]" );
					scriptValue = performFormatJS(uriTmp, scriptValue);
					scriptTmp = ScriptStore.getInstanse().putOrCreate(uriTmp, scriptValue, uriTmp);
				}
			} else{
				System.out.println("#"+level+"#gives back cached "+scriptValue);
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

	static int level = 0;
	
	public static String performFormatJS(String uriTmp, String scriptValue) {//, <-- ScriptItem scriptTmp
		level ++;

		final String scriptPath = SServlet.class.getClassLoader(). getResource("beautifyALL.js").toExternalForm();
		String strSctiptUrl = uriTmp;
		if (uriTmp.indexOf("?")>0){
			//strSctiptUrl = strSctiptUrl.substring(0,uriTmp.indexOf("?") );
			strSctiptUrl += "&";
		}else{
			strSctiptUrl += "?";
		}
		strSctiptUrl += "lllililII="+level+"&LIllillILlIlLI1="+uriTmp.length()+"&skip=yes&hash="+uriTmp.hashCode()+"&ts="+System.currentTimeMillis();
		
		 
		String[] args = new String[]{scriptPath, "-i", "1", strSctiptUrl};

		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		ByteArrayOutputStream baErr = new ByteArrayOutputStream();
		PrintStream myOut = new PrintStream(baOut);
		PrintStream myErr = new PrintStream(baErr);					
		
		try{
			synchronized (Main.class) {

				if (1==2){
					Main.setOut( myOut );
					Main.setErr( myErr );						
					Main.main(args );
				}else{
					return scriptValue;
				}
			}
		}catch(java.security.AccessControlException e){				
			e.printStackTrace(myErr);
		}catch(java.lang.SecurityException e){
			myOut.append("\n<br><pre><!-- //*"+e.getMessage()+":::"+uriTmp+"*// --></pre>\n");
			e.printStackTrace(myErr);
		}catch(Throwable e){
			e.printStackTrace(myErr);
		}finally{
			level --;
		}
		if (baErr.toString().length() > 0 && baErr.toString().indexOf( "exitVM" )==-1){
			System.out.println(baOut.toString());
			//scriptValue = scriptTmp.getValue();
		}else{
			scriptValue = new String(baOut.toByteArray());
		}
		return scriptValue;
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


 