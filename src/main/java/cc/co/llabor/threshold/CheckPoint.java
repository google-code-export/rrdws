package cc.co.llabor.threshold;
 
import java.io.IOException;

import org.jrobin.core.Datasource;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;

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
	public CheckPoint(long timestamp,Threshold toCheck) throws IOException, RrdException{
		this.timestamp = timestamp;
		this.toCheck = toCheck;
		String ds = toCheck.getDatasource();
		RrdDb rrd; 
			rrd = RrdDbPool.getInstance().requestRrdDb( ds  );
			//TODO speed???
			long fetchStart = (timestamp ) ;
			long fetchEnd = timestamp+1;
			FetchRequest rTmp = rrd.createFetchRequest("AVERAGE", fetchStart , fetchEnd );
			double[] vals = rTmp.fetchData().getValues("speed"); 
			 
			double val = vals[0];	
			this.setValue(val);
		 
		
	
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
	
	Exception lastError = null;
	int errorCount = 0;
	private double value; 
	public void setValue(double val) {
		this.value = val;
	}
	public Exception getLastError() { 
			return lastError;
	}
	public void setLastError(Exception lastError) {
		this.lastError = lastError;
	}
	public int getErrorCount() { 
			return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public void processError(Exception e) {
		errorCount++; 
		lastError = e;
	}
	public double getValue() {
		return this.value;
	}
}


 