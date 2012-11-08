<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="ws.rrd.logback.ServletListener"%>
<%@page import="org.jrobin.mrtg.server.IfDsicoverer"%>
<%
ThreadGroup ownTG = ServletListener.getDefaultThreadGroup();
String host = "127.0.0.1:161"; 
try{host = request.getParameter("host").toString();}catch(Exception e){}
String community = "public";
try{community = request.getParameter("community").toString();}catch(Exception e){}
String numericOid = "1.3.6.1.4.1.42.2.145.3.163.1"; // standart sun start-OID
try{numericOid = request.getParameter("numericOid").toString();}catch(Exception e){}
String ifDescr = "jvmMgtMIB";
//String userName = "admin";
//String pwd = "TopS3creT";
// refresh-blocking
String reqId = ""+session.getAttribute("reqId" );
if (reqId.equals( request.getParameter("reqId")) ){
	IfDsicoverer.startDiscoverer(ownTG, host, community, numericOid, ifDescr);
	session.setAttribute("reqId",null );
}

reqId = ""+Math.random()+":"+System.currentTimeMillis();
session.setAttribute("reqId",reqId );

%><html>
<body onload="document.forms[0].host.focus()"> 
<form>
	<input type="text" name="host"  size="100" value="<%=StringEscapeUtils.escapeHtml( ""+host)%>">
	<input type="text" name="community"  size="100" value="<%=StringEscapeUtils.escapeHtml( ""+community)%>">
	<input type="text" name="numericOid"  size="100" enabled="false" value="<%=StringEscapeUtils.escapeHtml( ""+numericOid)%>">
	<input type="text" name="ifDescr"  size="100" enabled="false" value="<%=StringEscapeUtils.escapeHtml( ""+ifDescr)%>">
	<input type="text" name="reqId"  size="100" enabled="false" value="<%=StringEscapeUtils.escapeHtml( ""+reqId)%>">
	
	<input type="submit">
</form>

<h3>active discovering-pool</h3>
<%
String dList[] = IfDsicoverer.listDiscoverer();
for (String l:dList){
%><%=l%><br>
<%
}
%>
</body>
</html>