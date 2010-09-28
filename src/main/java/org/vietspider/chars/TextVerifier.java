package org.vietspider.chars;

import org.vietspider.html.HTMLNode;
import org.vietspider.html.parser.NodeImpl;
import org.vietspider.token.TypeToken;
import org.vietspider.token.attribute.Attribute;
import org.vietspider.token.attribute.Attributes;

public abstract class TextVerifier implements ValueVerifier{
	
	



	public boolean verify(HTMLNode node, String nodeName, String attrName) {
	    if(node.isNode(nodeName) || (nodeName.length() == 1 && nodeName.charAt(0) == '*')) {
	        Attributes attrs = node.getAttributes();  
			Attribute attr = attrs.get(attrName);
			if ("form".equals(nodeName) && attr == null && !(((NodeImpl)node).getType() == TypeToken.CLOSE) ) {
				Attribute newAction = new Attribute("action","");
				node.getAttributes().add(newAction );
				return true;
			}
	        if(attr == null)  return false;
	        String value = attr.getValue();
	        if(value == null) return false;
	        return verify(value);
	    }else
	    	return false; 
	    
	}	
  
  public final String eval(HTMLNode node, String nodeName, String attrName){
	  Attributes attrs = node.getAttributes();
	  Attribute attr = attrs.get(attrName);
	  String value = attr.getValue();
	  return value;
  }
  protected abstract boolean verify(String value); 
  
public boolean startWith (String line, String...pattern){
    for(String ele : pattern) {
      if(line.startsWith(ele)) return true;
    }
    return false;
  }
  
  public boolean endWith(String line, String...pattern){
    for(String ele : pattern) {
      if(line.endsWith( ele)) return true;
    }
    return false;
  }
  
  public boolean existIn(String line, String...pattern){
    for( String ele : pattern) {
      if(line.indexOf( ele) > -1) return true;
    }
    return false;
  }
  
  public boolean existAll(String line, String...pattern){
    for(String ele : pattern) {
      if(!(line.indexOf( ele) > -1)) return false;
    }
    return true;
  }
  
  public boolean equalsIn (String line, String...pattern){
    for( String ele : pattern) {
      if(line.equals( ele)) return true;
    }
    return false;
  }
  
  public boolean startOrEnd(String line , String[] start, String[] end){
    return startWith(line, start)|| endWith(line, end);
  }
  
  public boolean startAndEnd(String line , String[] start, String[] end){
    return startWith(line, start) && endWith(line, end);
  }	
  
  public boolean startOrEndOrExist(String line , String[] start, String[] end, String[] exist){
    return startWith(line, start)|| endWith(line, end) || existIn(line, exist);
  }
  
  public boolean startAndEndAndExist(String line , String[] start, String[] end, String[] exist){
    return startWith(line, start)&& endWith(line, end) && existIn(line, exist);
  }
  
  public boolean startAndEndAndExistAll(String line , String[] start, String[] end, String[] exist){
    return startWith(line, start)&& endWith(line, end) && existAll(line, exist);
  }

}
