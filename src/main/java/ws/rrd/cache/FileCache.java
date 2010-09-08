package ws.rrd.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream; 
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
 

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

	public static final String NAMESPACE = "namespace";
	private File basedir ;

	public FileCache(Map arg0) {
		this.putAll(arg0);
		String baseDirName = ".filecache";
		String namespace = toName (""+ arg0.get(NAMESPACE));
		//System.getProperty("user.home");
		basedir = new File(baseDirName+File.separator+namespace);
		if (!basedir.exists()){
			basedir.mkdir();
			System.out.println("BASEDIR [" + basedir +"] creted.");
		}
	}

	
	public void addListener(CacheListener arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	
	public void clear() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	
	public boolean containsKey(Object arg0) {
		return new File(this.basedir, ""+arg0).exists();
	}

	
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return false;
		}
	}

	
	public Set entrySet() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	
	public void evict() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	
	public Object get(Object key) {
		Object retval = null;
		try {
			File fTmp = createFile(  key);
			FileInputStream fis;
			

			fis = new FileInputStream(fTmp );
			ObjectInputStream ois = new ObjectInputStream(fis);
			retval = ois.readObject();		
			fis.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
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

	
	public Map getAll(Collection arg0) throws CacheException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	
	public CacheEntry getCacheEntry(Object arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	
	public CacheStatistics getCacheStatistics() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return false;
		}
	}

	
	public Set keySet() {
		Set<String> retval = new HashSet<String> ();
		retval .addAll( Arrays.asList( this.basedir .list() )) ;
		return retval;
	}

	
	public void load(Object arg0) throws CacheException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	
	public void loadAll(Collection arg0) throws CacheException {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	
	public Object peek(Object key) { 
		// TODO impement HIT-rate
			return this.get(key);
	}

	String toName(Object key){
		String retval = ""+key;
		String from2[][]={
				{":", ".."},
				{"\\", "=slash="},
				{"\n", "=n="},
				{"\b", "=bs="},
				{"\t", "=tab="},
				{"/", "=backslash="},
				{"\"", "=2="},
				{"\'", "=1="} 
		};
		for (String[]from2to:from2){
			retval = retval.replace(from2to[0],from2to[1]);
		}
		return retval ;
	}
	
	
	public Object put(Object key, Object arg1) {
		
		FileOutputStream fout;
		try {
			
			File fileTmp = createFile(key);
			try{
				new File(fileTmp.getParent().replace(".", File.separator)).mkdirs();
			}catch(Exception e){e.printStackTrace();}
			fout = new FileOutputStream(fileTmp );
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

	private File createFile(Object key) {
		String keyStr = toName(key);
		File retval =  new File(this.basedir , keyStr); 
		return retval;
	}

	
	public void putAll(Map arg0) {
		 for(Iterator  it = arg0.keySet().iterator();it.hasNext();){
			 Object key = it.next();
			 Object val = arg0.get(key);
			 this.put( key, val );
		 }
	}

	
	public Object remove(Object key) {
		Object retval = get(key);
		File fileTmp = createFile(key);
		if (fileTmp.exists() && retval != null){
			File dest = createFile("~"+key);
			fileTmp.renameTo(dest );
		}
		return retval;
	}

	
	public void removeListener(CacheListener arg0) {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
		}
	}

	
	public int size() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return 0;
		}
	}

	
	public Collection values() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 14.04.2010");
		else {
			return null;
		}
	}

}


 