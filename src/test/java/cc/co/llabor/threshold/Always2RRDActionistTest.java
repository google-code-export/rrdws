package cc.co.llabor.threshold;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;

import cc.co.llabor.threshold.rrd.Threshold;
import cc.co.llabor.threshold.syso.StdOutActionist;


/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  14.11.2011::16:22:55<br> 
 */
public class Always2RRDActionistTest extends TestCase {

	RrdDb rrdDb;
	RrdDef rrdDef;
	long startTime = 920800000L;

	private int stayCount = 0;
	private int stayCounter; // 920800000L == [Sun Mar 07 10:46:40 CET
	/**
	 * this method will simulate the short timeout for asynchron model of
	 * testing, when a lot of event stay on the queue for processing, to give a
	 * change to process it somehow ....
	 * 
	 * @author vipup
	 * @param capTmp
	 */
	public void testNotificationMVEL() throws RrdException, IOException {
			double baseLine = 80; // should be smart enough ;)
			double delta = 15;
			long tenMins = 60*10;  // repeat alert any 10 mins
			Threshold stdOutNotificator = new StdOutActionist( getRRDName(), 
					"!("+
					"rrd.lastDatasourceValues[0] > "+ (baseLine-delta) +" && "+
					"rrd.lastDatasourceValues[0] < "+ (baseLine+delta) +""+
					")" 
					  , tenMins);
			System.out.println(stdOutNotificator);
	//		testSinBaseLine(stdOutNotificator); 
			AlertCaptain capTmp = AlertCaptain.getInstance();
			capTmp.register(stdOutNotificator);
			Sample sample = rrdDb.createSample();
			long lastTimeTmp = -1;
			double lastSpeed = 0;
			//double baseLine = ((RddUpdateAlerter) headHunter).getBaseLine();
			// 1 Day to go...
			for (int secTmp = 1; secTmp < 60 * 60 * 12; secTmp += 11) {
				lastTimeTmp = startTime + secTmp;
				double d = 22 * Math.sin((.0001356 * secTmp));
				lastSpeed = d * Math.sin(Math.E * .000531 * secTmp);
				lastSpeed += baseLine;
				// Realdata production
				sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
				// observation of trarget rrd -- here will be simulation of Time!
				capTmp.tick(lastTimeTmp);
				//stayABit(capTmp);
			}
			capTmp.unregister(stdOutNotificator);
			
			int xTmp = ((StdOutActionist)stdOutNotificator).getNotificationCounter();
			
			if (capTmp.isAsync())
				assertTrue("! 7>x ("+xTmp+") > 11!", xTmp > 7 && xTmp < 14);
			else
				assertTrue("! 20>"+xTmp+" > 20!", xTmp == 9);
	
		}

	@Override
	protected void setUp() throws Exception {
	
		super.setUp();
		
		// 1999]
		rrdDef = new RrdDef(getRRDName());
		String path2RRD = rrdDef.getPath();
		File rrdFile = new File(path2RRD);
		if (rrdFile.exists()) {
			rrdFile.delete();
		}
		File rrdTholdFile = new File(path2RRD + "Thold.RRD");
		if (rrdTholdFile.exists()) {
			rrdTholdFile.delete();
		}
	
		rrdDef.setStartTime(startTime);
		rrdDef.setStep(55);
	
		rrdDef.addDatasource("speed", "GAUGE", 600, Double.NaN, Double.NaN);
	
		rrdDef.addArchive("AVERAGE", 0.5, 1, 3600);
		rrdDef.addArchive("AVERAGE", 0.5, 6, 700);
		rrdDef.addArchive("AVERAGE", 0.5, 24, 797);
		rrdDef.addArchive("AVERAGE", 0.5, 288, 775);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 1, 600);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 24, 797);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 1, 600);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 6, 700);
		rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 24, 797);
		rrdDef.addArchive(ConsolFuns.CF_LAST, 0.1, 24, 22111);
		rrdDb = new RrdDb(rrdDef);
		
		
		setUpSync();
	
	}
	
	

	
	protected void setUpSync() throws Exception {
	
		super.setUp(); 
		AlertCaptain.getInstance().setAsync(false);
	
	}
	
	private static final String TEST_RRD = "test.rrd";
	private String getRRDName() {
		return this.getClass().getName() + "_" + this.getName() + "_" + TEST_RRD;
	}

	@Override
	protected void tearDown() throws Exception {
		rrdDb.close();
		// close all RRDs..
		RrdDbPool instance;
	
		instance = RrdDbPool.getInstance();
		instance.reset();
	
		super.tearDown();
	
	}

	// NO notificationIntervalInSecs
	public void testNotificationMVEL2RRD() throws RrdException, IOException {
			double baseLine = 80; // should be smart enough ;)
			double delta = 15;
			long tenMins = 60*10;  // repeat alert any 10 mins
			MVELActionist stdOutNotificator = new StdOutActionist( getRRDName(), 
					"!("+
					"rrd.lastDatasourceValues[0] > "+ (baseLine-delta) +" && "+
					"rrd.lastDatasourceValues[0] < "+ (baseLine+delta) +""+
					")" 
					  , tenMins);
			System.out.println(stdOutNotificator);
	//		testSinBaseLine(stdOutNotificator);
			
			Always2RRDActionist a2rrd = new Always2RRDActionist(stdOutNotificator);
			AlertCaptain capTmp = AlertCaptain.getInstance();
			//capTmp.register(a2rrd);
			Sample sample = rrdDb.createSample();
			long lastTimeTmp = -1;
			double lastSpeed = 0;
			//double baseLine = ((RddUpdateAlerter) headHunter).getBaseLine();
			// 1 Day to go...
			for (int secTmp = 1; secTmp < 60 * 60 * 12; secTmp += 11) {
				lastTimeTmp = startTime + secTmp;
				double d = 22 * Math.sin((.0001356 * secTmp));
				lastSpeed = d * Math.sin(Math.E * .000531 * secTmp);
				lastSpeed += baseLine;
				// Realdata production
				sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
				// observation of trarget rrd -- here will be simulation of Time!
				capTmp.tick(lastTimeTmp);
				//stayABit(capTmp);
			}
			stdOutNotificator = (MVELActionist) capTmp.unregister(a2rrd);
			
			int xTmp = ((AbstractActionist)stdOutNotificator).getNotificationCounter();
			
			if (capTmp.isAsync())
				assertTrue("! 7>x ("+xTmp+") > 11!", xTmp > 7 && xTmp < 14);
			else
				assertTrue("! 20>"+xTmp+" > 20!", xTmp == 9);
	
		}
	// WITH notificationIntervalInSecs
	public void testNotificationMVEL_Notify2RRD() throws RrdException, IOException {
			double baseLine = 80; // should be smart enough ;)
			double delta = 15;
			long tenMins = 60*10;  // repeat alert any 10 mins
			MVELActionist stdOutNotificator = new StdOutActionist( getRRDName(), 
					"!("+
					"rrd.lastDatasourceValues[0] > "+ (baseLine-delta) +" && "+
					"rrd.lastDatasourceValues[0] < "+ (baseLine+delta) +""+
					")" 
					  , tenMins);
			System.out.println(stdOutNotificator);
	//		testSinBaseLine(stdOutNotificator);
			
			Always2RRDActionist a2rrd = new Always2RRDActionist(stdOutNotificator);
			AlertCaptain capTmp = AlertCaptain.getInstance();
			capTmp.register(a2rrd);
			Sample sample = rrdDb.createSample();
			long lastTimeTmp = -1;
			double lastSpeed = 0;
			//double baseLine = ((RddUpdateAlerter) headHunter).getBaseLine();
			// 1 Day to go...
			for (int secTmp = 1; secTmp < 60 * 60 * 12; secTmp += 11) {
				lastTimeTmp = startTime + secTmp;
				double d = 22 * Math.sin((.0001356 * secTmp));
				lastSpeed = d * Math.sin(Math.E * .000531 * secTmp);
				lastSpeed += baseLine;
				// Realdata production
				sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
				// observation of trarget rrd -- here will be simulation of Time!
				capTmp.tick(lastTimeTmp);
				stayABit(capTmp);
			}
			stdOutNotificator = (MVELActionist) capTmp.unregister(a2rrd);
			
			int xTmp = ((AbstractActionist)stdOutNotificator).getNotificationCounter();
			
			if (capTmp.isAsync())
				assertTrue("! 7>x ("+xTmp+") > 11!", xTmp > 7 && xTmp < 14);
			else
				assertTrue("! 20>"+xTmp+" > 20!", xTmp == 9);
	
		}

	private void stayABit(AlertCaptain capTmp) {
	
		if (capTmp.isAsync() && capTmp.getQueueLength() > 11010) {
			// try {
			System.out.println("QueueLen = " + capTmp.getQueueLength()
					+ "wait a sec:#" + stayCounter);
			try {
				Thread.sleep(1111);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}// setAsync
				// Thread.yield();//setAsync
				// capTmp.wait(1000);
			stayCounter++;
			System.out.println("#WakeCouter =" + capTmp.getWakeCounter()
					+ "  QueueLen = " + capTmp.getQueueLength()
					+ " stayCounter = " + stayCounter);
			System.out.println("AlertCaptain is "
					+ (capTmp.isAlive() ? " alive..." : "DEAD!"));
	
			if (!capTmp.isAlive()) {
				System.out.println("getProcessStart::"
						+ capTmp.getProcessStart());
				System.out.println("getProcessEnd::" + capTmp.getProcessEnd());
				if (capTmp.getLastExc() != null) {
					capTmp.getLastExc().printStackTrace();
				} else {
					return;
				}
			} else {
				System.out.println("getProcessStart::"
						+ capTmp.getProcessStart());
				System.out.println("getProcessEnd::" + capTmp.getProcessEnd());
				System.out.println("capTmp.getLastExc()::"
						+ capTmp.getLastExc());
				stayCount++;
				if (stayCount < 10111) {
					try {
						capTmp.notifyAll();
					} catch (Throwable e) {
					}
				} else {
					capTmp.kill();
					throw new RuntimeException("let me out!");
				}
			}
			// } catch (Exception e) {//Interrupted
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }catch (Throwable e) {//Interrupted
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

}


 