package cc.co.llabor.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.io.PrintWriter;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import ws.rrd.server.LServlet;

/** 
 * <b>Description:TODO</b>
 * @author      gennady<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 OXSEED AG <br>
 * <b>Company:</b>       OXSEED AG  <br>
 * 
 * Creation:  Sep 8, 2010::8:11:04 PM<br> 
 */
public class Beauty {
	
	static byte[] bScript = "".getBytes();
	static String beautyScript = "";
	static {
		try {
			bScript = LServlet.getResourceAsBA("beautifyALL.js");
			beautyScript = new String(bScript);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public  String fire(String scriptIn) throws IOException, ScriptException{
			String retval = "";
	        ScriptEngineManager manager = new ScriptEngineManager();
	        ScriptEngine engine = manager.getEngineByName("JavaScript");   
	        
			engine.put("x", beautyScript); 
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        ByteArrayOutputStream berr = new ByteArrayOutputStream();
	        engine.getContext().setWriter(new PrintWriter(bout, true));
	        engine.getContext().setErrorWriter(new PrintWriter(berr,true)); 
	        retval = bout.toString();
	        engine.put("b", scriptIn  );        
	        bout.reset();
	        engine.eval("eval(x);");
	        retval = bout.toString();
	        return retval;
	}
	
	protected void _todo() throws ScriptException{
		ScriptEngineManager manager = new ScriptEngineManager();
		        
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		// Now, pass a different script context
        ScriptContext newContext = new SimpleScriptContext();
        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

        // add new variable "x" to the new engineScope        
        engineScope.put("x", "world");

        // execute the same script - but this time pass a different script context
        engine.eval("println(x);", newContext);
         
	}

}


 