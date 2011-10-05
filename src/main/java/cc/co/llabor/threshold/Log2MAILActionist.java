package cc.co.llabor.threshold;

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
public class Log2MAILActionist extends StdOutActionist{

	private static final Logger log = LoggerFactory.getLogger("MAIL2ROOT");
	
	Log2MAILActionist(Properties props){
		super(props);
	}

	public Log2MAILActionist(String rrdName, String monitorArgs,
			long notificationInterval) {
		super(rrdName, monitorArgs, notificationInterval);
		 
	}
	@Override
	public String getAction() { 
			return "log4j";
	}	
	

	@Override
	/**
	 * pid
	 */
	public String getActionArgs() {
		 	return "hiLog4J @{}#{} {} ,{} ";
	}

 	@Override
	public void performAction(long timestampSec) {
		
		if (inIncidentTime()>=0)
		if (timestampSec >this.lastNotificationTimestamp)	
		{
			this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
			String action = this.getAction();
			if ("log4j".equals(action)){
				log.info( getActionArgs() , new Object[]{this ,  notificationCounter++, timestampSec, new Date(timestampSec*1000)});
			}else{
				String message = "unknown Action:"+action;
				throw new RuntimeException(message );
			}
		}
		
		
	}

}


 