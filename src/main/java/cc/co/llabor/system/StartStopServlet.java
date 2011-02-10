 
package cc.co.llabor.system;   

import java.io.IOException; 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;  
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

	public void init(ServletConfig config) throws ServletException{
		try {
			initShutdownHook();  
		} catch (Exception e) {
			log.error("RRD initShutdownHook : ", e);
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
		} catch (RrdException e1) {
			log.error("doStop() failed", e1);
		}
		log.info("Stoped");
	}
}
