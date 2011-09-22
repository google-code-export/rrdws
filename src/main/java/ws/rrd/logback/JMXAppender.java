package ws.rrd.logback;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import ws.rrd.csv.RrdKeeper;

import jmxlogger.integration.log4j.JmxLogAppender;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  14.09.2011::10:49:38<br> 
 */
public class JMXAppender extends JmxLogAppender{
	
    @Override
    protected void append(LoggingEvent log4jEvent) {
    	super.append(log4jEvent);
    	int level = log4jEvent.getLevel().toInt();
    	RrdKeeper instance = RrdKeeper.getInstance();
    	instance.logged();
		switch (level) {
			case Priority.ALL_INT :
				instance.loggedTRACE();
				break;
			case Priority.DEBUG_INT :
				instance.loggedDEBUG();
				break;
			case Priority.ERROR_INT:
				instance.loggedERROR();
				break;
			case Priority.FATAL_INT  :
				instance.loggedFATAL();
				break;
			case Priority.WARN_INT  :
				instance.loggedWARN();
				break;
			case Priority.INFO_INT :
				instance.loggedINFO();
				break;

			default :
				break;
		}
    	
    }
    {
    	try {
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			initShutdownHook() ;
    		System.out.println("------------------------------------------------------------------");
    		System.out.println("------------------------------------------------------------------");
    		System.out.println("------------------------------------------------------------------");
    		System.out.println("------------------------------------------------------------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			e.printStackTrace();
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
		}
    }

    /**
	 * This method seths a ShutdownHook to the system
	 *  This traps the CTRL+C or kill signal and shutdows 
	 * Correctly the system.
	 * @throws Exception
	 */ 
	 public void initShutdownHook() throws Exception {
		 
		Runtime.getRuntime().addShutdownHook(new Thread("rrd.JMXAppender.ShutdownHook") {
			public void run() {
				close();
			}
		});
	}    
    
}


 