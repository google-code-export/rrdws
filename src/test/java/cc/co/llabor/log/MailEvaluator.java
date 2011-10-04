package cc.co.llabor.log;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <b>Description:TODO</b>
 * http://www.manning-sandbox.com/thread.jspa?messageID=114106
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.09.2011::10:36:20<br> 
 */
public class MailEvaluator implements TriggeringEventEvaluator, OptionHandler {
	private static final Logger log = LoggerFactory.getLogger(MailEvaluator.class .getName());

	public boolean isTriggeringEvent(LoggingEvent event) {
		return "cc.co.llabor.log.GtalkAppenderTest".equals( event.categoryName ); 
		//return true; 
	}

	@Override
	public void activateOptions() {
		log.debug("alway active!");
	}
}


 