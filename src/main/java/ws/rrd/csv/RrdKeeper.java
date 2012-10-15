package ws.rrd.csv;
 
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method; 
import java.util.HashMap; 
import java.util.Map; 
import java.util.Properties;

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

import net.sf.jsr107cache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.rrd.pid.arduino.Pid;

import cc.co.llabor.cache.Manager;

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
        	if ( !isPID(name) ){
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
        	}else{ // still the same as above
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
        }        
        return attrs;
    }

	public boolean isPID(String name) {
		return name.indexOf("::")>0;
	}
    long lastSyncTimeMilliseconds = -1;
    long lastUpdatesCounter = -1;
	private double health;
	private double healthFactor; 

	/**
	 * accumulate new absolute value and retunt diff from prev
	 * @author vipup
	 * @param namePar
	 * @param newVal
	 * @return
	 */
	double accumulateValue(String namePar, double newVal){
		double retval = 0;
		try{
			String s = ""+ _metrics .get(namePar);
			_metrics .put(namePar,newVal);
			retval = Double.parseDouble( s);
			retval = newVal - retval;
		}catch(Exception e){}
		return retval;
	}
	
	private void syncValues() {
    	long startTmp = System.currentTimeMillis();

    	health += -100.0 * accumulateValue("loggedFatal",loggedFatal); 
    	health += -10.0 * accumulateValue("loggedError",loggedError); 
    	health += -10.0 * accumulateValue("loggedWarn",loggedWarn);    
    	health += 1.0 * accumulateValue("loggedInfo",loggedInfo);   
    	health += 10.0 * accumulateValue("loggedDebug",loggedDebug); 
    	health += 100.0 * accumulateValue("loggedTrace",loggedTrace); 

    	health += healthFactor ;
    	
    	
    	_metrics .put("health",new Double( health ) );

    	
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
    	// here is some statistical calculations 
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
    	
    	// here is some PID - calculations ()
    	if ( sinceLastMsTmp  >5*1000){ // 5 sec ((((also 1 per min ??
    		String [] names = _metrics.keySet().toArray(new String[]{});
			for (String name : names) {
    			  if ( isPID(name) )continue; // only 1-st level of metrics will be processed with PID-controller
    	          if (!"health".equals(name))continue; 
    	          Pid pid = getPid(name);
    	          Number val = _metrics.get(name);
    	          double input =   val.doubleValue();
    	          // calculate integrated value
    	          double newVal = pid.Compute(input);
    	          healthFactor = newVal ; ///????
    	          storeVpid( name, pid);
    	          _metrics .put(name+"::"+"newVal",    new Double(newVal ) ); 
    	          
    	          Properties pidProps = pid.toProperties();
    	          for (String key:pidProps.keySet().toArray(new String[]{})){
    	        	  _metrics .put(name+"::"+key,new Double(pidProps.getProperty(key)));
    	          }
    	     }     
    	}
	}
    
    
    
    
	private Pid getPid(String name) {
		String name2 = Pid.class.getName();
		Cache _vpids = Manager.getCache(name2); 
		String persistenceName = name+".properties";
		Properties pVpid = (Properties) _vpids.get(persistenceName );
		Pid retval = null;
		try{
			 retval  = new Pid (pVpid);
		}catch(Exception e){e.printStackTrace();}
		
		if (retval==null){
 
			retval = new Pid( 1, -1,100, .123, .135, .012, Pid.DIRECT);
			storeVpid( name, retval);
		}
		return retval;
	}

	public void storeVpid( String name, Pid retval) {
		Cache _vpids = Manager.getCache(Pid.class.getName()); 
		String persistenceName = name+".properties";		
		_vpids.put(persistenceName, retval.toProperties());
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


 