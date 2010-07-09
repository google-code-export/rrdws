
<%@page import="net.sf.jsr107cache.Cache"%>
<%@page import="ws.rrd.mem.MemoryFileCache"%>
<%
String newbanTmp = "";
newbanTmp += request.getRequestURL();
newbanTmp += ":";
newbanTmp += request.getQueryString();
newbanTmp += ":";
newbanTmp += request.getRemoteAddr();
newbanTmp += ":";
newbanTmp += request.getSession(true).getId();
newbanTmp += "\n";

System.out.println(newbanTmp);
Cache cache = MemoryFileCache.getCache();
String  banlistTmp = ""+cache.get("--banlist--");
if (banlistTmp.indexOf(newbanTmp) == -1){
	banlistTmp += newbanTmp;
	cache.put("--banlist--", banlistTmp);
}else{
	%><!-- ignored   --><%
	System.out.println("ignored");
}

%>
<script language="javascript">
    function DoCheckLength(aTextBox) {
      if (aTextBox.value.length>13) {
        document.theForm.submit();
        //beep.play();
      }
    }
</script>
<style type="text/css">
input.ban { 
	padding-left: 28px;            
	background: url("blackhole.gif");               
	background-repeat: no-repeat;
	border: none; 
	background-position: left center;                 
	height: 64px;   
	width: 64px;   
}
</style>
<FORM NAME="theForm" action="bh.jsp?">
<input type="text" class="ban" maxlength="512" size="1"
onkeyup="DoCheckLength(this);"
onmouseout="DoCheckLength(this);"
onkeyup="DoCheckLength(this);"
ID="ad" NAME="ad">
</FORM>
<%try{%>
<!-- <%=cache.keySet()%> -->
<%}catch(Throwable e){}%>
