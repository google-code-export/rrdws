package org.jrobin.svg.awt;

public class Graphics2D extends Graphics {

	private BufferedImage bi;
	private String keyTextAntialiasing;
	private String valueTextAntialiasOn;
	private Paint paint;
	private Stroke stroke;

	public Graphics2D(BufferedImage bufferedImage) {
		this.bi = bufferedImage;
	}

	public void dispose() {
		System_out_println("<!--   //////////////DISPOSE??????????????  -->");
		
	}

	public AffineTransform getTransform() {
		return new AffineTransform();
	}

	/**
	 * <clipPath id="cp1"></clipPath>
	 * @author xco5015
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setClip(int x, int y, int width, int height) {
		System_out_println( "<clipPath id=\"_X"+x+"_Y"+y+"_W"+width+"_H"+height+"\">"); 
			fillRect (x,  y,  width,  height);
		System_out_println( "</clipPath>"); 
	}

	public void translate(int x, int y) {
		System_out_println( "translate(int "+x+", int"+ y+"  ) ");
	}

	public void rotate(double angle) {
		System_out_println( "<!-- rotate(int "+angle+"  )  -->");
	}

	public void setTransform(AffineTransform aftInitial) {
		System_out_println( "<!-- setTransform(int "+aftInitial+"  )  -->");
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	/**
	 * <!-- schwarzer Hintergrund -->
  <rect x="0" y="0" width="360" height="510"
    style="fill:black; stroke:none;" />
	 * @author xco5015
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void fillRect(int x, int y, int width, int height) {
		System_out_println("<rect x=\""+x+"\" y=\""+ y+"\" width=\""+width+"\" height=\""+height+"\" style=\"fill:black; stroke:none;\">" );
	}

	public void fillPolygon(int[] x, int[] y, int length) {
		System_out_println("fillPolygon(int[] "+x+", int[]"+ y+", length "+length+"  )");
	}

	public void drawPolygon(int[] dev, int[] dev2, int length) {
		System_out_println("drawPolygon(int[] "+dev+", int[]"+ dev2+", length "+length+"  )");
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
		//System_out_println("setStroke(Stroke"+ stroke+")");
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		drawPolyline(  new double[] {(double)x1,x2} , new double[] { y1,y2}, 2 );
	}

	public void drawPolyline(int[] x, int[] y, int length) {
		System_out_println("drawPolyline(int[] "+x+", int[]"+ y+", length "+length+"  )");
	}	
	public void drawPolyline(double[] x, double[] y, int length) {
		System_out_println("<polyline points=\"");
		for (int i=0;i<length;i++)
			System.out.print("  "+x[i]+","+ y[i]   );
		System_out_println("\"style=\"fill:white;stroke:red;stroke-width:2\"/>");
		
	}

	public void setFont(Font font) {
		System_out_println("setFont(  "+font+"  )");
	}

	public void drawString(String text, int x, int y) {
		System_out_println("drawString(  "+text+"@"+x+":"+y+"  )");
	}

	public FontRenderContext getFontRenderContext() {
		return FontRenderContext.DEFAULT;
	}

	public void setRenderingHint(String keyTextAntialiasing,
			String valueTextAntialiasOn) {
		this.keyTextAntialiasing =  keyTextAntialiasing;
		this.valueTextAntialiasOn =  valueTextAntialiasOn;
		//System_out_println("keyTextAntialiasing = "+keyTextAntialiasing + ", valueTextAntialiasOn ="+valueTextAntialiasOn);
	}

	private void System_out_println(String string) {
		System.out.println(string);
		this.bi.echo(string);
	}
 

}
