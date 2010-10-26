<!-- <%=Request.QueryString%>   http://www.informit.com/articles/article.aspx?p=28502&seqNum=4 -->
<html><body>
<script type="text/javascript">
<% 
	Response.Cookies["newcode"].Value =  Request.Params["code"];
%><%=Response.Cookies["newcode"].Value%>  
</script>

</body>
</html>

