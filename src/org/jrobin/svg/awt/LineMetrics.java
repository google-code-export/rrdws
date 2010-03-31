package org.jrobin.svg.awt;
/** 
 * <b>Description:TODO</b>
 * @author      xco5015<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 OXSEED AG <br>
 * <b>Company:</b>       OXSEED AG  <br>
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
		return data.length()%3;
	}

	public int getDescent() {
		return data.length()%5;
	}

}


 