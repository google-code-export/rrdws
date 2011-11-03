package cc.co.llabor.threshold.rrd.update;

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
	//!!! HERE IS THE GENERAL CHECK!!!!  toCheck.getMonitorArgs()
	//  !----- -10--- -9-----...------0-----1----2 F(t) ------3------5-----11 ..---!
	//                >!<---------(lowLimit)
	/// >>--- alert -->! 
	@Override
	public boolean checkIncident(double val, long timestamp) {
		return (
				val <  this.getBaseLine()  
			);
	}
  
 
}


 