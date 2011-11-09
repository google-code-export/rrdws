package cc.co.llabor.threshold.log2;

import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <b>Do almost the same as StdOutActionist, except the OUT.</b>
 * demonstration how to redefine the functionality 
 * 
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  04.10.2011::13:43:09<br> 
 */
public class Log2MAILActionist extends Log4JActionist{ 
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -4339988003907108789L;
	
	
	private static final Logger log = LoggerFactory.getLogger("MAIL2ROOT");
	
	public Log2MAILActionist(String string, String string2, long l) {
		super( string, string2, l);
	}

 	public Log2MAILActionist(Properties props) {
		super(props);
		init(props);
	}

	@Override
 	protected void act(long timestampSec) {
		log.info( getActionArgs() , new Object[]{this ,   timestampSec, new Date(timestampSec*1000)});
		beep();
	}

}


 