package cc.co.llabor.threshold.nagios.o;

import java.util.List;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  21.02.2012::09:50:53<br> 
 */
public class Host extends Service {
	// host_name: This directive is used to define a short name used to identify
	// the host. It is used in host group and service definitions to reference
	// this particular host. Hosts can have multiple services (which are
	// monitored) associated with them. When used properly, the $HOSTNAME$ macro
	// will contain this short name.
	// alias: This directive is used to define a longer name or description used
	// to identify the host. It is provided in order to allow you to more easily
	// identify a particular host. When used properly, the $HOSTALIAS$ macro
	// will contain this alias/description.
	String alias;
	// address: This directive is used to define the address of the host.
	// Normally, this is an IP address, although it could really be anything you
	// want (so long as it can be used to check the status of the host). You can
	// use a FQDN to identify the host instead of an IP address, but if DNS
	// services are not available this could cause problems. When used properly,
	// the $HOSTADDRESS$ macro will contain this address. Note: If you do not
	// specify an address directive in a host definition, the name of the host
	// will be used as its address. A word of caution about doing this, however
	// - if DNS fails, most of your service checks will fail because the plugins
	// will be unable to resolve the host name.
	// display_name: This directive is used to define an alternate name that
	// should be displayed in the web interface for this host. If not specified,
	// this defaults to the value you specify for the host_name directive. Note:
	// The current CGIs do not use this option, although future versions of the
	// web interface will.
	// parents: This directive is used to define a comma-delimited list of short
	// names of the "parent" hosts for this particular host. Parent hosts are
	// typically routers, switches, firewalls, etc. that lie between the
	// monitoring host and a remote hosts. A router, switch, etc. which is
	// closest to the remote host is considered to be that host's "parent". Read
	// the "Determining Status and Reachability of Network Hosts" document
	// located here for more information. If this host is on the same network
	// segment as the host doing the monitoring (without any intermediate
	// routers, etc.) the host is considered to be on the local network and will
	// not have a parent host. Leave this value blank if the host does not have
	// a parent host (i.e. it is on the same segment as the Nagios host). The
	// order in which you specify parent hosts has no effect on how things are
	// monitored.
	// hostgroups: This directive is used to identify the short name(s) of the
	// hostgroup(s) that the host belongs to. Multiple hostgroups should be
	// separated by commas. This directive may be used as an alternative to (or
	// in addition to) using the members directive in hostgroup definitions.
	// check_command: This directive is used to specify the short name of the
	// command that should be used to check if the host is up or down.
	// Typically, this command would try and ping the host to see if it is
	// "alive". The command must return a status of OK (0) or Nagios will assume
	// the host is down. If you leave this argument blank, the host will not be
	// actively checked. Thus, Nagios will likely always assume the host is up
	// (it may show up as being in a "PENDING" state in the web interface). This
	// is useful if you are monitoring printers or other devices that are
	// frequently turned off. The maximum amount of time that the notification
	// command can run is controlled by the host_check_timeout option.
	List<HostGroup> groups;
	
	// initial_state: By default Nagios will assume that all hosts are in UP
	// states when it starts. You can override the initial state for a host by
	// using this directive. Valid options are: o = UP, d = DOWN, and u =
	// UNREACHABLE.
	// max_check_attempts: This directive is used to define the number of times
	// that Nagios will retry the host check command if it returns any state
	// other than an OK state. Setting this value to 1 will cause Nagios to
	// generate an alert without retrying the host check. Note: If you do not
	// want to check the status of the host, you must still set this to a
	// minimum value of 1. To bypass the host check, just leave the
	// check_command option blank.
	// check_interval: This directive is used to define the number of
	// "time units" between regularly scheduled checks of the host. Unless
	// you've changed the interval_length directive from the default value of
	// 60, this number will mean minutes. More information on this value can be
	// found in the check scheduling documentation.
	// retry_interval: This directive is used to define the number of
	// "time units" to wait before scheduling a re-check of the hosts. Hosts are
	// rescheduled at the retry interval when they have changed to a non-UP
	// state. Once the host has been retried max_check_attempts times without a
	// change in its status, it will revert to being scheduled at its "normal"
	// rate as defined by the check_interval value. Unless you've changed the
	// interval_length directive from the default value of 60, this number will
	// mean minutes. More information on this value can be found in the check
	// scheduling documentation.
	// active_checks_enabled *: This directive is used to determine whether or
	// not active checks (either regularly scheduled or on-demand) of this host
	// are enabled. Values: 0 = disable active host checks, 1 = enable active
	// host checks (default).
	// passive_checks_enabled *: This directive is used to determine whether or
	// not passive checks are enabled for this host. Values: 0 = disable passive
	// host checks, 1 = enable passive host checks (default).
	// check_period: This directive is used to specify the short name of the
	// time period during which active checks of this host can be made.
	// obsess_over_host *: This directive determines whether or not checks for
	// the host will be "obsessed" over using the ochp_command.
	// check_freshness *: This directive is used to determine whether or not
	// freshness checks are enabled for this host. Values: 0 = disable freshness
	// checks, 1 = enable freshness checks (default).
	// freshness_threshold: This directive is used to specify the freshness
	// threshold (in seconds) for this host. If you set this directive to a
	// value of 0, Nagios will determine a freshness threshold to use
	// automatically.
	// event_handler: This directive is used to specify the short name of the
	// command that should be run whenever a change in the state of the host is
	// detected (i.e. whenever it goes down or recovers). Read the documentation
	// on event handlers for a more detailed explanation of how to write scripts
	// for handling events. The maximum amount of time that the event handler
	// command can run is controlled by the event_handler_timeout option.
	// event_handler_enabled *: This directive is used to determine whether or
	// not the event handler for this host is enabled. Values: 0 = disable host
	// event handler, 1 = enable host event handler.
	// low_flap_threshold: This directive is used to specify the low state
	// change threshold used in flap detection for this host. More information
	// on flap detection can be found here. If you set this directive to a value
	// of 0, the program-wide value specified by the low_host_flap_threshold
	// directive will be used.
	// high_flap_threshold: This directive is used to specify the high state
	// change threshold used in flap detection for this host. More information
	// on flap detection can be found here. If you set this directive to a value
	// of 0, the program-wide value specified by the high_host_flap_threshold
	// directive will be used.
	// flap_detection_enabled *: This directive is used to determine whether or
	// not flap detection is enabled for this host. More information on flap
	// detection can be found here. Values: 0 = disable host flap detection, 1 =
	// enable host flap detection.
	// flap_detection_options: This directive is used to determine what host
	// states the flap detection logic will use for this host. Valid options are
	// a combination of one or more of the following: o = UP states, d = DOWN
	// states, u = UNREACHABLE states.
	// process_perf_data *: This directive is used to determine whether or not
	// the processing of performance data is enabled for this host. Values: 0 =
	// disable performance data processing, 1 = enable performance data
	// processing.
	// retain_status_information: This directive is used to determine whether or
	// not status-related information about the host is retained across program
	// restarts. This is only useful if you have enabled state retention using
	// the retain_state_information directive. Value: 0 = disable status
	// information retention, 1 = enable status information retention.
	// retain_nonstatus_information: This directive is used to determine whether
	// or not non-status information about the host is retained across program
	// restarts. This is only useful if you have enabled state retention using
	// the retain_state_information directive. Value: 0 = disable non-status
	// information retention, 1 = enable non-status information retention.
	// contacts: This is a list of the short names of the contacts that should
	// be notified whenever there are problems (or recoveries) with this host.
	// Multiple contacts should be separated by commas. Useful if you want
	// notifications to go to just a few people and don't want to configure
	// contact groups. You must specify at least one contact or contact group in
	// each host definition.
	// contact_groups: This is a list of the short names of the contact groups
	// that should be notified whenever there are problems (or recoveries) with
	// this host. Multiple contact groups should be separated by commas. You
	// must specify at least one contact or contact group in each host
	// definition.
	// notification_interval: This directive is used to define the number of
	// "time units" to wait before re-notifying a contact that this service is
	// still down or unreachable. Unless you've changed the interval_length
	// directive from the default value of 60, this number will mean minutes. If
	// you set this value to 0, Nagios will not re-notify contacts about
	// problems for this host - only one problem notification will be sent out.
	// first_notification_delay: This directive is used to define the number of
	// "time units" to wait before sending out the first problem notification
	// when this host enters a non-UP state. Unless you've changed the
	// interval_length directive from the default value of 60, this number will
	// mean minutes. If you set this value to 0, Nagios will start sending out
	// notifications immediately.
	// notification_period: This directive is used to specify the short name of
	// the time period during which notifications of events for this host can be
	// sent out to contacts. If a host goes down, becomes unreachable, or
	// recoveries during a time which is not covered by the time period, no
	// notifications will be sent out.
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
	// stalking_options: This directive determines which host states "stalking"
	// is enabled for. Valid options are a combination of one or more of the
	// following: o = stalk on UP states, d = stalk on DOWN states, and u =
	// stalk on UNREACHABLE states. More information on state stalking can be
	// found here.
	// notes: This directive is used to define an optional string of notes
	// pertaining to the host. If you specify a note here, you will see the it
	// in the extended information CGI (when you are viewing information about
	// the specified host).
	// notes_url: This variable is used to define an optional URL that can be
	// used to provide more information about the host. If you specify an URL,
	// you will see a red folder icon in the CGIs (when you are viewing host
	// information) that links to the URL you specify here. Any valid URL can be
	// used. If you plan on using relative paths, the base path will the the
	// same as what is used to access the CGIs (i.e. /cgi-bin/nagios/). This can
	// be very useful if you want to make detailed information on the host,
	// emergency contact methods, etc. available to other support staff.
	// action_url: This directive is used to define an optional URL that can be
	// used to provide more actions to be performed on the host. If you specify
	// an URL, you will see a red "splat" icon in the CGIs (when you are viewing
	// host information) that links to the URL you specify here. Any valid URL
	// can be used. If you plan on using relative paths, the base path will the
	// the same as what is used to access the CGIs (i.e. /cgi-bin/nagios/).
	// icon_image: This variable is used to define the name of a GIF, PNG, or
	// JPG image that should be associated with this host. This image will be
	// displayed in the various places in the CGIs. The image will look best if
	// it is 40x40 pixels in size. Images for hosts are assumed to be in the
	// logos/ subdirectory in your HTML images directory (i.e.
	// /usr/local/nagios/share/images/logos).
	// icon_image_alt: This variable is used to define an optional string that
	// is used in the ALT tag of the image specified by the <icon_image>
	// argument.
	// vrml_image: This variable is used to define the name of a GIF, PNG, or
	// JPG image that should be associated with this host. This image will be
	// used as the texture map for the specified host in the statuswrl CGI.
	// Unlike the image you use for the <icon_image> variable, this one should
	// probably not have any transparency. If it does, the host object will look
	// a bit wierd. Images for hosts are assumed to be in the logos/
	// subdirectory in your HTML images directory (i.e.
	// /usr/local/nagios/share/images/logos).
	// statusmap_image: This variable is used to define the name of an image
	// that should be associated with this host in the statusmap CGI. You can
	// specify a JPEG, PNG, and GIF image if you want, although I would strongly
	// suggest using a GD2 format image, as other image formats will result in a
	// lot of wasted CPU time when the statusmap image is generated. GD2 images
	// can be created from PNG images by using the pngtogd2 utility supplied
	// with Thomas Boutell's gd library. The GD2 images should be created in
	// uncompressed format in order to minimize CPU load when the statusmap CGI
	// is generating the network map image. The image will look best if it is
	// 40x40 pixels in size. You can leave these option blank if you are not
	// using the statusmap CGI. Images for hosts are assumed to be in the logos/
	// subdirectory in your HTML images directory (i.e.
	// /usr/local/nagios/share/images/logos).
	// 2d_coords: This variable is used to define coordinates to use when
	// drawing the host in the statusmap CGI. Coordinates should be given in
	// positive integers, as they correspond to physical pixels in the generated
	// image. The origin for drawing (0,0) is in the upper left hand corner of
	// the image and extends in the positive x direction (to the right) along
	// the top of the image and in the positive y direction (down) along the
	// left hand side of the image. For reference, the size of the icons drawn
	// is usually about 40x40 pixels (text takes a little extra space). The
	// coordinates you specify here are for the upper left hand corner of the
	// host icon that is drawn. Note: Don't worry about what the maximum x and y
	// coordinates that you can use are. The CGI will automatically calculate
	// the maximum dimensions of the image it creates based on the largest x and
	// y coordinates you specify.
	// 3d_coords: This variable is used to define coordinates to use when
	// drawing the host in the statuswrl CGI. Coordinates can be positive or
	// negative real numbers. The origin for drawing is (0.0,0.0,0.0). For
	// reference, the size of the host cubes drawn is 0.5 units on each side
	// (text takes a little more space). The coordinates you specify here are
	// used as the center of the host cube.
}


 