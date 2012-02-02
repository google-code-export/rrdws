package ws.rrd.server;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  02.02.2012::15:25:57<br> 
 */
public class BlackListedException extends Exception {

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -3569003794794913152L;

	public BlackListedException(String decodedUrl) {
		super(decodedUrl);
	}

}


 