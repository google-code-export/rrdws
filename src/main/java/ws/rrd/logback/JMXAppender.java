package ws.rrd.logback;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;  
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.ObjectName; 

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorCode; 

import ws.rrd.csv.RrdKeeper;
//import jmxlogger.integration.log4j.JmxLogAppender;
import jmxlogger.tools.JmxConfigStore; 
//import jmxlogger.tools.JmxLogService;
import jmxlogger.tools.ToolBox;
import jmxlogger.tools.JmxConfigStore.ConfigEvent; 
/**
 * This class implements the Log4J appender for JmxLogConfigurer.  It can be used to broadcast
 * Log4J log events as JMX events.  When this class is initialized by the logging
 * framework, it creates a JMX MBean that emitts log event. 
 *
 * @author vladimir.vivien
 */ 
public class JMXAppender extends AppenderSkeleton{ 
    private JMXService jmxLogService;
    private JmxConfigStore configStore;
    private Layout logLayout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");
    
	 /**
     * Default constructor.  Creates a new JMX MBean emitter and registers that
     * emitter on the underlying Platform MBeanServer.
     */
    public JMXAppender() {
        initializeLogger();
        System.out.println("JMXAppender inited.");
        //configure();
    }

    /**
     * Constructor which takes a JMX ObjectName instance used to create the JMX
     * event emitter.
     * @param name - ObjectName instance used to register MBean emitter.
     */
    public JMXAppender(ObjectName name){
        this();
        setObjectNameValue(name);
        //configure();
    }

    /**
     * Constructor which takes an MBeanServer where the MBean will be created.
     * @param server
     */
    public JMXAppender(MBeanServer server){
    	this();
        setMBeanServerValue(server);
        //configure();
    }

    /**
     * Constructor which specifies a default MBeanServer and ObjectName for the J
     * JMX MBean event emitter.
     * @param server - default server to use.
     * @param name - JMX ObectName to use for JMX MBean emitter.
     */
    public JMXAppender(MBeanServer server, ObjectName name){
        this(server);//initializeLogger();
        //setMBeanServerValue(server);
        setObjectNameValue(name);
    }	  

    @Override
    public void setThreshold(Priority level){
        super.setThreshold(level);
        configStore.putValue(ToolBox.KEY_CONFIG_LOG_LEVEL, level.toString());
        configStore.postEvent(new ConfigEvent(this, ToolBox.KEY_CONFIG_LOG_LEVEL, level.toString()));
    }

    public void setObjectName(String objName){
        setObjectNameValue(createObjectNameInstance(objName)); 
    }

    public String getObjectName(String objName){
        ObjectName name = (ObjectName) configStore.getValue(ToolBox.KEY_CONFIG_JMX_OBJECTNAME);
        return name != null ? name.toString() : null;
    }

    public void setObjectNameValue(ObjectName name){
        configStore.putValue(ToolBox.KEY_CONFIG_JMX_OBJECTNAME, name);
    }
    public ObjectName getObjectNameValue() {
        return (ObjectName)configStore.getValue(ToolBox.KEY_CONFIG_JMX_OBJECTNAME); 
    }

    public void setMBeanServer(String domain) {
        setMBeanServerValue(createServerInstance(domain)); 
    }
    public String getMBeanServer() {
        MBeanServer svr = (MBeanServer)configStore.getValue(ToolBox.KEY_CONFIG_JMX_SERVER);
        return svr != null ? svr.toString() : null;
    }
    
    public void setMBeanServerValue(MBeanServer server){
        configStore.putValue(ToolBox.KEY_CONFIG_JMX_SERVER, server);
    } 
    
public MBeanServer getMBeanServerValue() {
     return (MBeanServer)configStore.getValue(ToolBox.KEY_CONFIG_JMX_SERVER); 
}

public void setFilterExpression(String exp){
    configStore.putValue(ToolBox.KEY_CONFIG_FILTER_EXP, exp);
    configStore.postEvent(new ConfigEvent(this, ToolBox.KEY_CONFIG_FILTER_EXP, exp));
}

public String getFilterExpression(){
    return (String)configStore.getValue(ToolBox.KEY_CONFIG_FILTER_EXP); 
}

public void setFilterScriptFile(String fileName) {
    File f = new File(fileName);
    if(ToolBox.isFileValid(f)){
        configStore.putValue(ToolBox.KEY_CONFIG_FILTER_SCRIPT, f);
        configStore.postEvent(new ConfigEvent(this, ToolBox.KEY_CONFIG_FILTER_SCRIPT, f));
    }  
}

public String getFilterScriptFile(){
    File file = (File)configStore.getValue(ToolBox.KEY_CONFIG_FILTER_SCRIPT);
    return (file != null) ? file.getAbsolutePath() : null;
}

public String getServerAddress() {
    return (String)configStore.getValue(ToolBox.KEY_CONFIG_SERVER_ADDR);

}
public void setServerAddress(String addr){
    configStore.putValue(ToolBox.KEY_CONFIG_SERVER_ADDR, addr);

}

/**
 * Log4J life cycle method, called once all gettters/setters are called.
 */
@Override
	public void activateOptions() {
		try {
			configure(); 
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			if (!jmxLogService.isStarted()) {
				jmxLogService.start();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {

			ServletListener.registerToKilling(this);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}


/**
 * Log4J life cycle method,  
 * 
 * Finalize this appender by calling the derived class'<code>close</code> method.
 * 
 * 
 * @since 0.8.4 
 * 
 * */
@Override
public void finalize(){
	this.close();
	super.finalize();
} 






/**
 * Log4J framework method, called when a jmxLogService logs an event.  Here, it
 * sends the log message to the JMX event bus.
 * @param log4jEvent
 */
//@Override
protected void appendORIGINAL(LoggingEvent log4jEvent) {
    // check configuration
    if (!isConfiguredOk()) {
        errorHandler.error("Unable to log message, check configuration.");
        return;
    }

    // determine if we can go forward with log
    if(!isLoggable(log4jEvent)){
        return;
    }

    // log message
    String msg;
    try {
        msg = layout.format(log4jEvent);
        Map<String,Object> event = prepareLogEvent(msg,log4jEvent);
        jmxLogService.log(event);
    }catch(Exception ex){
       errorHandler.error("Unable to send log to JMX.",
               ex, ErrorCode.GENERIC_FAILURE);
    }
}

/**
 * Log4J life cycle method, stops the JMX MBean emitter.
 */
 
public synchronized void close()
{
	jmxLogService.stop(); 
	System.out.println("JMXAppender destroy...");
    //super.close();
	this.closed = true;
    System.out.println("JMXAppender destroy compleete.");
}




/**
 * Log4J convenience method to indicate the need for a Layout.
 * @return
 */
public boolean requiresLayout() {
    return true; 
} 
/**
 * Determines whether the Appender's configuration is OK.
 * @return boolean
 */
private boolean isConfiguredOk(){
    return jmxLogService != null &&
            jmxLogService.isStarted() &&
            configStore.getValue(ToolBox.KEY_CONFIG_JMX_SERVER) != null &&
            configStore.getValue(ToolBox.KEY_CONFIG_JMX_OBJECTNAME) != null &&
            layout != null &&
            this.getThreshold() != null;
}

/**
 * Figures out if appender can log event
 * @return

 */
private boolean isLoggable(LoggingEvent event) {
    return (event.getLevel().isGreaterOrEqual(this.getThreshold())); 
}

/**
 * Initialize the JMX Logger object.
 */
private void initializeLogger() {
    jmxLogService = (jmxLogService == null) ? JMXService.createInstance() : jmxLogService;
    configStore = jmxLogService.getDefaultConfigurationStore();
    // what to do when a value is update
    configStore.addListener(new JmxConfigStore.ConfigEventListener() {
        public void onValueChanged(JmxConfigStore.ConfigEvent event) {
            if (event.getKey().equals(ToolBox.KEY_CONFIG_LOG_LEVEL) && event.getSource() != JMXAppender.this) {
                setInternalThreshold((String) event.getValue()); 
            } 
        }
    });
}

private void setInternalThreshold(String t){
    super.setThreshold(Level.toLevel(t));
 
}

/**
 * Configures the value objects for the appender.
 */
private void configure() {
    // configStore layout
    if (getLayout() == null) {
        setLayout(logLayout);
    }
    // configStore level
    if(getThreshold() == null){
        setThreshold(Level.DEBUG);
    }

    // configure server
    if (configStore.getValue(ToolBox.KEY_CONFIG_JMX_SERVER) == null) {
        this.setMBeanServerValue(createServerInstance("platform"));
    }

    // configure internal object name
    if(configStore.getValue(ToolBox.KEY_CONFIG_JMX_OBJECTNAME) == null){
        setObjectNameValue(ToolBox.buildDefaultObjectName(Integer.toString(this.hashCode())));
    }
}   
 

/**
 * Transfers Log4J LoggingEvent data to a map to be passed to JMX event bus.
 * @param fmtMsg
 * @param record
 * @return Map containing the event to be logged.
 */
private Map<String,Object> prepareLogEvent(String fmtMsg, LoggingEvent record){
    Map<String,Object> event = new HashMap<String,Object>();
    long ts = new Date().getTime();
    event.put(ToolBox.KEY_EVENT_SOURCE,this.getClass().getName());
    event.put(ToolBox.KEY_EVENT_LEVEL,record.getLevel().toString());
    event.put(ToolBox.KEY_EVENT_LOGGER,record.getLoggerName());
    event.put(ToolBox.KEY_EVENT_FORMATTED_MESSAGE,fmtMsg);
    event.put(ToolBox.KEY_EVENT_RAW_MESSAGE, record.getMessage());
    /* most log4j usage is below necessary version, therefore do not support
     * the following methods.
     */
    //event.put(ToolBox.KEY_EVENT_SEQ_NUM, new Long(record.getTimeStamp()));
    event.put(ToolBox.KEY_EVENT_SEQ_NUM, new Long(ts));
//    event.put(ToolBox.KEY_EVENT_SOURCE_CLASS,
//            (record.locationInformationExists())
//            ? record.getLocationInformation().getClassName()
//            : "Unavailable");
//    event.put(ToolBox.KEY_EVENT_SOURCE_METHOD,
//            (record.locationInformationExists())
//            ? record.getLocationInformation().getMethodName()
//            : "Unavailable" );
    event.put(ToolBox.KEY_EVENT_SOURCE_THREAD,
            record.getThreadName());
    event.put(ToolBox.KEY_EVENT_THROWABLE,
            (record.getThrowableInformation() != null)
            ? record.getThrowableInformation().getThrowable()
            : null);
    //event.put(ToolBox.KEY_EVENT_TIME_STAMP, new Long(record.getTimeStamp()));
    event.put(ToolBox.KEY_EVENT_TIME_STAMP, new Long(ts));
    return event;
}

private ObjectName createObjectNameInstance(String name){
    ObjectName objName = null;
    if(name == null){
        objName = ToolBox.buildDefaultObjectName(Integer.toString(this.hashCode()));
    }else{
        objName = ToolBox.buildObjectName(name);
    }
    return objName;
}

private MBeanServer createServerInstance(String domain) {
    MBeanServer svr = ManagementFactory.getPlatformMBeanServer();
    if(domain != null && !domain.equalsIgnoreCase("platform")){
        svr = ToolBox.findMBeanServer(domain);
    }
    return svr;
} 
	 /**
     * Prepares event information as Notification object.
     * @param event
     * @return Notification
     */
    private Notification buildNotification(Map<String,Object> event){
        long seqnum = (event.get(ToolBox.KEY_EVENT_SEQ_NUM) != null) ? (Long)event.get(ToolBox.KEY_EVENT_SEQ_NUM) : 0L;
        long timestamp  = (event.get(ToolBox.KEY_EVENT_TIME_STAMP) != null) ? (Long)event.get(ToolBox.KEY_EVENT_TIME_STAMP) : 0L;
 

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
    	//super.append(log4jEvent);
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
    	// instead of super ...
		appendORIGINAL(log4jEvent);
    }    
    
    
    {
    	try {
    		System.out.println("<jmxlog!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!   !!!!  !!! !!!!   !!!      !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!! !!!!! ! !! !!!!! !!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!! !!!!! !! ! !!!!! !!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!! !!!!! !!!  !!!!! !!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!     !!! !!!! !!!!   !!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		initShutdownHook() ;
    		System.out.println("------------------------------------------------------------------");
    		System.out.println("---------------    --- --  ---------------------------------------");
    		System.out.println("--------------- --- --   -----------------------------------------");
    		System.out.println("--------------- --- -- -- ----------------------------------------");
    		System.out.println("----------------   --- --- ---------------------------------------");
    		System.out.println("------------------------------------------------------------<jmxlog");
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			System.out.println("*************************************************************************");
			e.printStackTrace();
			System.out.println("*************************************************************************");
			System.out.println("*****************************              ******************************");
			System.out.println("***************************** error@jmxlog ******************************");
			System.out.println("*****************************              ******************************");
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
				System.out.println("<jmxlog!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!! !!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!! ! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		close();
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("---------------------------        -------------------------------");
	    		System.out.println("--------------------------- jmxlog -------------------------------");
	    		System.out.println("---------------------------        -------------------------------");
	    		System.out.println("------------------------------------------------------------------");
	    		System.out.println("-----------------------------------------------------------<jmxlog");				
				
			}
		});
	}    
    
}


 