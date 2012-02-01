<%@page 
import="cc.co.llabor.threshold.rrd.Threshold"%><%@page 
import="java.util.List"%><%@page 
import="cc.co.llabor.threshold.AlertCaptain"%><%@page 
import="cc.co.llabor.threshold.MVELActionist"%><%
List<Threshold>  todos = AlertCaptain.getInstance().getToDo();
%><HTML>
<head><title>defined Threshoulds...</title>
<link rel="stylesheet" type="text/css" media="screen,print" href="css/table.css" /> 
<link rel="stylesheet" type="text/css" href="css/main.css" />
<link rel="stylesheet" type="text/css" media="all" href="css/screen.css" />
<link rel="stylesheet" type="text/css" media="print" href="css/print.css" />
<style>
.td-1 { 
    background-color: #CCFFFF;
}
.td-2 { 
    background-color: #FFFFCC;
}
.td-3 { 
    background-color: #EEFFEE;
}
</style>
</head>
<BODY>
<TABLE>
<%
int i= 0;
for (cc.co.llabor.threshold.rrd.Threshold todo :todos){
%>
<TR class=td-<%=i%3%>  >
	<TD>
		<%=i++ %>
	</TD>
	<TD>
		<%=todo %>
	</TD>
</TR>
<%
}
%>
</TABLE>
<A HREF="NextPage.jsp">Continue</A>
</BODY>
</HTML>
