package cc.co.llabor.threshold;

import java.util.ArrayList;
import java.util.List; 

import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  02.11.2011::22:50:09<br> 
 */
public abstract class CompositeAlerter extends AbstractActionist /*implements List<Threshold>*/{
	private static final String PREFIX_STRING = "l";

	protected List<Threshold> chainOfAlerters = new ArrayList<Threshold>();
	
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -3139647543813110512L;

	protected void agregate(Threshold theT){
		chainOfAlerters.add(theT);
	}
	@Override
	public String getDsName() {
		String retval=null;
		for (Threshold theT:chainOfAlerters){
			 retval = ((AbstractAlerter)theT).getDsName() ;
			 break;
		}		
		return retval;
	}	
 	@Override
	public void performAction(long timestampSec) {
		for (Threshold theT:chainOfAlerters){
			theT.performAction(timestampSec);
		}
	}

	@Override
	public void performSleep(long timestamp) {
		for (Threshold theT:chainOfAlerters){
			theT.performSleep(timestamp);
		}
	}

	@Override
	public void stop() {
		for (Threshold theT:chainOfAlerters){
			theT.stop( );
		}
	}
	
	

	@Override
	/**
	 * will check only the first
	 */
	protected boolean checkIncident(double val, long timestamp) {
		boolean retval=false;
		for (Threshold theT:chainOfAlerters){
			 retval = ((AbstractAlerter)theT).checkIncident(val,   timestamp);
			 break;
		}		
		return retval;
	}	
	

	/**
	 * something like Cpu_Load
	 */
	@Override
	public String getMonitorType() {
		String retval="";
		int i=0;
		for (Threshold theT:chainOfAlerters){
			retval += PREFIX_STRING+i+"={" ;
			retval += theT.getClass().getName();
			retval += "};";
			i++;
		}		
		return retval;
	}
 
 
	@Override
	/**
	 * ...and _Average:1Hour _?
	 */
	public String getMonitorArgs() {
		String retval="";
		int i=0;
		for (Threshold theT:chainOfAlerters){
			String monitorArgs2 = theT.getMonitorArgs();
			if (monitorArgs2 != null) {
				retval += PREFIX_STRING + i + "={";

				retval += monitorArgs2;
				retval += "};";
			}
			i++;
		}		
		return retval;
	}

	
	@Override
	/**
	 * shell://kill
	 */
	public String getAction() {
		String retval="";
		int i=0;
		for (Threshold theT:chainOfAlerters){
			String action2 = theT.getAction();
			if (action2 !=null){
				retval += PREFIX_STRING+i+"={" ;
				
				retval += action2;
				retval += "};";
			}
			i++;
		}		
		return retval;
	}

	@Override
	/**
	 * pid
	 */
	public String getActionArgs() {
		String retval="";
		int i=0;
		for (Threshold theT:chainOfAlerters){

			String actionArgs2 = theT.getActionArgs();
			if (actionArgs2 !=null){
			retval += PREFIX_STRING+i+"={" ;
			retval += actionArgs2;
			retval += "};";
			}
			
			i++;
		}		
		return retval;
	}	
	
	@Override
	public double getBaseLine() {
		double retval=Double.NaN; 
		for (Threshold theT:chainOfAlerters){
 			retval = theT.getBaseLine();
			break;
		}		
		return retval;  
	}
}


 