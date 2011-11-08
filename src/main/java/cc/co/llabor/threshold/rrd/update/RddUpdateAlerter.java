package cc.co.llabor.threshold.rrd.update;

import java.io.IOException;
import java.util.Properties;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;

import cc.co.llabor.threshold.AbstractActionist;
import cc.co.llabor.threshold.AbstractAlerter;

/**
 * <b>Hardcoded RRD-linked Alerter, which will controll the RRD-DB directly
 * </b>
 * 
 * for any monitored RRD-sensor will be created associated
 * RRD-DB with CF_AVERAGE,CF_MAX and CF_MIN counters.
 * Depend on the rules AlertCaptain will organize Observer<->Observable mechanism for any defined pair.
 * 
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 01.09.2011::16:48:15<br>
 */
public abstract class RddUpdateAlerter extends AbstractActionist {
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 6260786617371803021L;

	private Sample sample;

	private RrdDb rrdDb;

	public RrdDb getRrdDb() { 
			return rrdDb;
	}
	
 	

	public void setRrdDb(RrdDb rrdDb) {
		this.rrdDb = rrdDb;
	}

	private void init(String rrdPar) throws RrdException, IOException {
		RrdDef rrdDef = new RrdDef(rrdPar);
		long startTime = 920800000L; // 920800000L == [Sun Mar 07 10:46:40 CET 1999]
		rrdDef.setStartTime(startTime);
		rrdDef.setStep(1);
		rrdDef.addDatasource("speed", "GAUGE", 600, Double.NaN, Double.NaN);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.1, 1, 3600);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 24, 797);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.5, 288, 775);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.1, 1, 3600);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 24, 797);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 288, 775);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.1, 1, 3600);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 24, 797);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 288, 775);
		rrdDb = new RrdDb(rrdDef);
		
		sample = rrdDb.createSample();

	}

	
	public RddUpdateAlerter(Properties props){
		super(props);
	}
	
	public RddUpdateAlerter(String rrdName, double baseLine, long activationTimeoutInSeconds) {
		this.rrdName = rrdName;
		this.monitorType = "mvel";
		this.baseLine = baseLine;
		this.activationTimeoutInSeconds = activationTimeoutInSeconds;
		try {
			init(this.rrdName + ".Thold.RRD");
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void init(Properties props) {
		// do nothing
	}
	
 
	@Override
	protected void act(long timestampSec) {
		try {
			long activatingTimepoint = this.inIncidentTime()
					+ this.getSpanLength();
			boolean isActivated = timestampSec > activatingTimepoint;
			int lowLevel = isActivated
					? ACTIVE_ALERT_VALUE
					: COUNTDOWN_TO_DEACTIVE_VALUE;
			String valTmp = ""
					+ (this.incidentTime == -1 ? lowLevel : lowLevel);
			this.sample.setAndUpdate("" + (timestampSec) + ":" + valTmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public static int PASSIVE_VALUE = 0;
	public static int ACTIVE_ALERT_VALUE = 255;
	public static int COUNTDOWN_VALUE = -256;
	public static int COUNTDOWN_TO_DEACTIVE_VALUE = 100;
	
	
	@Override
	public void performSleep(long timestamp) {
		try {
			long activatingTimepoint = this.inIncidentTime()
					+ this.getSpanLength();
			boolean isActivated = timestamp > activatingTimepoint;
			int lowLevel = isActivated ? PASSIVE_VALUE : COUNTDOWN_VALUE;
			String valTmp = ""
					+ (this.incidentTime == -1 ? lowLevel : lowLevel);
			this.sample.setAndUpdate("" + (timestamp) + ":" + valTmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
 		}
	}

	@Override
	public void stop() { 
		try {
			rrdDb.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
