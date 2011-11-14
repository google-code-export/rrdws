package cc.co.llabor.threshold;
  
import java.util.Properties;
 
import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>The very first Actions-based implementation of Threshold</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.09.2011::16:37:58<br> 
 */
public abstract class AbstractActionist extends AbstractAlerter { 
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -1411482451587802533L;
	protected void setDsName(String dsName) {
		this.dsName = dsName;
	}

	protected String dsName = "speed";
	protected String actionArgs = null;
	protected String monitorArgs = null;
	protected String action = null;
	protected String monitorType = null;

	private volatile long lastNotificationTimestamp = -1;
	private volatile int notificationCounter = 0;
	protected volatile long notificationIntervalInSecs = -1; // enabled whe positive 
	
	@Override
	protected
	synchronized void reactIncidentIfAny(long timestampSec) {
		
		// for periodical notification only
		if (this.notificationIntervalInSecs>0){ // filtering only for
			if(isInIncident( timestampSec ))
			if (timestampSec >this.lastNotificationTimestamp){
				super.reactIncidentIfAny(timestampSec);
				this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
				notificationCounter++;
			}else{
				// the action will be spressed till the next
			}
			
		}else{ //default mode for unconfigured 
			super.reactIncidentIfAny(timestampSec);
		}
	 }


	
	protected AbstractActionist( ){
//		monitorType = "mvel";
//		init(new Properties());
	}
	
	public AbstractActionist(Properties props){
		init(props);
	}	 
	
	public AbstractActionist(String rrdName, String monitorArgs, 	int notificationInterval) {
		this(rrdName, monitorArgs, 	(long)notificationInterval);
	}
	public AbstractActionist(String rrdName, String monitorArgs, 	long notificationInterval) {
		this.rrdName = 	rrdName;	//			RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdName ); 
		this.monitorArgs  =  monitorArgs ;
		this.notificationIntervalInSecs = notificationInterval;
		this.setDsName  ("speed");
		Properties props = new Properties();
		props.setProperty(Threshold.DATASOURCE , rrdName);
		props.setProperty(Threshold.MONITOR_ARGS , monitorArgs);
		props.setProperty(Threshold.BASE_LINE , ""+baseLine); 
		props.setProperty(Threshold.SPAN_LENGTH , ""+activationTimeoutInSeconds); 
		
		init(props );
	}
	
	public AbstractActionist(String rrdName, double baseLine, long activationTimeoutInSeconds){
		Properties props = new Properties();
		props.setProperty(Threshold.DATASOURCE , rrdName);
		props.setProperty(Threshold.BASE_LINE , ""+baseLine); 
		props.setProperty(Threshold.SPAN_LENGTH , ""+activationTimeoutInSeconds); 
		
		init(props );
	}	
//	public CompositeAlerter(Properties props){
//		super(props);
//		init(props);
//	}
	protected abstract void init(Properties props) ;	
	
	public int getNotificationCounter() { 
			return notificationCounter;
	}
	@Override
	public String getDsName() {
		return this.dsName;
	}	

	/**
	 * something like Cpu_Load
	 */
	@Override
	public String getMonitorType() {
			return monitorType ;
	}
 
	@Override
	/**
	 * ...and _Average:1Hour _?
	 */
	public String getMonitorArgs() { 
		return monitorArgs ;
	}

	
	@Override
	/**
	 * shell://kill
	 */
	public String getAction() { 
			return action ;
	}

	@Override
	/**
	 * pid
	 */
	public String getActionArgs() {
		 	return actionArgs;
	}
 

	@Override
	public void stop() {
		 // just do nothig by default
	} 
}


 