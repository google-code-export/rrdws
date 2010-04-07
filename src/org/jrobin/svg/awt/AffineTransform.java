package org.jrobin.svg.awt;

public class AffineTransform {
//transform="translate(522, 4 ) rotate(90)"
	public String toString(){
		return ""+
		((x+y+rotation)==0?"":"transform=\"")+
		(rotation == 0?"":(" rotate("+rotation+") " )+
		(((x+y) == 0)?"":(" translate("+x+" , "+y+") " )+
		((x+y+rotation)==0?"":"\"")
		))
				;
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
