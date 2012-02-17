package cc.co.llabor.threshold.nagios;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axis.utils.ByteArray;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  17.02.2012::10:29:29<br> 
 */
public class CommentedPropertiesTest  extends TestCase {

	@Before
	public void setUp() throws Exception {  
	}

	@After
	public void tearDown() throws Exception {
	}


	public void test1st() throws IOException{
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("nagios/commented.properties");
			CommentedProperties pTmp = new CommentedProperties();
			pTmp .load(inStream);
			//host_name=host1.domain.com
			assertEquals("host1.domain.com", pTmp.get("host_name"));
		  			
	}

	public void test2nd() throws IOException{
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("nagios/commented.properties");
			CommentedProperties pTmp = new CommentedProperties();
			pTmp .load(inStream);
			//host_name=host1.domain.com
			assertEquals("host1.domain.com", pTmp.get("host_name"));
			assertEquals("", pTmp.getPropertyComment("host_name"));
		   
	}
	
	

	public void test3rd() throws IOException{
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("nagios/commented.properties");
			CommentedProperties pTmp = new CommentedProperties();
			pTmp .load(inStream);
			//host_name=host1.domain.com
			assertEquals("host1.domain.com", pTmp.get("host_name"));
			assertEquals("#2min", pTmp.getPropertyComment("notification_interval"));
		   
	}
		
	public void test3rdStore() throws IOException {
		InputStream inStream = this.getClass().getClassLoader()
				.getResourceAsStream("nagios/commented.properties");
		CommentedProperties pTmp = new CommentedProperties();
		pTmp.load(inStream);
		// host_name=host1.domain.com
		assertEquals("host1.domain.com", pTmp.get("host_name"));
		assertEquals("#2min", pTmp.getPropertyComment("notification_interval"));

		OutputStream out = new ByteArrayOutputStream();
		// store - restore again
		pTmp.store(out, "");
		out.flush();
		String outData = out.toString();
		System.out.println(outData);
		assertEquals(outData.indexOf("#2min"), 326);
		assertEquals(outData.indexOf("notification_interval"), 333);
	}
		
	public void test3rdStoreRestore() throws IOException {
		InputStream inStream = this.getClass().getClassLoader()
				.getResourceAsStream("nagios/commented.properties");
		CommentedProperties pTmp = new CommentedProperties();
		pTmp.load(inStream);
		// host_name=host1.domain.com
		assertEquals("host1.domain.com", pTmp.get("host_name"));
		assertEquals("#2min", pTmp.getPropertyComment("notification_interval"));

		OutputStream out = new ByteArrayOutputStream();
		// store - restore again
		pTmp.store(out, "");
		out.flush();
		String outData = out.toString();
		System.out.println(outData);
		assertEquals(outData.indexOf("#2min"), 326);
		assertEquals(outData.indexOf("notification_interval"), 333);
		
		
		CommentedProperties prTmp = new CommentedProperties();
		prTmp.load(new ByteArrayInputStream(outData.getBytes()));
		
		// restored have to be same with stored
		assertEquals("host1.domain.com", prTmp.get("host_name"));
		assertEquals("#2min", prTmp.getPropertyComment("notification_interval"));
		
		
	}	

	
	
	
	public void test3rdStore_STD_Restore() throws IOException {
		InputStream inStream = this.getClass().getClassLoader()
				.getResourceAsStream("nagios/commented.properties");
		CommentedProperties pTmp = new CommentedProperties();
		pTmp.load(inStream);
		// host_name=host1.domain.com
		assertEquals("host1.domain.com", pTmp.get("host_name"));
		assertEquals("#2min", pTmp.getPropertyComment("notification_interval"));

		OutputStream out = new ByteArrayOutputStream();
		// store - restore again
		pTmp.store(out, "");
		out.flush();
		String outData = out.toString();
		System.out.println(outData);
		assertEquals(outData.indexOf("#2min"), 326);
		assertEquals(outData.indexOf("notification_interval"), 333);
		
		
		Properties prTmp = new Properties();
		prTmp.load(new ByteArrayInputStream(outData.getBytes()));
		
		// restored by STD_Property should be similar also !
		assertEquals("host1.domain.com", prTmp.get("host_name"));
		assertEquals("120", prTmp.getProperty("notification_interval"));
		
		
	}		
}


 