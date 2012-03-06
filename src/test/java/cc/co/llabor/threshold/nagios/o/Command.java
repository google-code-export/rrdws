package cc.co.llabor.threshold.nagios.o;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.02.2012::17:58:02<br> 
 */
public class Command {
	// command_name: This directive is the short name used to identify the
	// command. It is referenced in contact, host, and service definitions (in
	// notification, check, and event handler directives), among other places.
	String name;
	// command_line:
	String commandline;
	// This directive is used to define what is actually executed by Nagios when
	// the command is used for service or host checks, notifications, or event
	// handlers. Before the command line is executed, all valid macros are
	// replaced with their respective values. See the documentation on macros
	// for determining when you can use different macros. Note that the command
	// line is not surrounded in quotes. Also, if you want to pass a dollar sign
	// ($) on the command line, you have to escape it with another dollar sign.
	//
	// NOTE: You may not include a semicolon (;) in the command_line directive,
	// because everything after it will be ignored as a config file comment. You
	// can work around this limitation by setting one of the $USER$ macros in
	// your resource file to a semicolon and then referencing the appropriate
	// $USER$ macro in the command_line directive in place of the semicolon.
	//
	// If you want to pass arguments to commands during runtime, you can use
	// $ARGn$ macros in the command_line directive of the command definition and
	// then separate individual arguments from the command name (and from each
	// other) using bang (!) characters in the object definition directive (host
	// check command, service event handler command, etc) that references the
	// command. More information on how arguments in command definitions are
	// processed during runtime can be found in the documentation on macros.
}


 