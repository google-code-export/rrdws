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
public abstract class MVELActionist extends AbstractActionist { 
	{
		this.monitorType= "mvel";
	}

	
	public MVELActionist(Properties props){ 
		init(props);
	}	 	
	
	private static final Properties defaultProps = new Properties();
	static {
		defaultProps .setProperty(Threshold.DATASOURCE, "test.rrd"); 
	}
	
	public MVELActionist( ){  
		init(defaultProps );
	}	
	public MVELActionist(String rrdName, String monitorArgs, 	long notificationInterval) {
		this.rrdName = 	rrdName;	//			RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdName ); 
		this.monitorArgs  =  monitorArgs ;
		this.notificationIntervalInSecs = notificationInterval;
		this.setDsName  ("speed"); 
		defaultProps.setProperty(Threshold.DATASOURCE , rrdName);
		defaultProps.setProperty(Threshold.MONITOR_ARGS , monitorArgs);
		defaultProps.setProperty(Threshold.BASE_LINE , ""+baseLine); 
		defaultProps.setProperty(Threshold.SPAN_LENGTH , ""+activationTimeoutInSeconds); 
		init(defaultProps );
	}
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -1411482451587802533L;
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


 