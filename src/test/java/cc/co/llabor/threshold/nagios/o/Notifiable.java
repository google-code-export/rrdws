package cc.co.llabor.threshold.nagios.o;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.02.2012::17:55:22<br> 
 */
public interface Notifiable {

//	service_notifications_enabled	1
//	boolean isEnabled();
//
//	service_notification_period     24x7
//
//	host_notification_period        24x7
	TimePeriod getPeriod();
//
//	service_notification_options    w,u,c,r

//
//	host_notification_options       d,u,r
	
//
//	service_notification_commands   notify-by-email
	Command getCommand();
	
	

	// notification_interval: This directive is used to define the number of
	// "time units" to wait before re-notifying a contact that this service is
	// still down or unreachable. Unless you've changed the interval_length
	// directive from the default value of 60, this number will mean minutes. If
	// you set this value to 0, Nagios will not re-notify contacts about
	// problems for this host - only one problem notification will be sent out.
	int getNotification_interval();
	
	// first_notification_delay: This directive is used to define the number of
	// "time units" to wait before sending out the first problem notification
	// when this host enters a non-UP state. Unless you've changed the
	// interval_length directive from the default value of 60, this number will
	// mean minutes. If you set this value to 0, Nagios will start sending out
	// notifications immediately.
	int getFirst_notification_delay();
	
	// notification_period: This directive is used to specify the short name of
	// the time period during which notifications of events for this host can be
	// sent out to contacts. If a host goes down, becomes unreachable, or
	// recoveries during a time which is not covered by the time period, no
	// notifications will be sent out.
	int getNotification_period();
	
	// notification_options: This directive is used to determine when
	// notifications for the host should be sent out. Valid options are a
	// combination of one or more of the following: d = send notifications on a
	// DOWN state, u = send notifications on an UNREACHABLE state, r = send
	// notifications on recoveries (OK state), f = send notifications when the
	// host starts and stops flapping, and s = send notifications when scheduled
	// downtime starts and ends. If you specify n (none) as an option, no host
	// notifications will be sent out. If you do not specify any notification
	// options, Nagios will assume that you want notifications to be sent out
	// for all possible states. Example: If you specify d,r in this field,
	// notifications will only be sent out when the host goes DOWN and when it
	// recovers from a DOWN state.
	
	// notifications_enabled *: This directive is used to determine whether or
	// not notifications for this host are enabled. Values: 0 = disable host
	// notifications, 1 = enable host notifications.
//@see	service_notifications_enabled	1
	boolean isEnabled();	
	
}


 