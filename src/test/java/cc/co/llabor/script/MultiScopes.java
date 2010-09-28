package cc.co.llabor.script;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.script.*;

import ws.rrd.server.LServlet;
//http://download.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html
public class MultiScopes {
    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
       
        byte[] bScript = LServlet.getResourceAsBA("beautifyALL.js");
        engine.put("x", new String(bScript));
        // print global variable "x"
        if (1==2)engine.eval("println(x);");
        
        // again with another OUT
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        engine.getContext().setWriter(new PrintWriter(bout, true));
        engine.getContext().setErrorWriter(new PrintWriter(System.err));
       
 
        if (1==2)engine.eval("println(x);");
        System.out.println(bout.toString());
        byte[] b = LServlet.getResourceAsBA("fpJ2E8bIc2s.js");
        //engine.getContext().setReader(new InputStreamReader(new ByteArrayInputStream(b) ));
       
        engine.put("b",  new String(b)  );        
        bout.reset();
        System.out.println("----------"+bout.toString());
        Object o = engine.eval("eval(x);");
        System.out.println(o);
        System.out.println(bout.toString());
                
 
       
        
//        byte[] b = LServlet.getResourceAsBA("fpJ2E8bIc2s.js");
//        engine.put("arguments", new String(b));
//        
//        engine.eval("eval(x);");
        
        
        // the above line prints "hello"
        

/*        // Now, pass a different script context
        ScriptContext newContext = new SimpleScriptContext();
        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

        // add new variable "x" to the new engineScope        
        engineScope.put("x", "world");

        // execute the same script - but this time pass a different script context
        engine.eval("println(x);", newContext);
        // the above line prints "world"
*/    }
}