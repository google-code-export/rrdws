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
if (i>100)break;
String iTmp =reg.getPath2db().get(key);
String titleTmp = "`" + key +"`";
String aVal = "xwin.jsp?db="+ iTmp+"&_t="+titleTmp;
String aTxt = ""+(i%3);
String imgSrc = "gen.jsp?db="+ iTmp;
%> <%=prefix%>[ "<%=i %>" , "<a target='_blank' href='<%=aVal%>'><%=aTxt%></a>" , "<%=iTmp%>" ,"<a target='_blank' href='<%=aVal%>'><%=key%></a>" , "<img src='<%=imgSrc%>'/>" ]

<%
prefix = ",";
} // for (String key:reg.getPath2db().keySet() ){
} // if (null != reg){
%>
] }