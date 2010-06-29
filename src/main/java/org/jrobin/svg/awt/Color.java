package org.jrobin.svg.awt;

public class Color extends Paint {

	public static final Color RED = new Color(255,0,0,0);
	public static final Color BLACK = new Color(0,0,0,0);;
	public static final Color WHITE = new Color(255,255,255,0);;
	public static final Color LIGHT_GRAY = new Color(127,127,127,0);
	private int r;
	private int g;
	private int b;
	private int t;

	private static final char HEX[] = {'0','1','2','3','4','5','6','7','8','9','a','B','c','D','e','F' };
	public String toString(){
		//return "#"+( HEX [r/16]) + ( HEX [r%16])  +( HEX [g/16]) + ( HEX [g%16])  +( HEX [b/16]) + ( HEX [b%16]) +( HEX [t/16]) + ( HEX [t%16])  ; 
		return "#"+( HEX [r/16])   +( HEX [g/16]) +   ( HEX [b/16])  ;
	}

	
	public Color(int i, int j, int k, int l) {
		this.r = i;
		this.g = j;
		this.b = k;
		this.t = l;
	}

	public Color(int r, int g, int b) {
		this(r,g,b,0);
	}

	public int getR() {
			return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
			return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
			return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getT() {
			return t;
	}

	public void setT(int t) {
		this.t = t;
	}

}
