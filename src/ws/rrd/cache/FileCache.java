package ws.rrd.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sun.corba.se.impl.orbutil.ObjectWriter;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheEntry;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheListener;
import net.sf.jsr107cache.CacheStatistics;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  14.04.2010::14:39:39<br> 
 */
public class FileCache implements Cache {

	public FileCache(Map arg0) {
		this.putAll(arg0);
	}

	@Override
	public void addListener(CacheListener arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	@Override
	public boolean containsKey(Object arg0) {
		return new File(""+arg0).exists();
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return false;
		}
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	@Override
	public void evict() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	@Override
	public Object get(Object arg0) {
		Object retval = null;
		try {
			String name = toName(arg0);
			FileInputStream fis;
			

			fis = new FileInputStream(name );
			ObjectInputStream ois = new ObjectInputStream(fis);
			retval = ois.readObject();		
			fis.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return retval;
		}

	}

	@Override
	public Map getAll(Collection arg0) throws CacheException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	@Override
	public CacheEntry getCacheEntry(Object arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	@Override
	public CacheStatistics getCacheStatistics() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return false;
		}
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	@Override
	public void load(Object arg0) throws CacheException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	@Override
	public void loadAll(Collection arg0) throws CacheException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	@Override
	public Object peek(Object arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	String toName(Object key){
		return (""+key).replace(":", "..");
	}
	
	@Override
	public Object put(Object key, Object arg1) {
		String keyStr = toName(key);
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(keyStr);
			ObjectOutputStream wr = new ObjectOutputStream(fout);
			wr.writeObject(arg1);
			wr.close();
			return arg1;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e;
		}
		
	}

	@Override
	public void putAll(Map arg0) {
		 for(Iterator  it = arg0.keySet().iterator();it.hasNext();){
			 Object key = it.next();
			 Object val = arg0.get(key);
			 this.put( key, val );
		 }
	}

	@Override
	public Object remove(Object arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	@Override
	public void removeListener(CacheListener arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return 0;
		}
	}

	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

}


 