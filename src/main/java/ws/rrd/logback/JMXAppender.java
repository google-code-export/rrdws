package ws.rrd.logback;

import java.util.Map;

import javax.management.Notification;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import ws.rrd.csv.RrdKeeper;

import jmxlogger.integration.log4j.JmxLogAppender;
import jmxlogger.tools.ToolBox;

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
	private Map <String,Long> statistics;	
	   /**
     * Prepares event information as Notification object.
     * @param event
     * @return Notification
     */
    private Notification buildNotification(Map<String,Object> event){
        long seqnum = (event.get(ToolBox.KEY_EVENT_SEQ_NUM) != null) ? (Long)event.get(ToolBox.KEY_EVENT_SEQ_NUM) : 0L;
        long timestamp  = (event.get(ToolBox.KEY_EVENT_TIME_STAMP) != null) ? (Long)event.get(ToolBox.KEY_EVENT_TIME_STAMP) : 0L;

        // keep a copy of the stats
        statistics = (Map<String, Long>) event.get(ToolBox.KEY_EVENT_LOG_STAT);

        Notification note = new Notification(
                ToolBox.getDefaultEventType(),
                (String)event.get(ToolBox.KEY_EVENT_SOURCE),
                seqnum,
                timestamp,
                (String)event.get(ToolBox.KEY_EVENT_FORMATTED_MESSAGE));
        note.setUserData(event);
        return note;
    }
    long seqCounter = 0;
    private Notification buildNotification(LoggingEvent event){
        long seqnum = seqCounter++;
        long timestamp  =  event.getStartTime();

        // keep a copy of the stats
//        statistics = (Map<String, Long>) event.get(ToolBox.KEY_EVENT_LOG_STAT);

        Notification note = new Notification(
                ToolBox.getDefaultEventType(),
                event.getLoggerName() ,
                seqnum,
                timestamp,
                event.getMessage().toString() );
        note.setUserData(event.getRenderedMessage());
        return note;
    }
	
    @Override
    protected void append(LoggingEvent log4jEvent) {
    	super.append(log4jEvent);
    	int level = log4jEvent.getLevel().toInt();
    	RrdKeeper instance = RrdKeeper.getInstance();
    	instance.logged();
    	Object handback = log4jEvent;
		// @jmxlogger.tools.JmxLogEmitter.sendLog(Map<String, Object>)
    	//sendNotification(buildNotification(event));
		Notification notification = buildNotification(log4jEvent);
		handback = ""+handback;
		instance.handleNotification(notification , handback);
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
    		System.out.println("!!!!!!!!!   !!!!  !!! !!!!   !!!      !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!! !!!!! ! !! !!!!! !!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!! !!!!! !! ! !!!!! !!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!! !!!!! !!!  !!!!! !!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!     !!! !!!! !!!!   !!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			initShutdownHook() ;
    		System.out.println("---------------    --- --  ---------------------------------------");
    		System.out.println("--------------- --- --   -----------------------------------------");
    		System.out.println("--------------- --- -- -- ----------------------------------------");
    		System.out.println("----------------   --- --- ---------------------------------------");
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
	    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!! !!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!! ! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		close();
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("------------------------------------------------------------------");				
				
			}
		});
	}    
    
}


 