 
package cc.co.llabor.system;    
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;  

import org.collectd.DataWorker; 
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.jrobin.mrtg.MrtgException;
import org.jrobin.mrtg.server.IfDsicoverer;
import org.jrobin.mrtg.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   

import cc.co.llabor.features.Repo;
import cc.co.llabor.watchdog.AbstractLimitWatchDog;
import cc.co.llabor.watchdog.DogFarm;
import cc.co.llabor.watchdog.HighLimitWatchDog;
import cc.co.llabor.watchdog.LowLimitWatchDog;  
import cc.co.llabor.watchdog.RRDHighLimitWatchDog;

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
	public static boolean isGAE() {
		return !(System.getProperty("com.google.appengine.runtime.version")==null);
	}
	
	AbstractLimitWatchDog lTimer;
	AbstractLimitWatchDog hTimer;
	
	public void init(ServletConfig config) throws ServletException{
		try {
			initShutdownHook();  
		} catch (Exception e) {
			log.error("RRD initShutdownHook : ", e);
		}		
		if ( !isGAE()){
			String[] arg0=new String[]{};
			// collectd SERVER
			startCollectdServer(arg0);
			
			// collectd CLIENT
			startColelctdClient();
					
			// start collectd queue-worker
			startCollectdWorker();
		}		
		
		if (isMRTGEnabled()){
			startMrtgServer();
		}
		  
		try {
			hTimer = DogFarm.startTimer( HighLimitWatchDog.class );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lTimer = DogFarm.startTimer( LowLimitWatchDog.class );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			lTimer = DogFarm.startTimer( RRDHighLimitWatchDog.class );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
		
		super.init(config); 
	}

	private void startMrtgServer() {
		log.info(Repo.getBanner("mrtgServer"));
		 String[] acceptedClients = new String[]{};
		//jrobin/mrtg/server/Server
		try {
			Server.main(acceptedClients);
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initAutoDiscover(String hostPar, String communityPar, String numericOid, String ifDescr){
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		log.info("SNMP autodiscovery started for: host{},community{} from OID:{} || {}" ,new Object[]{hostPar,  communityPar,  numericOid,  ifDescr });
		IfDsicoverer.startDiscoverer(mythreads, hostPar, communityPar, numericOid, ifDescr);
	}
	private boolean isMRTGEnabled() {
		RuntimeMXBean RuntimemxBean = ManagementFactory.getRuntimeMXBean();
		
		// grep com.sun.management.snmp
		boolean retval = true; 
		String ifDescr ="jvmMgtMIB";
		String numericOid =".0";
		String communityPar="public";
		String hostPar="127.0.0.1:161";
		for(String arg:RuntimemxBean.getInputArguments()) {
			System.out.println(arg);
			
/*
 * 
 * -Dcom.sun.management.snmp.port=1616, 
-Dcom.sun.management.snmp.acl=false, 
-Dcom.sun.management.snmp.interface=127.0.0.1
#http://download.oracle.com/javase/1.5.0/docs/guide/management/SNMP.html#snmp_properties
com.sun.management.snmp.port=portNum
com.sun.management.snmp.acl.file=ACLFilePath
com.sun.management.snmp.trap	Remote port to which the SNMP agent sends traps. 	162
com.sun.management.snmp.interface	Optional. The local host InetAddress, to force the SNMP agent to bind to the given InetAddress. This is for multi-home hosts if one wants to listen to a specific subnet only.	N/A
com.sun.management.snmp.acl.file	Path to a valid ACL file. After the JVM has started, modifying the ACL file has no effect.	JRE_HOME/lib/management/snmp.acl
#http://download.oracle.com/javase/6/docs/technotes/guides/management/snmp.html
Property Name	
Description	
Default

com.sun.management.snmp.trap	
Remote port to which the SNMP agent sends traps.	
162

com.sun.management.snmp. interface	
Optional. The local host InetAddress, to force the SNMP agent to bind to the given InetAddress. This is for multi-home hosts if one wants to listen to a specific subnet only.	
Not applicable

com.sun.management.snmp.acl	
Enables or disables SNMP ACL checks.	
true

com.sun.management.snmp. acl.file	
Path to a valid ACL file. After the Java VM has started, modifying the ACL file has no effect.	
JRE_HOME/lib/management/snmp.acl
			
 */		
			if (arg.indexOf("com.sun.management.snmp.interface")>=0){
				hostPar = arg.substring(arg.indexOf("=")+1)+ hostPar.substring(hostPar.indexOf(":"));
			}
			if (arg.indexOf("com.sun.management.snmp.port")>=0){
				hostPar = hostPar.substring(0, hostPar.indexOf(":")+1)+arg.substring(arg.indexOf("=")+1);
			}
			if (arg.indexOf("com.sun.management.snmp")>=0){
				retval =true;
				// initiate own Autodiscover
				int val = arg.indexOf("com.sun.management.snmp"); 
			}
		}
		initAutoDiscover(hostPar, communityPar, numericOid, ifDescr);
		return retval;
		
	}

	/**
	 * @author vipup
	 */
	private void startCollectdWorker() { 
		//rrdDataWorker.dat
		log.info(Repo.getBanner( "rrdDataWorker"));
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
		log.info(Repo.getBanner( "collectServer"));
		serverLauncher = new ServerLauncher(arg0);
		Thread t1 = new Thread ( this.mythreads, serverLauncher, "jcollectd_Server");
		t1.setDaemon(true);
		t1.start();		
	}

	ClientLauncher clientLauncher;
	/**
	 * @author vipup
	 */
	private void startColelctdClient() {
		log.info(Repo.getBanner( "collectClient"));
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
		
		DogFarm.stopTimer( lTimer  ); 
		log.info(Repo.getBanner( "+lTimer"));
		DogFarm.stopTimer( hTimer  );
		log.info(Repo.getBanner( "+hTimer"));
		
		try {
			Server.getInstance().stop();
		} catch (MrtgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info(Repo.getBanner( "+rrdws"));
		log.info("Stoped");
	}
}
