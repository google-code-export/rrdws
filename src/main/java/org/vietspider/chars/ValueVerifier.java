/***************************************************************************
 * Copyright 2004-2006 The VietSpider Studio All rights reserved.  *
 **************************************************************************/
package  org.vietspider.chars;

import java.net.URL;

import org.vietspider.html.HTMLNode;

/**
 * Author : Thuannd
 *         nhudinhthuan@yahoo.com
 * Apr 21, 2006
 */
public interface ValueVerifier {
  //public boolean verify(String text);
 
public boolean verify(HTMLNode node, String nodeName, String attrName);

public  String eval(HTMLNode node, String nodeName, String attrName);

public void modi(HTMLNode node, String nodeName, String attrName, String newValue);
}
