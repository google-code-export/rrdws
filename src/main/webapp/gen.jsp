<%@page import="org.jrobin.cmd.RrdCommander"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.ByteArrayInputStream"%><%@page  contentType="image/gif"%><%
response.setContentType("image/gif");
%><%
String dbParName = request.getParameter("db");
String dbName = dbParName==null?"X-1979395149":dbParName;
String EXT = ".rrd";
dbName = dbName.toLowerCase().indexOf(EXT)>0?dbName.substring(0,dbName.length()-EXT.length()):dbName; 
String cmdTmp = "rrdtool graph ./img.tmp/"+dbName+".gif --color BACK#11111111 --only-graph  -o -h 32 -w 64 --start=end-1week  DEF:dbdata="+dbName+".rrd:data:AVERAGE  LINE2:dbdata#44EE4499  LINE1:dbdata#003300AA ";
RrdCommander.execute(cmdTmp);
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setHeader("Content-Disposition", "inline;filename="+dbName+".gif");
%><%
try{
	FileInputStream fio = new  FileInputStream("./img.tmp/"+dbName+".gif");
	byte[]buf = new byte[1023];
	for (int i=fio.read(buf);i>0;i=fio.read(buf)){
		response.getOutputStream().write(buf,0,i);
		response.getOutputStream().flush();
	}
}catch(Throwable e){ 
		e.printStackTrace(response.getWriter());
}
%>