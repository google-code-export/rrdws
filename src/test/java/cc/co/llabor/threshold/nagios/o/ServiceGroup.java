package cc.co.llabor.threshold.nagios.o;

import java.util.List;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  21.02.2012::09:46:15<br> 
 */
public class ServiceGroup {
	// servicegroup_name: This directive is used to define a short name used to
	// identify the service group.
	// alias: This directive is used to define is a longer name or description
	// used to identify the service group. It is provided in order to allow you
	// to more easily identify a particular service group.
	String name;
	// members:
	// This is a list of the descriptions of services (and the names of their
	// corresponding hosts) that should be included in this group. Host and
	// service names should be separated by commas. This directive may be used
	// as an alternative to the servicegroups directive in service definitions.
	// The format of the member directive is as follows (note that a host name
	// must precede a service name/description):
	//
	// members=<host1>,<service1>,<host2>,<service2>,...,<hostn>,<servicen>
	// servicegroup_members: This optional directive can be used to include
	// services from other "sub" service groups in this service group. Specify a
	// comma-delimited list of short names of other service groups whose members
	// should be included in this group.
	List<Service> members;
	// notes: This directive is used to define an optional string of notes
	// pertaining to the service group. If you specify a note here, you will see
	// the it in the extended information CGI (when you are viewing information
	// about the specified service group).
	String notes;
	// notes_url: This directive is used to define an optional URL that can be
	// used to provide more information about the service group. If you specify
	// an URL, you will see a red folder icon in the CGIs (when you are viewing
	// service group information) that links to the URL you specify here. Any
	// valid URL can be used. If you plan on using relative paths, the base path
	// will the the same as what is used to access the CGIs (i.e.
	// /cgi-bin/nagios/). This can be very useful if you want to make detailed
	// information on the service group, emergency contact methods, etc.
	// available to other support staff.
	String notes_url;
	// action_url: This directive is used to define an optional URL that can be
	// used to provide more actions to be performed on the service group. If you
	// specify an URL, you will see a red "splat" icon in the CGIs (when you are
	// viewing service group information) that links to the URL you specify
	// here. Any valid URL can be used. If you plan on using relative paths, the
	// base path will the the same as what is used to access the CGIs (i.e.
	// /cgi-bin/nagios/).
	String action_url;
}


 