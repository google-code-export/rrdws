package cc.co.llabor.mail;
 

import org.junit.Test;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.08.2010::17:10:33<br> 
 */
public class ForwarderTest {

	
 
	@Test
	public void testDoTestPost() {
		
		Forwarder fwd = new Forwarder(); 

        String strTo = "admin@example.com";
		String strToMemo = "Example.com Admin";
        String strFrom = "user@example.com";
		String strFromMemo = "Mr. User";
        String strSubject = "Your Example.com account has been activated";
        String strBody = "...";
        
        fwd.doPost(
        		strTo, strToMemo, 
        		strFrom, strFromMemo, 
        		strSubject, strBody  
        		);
	}

	@Test
	public void testDoTestPostMime() {

		Forwarder fwd = new Forwarder(); 
		
		
        String strTo = "admin@example.com";
		String strToMemo = "Example.com Admin";
        String strFrom = "user@example.com";
		String strFromMemo = "Mr. User";
        String strSubject = "Your Example.com account has been activated";
        String strBody = "...";
        
        byte[] pdfData = new byte[1];
        
        fwd.doPost(
        		strTo, strToMemo, 
        		strFrom, strFromMemo, 
        		strSubject, strBody,  
        		"manual.pdf", pdfData , "application/pdf"
        		);
	}	
		

}


 