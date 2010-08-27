package ws.rrd.mem; 
import java.util.Properties;
import java.util.TreeMap;

import com.no10x.cache.Manager;
 

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;
  
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
		
		ScriptItem scriptValue = (ScriptItem) scriptStore.get(cacheKey);
		if (scriptValue == null){
			scriptValue = new ScriptItem(value);
			System.out.println("ScriptStore:: "+cacheKey +" == "+scriptValue);
			scriptValue.addReffer(refPar);
			scriptStore.put(cacheKey, scriptValue );
		}  else{
			scriptValue.addReffer(refPar);
			scriptStore.put(cacheKey, scriptValue );			
		}
		return scriptValue;
	}
	
}


 