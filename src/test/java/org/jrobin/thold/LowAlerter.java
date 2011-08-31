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
public class LowAlerter extends HighAlerter implements Threshold {

	public LowAlerter(String rrdName, double lowLimit,
			long activationTimeoutInSeconds) {
		super(rrdName, lowLimit, activationTimeoutInSeconds);
		 
	}

	public double getLowLimit() { 
		return super.getHiLimit();
	}
	public double getHiLimit() {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 31.08.2011");
		else {
		return 0;
		}
	}
 
}


 