package org.jrobin.thold;

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
 * Creation:  31.08.2011::22:15:56<br> 
 */
public class BaselineAlerter implements Threshold {
	private String rrdName;
	private double baseLine;
	private long activationTimeoutInSeconds;
	private String action;
	private String actionArgs;
	private RrdDb rrdDb;
	private Sample sample;
	private double delta;

	public double getDelta() { 
			return delta;
	}

	public BaselineAlerter(String rrdName, double baseLine, double allowedDelta,  long activationTimeoutInSeconds) {
		this.rrdName = rrdName;
		this.delta = allowedDelta;
		this.baseLine = baseLine;
		this.activationTimeoutInSeconds = activationTimeoutInSeconds ;
		try {
			init(this.rrdName +".stat.rrd");
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init(String rrdPar) throws RrdException, IOException{
		RrdDef rrdDef = new RrdDef(rrdPar);
		long startTime = 920800000L; //920800000L == [Sun Mar 07 10:46:40 CET 1999] 
 
		rrdDef.setStartTime(startTime );
		rrdDef.setStep(1); 
		rrdDef.addDatasource("speed", "GAUGE", 600 , Double.NaN, Double.NaN);		
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE, 0.1, 1, 3600);
		rrdDef.addArchive(ConsolFuns.CF_AVERAGE , 0.5, 6, 700);
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
	
	@Override
	public String getDatasource() {
		return this.rrdName;
	}

	@Override
	public String getMonitorType() {
		return this.getClass().getName();
	}

	@Override
	public String getMonitorArgs() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
			return null;
		}
	}

	@Override
	public String getAction() { 
			return action; 
	}
	

	public void performAction(long timestamp) { 
			try {
				long activatingTimepoint = this.inIncidentTime() + this.getSpanLength();
				boolean isActivated = timestamp>activatingTimepoint;
				int lowLevel = isActivated? 44:111;
				String valTmp = ""+(this.IncidentTime==-1?lowLevel:this.baseLine);
				this.sample.
					setAndUpdate(""+(timestamp)+":"+valTmp  );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RrdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	public void performSleep(long timestamp) { 
		try {
			long activatingTimepoint = this.inIncidentTime() + this.getSpanLength();
			boolean isActivated = timestamp>activatingTimepoint;
			int lowLevel = isActivated? 0:-111;
			String valTmp = ""+(this.IncidentTime==-1?lowLevel:lowLevel);
			this.sample.
				setAndUpdate(""+(timestamp)+":"+valTmp  );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
}	

	@Override
	public long getSpanLength() { 
			return activationTimeoutInSeconds; 
	}

	long IncidentTime = -1;
	@Override
	public long inIncidentTime() { 
		return IncidentTime;
	}

 
 
	public void incident(long timestamp) {
		if (IncidentTime ==-1)
			IncidentTime = timestamp;
	}

	@Override
	public void clear() {
		IncidentTime = -1;
	}

	@Override
	public String getActionArgs() {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
		return null;
		}
	}

	public double getBaseLine() { 
		return this.baseLine;
	}
}


 