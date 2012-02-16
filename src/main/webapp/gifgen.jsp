<%@page import="java.io.ByteArrayInputStream"%><%@page  contentType="image/gif"%><%
response.setContentType("image/gif");
%><%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setHeader("Content-Disposition", "inline;filename=sss.gif");
%><%
 
System.out.println("== RrdWS GIF-GEN...");
try{
	Object o = null;
	String cmdTmp = request.getParameter("cmd");
	if (cmdTmp != null) {
		System.out.println(cmdTmp);
		cmdTmp = cmdTmp.replace("\\", "\n");
		o = org.jrobin.cmd.RrdCommander.execute(cmdTmp);

		if (o instanceof org.jrobin.svg.RrdGraphInfo) {
			org.jrobin.svg.RrdGraphInfo oInf = (org.jrobin.svg.RrdGraphInfo) o;
			session.setAttribute("svg", oInf.getBytes());
		}
		if (o instanceof org.jrobin.graph.RrdGraphInfo) {
			org.jrobin.graph.RrdGraphInfo oInf = (org.jrobin.graph.RrdGraphInfo) o;
			session.setAttribute("gif", oInf.getBytes());
		}
	}

	
	java.io.InputStream fio = new  java.io.ByteArrayInputStream( (byte[])session.getAttribute("gif") );
	byte[]buf = new byte[1023];
	for (int i=fio.read(buf);i>0;i=fio.read(buf)){
		response.getOutputStream().write(buf,0,i);
		response.getOutputStream().flush();
	}
}catch(Throwable e){ 
		e.printStackTrace(response.getWriter());
}
%>