package cc.co.llabor.threshold.nagios;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import org.apache.batik.dom.util.HashTable;

import net.sf.jsr107cache.Cache;
import cc.co.llabor.cache.Manager;
import cc.co.llabor.props.CommentedProperties;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  17.02.2012::15:47:28<br> 
 */
public abstract class Cfg implements Cloneable{
 
	abstract String getCfgName(); 
	private static int idCounter = 0;
	private int id = idCounter ++;
	private boolean inited = false;		
	public String getName() {
		String string = this.getCfgName()+"{" +this.id+"}.properties";
		//System.out.println(string);
		return string;
	}

	public synchronized void flush(CommentedProperties  theP) {
		String name = this.getName();
		Cache cache = this.getCache();
		Object put = cache.put(name, theP);
		this.inited = put !=null;
	}
	public Cfg makeEmptyClone() throws CloneNotSupportedException {
		Cfg retval = (Cfg) this.clone();
		retval .id = idCounter++;
		return retval ;
	}

	private Cache getCache() {
		String cacheName =CfgReader.class.getName();
		Cache cache = Manager.getCache(cacheName  );
		return cache;
	}
	
	CommentedProperties toProperties() { 
		Object object = this.getCache().get(getName());
		CommentedProperties retval =  (CommentedProperties) object;
		try {
			if (retval == null){
				LinkedList<Cfg> readFromCfg = CfgReader.readFromCfg(this);
				Cfg o = readFromCfg.element();
				retval = o.toProperties();
			}
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}
	
	public String get(String key) { 
		CommentedProperties properties = this.toProperties();
		String retval = properties.getProperty(key); 
		return retval ;
	}
	public String get(String key, int index) throws IOException {
		LinkedList<Cfg> readFromCfg = CfgReader.readFromCfg(this);
		Cfg cfg = readFromCfg.get(index);
		return cfg.get(key);
	}

	public boolean isInited() { 
		return inited;

	}

	public void setInited(boolean b) {
		inited = b;
	}	
	
}


 