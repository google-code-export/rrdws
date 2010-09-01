package cc.co.llabor.mail;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart; 
import javax.mail.util.ByteArrayDataSource;

import com.no10x.cache.MemoryFileItem;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.08.2010::16:52:27<br> 
 */
public class Forwarder {


	private String strTo = "vasIlIIJ.pupkIN@gmAIl.com";
	private String strToMemo = "vasIlIIJ.pupkIN@gmAIl.com";
	private String strFrom =  "vasIlIIJ.pupkIN@LLabOR.co.CC";
	private String strFromMemo = "<\""+strFrom+ "\">";

	public Forwarder( ) {
	 
	}
	
	public Forwarder(String defaultforwardto) {
		this.strTo = defaultforwardto;
		this.strToMemo = "<\""+defaultforwardto+"\">"; 
	}

	public void doPost(String strTo, String strToMemo, String strFrom, String strFromMemo, String strSubject, String strBody){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
			InternetAddress toAddress = new InternetAddress(strTo, strToMemo);
			InternetAddress fromAddress = new InternetAddress(strFrom, strFromMemo);
			msg.setFrom(fromAddress); 
			msg.addRecipient(Message.RecipientType.TO, toAddress);
			
			msg.setSubject(strSubject);
            msg.setText(strBody);

            Multipart mp = new MimeMultipart(); 
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(strBody, "text/html");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName("");
            attachment.setContent(strBody.getBytes() ,"text/plain");
            mp.addBodyPart(attachment); 
            msg.setContent(mp);	 
            Transport.send(msg);	
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
        	
        }
	}
	
	public void doPost(
			String strTo, String strToMemo, 
			String strFrom, String strFromMemo, 
			String strSubject, String strBody, 
			String attachmentName, byte[] attachmentData, String strAttachContentType   
		){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
		
        try {
            Message msg = new MimeMessage(session);
			InternetAddress toAddress = new InternetAddress(strTo, strToMemo);
			InternetAddress fromAddress = new InternetAddress(strFrom, strFromMemo);
			msg.setFrom(fromAddress); 
			msg.addRecipient(Message.RecipientType.TO, toAddress);
			
			msg.setSubject(strSubject);
            msg.setText(strBody);

            Multipart mp = new MimeMultipart(); 
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(strBody, "text/html");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(attachmentName);
            attachment.setContent(attachmentData, strAttachContentType);
            mp.addBodyPart(attachment); 
            msg.setContent(mp);	 
            Transport.send(msg);	
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("eOf"+strTo+strToMemo+strFrom+strFromMemo+strSubject+strBody);
        } 
	}

	public void doPost(String strTo2, String strToMemo2, String strFrom2,
			String strFromMemo2, String strSubject, String strBody,
			List<MemoryFileItem> items) throws IOException {
        Properties props = new Properties();      // 
        props.put("mail.smtp.host", "mail.host");
        props.put("mail.smtp.port", "" + 25);
        props.put("mail.debug", "true"  );
		
        Authenticator fakeAuth = null;//new FakeAuthenticator();
		Session session = Session.getDefaultInstance(props, fakeAuth );
		
        try {
            Message msg = new MimeMessage(session);
			InternetAddress toAddress = new InternetAddress(strTo, strToMemo);
			InternetAddress fromAddress = new InternetAddress(strFrom, strFromMemo);
			msg.setFrom(fromAddress); 
			msg.addRecipient(Message.RecipientType.TO, toAddress);
			
			msg.setSubject(strSubject);
            msg.setText(strBody);

            Multipart mp = new MimeMultipart(); 
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(strBody, "text/html");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            for (MemoryFileItem item:items){
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.setFileName( item.getName());
				DataSource dsTmp = new ByteArrayDataSource(item.getInputStream() , item.getContentType());
				DataHandler dhTmp = new DataHandler( dsTmp  );		 
                attachment.setDataHandler( dhTmp   ); 
                 
				
                mp.addBodyPart(attachment);
                
                	             	
            }//session.getTransport(toAddress)

            Transport.send(msg);	//Transport.class.getClassLoader().getParent().getParent().getParent().getParent()
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{ //session.getTransport("smtp")// OK!!! : session.getTransport("smtp")
        	System.out.println("eOf"+strTo2+strToMemo2+strFrom2+strFromMemo2+strSubject+strBody+items);
        } 
	}

	
 
 
}


 