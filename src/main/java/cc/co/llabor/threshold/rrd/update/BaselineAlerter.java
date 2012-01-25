package cc.co.llabor.threshold.rrd.update;
  

import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.08.2011::22:15:56<br> 
 */
public class BaselineAlerter extends RddUpdateAlerter implements Threshold {

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 1792920230094305597L;
	private double gap;

	public double getGap() { 
			return gap;
	}
 
	public BaselineAlerter(String rrdName, double baseLine, double gap,  long activationTimeoutInSeconds) {
		super(rrdName, baseLine,activationTimeoutInSeconds );
		this.gap = gap; 
	}	


	@Override
	public boolean checkIncident(double val, long timestamp) {
//	} //BaselineAlerter
	//  !----- -10--- -9-----...------0-----1----2 F(t) ------3------5-----11 ..---!
	//                (Baseline)-------->!<
	//                (gap)-------->!<==>!<==>! 
	/// >>------ alert ------------>!         !<--------------- alert ------------<<
	
	//else if (toCheck instanceof BaselineAlerter){
//		BaselineAlerter baselineAlerter = (BaselineAlerter)toCheck;
//		if (
//			val >  baselineAlerter.getBaseLine() +baselineAlerter.getDelta()
//			||
//			val <  baselineAlerter.getBaseLine() -baselineAlerter.getDelta()
//		){
//			toCheck.incident(charlieTmp.timestamp);
//		}else{
//			toCheck.clear();
//		}

		return (
			val >  this.getBaseLine() +this.getGap()
			||
			val <  this.getBaseLine() -this.getGap()
		);
	}




	
}


 