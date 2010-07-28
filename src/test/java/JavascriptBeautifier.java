import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	
	public void testCMD(){
		String path = this.getClass().getResource("").getPath();
		path  = path.substring(0,path .length()-1);
		System.setProperty("user.dir", path);
		System.setProperty("user.home", path);
		System.out.println((new File(".").getAbsolutePath()));
		path = this.getClass().getResource("beautify-cl.js").getPath();
		String[] args = new String[]{path, "-i", "1", "http://www.google-analytics.com/ga.js"};
		Main.main(args ); 
	}
	
	public void test1(){
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

	public void testIS() throws IOException{
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


 