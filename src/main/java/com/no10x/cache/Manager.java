package com.no10x.cache;

import java.util.Properties;

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
 * Creation:  26.08.2010::20:14:35<br> 
 */
public class Manager {
	
	static{
		 // try to load default cache conf
		 if (System.getProperty("com.google.appengine.runtime.version")==null){
			 //net.sf.jsr107cache.CacheFactory=ws.rrd.cache.BasicCacheFactory
			 try{
				 java.io.InputStream in = MemoryFileCache.class.getClassLoader().getResourceAsStream("jcache.properties");
				 Properties prTmp = new Properties();
				 prTmp.load(in);
				 String key = "net.sf.jsr107cache.CacheFactory";
				 String val = prTmp.getProperty(key);
				 System.setProperty(key, val);
			 }catch(Exception e){
				 e.printStackTrace();
			 } 
		 }		
	}
      	
	
	public static Cache getCache()   {
		String nsTmp = ".defaultcache";
		try{
			throw new RuntimeException("");
		}catch(Throwable  e){
			nsTmp = "Hx0"+e.getStackTrace()[2] ;//getCache
		}
		nsTmp = MemoryFileCache.class.getName();
		return getCache(nsTmp);
	}

	public static Cache getCache(String cacheNS)   { 
		
		if (cacheNS == null)return getCache("DEFAULT");
		
		CacheManager cm = CacheManager.getInstance();
		Cache retval = cm.getCache (cacheNS);
		if (retval == null)
		synchronized (CacheManager.class) { 
			if (retval == null)
			try {
				CacheFactory cacheFactory;
				cacheFactory = cm.getCacheFactory();
				Properties props = new Properties();
				props.put(NS.NAMESPACE, cacheNS );
				Cache cacheTmp;
				cacheTmp = cacheFactory.createCache(props);
				cm.registerCache(cacheNS, cacheTmp);
				retval = cacheTmp;
			} catch (CacheException e) { 
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} 
		return  retval; 
	}

}


 