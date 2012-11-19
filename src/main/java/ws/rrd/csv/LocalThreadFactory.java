package ws.rrd.csv;

import java.util.concurrent.ThreadFactory;

import ws.rrd.logback.ServletListener;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  19.11.2012::13:31:51<br> 
 */
public class LocalThreadFactory implements ThreadFactory {

	private static long tCounter = 0;
	
	@Override
	public Thread newThread(Runnable r) {
		ThreadGroup tgTmp = ServletListener.getDefaultThreadGroup();
		Thread retval = new Thread(tgTmp,"heartbeat#"+tCounter ++);
		return retval ;
	}

}


 