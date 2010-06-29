package org.jrobin.svg.awt;

public class AffineTransform {
//transform="translate(522, 4 ) rotate(90)"
	//double radians = (Math.PI / 180) * degrees;
	public String toString(){
		double degrees =   rotation / (Math.PI / 180) ;
		return ""+
		 "transform=\"" +
		 "translate("+x+" , "+y+") "  +
		 " rotate("+degrees+") "  +
		 "\"" ;
	}
	
	double rotation = 0; 
	public void rotate(double angle) {
		rotation += angle;
	}
	int x = 0;
	int y = 0;
	public void translate(int x, int y) {
		this.x+=x;
		this.y+=y;
	}

}
