<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page import="java.util.Properties"%>
<%@page import="cc.co.llabor.threshold.rrd.Threshold"%>
<%@page import="cc.co.llabor.threshold.AlertCaptain"%>
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
<%@page 
	import="ws.rrd.csv.Registry"%><%@page 
	import="net.sf.jsr107cache.Cache"%><%@page 
	import="cc.co.llabor.cache.Manager"%><html><head><title>Alerts@JRRD</title>
<body>
<%
	
	String sT = "";
	try{
		sT = request.getParameter("textVal");
		System.out.println("IN:::"+sT);
		
	}catch(Exception e){}
	for (Threshold theT: AlertCaptain.getInstance().list()){
		Properties p = AlertCaptain.toProperties(theT);
		sT +=theT+"\n"+p+"\n\n";
		
	}
%><form method="POST">
<table  width="1100"><tr  width="1100"><td width="1100">
<textarea name="textVal" name="textValue" width="1100" rows="11" cols="111">
<%=sT %>
</textarea>
</td></tr>
<tr><td>
<input type="submit"/>
</td></tr>
</table></form>

</body>
</html>
  