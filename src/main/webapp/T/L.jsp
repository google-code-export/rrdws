<%@page import="ws.rrd.server.LServlet"%>

<% String proxyhome = LServlet.SwapServletUrl; %>
<% String refererTmp = request.getHeader("Referer"); %>
<div id="tbar"
	style="color: #000; background: #FFF; z-index: 99999; width: 100%; position: fixed; bottom: 0; left: 0">
<table class="bd" width="100%" border="0" cellpadding="0"
	cellspacing="0" background="white_background_1.gif">
	<tr height="25px">
	
		<td align="left">
			<iframe src="bh.jsp" height="64" width="64" scrolling="no" marginheight="0" marginwidth="0" frameborder="0"></iframe>
		</td>
	
		<td align="left">
		<form target="_self" method="post" action="/">
		<table border="0" background="white_background_10.gif">
			<tr>
				<td width="60"><a href="#"
					onclick="document.getElementById('tbar').style.display='none';return false;">[close]</a></td>
				<td width="100"><a href="<%=proxyhome%>" target="_blank"
					style="color: #000; font-size: 10px">PROXY HOME</a></td>
				<td width="100"><a href="<%=refererTmp%>"
					target="_blank" style="color: #000; font-size: 10px">ORIGINAL
				PAGE</a></td>
				<td align="right"><input type="text" style="width: 400px"
					name="url" value="" width="100%" /></td>
				<td width="100"><input type="submit" style="width: 100%"
					value="  OK  " /></td>
			</tr>
		</table>
		</form>
		</td>

		
		<td width="200">
		<table width="100%" cellpadding="0" cellspacing="0" background="white_background_4.gif">
			<tr>
				<td>
				<table width="100">
					<tr>
						<td>COOKIE:</td>
						
						<td class="true"><a target="_self" class="a"
							href="<%=proxyhome%><%=refererTmp%>?bpxckmod=off">ON</a></td>
					</tr>
				</table>
				</td>
				<td>
				<table width="100" background="white_background_5.gif">
					<tr>
						<td>SCRIPT:</td>
						<td class="true"><a target="_self" class="a"
							href="<%=proxyhome%><%=refererTmp%>?bpxjsmod=off">ON</a></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<script>if (top != self){document.getElementById('tbar').style.display='none';}</script>
