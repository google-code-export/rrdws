package org.jrobin.thold;

import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  30.08.2011::16:42:16<br> 
 */
public class CheckPoint {
	public CheckPoint(long timestamp,Threshold toCheck){
		this.timestamp = timestamp;
		this.toCheck = toCheck;
	}
	public long getTimestamp() { 
			return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Threshold getToCheck() { 
			return toCheck;
	}
	public void setToCheck(Threshold toCheck) {
		this.toCheck = toCheck;
	}
	long timestamp;
	Threshold toCheck;
}


 