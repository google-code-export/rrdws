package org.jrobin.svg.awt;

public class BasicStroke implements Stroke{

	private float width = 1.0f;
	
	public String toString(){
		return ""+width;
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

}
