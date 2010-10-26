<%@page import="ws.rrd.server.LServlet"%><% 
	final String KEY = LServlet.TRACER;
	if ("true".equals( request.getParameter(KEY))){
 		session.setAttribute(KEY,"true");
 		LServlet.TRACE = true;
	}
	if ("false".equals( request.getParameter(KEY))){
 		session.setAttribute(KEY,"false");
 		LServlet.TRACE = false;
	}
 	String isTMP = ""+LServlet.TRACE ;	
	String imgName = "true".equals(isTMP) ? "tr1.jpeg" : "tr0.jpeg";
	String contraTmp = "true".equals(isTMP)?"false":"true";
%> 
<a href="tracer.jsp?<%=KEY%>=<%=contraTmp%>">
<img 	
	height="32px" width="32px"  
	alt="<%=KEY+"="+isTMP%>" 
	src="<%=LServlet.SwapServletUrl %>../img/<%=imgName%>">
</a>