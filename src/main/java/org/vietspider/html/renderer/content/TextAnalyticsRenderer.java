/***************************************************************************
 * Copyright 2001-2008 The VietSpider         All rights reserved.  		 *
 **************************************************************************/
package org.vietspider.html.renderer.content;

import java.util.List;

import org.vietspider.common.text.TextCounter;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.Name;

/** 
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Sep 30, 2008  
 */
public final class TextAnalyticsRenderer {

  private String text;
  
  private int word = 0;
  private int paragraph = 0;
  private int sentence = 0;
  
  public TextAnalyticsRenderer (HTMLNode root) {
    StringBuilder builder = new StringBuilder();
    build(builder, root);
    text = builder.toString();
    
    processTextValue();
  }

  public void build(StringBuilder builder, HTMLNode node) {
    switch (node.getName()) {
    case CONTENT:
      char [] chars = node.getValue();
      if(!isEmpty(chars)) {
        for(int k = 0; k < chars.length; k++) {
          builder.append(chars[k] == '\n' ? ' ' : chars[k]);
        }
        HTMLNode parent = node.getParent();
        if(parent != null && parent.isNode(Name.SPAN)) builder.append(' ');
      } 
      break;
    case IMG:
    case OBJECT:
    case H1:
    case H2:
    case H3:
    case H4:
    case H5:
    case H6:
    case BR:
    case P:
    case LI:   
      if(!isEndWithNewLine(builder)) {
        builder.append('\n');
      }
      break;
    case TR:
    case TABLE:
    case TD:
    case DIV:
      if(!isEndWithNewLine(builder)) {
        builder.append('\n');
      }
      break;
    case SCRIPT:
    case STYLE:
      return;
    default:
      if(builder.length() > 0) {
        char c = builder.charAt(builder.length()-1);
        if(!(Character.isWhitespace(c) 
            || Character.isSpaceChar(c))) {
          builder.append(' ');
        }
      }
    break;
    }
    List<HTMLNode> children = node.getChildren();
    if(children == null) return;
    for(int i = 0; i < children.size(); i++) {
      build(builder, children.get(i));
    }
  }

  public String getTextValue() { return text; }

  private final boolean isEndWithNewLine(StringBuilder value) {
    int i = value.length()-1;
    while(i > -1) {
      char c = value.charAt(i);
      if(c == '\n') {
        return true;
      } else if(Character.isWhitespace(c) 
          || Character.isSpaceChar(c)) {
        i--;
        continue;
      } 
      return false;
    }
    return true;
  }

  private final boolean isEmpty(char [] chars) {
    int i = 0;
    while(i < chars.length) {
      if(Character.isWhitespace(chars[i]) 
          || Character.isSpaceChar(chars[i])) {
        i++;
        continue;
      }
      return false;
    }
    return true;
  }

  private void processTextValue() {
    String [] elements = text.split("\n");
    TextCounter textCounter = new TextCounter();
    for(int i = 0; i < elements.length; i++) {
      int value  = textCounter.countSentence(elements[i]);
      if(value > 1) {
//        System.out.println(" : == =======================================");
//        System.out.println("papa a "+ elements[i]);
        paragraph++;
      }
      sentence += value;
      
      word += textCounter.countWords(elements[i]);
    }
  }

  public int getWord() { return word; }

  public int getParagraph() { return paragraph; }

  public int getSentence() { return sentence; }
  
  public int compare(TextAnalyticsRenderer renderer) {
    int compare = paragraph - renderer.getParagraph();
    if(compare != 0) return compare;
    compare = sentence - renderer.getSentence();
    if(compare != 0) return compare;
    return word - renderer.getWord();
  }
  
}
