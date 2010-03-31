package org.jrobin.svg.awt;

import java.io.InputStream;

//import com.sun.image.codec.jpeg.JPEGDecodeParam;

public class Font {

	public Font(String defaultFontName, String plain2, int i) {
		// TODO Auto-generated constructor stub
	}
	public static final String BOLD = null;
	public static final String PLAIN = null;
	public static final String TRUETYPE_FONT = null;
	public static Font createFont(String truetypeFont, InputStream fontStream) {
		// TODO Auto-generated method stub
		return null;
	}
	public LineMetrics getLineMetrics(String dummyText, FontRenderContext fontRenderContext) {
		String data = "LineMetrics getLineMetrics(String "+dummyText+" , FontRenderContext "+ fontRenderContext+" )";
		System.out.println(data);
		return new LineMetrics(data);
	}
	public Rectangle2D getStringBounds(String text, int i, int length,
			FontRenderContext fontRenderContext) {
		String data = "Rectangle2D: getStringBounds(String :"+text+" , int"+ i+" , int "+ length+" , FontRenderContext "+fontRenderContext +");";
		System.out.println(data);
		return new Rectangle2D (data);
	}
	public Font deriveFont(String plain2, int i) {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 31.03.2010");
		else {
		return null;
		}
	}
 

}
