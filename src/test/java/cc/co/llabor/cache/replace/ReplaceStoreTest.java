package cc.co.llabor.cache.replace;

import java.io.IOException;
import java.util.Properties;

import ws.rrd.server.LServlet;
import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  14.09.2010::13:36:33<br> 
 */
public class ReplaceStoreTest extends TestCase {

	public void testBASIC() throws IOException {
		ReplaceStore replacer = ReplaceStore.getInstanse();
		//"e:\Programme\Apache Software Foundation\apache-tomcat-6.0.18\bin\.filecache\SCRIPTSTORE\https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js"
		//String url = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js";

		String scriptIn = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "1.html"));
		String rulesUrl = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js.properties".replace("%2F", "/");
		/**
		 * The single matching group of this regex are the digits ((?:\\d){1,3}),
		 * which correspond to group 1.
		 */
		String fLINK = "href=(?:\"|\')?Topic((?:\\d){1,3})\\.cjp(?:\"|\')?";

		/**
		 * The "$1" refers to matching group 1 of fLINK (the digits).
		 */
		String fFRAGMENT = "href=#$1";
		Properties value = new Properties();
		value.put(fLINK, fFRAGMENT);
		replacer.putOrCreate(rulesUrl, value);
		String actual = replacer.replaceByRules(rulesUrl, scriptIn);
		String expected = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "1OK.html"));		
		assertEquals(actual, expected, actual);
	}

	public void testReplace_AB() throws IOException {
		ReplaceStore replacer = ReplaceStore.getInstanse();
		replacer.setCollectParents(true);
		//"e:\Programme\Apache Software Foundation\apache-tomcat-6.0.18\bin\.filecache\SCRIPTSTORE\https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js"
		//String url = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js";
	
		String scriptIn = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		String rulesUrl = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js".replace("%2F", "/");
		String rulesUrlParent = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27".replace("%2F", "/");
	
		Properties value1 = new Properties();
		value1.put("javascript", "cobol");
		replacer.putOrCreate(rulesUrlParent, value1);
		Properties value2 = new Properties();
		value2.put("window.location.href", "xXx");
		
		replacer.putOrCreate(rulesUrl, value2); 
		String actual = replacer.replaceByRules(rulesUrl, scriptIn);
		
		String expected =  new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		expected = expected
			.replace("window.location.href", "xXx")
			.replace("javascript", "cobol");
		assertEquals(actual, expected , actual); 
	
	}

	public void testGetByURL() throws IOException {
		ReplaceStore replacer = ReplaceStore.getInstanse();
		replacer.setCollectParents(false);
		//"e:\Programme\Apache Software Foundation\apache-tomcat-6.0.18\bin\.filecache\SCRIPTSTORE\https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js"
		//String url = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js";
	
		String scriptIn = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		String rulesUrl = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js".replace("%2F", "/");
		String rulesUrlParent = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27".replace("%2F", "/");
	
		//Properties value1 = new Properties();
		//replacer.putOrCreate(rulesUrlParent, value1);
		Properties value2 = new Properties();
		value2.put("window.location.href", "xXx");
		//value2.put("xXx", "yYy");
		
		replacer.putOrCreate(rulesUrl, value2); 
		String actual = replacer.replaceByRules(rulesUrl, scriptIn);
		
		String expected =  new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		expected = expected
			.replace("window.location.href", "xXx")
			//.replace("xXx", "yYy")
			;
		assertEquals(actual, expected , actual); 
	
	}

	public void testReplace_BA() throws IOException {
		ReplaceStore replacer = ReplaceStore.getInstanse();
		replacer.setCollectParents(true);
		//"e:\Programme\Apache Software Foundation\apache-tomcat-6.0.18\bin\.filecache\SCRIPTSTORE\https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js"
		//String url = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js";
	
		String scriptIn = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		String rulesUrl = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js".replace("%2F", "/");
		String rulesUrlParent = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27".replace("%2F", "/");
	
		Properties value1 = new Properties();
		value1.put("window.location.href", "xXx");
		replacer.putOrCreate(rulesUrlParent, value1);
		Properties value2 = new Properties();
		value2.put("javascript", "cobol");
		
		replacer.putOrCreate(rulesUrl, value2); 
		String actual = replacer.replaceByRules(rulesUrl, scriptIn);
		
		String expected =  new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		expected = expected
			.replace("window.location.href", "xXx")
			.replace("javascript", "cobol");
		assertEquals(actual, expected , actual); 
	
	}

	public void testReplace_ABA() throws IOException {
		ReplaceStore replacer = ReplaceStore.getInstanse();
		replacer.setCollectParents(true);
		//"e:\Programme\Apache Software Foundation\apache-tomcat-6.0.18\bin\.filecache\SCRIPTSTORE\https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js"
		//String url = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js";
	
		String scriptIn = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		String rulesUrl = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js".replace("%2F", "/");
		String rulesUrlParent = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27".replace("%2F", "/");
	
		Properties value1 = new Properties();
		value1.put("window.location.href", "xXx");
		value1.put("javascript", "cobol");
		
		replacer.putOrCreate(rulesUrlParent, value1);
		Properties value2 = new Properties();
		value2.put("javascript", "pascal");
		
		replacer.putOrCreate(rulesUrl, value2); 
		String actual = replacer.replaceByRules(rulesUrl, scriptIn);
		
		String expected =  new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		expected = expected
			.replace("window.location.href", "xXx")
			.replace("javascript", "cobol");
		assertEquals(actual, expected , actual); 
	
	}

	public void testReplace_ABB() throws IOException {
		ReplaceStore replacer = ReplaceStore.getInstanse();
		replacer.setCollectParents(true);
		//"e:\Programme\Apache Software Foundation\apache-tomcat-6.0.18\bin\.filecache\SCRIPTSTORE\https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js"
		//String url = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js";
	
		String scriptIn = new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		String rulesUrl = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27%2F2%2FGoogle%2520Code%2520Playground-Dateien%2FgetTOC.js".replace("%2F", "/");
		String rulesUrlParent = "https%3A%2F%2Fpegasus.peras.fiducia.de%2FWebUrlaub27".replace("%2F", "/");
	
		Properties value1 = new Properties();
		value1.put("window.location.href", "xXx");
		value1.put("javascript", "pascal");
		
		replacer.putOrCreate(rulesUrlParent, value1);
		Properties value2 = new Properties();
		value2.put("javascript", "cobol");
		
		replacer.putOrCreate(rulesUrl, value2); 
		String actual = replacer.replaceByRules(rulesUrl, scriptIn);
		
		String expected =  new String(LServlet.getResourceAsBA(this.getClass()
				.getPackage().getName().replace(".", "/")
				+ "/" + "myMouseOut.js"));
		expected = expected
			.replace("window.location.href", "xXx")
			.replace("javascript", "pascal");
		assertEquals(actual, expected , actual); 
	
	}

}
