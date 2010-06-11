<%@page import="ws.rdd.net.UrlFetchTest"%><%
UrlFetchTest o =  new UrlFetchTest();
String urlTmp = request.getParameter("url");
 %><%=""+o.testFetchUrl(urlTmp)%>