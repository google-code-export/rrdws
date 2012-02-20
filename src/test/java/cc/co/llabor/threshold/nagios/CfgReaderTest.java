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
		LinkedList<Cfg> c = CfgReader.readFromCfg("contacts.cfg") ;//c = new ServicesCfg();
		//assertEquals(c.get("contact_name"), "nagiosadmin");
		//name    generic-service  
		assertEquals(c.get(0).get("address1"), "11111111@icq.com");
		assertEquals(c.get(0).toProperties().keySet().size() ,11); 
	}
	
	public void testWindows() throws IOException	{
		LinkedList<Cfg> c = CfgReader.readFromCfg("windows.cfg") ;//c = new ServicesCfg();
		//assertEquals(c.get("contact_name"), "nagiosadmin");
		//name    generic-service  
		assertEquals(c.get(0).get("address"), "192.168.1.20");
		assertEquals(c.get(0).toProperties().keySet().size() ,4); 
	}	

	
	public void testServices() throws IOException	{
		LinkedList<Cfg> c = CfgReader.readFromCfg("services.cfg") ;//c = new ServicesCfg();
		//assertEquals(c.get("contact_name"), "nagiosadmin");
		//name    generic-service  
		assertEquals(c.get(0).get("name"), "generic-service");
		assertEquals(c.get(0).toProperties().keySet().size() ,13);
		//assertEquals(c.get("check_command"), "check_http");
		assertEquals(c.get(1).get("check_command" ), "check_http");
		assertEquals(c.get(1).toProperties().keySet().size(), 13);
		assertEquals(c.get(2).get("check_command" ), "check_ping!100.0,20%!500.0,60%");
		assertEquals(c.get(2).toProperties().keySet().size(), 13 );
	}	

	//servicedependency.cfg
	public void testServicesDeps() throws IOException	{
		LinkedList<Cfg> c = CfgReader.readFromCfg("servicedependency.cfg") ;//c = new ServicesCfg();
		//assertEquals(c.get("contact_name"), "nagiosadmin");
		//name    generic-service  
		assertEquals(c.get(0).get("host_name"), "Host B");
		assertEquals(c.get(0).toProperties().keySet().size() ,6); 
	}	
	
	
	
	//timeperiod
	public void testTtimeperiod() throws IOException	{
		LinkedList<Cfg> c = CfgReader.readFromCfg("timeperiod.cfg") ;//c = new ServicesCfg();
	 	//timeperiod_name		misc-date-ranges
		assertEquals(c.get(2).get("timeperiod_name"), "misc-date-ranges");
		assertEquals(c.get(2).toProperties().keySet().size() ,8); 
	}		
	
	//contactgroup
	public void testContactgroup() throws IOException	{
		LinkedList<Cfg> c = CfgReader.readFromCfg("contactgroup.cfg") ;//c = new ServicesCfg();
	 	//alias			Novell Administrators
		assertEquals(c.get(0).get("alias"), "Novell Administrators");
		assertEquals(c.get(0).toProperties().keySet().size() ,8); 
	}		
}


 