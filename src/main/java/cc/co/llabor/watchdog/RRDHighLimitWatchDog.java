package cc.co.llabor.watchdog;
/** 
 * <b>Description: memory using process.</b>
 * 
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.09.2010::12:06:10<br> 
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sf.jsr107cache.Cache;

import org.jrobin.core.Archive;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;

import cc.co.llabor.cache.Manager;
 
public class RRDHighLimitWatchDog extends AbstractLimitWatchDog {
	
	private String toEXEC = "notepad.exe";
	private String rrdPATH = "C0824CE0/java.lang/tc70/LOC/9090@C0824CE0-Memory/gauge/Heap_used";
	
	
	public RRDHighLimitWatchDog() {
		// db=X1840861098.rrd
		// \\FIS00Q3S\Webdienst(Standardwebsite)\Aktuelle anonyme Benutzer 	== 	X1813573884.rrd
		// C0824CE0/java.lang/Eclipse/GAllileo@C0824CE0-Memory/gauge/Heap_used 	== 	X1840861098.rrd 	
		//Cache cache = Manager.getCache(this.getClass().getName());
		//toEXEC = cache.get ("toEXEC")==null? toEXEC: ""+cache.get ("toEXEC") ;
		//rrdPATH = cache.get ("rrdPATH")==null? rrdPATH: ""+cache.get ("rrdPATH") ;
		//ResourceBundle bTmp = ResourceBundle.getBundle(this.getClass().getCanonicalName());
		try {
			
			Cache cache = Manager.getCache(this.getClass().getCanonicalName());
			
			
			Properties bTmp = (Properties)cache .get(".properties");
			if (bTmp == null){
				bTmp  = new Properties();
				String rnameTmp = this.getClass().getCanonicalName().replace(".","/")+".properties";
				InputStream in = this.getClass().getClassLoader().getResourceAsStream( rnameTmp );
				bTmp.load(in);
				cache .put(".properties",bTmp );				
			}
				
				
			toEXEC = bTmp.getProperty("toEXEC", toEXEC);
			rrdPATH = bTmp.getProperty("rrdPATH", rrdPATH);
			this.WARN_MESSAGE = bTmp.getProperty("WARN_MESSAGE", WARN_MESSAGE);
			this.errorCount = Integer.parseInt( bTmp.getProperty("errorCount", ""+errorCount) );
				
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	protected void do911() {
		log.info( "restart IIS..........................");
		try {
			Runtime.getRuntime().exec(this.toEXEC );
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	protected void doWarning(long lowMemory) {
		// 		mfree1MB();
		log.warn(this.WARN_MESSAGE +"::"+lowMemory); ;
	}
 
 
	@Override
	protected long getCurrent() {
		long retval= -1; 
		RrdDbPool instance;
		try {
			
			/**
			 * BE CAREFULL WITH reimpelmentation THIS METHOD! The risk is to look all exisitng RRD-Databases
			 * 
			 * @author vipup
			 * @param xpath
			 * @return
			 */
			//private static final String xpath2Hash(String xpath) {
				String rrddb = "X"+this.rrdPATH.hashCode()+".rrd";
			//	checkReg(rrddb, xpath);
			//	return rrddb;
			//}			
			
			instance = RrdDbPool.getInstance();
			//instance.reset();
			RrdDb db = instance .requestRrdDb(rrddb);
			long resolution = 1; // 1 sec ??
			long startTime = System.currentTimeMillis() - 65000; // 65 sec
			// Returns archive consolidation function ("AVERAGE", "MIN", "MAX" or "LAST").
			String consolFun = "AVERAGE";
			double a = db.getLastDatasourceValue("data") ;//findStartMatchArchive(consolFun, startTime, resolution);
			retval =  (long)a; 
 
		} catch (RrdException e1) {
			log.error(  "doStop() failed", e1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return retval ;
	}

	@Override
	protected long getHiLimit() {
		return 130000000 ; // 55 anon-users ;
	} 
	@Override
	protected void doDefault() {
		  System.out.println("nothong to do........");
	}
	/**
	 * this method will control limit of one variable ( current --> LowLimit )<br>
	 * -- in case when the limit is reached, the number of reminder "<b>doWarning()</b>" will be performed __<i>limitCount</i>__ times
	 * <br>
	 * -- if in the limit don't goes back after  "maxLimitCount", the <b>do911()</b> will be performed
	 *<br>
	 * -- by default, when limit *is*not*reached* the <b>doDefault()</b> will be performed
	 * <br> 
	 */
	public void run() {
		callCount++; 
		long lowLimitTmp = getLowLimit();
		long hiLimitTmp = getHiLimit();
		long currentTmp = getCurrent() ;
		// check for alert-mode
 		if (currentTmp <  lowLimitTmp || currentTmp >  hiLimitTmp) {
			long currentTimeMillis = System.currentTimeMillis();
			if ((currentTimeMillis - lastLimitTimestamp ) > ((60+5)*1000) )// (65*1000) ) // 1 min and 5 sec 
			{ //reset counter 
				limitCount = 0;
			}else{ 
				limitCount ++; 
				doWarning(limitCount);				 
			}
			lastLimitTimestamp = currentTimeMillis;
			if (limitCount >maxLimitCount) {
				do911();
			}
		}else{ // baseline is checked
			try{ 
				doDefault();  
			}catch (java.lang.OutOfMemoryError e) {
				oumErrorCount++;
				e.printStackTrace();
				do911();
			}catch (Exception e) {
				errorCount ++;
				e.printStackTrace();
			}
		}
	
	}
	@Override
	protected long getLowLimit() {
		return 0;
	}
}