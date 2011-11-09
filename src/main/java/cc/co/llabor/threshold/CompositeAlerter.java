package cc.co.llabor.threshold;

import java.util.ArrayList;
import java.util.List; 
import java.util.Properties;

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
	private static final String PREFIX_STRING = ".";
	

	protected String aList[]=null;
	protected List<Threshold> chainOfAlerters = new ArrayList<Threshold>();
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
	 * the compisite-alerter check only the condition of FIRST, and perform action of ALL
	 */
	@Override
	public String toString(){
		String dsName = this.getDsName();
		String datasource2 = this.getDatasource();
		String monitorType = this.chainOfAlerters.get(0).getMonitorType();
		String monitorArgs = this.chainOfAlerters.get(0).getMonitorArgs();
		monitorArgs = monitorArgs==null?"":monitorArgs;
		monitorArgs = monitorArgs.replace("${baseLine}", ""+this.chainOfAlerters.get(0).getBaseLine());
		
		String action2 = "";//this.getAction();// this.chainOfAlerters.get(chainOfAlerters.size()-1).getAction();
		for (Threshold thTmp:this.chainOfAlerters){
			action2 +=thTmp.getAction();
			action2 = action2 +" ("+thTmp.getActionArgs()+")";
			action2+=";";
		}
		String actionArgs = "";//this.getActionArgs();//this.chainOfAlerters.get(chainOfAlerters.size()-1).getActionArgs();
		return dsName + ":" + 
				datasource2 + 
				"@" + 
				monitorType + 
				"://" + 
				monitorArgs + 
				"?" + 
				action2 + 
				" ( " + 
				actionArgs + 
				" )";
	}	
	protected void init (Properties props){
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
	}
	
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -3139647543813110512L;

	// mark properties with non-chained behavior
	private static final boolean FIRSTONLY = true;

	protected void agregate(Threshold theT){
		chainOfAlerters.add(theT);
	}
	@Override
	public String getDsName() {
		String retval=null;
		for (Threshold theT:chainOfAlerters){
			 retval = ((AbstractAlerter)theT).getDsName() ;
			 if (FIRSTONLY)break;
		}		
		return retval;
	}	
	@Override
	public String getDatasource() {
		String retval=null;
		for (Threshold theT:chainOfAlerters){
			 retval = ((AbstractAlerter)theT).getDatasource() ;
			 if (FIRSTONLY)break;
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
	
	


	

	/**
	 * something like Cpu_Load
	 */
	@Override
	public String getMonitorType() {
		String retval=null;
		int i=0;
		for (Threshold theT:chainOfAlerters){
			retval = retval==null?"":retval;
			retval += PREFIX_STRING+i+"={" ;
			retval += theT.getMonitorType();
			retval += "};";
			i++;
			if (FIRSTONLY)break;
		}		
		return retval;
	}
 
 
	@Override
	/**
	 * ...and _Average:1Hour _?
	 */
	public String getMonitorArgs() {
		String retval=null;
		int i=0;
		for (Threshold theT:chainOfAlerters){
			retval = retval==null?"":retval;
			String monitorArgs2 = theT.getMonitorArgs();
			if (monitorArgs2 != null) {
				retval += PREFIX_STRING + i + "={";
				retval += monitorArgs2;
				retval += "};";
			}
			i++;
			if (FIRSTONLY)break;
		}		
		return retval;
	}

	
	@Override
	/**
	 * shell://kill
	 */
	public String getAction() {
		String retval=null;
		int i=0;
		for (Threshold theT:chainOfAlerters){
			retval = retval==null?"":retval;
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
		String retval=null;
		int i=0;
		for (Threshold theT:chainOfAlerters){
			retval = retval==null?"":retval;
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
	@Override
	public boolean equals(Object obj) { 
		if (obj!=null && obj instanceof CompositeAlerter){
			CompositeAlerter o = (CompositeAlerter)obj;
			return
			o.rrdName == this.rrdName  &&
			o.getClass().getName() == this.getClass().getName()   &&
			o.aList.length == this.aList.length;				
		}
		else {
			return super.equals(obj);
		}
	}
	
	
	
//	@Override
//	public String toString() {
//		String retval="";
//		retval+=
//		for (Threshold theT:chainOfAlerters){
// 			retval = theT.getBaseLine();
//			break;
//		}		
//		return retval;  
//	}
}


 