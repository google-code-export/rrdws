package cc.co.llabor.threshold.nagios;

import java.io.*;
import java.util.*;
/** 
 * <b> 
 * The CommentedProperties class is an extension of java.util.Properties
 * to allow retention of comment lines and blank (whitespace only) lines
 * in the properties file.
 * 
 * </b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  17.02.2012::10:02:59<br> 
 */
public class CommentedProperties extends java.util.Properties {

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = 9183011519738049548L;

	private static final String STD_ENCODING = "ISO-8859-1";

	private static final String EOL = "\n";

	/**
	 * parallel comments-properties-array
	 */
	java.util.Properties  comments = new Properties();

	private int linecount;

	/**
	 * Load properties from the specified InputStream. 
	 * Overload the load method in Properties so we can keep comment and blank lines.
	 * @param   inStream   The InputStream to read.
	 */
	public void load(InputStream inStream) throws IOException
	{
		// The spec says that the file must be encoded using ISO-8859-1.
		BufferedReader inBuf =
		new BufferedReader(new InputStreamReader(inStream, STD_ENCODING));
		StringWithComments line;
		try{
			while ((line = readln(inBuf)) != null) {
				String key_val []= line.getLine().replace(" ", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t", ";").split("=");
				String keyTmp = key_val [0];
				String valTmp= line.getLine().substring(keyTmp.length()+1).trim(); 
				String comments2 = line.getComments();
				this.comments.put(keyTmp, comments2);
				this.put(keyTmp, valTmp); 
			}
		}catch (NullPointerException e){}
	}
	/**
	 * return trimmed, uncommented, non-empty line of CFG-File
	 * @author vipup
	 * @param inBuf 
	 * @return
	 * @throws IOException
	 */
	private StringWithComments readln(BufferedReader inBuf) throws IOException {
		String retval = inBuf.readLine();
		
		linecount++;
		String skipped = "";
		String prefix = "";
		try{
			retval = retval.trim();
			// #
			while (retval.startsWith("#")||"".equals( retval) ){
				skipped += prefix;
				skipped += retval;
				prefix += EOL;
				retval = inBuf.readLine();
				retval = retval.trim();				
			}
			
		}catch(NullPointerException e){
			//e.printStackTrace();
		}
		//System.out.println(skipped);
		return new StringWithComments(retval, skipped);
	}
	/**
	 * Write the properties to the specified OutputStream.
	 * 
	 * Overloads the store method in Properties so we can put back comment	
	 * and blank lines.													  
	 * 
	 * @param out	The OutputStream to write to.
	 * @param header Ignored, here for compatability w/ Properties.
	 * 
	 * @exception IOException
	 */
	public void store(OutputStream out, String header) throws IOException
	{
		// The spec says that the file must be encoded using ISO-8859-1.
		PrintWriter pw
		= new PrintWriter(new OutputStreamWriter(out, STD_ENCODING));
 
		for(String key:this.keySet().toArray(new String[]{})){
			String commentTmp = comments.getProperty(key);
			if (commentTmp!=null){
				pw.println(commentTmp);
			}
			pw.println(key+"="+this.getProperty(key));
		} 
		pw.flush ();
	}
  
	@Override
	public Object setProperty(String key, String value){
		return super.setProperty(key, value); 
	}
	
	public Object setProperty(String key, String value, String comment){
		this.comments.put(key, comment.trim());
		return super.setProperty(key, value); 
	}
	
	/**
	 * Return comment, associated with property
	 * @author vipup
	 * @param string
	 * @return
	 */
	public String getPropertyComment(String key) { 
		return comments.getProperty(key); 
	}
	
}


 