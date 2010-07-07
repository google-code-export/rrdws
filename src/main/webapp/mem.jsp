<%@page import="ws.rrd.mem.MemoryFileItem"%>
<%@page import="java.io.OutputStream"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="ws.rrd.mem.MemoryFileCache"%><%
/**
deliver stored by <b>push.jsp</b> data via name: 
<a>http://x.x.x.x/mem.jsp?name=test.svg</a>
*/
String nameTmp = request.getParameter("name");
MemoryFileItem item = MemoryFileCache. get ( nameTmp  );
response.setContentType(item.getContentType());//"image/svg+xml;charset=UTF-8"
%><%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setHeader("Content-Disposition", "inline;filename="+item.getName()+"");
%><%
try{
	OutputStream outTmp = response.getOutputStream();
	outTmp.write(item.get());
	outTmp.flush();
	outTmp.close();
}catch(Throwable e){ 
		e.printStackTrace(response.getWriter());
}
%>