package cc.co.llabor.threshold.syso;
 
import java.util.Date; 
import java.util.Properties; 

import cc.co.llabor.threshold.AbstractActionist;
/** 
 * <b>The very first Actions-based implementation of Threshold</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.09.2011::16:37:58<br> 
 */
public class StdOutActionist extends AbstractActionist{  
	public StdOutActionist(String string, String string2, long i) {
		super(string, string2, i);
	}
	public StdOutActionist(Properties props) {
		super(props);
		init(props); 
	}	

	@Override
	protected void init(Properties props) 	{  
		this.dsName = "speed";
		this.action =  "syso";

		this.monitorType =  "mvel";
		this.actionArgs =  "HELLO STDOUT"; 
	}		
 
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -1411482451587802533L;
 
 	@Override
	protected void act(long timestampSec) {
				System.out.println(actionArgs +"N"+(notificationCounter++)+"Z"+new Date(timestampSec*1000));
	}
}


 