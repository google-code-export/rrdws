package ws.rrd.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheManager;

import org.jrobin.cmd.RrdCommander;
import org.jrobin.core.RrdException;

import com.no10x.cache.Manager;
 

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

	    public static final String MM_DD_YYYY_HH_MM_SS_SSS = "MM/dd/yyyy HH:mm:ss.SSS";
		final static SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY_HH_MM_SS_SSS);

		@Override
		public Object perform(String xpath, String timestamp, String data) {
			Object retval = "";
			
			long timestampTmp = 0;
			try {
				synchronized (RrdUpdateAction.class) { 
					Date parseVal = sdf.parse(timestamp);
					timestampTmp = parseVal.getTime();  
				}
			} catch (ParseException e1) { 
				//e1.printStackTrace();
				retval = e1;
			} catch (java.lang.NumberFormatException e) {
				retval = e;
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
				" --step 60 " +
				"				DS:data:GAUGE:240:U:U " +
				"				RRA:AVERAGE:0.5:3:480 " +
				"				RRA:AVERAGE:0.5:17:592 " +
				"				RRA:AVERAGE:0.5:131:340 " +
				"				RRA:AVERAGE:0.5:731:719 " +
				"				RRA:AVERAGE:0.5:10000:273 " +
				"				RRA:MAX:0.5:3:480 " +
				"				RRA:MAX:0.5:17:592 " +
				"				RRA:MAX:0.5:131:340 " +
				"				RRA:MAX:0.5:731:719 " +
				"				RRA:MAX:0.5:10000:273 " +
				"				RRA:MIN:0.5:3:480 " +
				"				RRA:MIN:0.5:17:592 " +
				"				RRA:MIN:0.5:131:340 " +
				"				RRA:MIN:0.5:731:719 " +
				"				RRA:MIN:0.5:10000:273 " +
												" "; 
			return cmdCreate;
		}

	    private static volatile long last_clean = 0;
	    private static Cache cache = Manager.getCache();
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
 
		/**
		 * BE CAREFULL WITH reimpelmentation THIS METHOD! The risk is to look all exisitng RRD-Databases
		 * 
		 * @author vipup
		 * @param xpath
		 * @return
		 */
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


 