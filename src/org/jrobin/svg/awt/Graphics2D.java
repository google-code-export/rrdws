package org.jrobin.svg.awt;

public class Graphics2D extends Graphics {

	private BufferedImage bi;
	private String keyTextAntialiasing;
	private String valueTextAntialiasOn;
	private Paint paint;
	private Stroke stroke = new BasicStroke(0);
	private Paint fill;
	

	public Graphics2D(BufferedImage bufferedImage) {
		this.bi = bufferedImage;
	}

	public void dispose() {
		System_out_println("<!--   //////////////DISPOSE??????????????  -->");
		
	}
	AffineTransform transformation = new AffineTransform();
	public AffineTransform getTransform() {
		
		return transformation;
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
		this.transformation.translate(x, y);
		System_out_println( "<!-- translate(int "+x+", int"+ y+"  )  -->");
	}

	public void rotate(double angle) {
		this.transformation.rotate(angle);
		System_out_println( "<!-- rotate(int "+angle+"  )  -->");
	}

	public void setTransform(AffineTransform aftInitial) {
		this.transformation = aftInitial;
		System_out_println( "<!-- setTransform(int "+aftInitial+"  )  -->");
	}

	
	public void setPaint(Paint paint) {
		System_out_println( "<!-- setPaint (  "+paint+"  )  -->");
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
		Paint fillBak = this.fill;
		this.fill = this.paint;//fill-opacity=\"0.1\" 
		System_out_println("<rect   x=\""+x+"\" y=\""+ y+"\" width=\""+width+"\" height=\""+height+"\" style=\"fill:"+this.paint+"; stroke:none ;\"/>" );
		this.fill = fillBak;
	} 

	public void fillPolygon(int[] x, int[] y, int length) {
		Paint fillBak = this.fill;
		this.fill = this.paint;
		drawPolyline(  x,   y,  length);
		this.fill = fillBak;
	}
	public void fillPolygon(double[] x, double[] y, int length) {
		Paint fillBak = this.fill;
		this.fill = this.paint;
		drawPolyline(  x,   y,  length);
		this.fill = fillBak;
	}

	public void drawPolygon(int[] x, int[] y, int length) {
		drawPolyline(  x,   y,  length);
	}
	public void drawPolygon(double[] x, double[] y, int length) {
		drawPolyline(  x,   y,  length);
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
		System_out_println("<!-- setStroke(Stroke"+ stroke+") -->");
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		System_out_println(" <!-- line ( "+ x1 +":"+ y1 + " - "  + x2 +":"  + y2 +") -->" );
		drawPolyline(  new double[] {(double)x1,x2} , new double[] { y1,y2}, 2 );
	}

	public void drawPolyline(int[] x, int[] y, int length) {
		double[] yD = new double[length];
		double[] xD = new double[length];
		for(int i=0;i<length;i++){
			xD[i]=x[i];
			yD[i]=y[i];
		}
		drawPolyline(  xD,   yD,  length);
	}	
	public void drawPolyline(double[] x, double[] y, int length) {
		System_out_println("<polyline  points=\"");
		for (int i=0;i<length;i++){
			if (("NaN".equals( ""+ y[i])) ||("NaN".equals( ""+ x[i])))   continue;
			 double xTmp =  "NaN".equals( ""+ x[i])? xBottom:x[i] ;
			 double yTmp =  "NaN".equals( ""+ y[i])? yBottom:y[i] ;
			  
			 System_out_println("  "+xTmp+","+ yTmp   );
		}
		if (this.fill == null)
			System_out_println("\" style=\"fill:none;stroke:"+this.paint+";stroke-width:"+this.stroke.getWidth()+" \"/>");
		else
			System_out_println("\" style=\"fill:"+this.fill+ ";stroke:"+this.stroke+";stroke-width:"+this.stroke.getWidth()+" \"/>");
		
	}

	Font font ;
	public void setFont(Font font) {
		this.font =  font;
		System_out_println("<!-- setFont(  "+font+"  ) -->");
	}

	public void drawString(String text, int x, int y) {	
		System_out_println("<text "+ this.font +"  x=\""+x+"\" y=\""+y+"\" "+this.transformation+" >"+text+"</text>");
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

	@Override
	public void paintIcon(int i, int j, byte[] data) {
		this.bi.createGraphics(new StringBuffer("<svg>"+new String(data)+"</svg>"));		 
	}

	private double yBottom  = 0.0;
	private double xBottom  = 0.0;
	
	public void fillPolygon(double[] x, double yBottom, double[] yTop, int length) {
		this.yBottom = yBottom;
		if (yTop[length-1]==yBottom){
			this.fillPolygon(x, yTop, length);
		}else{
			// fix polygon
			double x1 []= new double[length+1] ;
			double y1 []= new double[length+1] ;
			// cp x -> x1 , y -> y1
			int i=0;
			for (;i<length;i++){
				x1[i]=x[i];
				y1[i]=yTop[i];
			}
			x1[i] = x[length-1];
			y1[i] = yBottom;
			this.fillPolygon(x1, y1, length+1);
		}
	}	
	public void drawPolygon(double[] x, double yBottom, double[] yTop, int length) {
		this.yBottom = yBottom;
		this.drawPolygon(x, yTop, length);
	}
 

}
