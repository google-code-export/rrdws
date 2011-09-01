package org.jrobin.thold;
 
import java.io.IOException;
import java.util.ArrayList;
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
	public AlertCaptain() {
		this( new LinkedList<CheckPoint>());
	}
	AlertCaptain ( Queue<CheckPoint> q){
		this.queue = q;
		log.info(Repo.getBanner( "AlertCaptain"));
	}
	public void register(Threshold e) {
		ToDo.add(e);
	}
	public void tick() {
		long timestamp = System.currentTimeMillis();
		this.tick(  timestamp );
	}
	public void tick(long timestamp) {
		 wakeUp(timestamp);
	}
	
	public static final boolean IS_ASYNC = false;
	/**
	 * put the time-related-job into queue
	 * 
	 * @author vipup
	 * @param timestamp
	 */
	private void wakeUp(long timestamp) {
		for (Threshold toCheck:this.ToDo){  
			CheckPoint e = new CheckPoint(timestamp, toCheck );
			if (IS_ASYNC){
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
				try {
					CheckPoint charlieTmp = queue.poll();
					processData(charlieTmp ); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			Datasource dsTmp = rrd.getDatasource("speed");
			double val = dsTmp.getLastValue(); 
			
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
	
}


 