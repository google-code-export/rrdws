package org.jrobin.svg.awt;

import java.io.InputStream;

//import com.sun.image.codec.jpeg.JPEGDecodeParam;

public class Font {

	/**
	 *  
  <text font-family="Verdana" font-size="42.5" fill="blue" >
    <textPath xlink:href="#MyPath" startOffset="80%">
      We go up, then we go down, then up again
    </textPath>
  </text>

	 */
	public String toString(){
		return "" +
				"font-family=\""+name+"\" " +
				"font-size=\""+size+"\" " +
				"font-style=\""+style +"\" " +
				"font-weight=\""+weight +"\" " +
				"fill=\""+color+ "\" " +
				"stroke-width=\"0\" " +
				"stroke=\""+stroke+"\"";
	}
	
	private String name;
	private String style = "normal" ; // normal | italic | oblique
	private String weight = "normal" ;//normal | bold | bolder | lighter
	private int size = 10;
	private String color = "black";
	private String stroke="darkgreen";
	
	public Font(String defaultFontName, String plain2, int i) {
		this.name = defaultFontName;
		this.weight = plain2;
		this.size = i;
	}
	/*weight */
	public static final String BOLD = 	"bold";
	public static final String PLAIN = "normal";
	/*font-family  */
	public static final String TRUETYPE_FONT = "Verdana";
	public static Font createFont(String truetypeFont, InputStream fontStream) {
		// TODO Auto-generated method stub
		return null;
	}
	public LineMetrics getLineMetrics(String dummyText, FontRenderContext fontRenderContext) {
		String data = "LineMetrics getLineMetrics(String "+dummyText+" , FontRenderContext "+ fontRenderContext+" )";
		//System.out.println(data);
		return new LineMetrics(data);
	}
	public Rectangle2D getStringBounds(String text, int i, int length,
			FontRenderContext fontRenderContext) {
		String data = text;
		//System.out.println(data);
		return new Rectangle2D (data);
	}
	public Font deriveFont(String plain2, int i) {
		if (BOLD.equals(plain2))return new Font(TRUETYPE_FONT, BOLD, i);
		else return new Font(TRUETYPE_FONT, PLAIN, i);
	}
 

}
