package cc.co.llabor.threshold;

import java.util.Properties;

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
	
	void _nevercalledM(){
		Properties props = new Properties();
		this.action =props.getProperty(Threshold.ACTION ) ;
		this.actionArgs =props.getProperty(Threshold.ACTION_ARGS ) ;
		this.rrdName =props.getProperty(Threshold.DATASOURCE ) ;
		this.dsName =props.getProperty(Threshold.DS_NAME ) ;
		this.type =props.getProperty(Threshold.MONITOR_TYPE ) ;
		this.monitorArgs =props.getProperty(Threshold.MONITOR_ARGS ) ;
		this.activationTimeoutInSeconds = Integer.parseInt( props.getProperty(Threshold.SPAN_LENGTH  ));
		this.baseLine = Double.parseDouble(  props.getProperty(Threshold.BASE_LINE ));
		 
	}
	
	protected void init(Properties props) {
		// #1
		double hiLimit = 130; // should be smart enough ;)
		long tenSecondds = 11 ; // 10 sec is maximal time to start to do
									// something...
		Threshold rrdWriter = null;
		int caseId = -1; 
		switch (caseId ){
			// TODO 
			default:
				rrdWriter = new HighAlerter(rrdName, hiLimit, tenSecondds);
		} 
		this.agregate(rrdWriter);
		// #2 
		Threshold stdOutNotificator = new Log4JActionist(props);
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

	@Override
	protected void act(long timestampSec) {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 04.11.2011");
		else {
		}
	}  
}


 