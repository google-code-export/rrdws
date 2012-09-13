<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >

<head>    
    <link rel="RRDWS icon" href="favicon.ico" type="image/ico">
	<div class='corner' id='angle'>
        <a href="/corner" target="_blank"><IMG SRC="img/btn_track.gif">rrd4u</a>
    </div>
    <div class='cornerA' id='cornerA'>
        <a href="/corner_a" target="_blank"><IMG SRC="img/btn_prev.gif">A</a>
    </div>    
    <div class='cornerB' id='cornerB'>
        <a href="/corner_b" target="_blank"><IMG SRC="img/btn_sharethis.gif">b</a>
    </div>    

    <div class='cornerD' id='cornerD'>
        <a href="/corner_d" target="_blank">d.<IMG SRC="img/button-flag.gif"></a>
    </div>

    <title>rrd4 u</title>
    <link rel="apple-touch-icon-precomposed" href="/security/icons/apple-touch-icon-57x57-precomposed.png" />
	<link rel="apple-touch-icon-precomposed" sizes="114x114" href="/security/icons/apple-touch-icon-114x114-precomposed.png" />
	<link rel="apple-touch-icon-precomposed" sizes="72x72" href="/security/icons/apple-touch-icon-72x72-precomposed.png" />
    
    <link rel="Stylesheet" href="css/corner.css" type="text/css">
<link rel="stylesheet" type="text/css" media="screen,print" href="//www.ibm.com/common/v14/table.css" /> 
<link rel="stylesheet" type="text/css" href="//www.ibm.com/common/v14/main.css" />
<link rel="stylesheet" type="text/css" media="all" href="//www.ibm.com/common/v14/screen.css" />
<link rel="stylesheet" type="text/css" media="print" href="//www.ibm.com/common/v14/print.css" />
 
</head>
<body  onLoad="return 1;"> 

    <div class='cornerC' id='cornerC'>
        <a href="list.jsp" target="_blank"><IMG SRC="img/s1.jpeg">list</a>
        <a href="mvel.jsp" target="_blank"><IMG SRC="img/ninja_ico_gui.png">mvel</a>
        <a href="formats.jsp" target="_blank"><IMG height=32 width=32 SRC="img/bug.gif">formats</a>
		<a href="_ah/mail/" target="_blank"><IMG SRC="img/ait.jpg">mail</a>
		<a href="push.jsp" target="_blank"><IMG SRC="img/ninja_ico_consock.jpg">push</a>
		<a href="getput.jsp" target="_blank"><IMG SRC="img/ninja_ico_consock.jpg">+<IMG SRC="img/ninja_ico_listen.jpg">get2put</a>		
		<a href="l/aHR0cDovL2hhYnJhaGFici5ydS8=" target="_blank"><IMG SRC="img/ajax-loader.gif">habr</a>
		<a href="l/aHR0cDovL2ZpZHVjaWEuZGUv" target="_blank"><IMG SRC="img/ajax-loader.gif">I</a>		
    </div>    
<%@page import="org.jrobin.cmd.RrdCommander"%>
<%@page import="org.jrobin.svg.RrdGraphInfo"%>
<!--
<table > 
	<tr   >
		<td >
						<a href="https://rrdsaas.appspot.com/rrd.jsp"  title=" RRD Java impl ">
						RRDSAAS <%=System.currentTimeMillis() %>
	
		</td>
		<td height="64" width="64">
			img.svg:<img src="JavaLogo.svg" height="64" width="64" alt="javalogo as img.svg" /> 
		</td><td height="64" width="64">
			embed.svg :
			<embed src="JavaLogo.svg" type="image/svg+xml" height="64" width="64"  scale=true alt="javalogo as embed.svg" />
		</td><td height="64" width="64">	
			embed+p.svg :
			<embed src="JavaLogo.svg" height="64" width="64"  type="image/svg+xml" 
			scale=true alt="javalogo as embed+plug.svg"  pluginspage="http://www.adobe.com/svg/viewer/install/" /> 
		</td><td height="64" width="64">	
			object.svg :
			<object data="JavaLogo.svg" height="64" width="64" type="image/svg+xml" alt="javalogo as object+plug.svg" 
			codebase="http://www.adobe.com/svg/viewer/install/" />
			</a>
		</td><td height="64" width="64">	
			IMG-X :
			<IMG src="gif.jsp" height="64" width="64"   />
		</td> 	
	</tr>	
</table>
-->
<!-- TUTORIAL --->
<table width="100%" >
	<tr width="100%" height="64">
	<td>
		<table width="100%">
			<tr>
				<td>
					<iframe src="man/man.html" width="1310%"  height="456">
						a lot of thanks to Alex van den Bogaerdt for this very kindly checked documentation. Unfortunately this page is dropped from original https://rrd4j.dev.java.net/tutorial.html...
						... but __fortunately___ I backuped it! :)
					</iframe>
				</td>	
			</tr>				
		</table>	 
	</td>	
	<td>
		<table ><tr><th>1</th><th>2</th> </tr>
				<tr >
					<td>Execution result:</td>
				</tr>
				<tr>
					
		<%
			System.out.println("== RrdWS/RRDTool commander exec...");
			Object o = null;
			String cmdTmp = request.getParameter("cmd");
			java.io.ByteArrayOutputStream bufOut = new java.io.ByteArrayOutputStream ();
			java.io.PrintWriter parceOutputWriter = new java.io.PrintWriter(bufOut, true) ;
			try{ 
				if (cmdTmp != null) {
					System.out.println(cmdTmp);
					cmdTmp = cmdTmp.replace("\\", "\n");
					//RrdCommander.setRrdDbPoolUsed(false);
					o = RrdCommander.execute(cmdTmp);
					
					
					if (o instanceof org.jrobin.svg.RrdGraphInfo) {
						org.jrobin.svg.RrdGraphInfo oInf = (org.jrobin.svg.RrdGraphInfo) o;
						session.setAttribute("svg", oInf.getBytes());
					}
					if (o instanceof org.jrobin.graph.RrdGraphInfo) {
						org.jrobin.graph.RrdGraphInfo oInf = (org.jrobin.graph.RrdGraphInfo) o;
						session.setAttribute("gif", oInf.getBytes());
					}
				}
			}catch(Throwable e){
				e.printStackTrace(parceOutputWriter);
			}
			bufOut.flush();
			bufOut.close();
			%><td  ><%="<textarea>"+bufOut.toString()+"</textarea>"%></td><%			
		%> 			<td  >
						<textarea><%=o%></textarea>
					</td>	
				
				</tr>		
				<tr>
					<td>LastCMD:</td>
					<td width="80%">
						<textarea> <%=cmdTmp%> </textarea>
					</td>
			</tr > <tr > 
					
					<td width="10%" height="64">	
								Tomcat RESULT Image:
					</td>	<td width="10%" height="64">	
								GAE RESULT Image:
					</td>
			</tr > <tr > 
					<td width="10%" height="64">	
								<IMG src="speed.gif" height="64" width="164"  alt="tomcat/jee rrdoutput" />							
					</td>	<td width="10%" height="64">	
								<embed src="svg.jsp" type="image/svg+xml" height="100%" width="100%"  alt="gae rrdoutput" />
					</td>	
			</tr > <tr > 			
				<td>  
					<form method="post">
						<table>
							<tr > 
									<td>
										<%
										String testCOLOR = "FFFFFF000000CCAA";
										testCOLOR  = testCOLOR   .substring(  (int)(System.currentTimeMillis()%10));
										testCOLOR   = testCOLOR  .substring(0,6);
										String cccTMP = (""+cmdTmp).hashCode()%2==0?"graph":"graphsvg";
										String lnnTMP = ""+ (((int)(System.currentTimeMillis()%3)+1));
										String ttTMP = new String[]{"amazing","greate ", "fine", "super", "geil", "perfect", "bombastisch", "excelent", "ideal", "fantastic", "unique", "unreal"}[Math.abs((""+cmdTmp).hashCode()%11)];
										String vvvTMP = new String[]{"vip","vasja ", "pupkin", "vasilij", "ivanovich", "!no pasaran!", "bl-ky!", "Gra-Vi-Ca pa", "KinDzaDza", "Ma-Ma, Ma-Ma, ..", "Ky!", " "}[Math.abs((""+cmdTmp).hashCode()%11)];
										String testCMD = " rrdtool "+cccTMP + " speed.gif  -v '"+vvvTMP+"'  -t 'RRDWS is "+ttTMP+"!'  --start 920804400 --end 920808000  DEF:myspeed=test.rrd:speed:AVERAGE  LINE"+lnnTMP+":myspeed#"+testCOLOR;
										%> <textarea name="cmd"  cols="60" rows="4" value="<%=testCMD%>"><%=testCMD%></textarea>
									</td>
							</tr > <tr > 
									<td>	
										<<<-- if u don't know what to do -- just try the default action - press the button ;)  
									</td>	
							</tr > <tr > 
									<td>										
										<input type="submit" />
									</td>	
							</tr> 
						</table>
					</form>	
				</td>	<td width="10%" height="64">	
				</td>	
			</tr> 
		</table>		
	</td>
</tr>
</table>		
<!-- eo TUTORIAL -->
	
<table>		
	<tr> 
		<td width="5%" valign="top" align="left" >	
				<iframe width="200" height="200" src="js/clock.html"></iframe>
		</td> <td  width="564"  valign="top" align="left" >
				<img src="speed.svg"    height="164" width="564"   alt="gae rrdoutput" />
		</td> <td width="51%" valign="top" align="left" >	
				<IMG src="speed.gif"    alt="tomcat/jee rrdoutput" />							
		</td>	
	</tr>
</table>	
