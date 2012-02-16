<%@page import="java.io.FileInputStream"
%><%@page import="java.io.ByteArrayInputStream"
%><%@page  contentType="image/gif"%><%
response.setContentType("image/gif");
%><%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setHeader("Content-Disposition", "inline;filename=gif.gif");
%><%
try{
	FileInputStream fio = new  FileInputStream("gif.gif");
	byte[]buf = new byte[1023];
	for (int i=fio.read(buf);i>0;i=fio.read(buf)){
		response.getOutputStream().write(buf,0,i);
		response.getOutputStream().flush();
	}
}catch(Throwable e){ 
		e.printStackTrace(response.getWriter());
}
%>