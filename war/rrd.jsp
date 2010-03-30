<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
<%@page import="org.jrobin.cmd.RrdCommander"%>

<%=1+3%>
<%
System.out.println("== Rrd4j's RRDTool commander ==");
String cmdTmp = request.getParameter("cmd");
System.out.println(cmdTmp);
RrdCommander.setRrdDbPoolUsed(false);
RrdCommander.execute(cmdTmp);
%>
<%
try{
		(new RrdGraphCmdTest()).testExecute();
	}catch(Throwable e){
		Chk.chk(e);
			e.printStackTrace();
}
%>