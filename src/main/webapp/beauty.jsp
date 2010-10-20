<%@page import="ws.rrd.server.LServlet"%><%
 	
	if ("true".equals( request.getParameter(LServlet.BEAUTIFY)))
 		session.setAttribute(LServlet.BEAUTIFY,"true");	
	if ("false".equals( request.getParameter(LServlet.BEAUTIFY)))
 		session.setAttribute(LServlet.BEAUTIFY,"false");	
 	String isBeautify = ""+session.getAttribute(LServlet.BEAUTIFY);	
	String imgName = "true".equals(isBeautify) ? "beauty.jpeg" : "girl.jpeg";
	String contraTmp = "true".equals(isBeautify)?"false":"true";
%>
<a href="beauty.jsp?<%=LServlet.BEAUTIFY%>=<%=contraTmp%>">
<img 	height="64px" width="64px"  src="<%=LServlet.SwapServletUrl %>../img/<%=imgName%>">
</a>