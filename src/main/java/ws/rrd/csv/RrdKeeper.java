package ws.rrd.csv;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;


 

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  07.09.2011::17:10:50<br> 
 */
public class RrdKeeper implements NotificationBroadcaster, DynamicMBean{

	private static final String DOMAIN = "rrdMX";
	static RrdKeeper me = new RrdKeeper(); // jaja! natuerlich. singleton :-P...
	
	private RrdKeeper(){
		super();
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void init() throws Exception {
        MBeanServer bs =
            ManagementFactory.getPlatformMBeanServer();    	
        ObjectName name = new ObjectName(DOMAIN + ":" + "type=" + ""+this.getClass().getName());
        try{
        	bs.registerMBean(this, name);
        }catch(InstanceAlreadyExistsException e)
        {
        	MBeanInfo oldOne = bs.getMBeanInfo(name);
        	System.out.println("ungeristered MBean:"+oldOne);
        	bs.unregisterMBean(name);
        	bs.registerMBean(this, name);
        }
        
   }
	
	public static RrdKeeper getInstance() {
		return me;
	}
	@Override
	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		}
	}
	@Override
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		}
	}
	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		return null;
		}
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReflectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		return null;
		}
	}
	private MBeanConstructorInfo[] getConstructors() {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.09.2011");
		else {
		return null;
		}
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
    	 
		syncValues();
    	
    	
    	
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
    	long startTmp = System.currentTimeMillis();
		
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
    	
    	long nowTmp = System.currentTimeMillis();
    	_metrics .put("timePerSync",   new Long(startTmp - nowTmp  ));
    	long sinceLastMsTmp = nowTmp - lastSyncTimeMilliseconds;
		_metrics .put("timeSinceLastSync",   new Long(sinceLastMsTmp));
		double updatesPerSecond = updateCounter - lastUpdatesCounter ;
		_metrics .put("updateDelta",   new Double(updatesPerSecond));
		
		updatesPerSecond = sinceLastMsTmp>0?(1000*updatesPerSecond)/sinceLastMsTmp:updatesPerSecond;
		lastUpdatesCounter  = updateCounter ;
		
		_metrics .put("updatesPerSecond",   new Double(updatesPerSecond));

		lastSyncTimeMilliseconds = nowTmp ;

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

}


 