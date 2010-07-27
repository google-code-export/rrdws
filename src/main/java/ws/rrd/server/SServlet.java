package ws.rrd.server; 
import java.io.IOException; 
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
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
		String scriptValue = scriptTmp.getValue();
		String linesTmp[] = scriptValue.split("\n");
		if (linesTmp[0].toLowerCase().trim().startsWith("<script"))
		if (linesTmp[linesTmp.length-1].toLowerCase().trim().startsWith("</script")){
			linesTmp[0] = "/*" +linesTmp[0] + "*/";
			linesTmp[linesTmp.length-1] = "/*" +linesTmp[linesTmp.length-1] + "*/";
			scriptValue = "";
			for (String nextLine:linesTmp)
				scriptValue += nextLine+"\n";
		}
		 
		out.write(scriptValue.getBytes());
	} 
}


 