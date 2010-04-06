<%@page import="java.util.List"%>
<%@page import="javax.jdo.Query"%>
 
<%@page import="javax.jdo.PersistenceManager"%>
<%@page import="ws.rdd.jdo.RRD_JDOHelper"%>
<%@page import="ws.rdd.jdo.Blob"%> 
<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
<%@page import="org.jrobin.cmd.RrdCommander"%>


<%="<pre>"+System.getProperties()+"</pre>"%>
 
 <form  method="post" action="/rrd.jsp?" >
 <<textarea    name="cmd"  cols="50" rows="10" ></textarea>
 <input type="submit"/>
</form>

<%
System.out.println("== Rrd4j's RRDTool commander ==");
String cmdTmp = request.getParameter("cmd");
System.out.println(cmdTmp);
cmdTmp = cmdTmp.replace("\\","\n");  

RrdCommander.setRrdDbPoolUsed(false);
RrdCommander.execute(cmdTmp);
%>
 