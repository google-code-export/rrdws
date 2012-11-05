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
	private ThreadGroup threadGroup;
	final String namePrefix;

	public LocalThreadPoolFactory() {
		SecurityManager s = System.getSecurityManager();
		threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread() .getThreadGroup();
		namePrefix = "jmxlogger-" + poolNumber.getAndIncrement() + "-thread-";
	}
	public LocalThreadPoolFactory(ThreadGroup threadGroup) {
		this.threadGroup = threadGroup;
		namePrefix = "jmxlogger=" + poolNumber.getAndIncrement() + "-thread-";
	}

	@Override
	public Thread newThread(Runnable task) {
		Thread thread = new Thread(threadGroup, task);
		thread.setName(namePrefix + threadNumber.getAndIncrement());
		thread.setDaemon(true);
		return thread;
	}

}
