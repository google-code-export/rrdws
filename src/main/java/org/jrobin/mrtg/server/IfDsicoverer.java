package org.jrobin.mrtg.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.percederberg.mibble.MibValueSymbol;

import org.jrobin.mrtg.MrtgException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  22.07.2011::15:19:37<br> 
 */
class IfDsicoverer implements Runnable{
	
	

	/**
	 * @author vipup
	 * @param snmpReader
	 */
	IfDsicoverer(String hostPar, String communityPar, String numericOid,
			String ifDescr) {
		this.host = hostPar;
		this.community = communityPar;
		this.numOID = numericOid;
		this.ifDescr = ifDescr;
	}

	private String host;
	private String community;
	private String numOID;
	private String ifDescr;
	private int id;
	private String key;
	private static  Logger log = LoggerFactory.getLogger(IfDsicoverer.class .getName());

	@Override
	public void run() {
		Poller commPar;
		try {
			commPar = new Poller(host, community);
			Server instanceTmp = Server.getInstance();
			MibValueSymbol lastSymbol = commPar.getLastSymbol();
			if (lastSymbol==null){
				try{// TODO lets statrt from start ?
					commPar.getNextSNMPv2( commPar.getNumericOid( "jvmMgtMIB"));
					lastSymbol = commPar.getLastSymbol();
				}catch(Throwable e){e.printStackTrace();}
			}
			String descr = lastSymbol.getName();
			descr = descr==null?lastSymbol.getName()+"!"+"]":descr ;
			int samplingInterval= 60;
			boolean active = true;					
			String lastKey = null;
			int retcode = -1;
			for (String retvLTmp = commPar.getNextSNMPv2( numOID);lastKey !=""+commPar.getLastOID();numOID = ""+commPar.getLastOID()){
				String iPrefixTmp = ifDescr.substring(0, ifDescr.lastIndexOf("/"));
				String ifPathTmp = iPrefixTmp+"/"+descr;
				String chkOID = commPar.toNumericOID(ifPathTmp);
				numOID = ""+commPar.getLastOID();
				try{ // add only Digital-metricas
					double valTmp = Double.parseDouble( retvLTmp );
					String pathToAdd = toXName(commPar);
					
					retcode = instanceTmp.addLink(host, pathToAdd, 2, numOID, samplingInterval, active );
					
					while (retcode ==-255){// // there are ling with path BUT! diff OID
					// try to combite path+OID as a new path...
						Port pTmp = instanceTmp.getDeviceList().getRouterByHost(host).getLinkByIfDescr(pathToAdd);
						String pOIDTmp = pTmp.getIfAlias();
						// logical numOID - pOIDTmp:
						String prefixTmp = ""+commPar.getLastSymbol().getMib().getSymbolByOid(numOID).getValue();
						String suffixTmp = numOID.substring(prefixTmp.length()) ;
						pathToAdd = pathToAdd +"/["+suffixTmp+"]";
						
						retcode = instanceTmp.addLink(host, pathToAdd, 2, numOID, samplingInterval, active );
												
					}
					if (retcode ==0)
						log.debug("+++ chkOID:{}=={}::{}[5:{}]4:{}3:{}2:{}1:{} ",new Object[]{chkOID,retcode,retvLTmp, valTmp, numOID ,ifPathTmp});
					else
						log.trace( "... numOID:{} = {}" , numOID, retcode  );
					
				}catch(Throwable eDDD){
					log.trace( chkOID, eDDD );
					
				}
				// skip (1)
				retvLTmp = commPar.getNextSNMPv2( numOID);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MrtgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			discovererPool.remove(key);
			System.out.println("##"+id+"##"+key+" 8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-)))) ");
			System.out.println("##"+id+"##"+key+" 8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-))))  8-)))) ");
		}

			
	}
	private String toXName(Poller commPar) {
		MibValueSymbol lastSymbol;
		String pathToAdd = "";
		lastSymbol = commPar.getLastSymbol() ;
		for (String nextNameTmp = lastSymbol.getName();lastSymbol!=null;lastSymbol= lastSymbol.getParent()){
			nextNameTmp = lastSymbol.getName();
			pathToAdd = "/"+nextNameTmp+pathToAdd;
			
		}
		return pathToAdd;
	}
	
	
	private static int dCounter = 0;

	final static Map<String, Thread> discovererPool = new HashMap<String, Thread>();
	public static void startDiscoverer(ThreadGroup tgPar, String hostPar, String communityPar,	String numericOid, String ifDescrPar) {
		String key = hostPar +"::"+communityPar;
		synchronized (discovererPool) {
			Thread theT  =  discovererPool.get(key );
			if (theT  == null){
				IfDsicoverer newDiscoverer = new IfDsicoverer(hostPar, communityPar, numericOid, ifDescrPar);
				dCounter++;
				
				newDiscoverer.setId(dCounter);
				newDiscoverer.setKey(key);
				Thread t2Run = new Thread(tgPar, newDiscoverer, "Discoverer#"+newDiscoverer.getId()+" :"+hostPar);
				t2Run.setDaemon(false);
				t2Run.start();
			}else{
				System.out.println("DUPLICATING Discovery quering of ///////////////////////"+key+"/////////// IGNORED.");
			}
		}		
	}
	private void setKey(String key) {
		this.key = key;
	}
	private int getId() { 
		return id;
		 
	}
	private void setId(int i) {
		id = i;
	}
	
}

 