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
public class Log4JActionist extends StdOutActionist{
	
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 3434669922946134100L;

	public static final String LOG4J = "log4j";

	private static final Logger log = LoggerFactory.getLogger("L4JACT");

	public Log4JActionist(String rrdName, String monitorArgs,
			long notificationInterval) {
		super(rrdName, monitorArgs, notificationInterval);
		 
	}
	Log4JActionist(Properties props){
		super(props);
	}
	
	{ 
			this.action =  LOG4J;
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
			if (LOG4J.equals(action)){
				Logger logTmp = LoggerFactory.getLogger("D_LOG");
				logTmp.error( getActionArgs() , new Object[]{this ,  ""+notificationCounter++, ""+timestampSec, new Date(timestampSec*1000)});
			}else{
				String message = "unknown Action:"+action;
				throw new RuntimeException(message );
			}
		}
		
		
	}

}


 