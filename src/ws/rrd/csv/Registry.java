package ws.rrd.csv;

import java.io.Serializable;
import java.util.HashMap;
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
	
	public void register(String rrddb, String xpath) {
		db2path.put(rrddb, xpath);
		path2db.put(xpath,rrddb );
	}
	
	public void unregister(String rrddb, String xpath) {
		db2path.remove(rrddb);
		path2db.remove(xpath);
	}

	public Map<String, String> getDb2path() {
 
			return new HashMap<String, String>(db2path);
	}

	public Map<String, String> getPath2db() {
			TreeMap<String, String> treeMap = new TreeMap<String, String>(path2db);
			return treeMap;
	}

}


 