<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%
Locale loc = Locale.getDefault();
try{
	String []lTmp = request.getParameter("locale").split("_");
	loc =  new Locale(lTmp[0] );
	loc =  new Locale(lTmp[0],lTmp[1]);
	loc =  new Locale(lTmp[0],lTmp[1],lTmp[2]);
}catch(Throwable e){e.printStackTrace();}
NumberFormat curTmp =NumberFormat .getCurrencyInstance(loc);
NumberFormat numTmp =NumberFormat .getNumberInstance(loc);
NumberFormat perTmp =NumberFormat .getPercentInstance(loc); 
%>
Locale:<%=loc %><br>
Locale:<%=loc.getCountry() %><br>
Locale:<%=loc.getDisplayCountry() %><br>
Locale:<%=loc.getDisplayCountry() %><br>
Locale:<%=loc.getLanguage() %><br>
Locale:<%=loc.getVariant() %><br>
Locale:<%=loc.getVariant() %><br>
<table><tr><td>
<% int i=0;%>
<%for (Locale lTmp : Locale .getAvailableLocales()){%>
<%= (i%15==0)?"</td></tr><tr><td>":""%>
<a href="?locale=<%= lTmp %>"><%= lTmp %> 
<img src="iso3166-1/<%=lTmp.getCountry().toLowerCase()%>.png"> </a>
<img src="iso3166-1/<%=lTmp.getLanguage().toLowerCase()%>.png"> </a>
</td><td>
 
<%
i++;
} %>
</td></tr></table>
getCurrencyInstance::<%=curTmp .format(12345678.90) %><br>
getNumberInstance::<%=numTmp .format(12345678.90) %><br>
getPercentInstance::<%=perTmp .format(12345678.90) %><br>

SimpleDateFormat.FULL::<%=SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL,loc).format(new Date(0))  %><br>
SimpleDateFormat.LONG::<%=SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,loc).format(new Date(0))  %><br>
SimpleDateFormat.MEDIUM::<%=SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM,loc).format(new Date(0))  %><br>
SimpleDateFormat.SHORT::<%=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,loc).format(new Date(0))  %><br>

SimpleDateFormat.FULL::<%=SimpleDateFormat.getTimeInstance(SimpleDateFormat.FULL,loc).format(new Date(0))  %><br>
SimpleDateFormat.LONG::<%=SimpleDateFormat.getTimeInstance(SimpleDateFormat.LONG,loc).format(new Date(0))  %><br>
SimpleDateFormat.MEDIUM::<%=SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM,loc).format(new Date(0))  %><br>
SimpleDateFormat.SHORT::<%=SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT,loc).format(new Date(0))  %><br>

