package cc.co.llabor.threshold;

import cc.co.llabor.threshold.rrd.Threshold;

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 01.09.2011::16:46:16<br>
 */
public abstract class AbstractAlerter implements Threshold {
	protected String rrdName;
	protected double baseLine;
	// have to be triggered immediately 
	protected long activationTimeoutInSeconds=600;//10 mins
 

	public String toString(){
		return  this.rrdName+"@"+this.getMonitorType()+":"+this.getMonitorArgs()+"?"+this.getAction()+" ( "+this.getActionArgs() +" )";
	}
	
	
	long incidentTime = -1;
	@Override
	public long getSpanLength() {
		return activationTimeoutInSeconds;
	}

	@Override
	public long inIncidentTime() {
		return incidentTime;
	}

	public void incident(long timestamp) {
		if (incidentTime == -1)
			incidentTime = timestamp;
	}

	
	@Override
	public void clear(long timestamp) {
		incidentTime = -1;
	}


	public double getBaseLine() {
		return this.baseLine;
	}
	@Override
	public void reactIncidentIfAny(long timestamp) {
		long inIncidentTime = this.inIncidentTime();
		long spanLength = this.getSpanLength();

		if (inIncidentTime > 0 && (inIncidentTime + spanLength) < timestamp) {
			this.performAction(timestamp);
		} else {
			this.performSleep(timestamp);
		}
	}

	@Override
	public String getDatasource() {
		return this.rrdName;
	}
 
	/**
	 * something like Cpu_Load
	 */
	@Override
	public String getMonitorType() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
			return null;
		}
	}
 
	@Override
	/**
	 * ...and _Average:1Hour _?
	 */
	public String getMonitorArgs() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
			return null;
		}
	}

	
	@Override
	/**
	 * shell://kill
	 */
	public String getAction() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
			return null;
		}
	}

	@Override
	/**
	 * pid
	 */
	public String getActionArgs() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
			return null;
		}
	}
}
