package cc.co.llabor.threshold.rrd;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;

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
	/** 	
	 * unique ID for RRD-Db, formerly RRD-name, that used for retrieve it via 
	 * 		String rrdDef = toCheck.getDatasource();
	 *  	...
	 *      RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdDef );
	 *      ...
	 * 
	 * @author vipup
	 * @return
	 */
	public String getDatasource();
	/** 	<monitor type> := One of the six supported types: exact, value, relation, hunt, quotient, or failures. Case insensitive.
	 * 
	 * @author vipup
	 * @return
	 */
	public String getMonitorType();
	/**
	 * // 	<monitor type args> := a colon-delimited list of arguments specific to each monitor type. Case sensitive
	 */
	public String getMonitorArgs();
	/** //	<ACTION> := One of six supported actions: SNMP, MAIL, EXEC, FUNC, META or FILE. Case insensitive.
	 * 
	 * @author vipup
	 * @return
	 */
	public String getAction();
	/**
	 * //	<action args> := a colon-delimited list of arguments specific to each action. Case sensitive in most cases.
	 */
	public String getActionArgs();

	/** 
	 * performAction	-  perform action from itself
	 * same with prev, just without AlertAction... ??
	 * altertate for 2 prev-methods???
	 * 	 */
	public void performAction(long timestampSec);
	/** 
	 * same with performAction, just without AlertAction... ??
	 * 	
	 * @author vipup
	 * @param timestamp
	 */
	public void performSleep(long timestamp); 	
	
//	<SPAN> := Spanning keyword: SPAN. Case insensitive.
//	@deprecated ???<span-length> := Number of time spans a thresholds should fail before triggering an action. Number.
	/**
	 * !! a number of seconds a thresholds should fail before triggering an
	 * action.
	 */
	public long getSpanLength();
	
	/**
	 * return the time since the Threshold goes into its 
	 * limit-incident ( limit is reached ) 
	 * OR -1 if not the case.
	 * @see getSpanLength()
 	 * @author vipup
	 * @return
	 */
	public long inIncidentTime();	
	/**
	 * Start incident....
	 * mark it as Activated-Threshold
	 * 
	 * @author vipup
	 * @param timestampSec
	 */
	public void incident(long timestampSec);
	/**
	 * clear incident - mark it as inactive(passive)
	 * @author vipup
	 * @param timestampSec
	 */
	public void clear(long timestampSec);
	/**
	 * will be called for any update-action
	 * (pre-action for reactIncidentIfAny)
	 * 
	 * the value ahve to be cheched for triggering the Threshold
	 */
	public boolean checkIncident(double val, long timestamp);
	/**
	 * have to implement alert-reaction for incident-state
	 * @author vipup
	 * @param timestamp
	 */
	public void reactIncidentIfAny(long timestamp);
	/**
	 * will be called at destroy-time 
	 * @author vipup
	 */
	public void stop();


}


 