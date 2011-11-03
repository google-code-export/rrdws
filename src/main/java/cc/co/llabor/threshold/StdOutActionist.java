package cc.co.llabor.threshold;
 
import java.util.Date; 
import java.util.Properties; 
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
	{ 
		this.dsName = "speed";
		this.action =  "syso";
		this.actionArgs =  "HELLO STDOUT"; 
	}		
	public StdOutActionist(String rrdName, String monitorArgs, long notificationInterval) {
		super(rrdName, monitorArgs, notificationInterval);
	}
	protected StdOutActionist(Properties props) {
		super(props);
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


 