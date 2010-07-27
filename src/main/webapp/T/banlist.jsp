<%@page import="net.sf.jsr107cache.Cache"%>
<%@page import="ws.rrd.mem.MemoryFileCache"%>
<%  
Cache cache = MemoryFileCache.getCache();
String  banlistTmp = ""+cache.get("--banlist--"); 
%>  
<!-- <%=banlistTmp%> --> 
