package ws.rrd.logback;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Description:Factory for creating Daemon-Threads, in the same ThreadGroup.</b>
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
	static final AtomicInteger threadNumber = new AtomicInteger(1);
	 
	 
	final String namePrefix ;
	private JMXService jmxservice;
 

	public LocalThreadPoolFactory(String string, JMXService jmxservice) {
		namePrefix = "jmxlogger-" + poolNumber.getAndIncrement() + "-"+string+"-";
		// TODO here is workaround for delayed log4j-destroy mechanism.
		// TODO fix it in favor to CORRECT Log4j Appender-destroy-API.
		this.jmxservice = jmxservice;
	}

	@Override
	public Thread newThread(Runnable task) {
		ThreadGroup threadGroup = ServletListener.getDefaultThreadGroup(this);
		Thread thread = new Thread(threadGroup , task);
		thread.setName(namePrefix + threadNumber.getAndIncrement());
		thread.setDaemon(true);
		return thread;
	}
	// TODO here is workaround for delayed log4j-destroy mechanism.
	// TODO fix it in favor to CORRECT Log4j Appender-destroy-API.
	public void close() {
		jmxservice.stop();
	}

}
