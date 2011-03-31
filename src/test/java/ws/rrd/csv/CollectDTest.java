package ws.rrd.csv;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cc.co.llabor.cache.BasicCacheFactory;

import junit.framework.TestCase;
 

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 *         <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 * Creation: 20.04.2010::11:59:24<br>
 */
public class CollectDTest extends TestCase{
	
 
	 
	public void testCSVTXT() throws IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream resourceAsStream = classLoader .getResourceAsStream("collectd.txt");
		TextLineIterator p = new TextLineIterator(resourceAsStream);
		// testdrive into System.out
		DateFormat SDF = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.US);
		Action a = new ToStringPrintlnAction(SDF); 
		try{//p.setIgnoreWrongLine(true);
			p.perform(a);
			String out = a.toString();
			System.out.println(out);
			assertTrue( out.indexOf("rrdtool create X-1722861190.rrd --start 1301583237 --step 60")>0);
			assertTrue(out.indexOf("rrdtool update X-1781070033.rrd 1301583274:9332")>0);
			assertTrue(out.indexOf("rrdtool update X-1261962686.rrd 1301584867:0.0")>0);
		}catch(Exception e){
			e.printStackTrace();
			assertTrue(a.toString(), e instanceof ArrayIndexOutOfBoundsException);
		}
	}
	 
	
	
	public void testExecuteUpdate() throws IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream resourceAsStream = classLoader .getResourceAsStream("collectd.txt");
		TextLineIterator p = new TextLineIterator(resourceAsStream);
		// testdrive into System.out
		SimpleDateFormat SDF = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.US);				
		Action a = new RrdUpdateAction(SDF);
		p.perform(a);
	}	
	 
}
