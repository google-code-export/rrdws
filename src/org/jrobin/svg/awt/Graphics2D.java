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
		// TODO Auto-generated method stub
		
	}

	public AffineTransform getTransform() {
		return new AffineTransform();
	}

	public void setClip(int x, int y, int width, int height) {
		System.out.println( "setClip(int "+x+", int"+ y+" , int "+width+", int "+height+") ");
	}

	public void translate(int x, int y) {
		System.out.println( "translate(int "+x+", int"+ y+"  ) ");
	}

	public void rotate(double angle) {
		System.out.println( "rotate(int "+angle+"  ) ");
	}

	public void setTransform(AffineTransform aftInitial) {
		System.out.println( "setTransform(int "+aftInitial+"  ) ");
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void fillRect(int x, int y, int width, int height) {
		System.out.println("fillRect(int "+x+", int"+ y+", int "+width+", int "+height+")");
	}

	public void fillPolygon(int[] x, int[] y, int length) {
		System.out.println("fillPolygon(int[] "+x+", int[]"+ y+", length "+length+"  )");
	}

	public void drawPolygon(int[] dev, int[] dev2, int length) {
		System.out.println("drawPolygon(int[] "+dev+", int[]"+ dev2+", length "+length+"  )");
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		System.out.println("drawLine(int  "+x1+", int "+ y1+"int  "+x2+", int "+ y2  +"  )");
	}

	public void drawPolyline(int[] x, int[] y, int length) {
		System.out.println("drawPolyline(int[] "+x+", int[]"+ y+", length "+length+"  )");
	}	
	public void drawPolyline(double[] x, double[] y, int length) {
		System.out.println("drawPolyline(int[] "+x+", int[]"+ y+", length "+length+"  ){");
		for (int i=0;i<length;i++)
			System.out.print("  "+x[i]+","+ y[i]   );
		System.out.println("}");
		
	}

	public void setFont(Font font) {
		System.out.println("setFont(  "+font+"  )");
	}

	public void drawString(String text, int x, int y) {
		System.out.println("drawString(  "+text+"@"+x+":"+y+"  )");
	}

	public FontRenderContext getFontRenderContext() {
		return FontRenderContext.DEFAULT;
	}

	public void setRenderingHint(String keyTextAntialiasing,
			String valueTextAntialiasOn) {
		this.keyTextAntialiasing =  keyTextAntialiasing;
		this.valueTextAntialiasOn =  valueTextAntialiasOn;
		System.out.println("keyTextAntialiasing = "+keyTextAntialiasing + ", valueTextAntialiasOn ="+valueTextAntialiasOn);
	}

 

}
