package ws.rrd.csv;
 
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.DynamicMBean;
import javax.management.ImmutableDescriptor;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.DescriptorSupport;
import javax.naming.InitialContext;
  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


 

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  07.09.2011::17:10:50<br> 
 */
public class RrdKeeper implements NotificationBroadcaster, NotificationListener , DynamicMBean{

	private static final String DOMAIN = "rrdMX";
	static RrdKeeper me = new RrdKeeper(); // jaja! natuerlich. singleton :-P...
	static private Map<String, RrdNotificator> listeners = null;
	static {
		listeners = new HashMap<String, RrdNotificator>();
		try {
			me.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private RrdKeeper(){
		super();
	}
	
    private synchronized void init() throws Exception {
        MBeanServer bs =
            ManagementFactory.getPlatformMBeanServer();    	
        ObjectName name = new ObjectName(DOMAIN + ":" + "type=" + ""+this.getClass().getName());
        try{
        	bs.registerMBean(this, name);
        	assert bs.getObjectInstance(name) != null : "RRDKeeper MBean is not registered";
        	// register itself as listener
        	NotificationListener listener = this;
			NotificationFilter filter = new EverybodyFilter();
			Object handback = this;
			
			this.addNotificationListener(listener, filter, handback);
			
			initLogger();
        }catch(InstanceAlreadyExistsException e)
        {e.printStackTrace();
        	MBeanInfo oldOne = bs.getMBeanInfo(name);
        	System.out.println("ungeristered MBean:"+oldOne);
        	try{
        		bs.unregisterMBean(name);
        	}catch(Throwable ee){
        		ee.printStackTrace();
        	}
        	init();
        } 
   }
	/**
	 * /jmx-logger-log4j/src/test/java/jmxlogger/test/JmxLogAppenderTest.java
	 * @author vipup
	 * @throws InstanceNotFoundException 
	 */
	private void initLogger() throws InstanceNotFoundException { 
        //Logger logger = Logger.getLogger(JmxLogAppenderTest.class);
        //DOMConfigurator.configure("log4j.xml");
        MBeanServer platformServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = /*ToolBox.*/buildObjectName("rrdMX:type=ws.rrd.csv.RrdKeeper");
        //LogListener lstnr = new LogListener();
        NotificationListener lstnr = this; 
        platformServer.addNotificationListener(objectName, lstnr, null,null);

//        log.warn("Hello World! {}", this);
//        log.debug("This is a test for   log4j @rrdKeeper.{}", this);
//        log.info("Do not attempt to change the channel.{}", this);
	}
	
    /**
     * Util method to build a standard JMX ObjectName.  It wraps all of the exceptions into a RuntimeException.
     * @param name - the string representation of ObjectName
     * @return new instance of ObjectName
     */
    public static ObjectName buildObjectName(String name){
        ObjectName objName = null;
        try {
            objName = new ObjectName(name);
        } catch (MalformedObjectNameException ex) {
            throw new RuntimeException(ex);
        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);
        }
        return objName;
    }	
	
	public static RrdKeeper getInstance() { 
		return me;
	}
	
	
	
	@Override
	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, final Object handback)
			throws IllegalArgumentException {
		Object[] argArray = new Object[]{listener, filter,handback};
		log.info("addNotificationListener( {},{},{})" ,argArray );
		String key = "/"+filter.toString(); // TODO xpath ??
		RrdNotificator theNext = new RrdNotificator (listener, filter,handback);
		listeners.put(key , theNext);
	}
	@Override
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		
		if (listeners.containsValue(listener)){
			log.info("--------  removeNotificationListener( {} )" ,listener );
		}else{
			log.info("!!!!!!!!!!  removeNotificationListener( {} )" ,listener );
		}
	}
	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		MBeanNotificationInfo[] retval = new MBeanNotificationInfo[]{};
		List<MBeanNotificationInfo> notification = new ArrayList<MBeanNotificationInfo>();
		for (int i=0;i<_metrics.size();i++){
			/*
			 * Parameters:
notifTypes The array of strings (in dot notation) containing the notification types that the MBean may emit. This may be null with the same effect as a zero-length array.
name The fully qualified Java class name of the described notifications.
description A human readable description of the data.
descriptor The descriptor for the notifications. This may be null which is equivalent to an empty descriptor.

			 */
			String description = "description #"+i;
			Descriptor descriptor = (i%2) == 0?
						new DescriptorSupport():
						new ImmutableDescriptor("i"+i+"="+ _metrics.keySet().toArray() [ i ],  "a=b=c");
			String name = this.getClass().getName(); //"x# ["+i+"]="+ _metrics.values().toArray() [ i ];
			String[] notifTypes = {
						"String".getClass().getName()
						 , Long.class.getName()
						 , Double.class.getName()
						 , Float.class.getName()
						 ,  Integer.class.getName()
						 ,  Float.class.getName()
						};
			MBeanNotificationInfo e =new MBeanNotificationInfo(notifTypes, name, description, descriptor);// new RrdNotifInf(notifTypes, name, description, descriptor);
			notification.add(e );
		}
		retval  =   notification.toArray(retval);
		return retval;
	}
	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		syncValues();
		return _metrics.get(	attribute);
	}
	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		}
	}
	@Override
	public AttributeList getAttributes(String[] attributes) {
		AttributeList  retval = new AttributeList  ();
		for (String attr:attributes){
			try {
				retval.add((Number) getAttribute(attr));
			} catch (AttributeNotFoundException e) {
				log.error("getAttributes{}{}",attributes, e);
			} catch (MBeanException e) {
				log.error("getAttributes{}{}",attributes, e);
			} catch (ReflectionException e) {
				log.error("getAttributes{}{}",attributes, e);
			}
		}
		
		return retval;
	}
	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		return null;
		}
	}
	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		return null;
		}
	}
	@Override
    public MBeanInfo getMBeanInfo() {
        MBeanInfo info =
            new MBeanInfo(getClass().getName(),
                          RrdKeeper.class.getName(),
                          getAttributeInfo(),
                          1==1?null:getConstructors(), //constructors
                        		  1==1?null:getOperations() , //operations
                        				  1==1?null:getNotificationInfo()); //notifications
        return info;
    }
    private MBeanOperationInfo[] getOperations() { 
    	String descTmp = "resetCouters(...)";
		Method methodTmp =this.getClass().getMethods()[0];
		MBeanOperationInfo[] retval = new MBeanOperationInfo[]
	     
	     {
    			new MBeanOperationInfo(descTmp, methodTmp)
	     };
		return retval;
		
	}
	private MBeanConstructorInfo[] getConstructors() { 
		Constructor constructor = null;
		String description = ""+constructor ;
		
		MBeanConstructorInfo[] retval  = null;
		try{
			constructor  = this.getClass().getConstructors()[0];
			MBeanConstructorInfo theOneConstr = new MBeanConstructorInfo(description, constructor);
			retval = new MBeanConstructorInfo[]{
					theOneConstr 
				};
		}catch(Throwable e){}
		
		return retval ;
		
	}
	protected String getAttributeType(String name) {
        return _metrics.get(name).getClass().getName();
    }

    protected String getAttributeDescription(String name) {
        return name + " Attribute";   
    }
    
    public void update(){
    	updateCounter++;
    }

    public void success(){
    	successCounter++;
    }    

    public void error(){
    	errorCounter++;
    }        
    
    private Map<String,Number> _metrics = new HashMap<String, Number> ();
    protected MBeanAttributeInfo[] getAttributeInfo() { 
		this.syncValues();
        MBeanAttributeInfo[] attrs =
            new MBeanAttributeInfo[_metrics.size()];
        int i=0;
        for (String name : _metrics.keySet()) {
            String attributeType = getAttributeType(name);
			String attributeDescription = getAttributeDescription(name);
			attrs[i++] =
                new MBeanAttributeInfo(name,
                                       attributeType,
                                       attributeDescription,
                                       true,   // isReadable
                                       false,  // isWritable
                                       false); // isIs
        }        
        return attrs;
    }
    long lastSyncTimeMilliseconds = -1;
    long lastUpdatesCounter = -1; 
    private void syncValues() {
//    	if (!inited){
//    		System.out.println("not inited yet...");
//    		try {
//    			init();
//    		} catch (Exception e) { 
//    			e.printStackTrace();
//    		}    		
//    	}
    	long startTmp = System.currentTimeMillis();

    	_metrics .put("loggedFatal",loggedFatal);
    	_metrics .put("loggedError",loggedError);
    	_metrics .put("loggedWarn",loggedWarn);
    	_metrics .put("loggedInfo",loggedInfo);
    	_metrics .put("loggedDebug",loggedDebug);    	
    	_metrics .put("loggedTrace",loggedTrace);
    	_metrics .put("loggedCounter",loggedCounter);
		_metrics .put("updateCounter",  new Long(updateCounter)  );
    	_metrics .put("successCounter",  new Long(successCounter));
    	_metrics .put("errorCounter",  new Long(errorCounter));
    	_metrics .put("warningCounter",  new Long(warningCounter));
    	_metrics .put("createCounter",  new Long(createCounter));
    	_metrics .put("fatalCounter",  new Long(fatalCounter));
    	_metrics .put("lastSyncTimeMilliseconds",   new Long(lastSyncTimeMilliseconds));
    	_metrics .put("currentTimeMilliseconds",   new Long(lastSyncTimeMilliseconds ));
    	_metrics .put("mathRandom",   new Double( Math.random() ) );
    	_metrics .put("sinusT",   new Double( Math.sin( lastSyncTimeMilliseconds *  (7.0/(1000*60*60*24*Math.PI)) ) ) );
    	
//    	if(listeners!=null)// workaroud for static-initialisation
//    	for (RrdNotificator l:listeners.values()){
////    		System.out.println(l);
////    		Object handback = "not#"+updateCounter;
////			String typeTmp = "".getClass().getName();
////			String msgTmp = "rrd self-notification#"+this.updateCounter;
////			Notification notification = new RrdNotification(typeTmp , handback, this.updateCounter, startTmp, msgTmp );
////			l.getListener().handleNotification(notification , handback );
//    		log.error( "Notification for {} - {} ", l, this  );
//    	}
//    	try{
//    		log.trace( "sync  # {} ",  ""+ this.updateCounter  );
//    	}catch(Throwable ee){
//    		log.warn(  "syncValues() ",ee);
//    	}
//    	
    	long nowTmp = System.currentTimeMillis();
    	
    	long sinceLastMsTmp = nowTmp - lastSyncTimeMilliseconds;
    	if ( sinceLastMsTmp  >60*1000){ // 1 per min
    		_metrics .put("timePerSync",   new Long(startTmp - nowTmp  ));
    		_metrics .put("timeSinceLastSync",   new Long(sinceLastMsTmp));
    		double updatesPerSecond = updateCounter - lastUpdatesCounter ;
    		_metrics .put("updateDelta",   new Double(updatesPerSecond));
    		updatesPerSecond =  (1000*updatesPerSecond)/sinceLastMsTmp;
    		lastUpdatesCounter  = updateCounter ;
    		_metrics .put("updatesPerSecond",   new Double(updatesPerSecond));
    		lastSyncTimeMilliseconds = nowTmp ;
    	}				

		

	}
	public void warning() {
		warningCounter++;
	}
	public void create() {
		createCounter++;
	}
	public void fatal() {
		fatalCounter++;
	}
    long updateCounter = 0;
    long successCounter = 0;
    long errorCounter = 0;
   
    long warningCounter = 0;
	long createCounter = 0;
	long fatalCounter = 0;
	private static final Logger log = LoggerFactory.getLogger(RrdKeeper.class .getName());

	@Override
	public void handleNotification(Notification notification, Object handback) {
		 System.out.println("NOTIFICATION>>>"+notification+"\""+handback);
	}
	private int loggedFatal = 0;
	private int loggedError = 0;
	private int loggedWarn = 0;
	private int loggedInfo = 0;
	private int loggedDebug = 0;
	private int loggedTrace = 0;
	private int loggedCounter = 0;
	
	 
	public void loggedFATAL() {
		loggedFatal++;
	}
	public void loggedERROR() {
		loggedError++;
	}
	public void loggedWARN() {
		loggedWarn++;
	}
	public void loggedINFO() {
		loggedInfo++;
	}
	public void loggedDEBUG() {
		loggedDebug++;
	}
	public void loggedTRACE() {
		loggedTrace++;
	}
	public void logged() {
		loggedCounter++;
	}	

}


 