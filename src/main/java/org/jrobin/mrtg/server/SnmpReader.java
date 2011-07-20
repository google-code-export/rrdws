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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

import java.io.IOException;

class SnmpReader   {
	static final int RECONFIGURE_RETRIES = 3;

	private Device router;
	private Port link;

	private Poller comm;

	private static final Logger log = LoggerFactory.getLogger(SnmpReader.class .getName());

	SnmpReader(Device router, Port link) {
		
		link.setSampling(true);
		this.router = router;
		this.link = link;
	}

	public void run() {
		String community = null;
		String host = null;
		String ifDescr = null;
		int ifIndex = -1 ;
		
		try {
			community = router.getCommunity();
			host = router.getHost();
			comm = new Poller(host, community);
			ifIndex = link.getIfIndex();
			if(ifIndex < 0) {
				findIfIndex();
			}
			if(ifIndex >= 0) {
				ifDescr = link.getIfDescr();
				String oidTmp = comm.toNumericOID(ifDescr);
				Debug.print("Sampling: " + ifDescr + "@" + host + 	" [" + ifIndex + "]{"+oidTmp+"}"); 
				 
				String value  = null;
				if ("DEV_MOD".length()==7){
					value  = comm.getNextSNMPv2(oidTmp );
					RawSample sample = createRawSample(value);
					link.processSample(sample);
				}
				if (link.getSnmpVersion() == 1)
					value  = comm.get(ifDescr, ifIndex);
				else
					value  = comm.getSNMPv2(oidTmp );
				RawSample sample = createRawSample(value);
				link.processSample(sample);
			}
		}
		catch (IOException e) {
			Debug.print("IOException on " + getLabel() + ": " + e);
			deactivateLink(e);
		} catch (MrtgException e) {
			deactivateLink(e);			
			Debug.print("MrtgException on " + getLabel() + ": " + e);
		} catch (ArrayIndexOutOfBoundsException e) {
			deactivateLink(e);
			Debug.print("MrtgException on " + getLabel() + ": " + e);
		} catch (java.lang.IllegalArgumentException	 e) {
			deactivateLink(e);
			Debug.print("java.lang.IllegalArgumentException	" + getLabel() + ": " + e);
			
			
		} finally {
			if(comm != null) {
				comm.close();
			}
			link.setSampling(false);
		}
	}

	private void deactivateLink(Exception e) {
		String mesTmp = e.getMessage();
		String ifDescr= link.getIfDescr() ;
		String host = router.getHost();
		String theNext = null;
		try {
			link.error(e);

			if (mesTmp .indexOf( "not available for retrieval")>0){
				link.deactivate();
			} else {
				Server instanceTmp = Server.getInstance();
				if (link.getErrorCount()>5 && mesTmp .indexOf( "timed out")>0|| mesTmp.indexOf("Bad OID")>=0 || mesTmp.indexOf("No such instance")>=0){ // autodiscover	
					String numericOid = link.getIfAlias(); if (numericOid == comm.toNumericOID(numericOid)) numericOid = comm.toNumericOID(numericOid.substring(0,numericOid.lastIndexOf("/"))); 	
					theNext = comm. getNextSNMPv2(numericOid);				
					String descr = comm.getLastSymbol().getName();
					descr = descr==null?comm.getLastSymbol().getName()+"!"+link.getErrorCount()+"]":descr ;
					int samplingInterval= 60;
					boolean active = true;
					link.deactivate();
					//instanceTmp.addLink(host, ifDescr, descr, samplingInterval, active );
					
					String lastKey = null;
					int retcode = -1;
					for (String retvLTmp = comm.getNextSNMPv2( numericOid);lastKey !=""+comm.getLastOID();numericOid = ""+comm.getLastOID()){
						String iPrefixTmp = ifDescr.substring(0, ifDescr.lastIndexOf("/"));
						String ifPathTmp = iPrefixTmp+"/"+descr;
						String chkOID = comm.toNumericOID(ifPathTmp);
						numericOid = ""+comm.getLastOID();
						retcode = instanceTmp.addLink(host, ifPathTmp, 2, numericOid, samplingInterval, active );
						// skip (1)
						retvLTmp = comm.getNextSNMPv2( numericOid);
						System.out.println("chkOID:"+chkOID+"=="+retcode +"::"+retvLTmp +" ..........."+numericOid + "////"+ifPathTmp);
					}
					
				}else  if (link.getErrorCount()>1 && (mesTmp .indexOf( "timed out")>0 || mesTmp.indexOf("No such instance")>=0)){ // 2nd try via ver2
					link.setSnmpVersion(2);
					String oid = link.getIfAlias();
					theNext = comm.getNextSNMPv2(oid); //oid = 	comm.toNumericOID(oid)			
					String descr = comm.getLastSymbol().getName();
					descr = descr==null?comm.getLastSymbol().getName()+"!"+link.getErrorCount()+"]":descr ;
					int samplingInterval= 60;
					boolean active = true;
					int retcode = instanceTmp.addLink(host, ifDescr, 2, descr, samplingInterval, active );
					if (retcode == -2 ){ // already existing interface
						retcode = instanceTmp.addLink(host, ifDescr+"/"+descr, 2, oid, samplingInterval, active );
					}
					System.out.println("added??:::"+retcode);

				}else  if (link.getErrorCount()>32){
					link.deactivate();
					Debug.print(".. deactivated" + getLabel() + ": " + e);
				}
			}			
		} catch (MrtgException e1) {
			log.error("deactivateLink(Exception e)"+theNext,  e1);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			log.error("deactivateLink(IOException e)"+theNext,  e2);
		}catch (Throwable  e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			log.error("deactivateLink(IOException e)"+theNext,  e2);
		}
	}

	private void findIfIndex() throws MrtgException {
		for(int i = 0; i < RECONFIGURE_RETRIES; i++) {
			try {
				String ifDescr = link.getIfDescr();
				int ifIndex = comm.getIfIndexByIfDescr(ifDescr);
				if(ifIndex >= 0) {
					// new port number found
					String alias = comm.toNumericOID(  ifDescr ) +"."+ ifIndex ;
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
				log.error("IOError while reconfiguring " + getLabel() + ": " , ioe);
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
