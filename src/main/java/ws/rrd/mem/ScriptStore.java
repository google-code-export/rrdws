package ws.rrd.mem; 
import java.io.IOException; 
import javax.script.ScriptException;
import ws.rrd.server.SServlet;
import com.no10x.cache.Manager;  
import net.sf.jsr107cache.Cache; 
  
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  26.07.2010::11:42:57<br> 
 */ 
public class ScriptStore { 
	 
	private static final String SCRIPTSTORE = "SCRIPTSTORE";
	private static final ScriptStore me = new ScriptStore();
	Cache  scriptStore  = null;
	ScriptStore() {
		this.scriptStore = Manager.getCache(SCRIPTSTORE);		
	}
  

	public static ScriptStore getInstanse() {
		return me;
	}

	public ScriptItem getByValue(String scriptValue) {
		
		return (ScriptItem) scriptStore.get(scriptValue);
	}
	public ScriptItem getByURL(String scriptURL) {
		
		String key = scriptURL;
		return (ScriptItem) scriptStore.get(key );
	}

	public ScriptItem putOrCreate(String cacheKey, String value, String refPar ) { 
		ScriptItem jsItem = (ScriptItem) scriptStore.get(cacheKey);
		if (jsItem == null){ 
			jsItem = new ScriptItem(value); 
			jsItem.addReffer(refPar); 
		}  else{  // only for existing entries
			/*
			 * <script type="text/javascript">
   					mw.usability.addMessages({'vector-collapsiblenav-more':'More languages','vector-editwarning-warning':'Leaving this page may cause you to lose any changes you have made.\nIf you are logged in, you can disable this warning in the \"Editing\" section of your preferences.','vector-simplesearch-search':'Search','vector-simplesearch-containing':'containing...'});
				</script>
			 */
			if (value.trim().toLowerCase().startsWith("<script")){
				String newVal = "";
				String[] lines =value.split("\n");
				int i=0;
				for (String sTmp :lines){
					if (i ==0 || i==lines.length-1)
						newVal += "/*";
					newVal+= sTmp;
					if (i ==0 || i==lines.length-1)
						newVal += "*/";
					newVal +="\n";
					i++;
				}
				value = newVal;
			}
			jsItem.setValue(value); 
			reformat(cacheKey, jsItem);		
			jsItem.addReffer(refPar); 
		}
		
		synchronized (SCRIPTSTORE) {
			Object o = scriptStore.peek(cacheKey); 
			if (jsItem != o) {// check similarity
				o = scriptStore.remove(cacheKey);
				if (1==2)System.out.println(o);
				scriptStore.put(cacheKey, jsItem );
			}
		} 
		return jsItem;
	}


	private void reformat(String cacheKey, ScriptItem jsItem) {
		try{
			String jsValue = jsItem.getValue();
			jsValue  = SServlet. performFormatJS(cacheKey, jsValue );
			
 
			String linesTmp[] = jsValue.split("\n");
			if (linesTmp[0].toLowerCase().trim().startsWith("<script"))
			if (linesTmp[linesTmp.length-1].toLowerCase().trim().startsWith("</script")){
				linesTmp[0] = "/*" +linesTmp[0] + "*/";
				linesTmp[linesTmp.length-1] = "/*" +linesTmp[linesTmp.length-1] + "*/";
				jsValue = "";
				for (String nextLine:linesTmp)
					jsValue += nextLine+"\n";
			}
			// http://stackoverflow.com/questions/18985/javascript-beautifier
			jsItem.setValue( jsValue );
		}catch(IOException e){
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}


 