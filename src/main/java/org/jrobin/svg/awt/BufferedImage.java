package org.jrobin.svg.awt;
 

public class BufferedImage {

	private Graphics2D g;
	private int width;
	private int height;
	private String typeIntRgb;
	private StringBuffer buffer;

	public BufferedImage(int width, int height, String typeIntRgb) {
		this.width = width;
		this.height = height;
		this.typeIntRgb = typeIntRgb;
		createGraphics(new StringBuffer());
	}

	public static final String TYPE_INT_RGB = null;

	public Graphics2D createGraphics(StringBuffer buffer) {
		this.g = new Graphics2D(this);
		this.buffer = buffer;
		return this.g;
	}

	public Graphics getGraphics() {
		return g;
	}

	public int getWidth() {
			return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
			return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTypeIntRgb() {
			return typeIntRgb;
	}

	public void setTypeIntRgb(String typeIntRgb) {
		this.typeIntRgb = typeIntRgb;
	}

	public void echo(String string) {
		this.buffer.append( string );
	}

	public byte[] getBytes() {
		return this.buffer.toString().getBytes();
	}

}
