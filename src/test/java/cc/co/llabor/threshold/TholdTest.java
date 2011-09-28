package cc.co.llabor.threshold;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import junit.framework.TestCase;
import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDef;
import cc.co.llabor.threshold.rrd.Threshold;

public class TholdTest extends TestCase {
	RrdDef rrdDef;
	RrdDb rrdDb;
	long startTime = 920800000L;
	private int stayCounter; // 920800000L == [Sun Mar 07 10:46:40 CET
	 
	
	private String getRRDName(){
		return this.getClass().getName()+"_"+this.getName()+"_"+TEST_RRD;
	}
	@Override
	protected void setUp() throws Exception {

		super.setUp();

		// 1999]
		rrdDef = new RrdDef(getRRDName());
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
	private static final String TEST_RRD = "test.rrd";
	public void testExecuteSinHighAlert() throws RrdException, IOException {

		int MAX_POSSIBLE_IQ = 200;
		double hiLimit = 130; // should be smart enough ;)
		long tenSecondds = 1111; // 10 sec is maximal time to start to do
									// something...
		Threshold headHunter = new HighAlerter(getRRDName(), hiLimit, tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();

		capTmp.register(headHunter);

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 24; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = MAX_POSSIBLE_IQ * Math.sin((.0001356 * secTmp));
			lastSpeed = d * Math.sin(.000531 * secTmp);
			lastSpeed += 100;
			// Realdata production 
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);

		}
		 

		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		graphDef.datasource("HighLIMIT", "" + hiLimit);
		graphDef.line("HighLIMIT", new Color(0, 0x33, 0xAA), "minimal_IQ", 4);

		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);

		// .stat.rrd
		graphDef.datasource("myspeedAlert",  getTholdName(), "speed",
				"AVERAGE");
		graphDef.line("myspeedAlert", new Color(0, 0xFF, 0), "recruit IT!", 4);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");
		capTmp.unregister(headHunter);
	}
	private String syncToText() {
		return  AlertCaptain.getInstance().isAsync()?"!ASYNC!":"_sync_";
	}
	private void stayABit(AlertCaptain capTmp) {
		
		if (capTmp.isAsync() && capTmp.getQueueLength() > 11010){
			try {
				System.out.println("QueueLen = "+capTmp.getQueueLength()+"wait a sec:#"+stayCounter);
				Thread.sleep(1111);//setAsync
				//Thread.yield();//setAsync
				//capTmp.wait(1000);
				stayCounter++;
				System.out.println("#WakeCouter ="+capTmp.getWakeCounter()+"  QueueLen = "+capTmp.getQueueLength()+" stayCounter = "+stayCounter);
			} catch (Exception e) {//Interrupted
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void testExecutePureSin() throws RrdException, IOException {

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 24; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			lastSpeed += 100.0 * Math.sin((.0003 * secTmp));
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
		}
 

		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename("all2.gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());
		graphDef.datasource("myspeedMAX", getRRDName(), "speed", ConsolFuns.CF_MAX);
		graphDef.line("myspeedMAX", new Color(0xFF, 0xAF, 0xAF), "max", 2);
		graphDef.datasource("myspeedMIN", getRRDName(), "speed", ConsolFuns.CF_MIN);
		graphDef.line("myspeedMIN", new Color(0xFF, 0xAF, 0xFF), "min", 2);
		graphDef.datasource("myspeedLAST", getRRDName(), "speed",
				ConsolFuns.CF_LAST);
		graphDef.line("myspeedLAST", new Color(0x1F, 0x1F, 0x1F), "last", 3);
		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);

		graphDef.datasource("LowLIMIT", "55000");
		graphDef.line("LowLIMIT", new Color(0, 0xAA, 0xFF), "LOW", 1);
		graphDef.datasource("HighLIMIT", "550000");
		graphDef.line("HighLIMIT", new Color(0, 0xFF, 0xAA), "HI", 1);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");

	}
	public void testExecuteSin() throws RrdException, IOException {

		int MAX_POSSIBLE_IQ = 200;

		double hiLimit = 130; // should be smart enough ;)
		long tenSecondds = 10; // 10 sec is maximal time to start to do
								// something...
		Threshold headHunter = new HighAlerter(getRRDName(), hiLimit, tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();

		capTmp.register(headHunter);

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 24; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = MAX_POSSIBLE_IQ * Math.sin((.0001356 * secTmp));
			lastSpeed = d * Math.sin(.000531 * secTmp * secTmp);
			lastSpeed += 100;
			// Realdata production
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);
			
		}
		capTmp.unregister(headHunter);

		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		graphDef.datasource("HighLIMIT", "" + hiLimit);
		graphDef.line("HighLIMIT", new Color(0, 0x33, 0xAA), "minimal_IQ", 4);

		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);

		// .stat.rrd
		graphDef.datasource("myspeedAlert", getTholdName(), "speed",
				"AVERAGE");
		graphDef.line("myspeedAlert", new Color(0, 0xFF, 0), "recruit IT!", 4);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");

	}
	public void testExecuteSinPow2() throws RrdException, IOException {

		int MAX_POSSIBLE_IQ = 140;

		double hiLimit = 60; // should be smart enough ;)
		long tenSecondds = 10; // 10 sec is maximal time to start to do
								// something...
		Threshold headHunter = new HighAlerter(getRRDName(), hiLimit, tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();

		capTmp.register(headHunter);

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 12; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = 140 * Math.sin((.0001356 * secTmp));
			lastSpeed = d * Math.sin(Math.E * .000531 * secTmp * secTmp);

			// lastSpeed = lastSpeed>0?lastSpeed:-lastSpeed;

			// Realdata production
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);
		}
		capTmp.unregister(headHunter);

		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		graphDef.datasource("HighLIMIT", "" + hiLimit);
		graphDef.line("HighLIMIT", new Color(0, 0x33, 0xAA), "minimal_IQ", 4);

		// .stat.rrd
		graphDef.datasource("myspeedAlert", getTholdName(), "speed",
				"AVERAGE");
		graphDef.line("myspeedAlert", new Color(0, 0xFF, 0), "recruit IT!", 3);
		graphDef.datasource("myspeedAlertMAX", getTholdName(), "speed",
				ConsolFuns.CF_MAX);
		graphDef.line("myspeedAlertMAX", new Color(0xAF, 0xAF, 0xFF),
				"recruitMAX", 2);
		graphDef.datasource("myspeedAlertMIN", getTholdName(), "speed",
				ConsolFuns.CF_MIN);
		graphDef.line("myspeedAlertMIN", new Color(0, 0xFF, 0xFF),
				"recruitMIN", 1);

		// f(x)
		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 1);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");

	}
	private String getTholdName() {
		return getRRDName() + ".Thold.RRD";
	}

	public void testExecuteSinLowAlert() throws RrdException, IOException {

		int MAX_POSSIBLE_IQ = 200;

		double lowLimit = 40; // should be smart enough ;)
		long tenSecondds = 1111; // 10 sec is maximal time to start to do
									// something...
		Threshold debiKiller = new LowAlerter(getRRDName(), lowLimit, tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();

		capTmp.register(debiKiller);

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 24; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = MAX_POSSIBLE_IQ * Math.sin((.0001356 * secTmp));
			lastSpeed = d * Math.sin(.000531 * secTmp);
			lastSpeed += 100;
			// Realdata production
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);
		}
		capTmp.unregister(debiKiller);

		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		graphDef.datasource("LowLIMIT", "" + lowLimit);
		graphDef.line("LowLIMIT", new Color(0, 0x33, 0xAA), "minimal_IQ", 4);

		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);

		// .stat.rrd
		graphDef.datasource("myspeedAlert", getTholdName(), "speed",
				"AVERAGE");
		graphDef.line("myspeedAlert", new Color(0, 0xFF, 0), "terminate IT!", 4);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");

	}
	public void testExecuteSinBaseLine() throws RrdException, IOException {

		int MAX_POSSIBLE_IQ = 140;

		double baseLine = 80; // should be smart enough ;)
		double delta = 15;
		long tenSecondds = 10; // 10 sec is maximal time to start to do
								// something...
		Threshold headHunter = new BaselineAlerter(getRRDName(), baseLine, delta,
				tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();

		capTmp.register(headHunter);

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 12; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = 22 * Math.sin((.0001356 * secTmp));
			lastSpeed = d * Math.sin(Math.E * .000531 * secTmp);
			lastSpeed += baseLine;
			// lastSpeed = lastSpeed>0?lastSpeed:-lastSpeed;

			// Realdata production
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);
		}
		capTmp.unregister(headHunter);

		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		graphDef.datasource("BaseLINE", "" + baseLine);
		graphDef.line("BaseLINE", new Color(0, 0x33, 0xAA), "BaseLINE", 4);
		// delta
		graphDef.datasource("BaseLINEHi", "" + (baseLine + delta));
		graphDef.line("BaseLINEHi", new Color(0, 0x33, 0x66), "highest", 2);
		graphDef.datasource("BaseLINELow", "" + (baseLine - delta));
		graphDef.line("BaseLINELow", new Color(0, 0x66, 033), "lowest", 2);

		// .stat.rrd
		graphDef.datasource("myspeedAlert", getTholdName(), "speed",
				"AVERAGE");
		graphDef.area("myspeedAlert", new Color(0xFF, 0x5F, 0), "AA!");
		graphDef.datasource("myspeedAlertMAX", getTholdName(), "speed",
				ConsolFuns.CF_MAX);
		graphDef.line("myspeedAlertMAX", new Color(0xAF, 0x3F, 0x3F),
				"countDown started", 1);
		graphDef.datasource("myspeedAlertMIN", getTholdName(), "speed",
				ConsolFuns.CF_MIN);
		graphDef.line("myspeedAlertMIN", new Color(0, 0xFF, 0xFF), "getReady",
				1);

		// f(x)
		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0x5F, 0xFF, 0), "F(t)", 1);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");

	}
	public void testExecuteSinBaseLinePow2() throws RrdException, IOException {

		int MAX_POSSIBLE_IQ = 140;

		double baseLine = 80; // should be smart enough ;)
		double delta = 15;
		long tenSecondds = 10; // 10 sec is maximal time to start to do
								// something...
		Threshold headHunter = new BaselineAlerter(getRRDName(), baseLine, delta,
				tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();

		capTmp.register(headHunter);

		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 12; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = 122 * Math.sin((.00001356 * secTmp));
			lastSpeed = d * Math.sin(Math.E * .000531 * secTmp);
			lastSpeed += baseLine;
			// lastSpeed = lastSpeed>0?lastSpeed:-lastSpeed;

			// Realdata production
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);
		}
		capTmp.unregister(headHunter);

		 
		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		graphDef.datasource("BaseLINE", "" + baseLine);
		graphDef.line("BaseLINE", new Color(0, 0x33, 0xAA), "BaseLINE", 4);
		// delta
		graphDef.datasource("BaseLINEHi", "" + (baseLine + delta));
		graphDef.line("BaseLINEHi", new Color(0, 0x33, 0x66), "highest", 2);
		graphDef.datasource("BaseLINELow", "" + (baseLine - delta));
		graphDef.line("BaseLINELow", new Color(0, 0x66, 033), "lowest", 2);

		// .stat.rrd
		graphDef.datasource("myspeedAlert", getTholdName(), "speed",
				"AVERAGE");
		graphDef.area("myspeedAlert", new Color(0xFF, 0x1F, 0x1F), "AA!");
		graphDef.datasource("myspeedAlertMAX", getTholdName(), "speed",
				ConsolFuns.CF_MAX);
		graphDef.line("myspeedAlertMAX", new Color(0xAF, 0x3F, 0x3F),
				"countDown started", 1);
		graphDef.datasource("myspeedAlertMIN", getTholdName(), "speed",
				ConsolFuns.CF_MIN);
		graphDef.line("myspeedAlertMIN", new Color(0, 0xFF, 0xFF), "getReady",
				1);

		// f(x)
		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0x5F, 0xFF, 0), "F(t)", 1);

		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");
	}
	public void testExecuteNoisySinLowAlert() throws RrdException, IOException {
	
		int MAX_POSSIBLE_IQ = 200;
	
		double lowLimit = 60; // should be smart enough ;)
		long tenSecondds = 10; // 10 sec is maximal time to start to do
									// something...
		Threshold dumbKiller = new LowAlerter(getRRDName(), lowLimit, tenSecondds);
		AlertCaptain capTmp = AlertCaptain.getInstance();
	
		capTmp.register(dumbKiller);
	
		Sample sample = rrdDb.createSample();
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for (int secTmp = 1; secTmp < 60 * 60 * 24; secTmp += 1) { // 1 Day
			lastTimeTmp = startTime + secTmp;
			double d = MAX_POSSIBLE_IQ * Math.sin((.0001356 * secTmp));
			lastSpeed = d * Math.sin(.000531 * secTmp);
			lastSpeed +=   Math.random()*120.0;
			// Realdata production
			sample.setAndUpdate("" + (lastTimeTmp) + ":" + (lastSpeed));
			// observation of trarget rrd -- here will be simulation of Time!
			capTmp.tick(lastTimeTmp);
			stayABit(capTmp);
		}
		capTmp.unregister(dumbKiller);
	
		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime - 10 * 60);// seconds!
		graphDef.setEndTime(lastTimeTmp + 10 * 60);// seconds
		graphDef.setFilename(getRRDName() + ".gif");
		graphDef.setWidth(640);
		graphDef.setHeight(480);
		graphDef.setVerticalLabel(" " + new Date(startTime * 1000) + " - "
				+ new Date(lastTimeTmp * 1000));
		graphDef.setTitle("Test_"+ syncToText()+getRRDName());

		
		// .stat.rrd
		graphDef.datasource("myspeedAlertMIN", getTholdName(), "speed", ConsolFuns.CF_MIN );
		graphDef.area("myspeedAlertMIN", new Color(0x77,0xA7,  0xA7), "tMIN");
		graphDef.datasource("myspeedAlertMAX", getTholdName(), "speed", ConsolFuns.CF_MAX  );
		graphDef.area("myspeedAlertMAX", new Color(0xFF,0,  0), "tMAX");
		graphDef.datasource("myspeedAlert", getTholdName(), "speed", "AVERAGE");
		graphDef.area("myspeedAlert", new Color(0xFF,0xFF, 0), "terminate IT!");
		
		
		graphDef.datasource("LowLIMIT", "" + lowLimit);
		graphDef.line("LowLIMIT", new Color(0, 0x33, 0xAA), "dumb_IQ", 4);
	
		graphDef.datasource("myspeed", getRRDName(), "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0, 0, 0), "F(t)", 2);
		graphDef.datasource("myspeedMAX", getRRDName(), "speed", ConsolFuns.CF_MAX );
		graphDef.line("myspeedMAX", new Color(0x77, 0xFF,  0), "F(t)", 2);
		graphDef.datasource("myspeedMIN", getRRDName(), "speed", ConsolFuns.CF_MIN);
		graphDef.line("myspeedMIN", new Color(0, 0x77, 0xFF), "F(t)", 2);

//		graphDef.datasource("Alert", getTholdName(), "speed", "AVERAGE");
//		graphDef.area("Alert", new Color(0xFF, 0x1F, 0), "terminate IT!");
	
		RrdGraph graph = new RrdGraph(graphDef);
		// graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ, MAX_POSSIBLE_IQ,
				BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code: System.out.println("--");
	
	}

}
