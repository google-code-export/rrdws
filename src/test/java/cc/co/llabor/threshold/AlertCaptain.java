package cc.co.llabor.threshold;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue; 
import org.collectd.DataWorker;
import org.jrobin.core.Datasource;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import cc.co.llabor.features.Repo;
import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  30.08.2011::15:59:53<br> 
 */
public class AlertCaptain implements Runnable{

	public static AlertCaptain getInstance() { 
		return myself; 
	}

	List<Threshold> ToDo = new ArrayList<Threshold>();
	private static final Logger log = LoggerFactory.getLogger(DataWorker.class .getName());
	private boolean isAlive = true;
	
	
	private java.util.Queue<CheckPoint> queue;
	private int wakeCounter;
	private boolean isAsync = false;
	
	public boolean isAsync() { 
			return isAsync;
	}
	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}
	public int getWakeCounter() { 
			return wakeCounter;
	}
	public int getQueueLength(){
		return queue.size(); 
	}
	public AlertCaptain() {
		this( new LinkedList<CheckPoint>() );
	}
	AlertCaptain ( Queue<CheckPoint> q){
		isAsync = true;
		if (isAsync){
			this.queue = q;
			Thread th = new Thread(this, "AlertCaptain");
			th .setPriority(Thread.MAX_PRIORITY/2+Thread.MAX_PRIORITY/4); // 75%
			th.start();
		}
		log.info(Repo.getBanner( "AlertCaptain"));
	}
	public void register(Threshold e) {
		ToDo.add(e);
	}
	public void tick() {
		long timestamp = System.currentTimeMillis();
		this.tick(  timestamp /1000);
	}
	
	public void tick(long timestamp) {
		 wakeUp(timestamp);
	}
	 
	/**
	 * put the time-related-job into queue
	 * OR 
	 * process it syncronously
	 * 
	 * 
	 * @author vipup
	 * @param timestamp
	 */
	private void wakeUp(long timestamp) {
		wakeCounter++;
		for (Threshold toCheck:this.ToDo){  
			CheckPoint e = new CheckPoint(timestamp, toCheck );
			if (this.isAsync){
				queue.add(e);
			}else{
				processData(e ); 
			}
		}
	}

	public void kill(){
		isAlive = false;
	}
	public void run() {
		while(isAlive ){ 
			if (queue.isEmpty()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}else{
				CheckPoint charlieTmp =null;
				try {
					charlieTmp = queue.peek() ;
					if (charlieTmp ==null){
						//notifyAll();
						continue;
					}
					processData(charlieTmp ); 
					queue.remove(charlieTmp);
				} catch (	java.util.NoSuchElementException	e){
					if (charlieTmp !=null)
						charlieTmp.processError(e);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//charlieTmp.processError(e);
					//e.printStackTrace();
				}catch( Throwable e){
					//e.printStackTrace();
				} finally{
					
				}
			}
		}
		log.info(Repo.getBanner( "+AlertCaptain"));
	}

	private void processData(CheckPoint charlieTmp) { 
		Threshold toCheck = charlieTmp.getToCheck();
		String rrdDef = toCheck.getDatasource();
		try {
			RrdDb rrd = RrdDbPool.getInstance().requestRrdDb(rrdDef );
			double val  = 0;
			// synch mode
			if (this.isAsync){
				val = charlieTmp.getValue();
			}else{
				Datasource dsTmp = rrd.getDatasource("speed");
				val = dsTmp.getLastValue();
			}
			
			toCheck.checkIncident(val, charlieTmp.timestamp);
			toCheck.reactIncidentIfAny(charlieTmp.timestamp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	 

	static AlertCaptain myself = new AlertCaptain();
	public void unregister(Threshold headHunter) {
		try{
			if (ToDo.indexOf( headHunter )>=0){
				boolean o = ToDo.remove(headHunter);
				headHunter.stop();
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
 
	
}


 