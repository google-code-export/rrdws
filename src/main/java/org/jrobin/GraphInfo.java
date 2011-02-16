package org.jrobin;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  16.02.2011::16:04:01<br> 
 */
public interface GraphInfo {
	/**
	 * Returns graph bytes
	 *
	 * @return Graph bytes
	 */
	public byte[] getBytes() ;

	/**
	 * Returns the number of bytes in the graph file
	 *
	 * @return Length of the graph file
	 */
	public int getByteCount() ;
 

	/**
	 * Returns total graph height
	 *
	 * @return total graph height
	 */
	public int getHeight();

	/**
	 * Returns image information requested by {@link RrdGraphDef#setImageInfo(String)} method
	 *
	 * @return Image information
	 */
	public String getImgInfo();

	/**
	 * Returns PRINT lines requested by {@link RrdGraphDef#print(String, String, String)} method.
	 *
	 * @return An array of formatted PRINT lines
	 */
	public String[] getPrintLines();
	/**
	 * Returns total graph width
	 *
	 * @return total graph width
	 */
	public int getWidth();

	/**
	 * Dumps complete graph information. Useful for debugging purposes.
	 *
	 * @return String containing complete graph information
	 */
	public String dump() ;
}


 