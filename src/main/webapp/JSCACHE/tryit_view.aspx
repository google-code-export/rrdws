<html>
<body>
<% 
	Response.Cookies["newcode"].Value =  Request.Params["code"];
%>	
<script type="text/javascript">
{
document.write("<h1>This is <%=Response.Cookies["newcode"].Value%> a heading</h1>");
document.write("<p>This is a paragraph.</p>");
document.write("<p>This is another paragraph.</p>");
}
</script>

</body>
</html>

