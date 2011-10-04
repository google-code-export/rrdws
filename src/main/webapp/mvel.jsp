<%@page import="java.util.Enumeration"%><%@page 
	import="java.util.Arrays"%><%@page 
	import="java.sql.Array"%><%@page 
	import="java.util.Collections"%><%@page 
	import="org.apache.commons.lang.StringEscapeUtils"%><%@page 
	import="java.net.URLDecoder"%><%@page 
	import="java.net.URLEncoder"%><%@page 
	import="java.util.Date"%><%@page 
	import="jmxlogger.tools.ToolBox"%><%@page 
	import="org.mvel2.MVEL"%><%@page 
	import="java.util.HashMap"%><%@page 
	import="java.util.Map"%><%@page 
	import="java.io.Serializable"%><% 
String expression = request.getParameter("expression");
expression  = expression ==null? "foobar > 99":StringEscapeUtils.unescapeHtml(expression)  ;
// Compile the expression.
Serializable compiled = MVEL.compileExpression(expression);

Map<java.lang.String,java.lang.Object> vars = new HashMap<java.lang.String,java.lang.Object>();
String names = "";
for (Enumeration eTmp = session.getAttributeNames(); eTmp.hasMoreElements();){
	String aTmp = (String)eTmp.nextElement() ;
	names +=aTmp;
	names +=",";
	vars.put(  aTmp,  session.getAttribute(aTmp));
}
vars.put("foobar", new Integer(100));
vars.put("names", names);

//
        Map<String, Long> stats = new HashMap<String,Long>();
        stats.put(ToolBox.KEY_EVENT_START_TIME, new Long(new Date().getTime()));
        stats.put(ToolBox.KEY_EVENT_LOG_COUNTED, new Long(12345678));
        stats.put(ToolBox.KEY_EVENT_LOG_COUNT_ATTEMPTED, new Long(87654321));
        stats.put("INFO", new Long(12345));
        stats.put("DEBUG", new Long(1234567));
vars.put(ToolBox.KEY_EVENT_LOG_STAT, stats);

// Now we execute it.
Object result =  MVEL.eval(expression,session, vars);//
session.setAttribute("result", result);
String histTmp = ""+(new Date())+":"+expression+"="+result+ "<br>"+session.getAttribute("history") ;
session.setAttribute("history", histTmp);

%><html>
<body onload="document.forms[0].expression.focus()">
<%=expression%> = <%=result  %>
<form>
	<input  name="expression" type="text" name="expression"  size="100" value="<%=StringEscapeUtils.escapeHtml( ""+expression)%>">
	<input type="submit">
</form>
<%=histTmp%>
</body>
</html>