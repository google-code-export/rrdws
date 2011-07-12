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

import snmp.*;
import uk.co.westhawk.examplev2c.Util;
import uk.co.westhawk.snmp.stack.AsnObject;
import uk.co.westhawk.snmp.stack.GetBulkPdu;
import uk.co.westhawk.snmp.stack.SnmpContextBasisFace;
import uk.co.westhawk.snmp.stack.SnmpContextv2c;
import uk.co.westhawk.snmp.stack.varbind;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.*;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibTypeSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;

class Poller implements Observer {
	static final int SNMP_TIMEOUT = 60; // seconds

	static final String[][] OIDS = {
			// OID .1.3.6.1.4.1.42.2.145.3.163.1 @
			// http://download.oracle.com/javase/1.5.0/docs/guide/management/SNMP.html

			{"jvmMgtMIB", "1.3.6.1.4.1.42.2.145.3.163.1"},

			{"sysDescr", "1.3.6.1.2.1.1.1.0"},
			{"sysName", "1.3.6.1.2.1.1.5.0"},
			{"ifDescr", "1.3.6.1.2.1.2.2.1.2"},
			{"ifType", "1.3.6.1.2.1.2.2.1.3"},
			{"ifSpeed", "1.3.6.1.2.1.2.2.1.5"},
			{"sysUpTime", "1.3.6.1.2.1.1.3.0"},
			{"ifOperStatus", "1.3.6.1.2.1.2.2.1.8"},
			{"ifInOctets", "1.3.6.1.2.1.2.2.1.10"},
			{"ifOutOctets", "1.3.6.1.2.1.2.2.1.16"},
			{"ifInErrors", "1.3.6.1.2.1.2.2.1.14"},
			{"ifOutErrors", "1.3.6.1.2.1.2.2.1.20"},
			{"ifInDiscards", "1.3.6.1.2.1.2.2.1.13"},
			{"ifOutDiscards", "1.3.6.1.2.1.2.2.1.19"},
			{"ifAlias", "1.3.6.1.2.1.31.1.1.1.18"}

	};

	// state variables
	private SNMPv1CommunicationInterface comm;

	private SNMPObject lastOID;

	// SNMP v.2 vars
	private String bindAddr = "0.0.0.0";
	private SnmpContextv2c context;
	private String socketType = "Standard";
	private String host = "127.0.0.1";
	private int port = 161;
	private String community = "public";
	private int non_rep= 2;
	private int max_rep = 5;

	private Object lastValue=null;

	Poller(String hostAndPort, String communityPar) throws IOException {
		// check for port information
		String snmpHost = hostAndPort;
		int snmpPort = (int) (SNMPv1CommunicationInterface.SNMP_PORT * 100 + System
				.currentTimeMillis() % 100);
		int colonIndex = hostAndPort.indexOf(":");
		if (colonIndex != -1) {
			// port specified
			snmpHost = hostAndPort.substring(0, colonIndex);
			String portStr = hostAndPort.substring(colonIndex + 1);
			snmpPort = Integer.parseInt(portStr);
		}
		InetAddress snmpHostAddress = InetAddress.getByName(snmpHost);
		// TODO comm = new SNMPv1CommunicationInterface(0, snmpHostAddress,
		// community, snmpPort);
		comm = new SNMPv1CommunicationInterface(0, snmpHostAddress, snmpPort,
				communityPar);
		comm.setSocketTimeout(SNMP_TIMEOUT * 1000);

		// v.2 init
		// SNMP v.2 vars
		// bindAddr = snmpHostAddress;
		// socketType;
		host = snmpHost;
		port = snmpPort;
		community = communityPar;
		non_rep = 2;
		max_rep = 3;
	}

	String getNumericOid(String oid) {
		int n = OIDS.length;
		for (int i = 0; i < n; i++) {
			String name = OIDS[i][0], value = OIDS[i][1];
			if (oid.startsWith(name)) {
				return oid.replaceFirst(name, value);
			}
		}
		// probably numerical
		return oid;
	}
	/**
	 * read SNMP-value for the passes OID
	 * 
	 * @author vipup
	 * @param numericOid
	 * @return
	 * @throws IOException
	 */
	public String getSNMPv1(String numericOid) throws IOException {

		try {
			SNMPVarBindList newVars = comm.getMIBEntry(numericOid);// comm.getNextMIBEntry(
																	// numericOid);
																	// //
			SNMPSequence pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
			SNMPObject snmpObject = pair.getSNMPObjectAt(1);
			SNMPObject snmpKeyObject = pair.getSNMPObjectAt(0);
			this.setLastOID(snmpKeyObject);

			return snmpObject.toString().trim();
		} catch (SNMPBadValueException bve) {
			throw new IOException(bve);
		} catch (SNMPGetException ge) {
			throw new IOException(ge);
		}
	}

	/**
	 * SNMP v.2 via snmp4j
	 */
	/**
	 * read SNMP-value for the passes OID
	 * 
	 * @author vipup
	 * @param numericOid
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getSNMPv2(String numericOid) throws IOException {
		// AsnObject.setDebug(15);
		AsnObject.setDebug(1);

		// String host = util.getHost();
		// String bindAddr = Util.getBindAddress();
		// int port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
		// String socketType = util.getSocketType();
		// String community = util.getCommunity();

		// int non_rep = util.getIntParameter(NON, 0);
		// int max_rep = util.getIntParameter(MAX, 0);

		try {
			context = new SnmpContextv2c(host, port, bindAddr, socketType);
			context.setCommunity(community);

			GetBulkPdu pdu = new GetBulkPdu(context);
			pdu.addObserver(this);
			pdu.setNonRepeaters(non_rep);
			pdu.setMaxRepetitions(max_rep);

//			System.out.println("Setting host " + host + ", non_rep " + non_rep+ ", max_rep " + max_rep);

			//for (int i = 0; i < 10; i++) {
				// String oid = util.getProperty(Util.OID + i);
				String oid = numericOid;
				if (oid != null) {
//					System.err.println("Adding OID " + oid);
					pdu.addOid(oid);
				}
			//}

			pdu.send();
		} catch (java.io.IOException exc) {
//			System.out.println("IOException " + exc.getMessage());
			// System.exit(0);
		} catch (uk.co.westhawk.snmp.stack.PduException exc) {
//			System.out.println("PduException " + exc.getMessage());
			// System.exit(0);
		}
		String retval = null;
		for (int i = 0; i < max_rep || !context.isDestroyed(); i++) {
//			System.out.println("wait for snmp-resp..№" + i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new IOException("SNMP wait-limit reached:" + i, e);
			}
		}
		if (this.getLastOID().getValue() == numericOid){
			retval = this.lastValue.toString();
		}else{
			throw new IOException(" Error: No such name error. found :  "+this.getLastOID().getValue());
		}
		return "" + retval;

	}

	public String getNextSNMPv2(String numericOid) throws IOException {
		// AsnObject.setDebug(15);
		AsnObject.setDebug(1);

		// String host = util.getHost();
		// String bindAddr = util.getBindAddress();
		// int port = util.getPort(SnmpContextBasisFace.DEFAULT_PORT);
		// String socketType = util.getSocketType();
		// String community = util.getCommunity();
		//
		// int non_rep = util.getIntParameter(NON, 0);
		// int max_rep = util.getIntParameter(MAX, 0);

		try {
			context = new SnmpContextv2c(host, port, bindAddr, socketType);
			context.setCommunity(community);

			GetBulkPdu pdu = new GetBulkPdu(context);
			pdu.addObserver(this);
			pdu.setNonRepeaters(non_rep);
			pdu.setMaxRepetitions(max_rep);

//			System.err.println("Setting host " + host + ", non_rep " + non_rep					+ ", max_rep " + max_rep);

			//for (int i = 0; i < 10; i++) {
				// String oid = util.getProperty(Util.OID + i);
				String oid = numericOid;
				if (oid != null) {
//					System.err.println("Adding OID " + oid);
					pdu.addOid(oid);
				}
			//}

			pdu.send();
		} catch (java.io.IOException exc) {
			System.out.println("IOException " + exc.getMessage());
			// System.exit(0);
		} catch (uk.co.westhawk.snmp.stack.PduException exc) {
			System.out.println("PduException " + exc.getMessage());
			// System.exit(0);
		}

		String retval = null;
		for (int i = 0;lastValue==null && i < max_rep && !context.isDestroyed(); i++) {
//			System.out.println("wait for snmp-resp..№" + i);
			try {
				Thread.sleep(220);
			} catch (InterruptedException e) {
				//throw new IOException("SNMP wait-limit reached:" + i, e);				
			}
		}
//		if (this.getLastOID().getValue() == numericOid){
			retval = ""+this.lastValue;
// ++++++++++++++++++++++++++++++++++++   for _get_next_ it is not an error +++++++++++++++			
//		}else{
//			throw new IOException(" Error: No such name error. found :  "+this.getLastOID().toString());
//		}
		return "" + retval;

	}

	/**
	 * read next available SNMP-value for the passes OID
	 * 
	 * @author vipup
	 * @param numericOid
	 * @return
	 * @throws IOException
	 */
	public String getNextSNMPv1(String numericOid) throws IOException {

		try {
			SNMPVarBindList newVars = comm.getNextMIBEntry(numericOid); // comm.getMIBEntry(numericOid
																		// );//

			SNMPSequence pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
			SNMPObject snmpObject = pair.getSNMPObjectAt(1);
			SNMPObject snmpKeyObject = pair.getSNMPObjectAt(0);
			this.setLastOID(snmpKeyObject);
			return snmpObject.toString().trim();
		} catch (SNMPBadValueException bve) {
			throw new IOException(bve);
		} catch (SNMPGetException ge) {
			throw new IOException(ge);
		}
	}

	/**
	 * @author vipup
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	String toNumericOID(String oid) throws IOException {
		String numericOid = getNumericOid(oid);
		String oidName = "jvmClassesLoadedCount";
		try {
			checkMIB();

			int beginIndex = oid.lastIndexOf("/") + 1;
			oidName = oid.substring(beginIndex);
		} catch (Exception e) {
		}
		try {
			MibValueSymbol mibValueSymbol = ((MibValueSymbol) mib
					.getSymbol(oidName));
			MibValue mibValue = mibValueSymbol.getValue();
			numericOid = mibValue.toString();
		} catch (NullPointerException e) {
		}
		return numericOid;
	}

	String get(String oid, int index) throws IOException {
		String OID = toNumericOID(oid);
		String numericOid = OID + "." + index;
		String retval = null;
		try {
			retval = getSNMPv1(numericOid);
		} catch (IOException e) { // Workaround for SNMP v.1
			retval = getSNMPv1(OID + "." + 0);
		}
		return retval;
	}

	String[] get(String[] oids) throws IOException {
		int count = oids.length;
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = getSNMPv1(oids[i]);
		}
		return result;
	}

	static Mib mib = null;

	SortedMap walk(String base) throws IOException {
		SortedMap map = new TreeMap();
		checkMIB();
		String baseOid = getNumericOid(base);
		String currentOid = baseOid;

		try {
			Collection syms = mib.getAllSymbols();
			Collection impTmp = mib.getAllImports();
			// System.out.println(syms);
			String path = "" + base + "/";
			for (Object o : syms) { // ugly, but it works
				MibSymbol ms = (MibSymbol) o;
				// System.out.println("@@@@"+ms.getName());
				if (o instanceof MibTypeSymbol) {
					MibTypeSymbol sym = (MibTypeSymbol) o;
					// System.out.println(currentOid+" :== "+sym.getName()+":"+sym.getType().getName()+" =["+"]");

					// currentOid = sym.getName() ;
					if (currentOid.startsWith(baseOid)) {
						// store interface description
						map.put(currentOid, path);
					} else {
						// System.out.println("--"+ sym.getName());
						continue;
					}

				} else if (o instanceof MibValueSymbol) {
					MibValueSymbol sym = (MibValueSymbol) o;
					currentOid = sym.getValue().toString();
					String name = sym.getName();
					path = "" + base + "/" + name + "/";
					// System.out.println("."+name+ ":"+currentOid);
					if (currentOid.startsWith(baseOid)) {
						// extract interface number from oid
						int lastDot = currentOid.lastIndexOf(".");
						String indexStr = currentOid.substring(lastDot + 1);
						int index = Integer.parseInt(indexStr);
						// store interface description
						// map.put(new Integer(index), name);
						if (path.indexOf("jvmMgtMIB") >= 0) {
							// System.out.println("jvmClassesLoadedCount");
							String pathTmp = "/jvmMgtMIB/jvmMgtMIBObjects/jvmClassLoading/jvmClassesLoadedCount";
							String xpath = calcXPath(sym);
							// System.out.println("++:::"+currentOid+" PATH:"+xpath);
							map.put(currentOid, xpath);
						} else {
							String xpath = calcXPath(sym);
							// System.out.println("--:::"+currentOid+" PATH:"+xpath);
						}
						// System.out.println(":"+path+"index:"+index);
					} else {
						// System.out.println("!!!--"+ sym.getName());
						continue;
					}
				} else {
					// System.out.println(o );
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @author vipup
	 * @param sym
	 */
	private String calcXPath(MibValueSymbol sym) {
		String sinPath = sym.getName();

		for (MibSymbol pTmp = sym.getParent(); pTmp != null; pTmp = ((MibValueSymbol) pTmp)
				.getParent()) {
			sinPath = "/" + pTmp.getName() + "/" + sinPath;
			sinPath = sinPath.replace("//", "/");
		}
		return sinPath;
	}

	/**
	 * @author vipup
	 * @param mibloader
	 * @throws IOException
	 */
	private void checkMIB() throws IOException {
		MibLoader mibloader = new MibLoader();
		if (mib == null) {
			try {
				String mibpath = "snmp/JVM-MANAGEMENT-MIB.mib";
				ClassLoader classLoader = this.getClass().getClassLoader();
				URL fi = classLoader.getResource(mibpath);
				mibloader.addResourceDir(new File(fi.getFile()).getParent());
				InputStream resourceAsStream = classLoader
						.getResourceAsStream(mibpath);
				Reader in = new InputStreamReader(resourceAsStream);
				synchronized (MibLoader.class) {
					mib = mibloader.load(in);
				}
			} catch (MibLoaderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

				File file = new File(
						"src/resources/snmp/JVM-MANAGEMENT-MIB.mib");
				try {
					mib = mibloader.load(file);
				} catch (MibLoaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("MIB loaded:" + mib);
		}
	}

	SortedMap walkIfDescr() throws IOException {
		// Name
		// jvmMgtMIB!@#.iso.org.dod.internet.private.enterprises.sun.products.jmgt.standard.jsr163.jvmMgtMIB
		// @see
		// http://download.oracle.com/javase/1.5.0/docs/guide/management/SNMP.html
		SortedMap rawInterfacesMap = walk("jvmMgtMIB");
		SortedMap enumeratedInterfacesMap = new TreeMap();
		Collection enumeratedInterfaces = enumeratedInterfacesMap.values();
		// check for duplicate interface names
		// append integer suffix to duplicated name
		Iterator iter = rawInterfacesMap.keySet().iterator();
		while (iter.hasNext()) {
			Object ifIndex = iter.next();
			String ifDescr = (String) rawInterfacesMap.get(ifIndex);
			if (enumeratedInterfaces.contains(ifDescr)) {
				int ifDescrSuffix = 1;
				while (enumeratedInterfaces.contains(ifDescr + "#"
						+ ifDescrSuffix)) {
					ifDescrSuffix++;
				}
				ifDescr += "#" + ifDescrSuffix;
			}
			enumeratedInterfacesMap.put(ifIndex, ifDescr);//this.getNextSNMPv2(""+ifIndex)
		}
		return enumeratedInterfacesMap;
	}

	int getIfIndexByIfDescr(String ifDescr) throws IOException {
		SortedMap map = walkIfDescr();
		String OID = "null";
		try {
			OID = ((MibValueSymbol) mib.getSymbol("jvmClassesLoadedCount"))
					.getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object o = map.get(OID);
		int retval = -1;
		if (o == null)
			return retval;
		Object[] arrTmp = map.keySet().toArray();
		for (int i = 0; i < arrTmp.length; i++) {
			if (OID.equals(arrTmp[i])) {
				retval = i;
				break;
			}
		}
		return retval;
	}

	void close() {
		if (comm != null) {
			try {
				comm.closeConnection();
				comm = null;
			} catch (SocketException se) {
			}
		}
	}

	protected void finalize() {
		close();
	}

	private void setLastOID(SNMPObject lastOID) {
		this.lastOID = lastOID;
	}

	public SNMPObject getLastOID() {
		return lastOID;
	}

	public MibValueSymbol getLastSymbol() {
		return mib.getSymbolByOid("" + this.getLastOID());
	}

	@Override
	/**
	 * Implementing the Observer interface. Receiving the response from 
	 * the Pdu. 
	 */
	public void update(Observable obs, Object ov) {
		GetBulkPdu pdu = (GetBulkPdu) obs;
		if (pdu.getErrorStatus() == AsnObject.SNMP_ERR_NOERROR) {
			try {
				varbind[] vars = pdu.getResponseVarbinds();
				int sz = vars.length;
//				System.out.println("update2(): " + sz + " varbinds");
				for (int i = 0; i < sz; i++) {
					varbind var = (varbind) vars[i];
					long[]ldigs = 	var.getOid().getOid();
					int[]digs = new int[ldigs.length];
					for (int j=0;j<ldigs.length;j++ ){
						digs[j]=(int) ldigs[j];
					}
					SNMPObject oTmp = new SNMPObjectIdentifier(		digs		);
					this.setLastOID(oTmp );
					AsnObject value = var.getValue();
					this.setLastValue(var.toString() );
					System.out.println(i + " " + var.toString());
				}
			} catch (uk.co.westhawk.snmp.stack.PduException exc) {
//				System.out.println("update2(): PduException " + exc.getMessage());
			} catch (SNMPBadValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
//			System.out.println("update2(): " + pdu.getErrorStatusString());
		}
		context.destroy();
		// System.exit(0);
	}

	private void setLastValue(Object value) {
		this.lastValue = value;
	}

}
