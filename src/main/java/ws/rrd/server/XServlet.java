package ws.rrd.server;

import org.vietspider.html.HTMLDocument; 

/** 
 * <b>produce xmlht output</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  02.02.2011::17:41:08<br> 
 */
public class XServlet extends LServlet {
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 2011044130158675841L;

	/**
	 * @author vipup
	 * @param documentTmp
	 * @param contextEncStr
	 * @return
	 */
	public String renderDocument(HTMLDocument documentTmp, String contextEncStr) {
		String textValue; 
		textValue = documentTmp.getRoot().asXHTML();
		return textValue;
	}
}


 