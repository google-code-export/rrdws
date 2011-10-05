package cc.co.llabor.threshold;  

import java.io.File;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;

import net.sf.jsr107cache.Cache;
import cc.co.llabor.cache.Manager;
import cc.co.llabor.threshold.rrd.Threshold; 
import junit.framework.TestCase;

public class TholdIniTest extends TestCase {
	private static final String cacheNS = "thold";
	public void testInitFromFile() throws TholdException{
		String tholds = "1,2,3,4,5"; 
		AlertCaptain ac = AlertCaptain.getInstance();
		for (String tholdId :tholds.split(",")){
			Cache c = Manager.getCache(cacheNS);
			Object key = tholdId+".properties";
			Object thTmp = c.get(key );
			thTmp = ac.toThreshold(thTmp );
			assertTrue("!NOT Threshold!:"+thTmp, thTmp instanceof Threshold);
			assertTrue(((Threshold)thTmp).getDatasource().equals("test.rrd") );
		}
	} 
	/**
	 * test chained notification for case like:
	 * 	java.io.FileNotFoundException: Could not open C:\Users\xco5015\Documents\MyWorkspace\rrd\test.rrd [non existent]
	 * @author vipup
	 * @throws TholdException
	 */
	public void testInitAndCheckFromFile() throws TholdException{
		String tholds = "1,2,3,4,5";
		final StringBuffer localOut = new StringBuffer ("BEGIN");
		AlertCaptain ac = AlertCaptain.getInstance();
		ac.setAsync(false);
		Threshold localNotificator = 
			new Log4JActionist( "test.rrd", 
				"( 1==1)" , 600){
			public void performAction(long timestampSec) {
				//super.performAction( timestampSec);
				localOut.append( timestampSec );
				localOut.append( "," );
				//System.out.println(localOut);
			}
		};
		ac.register(localNotificator); 
		//if (1==1)
		for (String tholdId :tholds.split(",")){
			Cache c = Manager.getCache(cacheNS);
			Object key = tholdId+".properties";
			Object thTmp = c.get(key ); 
			thTmp = ac.toThreshold(thTmp );
			assertTrue("!NOT Threshold!:"+thTmp, thTmp instanceof Threshold);
			assertTrue(((Threshold)thTmp).getDatasource().equals("test.rrd") );
			ac.register((Threshold)thTmp);
//			assertTrue(((Threshold)thTmp).inIncidentTime() == -1  );
		}
		
		
		for (int i=0;i<2001;i+=60){
			ac.tick( i* 60 );// 200 mins on live 
		}
		assertEquals(""+localOut, localOut.toString().length(), 207);
	} 	
	
	 
	protected void setUpDB() throws Exception {
 
		RrdDef rrdDef;

		long startTime = 920800000L;
		RrdDb rrdDb;
		
		// 1999]
		rrdDef = new RrdDef("test.rrd"/*getRRDName()*/);
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

	}	

	protected void setUp() throws Exception {
		
		setUpDB();
	    /**/
		System.out.println("--------SETUP---------"); 
		Cache c = Manager.getCache(cacheNS);
		Threshold stdOutNotificator = new StdOutActionist( "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);	 
		c.put("1.properties",stdOutNotificator.toProperties() );
		stdOutNotificator = new StdErrActionist( "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);
		c.put("2.properties",stdOutNotificator.toProperties() );
		stdOutNotificator = new Log2MAILActionist( "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);
		c.put("3.properties",stdOutNotificator.toProperties() );
		stdOutNotificator = new Log4JActionist( "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);
		c.put("4.properties",stdOutNotificator.toProperties() );
		
		//dvalue
		
		stdOutNotificator = new Log4JActionist( "test.rrd", 
				"!("+
				"dvalue > "+ (1) +" && "+ "dvalue < "+ (111) +""+
				")" 
				  , 600);
		c.put("5.properties",stdOutNotificator.toProperties() );
	     
	}

	protected void tearDown() throws Exception {
		System.out.println("--------tearDown---------");  
		System.out.println("......-tearDown----......");
	}
}