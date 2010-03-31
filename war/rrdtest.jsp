<%@page import="org.jrobin.cmd.RrdSVGCmdTest"%>
<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
 
<%
try{
		if (1==1)(new RrdSVGCmdTest()).testExecute();
	}catch(Throwable e){
		Chk.chk(e);
		e.printStackTrace();
}
%>