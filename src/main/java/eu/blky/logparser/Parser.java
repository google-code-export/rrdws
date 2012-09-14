package eu.blky.logparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  11.09.2012::14:05:12<br> 
 */
public class Parser {
	public static final String DEFAULT_LINE_PATTERN =  
	  "(\\S+:)?(\\S+? \\S+?) \\S+? DEBUG \\S+? - DEMANDE_ID=(\\d+?) - listener (\\S+?) : (\\S+?)" ;
	private static final String DATE_PATTERN = "dd/MMM/yyyy:hh:mm:ss Z";
	
	private Pattern pattern;
	public Parser(String patternPar){
		this.pattern = Pattern.compile(DEFAULT_LINE_PATTERN);
		this.pattern = Pattern.compile(patternPar);
	}

	public EventLog parse9(String line) throws ParseException {
		  
		long httpSize;
	    long startTime;
	    long endTime;
	    String reqType = null;
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
	    int errorOffset= -1;
		if (null == line ) throw new ParseException("NULL in source!", errorOffset);
	    Matcher matcher = pattern.matcher(line); //matcher.groupCount()
	    if (matcher.matches()) {
	        String httpStatus = matcher.group(6);
	        httpSize = Long.parseLong( matcher.group(7));
	        long time = sdf.parse(matcher.group(4)).getTime();
	        startTime = time;
	        endTime = time + Integer.parseInt(  matcher.group(8) );
	        
	        if (matcher.group(5).toUpperCase().startsWith("POST /iboServer3.1/Ibo.Server.rem HTTP/1.1".toUpperCase()))  
	        	reqType = "PEGAV.3.1";
	        else if (matcher.group(5).toUpperCase().startsWith("POST /iboServer3.2/Ibo.Server.rem HTTP/1.1".toUpperCase()))  
	        	reqType = "PEGAV.3.2";
	        else if (matcher.group(5).toUpperCase().startsWith("POST /iboServer3.0/Ibo.Server.rem HTTP/1.1".toUpperCase()))  
	        	reqType = "PEGAV.3.0";
	        else if (matcher.group(5).toUpperCase().startsWith("GET /WebUrlaub30".toUpperCase()))  
	        	reqType = "WU3.0";
	        else if (matcher.group(5).toUpperCase().startsWith("GET /WebUrlaub31".toUpperCase()))  
	        	reqType = "WU3.1";
	        else
	        	reqType = "UNKNOWN";
	        String useragentTmp = matcher.group(9);
	        return new EventLog(useragentTmp , reqType, httpStatus, httpSize, startTime, endTime);
	    }
	    return null;
	}
	public EventLog parse8(String line) throws ParseException {
	  
		long httpSize;
	    long startTime;
	    long endTime;
	    String reqType = null;
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
	    int errorOffset= -1;
		if (null == line ) throw new ParseException("NULL in source!", errorOffset);
	    Matcher matcher = pattern.matcher(line); //matcher.groupCount()
	    if (matcher.matches()) {
	        String httpStatus = matcher.group(6);
	        httpSize = Long.parseLong( matcher.group(7));
	        long time = sdf.parse(matcher.group(4)).getTime();
	        startTime = time;
	        endTime = time + Integer.parseInt(  matcher.group(8) );
	        
	        if (matcher.group(5).toUpperCase().startsWith("POST /iboServer3.1/Ibo.Server.rem HTTP/1.1".toUpperCase()))  
	        	reqType = "PEGAV.3.1";
	        else if (matcher.group(5).toUpperCase().startsWith("POST /iboServer3.2/Ibo.Server.rem HTTP/1.1".toUpperCase()))  
	        	reqType = "PEGAV.3.2";
	        else if (matcher.group(5).toUpperCase().startsWith("POST /iboServer3.0/Ibo.Server.rem HTTP/1.1".toUpperCase()))  
	        	reqType = "PEGAV.3.0";
	        else if (matcher.group(5).toUpperCase().startsWith("GET /WebUrlaub30".toUpperCase()))  
	        	reqType = "WU3.0";
	        else if (matcher.group(5).toUpperCase().startsWith("GET /WebUrlaub31".toUpperCase()))  
	        	reqType = "WU3.1";
	        else
	        	reqType = "UNKNOWN"; 
	        return new EventLog( reqType, httpStatus, httpSize, startTime, endTime);
	    }
	    return null;
	}

}


 