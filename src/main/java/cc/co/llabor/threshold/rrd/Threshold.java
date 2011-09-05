package cc.co.llabor.threshold.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  23.05.2011::14:06:42<br> 
 * 
 * Syntax

monitor-thresholds = "<monitor-threshold> [, <monitor-threshold> ...]"

 */
public interface Threshold {
//	<data source> := a data source defined for the target; Case sensitive.
	public String getDatasource();
//	<monitor type> := One of the six supported types: exact, value, relation, hunt, quotient, or failures. Case insensitive.
	public String getMonitorType();
// 	<monitor type args> := a colon-delimited list of arguments specific to each monitor type. Case sensitive
	public String getMonitorArgs();
//	<ACTION> := One of six supported actions: SNMP, MAIL, EXEC, FUNC, META or FILE. Case insensitive.
	public String getAction();
//	<action args> := a colon-delimited list of arguments specific to each action. Case sensitive in most cases.
	public String getActionArgs();
// performAction	-  perform action from itself
// TODO altertate for 2 prev-methods???
	public void performAction(long timestampSec);
// same with prev, just without AlertAction... ??	
	public void performSleep(long timestamp); 	
	
//	<SPAN> := Spanning keyword: SPAN. Case insensitive.
//	@deprecated ???<span-length> := Number of time spans a thresholds should fail before triggering an action. Number.
//	!! a number of seconds a thresholds should fail before triggering an action.
	public long getSpanLength(); 
	
	// return the time since the Threshold goes into its limit-incident ( limit is reached ) OR -1 if not the case.
	// @see getSpanLength()
	public long inIncidentTime();	

	// Start incident....
	public void incident(long timestampSec);
	// clear incident 
	public void clear(long timestampSec); 
	void checkIncident(double val, long timestamp); 
	void reactIncidentIfAny(long timestamp);
	public void stop();


}


 