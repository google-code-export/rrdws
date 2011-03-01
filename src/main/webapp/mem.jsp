<%@page import="cc.co.llabor.cache.MemoryFileCache"%><%@page 
import="cc.co.llabor.cache.MemoryFileItem"%><%@page 
import="java.io.OutputStream"%><%@page 
import="org.apache.commons.fileupload.FileItem"%>
<% 
String nameTmp = request.getParameter("name");
try{
	MemoryFileItem item = MemoryFileCache.getInstance("DEFAULT.BAK"). get ( nameTmp  );
	response.setContentType(item.getContentType());//"image/svg+xml;charset=UTF-8"
	%><%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Content-Disposition", "inline;filename="+item.getName()+"");
	%><%=new String(item.get())%><%
}catch(Throwable e){	
	
	response.setStatus(501);
%><%=""+e.getMessage()%><%
e.printStackTrace(response.getWriter());
}
%>
