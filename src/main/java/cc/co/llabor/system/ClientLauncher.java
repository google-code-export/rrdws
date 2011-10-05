package cc.co.llabor.system;

import org.collectd.protocol.Network;



/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  04.04.2011::10:43:09<br> 
 */
public class ClientLauncher implements Runnable {

	@Override
	public void run(){
		try {
		//java.lang.instrument.Instrumentation instr = null;
		////java -javaagent:collectd.jar="udp://localhost#javalang" -jar sigar.jar
		////java -javaagent:collectd.jar="udp://localhost#javalang" -jar sigar.jar					
			String args = "udp://239.192.74.66:25826#rrd#javalang#tomcat";
			try{ // in case, when it is started from Servlet-JVM
				// -Djcd.dest=udp://239.192.74.66:25826 
				args = System.getProperty("jcd.dest","udp://"+Network.DEFAULT_V4_ADDR+":"+Network.DEFAULT_PORT+"")+"#rrd#javalang#tomcat";
			}catch(Exception e){e.printStackTrace();}
			org.collectd.mx.RemoteMBeanSender.premain(args , null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}


 