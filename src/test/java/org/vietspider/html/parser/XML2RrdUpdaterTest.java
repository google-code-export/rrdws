package org.vietspider.html.parser;
import java.io.InputStream;

import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ws.rrd.csv.Action;
import ws.rrd.csv.RrdUpdateAction;
import ws.rrd.csv.SystemOutPrintlnAction;
import ws.rrd.csv.ToStringPrintlnAction;

/**
 * <b>Description:TODO</b>
 * 
 * @author vipup<br>
 *         <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 * Creation: 09.02.2011::11:37:32<br>
 */
public class XML2RrdUpdaterTest {
	public static final int DONT_CHANGE_THIS_VALUE_AS_WELL_AS_XPATH2HASH_IMPLEMENTATION = 90;

	@Test
	public void testParse() throws Exception {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream input = classLoader.getResourceAsStream("xpath/exist1.xml");

		XMLReader reader = XMLReaderFactory.createXMLReader();
		//RrdUpdateAction rrdActioner = new  RrdUpdateAction();
		Action rrdActioner = new  SystemOutPrintlnAction();
		ContentHandler handler = new XPathContentHandler(rrdActioner);
		reader.setContentHandler(handler );
		InputSource in = new InputSource(input);
		reader.parse(in); 
	}
	
	@Test
	public void testXPATH2HASH() throws Exception {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream input = classLoader.getResourceAsStream("xpath/exist0.xml");

		XMLReader reader = XMLReaderFactory.createXMLReader();
		Action rrdActioner = new  ToStringPrintlnAction();
		 
		ContentHandler handler = new XPathContentHandler(rrdActioner);
		reader.setContentHandler(handler );
		InputSource in = new InputSource(input);
		reader.parse(in); 
/**
 * java.lang.AssertionError: /peras-intranet.noc.fiducia.de/manager/html/app[body='%2Frrd']/sessions-->
rrdtool create X-1293157220.rrd --start 1297216082 --step 60 				DS:data:GAUGE:240:U:U 				RRA:AVERAGE:0.5:3:480 				RRA:AVERAGE:0.5:17:592 				RRA:AVERAGE:0.5:131:340 				RRA:AVERAGE:0.5:731:719 				RRA:AVERAGE:0.5:10000:273 				RRA:MAX:0.5:3:480 				RRA:MAX:0.5:17:592 				RRA:MAX:0.5:131:340 				RRA:MAX:0.5:731:719 				RRA:MAX:0.5:10000:273 				RRA:MIN:0.5:3:480 				RRA:MIN:0.5:17:592 				RRA:MIN:0.5:131:340 				RRA:MIN:0.5:731:719 				RRA:MIN:0.5:10000:273  
rrdtool update X-1293157220.rrd 1297216092:10212
 expected:<-1> but was:<75>
	at org.junit.Assert.fail(Assert.java:71)
	at org.junit.Assert.failNotEquals(Assert.java:451)
	at org.junit.Assert.assertEquals(Assert.java:99)
	at org.vietspider.html.parser.XML2RrdUpdaterTest.testXPATH2HASH(XML2RrdUpdaterTest.java:84)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	at org.junit.internal.runners.TestMethodRunner.executeMethodBody(TestMethodRunner.java:99)
	at org.junit.internal.runners.TestMethodRunner.runUnprotected(TestMethodRunner.java:81)
	at org.junit.internal.runners.BeforeAndAfterRunner.runProtected(BeforeAndAfterRunner.java:34)
	at org.junit.internal.runners.TestMethodRunner.runMethod(TestMethodRunner.java:75)
	at org.junit.internal.runners.TestMethodRunner.run(TestMethodRunner.java:45)
	at org.junit.internal.runners.TestClassMethodsRunner.invokeTestMethod(TestClassMethodsRunner.java:66)
	at org.junit.internal.runners.TestClassMethodsRunner.run(TestClassMethodsRunner.java:35)
	at org.junit.internal.runners.TestClassRunner$1.runUnprotected(TestClassRunner.java:42)
	at org.junit.internal.runners.BeforeAndAfterRunner.runProtected(BeforeAndAfterRunner.java:34)
	at org.junit.internal.runners.TestClassRunner.run(TestClassRunner.java:52)
	at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:38)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:460)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:673)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:386)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:196)


 */		 
		org.junit.Assert.assertEquals(rrdActioner.toString(), rrdActioner.toString().indexOf("X-1293157220.rrd"),DONT_CHANGE_THIS_VALUE_AS_WELL_AS_XPATH2HASH_IMPLEMENTATION);
	}	
	
	@Test
	public void testRRDUpdate() throws Exception {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream input = classLoader.getResourceAsStream("xpath/exist1.xml");

		XMLReader reader = XMLReaderFactory.createXMLReader();
		RrdUpdateAction rrdActioner = new  RrdUpdateAction();
		//Action rrdActioner = new  SystemOutPrintlnAction();
		ContentHandler handler = new XPathContentHandler(rrdActioner);
		reader.setContentHandler(handler );
		InputSource in = new InputSource(input);
		reader.parse(in); 
	}
	
	
	

}
