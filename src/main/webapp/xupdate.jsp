<%@page 
	import="org.apache.commons.fileupload.servlet.ServletFileUpload"%><%@page 
	import="java.util.List"%><%@page 
	import="org.apache.commons.fileupload.FileUploadException"%><%@page 
	import="java.io.IOException"%><%@page 
	import="ws.rrd.csv.RrdUpdateAction"%><%@page 
	import="cc.co.llabor.cache.MemoryFileItemFactory"%><%@page 
	import="cc.co.llabor.cache.MemoryFileItem"%><%@page 
	import="cc.co.llabor.cache.MemoryFileCache"%><%@page 
	import="org.xml.sax.XMLReader"%><%@page 
	import="org.xml.sax.helpers.XMLReaderFactory"%><%@page 
	import="org.xml.sax.ContentHandler"%><%@page 
	import="org.xml.sax.InputSource"%><%@page 
	import="ws.rrd.xpath.XPathContentHandler"%><?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html 
	xmlns="http://www.w3.org/1999/xhtml">
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
			%> <table><tr><th> </th><th>Name</th><th>Size</th><th>Date</th><th>FN</th><th>ContentType</th><th>parce output</th></tr> <%
			int rowCounterTmp = 0;
            for(MemoryFileItem item : items) {
            	%> <tr> <%
                    item.flush(); 
                    %><td>    <%=rowCounterTmp++%>
					</td><td> <%=item.getName()%>
                    </td><td> <%=item.getSize()%>
                    </td><td> <%=item.getDate()%>
                    </td><td> <%=item.getFieldName()%>
                    </td><td> <%=item.getContentType()%>
                    </td><%
					session.setAttribute(item.getName(),item );                
                    nameTmp = MemoryFileCache.getInstance("DEFAULT.BAK"). put( item  );
					System.out.println( "XUPDATE.jsp:stored into memcache as ::["+nameTmp +"]");
					// process last XML-data
					XMLReader reader = XMLReaderFactory.createXMLReader();
					RrdUpdateAction rrdActioner = new  RrdUpdateAction();
					//Action rrdActioner = new  SystemOutPrintlnAction();
					ContentHandler handler = new XPathContentHandler(rrdActioner);
					java.io.ByteArrayOutputStream bufOut = new java.io.ByteArrayOutputStream ();
					java.io.PrintWriter parceOutputWriter = new java.io.PrintWriter(bufOut, true) ;
					((XPathContentHandler)handler).setOut(parceOutputWriter );
					reader.setContentHandler(handler );
					InputSource in = new InputSource(item.getInputStream());
					try{
						reader.parse(in); 
					}catch(Throwable e){
						e.printStackTrace(parceOutputWriter);
					}
					bufOut.flush();
					bufOut.close();
					%><td><%="<pre>"+bufOut.toString()+"</pre>"%></td><%
					
            	%> </tr> <%
				}
			%> <tr><td><a href="xupdate2.jsp">__back___</a></td></tr></table> <%
    }else{
%>
<body>
<form  method="post" enctype="multipart/form-data">
   Select XML(c) File for update:<br>
    <br><input name="Datei1" type="file" size="50" maxlength="100000" accept="text/*"/>
    <br><input name="Datei2" type="file" size="50" maxlength="100000" accept="text/*"/>
    <br><input name="Datei3" type="file" size="50" maxlength="100000" accept="text/*"/>
    <br><input name="Datei4" type="file" size="50" maxlength="100000" accept="text/*"/>
    <br><input name="Datei5" type="file" size="50" maxlength="100000" accept="text/*"/>
    <br><input name="Datei6" type="file" size="50" maxlength="100000" accept="text/*"/>
    <br><input name="Datei7" type="file" size="50" maxlength="100000" accept="text/*"/>
    <input type="submit"/>
   
</form>
<%     	
    }
} catch(FileUploadException e){ throw new IOException("Unable to handle uploaded file"); 
} catch(Throwable e){ e.printStackTrace(response.getWriter()); }

%> 
  </body>
</html>