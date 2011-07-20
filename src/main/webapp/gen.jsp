<%@page import="java.io.File"%><%@page import="java.io.OutputStream"%><%@page import="org.jrobin.cmd.RrdCommander"%><%@page import="java.io.FileInputStream"%><%@page import="java.io.ByteArrayInputStream"%><%@page  contentType="image/gif"%><%
response.setContentType("image/gif");
%><%
// init tmDIR
try{
	File tmpDir = new File("./img.tmp/");
	tmpDir.mkdirs();
}catch(Throwable e){ 
	e.printStackTrace(response.getWriter());
}
// gen.jsp generates gif.preview by RRD-name. 
// known usage: list.jsp
String dbParName = request.getParameter("db");
String dbName = dbParName==null?"X-1979395149":dbParName;
String EXT = ".rrd";
dbName = dbName.toLowerCase().indexOf(EXT)>0?dbName.substring(0,dbName.length()-EXT.length()):dbName; 
String _h = " 32 ";
String _w = " 64 ";
String fileNameTmp = "./img.tmp/"+dbName+".gif";
String cmdTmp = "rrdtool graph "+ fileNameTmp +" --color BACK#11111111 --only-graph  -o -h "+ _h +" -w  "+_w+" --start=end-1week  DEF:dbdata="+dbName+".rrd:data:AVERAGE  LINE2:dbdata#44EE4499  LINE1:dbdata#003300AA ";
java.io.File imgTmp = new java.io.File(fileNameTmp);
boolean isExpired = imgTmp .exists() && (imgTmp .lastModified() + 1000*60*5) < System.currentTimeMillis() ;
if (!imgTmp .exists()  || isExpired){
	System.out.println(""+imgTmp .lastModified()+"  1@@@@@@@@ "+(imgTmp .lastModified()  -System.currentTimeMillis() ) +" isExpired == "+isExpired);
	RrdCommander.execute(cmdTmp);
}else{
	//System.out.println("till update :"+ (imgTmp .lastModified() - System.currentTimeMillis() ) +" ms.");
}
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setHeader("Content-Disposition", "inline;filename="+dbName+".gif");
%><%
try{
	FileInputStream fio = new  FileInputStream(fileNameTmp);
	byte[]buf = new byte[1023];
	OutputStream respOutTmp = response.getOutputStream();
	for (int i=fio.read(buf);i>0;i=fio.read(buf)){
		respOutTmp.write(buf,0,i);
		respOutTmp.flush();
	}
	fio.close();
}catch(Throwable e){ 
		e.printStackTrace(response.getWriter());
}
%>