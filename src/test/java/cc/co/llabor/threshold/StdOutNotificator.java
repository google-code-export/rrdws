package cc.co.llabor.threshold;

import java.util.Date; 

/** 
 * <b>STDOUT Notificator</b>
 * 
 * 1) takes regular "event" from Alert-Captain, and forward it to wrapped RRD-Threshold 
 * 2) check EVERY_TIME_INTERVAL whether wrapped THOLD is IN_INCIDENT
 * 3) ^___and perform System.out.println("HELLO STDOUT")
 * 
 * 
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  28.09.2011::22:10:48<br> 
 */
public class StdOutNotificator extends BaselineAlerter{
    public StdOutNotificator(String rrdName, double baseLine, double gap,
			long activationTimeoutInSeconds) {
		super(rrdName, baseLine, gap, activationTimeoutInSeconds);
	}
	public static String HELLO_STDOUT = "HELLO STDOUT";
	long lastNotificationTimestamp = -1;
	int notificationCounter = 0;
	public int getNotificationCounter() { 
			return notificationCounter;
	}
	long notificationIntervalInSecs = 10 *60; // 10min ..1000 *
 	@Override
	public void performAction(long timestampSec) {
		super.performAction(timestampSec);
		if (super.inIncidentTime()>=0)
		if (timestampSec >this.lastNotificationTimestamp)	
		{
			this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
			System.out.println(HELLO_STDOUT +"N"+(notificationCounter++)+"Z"+new Date(timestampSec*1000));
		}
	}
}


 