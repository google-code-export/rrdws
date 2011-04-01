 
package cc.co.llabor.system;   

import java.io.IOException; 
import java.lang.instrument.Instrumentation;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;  

import org.collectd.QueueWorker;
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

	QueueWorker worker = null;
	public void init(ServletConfig config) throws ServletException{
		try {
			initShutdownHook();  
		} catch (Exception e) {
			log.error("RRD initShutdownHook : ", e);
		}		
		
		String[] arg0=new String[]{};
		// Main-Class: org.collectd.mx.MBeanReceiver
		// start collectd MBeanReceiver
		try {
			MBeanReceiver.main(arg0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// start collectd MBeanSender
		//
		//Premain-Class: org.collectd.mx.RemoteMBeanSender
		try {
			Instrumentation instr = null;
			String args = "";
			RemoteMBeanSender.premain(args , instr);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		// start collectd queue-worker
		try{
			worker = new QueueWorker();
			new Thread(worker, "rrd QueueWorker").start();
		}catch(Throwable e){
			e.printStackTrace();
		}
		
		super.init(config); 
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
		// close all RRDs..
		RrdDbPool instance;
		try {
			instance = RrdDbPool.getInstance();

			for (String rrdName : instance.getOpenFiles()) {
				try {
					log.info("cleaning rrd=[{}]..",rrdName);
					RrdDb rrdDb = instance.requestRrdDb(rrdName); 
					instance.release(rrdDb);
					log.info("compleete.");
				} catch (RrdException e) {
					log.error("failed", e);
				} catch (IOException e) {
					log.error("failed", e);
				}
			}
			worker.kill();
		} catch (RrdException e1) {
			log.error("doStop() failed", e1);
		}
		log.info("Stoped");
	}
}
