package cc.co.llabor.mail;
import java.io.IOException; 
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

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
	private static final Logger log = Logger.getLogger(MailHandlerServlet.class.getName());


	private String strTo = "vasIlIIJ.pupkIN@gOOglEmAIl.com".toLowerCase();//"vip@llabor.co.cc";
	private String strToMemo =  " "+strTo+ " ";;
	private String strFrom =  "vasIlIIJ.pupkIN@gOOgLemAIl.com".toLowerCase();
	private String strFromMemo = " "+strFrom+ " ";

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
        	log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (MessagingException e) {
			log.throwing(this.getClass().getName(), "doPost", e);
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
        	log.throwing(this.getClass().getName(), "doPost", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		}finally{
			System.out.println("eOf"+strTo+strToMemo+strFrom+strFromMemo+strSubject+strBody);
        } 
	}

	public void doPost(String strTo2, String strToMemo2, String strFrom2,
			String strFromMemo2, String strSubject, String strBody,
			List<MemoryFileItem> items) throws IOException, MessagingException {
		Properties props = new Properties(); // 
		// props.put("mail.smtp.host", "mail.host");
		// props.put("mail.smtp.port", "" + 25);
		props.put("mail.debug", "true" );

		Authenticator fakeAuth = null;// new FakeAuthenticator();
		Session session = Session.getDefaultInstance(props, fakeAuth);
		MyData md = new DefaultMe();
		try {
			Message msg = new MimeMessage(session); 
			Multipart mp = new MimeMultipart();
			//we'll have an htmlpart and an attachment partin our mimemultipart message
			MimeBodyPart htmlPart = new MimeBodyPart();
			 //setup message (from, to, subject, etc)
			msg.setFrom(new InternetAddress(md.getEmail()));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(md.getEmail(), "Dr. " + md.getNickname()));
			msg.setSubject(strSubject);
			//msg.setText(strBody); !!!
			htmlPart.setContent(strBody, "text/html");
			mp.addBodyPart(htmlPart);
			log.warning("MESSAGE FROM::" + strFrom2+"\nMESSAGE TO::" + strTo2+"\nSUBJ: " + strSubject+"  \n" + strBody);
			int i=0;
			for (MemoryFileItem item : items) {
				if ("null".equals( ""+item.getContentType())) continue;
				MimeBodyPart attachment = new MimeBodyPart();
                //prepare attachment using abytearraydatasource
				i++;
				byte[] attachmentData = item.get();
				DataSource dsTmp = new ByteArrayDataSource(attachmentData, item .getContentType());
				attachment.setFileName("fw.#"+i + item.getName());
				log.warning(">>>" + item.getName() + "::" + item.getContentType());
				DataHandler dhTmp = new DataHandler(dsTmp);
				attachment.setDataHandler(dhTmp);
                //put the parts together into a multipart 				
				mp.addBodyPart(attachment);
			} 
			msg.setContent(mp);
			msg.saveChanges();
			log.warning(" sending...");
			Transport.send(msg); // Transport.class.getClassLoader().getParent().getParent().getParent().getParent()
			log.warning(" done.");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} finally { // session.getTransport("smtp")// OK!!! :
					// session.getTransport("smtp")
			log.warning("--------------------eOf" + strTo2 + "|" + strToMemo2
					+ "|" + strFrom2 + "|" + strFromMemo2 + "|" + strSubject
					+ "|" + strBody);
		}
	}
	public void doPost(String strTo2, String strToMemo2, String strFrom2,
				String strFromMemo2, String strSubject, String strBody,
				Multipart mp) throws IOException {
        Properties props = new Properties();      // 
        // props.put("mail.smtp.host", "mail.host");
        //props.put("mail.smtp.port", "" + 25);
        //props.put("mail.debug", "true"  );
		
        Authenticator fakeAuth = null;//new FakeAuthenticator();
		Session session = Session.getDefaultInstance(props, fakeAuth );
		
        try {
            Message msg = new MimeMessage(session);
            
            MyData md = new DefaultMe();
			
            msg.setFrom(new InternetAddress(md.getEmail()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(md.getEmail(), "Dr. " +md.getNickname()));			
            
			msg.setSubject(strSubject);
			strBody = "<b>Hi</b>  Here is the encrypted ptscut list yourequested.  Please <b><i>enter your password</i></b> to open thedocument.\nCheers!";
            msg.setText(strBody);
            
            log.warning("MESSAGE FROM::"+strFrom2);
            log.warning("MESSAGE TO::"+strTo2);
            log.warning("SUBJ: "+strSubject);
            log.warning(" \n"+strBody);
            
            //Multipart mp = combineMultiPart(strBody, items);
            
            msg.setContent(mp);
            msg.saveChanges();
            log.warning(" sending...");
            Transport.send(msg);	//Transport.class.getClassLoader().getParent().getParent().getParent().getParent()
            log.warning(" done.");
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
        	log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			log.throwing(this.getClass().getName(), "doPost", e);
			e.printStackTrace();
		}finally{ //session.getTransport("smtp")// OK!!! : session.getTransport("smtp")
            log.warning("--------------------eOf"+strTo2+"|"+strToMemo2+"|"+strFrom2+"|"+strFromMemo2+"|"+strSubject+"|"+strBody );
        } 
	}

 

	
	       public void emailList(byte[] attachmentData) {
               Properties props = new Properties();
               Session session = Session.getDefaultInstance(props,null);
               session.setDebug(true);

//              String msgBody = "Here is an encrypted copy of thepatient scut list you requested.  Enter your password to open the  attached file.";
               String htmlBody = "<b>Hi</b>  Here is the encrypted ptscut list yourequested.  Please <b><i>enter your password</i></b> to open thedocument.\nCheers!";

               MyData md = new DefaultMe();
               try {

                       Message msg = new MimeMessage(session);
                       Multipart mp = new MimeMultipart();
                       //we'll have an htmlpart and an attachment partin our mimemultipart message
                       MimeBodyPart htmlPart = new MimeBodyPart();
                       MimeBodyPart attachment = new MimeBodyPart();

                       //setup message (from, to, subject, etc)
                       msg.setFrom(new InternetAddress(md.getEmail()));
                       msg.addRecipient(Message.RecipientType.TO, new InternetAddress(md.getEmail(), "Dr. " +md.getNickname()));
                       msg.setSubject("Here is the patient list yourequested.");
//                      msg.setText(msgBody);

                       //prepare html part
                       htmlPart.setContent(htmlBody, "text/html");

                       //prepare attachment using abytearraydatasource
                       DataSource src = new ByteArrayDataSource(attachmentData, "application/pdf");
                       attachment.setFileName("ptlist.pdf");
                       attachment.setDataHandler(new DataHandler(src));

                       //put the parts together into a multipart
                       mp.addBodyPart(htmlPart);
                       mp.addBodyPart(attachment);

                       //set the content of the message to be themultipart
                       msg.setContent(mp);
                       msg.saveChanges();//I think this is necessary,but, not sure....

                       Transport.send(msg);
               } catch (AddressException e) {
                       e.printStackTrace();
               } catch (MessagingException e) {
                       e.printStackTrace();
                       // c.sep("sfe: "+e.getInvalidAddresses()[0].toString());
               } catch (UnsupportedEncodingException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
               }
       } 		
	
	       public void emailLists(byte[][] attachmentDatas) {
               Properties props = new Properties();
               Session session = Session.getDefaultInstance(props,null);
               session.setDebug(true);

//              String msgBody = "Here is an encrypted copy of thepatient scut list you requested.  Enter your password to open the  attached file.";
               String htmlBody = "<b>Hi</b>  Here is the encrypted ptscut list yourequested.  Please <b><i>enter your password</i></b> to open thedocument.\nCheers!";

               MyData md = new DefaultMe();
               try {

                       Message msg = new MimeMessage(session);
                       Multipart mp = new MimeMultipart();
                       //we'll have an htmlpart and an attachment partin our mimemultipart message
                       MimeBodyPart htmlPart = new MimeBodyPart();
                       //setup message (from, to, subject, etc)
                       msg.setFrom(new InternetAddress(md.getEmail()));
                       msg.addRecipient(Message.RecipientType.TO, new InternetAddress(md.getEmail(), "Dr. " +md.getNickname()));
                       msg.setSubject("Here is the patient list yourequested.");
//                      msg.setText(msgBody);

                       //prepare html part
                       htmlPart.setContent(htmlBody, "text/html");
                       mp.addBodyPart(htmlPart);
                       int i=0;
                       for (byte[]attachmentData:attachmentDatas){
                    	   final MimeBodyPart attachment = new MimeBodyPart();
                    	   i++;
	                       //prepare attachment using abytearraydatasource
	                       DataSource src = new ByteArrayDataSource(attachmentData, "application/pdf");
	                       attachment.setFileName("ptlist"+i+".pdf");
	                       attachment.setDataHandler(new DataHandler(src));
	
	                       //put the parts together into a multipart 
	                       mp.addBodyPart(attachment);
                       }
                       //set the content of the message to be themultipart
                       msg.setContent(mp);
                       msg.saveChanges();//I think this is necessary,but, not sure....

                       Transport.send(msg);
               } catch (AddressException e) {
                       e.printStackTrace();
               } catch (MessagingException e) {
                       e.printStackTrace();
                       // c.sep("sfe: "+e.getInvalidAddresses()[0].toString());
               } catch (UnsupportedEncodingException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
               }
       } 		
 
}


 