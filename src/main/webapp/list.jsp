<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><meta http-equiv=Content-Type content="text/html;charset=utf-8">
<%@page import="ws.rrd.csv.Registry"%> 
<%@page import="net.sf.jsr107cache.Cache"%>
<%@page import="cc.co.llabor.cache.Manager"%>
<html><head><title>intro to JRRD...</title>
<link rel="stylesheet" type="text/css" media="screen,print" href="common/v14/table.css" /> 
<link rel="stylesheet" type="text/css" href="common/v14/main.css" />
<link rel="stylesheet" type="text/css" media="all" href="common/v14/screen.css" />
<link rel="stylesheet" type="text/css" media="print" href="/common/v14/print.css" />
<style> </style> </head>
<body bgcolor=#FFFFFF link=#006890 vlink=#003860 alink=#800000 text=#000000 topmargin="0" marginheight="0">
 <table class="data-table-1" summary="this is the table >;-)" >
<%
//list.jsp provide shor preview of all stored in REGISTY rrd.DBs
 
Cache cache = Manager.getCache();
Registry reg = (Registry) cache.get("REGISTRY");
if (null != reg){
int i=0;
for (String key:reg.getPath2db().keySet() ){
i++;
%>

<tr>
<td><%=i %></td>
	<td>
		<a href="xwin.jsp?db=<%=reg.getPath2db().get(key)%>&_t='<%=key%>'">
		<%=key %>
	</td>
<td>==</td>
<td><%=reg.getPath2db().get(key)%></td>
<td>
	<a href="xgen.jsp?db=<%=reg.getPath2db().get(key)%>&_h=200&_w=320&_start=end-1day">
		<img src="gen.jsp?db=<%=reg.getPath2db().get(key) %>"/>.
	</a>	
</td>
</tr>
<%
}
}
%>
   
</table>