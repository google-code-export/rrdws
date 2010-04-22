<%@page import="java.io.ByteArrayInputStream"%><%@page  contentType="image/svg+xml"%><%
response.setContentType("image/svg+xml;charset=UTF-8");
%><?xml version="1.0" standalone="no"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setHeader("Content-Disposition", "inline;filename=svg.svg");
%>
<%@page import="ws.rrd.Chk"%> 
 
<%
try{
	 	if (session.getAttribute("svg")!=null){
	 		out.write("<svg  version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
	 		out.write(new String(  (byte[])session.getAttribute("svg")));
	 		out.write("</svg>");
	 		out.flush();
	 		
	 	}else{
			String data = 
				"<svg width=\"100%\" height=\"100%\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"><title id=\"test-title\">testtitle</title><desc id=\"test-desc\">This test validates that text can be both stroked.</desc><g id=\"test-body-content\"><defs><filter id=\"SphereMapTest\" filterUnits=\"objectBoundingBox\" x=\"0\" y=\"0\" width=\"1\" height=\"1\" ><feDisplacementMap in=\"Texture\" in2=\"Map\" scale=\"64\" xChannelSelector=\"R\" yChannelSelector=\"G\"/> </filter></defs> <text x=\"20\" y=\"20\" fill=\"#00f\" stroke=\"yellow\" stroke-width=\".3\" transform=\"rotate(5.0) scale(1.8, 1.8)\" style=\"-inkscape-font-specification:Monotype Corsiva Italic;font-family:Monotype Corsiva;font-weight:normal;font-style:italic;font-stretch:normal;font-variant:normal\">SVG placeholder</text> <rect fill-opacity=\".4\" fill=\"#0ff\" x=\"1\" y=\"2\" width=\"128\" height=\"128\" filter=\"url(#SphereMapTest)\" />  <circle cx=\"10\" cy=\"20\" r=\"40\" fill=\"#0f0\" fill-opacity=\".6\"/> <circle cx=\"30\" cy=\"50\" r=\"30\" fill=\"#0f0\" fill-opacity=\".4\"/> <circle cx=\"60\" cy=\"70\" r=\"20\" fill=\"#0f0\" fill-opacity=\".2\"/>  </g></svg>";
 	 		out.write(data.toCharArray());
	 	}
	
	}catch(Throwable e){
		Chk.chk(e);
		e.printStackTrace();
}
%>