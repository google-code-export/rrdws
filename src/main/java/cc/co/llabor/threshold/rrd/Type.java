package cc.co.llabor.threshold.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  23.05.2011::13:50:33<br> 
 */
public enum Type {
	/**
	[ 229 ]
	  In this section, you can also change the Threshold Type which can be one of the following:
	  High / Low Values: The threshold is breached if the value is above or below
	  these numbers.
	  Baseline: A time range from the past is used to calculate acceptable minimum and
	  maximum values. The threshold is breached if the values deviate by this amount,
	  in percent.
	  Time Based: Similar to the high/low threshold, a time-based threshold is defined
	  by setting high and low numbers. In order to trigger the threshold, it must be
	  breached x number of times within the last y minutes (for example, 2 times within
	  the last 30 minutes).
	  */
	HighLowValue, 
	Baseline,
	TimeBased; 
}


 