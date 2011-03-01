<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<!-- % response.setContentType("application/vnd.wap.xhtml+xml"); %> -->
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%> 
<%@page import="java.util.List"%>  
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="java.io.IOException"%> 
<%@page import="cc.co.llabor.cache.MemoryFileItemFactory"%>
<%@page import="cc.co.llabor.cache.MemoryFileItem"%>
<%@page import="cc.co.llabor.cache.MemoryFileCache"%>
<html xmlns="http://www.w3.org/1999/xhtml"> <head> <title>RDD PUSH PAGE</title>
<link type="text/css" title="www" rel="stylesheet" media="screen,projection" href="css/screen.css" />
<link type="text/css" title="www" rel="stylesheet" media="screen,projection" href="css/screen-fonts.css" />
<link type="text/css" title="www" rel="stylesheet" media="print"  href="css/print.css" /> 
<link type="text/css" title="www" rel="stylesheet" media="handheld"  href="css/handheld.css" />


<link type="text/css" title="www" rel="stylesheet" media="screen,projection" href="http://rrd.llabor.co.cc/css/screen.css" />
<link type="text/css" title="www" rel="stylesheet" media="screen,projection" href="http://rrd.llabor.co.cc/css/screen-fonts.css" />
<link type="text/css" title="www" rel="stylesheet" media="print"  href="http://rrd.llabor.co.cc/css/print.css" />
<link type="text/css" title="www" rel="stylesheet" media="handheld"  href="http://rrd.llabor.co.cc/css/handheld.css" />

 </head>
<body>
<pre> 
This page push content of uploaded file into virtual RRD file-system (memcache).
</pre>
Name of "pushed" object is same with Uploaded-file.
<%
try{
    // Check that we have a file upload request
    if(ServletFileUpload.isMultipartContent(request)){
%>
<h3>uploaded resources:</h3> 
<%    	
            MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(4*1024*1024); // 4 MB
            // Parse the request
            List<MemoryFileItem> items = upload.parseRequest(request); 
%>
			<table>
<%             
            for(MemoryFileItem item : items) {
                    item.flush(); 
%>
                    <tr><td>Name</td><td><a href="mem.jsp?name=<%=item.getName()%>"><%=item.getName()%></a></td></tr> 
                    <tr><td>Size </td><td><%=item.getSize()%></td></tr>
                    <tr><td>Date </td><td><%=item.getDate()%></td></tr>
                    <tr><td>FN </td><td><%= item.getFieldName()%></td></tr>
                    <tr><td>ContentType </td><td><%=item.getContentType() %></td></tr>
<%                       
                    session.setAttribute(item.getName(),item );
					String nameTmp = MemoryFileCache.getInstance("DEFAULT.BAK"). put( item  );
					System.out.println( "stored into memcache as ::["+nameTmp +"]");
                    //pm.makePersistent(item);
            }
%>
			</table>
<% 
    }else{
%>
<form  method="post" enctype="multipart/form-data">
  <p>Select CSV File for update:<br>
    <input name="Datei" type="file" size="50" maxlength="100000" accept="text/*">
    <input type="submit"/>
  </p>
</form>
<p>
this data will be available as input for rrdtool.  
</p>
<%     	
    }
} catch(FileUploadException e){ throw new IOException("Unable to handle uploaded file. "+e.getMessage()); 
} catch(Throwable e){ e.printStackTrace(response.getWriter()); }

%> 
  </body>
</html>