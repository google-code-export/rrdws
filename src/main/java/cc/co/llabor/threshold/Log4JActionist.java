package cc.co.llabor.threshold;

import java.util.Date;

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
public class Log4JActionist extends StdOutActionist{

	private static final Logger log = LoggerFactory.getLogger("L4JACT");

	public Log4JActionist(String rrdName, String monitorArgs,
			long notificationInterval) {
		super(rrdName, monitorArgs, notificationInterval);
		 
	}
	@Override
	public String getAction() { 
			return "log4j";
	}	
	
	public String toString(){
		return this.getMonitorType()+":"+this.getMonitorArgs()+"?"+this.getAction()+" ( "+this.getActionArgs() +" )";
	}

 	@Override
	public void performAction(long timestampSec) {
		
		if (inIncidentTime()>=0)
		if (timestampSec >this.lastNotificationTimestamp)	
		{
			this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
			String action = this.getAction();
			if ("log4j".equals(action)){
				log.info( HELLO_STDOUT +"N"+(notificationCounter++)+"Z"+new Date(timestampSec*1000));
			}else{
				String message = "unknown Action:"+action;
				throw new RuntimeException(message );
			}
		}
		
		
	}

}


 