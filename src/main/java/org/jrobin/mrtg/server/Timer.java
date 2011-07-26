/* ============================================================
 * JRobin : Pure java implementation of RRDTool's functionality
 * ============================================================
 *
 * Project Info:  http://www.jrobin.org
 * Project Lead:  Sasa Markovic (saxon@jrobin.org);
 *
 * (C) Copyright 2003, by Sasa Markovic.
 *
 * Developers:    Sasa Markovic (saxon@jrobin.org)
 *                Arne Vandamme (cobralord@jrobin.org)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jrobin.mrtg.server;

import org.jrobin.mrtg.Debug;
import org.jrobin.mrtg.MrtgConstants;
import org.jrobin.mrtg.MrtgException;

import java.util.Collection;
import java.util.Queue;
import java.util.Vector;

class Timer  implements Runnable , MrtgConstants {
	private volatile boolean active = true;
	
	static final ThreadGroup defTG = new ThreadGroup( "mrtg" );
	

	private Queue<SnmpReader> queue;

	SnmpWorker readerTpp = null;
	
	Timer() {
		this(defTG);
	}
	Timer(ThreadGroup tgPar) {
		// Init Clockwork as SNMP-initiator
		Thread thr1 = new Thread(tgPar, this, "mrtg.Timer");
		// TODO =8-0
		thr1 .start();
		// Init SNMP-queue Reader
		SnmpWorker readerTpp = new SnmpWorker();
		// Init Clockwork as SNMP-initiator
		Thread thr2 = new Thread(tgPar, readerTpp, "mrtg.Worker");
		this.queue = readerTpp.queue;
		// ready to start....
		thr2 .start();
		
	}

	public void run() {
		DeviceList deviceList;
		try {
			deviceList = Server.getInstance().getDeviceList();
		} catch (MrtgException e1) { 
			e1.printStackTrace();
			return;
		}
		Debug.print("Scheduler started");
		while(active) {
			Vector routers = deviceList.getRouters();
			for(Object routerO : routers.toArray() ) {
				Device router = (Device)routerO;
				Collection<Port> links = router.getLinks();
				for (Object linkO  :links  ) {
					Port link = (Port) linkO;
                    if(router.isActive() && link.isActive() &&
						link.isDue() && !link.isSampling()) {
                    	SnmpReader snmpReader = new SnmpReader(router, link); 
                    	this.queue.add(snmpReader); 
					}
				}
			}
			// sleep for a while
			synchronized(this) {
				try {
					wait(SCHEDULER_RESOLUTION * 1000L);
				}
				catch (InterruptedException e) {
				}
			}
		}
		Debug.print("Scheduler ended");
	}

	void terminate() {
    	active = false;
    	this.readerTpp.kill();
		synchronized(this) {
			notify();
		}
	}
}
