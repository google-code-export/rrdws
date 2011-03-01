
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <div class='corner' id='angle'>
        <a href="/corner" target="_blank"><IMG SRC="img/btn_track.gif">rrd4u</a>
    </div>
    <div class='cornerA' id='cornerA'>
        <a href="/corner_a" target="_blank"><IMG SRC="img/btn_prev.gif">A
        	<iframe src="iframes/cmd.jsp">no iframe support!</iframe> 
        </a>
    </div>    
    <div class='cornerB' id='cornerB'>
        <a href="/corner_b" target="_blank"><IMG SRC="img/btn_sharethis.gif">b</a>
    </div>    
    <div class='cornerC' id='cornerC'>
        <a href="/corner_c" target="_blank"><IMG SRC="img/btn_memories.gif">.C</a>
    </div>    
    <div class='cornerD' id='cornerD'>
        <a href="/corner_d" target="_blank">d.<IMG SRC="img/button-flag.gif"></a>
    </div>
<html >
<head>
    <title>rrd4u</title>
    <link rel="Stylesheet" href="css/corner.css" type="text/css">
    
</head>
<body  onLoad="return 1;"> 
<%@page import="org.jrobin.cmd.RrdCommander"%>
<%@page import="org.jrobin.svg.RrdGraphInfo"%>

<table height="100%" width="100%">
	<tr width="100%" height="64">
		<td width="100%" height="64">
			<a href="https://rrdsaas.appspot.com/rrd.jsp"  title=" RRD Java impl ">
			RRDSAAS <%=System.currentTimeMillis() %> asdfd
			<img src="JavaLogo.svg" height="64" width="64" /> 
			<embed src="JavaLogo.svg" type="image/svg+xml" height="64" width="64" >
				</embed> 
				<embed src="JavaLogo.svg" height="64" width="64"  type="image/svg+xml"  pluginspage="http://www.adobe.com/svg/viewer/install/" /> 
				<object data="JavaLogo.svg" width="300" height="100" type="image/svg+xml" codebase="http://www.adobe.com/svg/viewer/install/" />
			</a>
			<IMG src="gif.jsp" height="64" width="64"   />
			<IMG src="speed.gif" height="64" width="164"   />
			
			
			
		</td>
	<tr width="100%" height="64">
	</tr>
	<tr>
		<td width="200" >
		<form method="post">
<%
String testCOLOR = "FFFFFF000000CCAA";
testCOLOR  = testCOLOR   .substring(  (int)(System.currentTimeMillis()%10));
testCOLOR   = testCOLOR  .substring(0,6);
String testCMD = " rrdtool graph speed.gif  --start 920804400 --end 920808000  DEF:myspeed=test.rrd:speed:AVERAGE  LINE"+(((int)(System.currentTimeMillis()%3)+1))+":myspeed#"+testCOLOR;
%>		
			RddCommand:: <textarea name="cmd"  cols="160" rows="4" value="<%=testCMD%>"><%=testCMD%></textarea>
			<<<-- if u don't know what to do -- just try the default action - press the button ;)  
			<input type="submit" />
		</form>
		</td>
	</tr>
	<tr >
		<td width="100%">
			<iframe src="https://rrd4j.dev.java.net/tutorial.html" width="93%" height="133">
			</iframe>
		</td>
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
				//RrdCommander.setRrdDbPoolUsed(false);
				o = RrdCommander.execute(cmdTmp);
				if (o instanceof org.jrobin.svg.RrdGraphInfo) {
					RrdGraphInfo oInf = (RrdGraphInfo) o;
					session.setAttribute("svg", oInf.getBytes());
				}
			}
		%> 	<embed src="svg.jsp" type="image/svg+xml" height="100%" width="100%" />
		
		<form method="post">Execution result::: <textarea
			readonly="readonly"  name="result"  cols="60" rows="4">
 <%=o%>
</textarea></form>


		</td>
	</tr>

	<tr>
		<td> 

	<!-- 
 <%=" "+System.getProperties()+" "%>
  --></td>
	</tr>
</table>
