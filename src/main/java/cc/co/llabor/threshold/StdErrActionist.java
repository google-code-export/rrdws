package cc.co.llabor.threshold;

import java.util.Date;

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
public class StdErrActionist extends StdOutActionist{

	public StdErrActionist(String rrdName, String monitorArgs,
			long notificationInterval) {
		super(rrdName, monitorArgs, notificationInterval);
		 
	}

 	@Override
	public void performAction(long timestampSec) {
		
		if (inIncidentTime()>=0)
		if (timestampSec >this.lastNotificationTimestamp)	
		{
			this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
			String action = this.getAction();
			if ("syso".equals(action)){
				System.err.println(HELLO_STDOUT +"N"+(notificationCounter++)+"Z"+new Date(timestampSec*1000));
			}else{
				String message = "unknown Action:"+action;
				throw new RuntimeException(message );
			}
		}
		
		
	}

}


 