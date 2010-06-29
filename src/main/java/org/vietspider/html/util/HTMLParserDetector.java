/***************************************************************************
 * Copyright 2001-2009 The VietSpider         All rights reserved.  		 *
 **************************************************************************/
package org.vietspider.html.util;

import java.io.File;
import java.util.List;

import org.vietspider.chars.CharsDecoder;
import org.vietspider.chars.refs.RefsDecoder;
import org.vietspider.common.io.DataReader;
import org.vietspider.html.HTMLDocument;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.Name;
import org.vietspider.html.NodeIterator;
import org.vietspider.html.parser.EncodingDetector;
import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.parser.NodeImpl;
import org.vietspider.token.attribute.Attribute;
import org.vietspider.token.attribute.Attributes;

/** 
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Apr 27, 2009  
 */
public class HTMLParserDetector extends HTMLParser2 {
  
  private String charset = null;
  private boolean decode = false;
  
  public HTMLParserDetector() {
  }
  
  public HTMLParserDetector(String charset_) {
    this.charset = charset_;
    if(charset != null && charset.trim().length() < 1) charset = null;
  }
  
  public HTMLDocument loadDocument(File file) throws Exception {
    DataReader reader = new DataReader();
    byte [] bytes = reader.load(file);
    return charset != null ? createDocument(bytes, charset) : detectDocument(bytes);
  }  
  
  public HTMLDocument createDocument(byte [] bytes) throws Exception {
    if(charset != null) {
      char [] chars = CharsDecoder.decode(charset, bytes, 0, bytes.length);
      if(decode) chars = new RefsDecoder().decode(chars);
      return createDocument(chars);
    }
    return detectDocument(bytes);
  }
  
  private HTMLDocument detectDocument(byte [] bytes) throws Exception {
    this.charset = detectCharset(bytes);
    char [] chars = CharsDecoder.decode(charset, bytes, 0, bytes.length);
    if(decode) chars = new RefsDecoder().decode(chars);
    return createDocument(chars);
  }
  
  public String detectCharset(byte [] bytes) {
    EncodingDetector encodingDetector = new EncodingDetector();
    String codeCharset = encodingDetector.detect(bytes);
    if(codeCharset == null) codeCharset = "utf-8";
    try {
      HTMLDocument document = createDocument(bytes, codeCharset);
      String docCharset = getCharset(document);
      if(docCharset == null 
          || charset.equalsIgnoreCase(docCharset)) return codeCharset;
      return docCharset;
    } catch (Exception e) {
      return codeCharset;
    }
  }
  
  public String getCharset(HTMLDocument document) throws Exception {
    HTMLNode root = document.getRoot();
    NodeIterator iterator = root.iterator();
    while(iterator.hasNext()) {
      HTMLNode n = iterator.next();
      if(!n.isNode(Name.META)) continue;
      if(n.isNode(Name.BODY)) break;
      Attributes attributes = n.getAttributes();
      Attribute attribute = attributes.get("http-equiv");
      if(attribute == null || attribute.getValue() == null) continue;

      if(!"content-type".equalsIgnoreCase(attribute.getValue().trim())) continue ;

      attribute = attributes.get("content");
      if(attribute == null) continue;
      String link = attribute.getValue();
      if(link == null) continue;
      int index = link.toLowerCase().indexOf("=");

      return link.substring(index+1);
    }
    return null;
  }
  
  public List<NodeImpl> createTokens(byte [] bytes) throws Exception {
    if(charset != null) {
      char [] chars = CharsDecoder.decode(charset, bytes, 0, bytes.length);
      if(decode) chars = new RefsDecoder().decode(chars);
      return createTokens(chars);
    }
    this.charset = detectCharset(bytes);
    char [] chars = CharsDecoder.decode(charset, bytes, 0, bytes.length);
    if(decode) chars = new RefsDecoder().decode(chars);
    return createTokens(chars);
  }

  public String getCharset() { return charset;  }
  public void setCharset(String charset) { this.charset = charset; }

  public boolean isDecode() { return decode; }
  public void setDecode(boolean decode) { this.decode = decode; }
  
}
