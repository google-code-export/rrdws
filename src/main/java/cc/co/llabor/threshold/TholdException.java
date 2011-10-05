package cc.co.llabor.threshold;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  05.10.2011::14:37:09<br> 
 */
public class TholdException extends ClassNotFoundException {

	public TholdException(String string) {
		super(string);
	}

	public TholdException(String string,  Exception e) {
		super(string, e);
	}

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 6463621348907432257L;

}


 