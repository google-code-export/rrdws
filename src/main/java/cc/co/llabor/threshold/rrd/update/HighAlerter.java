package cc.co.llabor.threshold.rrd.update;
 
import java.util.Properties;

import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  30.08.2011::15:46:23<br> 
 */
public class HighAlerter extends RddUpdateAlerter implements Threshold {

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 1424196870196989214L;
	
	
	public HighAlerter(Properties props){
		super(props);
	}
	
	public HighAlerter(String rrdName, double baseLine,
			long activationTimeoutInSeconds) {
		super(rrdName, baseLine, activationTimeoutInSeconds);
		 
	}
	//  !----- -10--- -9-----...------0-----1----2 F(t) ------3------5-----11 ..---!
	//                (HiLimit)---------------->!<
	///                                         !<--- alert ----------------------<< 
	@Override
	public boolean checkIncident(double val, long timestamp) {
		return (val > this.getBaseLine())  ;
	}
 
  

}


 