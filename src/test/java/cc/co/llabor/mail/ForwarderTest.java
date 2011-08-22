package cc.co.llabor.mail;
 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.smtp.MessageListener;
import org.subethamail.smtp.server.SMTPServer;
 
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

	SMTPServer smtpServer = null;
	@Before
	public void setup( ){
		// Send a test message mail-int.noc.fiducia.de 
		Collection<MessageListener>listeners = new ArrayList<MessageListener>();
		MessageListener l1 = new MyMessageListener();
		listeners.add(l1 );
		smtpServer = new SMTPServer(listeners );
		smtpServer.start();
	}
	@After
	public void teadDown( ){
 		smtpServer.stop();
	}
	
 
	@Test
	public void testDoTestPost() throws IOException, MessagingException {
		
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


 