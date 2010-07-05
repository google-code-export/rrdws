<%@page import="org.apache.http.Header"%><%@page import="java.io.InputStream"%><%@page import="java.io.OutputStream"%>
<%@page import="org.apache.http.message.BasicHttpResponse"%>
<%@page import="org.apache.http.HttpEntity"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="ws.rdd.net.UrlFetchTest"%><%
UrlFetchTest o =  new UrlFetchTest();
String urlTmp = request.getParameter("url");
String refTmp =  "http://localhost:8888/get.jsp"+"?url="+"http://www.fiducia.de/"; 
HttpResponse respTmp =o.fetchResp(urlTmp);
HttpEntity eTmp = ((BasicHttpResponse )respTmp).getEntity();
Header ctTmp = eTmp.getContentType();
response.setContentType(ctTmp.getValue());
InputStream contentTmp = eTmp.getContent();
OutputStream  outTmp  = response.getOutputStream();
if(ctTmp.getValue().indexOf("html") >= 0){
	// load all data for HTML ,and replace header
	int sizeTmp = Math.max(  (int)eTmp.getContentLength(), contentTmp.available());
	byte buf[] = new byte[sizeTmp];
	int readedTmp = contentTmp.read(buf);
	String wData = new String( buf,0,readedTmp ) ;
	wData = wData.replace( "<head>", "<HEAD><base href=\""+refTmp+"\">"  );
	wData = wData.replace( "</head>", "</HEAD>"  );
	outTmp.write(wData.getBytes());
	outTmp.close();
}else{
 	// plain copy 
	int sizeTmp = Math.max(  (int)eTmp.getContentLength(), contentTmp.available());
	byte buf[] = new byte[sizeTmp];
	int readedTmp = contentTmp.read(buf);
	outTmp.write(buf);
	outTmp.close();
 	
}
%>