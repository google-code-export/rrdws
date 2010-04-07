package org.jrobin.svg.awt;
/** 
 * <b>Description:TODO</b>
 * @author      vpupkin<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.03.2010::14:04:52<br> 
 */
public class ImageIcon {

	private byte[] data;

	public ImageIcon(byte[] imageData) {
		this.data = imageData;
	}

	public void paintIcon(Object object, Graphics g, int i, int j) {
		System.out.println( "public void paintIcon(Object object, Graphics g, int i, int j) ");
		g.paintIcon(i,j,getData());
	}

	public byte[] getData() {
			return data;
	}

}


 