package ws.rrd.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Map;
import java.util.TreeMap;

import org.jrobin.cmd.RrdCommander;
import org.jrobin.core.RrdException;

import ws.rrd.MemoryFileCache;
import ws.rrd.MemoryFileItem;
import ws.rrd.MemoryFileItemFactory;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.04.2010::11:48:50<br> 
 */
public class CSVParser {
	private String headersStr = null;
	String[] heads = null;
	final String DELIM = ",";
	BufferedReader rdr  ;
	
	public CSVParser(InputStream in) throws IOException{
        
		rdr  = new BufferedReader( new InputStreamReader ( in ) );
        
        headersStr  = rdr.readLine();
        heads = headersStr.split(DELIM);
        heads = combineChains(heads);
 
	}

	private String[]   combineChains(String[] quoteCOntainedStringPar ) {
		ArrayList<String> headsArr = new ArrayList<String>();
        for (int i=0;i<quoteCOntainedStringPar.length ;i++){
        	if (quoteCOntainedStringPar[i].startsWith("\"") && (!quoteCOntainedStringPar[i].endsWith("\""))){
        		StringBuffer sb = new StringBuffer("");
        		String suffix = "";
        		do{
        			sb.append(suffix);
        			sb.append(quoteCOntainedStringPar[i]);
        			if (quoteCOntainedStringPar[i].endsWith("\""))break; 
        			i++;        			
        			suffix = DELIM;        			
        		}while(i<quoteCOntainedStringPar.length);
        		headsArr.add(sb.toString().substring(1,sb.length()-1));
        	}else{
        		headsArr.add (quoteCOntainedStringPar[i].substring(1,quoteCOntainedStringPar[i].length()-1));
        	} 
        }
        quoteCOntainedStringPar = headsArr.toArray(new String[]{"XeXe"});
        return quoteCOntainedStringPar ;
	}

	public String[] getHeads() {
			return heads;
	} 
	
	//RrdCommander.execute(cmdTmp);
	public Object executeUpdate () throws IOException {
		
		
		Action a = new Action(){

			@Override
			public Object perform(String timestamp, String xpath, String data) {
				Object retval = "";
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
				long timestampTmp = 0;
				try {
					timestampTmp = sdf.parse(timestamp).getTime();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 
				String rrddb = "X"+xpath.hashCode()+".rrd";
				String cmdTmp = "rrdtool update "+rrddb +" "+(timestampTmp/1000L) +":"+ data; 
				try {   
					retval  = RrdCommander.execute(cmdTmp );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
					if (e instanceof FileNotFoundException){
						String cmdCreate = "rrdtool create "+rrddb+" --start "+((timestampTmp/1000L)-10)+" -s 300 "  +
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
						
						
						try {
							System.out.println(xpath +" --->  "+cmdCreate); 
							retval  =RrdCommander.execute(cmdCreate);
							MemoryFileCache.getCache().put(rrddb,xpath);
							retval  =RrdCommander.execute(cmdTmp );
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						} catch (RrdException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						}
					}
				} catch (RrdException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					retval = e; 
				} 
				return retval;
			}
			
			
		};
		 
		return this .perform(a ) ;
	}
	
	/**
	 * testdrive into System.out
	 * @author vipup
	 * @return
	 * @throws IOException
	 */
	public Object perform( ) throws IOException{
		Action a = new Action(){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
			@Override
			public Object perform(String timestamp, String xpath, String data) {
				try {
					long timestampTmp =  sdf.parse(timestamp).getTime();
					String cmdTmp = "rrdtool update "+xpath+" "+(timestampTmp/1000L) +":"+ data;
					System.out.println( cmdTmp  );
					return cmdTmp;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return e;
				}
			}	
		};
		return this .perform(a );		
	}
	
	
	/**
	 * iterate through in:InputStream line-by-line and call for any NewLine "rrdtool update" for corresponding rrd-DB.
	 *  - first column will be recognized as event-time
	 *  - will skip any duplicated header-lines
	 *  - will skip any duplicated header-lines
	 * 
	 * 
	 * @author vipup
	 * @param a
	 * @return
	 * @throws IOException
	 */
	Object perform(Action a) throws IOException{
		Map retval = new TreeMap();
		long start = System.currentTimeMillis();
		int lineCount =0;
        for (String nextLineStr = rdr.readLine();nextLineStr != null;nextLineStr = rdr.readLine()){
        	lineCount++;
        	if (nextLineStr.equals(headersStr))continue;
        	if (nextLineStr.equals(""))continue;
        	
        	String[] dataTmp =   nextLineStr.split(DELIM);
        	try{
        		dataTmp = combineChains(dataTmp);
        	}catch(StringIndexOutOfBoundsException e){
        		throw new IOException("error at parcing line "+lineCount +"["+nextLineStr+"]",e);
        	}
        	int i=0;
        	for (String next:dataTmp){
        		if (next == dataTmp[0])continue;
        		// timestamp, uri, data
        		if (!("".equals(next) || " ".equals(next) ) ){
        			String keyTmp = heads[i++];
        			final Object valTmp = a.perform(dataTmp[0], keyTmp,    next );
        			ArrayList line = (ArrayList )retval.get( keyTmp);
        			if( line == null ){
        				line = new ArrayList();
        				retval.put(keyTmp, line);
        			}
        			if (valTmp instanceof Exception){
        				//((Exception)valTmp ).printStackTrace();
        			}else{
        				line.add( ""+valTmp+":"+next );
        			}
        		}
        	}
        }
        System.out.println("done with "+(1000.00*(1+lineCount)/((System.currentTimeMillis()-start)+1) ) +" lps.");
        return retval;
	}
}


 