package org.vietspider.html; 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream; 
import java.io.PrintStream;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Main; 

/** 
 * <b>Description:TODO</b>
 * @author      gennady<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 OXSEED AG <br>
 * <b>Company:</b>       OXSEED AG  <br>
 * 
 * Creation:  Jul 28, 2010::10:35:38 PM<br> 
 */
public class JavascriptBeautifier extends TestCase{

	
	
	
	public void testEXEC() throws IOException{
		String scriptSource = readRes("/beautifyALL.js");
		scriptSource += "";
//
//		scriptSource += "//options = parse_opts('.','-i',' 1' );\n";
//		scriptSource += "var oTmp = js_beautify('http://www.google-analytics.com/ga.js');\n";
//		scriptSource += " print(js_beautify('http://www.google-analytics.com/ga.js'));";

// 		String  scriptURL =  "http://www.google-analytics.com/ga.js";
//		ContextFactory cf = ContextFactory.getGlobal();
//		Context cx = cf.enterContext(); 		
//		Scriptable scope = cx.initStandardObjects();
//		Script script = Main.loadScriptFromSource(cx, scriptSource, null , 11110, null);
//		final String scriptPath = this.getClass().getResource("/beautifyALL.js").toExternalForm();
//		String[] args = new String[]{scriptPath, "-i", "1", "http://localhost:9090/zazki/jsupload/jsupload.nocache.js"};
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		PrintStream myOut = new PrintStream(baOut,true);
		Main.setOut(myOut );
		//Main.main(args ); 	
		System.out.println(baOut.toString());
		//Object o = Main.evaluateScript(script, cx, scope);
		//System.out.println(o); 
	}
	
	/* its works locally only */
	public void _testCMD(){
		String path = this.getClass().getResource("").getPath();
		path  = path.substring(0,path .length()-1);
		System.setProperty("user.dir", path);
		System.setProperty("user.home", path);
		System.out.println((new File(".").getAbsolutePath()));
		path = this.getClass().getResource("beautify-cl.js").getPath();
		String[] args = new String[]{path, "-i", "1", "http://www.google-analytics.com/ga.js"};
		Main.main(args ); 
	}
	
	public void _test1(){
		ContextFactory cf = ContextFactory.getGlobal();
		Context cx = cf.enterContext(); 
		String scriptSource = "var a= 1; a";
		int lineno = 0;
		String securityDomain = null;
		String path = "";
		//(Context cx, String scriptSource, String path, int lineno, Object securityDomain)
		Script script = Main.loadScriptFromSource(cx, scriptSource, path , lineno, securityDomain);
		Scriptable scope = cx.initStandardObjects();
		Object o = Main.evaluateScript(script, cx, scope);
		System.out.println(o);
	}

	public void _testIS() throws IOException{
		ContextFactory cf = ContextFactory.getGlobal();
		Context cx = cf.enterContext(); 
		
		String scriptSource = " ";
		scriptSource = readRes("beautify.js");
		
		scriptSource += "var js_source_text = '" +
				//readRes("2format.js")+
				"http://www.google-analytics.com/ga.js"+
				"';";
		scriptSource += "js_beautify('http://www.google-analytics.com/ga.js');";
		int lineno = 0;
		String securityDomain = null;
		String path = this.getClass().getResource(".").getPath();
		//(Context cx, String scriptSource, String path, int lineno, Object securityDomain)
		Main.setOut(System.out);
		Main.setIn(  this.getClass().getResourceAsStream("2format.js") );
		Script script = Main.loadScriptFromSource(cx, scriptSource, path , lineno, securityDomain);
		Scriptable scope = cx.initStandardObjects();
		Object o = Main.evaluateScript(script, cx, scope);
		System.out.println(o);
	}

	private String readRes(String namePar) throws IOException {
		String scriptSource;
		InputStream in = this.getClass().getResourceAsStream(namePar);
		byte[] buf =  new byte[in.available()] ;
		in.read(buf);
		scriptSource = new String(buf);
		return scriptSource;
	}

}


 