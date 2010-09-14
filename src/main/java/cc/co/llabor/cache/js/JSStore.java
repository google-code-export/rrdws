package cc.co.llabor.cache.js; 
import java.io.IOException; 
import javax.script.ScriptException;
import ws.rrd.server.SServlet;
import cc.co.llabor.script.Beauty;

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
public class JSStore { 
	 
	private static final String SCRIPTSTORE = "SCRIPTSTORE";
	private static final JSStore me = new JSStore();
	Cache  scriptStore  = null;
	JSStore() {
		this.scriptStore = Manager.getCache(SCRIPTSTORE);		
	}
  

	public static JSStore getInstanse() {
		return me;
	}

	public Item getByValue(String scriptValue) {
		
		return (Item) scriptStore.get(scriptValue);
	}
	public Item getByURL(String scriptURL) {
		
		String key = scriptURL;
		return (Item) scriptStore.get(key );
	}

	public Item putOrCreate(String cacheKey, String value, String refPar ) { 
		Item jsItem = (Item) scriptStore.get(cacheKey);
		if (jsItem == null){ 
			/*
			 * <script type="text/javascript">
   					mw.usability.addMessages({'vector-collapsiblenav-more':'More languages','vector-editwarning-warning':'Leaving this page may cause you to lose any changes you have made.\nIf you are logged in, you can disable this warning in the \"Editing\" section of your preferences.','vector-simplesearch-search':'Search','vector-simplesearch-containing':'containing...'});
				</script>
			 */
			value = checkHeadAndFoot(value);			
			jsItem = new Item(value); 
			jsItem.addReffer(refPar); 
		}  else{  // only for existing entries 
			jsItem.addReffer(refPar); 
			if (!jsItem.isReadOnly()){
				jsItem.setValue(value); 
				reformat(cacheKey, jsItem);						
				jsItem.setReadOnly(true);
			}
		}
		
		synchronized (SCRIPTSTORE) {
			Object o = scriptStore.peek(cacheKey); 
			if (jsItem != o) {// check similarity
				try{
					o = scriptStore.remove(cacheKey);// if (o.hashCode() == jsItem.hashCode());
				}catch(NullPointerException e){
					e.printStackTrace();
				}
				if (1==2)System.out.println(o);
				
				boolean changed = true;
				try{
					changed = !((Item)o).getValue().equals(jsItem.getValue());
				}catch(NullPointerException e){
					e.printStackTrace();
				}
				if (changed){
					scriptStore.put(cacheKey, jsItem );
				}
			}
		} 
		return jsItem;
	}


	/**
	 * @author vipup
	 * @param value
	 * @return
	 */
	private String checkHeadAndFoot(String value) {
		if (value.trim().toLowerCase().startsWith("<script")){
			String newVal = "";
			value = value.replace(">", ">\n");
			value = value.replace("<", "\n<");
			String[] lines =value.split("\n");
			int i=0;
			for (String sTmp :lines){
				String trimTmp = sTmp.toLowerCase().trim();
				boolean shouldBeWraped = trimTmp.startsWith("<") 
					&& (trimTmp.replace(" ", ""). startsWith("<script") ||
						trimTmp.replace(" ", ""). startsWith("</script")) ;
				if (shouldBeWraped)
					newVal += "/*";
				newVal+= sTmp;
				if (shouldBeWraped)
					newVal += "*/";
				newVal +="\n";
				i++;
			}
			value = newVal;
		}
		return value;
	}


	private void reformat(String cacheKey, Item jsItem) {
		try{
			if (jsItem.isReadOnly()) return;  
			String jsValue = jsItem.getValue();
			jsValue = checkHeadAndFoot(jsValue);	
			// http://stackoverflow.com/questions/18985/javascript-beautifier
			jsValue  =  performFormatJS(cacheKey, jsValue ); 
			jsItem.setValue( jsValue );
		}catch(IOException e){
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	public static String performFormatJS(String uriTmp, String scriptValue) throws IOException  {//, <-- ScriptItem scriptTmp
		Item jsTmp = getInstanse().getByURL(uriTmp);
		String formattedJS = scriptValue;
		if (jsTmp!=null && jsTmp.readOnly ) {
			// already formatted
			formattedJS = jsTmp.getValue();
		}else{
			synchronized (uriTmp) { 
				Beauty b = new Beauty();  
				try{
					formattedJS = b.fire(scriptValue);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	
		return formattedJS;
	}
	
}


 