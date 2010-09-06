<%@page import="org.vietspider.html.HTMLDocument"%> >
<%@page import="org.vietspider.html.parser.HTMLParser2"%>
<%@page import="org.vietspider.html.HTMLNode"%>
<%

HTMLParser2 p2 =  new HTMLParser2();  
HTMLDocument docTmp = p2.createDocument("<html></html>");
HTMLNode root = docTmp.getRoot(); 
boolean isBeauty = ! root.isBeautify();
root.setBeautify(isBeauty);
String imgName = isBeauty? "beauty.jpeg":"girl.jpeg";
%><%@page import="ws.rrd.server.LServlet"%>
<img src="<%=LServlet.SwapServletUrl %>/../img/<%=imgName%>">