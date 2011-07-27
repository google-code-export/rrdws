<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >
<head>    
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

    <title>rrd4 u</title>
    <link rel="Stylesheet" href="css/corner.css" type="text/css">
<link rel="stylesheet" type="text/css" media="screen,print" href="//www.ibm.com/common/v14/table.css" /> 
<link rel="stylesheet" type="text/css" href="//www.ibm.com/common/v14/main.css" />
<link rel="stylesheet" type="text/css" media="all" href="//www.ibm.com/common/v14/screen.css" />
<link rel="stylesheet" type="text/css" media="print" href="//www.ibm.com/common/v14/print.css" />
 
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
					</td> 	
	</tr>	
	
</table>
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
						RrdGraphInfo oInf = (RrdGraphInfo) o;
						session.setAttribute("svg", oInf.getBytes());
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
			
				<td  >  



					<form method="post">
						<table>
							<tr > 
									<td>
										<%
										String testCOLOR = "FFFFFF000000CCAA";
										testCOLOR  = testCOLOR   .substring(  (int)(System.currentTimeMillis()%10));
										testCOLOR   = testCOLOR  .substring(0,6);
										String testCMD = " rrdtool graph speed.gif  --start 920804400 --end 920808000  DEF:myspeed=test.rrd:speed:AVERAGE  LINE"+(((int)(System.currentTimeMillis()%3)+1))+":myspeed#"+testCOLOR;
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
							</tr > <tr > 
	
			<td    	>	
							<iframe width="200" height="200" src="js/clock.html"></iframe>
				</td> 	 
					<td width="10%" height="64">	
								<IMG src="speed.gif"   alt="tomcat/jee rrdoutput" />							
					</td>	<td width="10%" height="64">	
								<embed src="svg.jsp" type="image/svg+xml"   alt="gae rrdoutput" />
					</td>	
				</tr>
</table>	
