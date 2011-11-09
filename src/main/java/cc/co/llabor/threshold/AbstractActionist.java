package cc.co.llabor.threshold;

import java.io.IOException;  
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.mvel2.MVEL; 
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
	protected String monitorType = "mvel";

	private volatile long lastNotificationTimestamp = -1;
	private volatile int notificationCounter = 0;
	private volatile long notificationIntervalInSecs = 10 *60; // 10min ..1000 *
	
	/**
	 * 
	 * TODO - remove
	 * 
	 * 
	 * 
	 * @author vipup
	 * 
	 * @deprecated
	 * 
	 */
	public final   void beep(){
		notificationCounter++;
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
	public void performSleep(long timestamp) { 
		// do nothing
	} 

 	@Override
	public void performAction(long timestampSec) { 
		if (inIncidentTime()>=0)
		if (timestampSec >this.lastNotificationTimestamp)	
		{
			this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
			act(timestampSec);
		}
	}

	protected abstract void act(long timestampSec);

	@Override
	public void stop() {
		 // just do nothig by default
	} 
	
	@Override
	protected boolean checkIncident(double val, long timestamp) {
		
		String monitorType = this.getMonitorType();
		if ("mvel".equals( monitorType)){
			String expression = this.getMonitorArgs();//["speed"]rrd.dsNames[0]
			Map<String , Object> ctx = new HashMap<String, Object>() ;
			try {
				RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(this.rrdName );
				ctx.put("rrd", rrd);
				ctx.put("val", val); // TODO still not used
				ctx.put("timestamp", timestamp);// TODO still not used
				ctx.put("this", this);// TODO still not used
				ctx.put("dvalue", rrd.getLastDatasourceValues()[0]);//rrd.lastDatasourceValues[0]
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RrdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Object result = null;
			try{
				result  = MVEL.eval(expression , ctx );
			}catch(Throwable e){
				// ignore any error
			}
			boolean retval = false;
			if ((result instanceof Boolean)){
				retval  =  ((Boolean)result).booleanValue();
			}
			return retval;				
		}else{
			if (1==2)throw new RuntimeException("unknown monitorType:"+monitorType);
			else return false;
		}
	} 
}


 