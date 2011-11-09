package cc.co.llabor.threshold.syso;
 
import java.util.Date; 
import java.util.Properties; 

import cc.co.llabor.threshold.AbstractActionist;
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
public class StdOutActionist extends AbstractActionist{  
	public StdOutActionist(String rrdName, String monitorArgs, 	long notificationInterval) {
		super(rrdName,   monitorArgs, 	  notificationInterval);
	}
	public StdOutActionist(Properties props) {
		super(props);
		init(props); 
	}	

	@Override
	protected void init(Properties props) 	{  
		try{
			this.action =props.getProperty(Threshold.ACTION ) ;
		}catch(Exception e){}try{
			this.actionArgs =props.getProperty(Threshold.ACTION_ARGS ) ;
		}catch(Exception e){}try{
			this.rrdName =props.getProperty(Threshold.DATASOURCE ) ;
		}catch(Exception e){}try{
			this.dsName =props.getProperty(Threshold.DS_NAME ) ;
		}catch(Exception e){}try{
			this.monitorType =props.getProperty(Threshold.MONITOR_TYPE ) ;
		}catch(Exception e){}try{
			this.monitorArgs =props.getProperty(Threshold.MONITOR_ARGS ) ;
		}catch(Exception e){}try{
			this.activationTimeoutInSeconds = Integer.parseInt( props.getProperty(Threshold.SPAN_LENGTH  ));
		}catch(Exception e){}try{
			this.baseLine = Double.parseDouble(  props.getProperty(Threshold.BASE_LINE ));
		}catch(Exception e){}
		this.dsName = "speed";
		this.action =  "syso";

		this.monitorType =  "mvel";
		this.actionArgs =  "HELLO STDOUT"; 
	}		
 
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -1411482451587802533L;
 
 	@Override
	protected void act(long timestampSec) {
				System.out.println(actionArgs +"N" +"Z"+new Date(timestampSec*1000));
				//getNotificationCounter();
				beep();
				
	}
}


 