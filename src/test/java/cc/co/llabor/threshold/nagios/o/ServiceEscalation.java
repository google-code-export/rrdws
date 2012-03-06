package cc.co.llabor.threshold.nagios.o;
/**
 * <b>Service escalations are completely optional and are used to escalate
 * notifications for a particular service. More information on how notification
 * escalations work can be found here.</b>
 * @see http://nagios.sourceforge.net/docs/3_0/escalations.html
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 21.02.2012::10:10:30<br>
 */
public class ServiceEscalation {
	// host_name: This directive is used to identify the short name(s) of the
	// host(s) that the service escalation should apply to or is associated
	// with.
	// hostgroup_name: This directive is used to specify the short name(s) of
	// the hostgroup(s) that the service escalation should apply to or is
	// associated with. Multiple hostgroups should be separated by commas. The
	// hostgroup_name may be used instead of, or in addition to, the host_name
	// directive.
	// service_description: This directive is used to identify the description
	// of the service the escalation should apply to.
	// first_notification: This directive is a number that identifies the first
	// notification for which this escalation is effective. For instance, if you
	// set this value to 3, this escalation will only be used if the service is
	// in a non-OK state long enough for a third notification to go out.
	// last_notification: This directive is a number that identifies the last
	// notification for which this escalation is effective. For instance, if you
	// set this value to 5, this escalation will not be used if more than five
	// notifications are sent out for the service. Setting this value to 0 means
	// to keep using this escalation entry forever (no matter how many
	// notifications go out).
	// contacts: This is a list of the short names of the contacts that should
	// be notified whenever there are problems (or recoveries) with this
	// service. Multiple contacts should be separated by commas. Useful if you
	// want notifications to go to just a few people and don't want to configure
	// contact groups. You must specify at least one contact or contact group in
	// each service escalation definition.
	// contact_groups: This directive is used to identify the short name of the
	// contact group that should be notified when the service notification is
	// escalated. Multiple contact groups should be separated by commas. You
	// must specify at least one contact or contact group in each service
	// escalation definition.
	// notification_interval: This directive is used to determine the interval
	// at which notifications should be made while this escalation is valid. If
	// you specify a value of 0 for the interval, Nagios will send the first
	// notification when this escalation definition is valid, but will then
	// prevent any more problem notifications from being sent out for the host.
	// Notifications are sent out again until the host recovers. This is useful
	// if you want to stop having notifications sent out after a certain amount
	// of time. Note: If multiple escalation entries for a host overlap for one
	// or more notification ranges, the smallest notification interval from all
	// escalation entries is used.
	// escalation_period: This directive is used to specify the short name of
	// the time period during which this escalation is valid. If this directive
	// is not specified, the escalation is considered to be valid during all
	// times.
	// escalation_options: This directive is used to define the criteria that
	// determine when this service escalation is used. The escalation is used
	// only if the service is in one of the states specified in this directive.
	// If this directive is not specified in a service escalation, the
	// escalation is considered to be valid during all service states. Valid
	// options are a combination of one or more of the following: r = escalate
	// on an OK (recovery) state, w = escalate on a WARNING state, u = escalate
	// on an UNKNOWN state, and c = escalate on a CRITICAL state. Example: If
	// you specify w in this field, the escalation will only be used if the
	// service is in a WARNING state.
}
