<%@page 
import="ws.rrd.csv.Registry"%><%@page 
import="net.sf.jsr107cache.Cache"%><%@page 
import="cc.co.llabor.cache.Manager"%><%
response.setContentType("text/plain;charset=UTF-8");
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
//response.setHeader("Content-Disposition", "inline;filename=xxx.json");
%><%
// 
Cache cache = Manager.getCache();
Registry reg = (Registry) cache.get("REGISTRY");
if (null != reg){
int i=0;
String prefix = "";
%>{ "aaData": [
<%
for (String key:reg.getPath2db().keySet() ){
i++;
%> <%=prefix%>[  "<%=1+(i%3)%>" ,"<%=i %>" , "<%=reg.getPath2db().get(key)%>" ,"<%=key%>" , "aaa" ]

<%
prefix = ",";
} // for (String key:reg.getPath2db().keySet() ){
} // if (null != reg){
%>
] }