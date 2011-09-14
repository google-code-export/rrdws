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

}


 