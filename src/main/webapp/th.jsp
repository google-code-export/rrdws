<%@page import="cc.co.llabor.threshold.rrd.Threshold"%>
<%@page import="java.util.List"%>
<%@page import="cc.co.llabor.threshold.AlertCaptain"%>
<%@page import="cc.co.llabor.threshold.MVELActionist"%>
<%
 
List<Threshold>  todos = AlertCaptain.getInstance().getToDo();

%>
<HTML>
<BODY>
<%
int i= 0;
for (cc.co.llabor.threshold.rrd.Threshold todo :todos){
%>
<%=i++ %>
<%
}
%>
<A HREF="NextPage.jsp">Continue</A>
</BODY>
</HTML>
