package ws.rrd.csv;

import static org.junit.Assert.*;

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

	@Test
	public void testCSVParser() throws IOException {
		CSVParser p = new CSVParser(this.getClass().getClassLoader().getResourceAsStream("test.csv"));
	}

}


 