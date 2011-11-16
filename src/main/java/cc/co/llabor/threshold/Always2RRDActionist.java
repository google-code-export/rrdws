package cc.co.llabor.threshold;

import java.util.Properties;

import cc.co.llabor.threshold.rrd.Threshold; 

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
	 */
	public Always2RRDActionist(MVELActionist clPar ) {
		super(); 
		String chTmp = storeWrapped(clPar );
		Properties myProps = new Properties();
		myProps.put("childs", chTmp);
		myProps.put(Threshold.DATASOURCE, clPar.getDatasource());
		init(myProps);
	}	



	private String toNICK(String namePar){
		String retval = "";
		String accu = "";
		String theLast = "LeaSt";
		for (String theC :namePar.replace(".", ",").split(",")){
			theLast = theC;
			accu+=theC.substring(0,1).toUpperCase();
		}
		retval = accu;
		accu = "";
// 		Log4JActionist -> AJL4		
//		String abcTmp = "abcdefghijklmnoprstuvwxwz1234567890".toUpperCase();
//		for (int i=0;i<abcTmp .length();i++){
//			String theC = abcTmp .substring(i,i+1);
//			if(theLast.indexOf(theC)>=0){
//				accu+=theC;
//			}
//			
//		}
//		namePar+=accu;

		
// Log4JActionist ->L4JA		
		accu = "";
		String abcTmp = "abcdefghijklmnoprstuvwxwxyz1234567890".toUpperCase();
		for (int i=0;i<theLast .length();i++){
			String theC = theLast.substring(i,i+1);
			if(abcTmp.indexOf(theC)>=0){
				accu+=theC;
			}
			
		}	
		retval +=accu;
		return retval;
	}	
	
	private String storeWrapped(MVELActionist clPar ) {
		String nick = "rrw";
		try{
			nick  = toNICK(clPar.getClass().getName());
		}catch(Throwable e){}
		String retval = nick +"@" + clPar.getDatasource();
		Properties props = new Properties();
		props.put(Threshold.DS_NAME, clPar.getDsName());
		props.put(Threshold.CLASS , clPar.getClass().getName() );
		props.put(Threshold.MONITOR_ARGS, clPar.getMonitorArgs() );
		props.put(Threshold.SPAN_LENGTH, "" + clPar.notificationIntervalInSecs );
		props.put(Threshold.ACTION , clPar.getAction()  );
		props.put(Threshold.ACTION_ARGS , clPar.getActionArgs()  );
		
		String childTmp =retval;
 		try {
 			AlertCaptain.storeToName(childTmp, props);
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
					((AbstractActionist)theNext).rrdName = props.getProperty(Threshold.DATASOURCE);
//					this.rrdName = this.rrdName == null ? theNext
//							.getDatasource() : this.rrdName;
					this.chainOfAlerters.add(theNext);
					AlertCaptain instance = AlertCaptain.getInstance();
					((AbstractActionist)theNext).notificationIntervalInSecs=60*10;
					this.notificationIntervalInSecs = 60*10;
					instance.register(theNext);
				} catch (TholdException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//storeRRDWRITER(this.rrdName);

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
//	public Always2RRDActionist(  String rrdName,
//			String monitorArgs, int notificationInterval) {
//		super();
//		String chTmp = storeRRDWRITER(rrdName);
//		Properties props = new Properties();
//		props.put(Threshold.DS_NAME, "data");
//		props.put(Threshold.MONITOR_ARGS, monitorArgs);
//		props.put(Threshold.SPAN_LENGTH, "" + notificationInterval);
//
//		Properties myProps = new Properties();
//		myProps.put("childs", chTmp);
//		myProps.put(Threshold.DATASOURCE, rrdName);
//		init(myProps);
//	}
//	@Override
//	public Properties toProperties() {
//		Properties retval = super.toProperties();
//		String value = "";
//		for (String child : this.aList) {
//			value = value.length() == 0 ? "" : value + ",";;
//			value += child;
//		}
//		// this.aList = props.getProperty("childs").split(",");
//		retval.put("childs", value);
//		return retval;
//	}
}
