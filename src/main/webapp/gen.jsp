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
long lastMod = imgTmp .lastModified() ;
boolean isExpired = imgTmp .exists() && (lastMod + 1000*60*5) < System.currentTimeMillis() ;

if (!imgTmp .exists()  || isExpired){
	System.out.println(""+lastMod+"  1@@@@@@@@ " +(lastMod -System.currentTimeMillis() ) +" isExpired == "+isExpired);
	RrdCommander.execute(cmdTmp);
}else{
	//System.out.println("till update :"+ (imgTmp .lastModified() - System.currentTimeMillis() ) +" ms.");
}
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
//http://stackoverflow.com/questions/1930158/how-to-parse-date-from-http-last-modified-header
//String dateString = "Wed, 09 Apr 2008 23:55:38 GMT";
//SimpleDateFormat formatTmp = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
//Date d = format.parse(dateString);
 
response.setDateHeader ("Expires", lastMod+ 1000*60*5); //prevents caching at the proxy server
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