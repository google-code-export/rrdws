package cc.co.llabor.cache.js;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL; 
import java.util.HashSet;
import java.util.Set;

import org.vietspider.html.util.HyperLinkUtil; 
import ws.rrd.server.LServlet; 
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  26.07.2010::11:45:51<br> 
 */
public class Item implements Serializable{ 
	/**
	 * @author vipup
	 */
	private static final long serialVersionUID = -7937242057537348262L;
	private String value;

	public Item(String value) {
		this.value = value;
	}

	public String getValue() { 
			return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	Set<String> refs = new HashSet<String>();

	public void addReffer(String refPar) {
		refs.add(refPar); 
		// TODO - search for identical Entries with diff xRef in the Cache
	}

	public Set<String> getRefs() {
 
			return refs;
	}

}


 