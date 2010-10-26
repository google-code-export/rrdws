<%@page import="cc.co.llabor.mail.Forwarder"%>
<%
try{
	Forwarder fw = new Forwarder();
	fw.emailLists(new byte[][]{
			"ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:".getBytes(),
			"ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:ASDFGHJKL:ASDFssssssssssssssssssGHJKL:".getBytes()}
	);
}catch(Throwable e){
%><%=e.getMessage()%><% e.printStackTrace(response.getWriter()); %>
<%
}
%>
