package cc.co.llabor.threshold;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.mvel2.MVEL;

/** 
 * <b>The very first Actions-based implementation of Threshold</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.09.2011::16:37:58<br> 
 */
public class StdOutActionist extends AbstractAlerter {

	

	
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -1411482451587802533L;
	protected String dsName = "speed";
	
	StdOutActionist(Properties props){
		this.action =props.getProperty("action") ;
		this.actionArgs =props.getProperty("actionArgs") ;
		this.rrdName =props.getProperty("datasource") ;
		this.dsName =props.getProperty("dsName") ;
		this.type =props.getProperty("monitorType") ;
		this.monitorArgs =props.getProperty("monitorArgs") ;
		this.activationTimeoutInSeconds = Integer.parseInt( props.getProperty("spanLength" ));
		this.baseLine = Double.parseDouble(  props.getProperty("BaseLine")); 		
	}	

	protected String actionArgs = "HELLO STDOUT";
	long lastNotificationTimestamp = -1;
	long notificationIntervalInSecs = 10 *60; // 10min ..1000 *
	int notificationCounter = 0;
	protected String monitorArgs = "a=1;b=2";
	protected String action = "syso";
	protected String type = "mvel";
	public StdOutActionist(String rrdName, double baseLine, double delta,
			long notificationInterval) {
		this.rrdName = 	rrdName;	//			RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdName ); 
		monitorArgs  = "1==2";
		this.notificationIntervalInSecs = notificationInterval;
	}
	public StdOutActionist(String rrdName, String monitorArgs,
			long notificationInterval) {
		this.rrdName = 	rrdName;	//			RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdName ); 

		this.monitorArgs  =  monitorArgs ;
		this.notificationIntervalInSecs = notificationInterval;
	}
	public int getNotificationCounter() { 
			return notificationCounter;
	}

 	@Override
	public void performAction(long timestampSec) {
		
		if (inIncidentTime()>=0)
		if (timestampSec >this.lastNotificationTimestamp)	
		{
			this.lastNotificationTimestamp = this.notificationIntervalInSecs +timestampSec ;
			String action = this.getAction();
			if ("syso".equals(action)){
				System.out.println(actionArgs +"N"+(notificationCounter++)+"Z"+new Date(timestampSec*1000));
			}else{
				String message = "unknown Action:"+action;
				throw new RuntimeException(message );
			}
		}
		
		
	}
	/**
	 * something like Cpu_Load
	 */
	@Override
	public String getMonitorType() {
			return type ;
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
	public void reactIncidentIfAny(long timestamp) {
		long inIncidentTime = this.inIncidentTime();
		long spanLength = this.getSpanLength();

		if (inIncidentTime >= 0 && (inIncidentTime + spanLength) < timestamp) {
			this.performAction(timestamp);
		} else {
			this.performSleep(timestamp);
		}
	}

	
	@Override
	public boolean checkIncident(double val, long timestamp) {
		
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
			throw new RuntimeException("unknown monitorType:"+monitorType);
		}
	}
	@Override
	public void stop() {
		System.out.println("BYE");
	}
	@Override
	public String getDsName() {
		return this.dsName;
	}	
}


 