<%@page import="java.io.OutputStream"%>
<%@page import="org.apache.commons.fileupload.FileItem"%><%@page import="ws.rrd.Chk"%> 
<%@page import="ws.rrd.MemoryFileCache"%><%
String nameTmp = request.getParameter("name");
FileItem item = MemoryFileCache. get ( nameTmp  );
response.setContentType("image/svg+xml;charset=UTF-8");
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
		Chk.chk(e);
		e.printStackTrace();
}
%>