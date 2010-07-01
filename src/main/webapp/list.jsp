<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><meta http-equiv=Content-Type content="text/html;charset=utf-8">
<%@page import="ws.rrd.csv.Registry"%>
<%@page import="ws.rrd.mem.MemoryFileCache"%>
<%@page import="net.sf.jsr107cache.Cache"%>
<html><head><title>intro to JRRD...</title>
<link rel="stylesheet" type="text/css" media="screen,print" href="//www.ibm.com/common/v14/table.css" /> 
<link rel="stylesheet" type="text/css" href="//www.ibm.com/common/v14/main.css" />
<link rel="stylesheet" type="text/css" media="all" href="//www.ibm.com/common/v14/screen.css" />
<link rel="stylesheet" type="text/css" media="print" href="//www.ibm.com/common/v14/print.css" />
<style> </style> </head>
<body bgcolor=#FFFFFF link=#006890 vlink=#003860 alink=#800000 text=#000000 topmargin="0" marginheight="0">
 <table class="data-table-1" summary="this is the table >;-)" >
<%
Cache cache = MemoryFileCache.getCache();
Registry reg = (Registry) cache.get("REGISTRY");
for (String key:reg.getPath2db().keySet() ){
%><tr><td><%=key %> </td><td>==</td><td> 
<%=reg.getPath2db().get(key) %></td>
<td><img src="gen.jsp?db=<%=reg.getPath2db().get(key) %>"/>
</td>
</tr>
<%
}
%>
</table>