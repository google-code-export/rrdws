<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%
Locale loc = Locale.getDefault();
try{
	loc =  new Locale  (request.getParameter("locale"));
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
 <%= lTmp %></td><td>
 <%= (i%15==0)?"</td></tr><tr><td>":""%>
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

