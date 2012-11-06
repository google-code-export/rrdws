package ws.rrd.logback;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.10.2012::15:50:22<br> 
 */
public class ServletListener implements ServletContextListener{
	
	private static final Logger log = LoggerFactory.getLogger(ServletListener.class  );
	static ThreadGroup mythreads = null;
	{
		String name = ""+System.currentTimeMillis();
		try{
			// @see org.collectd.mx.MBeanSender.getInstanceName()
			name = "rrd#"+System.getProperty("jcd.instance", name  );
			  
		}catch ( SecurityException e) {
			mythreads= Thread.currentThread().getThreadGroup() ;
		}
		try{ 
			mythreads= new ThreadGroup(name);
			mythreads.setDaemon(true);
		}catch ( SecurityException e) {
			mythreads= Thread.currentThread().getThreadGroup() ;
		}
	}	
	
	public static ThreadGroup getDefaultThreadGroup( ){
		return mythreads;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("!!!!!!!!!!!!!!!!!!rrd contextInitialized!!!!!!!!!!!!!"); 
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("..............rrd contextDestroyed...................");
		if (appenderToKill!=null){
			appenderToKill.close();
			appenderToKill = null;
		}
		log.info(".....................................................");
	}

	static JMXAppender appenderToKill =null;
	public static void registerToKilling(JMXAppender jmxAppender) {
		appenderToKill = jmxAppender;
	}

}


 