<%@page import="ws.rdd.jdo.RRD_JDOHelper"%>
<%@page import="ws.rdd.jdo.Blob"%> 
<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
<%@page import="org.jrobin.cmd.RrdCommander"%>


<%=" 111"+System.getProperties()%>
 
<% 
Blob firstTmp = new Blob( "1st" );

try {
	RRD_JDOHelper.getInstance().getPm().makePersistent(firstTmp);
} finally {
	out.append("!!!!!!!!!!!!!!!@!");
    //pm.close();
} 
%>
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