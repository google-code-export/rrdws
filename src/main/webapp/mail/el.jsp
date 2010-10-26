<%@page import="cc.co.llabor.mail.Forwarder"%>
<%
try{
	Forwarder fw = new Forwarder();
	fw.emailList("ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:".getBytes());
}catch(Throwable e){
%><%=e.getMessage()%><% e.printStackTrace(response.getWriter()); %>
<%
}
%>
