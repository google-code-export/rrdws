package cc.co.llabor.cache.css;  
import java.net.URL;
 

import org.vietspider.html.util.HyperLinkUtil;
 
import cc.co.llabor.cache.Manager;
import cc.co.llabor.script.Beauty;
  
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
		this.store =  Manager.getCache(CSSSTORE);		
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
			int lnCnt = 0;
			for (String line:actual.split("\n")){
				lnCnt++;
				try{
					if (line.startsWith("@import")){
						int iStart = line.indexOf("\"");
						int iEnd = line.lastIndexOf( "\"");
						String urlTmp = line.substring(iStart+1,iEnd );
						try{
							urlTmp = HyperLinkUtil.encodeLink(new URL(refPar), urlTmp);
						}catch(Throwable e){}
						out += "\n"; 
						out += line.substring(0,iStart); 
						out += "  \""; 
						out += "  "+urlTmp; 
						out += line.substring(iEnd); 
											
					}else if (line.startsWith("<")){
						// http://www.w3.org/TR/CSS21/syndata.html#comments
						out+= "/* <!-- "+line+" -->  */";
					}else{
						out+=line;
					}
				}catch(Throwable e){
					out+="//* ln:"+lnCnt+":"+line + e.getMessage() +"*/";
				}				
				out+="\n";				
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
			try{
				reformat(cacheKey, cssItem);
				cssItem.setReadOnly(true);
			}catch(Throwable e ){} 
		} 
		synchronized (CSSSTORE) {
			Object o = store.peek(cacheKey); 
			if (cssItem != o) {// check similarity
				o = store.remove(cacheKey); 
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


 