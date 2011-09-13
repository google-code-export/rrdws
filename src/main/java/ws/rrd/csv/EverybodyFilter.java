package ws.rrd.csv;

import javax.management.Notification;
import javax.management.NotificationFilter;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  12.09.2011::22:31:33<br> 
 */
public class EverybodyFilter implements NotificationFilter {

	@Override
	public String toString() { 
		return "EverybodyFilter@true"; 
	}

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -5280397707641079989L;

	@Override
	public boolean isNotificationEnabled(Notification notification) { 
			return true; 
	}

}


 