package ws.rrd.csv;

import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  12.09.2011::17:45:31<br> 
 */
public class RrdNotificator {

	private NotificationListener listener;
	private NotificationFilter filter;
	private Object handback;

	public RrdNotificator(NotificationListener listener,
			NotificationFilter filter, Object handback) {
		this.setListener(listener);
		this.setFilter(filter);
		this.setHandback(handback);
	}

	public void setListener(NotificationListener listener) {
		this.listener = listener;
	}

	public NotificationListener getListener() { return listener;
	}

	public void setFilter(NotificationFilter filter) {
		this.filter = filter;
	}

	public NotificationFilter getFilter() {  
		return filter;
	}

	public void setHandback(Object handback) {
		this.handback = handback;
	}

	public Object getHandback() { return handback;
	}

}


 