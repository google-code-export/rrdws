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
				"stroke-width=\""+stroke.getWidth()+"\" " +
				"stroke=\""+stroke+"\"";
	}
	
	private String name;
	private String style = "normal" ; // normal | italic | oblique
	private String weight = "normal" ;//normal | bold | bolder | lighter
	private int size = 10;
	private Color color = Color.BLACK;
	private Stroke  stroke= new BasicStroke(0);
	
	public Font(String defaultFontName, String plain2, int i) {
		this.name = defaultFontName;
		this.weight = plain2;
		this.size = i;
	}
	/*weight */
	public static final String BOLD = 	"bold";
	public static final String PLAIN = "normal";
	/*font-family  */
	public static final String TRUETYPE_FONT = "'Super Sans', Helvetica, sans-serif ";
	public static Font createFont(String truetypeFont, InputStream fontStream) {
		return ttf2font();
	}
	private static Font ttf2font() {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("not yet implemented since 07.04.2010");
		else {
		return null;
		}
	}
	public LineMetrics getLineMetrics(String dummyText, FontRenderContext fontRenderContext) {
		String data = "LineMetrics getLineMetrics(String: "+dummyText+" , FontRenderContext: "+ fontRenderContext+" )";
		System.out.println("<!-- getStringBounds ::"+data+" -->");
		return new LineMetrics(data);
	}
	public Rectangle2D getStringBounds(String text, int i, int length,
			FontRenderContext fontRenderContext) {
		String data = text;
		System.out.println("<!-- getStringBounds ::"+data+" -->");
		return new Rectangle2D (data);
	}
	public Font deriveFont(String plain2, int i) {
		if (BOLD.equals(plain2))return new Font(TRUETYPE_FONT, BOLD, i);
		else return new Font(TRUETYPE_FONT, PLAIN, i);
	}
	public Color getColor() {
			return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Stroke getStroke() {
			return stroke;
	}
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
 

}
