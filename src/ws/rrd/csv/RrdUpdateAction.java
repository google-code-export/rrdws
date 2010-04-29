package ws.rrd.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jrobin.cmd.RrdCommander;
import org.jrobin.core.RrdException;

import ws.rrd.MemoryFileCache;

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
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
			long timestampTmp = 0;
			try {
				timestampTmp = sdf.parse(timestamp).getTime();
			} catch (ParseException e1) { 
				//e1.printStackTrace();
				retval = e1;
			}

			String rrddb = "X"+xpath.hashCode()+".rrd";
			String cmdTmp = "rrdtool update "+rrddb +" "+(timestampTmp/1000L) +":"+ data; 
			try {   
				retval  = RrdCommander.execute(cmdTmp );
			} catch (IOException e) { 
				if (e instanceof FileNotFoundException){
					try {
						String cmdCreate = "rrdtool create " +
							""+rrddb+" --start "+(((timestampTmp-10)/1000L))+"" +
							" -s 300 "  +
							" DS:data:GAUGE:600:U:U  " +
							" RRA:AVERAGE:0.5:1:288 " +
							" RRA:MIN:0.5:1:288 " +
							" RRA:MAX:0.5:1:288 " +
							" RRA:AVERAGE:0.5:6:336 " +
							" RRA:MIN:0.5:6:336 " +
							" RRA:MAX:0.5:6:336 " +
							" RRA:AVERAGE:0.5:24:372 " +
							" RRA:MIN:0.5:24:372 " +
							" RRA:MAX:0.5:24:372 " +
							" RRA:AVERAGE:0.5:144:730 " +
							" RRA:MIN:0.5:144:730 " +
							" RRA:MAX:0.5:144:730 " +
							" RRA:LAST:0.5:1:288 " + 
							"";   
						System.out.println(xpath +" --->  "+cmdCreate); 
						retval  =RrdCommander.execute(cmdCreate);
						MemoryFileCache.getCache().put(rrddb,xpath);
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
 

}


 