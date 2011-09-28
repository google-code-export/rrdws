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
	protected long activationTimeoutInSeconds;
	protected String action;
	protected String actionArgs;

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

	@Override
	public String getActionArgs() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 30.08.2011");
		else {
			return null;
		}
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

}
