package org.jrobin.svg.awt;
/** 
 * <b>Description:TODO</b>
 * @author      vpupkin<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.03.2010::13:41:14<br> 
 */
public class PathIterator {

	private double[] Y;

	public PathIterator(double[] Y) {
		this.Y = Y;
	}

	public int[] getNextPath() {
		return new int[]{1,2,3,4,5};
	}

}


 