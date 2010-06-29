<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<!-- % response.setContentType("application/vnd.wap.xhtml+xml"); %>
 -->
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%> 
<%@page import="java.util.List"%>
<%@page import="ws.rrd.mem.MemoryFileCache"%>
<%@page import="ws.rrd.mem.MemoryFileItemFactory"%>
<%@page import="ws.rrd.mem.MemoryFileItem"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="java.io.IOException"%>
<%@page import="ws.rrd.csv.CSVParser"%>
<%@page import="ws.rrd.csv.Action"%>
<%@page import="ws.rrd.csv.RrdUpdateAction"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>    <title>RDD UPDATE PAGE</title>  </head>
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
					// process last CSV-data
					CSVParser csv = new CSVParser( item.getInputStream());
					Action a = new RrdUpdateAction();
					csv.setIgnoreWrongLine(true);
					Object o = csv.perform(a); 		 		
					response.getWriter().append( ""+ ("" + o).replace(", \\\\", ",\n \\\\"));
                    //pm.makePersistent(item);
            }
    }else{
%>
<body>
<form  method="post" enctype="multipart/form-data">
   Select CSV File for update:<br>
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