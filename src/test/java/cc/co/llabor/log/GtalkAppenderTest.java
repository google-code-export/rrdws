package cc.co.llabor.log;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 


/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  29.09.2011::00:16:14<br> 
 */
public class GtalkAppenderTest extends TestCase {

	private static final Logger log = LoggerFactory.getLogger(GtalkAppenderTest.class .getName());

	public void testLog2Gtalk(){
		log.debug("test");
	}
}


 