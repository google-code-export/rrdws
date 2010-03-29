<%@page import="org.rrd4j.cmd.RrdCommander"%>
<%=1+3%>
<%
System.out.println("== Rrd4j's RRDTool commander ==");
String cmdTmp = request.getParameter("cmd");
System.out.println(cmdTmp);
RrdCommander.execute(cmdTmp);
%>