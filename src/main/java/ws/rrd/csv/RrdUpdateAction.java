package ws.rrd.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.TreeMap;

import net.sf.jsr107cache.Cache;

import org.jrobin.cmd.RrdCommander;
import org.jrobin.core.RrdException;

import ws.rrd.mem.MemoryFileCache;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  27.04.2010::15:33:35<br> 
 */
public class RrdUpdateAction implements Action {

	 

		@Override
		public Object perform(String xpath, String timestamp, String data) {
			Object retval = "";
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
			long timestampTmp = 0;
			try {
				timestampTmp = sdf.parse(timestamp).getTime();  
			} catch (ParseException e1) { 
				//e1.printStackTrace();
				retval = e1;
			}

			
			String cmdTmp = makeUpdateCMD(data, timestampTmp, xpath); 
			try {   
				retval  = RrdCommander.execute(cmdTmp );
			} catch (IOException e) { 
				if (e instanceof FileNotFoundException){
					try {
						String cmdCreate = makeCreateCMD(timestampTmp, xpath);   
						System.out.println(xpath +" --->  "+cmdCreate); 
						retval  =RrdCommander.execute(cmdCreate);
						retval  =RrdCommander.execute(cmdTmp );
					} catch (IOException e1) { 
						//e1.printStackTrace();
						retval = e1;
					} catch (RrdException e1) { 
						//e1.printStackTrace();
						retval = e1;
					}
				}
			} catch (RrdException e) { 
				//e.printStackTrace();
				retval = e; 
			} 
			return retval;
		}

		public static final String makeCreateCMD(long timestampTmp, String xpath) {
			String rrddb = xpath2Hash(xpath);
			String cmdCreate = "rrdtool create " +
				""+rrddb+" --start "+(((timestampTmp-10000)/1000L))+"" +
				" --step 60" +
				"				DS:data:GAUGE:120:0:U" +
				"				RRA:AVERAGE:0.5:1:60" +
				"				RRA:AVERAGE:0.5:5:288" +
				"				RRA:AVERAGE:0.5:30:336" +
				"				RRA:AVERAGE:0.5:120:372" +
				"				RRA:AVERAGE:0.5:1440:365" +
				"				RRA:AVERAGE:0.5:60:8760" +
				"				RRA:LAST:0.5:1:60" +
				""; 
			return cmdCreate;
		}

	    private static volatile long last_clean = 0;
	    private static Cache cache = MemoryFileCache.getCache();
	    private static Registry reg = (Registry) cache.get("REGISTRY"); 
	    static int flushCount=0;
	    static int changeCount=0;
		private static void checkReg(String rrddb, String xpath  ) {
			if (reg == null){ 
				reg = new Registry();
			}	
			if (changeCount >10 ||(last_clean +10000) < System.currentTimeMillis()){
				synchronized (cache) { 
					cache.remove("REGISTRY");
					cache.put("REGISTRY", new Registry( reg.getDb2path() ));
					last_clean=System.currentTimeMillis();					
					System.out.println("REGISTRY Flush #"+flushCount+++":"+changeCount);
					changeCount =0;
				}
			}
			if (    reg != null && 
					reg.getDb2path()!=null && 
					reg.getDb2path().get(rrddb)!=null && 
					reg.getDb2path().get(rrddb).equals(xpath) ){
				//nothing to do
				return;
			} 
		 		

			

			reg.register(rrddb,xpath);
			changeCount++;
				
		}
 

		private static final String xpath2Hash(String xpath) {
			String rrddb = "X"+xpath.hashCode()+".rrd";
			checkReg(rrddb, xpath);
			return rrddb;
		}

		public final static String makeUpdateCMD(String data, long timestampTmp, String xpath) {
			String rrddb = xpath2Hash(xpath);
			String cmdTmp = "rrdtool update "+rrddb +" "+(timestampTmp/1000L) +":"+ data;
			return cmdTmp;
		}
 

}


 