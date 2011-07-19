<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <div class='corner' id='angle'>
        <a href="/corner" target="_blank"><IMG SRC="img/btn_track.gif">rrd4u</a>
    </div>
    <div class='cornerA' id='cornerA'>
        <a href="/corner_a" target="_blank"><IMG SRC="img/btn_prev.gif">A</a>
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
    <title>rrd4 u</title>
    <link rel="Stylesheet" href="css/corner.css" type="text/css">
    
</head>
<body  onLoad="return 1;"> 
<%@page import="org.jrobin.cmd.RrdCommander"%>
<%@page import="org.jrobin.svg.RrdGraphInfo"%>
<table > 
	<tr   >
		<td width="100" height="64">
						<a href="https://rrdsaas.appspot.com/rrd.jsp"  title=" RRD Java impl ">
						RRDSAAS <%=System.currentTimeMillis() %>
	
		</td>
					<td width="10%" height="64">
						<img src="JavaLogo.svg" height="64" width="64" alt="javalogo as img.svg" /> 
					</td><td width="10%" height="64">
						EMBED :
						<embed src="JavaLogo.svg" type="image/svg+xml" height="64" width="64"  scale=true alt="javalogo as embed.svg" />
					</td><td width="10%" height="64">	
						EMBED+p :
						<embed src="JavaLogo.svg" height="64" width="64"  type="image/svg+xml" scale=true alt="javalogo as embed+plug.svg"  pluginspage="http://www.adobe.com/svg/viewer/install/" /> 
					</td><td width="10%" height="64">	
						OBJ :
						<object data="JavaLogo.svg" width="300" height="100" type="image/svg+xml" alt="javalogo as object+plug.svg" codebase="http://www.adobe.com/svg/viewer/install/" />
						</a>
					</td><td width="10%" height="64">	
						IMG :
						<IMG src="gif.jsp" height="64" width="64"   />
					</td><td width="10%" height="64">	
						Tomcat RESULT Image:
						<IMG src="speed.gif" height="64" width="164"  alt="tomcat/jee rrdoutput" />
					</td><td width="10%" height="64">	
						GAE RESULT Image:
						<embed src="svg.jsp" type="image/svg+xml" height="100%" width="100%"  alt="gae rrdoutput" />
					</td> 		
	</tr>	
	
</table>
<!-- TUTORIAL --->
<table width="100%" >
	<tr width="100%" height="64">
		<td>
			<table height="100%" width="100%">
				<tr width="100%" height="64">
					<td>
						<iframe src="man/man.html" width="83%"  height="500"   >
						a lot of thanks to Alex van den Bogaerdt for this very kindly checked documentation. Unfortunately this page is dropped from original https://rrd4j.dev.java.net/tutorial.html...
						... but __fortunately___ I backuped it! :)
						</iframe>
					</td>	
			</tr>				
			</table>	 
		</td>	
	 
		<td  >
			<table height="100%" width="100%">
				<tr width="100%" height="64">
					<td>Execution result:</td>
				</tr>
				<tr>
					<td height="100%">
		<%
			System.out.println("== RrdWS/RRDTool commander exec...");
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
		%> 	
						<textarea> <%=o%> </textarea>
					</td>	
				</tr>		
				<tr width="100%" height="64">
					<td>LastCMD:</td>
				</tr>
				<tr width="100%" height="64">
					<td>
						<textarea> <%=cmdTmp%> </textarea>
					</td>
				</tr>
				
			</table>		
		</td>	 		
	</tr>
</table>	
<table>
	<tr width="100%"  > 
			<td  ><form method="post">
<%
String testCOLOR = "FFFFFF000000CCAA";
testCOLOR  = testCOLOR   .substring(  (int)(System.currentTimeMillis()%10));
testCOLOR   = testCOLOR  .substring(0,6);
String testCMD = " rrdtool graph speed.gif  --start 920804400 --end 920808000  DEF:myspeed=test.rrd:speed:AVERAGE  LINE"+(((int)(System.currentTimeMillis()%3)+1))+":myspeed#"+testCOLOR;
%> <textarea name="cmd"  cols="160" rows="4" value="<%=testCMD%>"><%=testCMD%></textarea>
			<<<-- if u don't know what to do -- just try the default action - press the button ;)  
			<input type="submit" />
		</form>
		</td>
					<td>  <iframe width="200" height="200" src="js/clock.html"></iframe>
			</td>
	</tr> 

	<tr>
		<td> 
		</embed> <!-- 
 <%=" "+System.getProperties()+" "%>
  --></td>
	</tr>
</table>
