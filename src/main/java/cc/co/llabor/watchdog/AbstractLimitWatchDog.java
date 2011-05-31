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
import java.util.*; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public abstract class AbstractLimitWatchDog extends TimerTask {
	protected static final Logger log = LoggerFactory.getLogger(AbstractLimitWatchDog.class.getName());	
	
	
	protected int callCount = 0;
	protected int oumErrorCount = 0;
	protected int errorCount = 0;
	protected int limitCount = 0;
	protected int maxLimitCount = 10;
	protected long lastLimitTimestamp = System.currentTimeMillis();

	protected String WARN_MESSAGE = "ATTENSION! Limit is reached"; 



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
		long currentTmp = getCurrent() ;
		if (currentTmp <= lowLimitTmp) {
			long currentTimeMillis = System.currentTimeMillis();
			if ((currentTimeMillis - lastLimitTimestamp ) > (65*1000) )// (65*1000) ) // 1 min and 5 sec 
			{ //reset counter 
				limitCount = 0;
			}else{ 
				limitCount ++; 
				doWarning(lowLimitTmp);				
			}
			lastLimitTimestamp = currentTimeMillis;
			if (limitCount >maxLimitCount) {
				do911();
			}
		}else{
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

	protected abstract  long getCurrent() ;
	protected abstract  long getLowLimit() ;
	protected abstract void doDefault() ;
	protected abstract void do911() ;
	protected abstract void doWarning(long lowMemory) ;


	protected   void curl(String string) {
		System.out.println("CURL...");
	}

	protected long getHiLimit() {
		// TODO Auto-generated method stub
		if (1==1)throw new RuntimeException("Abstract method not implemented since 31.05.2011");
		else {
		return 0;
		} 
	}
	
}