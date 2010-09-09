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
	
	/*
	 * 
#
09-09 09:05AM 39.667 /l/aHR0cDovL2hhYnJhaGFici5ydS9jc3MvMTI4NDAzMjc0NS9hbGwuY3Nz 500 29518ms 43031cpu_ms 0kb Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.1.7) Gecko/20091221 Firefox/3.5.7,gzip(gfe)
See details

195.200.34.50 - vasiliij.pupkin [09/Sep/2010:09:06:09 -0700] "GET /l/aHR0cDovL2hhYnJhaGFici5ydS9jc3MvMTI4NDAzMjc0NS9hbGwuY3Nz HTTP/1.1" 500 217 - "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.1.7) Gecko/20091221 Firefox/3.5.7,gzip(gfe)" "rrdsaas.appspot.com" ms=29518 cpu_ms=43032 api_cpu_ms=0 cpm_usd=1.198236 exit_code=104 throttle_code=1

#
W 09-09 09:06AM 09.143

ws.rrd.server.ExceptionUtils swapFailedException: Swap 'http://habrahabr.ru/css/1284032745/all.css' failed. Exception message: 'com.google.apphosting.api.DeadlineExceededException: This request (5cf11255d57a0732) started at 2010/09/09 16:05:40.384 UTC and was still executing at 2010/09/09 16:06:09.124 UTC.
	at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:408)
	at java.lang.StringBuilder.append(StringBuilder.java:136)
	at java.lang.StringBuilder.<init>(StringBuilder.java:110)
	at cc.co.llabor.script.Beauty.cleanAsync(Beauty.java:136)
	at cc.co.llabor.script.Beauty.cleanAsync(Beauty.java:143)
	at cc.co.llabor.script.Beauty.cleanCSS(Beauty.java:166)
	at cc.co.llabor.cache.css.CSStore.putOrCreate(CSStore.java:53)
	at ws.rrd.server.LServlet.performCSS(LServlet.java:439)
	at ws.rrd.server.LServlet.doGetPost(LServlet.java:254)
	at ws.rrd.server.LServlet.doGet(LServlet.java:68)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:693)
	at javax.se	 * 
	 * 
	 */
}


 