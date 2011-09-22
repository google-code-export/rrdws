package ws.rrd.server;
import java.io.Serializable;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  22.09.2011::16:15:39<br> 
 */
public class LCacheEntry implements Serializable {
	byte[] data;
	private long expired = System.currentTimeMillis() + 60*60*1000; // 1h
	public LCacheEntry(String urlStr, byte[] bytesTmp) {
		this.data = bytesTmp;
	}

	public void setExpired(long expired) {
		this.expired = expired;
	}

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 6659451347990402252L;

	public byte[] getBytes() {  
		return data; 
	}

	public long getExpired() { 
		return expired ;
		 
	}
}


 