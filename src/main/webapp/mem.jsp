<%@page import="cc.co.llabor.cache.MemoryFileCache"%><%@page 
import="cc.co.llabor.cache.MemoryFileItem"%><%@page 
import="java.io.OutputStream"%><%@page 
import="org.apache.commons.fileupload.FileItem"%><% 
// retrieve the content from named-cache-value and send it back to requestor with correspondent contentType
// @see push.jsp - complimental part for "pushing" data
String nameTmp = request.getParameter("name");
try{
	MemoryFileCache cacheTmp = MemoryFileCache.getInstance("DEFAULT.BAK");
	MemoryFileItem item = cacheTmp. get ( nameTmp  );
	String ctTmp = item.getContentType();
	response.setContentType(ctTmp);//"image/svg+xml;charset=UTF-8"
	System.out.println(item);
	System.out.println(ctTmp );
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Content-Disposition", "inline;filename="+item.getName()+"");
	response.getOutputStream().write(item.get());
}catch(Throwable e){	
    e.printStackTrace();	
	response.setStatus(501);
	e.printStackTrace(response.getWriter());
}
%>