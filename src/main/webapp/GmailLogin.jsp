<%@page import="ws.rdd.net.GmailLogin"%>
<%
GmailLogin.main(new String[]{request.getParameter("url")});
%>