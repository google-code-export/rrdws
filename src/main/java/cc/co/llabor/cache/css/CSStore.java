package cc.co.llabor.cache.css; 
import java.io.IOException; 
import java.net.URL;

import javax.script.ScriptException;

import org.vietspider.html.util.HyperLinkUtil;

import ws.rrd.server.LServlet;
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
public class CSStore { 
	 
	private static final String CSSSTORE = "CSSSTORE";
	private static final CSStore me = new CSStore();
	Cache  store  = null;
	CSStore() {
		this.store = Manager.getCache(CSSSTORE);		
	}
  

	public static CSStore getInstanse() {
		return me;
	}

	public Item getByValue(String scriptValue) {
		
		return (Item) store.get(scriptValue);
	}
	public Item getByURL(String scriptURL) {
		
		String key = scriptURL;
		return (Item) store.get(key );
	}

	public Item putOrCreate(String cacheKey, String value, String refPar ) { 
		Item cssItem = (Item) store.get(cacheKey);
		if (cssItem == null){ 
			 
			String actual =   value  ;	
			actual = actual
			.replace("@import", "\n@import" )
			.replace("\";", "\";\n" )
			;
			if (actual.startsWith("<")){
				actual = actual.replace(">", ">\n");
				actual = actual.replace("<", "\n<");
			}
			String out = "";
			for (String line:actual.split("\n")){
				if (line.startsWith("@import")){
					int iStart = line.indexOf("\"");
					int iEnd = line.lastIndexOf( "\"");
					String urlTmp = line.substring(iStart+1,iEnd );
					try{
						urlTmp = HyperLinkUtil.encodeLink(new URL(refPar), urlTmp);
					}catch(Throwable e){}
					out += "\n"; 
					out += line.substring(0,iStart); 
					out += " \""; 
					out += urlTmp; 
					out += line.substring(iEnd); 
										
				}else if (line.startsWith("<")){
					// http://www.w3.org/TR/CSS21/syndata.html#comments
					out+= "/* <!-- "+line+" -->  */";
				}else{
					out+=line;
				}
			}

			cssItem = new Item(out); 
			cssItem.addReffer(refPar); 
			
		}  else{  // only for existing entries
			if (cssItem.isReadOnly()) {
				cssItem.addReffer(refPar); 
				return cssItem;
			}
			//Beauty b = new Beauty(); 
			//String actual = b.cleanCSS( value );				
			cssItem.addReffer(refPar); 
			cssItem.setValue(value); 
			reformat(cacheKey, cssItem);
			cssItem.setReadOnly(true);
			
		}
		
		synchronized (CSSSTORE) {
			Object o = store.peek(cacheKey); 
			if (cssItem != o) {// check similarity
				o = store.remove(cacheKey);
				if (1==2)System.out.println(o);
				store.put(cacheKey, cssItem );
			}
		} 
		return cssItem;
	}


	private void reformat(String cacheKey, Item cssItem) {
		String val = cssItem.getValue();
		Beauty b = new Beauty(); 
		String actual = b.cleanCSS( val );
		cssItem.setValue(actual);
	}


	 

 
	
}


 