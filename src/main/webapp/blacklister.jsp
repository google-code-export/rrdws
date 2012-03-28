<%@page import="ws.rrd.server.LServlet"%><%
 	
	if ("true".equals( request.getParameter(LServlet.BLACKLISTER)))
 		session.setAttribute(LServlet.BEAUTIFY,"true");	
	if ("false".equals( request.getParameter(LServlet.BLACKLISTER)))
 		session.setAttribute(LServlet.BEAUTIFY,"false");	
 	String isBeautify = ""+session.getAttribute(LServlet.BLACKLISTER);	
	String imgName = "true".equals(isBeautify) ? "blacklON.png" : "blacklOFF.png";
	String contraTmp = "true".equals(isBeautify)?"false":"true";
%>
<a href="beauty.jsp?<%=LServlet.BEAUTIFY%>=<%=contraTmp%>">
<img 	height="64px" width="64px"  src="<%=LServlet.SwapServletUrl %>../img/<%=imgName%>">
</a>