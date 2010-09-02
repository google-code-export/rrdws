package cc.co.llabor.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;  

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ws.rrd.server.ExceptionUtils;

import com.no10x.cache.MemoryFileCache;
import com.no10x.cache.MemoryFileItem;
import com.no10x.cache.MemoryFileItemFactory;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.08.2010::16:40:27<br> 
 */
public class MailHandlerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MailHandlerServlet.class.getName());
    /**
	 * @author vipup
	 */
	private static final long serialVersionUID = 7394798284807457106L;
	
	//<init-param defaultforwardto='vasIlIIJ.pupkIN@gmAIl.com'/>
	
	String defaultforwardto = "vasIlIIJ.pupkIN@gOOglEmAIl.com".toLowerCase();///"vip@llabor.co.cc";

	public void doGetPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		System.out.print("doGetPost");
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, 
            HttpServletResponse resp) throws IOException {
		System.out.print("doGet");
		doPost(req, resp);
	}
	public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp) 
            throws IOException { 

		log.warning("do_Post000");
		//System.out.print("DoPOPST");
		
		//MimeMessage message = new MimeMessage(session, req.getInputStream()); 
		//ServletInputStream inTmp = req.getInputStream();

		log.warning("do_Post1");
		{ // JSP/content
			HttpServletRequest request = req ; 
			log.warning("do_Post2");
            //HttpServletResponse response = resp;
            //HttpSession httpSession = req.getSession();
			try{
				String strTo = "vasIlIIJ.pupkIN@gOOglEmAIl.com".toLowerCase();//vip@llabor.co.cc
				log.warning("do_Post3");
				/*
				 *     <input name="_from" type="text" value="">
    					<input name="_to" type="text" value="">
				 */
				strTo = "null".equals( ""+req.getParameter("_to") )?strTo:req.getParameter("_to");
				log.warning("do_Post4");
				String strToMemo = strTo;
				log.warning("do_Post5");
				String strFrom =  "vasIlIIJ.pupkIN@gOOglEmAIl.com";
				log.warning("do_Post6");
				strFrom = "null".equals( ""+req.getParameter("_from") )?strFrom:req.getParameter("_from");
				log.warning("do_Post7");
				String strFromMemo = strFrom;
				log.warning("do_Post8");
				String strSubject = "FW:"+request.getRemoteUser()+":"+request.getRequestURL();
				log.warning("do_Post9");
				String strBody  = "posted from <a href='"+req.getRequestURL()+"'>here</a> by "+req.getRemoteUser()+"@"+req.getRemoteHost();
				log.warning("do_PostA");
				//System.out.println("send from:"+strFrom.toLowerCase() +"  to:"+strTo.toLowerCase()+"...");
				log.warning("send from:"+strFrom.toLowerCase() +"  to:"+strTo.toLowerCase()+"...");
				// Check that we have a file upload request
			    if(ServletFileUpload.isMultipartContent(request)){ 
			    	log.warning("do_PostB");
		            MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
		            log.warning("do_PostC");
		            ServletFileUpload upload = new ServletFileUpload(factory);
		            log.warning("do_PostD");
		            upload.setSizeMax(4*1024*1024); // 4 MB 
		            // Parse the request
		            List<MemoryFileItem> items = upload.parseRequest(request);  
		            for(MemoryFileItem item : items) {
		            	if ("null".equals(""+item.getContentType()))continue;
	                    item.flush(); 
	                    log.warning( "Size:::::"+ item.getSize() );
	                    log.warning( "Date:::::"+ item.getDate() );
	                    log.warning( "FN:::::"+ item.getFieldName() );
	                    log.warning( "ContentType:::::"+ item.getContentType() );
						String nameTmp = MemoryFileCache. put( item  );
						log.warning( "stored into memcache as ::["+nameTmp +"]");
	                    //pm.makePersistent(item);
		            }
			    	strBody += parmsToString(request );
			    	Forwarder fwdTmp = new Forwarder(defaultforwardto);
			    	fwdTmp.doPost(strTo, strToMemo, strFrom, strFromMemo, strSubject, strBody, items);
			    }else{
			    	log.warning( "try to handle with GAE-API ....");
			    	try{
				    	// try to handle with GAE-API
				    	Properties props = new Properties(); 
				    	Session session = Session.getDefaultInstance(props, null); 
				    	log.warning( "try to handle with GAE-API ::[" +session);
				    	log.warning( "try to handle with GAE-API ::["+request.getMethod()+":"+request.getContentType()+"   ..");
				    	MimeMessage message = new MimeMessage(session, req.getInputStream());
				    	log.warning( "try to handle with GAE-API ::["+message +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getContentType() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getDescription() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getFileName() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getLineCount() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getMessageID() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getFrom() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getReplyTo() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getSubject()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getSize() +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getContent()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getDataHandler()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getInputStream()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getInputStream().available()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getRawInputStream()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getRawInputStream().available()  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getSender()	  +"]");
				    	log.warning( "try to handle with GAE-API ::["+message.getSentDate()  +"]");
				    	
				    	Multipart contentTmp = (Multipart)message.getContent(); 
				    	Forwarder fwdTmp = new Forwarder(defaultforwardto);
				    	fwdTmp.doPost(strTo, strToMemo, strFrom, strFromMemo, strSubject, strBody, contentTmp);
				    	
				    	 
			    	}catch(Exception e){
			    			e.printStackTrace();
			    			include(resp, "uploadform.html");
			    	}
			    }
			} catch(FileUploadException e){
				e.printStackTrace( );
				throw new IOException("Unable to handle uploaded file"); 
			} catch(Throwable e){ 
				e.printStackTrace( ); 
			}
			
		}
		
    }

	/**
	 * @author vipup
	 * @param request 
	 * @return
	 */
	private String parmsToString(HttpServletRequest request) {
		String strBodyTmp = "";
		try{
			for (Enumeration e = request.getParameterNames();e.hasMoreElements(); ){
				String nextKeyTmp =(String)e.nextElement();
				String nextValTmp =request.getParameter(nextKeyTmp);
				strBodyTmp += nextKeyTmp;
				strBodyTmp += "=";
				strBodyTmp += nextValTmp;
				strBodyTmp += "\n";
				log.warning(strBodyTmp);
			}
		}catch(Exception e){e.printStackTrace();}
		return strBodyTmp;
		
	}
	
	private byte[] getResourceAsBA(String namePar) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(namePar); 
		byte[] b = new byte[in.available()] ;
		in.read(b);
		return b;
	}
	
	
	private void include(HttpServletResponse resp, String resourceName) {
		try {
			ServletOutputStream out;
			out = resp.getOutputStream();			
			byte[] b = getResourceAsBA(resourceName);
			String newVal = new String(b);
			b = newVal.getBytes();
			out.write(b );
		} catch (IOException e) { 
			e.printStackTrace();
		}  	
	}
	public String getDefaultforwardto() { 
			return defaultforwardto;
	}
	public void setDefaultforwardto(String defaultforwardto) {
		this.defaultforwardto = defaultforwardto;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			this.log("Init ...");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		super.init(config);
		try {
			log.info("init compleet");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}	
}


 