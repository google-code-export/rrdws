package ws.rrd.server;

import java.io.IOException;
 
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.rrd.mem.ScriptItem;
import ws.rrd.mem.ScriptStore;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  26.07.2010::13:04:05<br> 
 */
public class SServlet extends HttpServlet{

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -5308225516841490806L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream out = resp.getOutputStream();
		String uriTmp =  LServlet.calcBase()+req.getRequestURI().substring(1);
		System.out.println("sendback "+uriTmp+" ...");
		ScriptItem scriptTmp = ScriptStore.getInstanse().getByURL(uriTmp);
		resp.setContentType("text/javascript");
		out.write(scriptTmp.getValue().getBytes());
	}

}


 