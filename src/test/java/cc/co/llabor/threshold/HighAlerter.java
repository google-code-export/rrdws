package cc.co.llabor.threshold;

import java.io.IOException;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;

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


 