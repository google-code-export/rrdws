<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
 
<%
try{
		if (1==1)(new RrdGraphCmdTest()).testExecute();
	}catch(Throwable e){
		Chk.chk(e);
		e.printStackTrace();
}
%>