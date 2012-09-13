<%@page import="ws.rrd.server.LServlet"%><%
 	String MY_CONST = LServlet.BLACKLISTER;
	if ("true".equals( request.getParameter(MY_CONST)))
 		session.setAttribute(MY_CONST,"true");	
	if ("false".equals( request.getParameter(MY_CONST)))
 		session.setAttribute(MY_CONST,"false");	
 	String isTHE = ""+session.getAttribute(MY_CONST);	
	String imgName = "true".equals(isTHE) ? "blacklON.png" : "blacklOFF.png";
	String contraTmp = "true".equals(isTHE)?"false":"true";
%>
<a href="beauty.jsp?<%=MY_CONST%>=<%=contraTmp%>">
<img 	height="64px" width="64px"  src="<%=LServlet.SwapServletUrl %>../img/<%=imgName%>">
</a>