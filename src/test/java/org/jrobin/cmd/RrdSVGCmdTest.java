package org.jrobin.cmd;
 
 
import java.io.ByteArrayInputStream; 
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;  
import org.jrobin.graph.RrdGraphConstants;
import org.jrobin.svg.RrdGraph;
import org.jrobin.svg.RrdGraphDef;
 
import org.jrobin.svg.awt.BufferedImage;
import org.jrobin.svg.awt.Color; 

//import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class RrdSVGCmdTest extends TestCase{

	private static final String TEST_RRD = "test.rrd";

	/*
	 * e:\Distr\rrd4j-2.0.5\lib\speed.gif
	 * 
$ rrdtool create test.rrd --start 920804400 DS:speed:COUNTER:600:U:U  RRA:AVERAGE:0.5:1:24 RRA:AVERAGE:0.5:6:10
$ rrdtool update test.rrd 920804500:12335 920804600:12337 920804700:12340
$ rrdtool update test.rrd 920804800:12345 920805000:12357 920805300:12363
$ rrdtool update test.rrd 920805700:12366 920805800:12367 920806100:12368
$ rrdtool update test.rrd 920806500:12383 920806800:12393 920807100:12399
$ rrdtool update test.rrd 920807400:12405 920807700:12411 920808000:12415
$ rrdtool update test.rrd 920808300:12420 920808600:12422 920808900:12423
$ rrdtool fetch test.rrd AVERAGE --start 920804400 --end 920809200 
 920804400:               nan
 920804700:               nan
 920805000: +4.0000000000E-02
 920805300: +2.0000000000E-02
 920805600:  +0.0000000000E00
 920805900:  +0.0000000000E00
 920806200: +3.3333333333E-02
 920806500: +3.3333333333E-02
 920806800: +3.3333333333E-02
 920807100: +2.0000000000E-02
 920807400: +2.0000000000E-02
 920807700: +2.0000000000E-02
 920808000: +1.3333333333E-02
 920808300: +1.6666666667E-02
 920808600: +6.6666666667E-03
 920808900: +3.3333333333E-03
 920809200:               nan
 $ rrdtool graph speed.gif      --start 920804400 --end 920808000  DEF:myspeed=test.rrd:speed:AVERAGE  LINE2:myspeed#FF0000
 
	 * 
	 * 
	 */ 
	public void testExecute1() throws RrdException, IOException {
		RrdGraphDef obj = testExecute();
		assertNotNull(obj);
	}

	public RrdGraphDef testExecute() throws RrdException, IOException {
		/*
		 * rrdtool create test.rrd             \
         --start 920804400          \
         DS:speed:COUNTER:600:U:U   \
         RRA:AVERAGE:0.5:1:24       \
         RRA:AVERAGE:0.5:6:10
         */
		RrdDef rrdDef = new RrdDef(TEST_RRD);
		rrdDef.setStartTime(920804400L);
		rrdDef.addDatasource("speed", "COUNTER", 600, Double.NaN, Double.NaN);
		rrdDef.addArchive("AVERAGE", 0.5, 1, 24);
		rrdDef.addArchive("AVERAGE", 0.5, 6, 10);
		RrdDb rrdDb = new RrdDb(rrdDef);
		//rrdDb.close();
		
		/*
		 * rrdtool update test.rrd 920804700:12345 920805000:12357 920805300:12363
 rrdtool update test.rrd 920805600:12363 920805900:12363 920806200:12373
 rrdtool update test.rrd 920806500:12383 920806800:12393 920807100:12399
 rrdtool update test.rrd 920807400:12405 920807700:12411 920808000:12415
 rrdtool update test.rrd 920808300:12420 920808600:12422 920808900:12423
 */
		 
		Sample sample = rrdDb.createSample();
		sample.setAndUpdate("920804700:12345");
		sample.setAndUpdate("920805000:12357");
		sample.setAndUpdate("920805300:12363");
		sample.setAndUpdate("920805600:12363");
		sample.setAndUpdate("920805900:12363");
		sample.setAndUpdate("920806200:12373");
		sample.setAndUpdate("920806500:12383");
		sample.setAndUpdate("920806800:12393");
		sample.setAndUpdate("920807100:12399");
		sample.setAndUpdate("920807400:12405");
		sample.setAndUpdate("920807700:12411");
		sample.setAndUpdate("920808000:12415");
		sample.setAndUpdate("920808300:12420");
		sample.setAndUpdate("920808600:12422");
		sample.setAndUpdate("920808900:12423");
		
		/*
		 * rrdtool fetch test.rrd AVERAGE --start 920804400 --end 920809200
		 * */
		
		FetchRequest fetchRequest = rrdDb.createFetchRequest("AVERAGE", 920804400L, 920809200L);
		FetchData fetchData = fetchRequest.fetchData();
		fetchData.dump();
		
		/*
		 * rrdtool graph speed.gif                     \
         --start 920804400 --end 920808000              \
         DEF:myspeed=test.rrd:speed:AVERAGE              \
         LINE2:myspeed#FF0000
		 * */
		
		rrdDb.close();
		
		
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(920804400L);
		graphDef.setEndTime(920808000L);
		graphDef.setFilename(RrdGraphConstants.IN_MEMORY_IMAGE);
		graphDef.setWidth(400);
		graphDef.setHeight(200);
		 
		graphDef.datasource("myspeed", TEST_RRD, "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), null, 2);

		return graphDef;
		
		
	}

	
	public InputStream generateSvg() throws RrdException, IOException{
		
		RrdGraphDef graphDef = testExecute();
		
		
		RrdGraph graph = new RrdGraph(graphDef);
		//graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html

		BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());


		// to save this graph as a PNG image (recommended file format)
		// use the following code:
		// graph.saveAsPNG("speed.png");
		
		byte[] buf = bi.getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(buf  );
		return in;		
		
				
	}

	 
}
