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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

class Device {
	private String host = "";
	private String community = "";
	private String descr = "";
	private boolean active = true;
	private Map<String,  Port> links = new HashMap<String, Port>();

	Device() { }

	Device(Node routerNode) {
		NodeList nodes = routerNode.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			Node firstChild = node.getFirstChild();
			String value = (firstChild != null)? firstChild.getNodeValue().trim(): null;
            if(name.equals("host")) {
				setHost(value);
			}
			else if(name.equals("community")) {
				setCommunity(value);
			}
			else if(name.equals("description")) {
				setDescr(value);
			}
			else if(name.equals("active")) {
				setActive(new Boolean(node.getFirstChild().getNodeValue().trim()).booleanValue());
			}
			else if(name.equals("interface")) {
				Port pTmp= new Port(node);
				//links.put(""+node.hashCode(),new Port(node));
				addLink(pTmp);
			}
		}
	}

	String getHost() {
		return host;
	}

	void setHost(String host) {
		if(host != null) {
			this.host = host;
		}
	}

	String getCommunity() {
		return community;
	}

	void setCommunity(String community) {
		if(community != null) {
			this.community = community;
		}
	}

	String getDescr() {
		return descr;
	}

	void setDescr(String descr) {
		if(descr != null) {
			this.descr = descr;
		}
	}

	boolean isActive() {
		return active;
	}

	boolean getActive() {
		return active;
	}

	void setActive(boolean active) {
		this.active = active;
	}

	Collection<Port> getLinks() {
		return links.values();
	}

 

	public String toString() {
		String buff = new String();
		buff += "Router: " + host + " -- " + "community=" + community + ", ";
		buff += "descr=" + descr + ", ";
		buff += "active=" + active + "\n";
		// dump links
		for(int i = 0; i < links.size(); i++) {
            Port link = (Port) links.get(i);
			buff += "  Link: " + link + "\n";
		}
		return buff;
	}

	Port getLinkByIfDescr(String ifDescr) {
		Port retval =  links.get(ifDescr); //links.get("ifDescr");	 getRouterInfo() ;System.out.println( links.keySet().contains(ifDescr)); .contains( ifDescr);	
		return retval ;
	}

	void addLink(Port link) {
		String ifDescrTmp = ""+link.getIfDescr();
		links.put(ifDescrTmp,link);
	}

	void removeLink(Port link) {
		String ifDescrTmp = ""+link.getIfDescr();
		links.remove(ifDescrTmp );
	}

	int getLinkCount() {
		return links.size();
	}

	String[] getAvailableLinks() throws IOException {
		Poller comm = null;
		try {
			comm = new Poller(host, community);
			Map links = comm.walkIfDescr();
			return (String[]) links.values().toArray( new String[0]);
		}
		finally {
			if(comm != null) {
				comm.close();
			}
		}
	}

	/**
	 * @deprecated
	 * 
	 * 
	 * @author vipup
	 * @return
	 */
	Hashtable getRouterInfo() {
		Hashtable table = new Hashtable();
		table.put("host", host);
		table.put("community", community);
		table.put("descr", descr);
		table.put("active", new Boolean(active));
		// add link info
		Vector linkData = new Vector();
		for (Port link :links.values()) {			
			linkData.add(link.getLinkInfo());
		}
		table.put("links", linkData);
		return table;
	}

	void appendXml(Element root) {
        Document doc = root.getOwnerDocument();
		Element routerElem = doc.createElement("router");
		root.appendChild(routerElem);
		Element hostElem = doc.createElement("host");
		hostElem.appendChild(doc.createTextNode(host));
		routerElem.appendChild(hostElem);
		Element commElem = doc.createElement("community");
		commElem.appendChild(doc.createTextNode(community));
		routerElem.appendChild(commElem);
		Element descrElem = doc.createElement("description");
		descrElem.appendChild(doc.createTextNode(descr));
		routerElem.appendChild(descrElem);
		Element activeElem = doc.createElement("active");
		activeElem.appendChild(doc.createTextNode("" + active));
		routerElem.appendChild(activeElem);
		for (Port link  : links.values() ) {			
			link.appendXml(routerElem);
		}
	}
}