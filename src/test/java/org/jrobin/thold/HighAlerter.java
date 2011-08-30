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
 * Creation:  30.08.2011::15:46:23<br> 
 */
public class HighAlerter implements Threshold {

	private String rrdName;
	private double hiLimit;
	private int activationTimeoutInSeconds;
	private String action;
	private String actionArgs;
	private RrdDb rrdDb;
	private Sample sample;

	public HighAlerter(String rrdName, double hiLimit, int activationTimeoutInSeconds) {
		this.rrdName = rrdName;
		this.setHiLimit(hiLimit);
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
		rrdDef.setStep(55);
		int MAX_POSSIBLE_IQ = 200;
		rrdDef.addDatasource("speed", "GAUGE", 600*MAX_POSSIBLE_IQ, Double.NaN, Double.NaN);
		
		rrdDef.addArchive("AVERAGE", 0.1, 1, 3600);
		rrdDef.addArchive("AVERAGE", 0.5, 6, 700);
		rrdDef.addArchive("AVERAGE", 0.5, 24, 797);
		rrdDef.addArchive("AVERAGE", 0.5, 288, 775); 		 
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
				String valTmp = ""+(this.IncidentTime==-1?0:this.hiLimit);
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

	public void setHiLimit(double hiLimit) {
		this.hiLimit = hiLimit;
	}

	public double getHiLimit() { 
		return hiLimit;
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

}


 