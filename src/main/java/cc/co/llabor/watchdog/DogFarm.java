package cc.co.llabor.watchdog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer; 
import java.util.logging.Logger;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  30.05.2011::13:05:22<br> 
 */
public class DogFarm {
	protected static Timer timer = new Timer(DogFarm.class.getName()+":Timer");

	public static AbstractLimitWatchDog startTimer(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Class  clazzTmp = Class.forName(className);
		return startTimer(clazzTmp);
	}
	public static AbstractLimitWatchDog startTimer(Class class1) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException { 
		int seconds = 60; 
		long mssTmp = (seconds * 1000); 
		Class parameterTypes[] = new Class[]{}; 
		// TODO store to store
		Constructor constructor = class1.getConstructor(parameterTypes );  
		AbstractLimitWatchDog o = (AbstractLimitWatchDog) constructor.newInstance(parameterTypes ); //Object a = new LowLimitWatchDog();
		timer.scheduleAtFixedRate(o, 1, mssTmp); 
		log.info( "----------------------++++=====   START "+class1.getClass()+" =====+++++------------------------------");
		return o;
	}

	public static void stopTimer(AbstractLimitWatchDog watchDog)   {
		log.info( "...."+watchDog.callCount+"....."+watchDog.errorCount+"....stopppppp......."+watchDog.oumErrorCount+"..........");
		// TODO remove from store
		timer.cancel();
	}

	protected static final Logger log = Logger.getLogger(AbstractLimitWatchDog.class.getName());
}


 