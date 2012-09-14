package eu.blky.logparser;

import java.util.HashSet;
import java.util.Set;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  11.09.2012::14:05:55<br> 
 */
public class EventLog {

	private long endTime;
	private String demandeId;
	private long httpSize;
	private long startTime;
	private String reqType;
	private String useragent;

	public EventLog(String demandeId, long httpSize, long startTime,
			long endTime) {
		this.setDemandeId(demandeId);
		this.setHttpSize(httpSize);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}

	public EventLog(String reqType, String httpStatus, long httpSize,
			long startTime2, long endTime2) {
		this(httpStatus, httpSize, startTime2, endTime2);
		this.setReqType(reqType);
	}

	public EventLog(String useragentPar, String reqType2, String httpStatus,
			long httpSize, long startTime2, long endTime2) {
		this(reqType2, httpStatus, httpSize, startTime2, endTime2 );
		this.setUseragent(useragentPar);
	}

	private void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() { return endTime;
	}

	private void setDemandeId(String demandeId) {
		this.demandeId = demandeId;
	}

	public String getDemandeId() { return demandeId;
	}

	private void setHttpSize(long httpSize) {
		this.httpSize = httpSize;
	}

	public long getHttpSize() { return httpSize;
	}

	private void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() { return startTime;
	}
	
	public String toString(){
		return 	"" +reqType +"@"+ demandeId +":"+startTime+"-" + endTime;
		
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqType() { return reqType;
	}

	private void setUseragent(String useragent) {
		this.useragent = useragent;
		collectUA(useragent);
	}

	private static Set<String> uaSet = new HashSet<String>();
	private static void collectUA(String useragent2) {
		uaSet.add(useragent2);
	}
	
	 

	public String getUseragent() { return useragent;
	}

 
	/**
	 * @return the uaSet
	 */
	public static Set<String> getUaSet() { return uaSet;
	}

}


 