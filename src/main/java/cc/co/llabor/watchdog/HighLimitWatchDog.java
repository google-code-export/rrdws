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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*; 
public class HighLimitWatchDog extends AbstractLimitWatchDog {

	protected static int mallocCount = 1;
	protected static long msPerMalloc = 0;
	
	public HighLimitWatchDog() {
		// Just for hide the constructor
	}
	protected void do911() {
		log.info( "curl( .HighLimitWatchDog.....");
		cleaning = true;
		curl("");
	}
	protected void doWarning(long lowMemory) {
		// 		mfree1MB();
		log.warn(this.WARN_MESSAGE +"::"+lowMemory);
		System.gc();
	}

	/**
	 * @author vipup
	 * @return
	 */
	public static long availMemory() {
		return Runtime.getRuntime().freeMemory() + (Runtime.getRuntime().maxMemory() -Runtime.getRuntime().totalMemory());
	}

	protected static ArrayList<Object> memPull = new ArrayList<Object>(); 
	
	/**
	 * @author vipup
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static String malloc1MB() throws IOException, ClassNotFoundException {
		long start = System.currentTimeMillis();
		byte[]buf = new  byte[1024*1024*1];
		Object o = new String(buf);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream ouout = new  ObjectOutputStream(out);
		ouout.writeObject(o);
		ouout.flush();
		byte[] byteArray = out.toByteArray();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		ObjectInputStream oin = new ObjectInputStream(byteArrayInputStream);
		Object o2 = oin.readObject(); 
		long end = System.currentTimeMillis();
		mallocCount  ++;
		memPull.add(o2);
		msPerMalloc  += end - start;
		log.warn( "allocated ::"+ msPerMalloc);
		return ""+o2;
	}
	public static void mfree1MB() {
		if (memPull.size()>0)
		try{
			memPull.remove(0);
			System.out.println("after clean////////////////////////"+memPull.size());
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public static long getMallocCount() { 
			return mallocCount;
	}
	
	public static long getMsPerMalloc() { 
		return msPerMalloc / mallocCount;
}

	@Override
	protected long getCurrent() {
		return availMemory() ;
	}

	@Override
	protected long getLowLimit() {
		return Runtime.getRuntime().maxMemory()*5/100; // 5% of total ;
	}
	boolean cleaning = false;
	@Override
	protected void doDefault() { 
		if (cleaning){
			mfree1MB();
		}
	}
}