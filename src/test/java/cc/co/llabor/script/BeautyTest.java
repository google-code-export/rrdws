package cc.co.llabor.script;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

import ws.rrd.server.LServlet;

/** 
 * <b>Description:TODO</b>
 * @author      gennady<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 OXSEED AG <br>
 * <b>Company:</b>       OXSEED AG  <br>
 * 
 * Creation:  Sep 8, 2010::8:20:43 PM<br> 
 */
public class BeautyTest {

	@Test
	public void testFire() throws IOException, ScriptException {
		Beauty b = new Beauty();
		String scriptIn = "var a=1;var b=2; ";
		String res = b.fire(scriptIn );
		//System.out.println(res);
		assertTrue(res,res.indexOf("\n")>0);
	}
	

	@Test
	public void testFire2() throws IOException, ScriptException {
		Beauty b = new Beauty();
		String scriptIn = new String ( LServlet.getResourceAsBA("fpJ2E8bIc2s.js") );
		String res = b.fire(scriptIn );
		//System.out.println(res);
		assertTrue(res,res.indexOf("\n")>0);
	}
	
	//gaemap1.js
	@Test
	public void testFire3() throws IOException, ScriptException {
		Beauty b = new Beauty();
		String scriptIn = new String ( LServlet.getResourceAsBA("gaemap1.js") );
		String res = b.fire(scriptIn );
		//System.out.println(res);
		assertTrue(res,res.indexOf("\n")>0);
	}
	

}


 