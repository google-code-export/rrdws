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
import org.jrobin.mrtg.MrtgException;

import java.io.IOException;

class SnmpReader  implements Runnable  {
	static final int RECONFIGURE_RETRIES = 3;

	private Device router;
	private Port link;

	private Poller comm;

	SnmpReader(Device router, Port link) {
		
		link.setSampling(true);
		this.router = router;
		this.link = link;
	}

	public void run() {
		try {
			String community = router.getCommunity();
			String host = router.getHost();
			comm = new Poller(host, community);
			int ifIndex = link.getIfIndex();
			if(ifIndex < 0) {
				findIfIndex();
			}
			if(ifIndex >= 0) {
				String ifDescr = link.getIfDescr();
				Debug.print("Sampling: " + ifDescr + "@" + host +
					" [" + ifIndex + "]");
 
				String value  = comm.get(ifDescr, ifIndex);
				RawSample sample = createRawSample(value);
				link.processSample(sample);
			}
		}
		catch (IOException e) {
			Debug.print("IOException on " + getLabel() + ": " + e);
		} catch (MrtgException e) {
			Debug.print("MrtgException on " + getLabel() + ": " + e);
		} catch (ArrayIndexOutOfBoundsException e) {
			Debug.print("MrtgException on " + getLabel() + ": " + e);
		} finally {
			if(comm != null) {
				comm.close();
			}
			link.setSampling(false);
		}
	}

	private void findIfIndex() throws MrtgException {
		for(int i = 0; i < RECONFIGURE_RETRIES; i++) {
			try {
				int ifIndex = comm.getIfIndexByIfDescr(link.getIfDescr());
				if(ifIndex >= 0) {
					// new port number found
					String alias = comm.get("ifAlias", ifIndex);
					link.setIfAlias(alias);
					link.switchToIfIndex(ifIndex);
					return;
				}
				else {
					// definitely no such interface
					break;
				}
			}
			catch(IOException ioe) {
				Debug.print("IOError while reconfiguring " + getLabel() + ": " + ioe);
			}
		}
		// new interface number not found after several retries
		link.deactivate();
		Debug.print("Link " + getLabel() + " not found, link deactivated");
	}

	private RawSample createRawSample(String value ) {
		RawSample sample = new RawSample();
		String host = router.getHost();
		sample.setHost(host);
		if(value != null) {
			sample.setValue(value) ;
			sample.setIfDescr(link.getIfDescr());
		}
 
		return sample;
	}

	String getLabel() {
		return link.getIfDescr() + "@" + router.getHost();
	}
}
