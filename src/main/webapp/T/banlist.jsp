<%@page import="com.no10x.cache.Manager"%>
<%@page import="net.sf.jsr107cache.Cache"%> 
<%  
Cache cache = Manager.getCache();
String  banlistTmp = ""+cache.get("--banlist--"); 
%>  
<!-- <%=banlistTmp%> --> 
