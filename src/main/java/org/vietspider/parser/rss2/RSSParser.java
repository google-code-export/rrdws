/*
 * Copyright 2004-2006 The VietSpider        All rights reserved.
 *
 * Created on January 24, 2007, 8:58 PM
 */
package org.vietspider.parser.rss2;

import java.util.List;

import org.vietspider.chars.refs.RefsDecoder;
import org.vietspider.parser.xml.XMLDocument;
import org.vietspider.parser.xml.XMLNode;
import org.vietspider.parser.xml.XMLParser;
import org.vietspider.serialize.XML2Bean;
import org.vietspider.token.attribute.Attribute;
import org.vietspider.token.attribute.AttributeParser;
import org.vietspider.token.attribute.Attributes;

/**
 * @author nhuthuan
 * Email: nhudinhthuan@yahoo.com
 */
public class RSSParser {
  
  private final static String CHANNEL = "channel";
  private final static String ITEM = "item";
  
  private final static String FEED = "feed";
  private final static String ENTRY = "entry";
  
  private final static String ENCLOSURE = "enclosure";
  private final static String URL = "url";
  private final static String TYPE = "type";
  private final static String REF = "ref";
  
  private final static String LINK = "link";
  
  public static synchronized MetaDocument createDocument(String text, RefsDecoder refsDecoder) throws Exception {
    return createDocument(XMLParser.createDocument(text, refsDecoder));
  }
  
  public static synchronized MetaDocument createDocument(XMLDocument xmlDocument) throws Exception {
    MetaDocument metaDocument = null;
    metaDocument = createRSSDocument(xmlDocument.getRoot());
    if(metaDocument != null) return metaDocument;
    
    List<XMLNode> children  = xmlDocument.getRoot().getChildren();
    if(children == null) return null;
    for(XMLNode child : children) {
      metaDocument = createRSSDocument(child);
      if(metaDocument != null) return metaDocument;
    }
    return null;
  }
  
  private static MetaDocument createRSSDocument(XMLNode node) throws Exception {
    List<XMLNode> children  = node.getChildren();
    if(children == null) return null;
    XML2Bean toBean = XML2Bean.getInstance();
    
    List<XMLNode> itemNodes = null;
    MetaDocument document = null;
    
    for(XMLNode child : children) {
      if(child.isNode(CHANNEL)) {
        if(document == null) document = new MetaDocument();
        document.setChannel(toBean.toBean(RSSChannel.class, child));
        itemNodes = child.getChildren();
        if(itemNodes == null) return document;
        for(XMLNode itemNode : itemNodes) {
          if(!itemNode.isNode(ITEM)) continue;
          RSSItem rssItem = toBean.toBean(RSSItem.class, itemNode);
          parseRSSLink(itemNode, rssItem);
          document.addItem(rssItem);
        }
      } else if(child.isNode(FEED)) {
        if(document == null) document = new MetaDocument();
        document.setChannel(toBean.toBean(FeedItem.class, child));
        itemNodes = child.getChildren();
        if(itemNodes == null) return document;
        
        for(XMLNode itemNode : itemNodes) {
          if(!itemNode.isNode(ENTRY)) continue;
          EntryItem entryItem = toBean.toBean(EntryItem.class, itemNode);
          parseEntryLink(itemNode, entryItem);
          document.addItem(entryItem);
        }
      } else if(child.isNode(ITEM)) {
        if(document == null) document = new MetaDocument();
        RSSItem rssItem = toBean.toBean(RSSItem.class, child);
        parseRSSLink(child, rssItem);
        document.addItem(rssItem);
      }
    }
    return document;
    
  }
  
  private static void parseRSSLink(XMLNode node, IMetaItem item) throws Exception {
    List<XMLNode> children  = node.getChildren();
    if(children == null) return ;
    for(XMLNode child : children) {
      if(child.isNode(LINK) && child.getChildren().size() > 0) {
        item.addLink(new MetaLink(child.getChild(0).getTextValue()));
      } else if(child.isNode(ENCLOSURE)) {
        Attributes attributes = AttributeParser.parse(child);
        MetaLink metaLink  = new MetaLink();
        Attribute attribute = attributes.get(URL);
        if(attribute != null) metaLink.setHref(attribute.getValue());
        attribute = attributes.get(TYPE);
        if(attribute != null) metaLink.setType(attribute.getValue());
        item.addLink(metaLink);
      }
    }
  }
  
  private static void parseEntryLink(XMLNode node, IMetaItem item) throws Exception {
    List<XMLNode> children  = node.getChildren();
    if(children == null) return ;
    XML2Bean toBean = XML2Bean.getInstance();
    
    for(XMLNode child : children) {
      if(!child.isNode(LINK)) continue;
      MetaLink metaLink = toBean.toBean(MetaLink.class, child);
      if(metaLink.getHref() == null || metaLink.getHref().trim().isEmpty()) {
        Attributes attributes = AttributeParser.parse(child);
        Attribute attribute = attributes.get(REF);
        if(attribute != null) metaLink.setHref(attribute.getValue());
      }
      item.addLink(metaLink);
    }
  }
  
}