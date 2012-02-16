package cc.co.llabor.features;

import junit.framework.TestCase;


/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  06.07.2011::13:46:26<br> 
 */
public class RepoTest extends TestCase{
	public void test1(){
		String actual = Repo.getBanner( "theTest");
		String expected = "passed";
		 this.assertEquals(expected, actual);
	}
}


 