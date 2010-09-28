package org.vietspider.chars;

import java.io.CharConversionException;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  10.06.2010::11:15:03<br> 
 */
public interface MyByteToCharConverter {

	String getCharacterEncoding();

	float getMaxCharsPerByte();

	int convert(byte[] ba, int off, int i, char[] ca, int j, int en) throws CharConversionException;

	void reset();

	int nextCharIndex();

	int flush(char[] ca, Object nextCharIndex, int en);

}


 