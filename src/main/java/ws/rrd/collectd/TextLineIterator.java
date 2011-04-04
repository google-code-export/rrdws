package ws.rrd.collectd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat; 

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.03.2011::17:49:41<br> 
 */
public class TextLineIterator {

	private BufferedReader in;

	public TextLineIterator(InputStream resourceAsStream) {
		this.in = new BufferedReader (new InputStreamReader(resourceAsStream));
	}

	public void perform(ws.rrd.csv.Action a) throws IOException {
		int lineCounter = 0;
		String line  = null;
		for(line = in.readLine();line != null ; line = in.readLine(), lineCounter++){
			if ( "null".equals(line))continue;
			if ( "" .equals(line))continue;
			try{
				DateFormat SDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss  yyyy");//SimpleDateFormat.getInstance();/
				int p2 = line.indexOf("] ");
				String timestamp= line.substring(1, p2) ;
				timestamp = timestamp.replace("CEST", "");
				timestamp = timestamp.replace("CET", "");
				timestamp = timestamp.replace("  ", " ");
				timestamp = timestamp.substring(4);
				String xpath = line.substring(  p2+2, line.lastIndexOf("=["));
				String STARTDATA = "[value=";
				int istart = line.lastIndexOf(STARTDATA)+STARTDATA.length();
				String data =line. substring( istart, line.length() -1);
				String partsTmp []= line.split("[ ]");
				
				try {// Do Mrz 31 18:05:47 Zentraleuropäische Sommerzeit 2011  SDF.format(new Date())
					timestamp = ""+SDF.parse(timestamp).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				a.perform(xpath, timestamp, data);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(e.getMessage()+"#"+lineCounter+"#{"+line+"}");
				//throw new ArrayIndexOutOfBoundsException(lineCounter);
			}
		}
		
	}

}


 