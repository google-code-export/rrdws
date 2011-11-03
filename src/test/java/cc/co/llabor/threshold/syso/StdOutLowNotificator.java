package cc.co.llabor.threshold.syso;

import java.util.Date;

import cc.co.llabor.threshold.rrd.update.LowAlerter;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.09.2011::16:37:58<br> 
 */
public class StdOutLowNotificator extends LowAlerter {

	public StdOutLowNotificator(String rrdName, double lowLimit, long activationTimeoutInSeconds) {
		super(rrdName, lowLimit, activationTimeoutInSeconds);
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


 