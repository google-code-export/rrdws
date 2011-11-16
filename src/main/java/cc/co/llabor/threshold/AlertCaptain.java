package cc.co.llabor.threshold;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import net.sf.jsr107cache.Cache;

import org.jrobin.core.Datasource;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.rrd.csv.RrdKeeper;
import cc.co.llabor.cache.Manager;
import cc.co.llabor.features.Repo;
import cc.co.llabor.threshold.rrd.Threshold;
import cc.co.llabor.threshold.syso.StdOutActionist;

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 30.08.2011::15:59:53<br>
 */
public class AlertCaptain implements Runnable, NotificationListener {



	static boolean inited = false;

	public static AlertCaptain getInstance() {
		if (!inited) {
			synchronized (Thread.class) {
				if (!inited) {
					if (myself.isAsync && !myself.isAlive) {
						System.out.println("starting ....");
						Thread th = new Thread(myself, "AlertCaptain");
						th.setPriority(Thread.MAX_PRIORITY / 2
								+ Thread.MAX_PRIORITY / 4); // 75%
						myself.isAlive = true;
						th.start();
						System.out.println("...done.");
						inited = true;
					}
				}
			}
		}
		return myself;
	}

	List<Threshold> ToDo = new ArrayList<Threshold>();
	private static final Logger log = LoggerFactory
			.getLogger(AlertCaptain.class.getName());
	private boolean isAlive = false;

	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	private java.util.Queue<CheckPoint> queue;
	private int wakeCounter;
	private boolean isAsync = false;
	private Throwable lastExc;
	public Throwable getLastExc() {
		return lastExc;
	}

	private long processStart;
	public long getProcessStart() {
		return processStart;
	}

	private long processEnd;
	private  Collection<Threshold> unmodifiableCollection = new HashSet<Threshold>();
	 

	public long getProcessEnd() {
		return processEnd;
	}
	public boolean isAsync() {
		return isAsync;
	}
	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}
	public int getWakeCounter() {
		return wakeCounter;
	}
	public int getQueueLength() {
		return queue.size();
	}
	public AlertCaptain() {
		this(new ConcurrentLinkedQueue<CheckPoint>());
	}
	AlertCaptain(Queue<CheckPoint> q) {
		isAsync = true;
		this.queue = q;
		log.info(Repo.getBanner("AlertCaptain"));
	}
	public void register(Threshold e) { 
		ToDo.add(e);
		NotificationListener listener = this;
		NotificationFilter filter = null;
		Object handback = null;
		RrdKeeper.getInstance().addNotificationListener(listener , filter , handback );
		syncUC();
	}
	
	public void tick() {
		long timestamp = System.currentTimeMillis();
		this.tick(timestamp / 1000);
	}

	public void tick(long timestampInSeconds) {
		wakeUp(timestampInSeconds);
	}

	/**
	 * put the time-related-job into queue OR process it synchronously
	 * 
	 * @author vipup
	 * @param timestamp
	 */
	private void wakeUp(long timestampInSeconds) {
		wakeCounter++;
		String rrdDef = null;
		RrdDbPool instance = null;
		for (Threshold activist : unmodifiableCollection ) {
			try{
				
				if (this.isAsync) {
					CheckPoint e = new CheckPoint(timestampInSeconds, activist);
					queue.add(e);
				} else {
					//processData(e);
					rrdDef = activist.getDatasource();
					instance = RrdDbPool.getInstance();
					RrdDb rrd = instance.requestRrdDb(rrdDef);										
  					String dsName = activist.getDsName();//"speed";
 					Datasource dsTmp = rrd.getDatasource(dsName);
 					double val = dsTmp.getLastValue();
					processData(val, timestampInSeconds, activist);
				}
			}catch(Exception e){
				
				// TODO ???????
				if (1==2)e.printStackTrace();
				unregister(activist);
			}
		}
	}


	public void kill() {
		isAlive = false;
	}
	public void run() {
		log.info(Repo.getBanner("AlertCaptain"));
		while (isAlive) { // show must go on..
			try {
				if (queue.isEmpty()) {
					wait100();
				} else {
					CheckPoint charlieTmp = null;
					try {
						charlieTmp = queue.peek();
						if (charlieTmp == null) {
							continue;
						}
						processStart = charlieTmp.getTimestamp();
						processData(charlieTmp);
						processEnd = charlieTmp.getTimestamp();
						queue.remove(charlieTmp);
					} catch (java.util.NoSuchElementException e) {
						if (charlieTmp != null)
							charlieTmp.processError(e);
					} catch (Exception e) {
						lastExc = e;
					} catch (Throwable e) {
						lastExc = e;
					}
				}
			} catch (Throwable e) {
				lastExc = e;
			} finally {
				// isAlive = false;
			}
		}
		System.out
				.println(" |nasd;famsadflasmdfhq;wrxqweuiqwxrf;heqx,rqlwexr,qwexr,qeurx..  ");
		log.info(Repo.getBanner("+AlertCaptain"));
	}
	public void wait100() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//Async
	private void processData(CheckPoint charlieTmp) {
		Threshold activist = charlieTmp.getToCheck();
		double val = charlieTmp.getValue();
		processData(val, charlieTmp.timestamp, activist);
	}
	
	//sync
	private void processData(double val, long timestampInSeconds, Threshold activist) {
 		
//		String rrdDef = activist.getDatasource();
//		try {
//			RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdDef);
			 
			// synch mode
//			if (this.isAsync) {
//				val = charlieTmp.getValue();
//			} else {
//				String dsName = activist.getDsName();//"speed";
//				Datasource dsTmp = rrd.getDatasource(dsName);
//				val = dsTmp.getLastValue();
//			}
			((AbstractAlerter)activist).performChunk(timestampInSeconds, val);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RrdException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	static AlertCaptain myself = new AlertCaptain();
	public Threshold unregister(Threshold activist) {
		try {
			if (ToDo.indexOf(activist) >= 0) {
				boolean o = ToDo.remove(activist);
				if (o){
					syncUC();
					activist.stop();
					return activist;
				}
					
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Threshold unregister(CompositeAlerter activist) {
		Threshold retval = null;
		for (Threshold actToDel :activist.chainOfAlerters){
			retval = unregister(actToDel);
		}
		return retval;
	}
	
	
	public Collection<Threshold> list(){
		return unmodifiableCollection;
	}
	void syncUC() {
		unmodifiableCollection = Collections.unmodifiableCollection(  new HashSet<Threshold>( this.ToDo ));
		//System.out.println(unmodifiableCollection);
	}
	public static Properties toProperties(Threshold thTmp) throws TholdException {
		Properties retval = new Properties();
		retval.setProperty(Threshold.CLASS, thTmp.getClass().getName());
//		this.action =props.getProperty("action") ;
//		this.actionArgs =props.getProperty("actionArgs") ;
//		this.rrdName =props.getProperty("datasource") ;
//		this.dsName =props.getProperty("dsName") ;
//		this.type =props.getProperty("monitorType") ;
//		this.monitorArgs =props.getProperty("monitorArgs") ;
//		this.activationTimeoutInSeconds = Integer.parseInt( props.getProperty("spanLength" ));
//		this.baseLine = Double.parseDouble(  props.getProperty("BaseLine")); 		

		retval.setProperty(Threshold.ACTION,thTmp.getAction()) ;
		retval.setProperty(Threshold.ACTION_ARGS,thTmp.getActionArgs() ) ;
		retval.setProperty(Threshold.DATASOURCE,thTmp.getDatasource()  ) ;
		retval.setProperty(Threshold.DS_NAME,thTmp.getDsName() ) ;
		retval.setProperty(Threshold.MONITOR_TYPE,thTmp.getMonitorType() ) ;
		retval.setProperty(Threshold.MONITOR_ARGS,thTmp.getMonitorArgs() ) ;
		retval.setProperty(Threshold.SPAN_LENGTH ,""+thTmp.getSpanLength()  ) ;
		retval.setProperty(Threshold.BASE_LINE,""+thTmp.getBaseLine() ) ;	
		
		return retval;
	}
	
	public static final String cacheNS = AlertCaptain.class.getName();
	
	public static Threshold restoreByName(String namePar) throws TholdException{
		Cache c = Manager.getCache(cacheNS);
 		Object storedProps = c.get(namePar+".properties");
		Threshold outTmp = toThreshold(storedProps) ;
		return outTmp;
	}
	
	public static void storeToName(String namePar, Threshold toStorePar) throws TholdException{ 
		Properties properties = toStorePar.toProperties();
		String fnTmp = namePar+".properties";
		storeToName(fnTmp, properties); 		
	}
	public static void storeToName(String namePar, Properties propsPar) throws TholdException{
		Cache c = Manager.getCache(cacheNS);
 		String fnTmp = namePar.endsWith(".properties")?namePar:namePar+".properties";
		c.put(fnTmp, propsPar); 		
	}	
	
	public static Threshold toThreshold(Object thTmp) throws TholdException {
		try {
			if (thTmp instanceof Properties) {
				Properties p = (Properties) thTmp;
				String clazz = "" + p.getProperty(Threshold.CLASS);
				Class cl = Class.forName(clazz);

				Class[] parameterTypes = new Class[]{Properties.class};
				Constructor<Threshold>  constructor = cl.getConstructor( parameterTypes );//instance.register( theNext );
				
				Threshold retval = (Threshold) constructor.newInstance(p );
				return retval;
			} else {
				throw new TholdException("invalid Thold def:" + thTmp);
			}
		} catch (ClassNotFoundException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		} catch (InstantiationException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		} catch (IllegalAccessException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		} catch (SecurityException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		} catch (NoSuchMethodException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		} catch (IllegalArgumentException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		} catch (InvocationTargetException e) {
			throw new TholdException("invalid Thold def:" + thTmp, e);
		}
	}
	@Override
	public void handleNotification(Notification notification, Object handback) {
		long ts = notification.getTimeStamp();
		//System.out.println(""+notification+handback);
		//log.error("#"+ts+"::"+notification+handback);
		this.tick(ts /1000);
	}

}
