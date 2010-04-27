package ws.rrd.csv;

import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader; 
import java.util.ArrayList; 
import java.util.Map;
import java.util.TreeMap;  

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

	String[] getHeads() {
			return heads;
	} 
	

	
	/**
	 * iterate through in:InputStream line-by-line and call for any NewLine "rrdtool update" for corresponding rrd-DB.
	 *  - first column will be recognized as event-time
	 *  - will skip any duplicated header-lines
	 *  - will ignore any duplicated-time samples
	 *  
	 * @author vipup
	 * @param a
	 * @return list of compleeted work
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


 