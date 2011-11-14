package cc.co.llabor.threshold;

import java.util.Properties;

import cc.co.llabor.threshold.rrd.Threshold;
import cc.co.llabor.threshold.rrd.update.HighAlerter; 

/**
 * <b>combine n-od-MVEL- and aalways RRDUpdate actionist</b>
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 02.11.2011::22:26:58<br>
 */
public class Always2RRDActionist extends CompositeAlerter {

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -7607540912607278569L;

	public Always2RRDActionist(Properties props) {
		init(props);
	}
	/**
	 * hardcoded Constructor for make composite Thresould A, which delegate
	 * activity to two sub-Actionists:
	 * 
	 * - HighAlerter - Log4JActionist (the class of second logger-pointed
	 * actionist comes from param)
	 * 
	 * @author vipup
	 * @param clPar
	 * @param rrdName
	 * @param monitorArgs
	 * @param notificationInterval
	 */
	public Always2RRDActionist(MVELActionist clPar, String rrdName,
			String monitorArgs, int notificationInterval) {
		super();
		String chTmp = storeRRDWRITER(rrdName);
		Properties props = new Properties();
		props.put(Threshold.DS_NAME, "data");
		props.put(Threshold.MONITOR_ARGS, monitorArgs);
		props.put(Threshold.SPAN_LENGTH, "" + notificationInterval);

		Properties myProps = new Properties();
		myProps.put("childs", chTmp);
		myProps.put(Threshold.DATASOURCE, rrdName);
		init(myProps);
	}
	@Override
	public Properties toProperties() {
		Properties retval = super.toProperties();
		String value = "";
		for (String child : this.aList) {
			value = value.length() == 0 ? "" : value + ",";;
			value += child;
		}
		// this.aList = props.getProperty("childs").split(",");
		retval.put("childs", value);
		return retval;
	}

	private String storeRRDWRITER(String rrdName) {
		String retval = "rrw@" + rrdName;// "redalert.RRDWRITER";//"redalert.RRDWRITER"
		Properties props4RRDWRITER = new Properties();
		props4RRDWRITER.put(Threshold.DS_NAME, "speed");
		props4RRDWRITER.put(Threshold.DATASOURCE, rrdName);
		props4RRDWRITER.put(Threshold.SPAN_LENGTH, "600");
		props4RRDWRITER.put(Threshold.BASE_LINE, "130");
		props4RRDWRITER.put(Threshold.CLASS, HighAlerter.class.getName());
		Threshold rwTmp;
		try {
			rwTmp = AlertCaptain.toThreshold(props4RRDWRITER);

			AlertCaptain.storeToName(retval, rwTmp);
		} catch (TholdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}
	protected void init(Properties props) {
		System.out.println(props);
		super.init(props);
		try {
			this.aList = props.getProperty("childs").split(",");
			for (String namePar : this.aList) {

				Threshold theNext;
				try {
					theNext = AlertCaptain.restoreByName(namePar);
					this.rrdName = this.rrdName == null ? theNext
							.getDatasource() : this.rrdName;
					this.chainOfAlerters.add(theNext);
					AlertCaptain instance = AlertCaptain.getInstance();
					instance.register(theNext);
				} catch (TholdException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
		}
		storeRRDWRITER(this.rrdName);

	}

}
