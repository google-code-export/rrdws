package org.jrobin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.IOException;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  15.04.2010::13:45:28<br> 
 */
public interface ImageWorkerInterface {

	void dispose();

	byte[] saveImage(String filename, String imageFormat, float imageQuality) throws IOException;

	byte[] getImageBytes(String imageFormat, float imageQuality) throws IOException;

	void loadImage(String overlayImage) throws IOException;

	int getFontAscent(Object font);

	void transform(int x, int y, double d); 
	void reset();

	void clip(int i, int j, int k, int l);

	void drawLine(int xorigin, int y, int i, int y2, Object color, Object basicStroke);

	int getStringWidth(String title, Object largeFont);

	void drawString(String title, int x, int y, Object largeFont, Object paint); 

	void setAntiAliasing(boolean antiAliasing);

	void drawPolyline(double[] x, double[] y, Object color, Object basicStroke);

	void fillPolygon(double[] x, double areazero, double[] y, Object color);

	void fillPolygon(double[] x, double[] lastY, double[] y, Object color);

	void fillRect(int i, int j, int xgif, int ygif, Object paint);

	void resize(int xgif, int ygif);

	double getFontHeight(Object smallFont);

}


 