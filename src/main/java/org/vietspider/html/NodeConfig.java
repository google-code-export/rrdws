/***************************************************************************
 * Copyright 2003-2006 by VietSpider - All rights reserved.  *
 *    *
 **************************************************************************/
package org.vietspider.html;
import org.vietspider.html.Tag;
import org.vietspider.html.MoveType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 *  Author : Nhu Dinh Thuan 
 *          Email:nhudinhthuan@yahoo.com
 * Jul 30, 2006
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NodeConfig {  
  Name name() ;
  Class<?> type();
  
  boolean only () default false;  
  boolean hidden () default false;
  boolean block() default false;
  
  org.vietspider.html.MoveType move() default org.vietspider.html.MoveType.ADD;
  
  org.vietspider.html.Tag start () default org.vietspider.html.Tag.REQUIRED;
  org.vietspider.html.Tag end () default org.vietspider.html.Tag.REQUIRED;  
  Class<?> [] end_types() default {};
  Name [] end_names() default {};
  
  
  Name [] parent() default {};
  Name [] children() default {};
  Class<?> [] children_types () default {}; 
}
