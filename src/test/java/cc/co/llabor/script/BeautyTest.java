package cc.co.llabor.script;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;
import org.vietspider.html.parser.Diff;

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
	

	// /compiled.137a.344458206715449423.css
	//@ T est
	/**
	 * @deprecated
	 */
	public void _testFireCSS() throws IOException, ScriptException {
		Beauty b = new Beauty();
		String scriptIn = new String ( LServlet.getResourceAsBA(   this.getClass().getPackage().getName().replace(".", "/")+"/"+"compiled.137a.344458206715449423.css") );
		String res = b.fireCSS( scriptIn );
		//System.out.println(res);
		assertTrue(res,res.indexOf("\n")>0);
	}
	
	
	//cleanCSS
	@Test
	public void testCSS() throws IOException, ScriptException {
		Beauty b = new Beauty();
		String scriptIn = new String ( LServlet.getResourceAsBA(   this.getClass().getPackage().getName().replace(".", "/")+"/"+"compiled.137a.344458206715449423.css") );
		String actual = b.cleanCSS( scriptIn );
		//System.out.println(res);
		String expected = new String ( LServlet.getResourceAsBA(   this.getClass().getPackage().getName().replace(".", "/")+"/"+"online.css") );
		//String expected = new String ( LServlet.getResourceAsBA(   this.getClass().getPackage().getName().replace(".", "/")+"/"+"java.css") );
		expected = expected.replace("\r\n", "\n").replace("\t", " ");
		Diff diff  = new Diff();//System.out.println(textValue);
		diff.setSAME(false); 
		String[] diffTmp = diff.diff(expected.split("\n"),actual.split("\n"));//actual
		assertEquals(diffTmp.length, 13); 
	}
	
}


 