package cc.co.llabor.threshold.rrd;

import java.io.Serializable;
import java.util.Properties;
 
/** 
 * <b>refactored from http://cricket.sourceforge.net/support/doc/monitor-thresholds.html </b>
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
public interface Threshold extends Serializable{
	String CLASS = "class";
	String BASE_LINE = "baseLine";
	String SPAN_LENGTH = "spanLength";
	String MONITOR_ARGS = "monitorArgs";
	String MONITOR_TYPE = "monitorType";
	String DS_NAME = "dsName";
	String DATASOURCE = "datasource";
	String ACTION_ARGS = "actionArgs";
	String ACTION = "action";	
	
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
	
	
	/** 
	 * <monitor type> := One of the six supported types: exact, value, relation, hunt, quotient, or failures. 
	 *  Case insensitive.
	 * 
	 * @author vipup
	 * @return
	 */
	public String getMonitorType();
	
	
	
	/**
	 * <monitor type args> := a colon-delimited list of arguments specific to each monitor type. Case sensitive
	 */
	public String getMonitorArgs();
	
	
	/** 
	 * <ACTION> := One of six supported actions: SNMP, MAIL, EXEC, FUNC, META or FILE. Case insensitive. ???
	 * 
	 * TODO - currently only STDOUT implemented...
	 * 
	 * TODO ??? log4j (compliant for most of cases like JMS, file, SMS, IM, MQ, EMAIL, e.t.c ), STDOUT, FILE, EXEC, SNMP, JCRONTAB ?? 
	 * 
	 * @author vipup
	 * @return
	 */
	public String getAction();
	
	
	/**
	 * 	<action args> := a colon-delimited list of arguments specific to each action. Case sensitive in most cases.
	 */
	public String getActionArgs();

	/** 
	 * performAction	-  perform action from itself
	 * Will be called for any case, when Monitor is triggered as Active :
	 * 	- Monitor.inIncident AND 
	 * 	- SpanLength + CureentTime > LastPerformedTime 
	 * 	 
	 */
	public void performAction(long timestampSec);
	/** 
	 * amost void implementation. Will be called for any case, when Monitor is passive AND update action was performed.
	 * 	
	 * @author vipup
	 * @param timestamp
	 */
	public void performSleep(long timestamp); 	
	
	/**
	 * !! a number of seconds a thresholds should fail before triggering an
	 * action.
	 */
	public long getSpanLength();
	

	/**
	 * will be called at destroy-time 
	 * @author vipup
	 */
	public void stop();


	public Properties toProperties();


	/**
	 * dataSourcaName
	 * @author vipup
	 * @return
	 */
	public String getDsName();


	double getBaseLine();


	//public void performChunk(long timestamp, double val);

 


}


 