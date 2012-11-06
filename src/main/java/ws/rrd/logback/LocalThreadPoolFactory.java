package ws.rrd.logback;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 05.11.2012::10:34:10<br>
 */
public class LocalThreadPoolFactory implements ThreadFactory {

	static final AtomicInteger poolNumber = new AtomicInteger(1);
	final AtomicInteger threadNumber = new AtomicInteger(1);
	 
	 
	final String namePrefix ;
 

	public LocalThreadPoolFactory(String string) {
		namePrefix = "jmxlogger-" + poolNumber.getAndIncrement() + "-"+string+"-";
	}

	@Override
	public Thread newThread(Runnable task) {
		ThreadGroup threadGroup = ServletListener.getDefaultThreadGroup();
		Thread thread = new Thread(threadGroup , task);
		thread.setName(namePrefix + threadNumber.getAndIncrement());
		thread.setDaemon(true);
		return thread;
	}

}
