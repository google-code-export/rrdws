package ws.rrd.json;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  22.11.2012::15:12:47<br> 
 */
public class Tool { 
	static String _mapChar2Escape[][]=  
	  // Be sure to have backslash at first. 
	  // We don't want to escape backslashes in escaped characters.
	{
	  	{"\\", "\\\\"},    // Backslash
		{"\"", "\\u0022"}, // Double quote
		{"'", "\\u0027"},  // Single quote
		{"&", "\\u0026"},  // Ampersand
		{"<", "\\u003c"},  // Less than
		{">", "\\u003e"},  // Greater than
		{"=", "\\u003d"}  // Equals
	};
	 
	/**
	 * Returns a new string that has JavaScript literals escaped.
	 *
	 * @param strSource Source string
	 * @return
	 */
	public static String escapeJavaScript(String strSource)
	{
	  String strEscaped = strSource;
	 
	  for (int i=0;i<_mapChar2Escape.length;i++)
	  {
	    strEscaped = strEscaped.replace(_mapChar2Escape[i][0] , _mapChar2Escape[i][1]  );
	  }
	 
	  return strEscaped;
	} 
}


 