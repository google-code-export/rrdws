/***************************************************************************
 * Copyright 2001-2006 VietSpider         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.vietspider.chars;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VietSpider
 * Author : Nhu Dinh Thuan
 *          thuan.nhu@exoplatform.com
 * Sep 13, 2006  
 */
public final class CharsUtil {
  
  public static int indexOf(char[] value, char [] c, int start){
    boolean is = false;
    for(int i = start; i < value.length; i++){
      is = true;
      for(int j = 0; j< c.length; j++){
        if(i+j < value.length && c[j] ==  value[i+j]) continue;
        is = false;
        break;
      }      
      if(is) return i;
    }
    return -1;
  }
  
  public static String cutAndTrim(String text) {
    char [] chars = text.toCharArray();
    return new String(cutAndTrim(chars, 0, chars.length)); 
  }
  
  public static char [] cutAndTrim(char [] data, int start, int end) {
    int s = start;
    int e = end-1;
    while(s < end){
      if(!Character.isWhitespace(data[s]) && !Character.isSpaceChar(data[s])) break;
      s++;
    }
    while(e > start){
      if(!Character.isWhitespace(data[e]) && !Character.isSpaceChar(data[e])) break;
      e--;
    }
    e++;
    if(e <= s) return new char[0];
    char [] newChar = new char[e - s];
    System.arraycopy(data, s, newChar, 0, newChar.length);
    return newChar;
  }
  
  public static char [] cutBySpace(char [] data, int start){
    int e = start;
    while(e < data.length){
      if(Character.isWhitespace(data[e])) break;
      e++;
    }   
    if(e <= start) return new char[0];
    char [] newChar = new char[e-start];   
    System.arraycopy(data, start, newChar, 0, newChar.length);
    return newChar;
  }
  
  public static int indexOfIgnoreCase(char[] value, char [] c, int start){
    boolean is = false;
    for(int i = start; i < value.length; i++){
      is = true;
      for(int j = 0; j< c.length; j++){        
        if(i+j < value.length 
           &&  Character.toLowerCase(c[j]) == Character.toLowerCase(value[i+j])) continue;
        is = false;
        break;
      }      
      if(is) return i;
    }
    return -1;
  }
  
  public static int indexOf(char[] value, char c, int start){
    for(int i = start; i < value.length; i++){
      if(c == value[i]) return i;
    }
    return -1;
  }
  
  public static int startWith(char[] value, char c){
    for(int i = 0; i < value.length; i++){
      if(Character.isSpaceChar(value[i]) || Character.isWhitespace(value[i])) continue;
      else if(c == value[i]) return i;
      else return -1;
    }
    return -1;
  }
  
  public static char[] copyOfRange(char[] original, int from, int to) {
    int newLength = to - from;
    if (newLength < 0) throw new IllegalArgumentException(from + " > " + to);
    char[] copy = new char[newLength];
    System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
    return copy;
  } 
  
  public static String[] split2Array(String value, char separator) {
    List<String> list = split2List(value, separator);
    return list.toArray(new String[list.size()]);
  }
  
  public static List<String> split2List(String value, char separator) {
    int start  = 0;
    int index  = 0;
    List<String> list = new ArrayList<String>();
    while(index < value.length()) {
      char c = value.charAt(index);
      if(c == separator) {
        list.add(value.substring(start, index));
        start = index+1;
      }
      index++;
    }
    if(start < value.length()) {
      list.add(value.substring(start, value.length()));
    }
    return list;
  }
  
  public static boolean startsWith(String text, String prefix, int toffset) {
    int po = 0;
    int pc = prefix.length();
    if(toffset < 0 || pc > text.length()) return false;
    
    char ta[] = text.toCharArray();
    int to = toffset;
    char pa[] = prefix.toCharArray();
    
    while (--pc >= 0) {
      char c1 = ta[to++];
      char c2 = pa[po++];
      if (c1 == c2 
          || Character.toLowerCase(c1) == Character.toLowerCase(c2)) continue;
      return false;
    }
    return true;
  }

//  public static void main(String args[]){ 
//    CharsUtil test = new CharsUtil();
//    String text = "TrAn thu do";
//    String prefix = "trAn ";
//    System.out.println(test.startsWith(text, prefix, 0));
//    String [] elements = test.split(value, ';');
    //  System.out.println(" === >"+ elements.length);
    //  for(String ele : elements) {
    //    System.out.println(ele);
    //  }
//  }

 /* public static void main(String args[]){
  * 
  *  String value = "Rất vui ; khi nghe; tin sẽ ;có Joomla ;plugin ;";
    String [] elements = test.split(value, ';');
    System.out.println(" === >"+ elements.length);
    for(String ele : elements) {
      System.out.println(ele);
    }
    int max = 1000000;
    
    long start  = System.currentTimeMillis();
    for(int i = 0; i < max; i++) {
      test2(value);
    }
    long end  = System.currentTimeMillis();
    long total1 = end - start;
    
    start  = System.currentTimeMillis();
    for(int i = 0; i < max; i++) {
      test1(value);
    }
    end  = System.currentTimeMillis();
    long total2 = end - start;
    
    String yahoo =" nhu dinh thuan nhu      dinh  ";
    String pattern = "dinh";
    System.out.println(indexOf(yahoo.toCharArray(), pattern.toCharArray(), 7));
    System.out.println(indexOf(yahoo.toCharArray(), 'd', 0));
    pattern = "DiNh";
    System.out.println(indexOf(yahoo.toCharArray(), pattern.toCharArray(), 7));
    System.out.println(indexOfIgnoreCase(yahoo.toCharArray(), pattern.toCharArray(), 7));
    
    char [] data = cutAndTrim(yahoo.toCharArray(), 19, 31);
    System.out.println("|"+new String(data)+"|");
    data = cutBySpace(yahoo.toCharArray(), 1);
    System.out.println("|"+new String(data)+"|");
  }*/
}
