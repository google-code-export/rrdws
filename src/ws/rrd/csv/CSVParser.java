package ws.rrd.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList; 

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
	
	public CSVParser(InputStream in) throws IOException{
        String DELIM = ",";
        BufferedReader rdr  = new BufferedReader( new InputStreamReader ( in ) );
        
        headersStr  = rdr.readLine();
        heads = headersStr.split(DELIM);
        ArrayList<String> headsArr = new ArrayList<String>();
        for (int i=0;i<heads.length ;i++){
        	if (heads[i].startsWith("\"") && (!heads[i].endsWith("\""))){
        		StringBuffer sb = new StringBuffer("");
        		String suffix = "";
        		do{
        			sb.append(suffix);
        			sb.append(heads[i]);
        			if (heads[i].endsWith("\""))break; 
        			i++;        			
        			suffix = DELIM;        			
        		}while(i<heads.length);
        		headsArr.add(sb.toString().substring(1,sb.length()-1));
        	}else{
        		headsArr.add (heads[i].substring(1,heads[i].length()-1));
        	} 
        }
        heads = headsArr.toArray(new String[]{"XeXe"});
        
        
        for (String nextLineStr = rdr.readLine();nextLineStr != null;nextLineStr = rdr.readLine()){
        	if (nextLineStr.equals(headersStr))continue;
        	
        	String[] dataTmp =   nextLineStr.split(DELIM);
        	int i=0;
        	for (String next:dataTmp){
        		System.out.println(heads[i++]+" == [" +next+"]");
        	}
        }
	}

	public String[] getHeads() {
			return heads;
	} 
}


 