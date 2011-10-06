package ws.rrd.csv;
 
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method; 
import java.util.HashMap; 
import java.util.Map; 
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException; 
import javax.management.DynamicMBean; 
import javax.management.InstanceAlreadyExistsException; 
import javax.management.InvalidAttributeValueException; 
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo; 
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationBroadcasterSupport; 
import javax.management.ObjectName;
import javax.management.ReflectionException; 
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
public class RrdKeeper extends NotificationBroadcasterSupport implements NotificationBroadcaster,   DynamicMBean{

	private static final String DOMAIN = "rrdMX";
	static RrdKeeper me = new RrdKeeper(); // jaja! natuerlich. singleton :-P... 
	static { 
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
            new MBeanInfo(
            	  this.getClass().getName(),
	              RrdKeeper.class.getName(),
	              getAttributeInfo(),
	              1==1?null:getConstructors(), 			//constructors
	              1==1?null:getOperations() ,        	//operations
	              1==1?null:getNotificationInfo()); 	//notifications
        return info;
    }
    private MBeanOperationInfo[] getOperations() { 
    	String descTmp = "resetCouters(...)";
		Method methodTmp =this.getClass().getMethods()[0];
		MBeanOperationInfo[] retval = 
			new MBeanOperationInfo[] { new MBeanOperationInfo(descTmp, methodTmp) };
		return retval;
		
	}
	private MBeanConstructorInfo[] getConstructors() { 
		Constructor constructor = null;
		String description = ""+constructor ;
		MBeanConstructorInfo[] retval  = null;
		try{
			constructor  = this.getClass().getConstructors()[0];
			MBeanConstructorInfo theOneConstr = 
				new MBeanConstructorInfo(description, constructor);
			retval = new MBeanConstructorInfo[]{theOneConstr };
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
                new MBeanAttributeInfo(
                		name,
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

	//@Override
	public void handleNotification(Notification notification, final Object handback) { 
		 sendNotification(notification);
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

	public void performNotification(String xpath, long timestamp, String data) {
		Notification notification = new //Notification(data, xpath,  timestamp );
		Notification("xpath", xpath, this.updateCounter, timestamp, data);
		sendNotification(notification );
	}	

}


 