package cc.co.llabor.threshold.nagios.o;

import java.util.List;

/** 
 * <b>http://nagios.sourceforge.net/docs/3_0/objectdefinitions.html#contactgroup</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.02.2012::17:38:40<br> 
 */
public class ContactGroup {
	// Directive Descriptions:contactgroup_name: This directive is a short name
	// used to identify the contact group.
	// alias: This directive is used to define a longer name or description used
	// to identify the contact group.
	String alias;
	// members: This optional directive is used to define a list of the short
	// names of contacts that should be included in this group. Multiple contact
	// names should be separated by commas. This directive may be used as an
	// alternative to (or in addition to) using the contactgroups directive in
	// contact definitions.
	List<Contact> contacts;
	// contactgroup_members: This optional directive can be used to include
	// contacts from other "sub" contact groups in this contact group. Specify a
	// comma-delimited list of short names of other contact groups whose members
	// should be included in this group.
	List<ContactGroup> contactgroups;
}


 