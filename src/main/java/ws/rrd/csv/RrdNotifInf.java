package ws.rrd.csv;

import javax.management.Descriptor;
import javax.management.MBeanNotificationInfo;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  08.09.2011::14:44:12<br> 
 */
public class RrdNotifInf extends MBeanNotificationInfo {

	public RrdNotifInf(String[] notifTypes, String name, String description,
			Descriptor descriptor) {
		super(notifTypes, name, description, descriptor);
		 
	}

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 85137577983530437L;

}


 