package org.jrobin.svg.awt;
/** 
 * <b>Description:TODO</b>
 * @author      vpupkin<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.03.2010::13:43:20<br> 
 */
public class LineMetrics {

	private String data;
	public String toString(){
		return "LineMetrics:::"+data;
	}

	public LineMetrics(String data) {
		this.data = data;
	}

	public int getAscent() {
		return 6;
	}

	public int getDescent() {
		return 6;
	}

}


 