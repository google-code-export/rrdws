package cc.co.llabor.threshold;

import cc.co.llabor.threshold.log2.Log4JActionist;
import cc.co.llabor.threshold.rrd.Threshold; 
import cc.co.llabor.threshold.rrd.update.HighAlerter;
/** 
 * <b>combine Log- and RRDUpdate actionist</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  02.11.2011::22:26:58<br> 
 */
public class RedAndLogActionist extends CompositeAlerter {

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -7607540912607278569L;
	
	public RedAndLogActionist(String rrdName, double baseLine, long activationTimeoutInSeconds){
		// #1
		double hiLimit = 130; // should be smart enough ;)
		long tenSecondds = 11 ; // 10 sec is maximal time to start to do
									// something...
		Threshold headHunter = new HighAlerter(rrdName, hiLimit, tenSecondds);	
		this.agregate(headHunter);
		// #2
		double delta = 15;
		long tenMins = 10*60;  // repeat alert any 10 mins
		Threshold stdOutNotificator = new Log4JActionist( rrdName, 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (baseLine-delta) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (baseLine+delta) +""+
				")" 
				  , tenMins);
		this.agregate(stdOutNotificator);
	}
	

	@Override
	public String getDsName() {
		String retval=null;
		for (Threshold theT:chainOfAlerters){
			 retval =  theT .getDsName( );// will check only the first
			 break;
		}		
		return retval;
	}  
}


 