/*
 * jcollectd
 * Copyright (C) 2009 Hyperic, Inc.
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; only version 2 of the License is applicable.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 */

package org.collectd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Queue;
 

import ws.rrd.csv.Action;
import ws.rrd.csv.RrdUpdateAction;
import ws.rrd.csv.TextLineIterator;

import cc.co.llabor.cache.Manager;

import net.sf.jsr107cache.Cache;
 
/**
 * collectd UDP protocol receiver.
 * See collectd/src/network.c:parse_packet
 */
public class   QueueWorker implements Runnable{

    	private java.util.Queue<String> queue;
    	QueueWorker ( Queue<String> q){
    		this.queue = q;
    	}
    	public void run() {
    		while(true){
    			if (queue.isEmpty()){
    				try {
    					Thread.sleep(100);
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}else{
					try {
	    				String data = queue.peek();
						byte[] b = data.getBytes();
						InputStream in = new ByteArrayInputStream(b ); 
						processData(in );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally{
						
					}
    			}
    			Cache cache = Manager.getCache("collectd");
    			Object key ="collectd_4_RRD_data";
    			String o = ""+cache.get(key);
    			// TODO Lock + synch
    			if (o!=null){
    				o = ""+cache.remove(key);
					queue.offer(o);
    				
    			}

    		}
    	}
		private void processData(InputStream resourceAsStream) throws IOException {
            //
			TextLineIterator p = new TextLineIterator(resourceAsStream);
			// testdrive into System.out
			SimpleDateFormat SDF = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.US);				
			Action a = new RrdUpdateAction(SDF);
			p.perform(a);
		}

     
}
