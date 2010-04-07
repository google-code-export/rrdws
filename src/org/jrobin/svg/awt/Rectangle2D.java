package org.jrobin.svg.awt;

//import com.sun.image.codec.jpeg.JPEGDecodeParam;

/** 
 * <b>Description:TODO</b>
 * @author      vpupkin<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.03.2010::13:48:11<br> 
 */
public class Rectangle2D {

	private String data;
	public String toString(){
		return "Rectangle2D:::"+data;
	}

	public Rectangle2D(String data) {
		this.data = data;
	}

	public Rectangle2D getBounds() {
		return this;
	}

	public double getWidth() {
		return 2 * Math.E * data.length();
	}

 

}


 