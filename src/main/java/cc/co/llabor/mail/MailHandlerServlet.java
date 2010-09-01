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
	
	String defaultforwardto = "vasIlIIJ.pupkIN@gmAIl.com";

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

		log.warning("doPost");
		System.out.print("doPost");
		
		//MimeMessage message = new MimeMessage(session, req.getInputStream()); 
		//ServletInputStream inTmp = req.getInputStream();

        Forwarder fwdTmp = new Forwarder(defaultforwardto);
		{ // JSP/content
			HttpServletRequest request = req ; 
            HttpServletResponse response = resp;
            HttpSession httpSession = req.getSession();
			try{
				String strTo = "vasIlIIJ.pupkIN@gmAIl.com";
				String strToMemo = strTo;
				String strFrom =  "vasIlIIJ.pupkIN@gOOglEmAIl.com";
				String strFromMemo = strFrom;
				String strSubject = "FW:"+request.getRemoteUser()+":"+request.getRequestURL();
				String strBody  = "let it be...";
				System.out.println("send from:"+strFrom.toLowerCase() +"  to:"+strTo.toLowerCase()+"..."); 
				// Check that we have a file upload request
			    if(ServletFileUpload.isMultipartContent(request)){ 
		            MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
		            ServletFileUpload upload = new ServletFileUpload(factory);
		            upload.setSizeMax(4*1024*1024); // 4 MB 
		            // Parse the request
		            List<MemoryFileItem> items = upload.parseRequest(request);  
		            for(MemoryFileItem item : items) {
	                    item.flush(); 
	                    System.out.println( "Size:::::"+ item.getSize() );
	                    System.out.println( "Date:::::"+ item.getDate() );
	                    System.out.println( "FN:::::"+ item.getFieldName() );
	                    System.out.println( "ContentType:::::"+ item.getContentType() );
						String nameTmp = MemoryFileCache. put( item  );
						System.out.println( "stored into memcache as ::["+nameTmp +"]");
	                    //pm.makePersistent(item);
		            }
			    	strBody += parmsToString(request );
			    	fwdTmp.doPost(strTo, strToMemo, strFrom, strFromMemo, strSubject, strBody, items);
			    }else{
			    	include(resp, "uploadform.html");
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
		for (Enumeration e = request.getParameterNames();e.hasMoreElements(); ){
			String nextKeyTmp =(String)e.nextElement();
			String nextValTmp =request.getParameter(nextKeyTmp);
			strBodyTmp += nextKeyTmp;
			strBodyTmp += "=";
			strBodyTmp += nextValTmp;
			strBodyTmp += "\n";
		}
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
		//TODO 
		if (1 == 1)
			throw new RuntimeException(
					"autogenerated from vipup return not checked value since01.09.2010 ;)!");
		else
			return defaultforwardto;
	}
	public void setDefaultforwardto(String defaultforwardto) {
		this.defaultforwardto = defaultforwardto;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.log("Init ...");
		super.init(config);
		log.info("init compleet");
	}	
}


 