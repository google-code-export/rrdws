package cc.co.llabor.threshold;  

import java.io.File;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdDef;

import net.sf.jsr107cache.Cache;
import cc.co.llabor.cache.Manager;
import cc.co.llabor.threshold.log2.Log2MAILActionist;
import cc.co.llabor.threshold.log2.Log4JActionist;
import cc.co.llabor.threshold.rrd.Threshold; 
import cc.co.llabor.threshold.syso.StdErrActionist;
import cc.co.llabor.threshold.syso.StdOutActionist;
import junit.framework.TestCase;

public class RedIniTest extends TestCase {
	private static final String cacheNS = "REDthold";
	public void testInitFromFile() throws TholdException{
		String tholds = "1,2,3,4,5"; 
		 
		for (String tholdId :tholds.split(",")){ 
			Object thTmp =   AlertCaptain.getInstance().restoreByName( tholdId);
			assertTrue("!NOT Threshold!:"+thTmp, thTmp instanceof Threshold);
			assertTrue(((Threshold)thTmp).getDatasource().equals("test.rrd") );
		}
	} 
	/**
	 * test chained notification for case like:
	 * 	java.io.FileNotFoundException: Could not open C:\xxx\xxx\xc\xxx\rrd\test.rrd [non existent]
	 * @author vipup
	 * @throws TholdException
	 */
	public void testInitAndCheckFromFile() throws TholdException{
		String tholds = "1,2,3,4,5";
		final StringBuffer localOut = new StringBuffer ("BEGIN");
		AlertCaptain ac = AlertCaptain.getInstance();
		ac.setAsync(false);
		Threshold localNotificator = 
			new Log4JActionist( "test.rrd",  "( 1==1)" , (long) 600){
			public void performAction(long timestampSec) {
				//super.performAction( timestampSec);
				localOut.append( timestampSec );
				localOut.append( "," );
				//System.out.p  0rintln(localOut);
			}
		};
		ac.register(localNotificator); 
		if (1==1)
		for (String tholdId :tholds.split(",")){
			Cache c = Manager.getCache(AlertCaptain.cacheNS);
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
	
	RrdDef rrdDef; 
	RrdDb rrdDb;
	protected void setUpDB() throws Exception {  
		long startTime = 920800000L; 
		
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
		AlertCaptain.getInstance();
		Threshold stdOutNotificator = new RedAndLogActionist(StdOutActionist.class, "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);	 
		AlertCaptain.getInstance().storeToName( "1",stdOutNotificator );
		stdOutNotificator = new  RedAndLogActionist(StdErrActionist.class, "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);
		AlertCaptain.getInstance().storeToName( "2",stdOutNotificator);
		stdOutNotificator = new  RedAndLogActionist(Log2MAILActionist.class, "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);
		AlertCaptain.getInstance().storeToName( "3",stdOutNotificator);
		stdOutNotificator = new  RedAndLogActionist(Log4JActionist.class,  "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);
		AlertCaptain.getInstance().storeToName( "4",stdOutNotificator);
		
		//dvalue
		
		stdOutNotificator = new  RedAndLogActionist( Log4JActionist.class, "test.rrd", 
				"!("+
				"dvalue > "+ (1) +" && "+ "dvalue < "+ (111) +""+
				")" 
				  ,  600);
		AlertCaptain.getInstance().storeToName( "5",stdOutNotificator);
	     
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
	/**
		 * test chained notification for case like:
		 * 	java.io.FileNotFoundException: Could not open C:\xxx\xxx\xc\xxx\rrd\test.rrd [non existent]
		 * @author vipup
		 * @throws TholdException
		 */
		public void testActionistStore() throws TholdException{
			AlertCaptain ac = AlertCaptain.getInstance();
			ac.setAsync(false);
			Cache c = Manager.getCache(cacheNS);
			// create
//			Threshold inTmp = new RedAndLogActionist(StdOutActionist.class, "test.rrd", 
//					"!("+
//					"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
//					"rrd.lastDatasourceValues[0] < "+ (111) +""+
//					")" 
//					  , 600);
			String theName = RedAndLogActionist.dump2Cache("test.rrd");
			
			Threshold inTmp = ac.restoreByName(theName);
			//store
			c.put("in.properties",inTmp.toProperties() );			
			// restore
			Object storedProps = c.get("in.properties");
			Threshold outTmp = ac.toThreshold(storedProps) ;
						
			// cmp
			assertEquals(""+inTmp, ""+outTmp);
 		}
}