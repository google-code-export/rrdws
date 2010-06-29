package ws.rrd.csv;

import java.io.Serializable;
import java.util.Collections; 
import java.util.Map;
import java.util.TreeMap;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.04.2010::17:28:52<br> 
 */
public class Registry implements Serializable{

	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -5268245947298702548L;
	Map<String, String> db2path = new TreeMap<String, String>();
	Map<String, String> path2db = new TreeMap<String, String>();
	
	public Registry(Map<String, String> db2path2) {
		this.db2path.putAll(db2path2);
		for (String key :this.db2path.keySet()){
			String value = this.db2path.get( key);
			this.path2db.put(value ,key );
		}
	}

	@SuppressWarnings("unchecked")
	public Registry() {
		this(Collections.EMPTY_MAP);
	}

	public void register(String rrddb, String xpath) {
		db2path.put(rrddb, xpath);
		path2db.put(xpath,rrddb );
	}
	
	public void unregister(String rrddb, String xpath) {
		db2path.remove(rrddb);
		path2db.remove(xpath);
	}

	public Map<String, String> getDb2path() {
			return Collections.unmodifiableMap(db2path);			
	}

	public Map<String, String> getPath2db() {			
			return  Collections.unmodifiableMap(path2db);
	}

}


 