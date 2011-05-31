package cc.co.llabor.watchdog;

import java.io.IOException;
 

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

public class LowLimitWatchDog extends HighLimitWatchDog { 
	public LowLimitWatchDog() {
		// Just for hide the constructor
	}	
	
	protected void doDefault() {
		//

		try {
			malloc1MB();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected long getCurrent() {
		return Runtime.getRuntime().maxMemory() - super.getLowLimit(); // 95% of total ;
	}

	@Override
	protected long getLowLimit() {
		return super.getCurrent() ;
	}

	protected void do911() {
		log.info( "curl( <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<!.LowLimitWatchDog@@@!!!.....");
		curl("");
	}

	protected void doWarning(long lowMemory) {
		// just print warning
		log.warn("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+this.WARN_MESSAGE +"::"+lowMemory);
		cleaning = false;
		System.gc();
	}
	 
}