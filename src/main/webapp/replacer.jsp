<%@page 
import="cc.co.llabor.cache.replace.ReplaceStore"%><%@page 
import="ws.rrd.server.LServlet"%><%
 	
	if ("true".equals( request.getParameter(LServlet.REPLACER))){
 		session.setAttribute(LServlet.REPLACER,"true");
 		ReplaceStore.getInstanse().setCollectParents(true);
	}
	if ("false".equals( request.getParameter(LServlet.REPLACER))){
 		session.setAttribute(LServlet.REPLACER,"false");
 		ReplaceStore.getInstanse().setCollectParents(false);
	}
 	String isReplacer = ""+ReplaceStore.getInstanse().isCollectParents() ;	
	String imgName = "true".equals(isReplacer) ? "r1.jpeg" : "r0.jpeg";
	String contraTmp = "true".equals(isReplacer)?"false":"true";
%>

<a href="replacer.jsp?<%=LServlet.REPLACER%>=<%=contraTmp%>">
<img 	
	height="32px" width="32px"  
	alt="<%="CollectParents="+isReplacer%>" 
	src="<%=LServlet.SwapServletUrl %>../img/<%=imgName%>">
</a>