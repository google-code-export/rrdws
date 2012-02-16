package cc.co.llabor.cache.js;

import java.io.Serializable;  
import java.util.HashSet;
import java.util.Set;
 
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
			accessCount ++;
			return value;
	}

	public void setValue(String value) {
		changed = System.currentTimeMillis();
		changeCount++;
		this.value = value;
	}
	
	volatile Set<String> refs = new HashSet<String>();
	boolean readOnly = false;

	volatile long created = System.currentTimeMillis();
	volatile long changed = System.currentTimeMillis();
	volatile long changeCount = 0;
	volatile long accessCount = 0;
	
	
	public void addReffer(String refPar) {
		changeCount++;
		refs.add(refPar);  
	}

	public Set<String> getRefs() { 
			return refs;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		if (!this.readOnly)
			this.readOnly = readOnly;
	}

	public long getCreated() {
 
			return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getChanged() {
 
			return changed;
	}

	public void setChanged(long changed) {
		this.changed = changed;
	}

	public long getChangeCount() {
 
			return changeCount;
	}

	public void setChangeCount(long changeCount) {
		this.changeCount = changeCount;
	}

	public long getAccessCount() {
 
			return accessCount;
	}

	public void setAccessCount(long accessCount) {
		this.accessCount = accessCount;
	}

}


 