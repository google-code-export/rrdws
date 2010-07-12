package org.vietspider.html;

import java.io.IOException;
import java.net.URL;

import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.util.HyperLinkUtil;
 

import junit.framework.TestCase;


/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  12.07.2010::10:35:11<br> 
 */
public class HTMLTest extends TestCase{
	public void testAHREF() throws Exception{
		HTMLParser2 parser2 = new HTMLParser2();
		String contextEncStr = "UTF-8";
		String contextTypeStr = "text/html";
		String a = "a.htm";//"0_aHR0cDovL2hhYnJhaGFici5ydS8=.htm";//
		String b =  "aHR0cDovL2hhYnJhaGFici5ydS8=.htm";		
		HTMLDocument documentTmp;
		HyperLinkUtil handler  = new HyperLinkUtil();
		java.io.InputStream in =  this.getClass().getClassLoader().getResourceAsStream(a);
		byte[] dataBuf = new byte[in.available()];
		in.read(dataBuf);
		try{
			documentTmp = parser2.createDocument(dataBuf , contextEncStr );// "utf-8"
		}catch(Exception e){					
			documentTmp = parser2.createDocument(dataBuf, null );// "utf-8"
		}		

    	HTMLNode rootTmp = documentTmp.getRoot();
		URL realURL = new URL("http://jUnIt.net/");
		String SwapServletUrl = "J://";
		HTMLNode node = documentTmp.getRoot();
		//		testCreateFullLink(rootTmp, SwapServletUrl, realURL );
//    	testCreateImageLink(documentTmp.getRoot(), SwapServletUrl, realURL);
		handler.createFullNormalLink(node,  SwapServletUrl,  realURL);
//    	testCreateMetaLink(rootTmp, SwapServletUrl, realURL);
		handler.createMetaLink(node,  SwapServletUrl,  realURL);
//     	testCreateScriptLink(rootTmp, SwapServletUrl, realURL);
		handler.createScriptLink( node,  SwapServletUrl,  realURL);
		System.out.println(documentTmp.getTextValue());
		
		assertTrue(documentTmp.getTextValue().indexOf("http:")==-1);
	}

}


 