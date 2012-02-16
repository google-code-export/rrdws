package cc.co.llabor.threshold.nagios; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader; 
import java.util.Properties;

import net.sf.jsr107cache.Cache; 
import cc.co.llabor.cache.Manager;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  16.02.2012::13:16:21<br> 
 */
public abstract class CfgReader implements Cloneable {
	CfgReader theNext =null;
	private static int idCounter = 0;
	private int id = idCounter ++;	
	private BufferedReader inBuf; 
	abstract String getCfgName(); 
	
	public String get(String key) {
		return get(key, 0);
	}
	/**
	 * search the key in the sub-Cfg on position == index
	 * @author vipup
	 * @param key
	 * @param index
	 * @return
	 * @throws NullPointerException
	 */
	public String get(String key, int index) { 
		String retval = null;  
		if (index == 0){
			retval = this.toProperties().getProperty(key); 
		}else{
			retval = this.theNext.get(key, index-1);
		}
		return retval;
	}

	{
		init();
	}
	private void init(){ //init only once
		String cfgName = getCfgName(); 
 		try {
 			InputStream in = this.getClass().getClassLoader().getResourceAsStream("nagios/"+cfgName);
 			inBuf = new BufferedReader(new InputStreamReader(in)  );
			readFromCfg( );
			inBuf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Cache getCache() {
		String cacheName =CfgReader.class.getName();
		Cache cache = Manager.getCache(cacheName  );
		return cache;
	}



	private void readFromCfg() throws IOException { 
		for (String line = readln(); line != null; line = readln()) {
			// define XXX{
			if (line.startsWith("define ")) { // accumulate props for one
											  // section... 
				// TODO assert line has a CfgName after define like  services.cfg @define service{@				 
				while (!(line = readln()).equals("}")){
					String key_val []= line.replace(" ", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t\t", "\t").replace("\t", ";").split(";");
					String keyTmp = key_val [0];
					String valTmp= key_val [1];
					Properties  theP = this.toProperties();
					theP.put(keyTmp, valTmp);
					this.flush(theP);
				}
				// here try to read the rest..
				this.theNext = getClone();
				this.theNext .readFromCfg();
			} 
		}  
	}

	public synchronized void flush(Properties theP) {
		this.getCache().put(this.getName(), theP);
	}

	public String getName() {
		String string = this.getCfgName()+"{" +this.id+"}.properties";
		//System.out.println(string);
		return string;
	}

	 
	
	private Properties toProperties() { 
		Properties retval =  (Properties) this.getCache().get(getName());
		retval = retval==null?new Properties():retval;
		return retval;
	}

	private CfgReader getClone() {
		CfgReader  retval = null;
		try {
			retval =  (CfgReader) this.clone();
			this.theNext =retval ; 
			retval.theNext = null;
			idCounter ++;
			retval.id = this.id+1;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}


	int linecount = -1;
	
	/**
	 * return trimmed, uncommented, non-empty line of CFG-File
	 * @author vipup
	 * @return
	 * @throws IOException
	 */
	private String readln() throws IOException {
		String retval = inBuf.readLine();
		linecount++;
		try{
			retval = retval.trim();
			// #
			while (retval.startsWith("#")||"".equals( retval) ){
				retval = inBuf.readLine();
				retval = retval.trim();
			}
		}catch(NullPointerException e){
			//e.printStackTrace();
		}
		//System.out.println(retval);
		return retval;
	}


}


 