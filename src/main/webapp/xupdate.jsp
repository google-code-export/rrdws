<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<!-- % response.setContentType("application/vnd.wap.xhtml+xml"); %>
 -->
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%> 
<%@page import="java.util.List"%> 
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="java.io.IOException"%> 
<%@page import="ws.rrd.csv.RrdUpdateAction"%>
<%@page import="com.no10x.cache.MemoryFileItemFactory"%>
<%@page import="com.no10x.cache.MemoryFileItem"%>
<%@page import="com.no10x.cache.MemoryFileCache"%>
<%@page import="org.xml.sax.XMLReader"%>
<%@page import="org.xml.sax.helpers.XMLReaderFactory"%>
<%@page import="org.xml.sax.ContentHandler"%> 
<%@page import="org.xml.sax.InputSource"%>
<%@page import="ws.rrd.xpath.XPathContentHandler"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>    <title>XML RDD UPDATE PAGE</title>  </head>
<%
try{
    // Check that we have a file upload request
    if(ServletFileUpload.isMultipartContent(request)){
          
            MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(94*1024*1024); // 4 MB
  
            // Parse the request
            List<MemoryFileItem> items = upload.parseRequest(request);
             
            String nameTmp = null;
            for(MemoryFileItem item : items) {
            	 
                    item.flush(); 
                    response.getWriter().append( "<br>Name:::"+ item.getName() );
                    response.getWriter().append( "<br>Size:::::"+ item.getSize() );
                    response.getWriter().append( "<br>Date:::::"+ item.getDate() );
                    response.getWriter().append( "<br>FN:::::"+ item.getFieldName() );
                    response.getWriter().append( "<br>ContentType:::::"+ item.getContentType() );                    
                    session.setAttribute(item.getName(),item );                
                    nameTmp = MemoryFileCache. put( item  );
					System.out.println( "stored into memcache as ::["+nameTmp +"]");
					// process last XML-data
					XMLReader reader = XMLReaderFactory.createXMLReader();
					RrdUpdateAction rrdActioner = new  RrdUpdateAction();
					//Action rrdActioner = new  SystemOutPrintlnAction();
					ContentHandler handler = new XPathContentHandler(rrdActioner);
					((XPathContentHandler)handler).setOut(response.getWriter() );
					reader.setContentHandler(handler );
					InputSource in = new InputSource(item.getInputStream());
					reader.parse(in); 
            }
    }else{
%>
<body>
<form  method="post" enctype="multipart/form-data">
   Select XML File for update:<br>
    <input name="Datei" type="file" size="50" maxlength="100000" accept="text/*"/>
    <input type="submit"/>
   
</form>
<%     	
    }
} catch(FileUploadException e){ throw new IOException("Unable to handle uploaded file"); 
} catch(Throwable e){ e.printStackTrace(response.getWriter()); }

%> 
  </body>
</html>