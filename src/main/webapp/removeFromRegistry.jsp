<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><meta http-equiv=Content-Type content="text/html;charset=utf-8">
<%@page import="ws.rrd.csv.Registry"%> 
<%@page import="net.sf.jsr107cache.Cache"%>
<%@page import="cc.co.llabor.cache.Manager"%>
<html><head><title>intro to JRRD...</title>
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
    background-color: #EEEEEE;
}
</style>

 </head>
<body bgcolor=#FFFFFF link=#006890 vlink=#003860 alink=#800000 text=#000000 topmargin="0" marginheight="0">
 <table class="data-table-3" summary="this is the table >;-)" >
<%
//removeFromRegistry.jsp provide remove ONE_ENTRY by key from common REGISTY rrd.DBs
 
 
Cache cache = Manager.getCache();
Registry reg = (Registry) cache.get("REGISTRY");

String key =  request.getParameter("rrd");
String val = reg.getDb2path().get(key);
reg.unregister(val, key);

