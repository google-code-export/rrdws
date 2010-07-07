<%@page  contentType="image/svg+xml"%><%
response.setContentType("image/svg+xml");
%><?xml version="1.0" standalone="no"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"> 
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="org.jrobin.cmd.RrdSVGCmdTest"%>   
<%
//test jsp for fetching-tech -  TODO : clean
try{
		if (1==2)(new RrdSVGCmdTest()).testExecute();
		ByteArrayOutputStream myOut = new ByteArrayOutputStream();
		//Svg2PngAndJpg.Svg2Jpg(this.getClass().getClassLoader().getResourceAsStream("test.svg"),myOut);
		InputStream in = (new RrdSVGCmdTest()).generateSvg();
		byte[]buf = new byte[in.available()]; 
		in.read(buf);
		out.write(new String(buf));
		//out.write(myOut.toString());
		
	}catch(Throwable e){ 
		e.printStackTrace(response.getWriter());
}
%>