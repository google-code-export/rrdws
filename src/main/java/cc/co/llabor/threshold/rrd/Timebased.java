package cc.co.llabor.threshold.rrd;
/** 
 * <b>Description:		'time_header' => array(
			'friendly_name' => 'Time Based Settings',
			'method' => 'spacer',
		)</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  23.05.2011::14:21:14<br> 
 */
public class Timebased {
	// If set and data source value goes above this number, alert will be triggered',
	long time_hi ;
	//'If set and data source value goes below this number, alert will be triggered',
	long  time_low ; 
	// The number of times the data source must be in breach of the threshold.',
	long  time_fail_trigger;
	//The amount of time in the past to check for threshold breaches.',
	long time_fail_length;    

}


 