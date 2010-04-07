<%@page import="java.util.List"%>
<%@page import="javax.jdo.Query"%>
<%@page import="javax.jdo.PersistenceManager"%>
<%@page import="ws.rdd.jdo.RRD_JDOHelper"%>
<%@page import="ws.rdd.jdo.Blob"%>
<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
<%@page import="org.jrobin.cmd.RrdCommander"%>
<%@page import="org.jrobin.svg.RrdGraphInfo"%>

<table height="100%" width="100%">
	<tr width="100%" height="64">
		<td width="100%" height="64">
			<a href="https://rrdsaas.appspot.com/rrd.jsp"  title=" RRD Java impl "> 
			<img src="Java-Logo.svg" height="64" width="64" /> </a>
		</td>
	<tr width="100%" height="64">
	</tr>

		<td width="200" >
		<form method="post">
			RddCommand:: <textarea name="cmd"  cols="60" rows="4"></textarea> 
			<input type="submit" />
		</form>
		</td>
	</tr>
	<tr >
		<td width="100%"><iframe
			src="https://rrd4j.dev.java.net/tutorial.html" width="101%"
			height="333"></iframe></td>
	</tr>
	<tr width="100%" height="333">

		<td  >
		<%
			System.out.println("== Rrd4j's RRDTool commander ==");
			String cmdTmp = request.getParameter("cmd");
			Object o = null;
			if (cmdTmp != null) {
				System.out.println(cmdTmp);
				cmdTmp = cmdTmp.replace("\\", "\n");
				RrdCommander.setRrdDbPoolUsed(false);
				o = RrdCommander.execute(cmdTmp);
				if (o instanceof org.jrobin.svg.RrdGraphInfo) {
					RrdGraphInfo oInf = (RrdGraphInfo) o;
					session.setAttribute("svg", oInf.getBytes());
				}
			}
		%> <embed src="svg.jsp" type="image/svg+xml" height="100%" width="100%" />
		<form method="post">Execution result::: <textarea
			readonly="readonly"  name="result"  cols="60" rows="4">
 <%=o%>
</textarea></form>


		</td>
	</tr>

	<tr>
		<td><embed src="Java-Logo.svg" type="image/svg+xml" width="64"
			height="64" />

		</embed> <!-- 
 <%=" "+System.getProperties()+" "%>
  --></td>
	</tr>
</table>
