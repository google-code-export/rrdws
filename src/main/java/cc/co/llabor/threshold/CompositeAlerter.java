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
public abstract class CompositeAlerter extends AbstractAlerter /*implements List<Threshold>*/{
	protected List<Threshold> chainOfAlerters = new ArrayList<Threshold>();
	
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -3139647543813110512L;

	protected void agregate(Threshold theT){
		chainOfAlerters.add(theT);
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
			retval += "#"+i+"={" ;
			retval += theT.getMonitorType();
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
			retval += "#"+i+"={" ;
			retval += theT.getMonitorArgs();
			retval += "};";
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
			retval += "#"+i+"={" ;
			retval += theT.getAction();
			retval += "};";
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
			retval += "#"+i+"={" ;
			retval += theT.getActionArgs();
			retval += "};";
			i++;
		}		
		return retval;
	}	
}


 