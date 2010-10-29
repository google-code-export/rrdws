<%@page import="ws.rrd.server.LServlet"%><% 
	final String KEY = "COOKER";
	if ("true".equals( request.getParameter(KEY))){
 		session.setAttribute(KEY,"true"); 
	}
	if ("false".equals( request.getParameter(KEY))){
 		session.setAttribute(KEY,"false"); 
	}
 	String isTMP = ""+LServlet.TRACE ;	
	String imgName = "true".equals(isTMP) ? KEY.toLowerCase()+"1.jpeg" : KEY.toLowerCase()+"0.jpeg";
	String contraTmp = "true".equals(isTMP)?"false":"true";
%> 
<a href="<%=KEY.toLowerCase()%>.jsp?<%=KEY%>=<%=contraTmp%>">
<img 	
	height="32px" width="32px"  
	alt="<%=KEY+"="+isTMP%>" 
	src="<%=LServlet.SwapServletUrl %>../img/<%=imgName%>">
</a>