package cc.co.llabor.threshold;  

import net.sf.jsr107cache.Cache;
import cc.co.llabor.cache.Manager;
import cc.co.llabor.threshold.rrd.Threshold; 
import junit.framework.TestCase;

public class TholdIniTest extends TestCase {
	private static final String cacheNS = "thold";
	public void testInitFromFile() throws TholdException{
		String tholds = "1,2,3,4,5";
		for (String tholdId :tholds.split(",")){
			Cache c = Manager.getCache(cacheNS);
			Object key = tholdId+".properties";
			Object thTmp = c.get(key );
			AlertCaptain ac = AlertCaptain.getInstance();
			thTmp = ac.toThreshold(thTmp );
			assertTrue("!NOT Threshold!:"+thTmp, thTmp instanceof Threshold);
			assertTrue(((Threshold)thTmp).getDatasource().equals("test.rrd") );
		}
	} 

	protected void setUp() throws Exception {
	    /**/
		System.out.println("--------SETUP---------"); 
		Cache c = Manager.getCache(cacheNS);
		Threshold stdOutNotificator = new StdErrActionist( "test.rrd", 
				"!("+
				"rrd.lastDatasourceValues[0] > "+ (1) +" && "+
				"rrd.lastDatasourceValues[0] < "+ (111) +""+
				")" 
				  , 600);	
		//c.put("1.properties",stdOutNotificator.toString() );
		c.put("1.properties",stdOutNotificator.toProperties() );
		c.put("2.properties",stdOutNotificator.toProperties() );
		c.put("3.properties",stdOutNotificator.toProperties() );
		c.put("4.properties",stdOutNotificator.toProperties() );
		c.put("5.properties",stdOutNotificator.toProperties() );
	     
	}

	protected void tearDown() throws Exception {
		System.out.println("--------tearDown---------");  
		System.out.println("......-tearDown----......");
	}
}