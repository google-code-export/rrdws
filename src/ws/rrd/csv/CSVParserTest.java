package ws.rrd.csv;

import java.io.IOException;

import org.junit.Test;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.04.2010::11:59:24<br> 
 */
public class CSVParserTest {
	
	public CSVParserTest(){
		System.setProperty("net.sf.jsr107cache.CacheFactory","ws.rrd.cache.BasicCacheFactory");
	}

	@Test
	public void testCSVParser() throws IOException {
		CSVParser p = new CSVParser(this.getClass().getClassLoader().getResourceAsStream("test.csv"));
		p.perform();
	}
	
	@Test
	public void testExecuteUpdate() throws IOException {
		CSVParser p = new CSVParser(this.getClass().getClassLoader().getResourceAsStream("test.csv"));
		Object o = p.executeUpdate();
		System.err.println((""+o).replace(", \\\\",",\n \\\\"));
	}	

}


 