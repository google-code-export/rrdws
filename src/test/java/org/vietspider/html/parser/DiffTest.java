package org.vietspider.html.parser;

import java.io.IOException;

import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  12.08.2010::18:39:37<br> 
 */
public class DiffTest extends TestCase {
	public void testAAA() throws IOException{
		Diff.main(new String[]{"A/AAA.txt","B/AAA.txt"});
	} 

}


 