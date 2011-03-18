/***************************************************************************
 * Copyright 2003-2006 by VietSpider - All rights reserved.  *
 *    *
 **************************************************************************/
package org.vietspider.html;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.vietspider.html.parser.HTML;
import org.vietspider.html.parser.NodeList;
import org.vietspider.token.Node;
import org.vietspider.token.attribute.AttributeParser;
import org.vietspider.token.attribute.Attributes;

/**
 *  Author : Nhu Dinh Thuan
 *          Email:nhudinhthuan@yahoo.com
 * Aug 3, 2006
 */
@SuppressWarnings("serial")
public abstract class HTMLNode implements Node<Name>, Serializable {

//  protected char [] value ;
  protected Name name;

  protected HTMLNode parent = null;  
  protected NodeList children ;
  
  protected Attributes attributes;
  
  protected volatile boolean isBeautify = false;
  
  protected HTMLNode( Name name){
//    this.value = value;
    this.name = name;
  }

  public abstract char[] getValue(); //{ return value;  }
  public abstract void setValue(char[] value); //{ this.value = value; }

  public NodeConfig getConfig() { 
    return HTML.getConfig(name); 
  }

  public boolean isNode(String nodeName){
    return name.toString().equalsIgnoreCase(nodeName);
  }
  public boolean isNode(Name n){ return name == n; } 
  public Name getName(){  return name ; }
  public void setName(Name name){  this.name = name; }

  public HTMLNode getParent() { return parent;  }
  
  @Deprecated()
  public void setParent(HTMLNode parent) { this.parent = parent; }
  
  public abstract void addChild(HTMLNode node);
  
  public abstract void addChild(int i, HTMLNode node);
  
  public abstract void setChild(int i, HTMLNode node);
  
  public HTMLNode getChild(int i) { return children.get(i); }
  
  public abstract void removeChild(HTMLNode node);
  
  public abstract void clearChildren();

  public List<HTMLNode> getChildren() { return children; }
  
  public boolean hasChildren() { return children != null ; }
  
  public int totalOfChildren() {
    return children == null ? 0 : children.size(); 
  }

  @Deprecated()
  public List<HTMLNode> getChildrenNode(){
    List<HTMLNode> list = new LinkedList<HTMLNode>();
    if(children  == null) return list;
    for(HTMLNode child : children){
      if(child.isTag()) list.add(child);
    }
    return list;
  } 
  

  /**
   * 
   * <!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">



<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
   * @author vipup
   * @return
   */
  public String asXHTML(){
	    StringBuilder builder = new StringBuilder();
	    String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	    xmlHeader += "<!DOCTYPE html \n";
	    xmlHeader += "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" ";
	    //xmlHeader += "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" "; 
	    xmlHeader += "\"http://zkoss.googlecode.com/svn/release-repository/DTD/"; 
	    xmlHeader += "xhtml1-transitional.dtd\" "; 
	    //xmlHeader += "\"target/classes/DTD/xhtml1-transitional.dtd\""; 
	    xmlHeader +=		">";	    
	    builder .append(xmlHeader);
	    builXHTML(builder);
	    String retval = builder.toString();
	    // TODO refactor 	    
	    retval = retval.replace("&nbsp;", "&#160;");
	    // TODO refactor 	    
	    retval = retval.replace("&copy;", "&#169;");
	    
	    //http://www.velocityreviews.com/forums/t118003-nowrap-not-xhtml-1-0-compliant.html
	    // TODO refactor 
	    retval = retval.replace(" nowrap>", " nowrap=\"nowrap\">");
		return retval;
  }  

  abstract public StringBuilder builXHTML(StringBuilder builder)  ;
  abstract public StringBuilder builXHTML(StringBuilder builder, int LEVEL);

public String getTextValue(){
    StringBuilder builder = new StringBuilder();
    buildValue(builder);
    return builder.toString();
  }  
  
  public int indexOfChild(HTMLNode node) {
    for(int i = 0; i < children.size(); i++) {
      if(children.get(i) == node) return i;
    }
    return -1;
  }
  
  public Attributes getAttributes() {
    if(attributes == null) {
      attributes = AttributeParser.parse(this);
    }
    return attributes;
  }
  
  abstract public StringBuilder buildValue(StringBuilder builder);
  
//  abstract public void buildTokens(List<HTMLNode> tokens) ;
  
  public String toString() { return new String(getValue());  }
  
  abstract public void clone(HTMLNode nodeParent);

  abstract public void traverse(NodeChildHandler handler);
  
  abstract public NodeIterator iterator();
  
  abstract public NodeIterator iterator(List<HTMLNode> ignores);

public boolean isBeautify() { 
		if (this.parent!=null)return parent.isBeautify();
		else return this.isBeautify;
}

public void setBeautify(boolean isBeautify) {
	if (this.parent!=null) parent.setBeautify( isBeautify );
	this.isBeautify = isBeautify;
}

/**
 * search for Node by id. First attribute __id__ with value ${id} will be returned
 * @author vipup
 * @param id
 * @return Node or null
 */
public HTMLNode getById(String id) {
	HTMLNode retval = null;
	try{
		if (this.getAttributes().get("id").getValue().equals(id)){
			retval = this;
		}
	}catch(NullPointerException e){}
	if(retval==null)
	try{
		for (HTMLNode childTmp:this.getChildren()){
			retval = childTmp.getById(id);
			if (retval!=null)break;
		}
		
	}catch(NullPointerException e){}
	return retval;
}
  
}
