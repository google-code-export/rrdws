/***************************************************************************
 * Copyright 2003-2006 by VietSpider - All rights reserved.  *
 *    *
 **************************************************************************/
package org.vietspider.html;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 *  Author : Nhu Dinh Thuan
 *          Email:nhudinhthuan@yahoo.com
 * Aug 5, 2006
 */
@SuppressWarnings("serial")
public class HTMLDocument implements Serializable {
  
  private HTMLNode root;
  
//  private CharsToken tokens;
  
  private HTMLNode doctype;
  
  private HashMap<String, Object> resources = new HashMap<String, Object>();
  
  public HTMLDocument() {
  }
  
  public HTMLDocument(HTMLNode root) {
    this.root = root;
  }
  
  public HTMLNode getDoctype() { return doctype; }
  
  public void setDoctype(HTMLNode doctype) { this.doctype = doctype;}
  
  public HTMLNode getRoot() { return root; }
  
  public void setRoot(HTMLNode root) { this.root = root; }
  
  public String getTextValue(){
    StringBuilder builder = new StringBuilder();
    List<HTMLNode> list = root.getChildren();
    for(HTMLNode ele : list){
      ele.buildValue(builder);
    }
    return builder.toString();
  }
  
  @SuppressWarnings("unchecked")
  public <T> T getResource(String key) {
    Object value = resources.get(key);
    return (T)value;
  }
  
  @SuppressWarnings("unchecked")
  public void putResource(String key, Object value) {
    resources.put(key, value);
  }

 /* public CharsToken getTokens() {
    if(tokens != null) return tokens;
    NodeImpl nodeImpl = (NodeImpl) root;
    tokens = new CharsToken(this);
    nodeImpl.buildCharTokens(tokens);
    return tokens; 
  }*/

}
