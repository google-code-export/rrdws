package org.jrobin.thold;
 

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDef; 

import cc.co.llabor.threshold.rrd.Threshold;

public class TholdTest extends TestCase{

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
	public void testExecuteSinHighAlert() throws RrdException, IOException {
 
		RrdDef rrdDef = new RrdDef(TEST_RRD);
		long startTime = 920800000L; //920800000L == [Sun Mar 07 10:46:40 CET 1999] 
 
		rrdDef.setStartTime(startTime );
		rrdDef.setStep(55);
		int MAX_POSSIBLE_IQ = 200;
		rrdDef.addDatasource("speed", "GAUGE", 600, Double.NaN, Double.NaN);
		
		rrdDef.addArchive("AVERAGE", 0.1, 1, 3600);
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
		RrdDb rrdDb = new RrdDb(rrdDef); 
		
		double hiLimit =  130; // should be smart enough ;)
		long tenSecondds = 1111; // 10 sec is maximal time to start to do something...
		Threshold headHunter = new HighAlerter(TEST_RRD, hiLimit, tenSecondds);
		AlertCaptain capTmp  =AlertCaptain .getInstance();   
		
		capTmp.register(headHunter);
		
		
		Sample sample = rrdDb.createSample(); 
		long lastTimeTmp = -1;
		double lastSpeed = 0;
		for(int secTmp=1;secTmp<60*60*24;secTmp+=1){ // 1 Day
			 lastTimeTmp = startTime+secTmp;
			 double d =  MAX_POSSIBLE_IQ * Math.sin(  (.0001356*secTmp)   );
			 lastSpeed  = d  *Math.sin( .000531*secTmp );
			 lastSpeed += 100;
			 // Realdata production
			 sample.setAndUpdate(""+(lastTimeTmp)+":"+(lastSpeed ));
			 // observation of trarget rrd -- here will be simulation of Time!
			 capTmp.tick(lastTimeTmp);
			 
		} 
		rrdDb.close(); 
		RrdGraphDef graphDef = new RrdGraphDef();
		graphDef.setStartTime(startTime -10*60);// seconds!
		graphDef.setEndTime(lastTimeTmp +10*60);//seconds
		graphDef.setFilename(this.getName()+".gif");
		graphDef.setWidth(800);
		graphDef.setHeight(600);
		graphDef.setVerticalLabel(" "+new Date(startTime*1000)+" - "+new Date(lastTimeTmp*1000));
		graphDef.setTitle("not for dummies!");

		graphDef.datasource("HighLIMIT", ""+hiLimit);
		graphDef.line("HighLIMIT", new Color( 0, 0x33, 0xAA), "minimal_IQ", 4);
		
		graphDef.datasource("myspeed", TEST_RRD, "speed", "AVERAGE");
		graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);
 
		//.stat.rrd				
		graphDef.datasource("myspeedAlert", TEST_RRD+".stat.rrd", "speed", "AVERAGE");
		graphDef.line("myspeedAlert", new Color(0,  0xFF,0 ), "recruit IT!" ,4);
		
		RrdGraph graph = new RrdGraph(graphDef);
		//graph.saveAsGIF("speed.gif");
		// https://rrd4j.dev.java.net/tutorial.html
		BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ,MAX_POSSIBLE_IQ,BufferedImage.TYPE_INT_RGB);
		graph.render(bi.getGraphics());
		// to save this graph as a PNG image (recommended file format)
		// use the following code:		System.out.println("--");
		
		
		
		
		
	}

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
		public void testExecute() throws RrdException, IOException {
			/*
			 * rrdtool create test.rrd             \
	         --start 920804000          \
	         DS:speed:COUNTER:600:U:U   \
	         RRA:AVERAGE:0.5:1:24       \
	         RRA:AVERAGE:0.5:6:10
	         */
			RrdDef rrdDef = new RrdDef(TEST_RRD);
			rrdDef.setStartTime(920804000L);
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
			sample.setAndUpdate("920804400:12345");
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
			 * rrdtool graph speed.gif                                 \
	         --start 920804000 --end 920808000               \
	         DEF:myspeed=test.rrd:speed:AVERAGE              \
	         LINE2:myspeed#FF0000
			 * */
			
			rrdDb.close();
			
			
			RrdGraphDef graphDef = new RrdGraphDef();
			graphDef.setStartTime(920804000L);
			graphDef.setEndTime(920808000L);
			graphDef.setFilename("all1.gif");
			graphDef.setWidth(640);
			graphDef.setHeight(480);
			 
			graphDef.datasource("myspeed", TEST_RRD, "speed", "AVERAGE");
			graphDef.line("myspeed", new Color(0xFF, 0, 0), null, 2);
			RrdGraph graph = new RrdGraph(graphDef);
			//graph.saveAsGIF("speed.gif");
			// https://rrd4j.dev.java.net/tutorial.html
			BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
			graph.render(bi.getGraphics());
			// to save this graph as a PNG image (recommended file format)
			// use the following code:		System.out.println("--");
			
			
			
			
			
		}

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
			public void testExecutePureSin() throws RrdException, IOException {
				/*
				 * rrdtool create test.rrd             \
		         --start 920804000          \
		         DS:speed:COUNTER:600:U:U   \
		         RRA:AVERAGE:0.5:1:24       \
		         RRA:AVERAGE:0.5:6:10
		         */
				RrdDef rrdDef = new RrdDef(TEST_RRD);
				long startTime = 920800000L; //920800000L == [Sun Mar 07 10:46:40 CET 1999] 
		 
				rrdDef.setStartTime(startTime );
				rrdDef.setStep(55);
				rrdDef.addDatasource("speed", "GAUGE", 600*100, Double.NaN, Double.NaN);
				
				rrdDef.addArchive("AVERAGE", 0.1, 1, 3600);
				rrdDef.addArchive("AVERAGE", 0.5, 6, 700);
				rrdDef.addArchive("AVERAGE", 0.5, 24, 797);
				rrdDef.addArchive("AVERAGE", 0.5, 288, 775);
				rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 1, 600);
				rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 6, 700);
				rrdDef.addArchive(ConsolFuns.CF_MAX, 0.5, 24, 797);		 
				rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 1, 600);
				rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 6, 700);
				rrdDef.addArchive(ConsolFuns.CF_MIN, 0.5, 24, 797);		 
				rrdDef.addArchive(ConsolFuns.CF_LAST, 0.5, 24, 3600);		 
				RrdDb rrdDb = new RrdDb(rrdDef); 
				Sample sample = rrdDb.createSample(); 
				long lastTimeTmp = -1;
				double lastSpeed = 0;
				for(int secTmp=1;secTmp<60*60*24;secTmp+=1){ // 1 Day
					 lastTimeTmp = startTime+secTmp;
					 lastSpeed += 100.0*Math.sin(  (.0003*secTmp)  );  
					 sample.setAndUpdate(""+(lastTimeTmp)+":"+(lastSpeed ));
				} 
				rrdDb.close(); 
				RrdGraphDef graphDef = new RrdGraphDef();
				graphDef.setStartTime(startTime -10*60);// seconds!
				graphDef.setEndTime(lastTimeTmp +10*60);//seconds
				graphDef.setFilename("all2.gif");
				graphDef.setWidth(800);
				graphDef.setHeight(600);
				graphDef.setVerticalLabel(" "+new Date(startTime*1000)+" - "+new Date(lastTimeTmp*1000));
				graphDef.setTitle("TitlePlace");
				graphDef.datasource("myspeedMAX", TEST_RRD, "speed", ConsolFuns.CF_MAX);
				graphDef.line("myspeedMAX", new Color( 0xFF,0xAF, 0xAF), "max", 2);
				graphDef.datasource("myspeedMIN", TEST_RRD, "speed", ConsolFuns.CF_MIN);
				graphDef.line("myspeedMIN", new Color( 0xFF, 0xAF,  0xFF), "min", 2);
				graphDef.datasource("myspeedLAST", TEST_RRD, "speed", ConsolFuns.CF_LAST);
				graphDef.line("myspeedLAST", new Color( 0x1F, 0x1F,  0x1F), "last", 3);
				graphDef.datasource("myspeed", TEST_RRD, "speed", "AVERAGE");
				graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);
		
				graphDef.datasource("LowLIMIT", "55000");
				graphDef.line("LowLIMIT", new Color( 0, 0xAA, 0xFF), "LOW", 1);
				graphDef.datasource("HighLIMIT", "550000");
				graphDef.line("HighLIMIT", new Color( 0, 0xFF, 0xAA), "HI", 1);
				
						
				
				RrdGraph graph = new RrdGraph(graphDef);
				//graph.saveAsGIF("speed.gif");
				// https://rrd4j.dev.java.net/tutorial.html
				BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
				graph.render(bi.getGraphics());
				// to save this graph as a PNG image (recommended file format)
				// use the following code:		System.out.println("--");
				
				
				
				
				
			}

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
				public void testExecuteSin() throws RrdException, IOException {
			 
					RrdDef rrdDef = new RrdDef(TEST_RRD);
					long startTime = 920800000L; //920800000L == [Sun Mar 07 10:46:40 CET 1999] 
			 
					rrdDef.setStartTime(startTime );
					rrdDef.setStep(55);
					int MAX_POSSIBLE_IQ = 200;
					rrdDef.addDatasource("speed", "GAUGE", 600, Double.NaN, Double.NaN);
					
					rrdDef.addArchive("AVERAGE", 0.1, 1, 3600);
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
					RrdDb rrdDb = new RrdDb(rrdDef); 
					
					double hiLimit =  130; // should be smart enough ;)
					long tenSecondds = 10; // 10 sec is maximal time to start to do something...
					Threshold headHunter = new HighAlerter(TEST_RRD, hiLimit, tenSecondds);
					AlertCaptain capTmp  =AlertCaptain .getInstance();   
					
					capTmp.register(headHunter);
					
					
					Sample sample = rrdDb.createSample(); 
					long lastTimeTmp = -1;
					double lastSpeed = 0;
					for(int secTmp=1;secTmp<60*60*24;secTmp+=1){ // 1 Day
						 lastTimeTmp = startTime+secTmp;
						 double d =  MAX_POSSIBLE_IQ * Math.sin(  (.0001356*secTmp)   );
						 lastSpeed  = d  *Math.sin( .000531*secTmp *secTmp);
						 lastSpeed += 100;
						 // Realdata production
						 sample.setAndUpdate(""+(lastTimeTmp)+":"+(lastSpeed ));
						 // observation of trarget rrd -- here will be simulation of Time!
						 capTmp.tick(lastTimeTmp);
						 
					} 
					rrdDb.close(); 
					RrdGraphDef graphDef = new RrdGraphDef();
					graphDef.setStartTime(startTime -10*60);// seconds!
					graphDef.setEndTime(lastTimeTmp +10*60);//seconds
					graphDef.setFilename(this.getName()+".gif");
					graphDef.setWidth(800);
					graphDef.setHeight(600);
					graphDef.setVerticalLabel(" "+new Date(startTime*1000)+" - "+new Date(lastTimeTmp*1000));
					graphDef.setTitle("not for dummies!");
			
					graphDef.datasource("HighLIMIT", ""+hiLimit);
					graphDef.line("HighLIMIT", new Color( 0, 0x33, 0xAA), "minimal_IQ", 4);
					
					graphDef.datasource("myspeed", TEST_RRD, "speed", "AVERAGE");
					graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 2);
			 
					//.stat.rrd				
					graphDef.datasource("myspeedAlert", TEST_RRD+".stat.rrd", "speed", "AVERAGE");
					graphDef.line("myspeedAlert", new Color(0,  0xFF,0 ), "recruit IT!" ,4);
					
					RrdGraph graph = new RrdGraph(graphDef);
					//graph.saveAsGIF("speed.gif");
					// https://rrd4j.dev.java.net/tutorial.html
					BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ,MAX_POSSIBLE_IQ,BufferedImage.TYPE_INT_RGB);
					graph.render(bi.getGraphics());
					// to save this graph as a PNG image (recommended file format)
					// use the following code:		System.out.println("--");
					
					
					
					
					
				}

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
					public void testExecuteSinPow2() throws RrdException, IOException {
				
						RrdDef rrdDef = new RrdDef(TEST_RRD);
						long startTime = 920800000L; //920800000L == [Sun Mar 07 10:46:40 CET 1999] 
				
						rrdDef.setStartTime(startTime );
						rrdDef.setStep(110);
						int MAX_POSSIBLE_IQ = 140;
						rrdDef.addDatasource("speed", "GAUGE", 600, Double.NaN, Double.NaN);
						
						rrdDef.addArchive("AVERAGE", 0.1, 1, 3600);
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
						RrdDb rrdDb = new RrdDb(rrdDef); 
						
						double hiLimit =  60; // should be smart enough ;)
						long tenSecondds = 10; // 10 sec is maximal time to start to do something...
						Threshold headHunter = new HighAlerter(TEST_RRD, hiLimit, tenSecondds);
						AlertCaptain capTmp  =AlertCaptain .getInstance();   
						
						capTmp.register(headHunter);
						
						
						Sample sample = rrdDb.createSample(); 
						long lastTimeTmp = -1;
						double lastSpeed = 0;
						for(int secTmp=1;secTmp<60*60*12;secTmp+=1){ // 1 Day
							 lastTimeTmp = startTime+secTmp;
							 double d =  140 * Math.sin(  (.0001356*secTmp)   );
							 lastSpeed  = d  *Math.sin( Math.E *.000531*secTmp *secTmp);
							 
							 //lastSpeed = lastSpeed>0?lastSpeed:-lastSpeed;
							  
							 // Realdata production
							 sample.setAndUpdate(""+(lastTimeTmp)+":"+(lastSpeed ));
							 // observation of trarget rrd -- here will be simulation of Time!
							 capTmp.tick(lastTimeTmp);
							 
						} 
						rrdDb.close(); 
						RrdGraphDef graphDef = new RrdGraphDef();
						graphDef.setStartTime(startTime -10*60);// seconds!
						graphDef.setEndTime(lastTimeTmp +10*60);//seconds
						graphDef.setFilename(this.getName()+".gif");
						graphDef.setWidth(800);
						graphDef.setHeight(600);
						graphDef.setVerticalLabel(" "+new Date(startTime*1000)+" - "+new Date(lastTimeTmp*1000));
						graphDef.setTitle("not for dummies!");
				
						graphDef.datasource("HighLIMIT", ""+hiLimit);
						graphDef.line("HighLIMIT", new Color( 0, 0x33, 0xAA), "minimal_IQ", 4);
						

				
						//.stat.rrd				
						graphDef.datasource("myspeedAlert", TEST_RRD+".stat.rrd", "speed", "AVERAGE");
						graphDef.line("myspeedAlert", new Color(0,  0xFF,0 ), "recruit IT!" ,3);
						graphDef.datasource("myspeedAlertMAX", TEST_RRD+".stat.rrd", "speed", ConsolFuns.CF_MAX);
						graphDef.line("myspeedAlertMAX", new Color(0xAF,0xAF ,  0xFF ), "recruitMAX" ,2);
						graphDef.datasource("myspeedAlertMIN", TEST_RRD+".stat.rrd", "speed", ConsolFuns.CF_MIN);
						graphDef.line("myspeedAlertMIN", new Color(0,  0xFF,0xFF ), "recruitMIN" ,1);
						
						// f(x)
						graphDef.datasource("myspeed", TEST_RRD, "speed", "AVERAGE");
						graphDef.line("myspeed", new Color(0xFF, 0, 0), "F(t)", 1);
						
						RrdGraph graph = new RrdGraph(graphDef);
						//graph.saveAsGIF("speed.gif");
						// https://rrd4j.dev.java.net/tutorial.html
						BufferedImage bi = new BufferedImage(MAX_POSSIBLE_IQ,MAX_POSSIBLE_IQ,BufferedImage.TYPE_INT_RGB);
						graph.render(bi.getGraphics());
						// to save this graph as a PNG image (recommended file format)
						// use the following code:		System.out.println("--");
						
						
						
						
						
					}

}
