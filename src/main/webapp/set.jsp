<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Enumeration"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	for(Enumeration en = request.getParameterNames() ;en.hasMoreElements();){
		String nextKey = ""+en.nextElement();
		String nextVal = request.getParameter(nextKey);
		if ("".equals(nextVal) || null == nextVal ){
			System.getProperties().remove(nextKey);
		}else{
			System.setProperty(nextKey, nextVal);
		}
		%>
		<tr background="00FFFF">
			<td>
			<%=nextKey%>
			</td>
			<td>
			<input  name="<%=nextKey%>" value="<%=nextVal%>">	<br>
			</td> 

		</tr>
		
		<% 
	}
	%>
<%
	  
	for(Enumeration en = System.getProperties().keys();en.hasMoreElements();){
		String nextKey = ""+en.nextElement();
		String nextVal = System.getProperty(nextKey); 
		%>
		<form id="sysPropForm" name="sysPropForm" method="get" action="/WebApp_ID/set.jsp" enctype="multipart/form-data">
 		<table>
		<tr>
			<td>
			<%=nextKey%>
			</td>
			<td>
			<input  name="<%=nextKey%>" value="<%=nextVal%>">	<br>
			</td>
			<td>
				<input  name="<%=nextKey%>" type="submit"  />
			</td>

		</tr>
		</table>
		<form>
		<% 
	}

	
 	
%>
	
	
</body>
</html>