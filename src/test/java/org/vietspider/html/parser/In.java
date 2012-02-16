package org.vietspider.html.parser;

import java.io.IOException;
import java.io.InputStream;
 
public class In {

	private String string;

	public In(String string) {
		this.string = string;
	}

	public String readAll() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(string);
		byte[] buf = new byte[in.available()];
		in.read(buf);
		 return new String(buf);
	}

}


 