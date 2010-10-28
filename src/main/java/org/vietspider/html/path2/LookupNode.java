/***************************************************************************
 * Copyright 2001-2008 The VietSpider         All rights reserved.  		 *
 **************************************************************************/
package org.vietspider.html.path2;

import java.util.ArrayList;
import java.util.List;

import org.vietspider.html.HTMLNode;
import org.vietspider.token.attribute.Attribute;
import org.vietspider.token.attribute.Attributes;

/** 
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Nov 14, 2008  
 */
public class LookupNode {

  public HTMLNode lookupNode(HTMLNode root, NodePath nodePath) {
    List<HTMLNode> list = lookupNodes(root, nodePath);
    return list == null || list.size() < 1 ? null : list.get(0); 
  }

  public List<HTMLNode> lookupNodes(HTMLNode root, NodePath[] nodePaths) {
    List<HTMLNode> htmlValues = new ArrayList<HTMLNode>();
    for(NodePath nodePath : nodePaths) {
      List<HTMLNode> list = lookupNodes(root, nodePath);
      if(list == null || list.size() < 1) continue;
      htmlValues.addAll(list);
    }
    return htmlValues;
  }

  public List<HTMLNode> lookupNodes(HTMLNode htmlRoot, NodePath nodePath) {
    if(nodePath == null) return null;

    INode [] inodes = nodePath.getNodes();
    if(inodes.length < 1) return null;
    List<HTMLNode> htmlValues = new ArrayList<HTMLNode>();
    if(inodes[0] instanceof NodeExp) {
      lookupNodes(htmlRoot, (NodeExp)inodes[0], htmlValues);
    } else {
      htmlValues.add(lookNode(htmlRoot, (Node)inodes[0]));
    }

    for(int i = 1; i < inodes.length; i++) {
      HTMLNode [] htmlNodes = htmlValues.toArray(new HTMLNode[htmlValues.size()]);
      htmlValues.clear();

      if(inodes[i] instanceof NodeExp) {
        NodeExp nodeExp = (NodeExp)inodes[i];
        for(HTMLNode htmlNode : htmlNodes) {
          if(htmlNode == null) continue;
          lookupNodes(htmlNode, nodeExp, htmlValues);
        }
        continue;
      }

      Node node = (Node)inodes[i];
      for(HTMLNode htmlNode : htmlNodes) {
        if(htmlNode == null) continue;
        HTMLNode test = lookNode(htmlNode, node);
        if(test == null) {
          continue;
        }
        htmlValues.add(lookNode(htmlNode, node));
      }
    }
    if(htmlValues.size() < 1 ) return null;
    return htmlValues;
  }

  public List<HTMLNode> lookupNodes(HTMLNode htmlNode, NodeExp nodeExp, List<HTMLNode> htmlValues) {
    List<HTMLNode> htmlChildren = htmlNode.getChildren();
//  List<HTMLNode> htmlValues = new ArrayList<HTMLNode>();  
//    System.out.println("node expresstion "+nodeExp.toString());
//    System.out.println("attributes length "+nodeExp.getAttributes().length);
    int counter  = 0;
    NodeMatcher matcher = new NodeMatcher();
    for(int i = 0; i < htmlChildren.size(); i++) {
      if(nodeExp.getName() != htmlChildren.get(i).getName()) continue;
      if(matcher.match(nodeExp.getPattern(), counter)) { 
        Attribute [] attrs = nodeExp.getAttributes(); 
        if(attrs == null || attrs.length < 1) {
//        System.out.println(" da xay ra roi ");
          htmlValues.add(htmlChildren.get(i));
        } else {
//          System.out.println(" xay ra ");
          Attributes nodeAttributes = htmlChildren.get(i).getAttributes();
          if(matcher.contains(nodeAttributes, attrs)) htmlValues.add(htmlChildren.get(i));
        }
      }
      counter++;
    }  
    return htmlValues;
  }

  public HTMLNode lookNode(HTMLNode htmlNode, Node inode) {
    List<HTMLNode> htmlChildren = htmlNode.getChildren();

    int counter  = 0;
    for(int i = 0; i < htmlChildren.size(); i++) {
      if(inode.getName() != htmlChildren.get(i).getName()) continue;
      if(inode.getIndex() == counter) return htmlChildren.get(i);
      counter++;
    }  

    return null;
  }

  public void remove(HTMLNode root, NodePath ... nodePaths){
    List<HTMLNode> nodes = new ArrayList<HTMLNode>();
    for(NodePath nodePath : nodePaths) {
      List<HTMLNode> matchValues = lookupNodes(root, nodePath); 
      if(matchValues != null) nodes.addAll(matchValues);
    }

    for(HTMLNode node : nodes) {
      if(node == null) continue;
      HTMLNode parent  = node.getParent();
      if(parent == null) continue;
      parent.removeChild(node);
    }
  } 

  public void removeFrom(HTMLNode root, NodePath path){
    HTMLNode element = lookupNode(root, path);
    if (element == null) return;
    java.util.Iterator<HTMLNode> iter =  element.getParent().getChildren().iterator();
    boolean remove = false;
    while(iter.hasNext()){
      HTMLNode ele = iter.next();
      if(!remove) remove = ele == element;
      if(remove) iter.remove();
    }
  } 
  

}
