package cc.co.llabor.threshold.nagios;


import java.io.IOException;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  16.02.2012::13:16:34<br> 
 */
public class CfgReaderTest  extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public void testContacts() throws IOException	{
		LinkedList<Cfg> c = CfgReader.readFromCfg("services.cfg") ;//c = new ServicesCfg();
		//assertEquals(c.get("contact_name"), "nagiosadmin");
		//name    generic-service  
		assertEquals(c.get(0).get("name"), "generic-service");
		//assertEquals(c.get("check_command"), "check_http");
		assertEquals(c.get(1).get("check_command" ), "check_http");
		assertEquals(c.get(2).get("check_command" ), "check_ping!100.0,20%!500.0,60%");
	}
	
}


 