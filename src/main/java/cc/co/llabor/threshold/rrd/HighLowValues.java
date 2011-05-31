package cc.co.llabor.threshold.rrd;
/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 23.05.2011::14:07:14<br>
 */
public class HighLowValues implements Threshold {
	Type type = Type.HighLowValue;
	double HighThreshold;
	double LowThreshold;
	long BreachDurationInSeconds;
	// When enabled, baseline monitoring checks the current data source value
	// against a value in the past. The available range of values is retrieved
	// and a minimum and maximum values are taken as a respective baseline
	// reference. The precedence however is on the &quot;hard&quot; thresholds
	// above.
	boolean bl_enabled;
	// Specifies the relative point in the past that will be used as a
	// reference. The value represents seconds, so for a day you would specify
	// 86400, for a week 604800, etc.',
	long bl_ref_time;
	// Specifies the time range of values in seconds to be taken from the
	// reference in the past',
	long bl_ref_time_range;
	// Specifies allowed deviation in percentage for the upper bound threshold.
	// If not set, upper bound threshold will not be checked at all.',
	double bl_pct_up;
	// Specifies allowed deviation in percentage for the lower bound threshold.
	// If not set, lower bound threshold will not be checked at all.',
	double bl_pct_down;
	// Number of consecutive times the data source must be in breach of the
	// baseline threshold for an alert to be raised.<br>Leave empty to use
	// default value (<b>Default: ' . read_config_option('alert_bl_trigger') . '
	// cycles</b>)',
	int bl_fail_trigger;
}
