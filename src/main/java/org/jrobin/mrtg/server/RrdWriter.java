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

import org.jrobin.core.*;
import org.jrobin.mrtg.Debug;
import org.jrobin.mrtg.MrtgConstants;
import org.jrobin.mrtg.MrtgException;

import ws.rrd.collectd.TextLineIterator;
import ws.rrd.csv.Action;
import ws.rrd.csv.RrdUpdateAction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

class RrdWriter extends Thread implements MrtgConstants {
	private RrdDefTemplate rrdDefTemplate;
	private int sampleCount, badSavesCount, goodSavesCount;
	private List queue = Collections.synchronizedList(new LinkedList());

	

	private volatile boolean active = true;

	RrdWriter() throws MrtgException {
		// get definition from template
		try {
			rrdDefTemplate = new RrdDefTemplate(new File(Config.getRrdTemplateFile()));
		} catch (IOException e) {
			throw new MrtgException(e);
		} catch (RrdException e) {
			throw new MrtgException(e);
		}
		start();
	}

    public void run() {
		Debug.print("Archiver started");
		// the code is plain ugly but it should work
		while(active) {
           	while(active && queue.size() == 0) {
			   synchronized(this) {
				   try {
					   wait();
				   } catch (InterruptedException e) {
					   Debug.print(e.toString());
				   }
			   }
			}
			if(active && queue.size() > 0) {
				RawSample rawSample = (RawSample) queue.remove(0);
				try {
					process(rawSample);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Debug.print("Archiver ended");
	}

	void terminate() {
		active = false;
		synchronized(this) {
			notify();
		}
	}
 
	private void process(RawSample rawSample) throws IOException {  			
		Action a = new RrdUpdateAction();
		String ifDescr = rawSample.getIfDescr();
		long string =  System.currentTimeMillis();
		String value = rawSample.getValue();
		a.perform(  ifDescr , string , value );
	}

	private String getRrdFilenameFor(RawSample rawSample) {
		String host = rawSample.getHost();
		String ifDescr = rawSample.getIfDescr();
		return getRrdFilename(host, ifDescr);
	}

	static String getRrdFilename(String host, String ifDescr) {
		String filename = ifDescr.replaceAll("[^0-9a-zA-Z]", "_") +
			"@" + host.replaceFirst(":", "_") + ".rrd";
		String rrdDir = Config.getRrdDir();
		return rrdDir + filename;
	}

	synchronized void store(RawSample sample) {
		queue.add(sample);
		sampleCount++;
		notify();
	}

 

	int getSampleCount() {
		return sampleCount;
	}

	int getBadSavesCount() {
		return badSavesCount;
	}

	int getGoodSavesCount() {
		return goodSavesCount;
	}

	int getSavesCount() {
		return getGoodSavesCount() + getBadSavesCount();
	}

}
