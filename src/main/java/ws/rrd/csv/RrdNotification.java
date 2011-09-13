package ws.rrd.csv;

import javax.management.Notification;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  12.09.2011::22:35:27<br> 
 */
public class RrdNotification extends Notification {

	public RrdNotification(String type, Object source, long sequenceNumber,
			long timeStamp, String message) {
		super(type, source, sequenceNumber, timeStamp, message);
		 
	}

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 5678150322622578255L;

}


 