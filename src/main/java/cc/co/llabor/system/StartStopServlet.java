 
package cc.co.llabor.system;   

import java.io.IOException; 
import java.lang.instrument.Instrumentation;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;  

import org.collectd.DataWorker;
import org.collectd.mx.MBeanReceiver;
import org.collectd.mx.RemoteMBeanSender;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   

public class StartStopServlet extends HttpServlet {
	
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -3432681267977857824L;
	private static final Logger log = LoggerFactory.getLogger(StartStopServlet.class .getName());
	private static int groupCounter = 0;
	ThreadGroup mythreads = new ThreadGroup("rrd@"+groupCounter++);
	
	ServerLauncher serverLauncher;
	 

	DataWorker worker = null;
	public void init(ServletConfig config) throws ServletException{
		try {
			initShutdownHook();  
		} catch (Exception e) {
			log.error("RRD initShutdownHook : ", e);
		}		
		
		String[] arg0=new String[]{};
		// collectd SERVER
		startCollectdServer(arg0);
		
		// collectd CLIENT
		startColelctdClient();
				
		// start collectd queue-worker
		startCollectdWorker();
		
		super.init(config); 
	}

	/**
	 * @author vipup
	 */
	private void startCollectdWorker() { 
			worker = new DataWorker(); 
			Thread t1 = new Thread(mythreads, worker, "rrd DataWorker");
			t1.setDaemon(true);
			t1.start(); 
	}

	
	/**
	 * @author vipup
	 * @param arg0
	 */
	private void startCollectdServer(final String[] arg0) {
		serverLauncher = new ServerLauncher(arg0);
		Thread t1 = new Thread ( this.mythreads, serverLauncher, "collectdServer");
		t1.setDaemon(true);
		t1.start();		
	}

	ClientLauncher clientLauncher;
	/**
	 * @author vipup
	 */
	private void startColelctdClient() {
		ClientLauncher clientLauncher = new ClientLauncher() ;
		Thread t1 = new Thread ( this.mythreads, clientLauncher, "collectdCLIENT");
		t1.setDaemon(true);
		t1.start();
	}

    /**
	 * This method seths a ShutdownHook to the system
	 *  This traps the CTRL+C or kill signal and shutdows 
	 * Correctly the system.
	 * @throws Exception
	 */ 
	 public void initShutdownHook() throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				doStop();
			}
		});
	}
     
    public void destroy() {
        doStop();
    }
    
    public void doStop() {
		log.info("Shutting down...");
		serverLauncher.destroyServer();
		// close all RRDs..
		RrdDbPool instance;
		try {
			instance = RrdDbPool.getInstance();
			instance.reset();

			worker.kill();
		} catch (RrdException e1) {
			log.error("doStop() failed", e1);
		}
		
		
		
		log.info("Stoped");
	}
}
