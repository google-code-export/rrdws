package ws.rrd.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import ws.rrd.MemoryFileCache;

import net.sf.jsr107cache.Cache;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  27.04.2010::15:34:56<br> 
 */
public class ToStringPrintlnAction implements Action {
			StringBuffer sb =new StringBuffer();
			public String toString(){
				return sb.toString();
			}
 
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
			@Override
			public Object perform(String xpath, String timestamp, String data) {
				try {
					long timestampTmp =  sdf.parse(timestamp).getTime();
					Cache cache = MemoryFileCache.getCache();
					Registry reg = (Registry) cache.get("REGISTRY");
					if (reg == null || reg.getPath2db() ==null || reg.getPath2db().get(xpath) == null){
						System_out_println( xpath + "-->"  );  
						String cmdCreateTmp = RrdUpdateAction.makeCreateCMD(timestampTmp, xpath) ;
						System_out_println( cmdCreateTmp  );
					}
					String  cmdUpdateTmp = RrdUpdateAction.makeUpdateCMD(data, timestampTmp, xpath) ; 
					System_out_println( cmdUpdateTmp  );
		
					return this;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return e;
				}
			}
			private void System_out_println(String cmdTmp) {
				System.out.println(cmdTmp);
				sb.append(cmdTmp);
				sb.append("\n");
				
			}	
 
}


 