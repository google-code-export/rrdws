<%
String testCOLOR = "FFFFFF000000CCAA";
testCOLOR  = testCOLOR   .substring(  (int)(System.currentTimeMillis()%10));
testCOLOR   = testCOLOR  .substring(0,6);
String testCMD = " rrdtool graph speed.gif  --start 920804400 --end 920808000  DEF:myspeed=test.rrd:speed:AVERAGE  LINE"+(((int)(System.currentTimeMillis()%3)+1))+":myspeed#"+testCOLOR;
%>
<form method="post" target=_parent >
cmd#<textarea name="cmd"  cols="160" rows="4" value="<%=testCMD%>"><%=testCMD%></textarea>
<input type="submit" />