package cc.co.llabor.threshold.nagios.o;

import java.util.List;

/**
 * <b>A contact definition is used to identify someone who should be contacted
 * in the event of a problem on your network. The different arguments to a
 * contact definition are described below.</b>
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 20.02.2012::17:36:52<br>
 */
public class Contact {
//	contact_name	contact_name
	String contact_name;
//	alias	alias
	String alias;
//	contactgroups	contactgroup_names
//	List<ContactGroup> contactgroups[];
//	host_notifications_enabled	[0/1]
//	boolean host_notifications_enabled;
//	service_notifications_enabled	[0/1]
//	boolean service_notifications_enabled;
//	host_notification_period	timeperiod_name
//	TimePeriod host_notification_period;
//	service_notification_period	timeperiod_name

//	host_notification_options	[d,u,r,f,s,n]
//	service_notification_options	[w,u,c,r,f,s,n]
//	host_notification_commands	command_name

//	service_notification_commands	command_name
	List<Notifiable>  nofifications;
//	email	email_address
	String email; 
//	pager	pager_number or pager_email_gateway
//	addressx	additional_contact_address
	String addressx;
//	can_submit_commands	[0/1]
//	retain_status_information	[0/1]
//	retain_nonstatus_information	[0/1]

}
