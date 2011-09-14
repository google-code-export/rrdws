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
    	switch (level) {
			case Priority.DEBUG_INT :
				RrdKeeper.getInstance().loggedDEBUG();
				break;
			case Priority.ERROR_INT:
				RrdKeeper.getInstance().loggedERROR();
				break;
			case Priority.FATAL_INT  :
				RrdKeeper.getInstance().loggedFATAL();
				break;
			case Priority.WARN_INT  :
				RrdKeeper.getInstance().loggedWARN();
				break;
			case Priority.INFO_INT :
				RrdKeeper.getInstance().loggedINFO();
				break;

			default :
				break;
		}
    	
    }

}


 