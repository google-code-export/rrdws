package org.jrobin.thold;

import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.08.2011::21:33:50<br> 
 */
public class LowAlerter extends RddUpdateAlerter implements Threshold {

	public LowAlerter(String rrdName, double lowLimit,
			long activationTimeoutInSeconds) {
		super(rrdName, lowLimit, activationTimeoutInSeconds);
		 
	}
 
	@Override
	public void checkIncident(double val, long timestamp) {
		if (
				val <  this.getBaseLine()  
			){
					this.incident(timestamp);
				}else{
					this.clear();
		}
	}
  
 
}


 