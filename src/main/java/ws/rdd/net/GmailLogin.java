package ws.rdd.net;

/***************************************************************************
 * Copyright 2001-2008 The VietSpider         All rights reserved.  		 *
 **************************************************************************/
 
import java.net.URL;
import java.util.List;

 
import org.vietspider.html.HTMLDocument;
import org.vietspider.html.HTMLNode;
import org.vietspider.html.parser.HTMLParser2;
import org.vietspider.html.util.HyperLinkUtil; 

/**
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Nov 2, 2008
 */
public class GmailLogin {
	  private static HyperLinkUtil handler  = new HyperLinkUtil();

	  private static void testGetLink(HTMLNode node){
	    List<String> list  = handler.scanSiteLink(node);
	    for(String ele : list)
	      System.out.println(ele);
	  }

	  private static void testCreateFullLink(HTMLNode node, URL home){
	    handler.createFullNormalLink(node, null, home);
	    List<String> list  = handler.scanSiteLink(node);
	    for(String ele : list)
	      System.out.println(ele);
	  }
//
//	  private static void testCreateImageLink(HTMLNode node, URL home){
//		    handler.createFullImageLink(node, null, home);
//		    List<String> list  = handler.scanImageLink(node);
//		    for(String ele : list)
//		      System.out.println(ele);
//	  }

	  public static void main(String[] args) {
	    try{
	      String baseURL =  "http://localhost:8888/GmailLogin.jsp?url=";
	      String urlStr = "http://www.fiduciA.de/service/suchergebnis.html?searchTerm=java";
	      try{
	    	  urlStr = args[0];
	      }catch(Throwable e){}
	      URL url = new URL(baseURL+urlStr);

	      String data = new UrlFetchTest().testFetchUrl( urlStr );
	      long startTmp = System.currentTimeMillis();
	      int lenTmp = 0;
	      int i=100;
	      for(;i<100;i++){
	    	  //data = new UrlFetchTest().testFetchUrl( "http://www.FIDUCIA.DE");
	    	  HTMLDocument documentTmp = new HTMLParser2().createDocument(data.getBytes(), "utf-8");
	    	  testCreateFullLink(documentTmp.getRoot(), url);
	    	  //testCreateImageLink(documentTmp.getRoot(), url);
	    	  lenTmp += documentTmp.getTextValue().length();
	      }
	      long stopTmp = System.currentTimeMillis();
	      long timeTmp = 1+stopTmp-startTmp;
	      System.out.println("ms per doc::"+(timeTmp)/i+ "   CPS ::"+lenTmp/timeTmp);
	      HTMLDocument document = new HTMLParser2().createDocument(data.getBytes(), "utf-8");
	      testGetLink(document.getRoot());
	      System.out.println("\n\n\n\n*********************************************************************\n\n\n\n");
	      testCreateFullLink(document.getRoot(), url);
	      System.out.println("\n\n\n\n*********************************************************************\n\n\n\n");
	      //testCreateImageLink(document.getRoot(), url);
	    }catch(Exception exp){
	      exp.printStackTrace();
	    }
	  }
}
