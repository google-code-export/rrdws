<%@page  contentType="image/gif"%><%@page errorPage="gif.gif"%><%@page import="java.io.InputStream"%><%@page import="java.io.FileInputStream"%><%
//OutputStream outTmp = response.getOutputStream();
InputStream inTmp = null;
try{
	inTmp = new FileInputStream("speed.gif");
}catch(Exception e){e.printStackTrace();}
if (inTmp == null){
	inTmp = this.getClass().getClassLoader().getResourceAsStream("speed.gif"); 
}
int b = 0;
try{
	for( b = inTmp.read();b>=0;b = inTmp.read()){
		out.write(b);
	}
	inTmp.close();
	out.flush();
	out.close();
	
}catch(Exception e){e.printStackTrace();}

%>
