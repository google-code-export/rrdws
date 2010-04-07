package org.jrobin.svg.awt;

public class BasicStroke implements Stroke{

	private float width = 0.2f;
	
	public String toString(){
		return ""+color;
	}

	public BasicStroke(int i) {
		this.width = i;
	}

	public BasicStroke(float width) {
		this.width = width;
	}

	public float getWidth() {
			return width;
	}
	Color color = Color.BLACK;
	@Override
	public Color getColor() {

		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
