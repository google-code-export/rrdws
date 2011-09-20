<%@page 
	import="java.util.List"%><%@page 
	import="cc.co.llabor.cache.MemoryFileItem"%><%@page 
	import="cc.co.llabor.cache.MemoryFileItemFactory"%><%@page 
	import="org.apache.commons.fileupload.servlet.ServletFileUpload"%><%@page
	import="cc.co.llabor.cache.MemoryFileItemFactory"%><%@page 
    contentType="application/json"%><%

	response.setContentType("application/json");		
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	response.setHeader("Content-Disposition", "attachment;filename=anno.json");	
	
	request.getSession(true);
System.out.println("reqURL :"+request.getRequestURL()+":"+request.getRequestURI());
System.out.println(" METHOD:"+  (request.getMethod().toLowerCase()) +"::"+request.getContentType() );
 for (Object par:request.getParameterMap().keySet().toArray()){
 	System.out.println(":"+par +" =="+request.getParameter(""+par ));
 } 

 byte[] buf = new byte[request.getInputStream().available()];
 request.getInputStream().read(buf);
 String theA = new String(buf);
 System.out.println(theA);
 String storedAnn = ""+session.getAttribute( "__ann__");
 System.out.println("replace :"+storedAnn +" ==>> ["+theA+"]");
 session.setAttribute( "__ann__",theA);
 
 if(ServletFileUpload.isMultipartContent(request)){ 
	 System.out.println("do_PostB");
     MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
     System.out.println("do_PostC");
     ServletFileUpload upload = new ServletFileUpload(factory);
     System.out.println("do_PostD");
     upload.setSizeMax(4*1024*1024); // 4 MB 
     // Parse the request
     List<MemoryFileItem> items = upload.parseRequest(request);  
     for(MemoryFileItem item : items) {
    	 System.out.println(item);
     }
 }else{
	 byte[] buf2 = new byte[request.getInputStream().available()];
	 request.getInputStream().read(buf2);
	 System.out.println(new String(buf2));
 }
%>
<%if ("null".equals(storedAnn) || 1==1 ){
	%>  
	
	
	
{
  "rows": [
    {
      "account_id": "39fc339cf058bd22176771b3e3029975", 
      "created": "2011-09-17T13:07:55.524531", 
      "text": "This is pretty sweet", 
      "_rev": "1-0c9717f75b49aa2412449e3316f380ec", 
      "tags": [
        "awesome"
      ], 
      "uri": "http://okfn.github.com/annotator/demo/bookmarklet/", 
      "ranges": [
        {
          "start": "/div/article/section[2]/p", 
          "end": "/div/article/section[2]/p", 
          "startOffset": 276, 
          "endOffset": 382
        }
      ], 
      "annotator_schema_version": "v1.0", 
      "user": {
        "id": "annotator-demo", 
        "name": "Annotator Demo"
      }, 
      "quote": "piece of text. Hover over the highlight with your mouse\n           to view, edit and delete the annotation", 
      "_id": "39fc339cf058bd22176771b3e33ab41c", 
      "type": "Annotation", 
      "id": "39fc339cf058bd22176771b3e33ab41c", 
      "permissions": {
        "read": [], 
        "admin": [
          "annotator-demo"
        ], 
        "update": [], 
        "delete": [
          "annotator-demo"
        ]
      }
    }, 
    {
      "account_id": "39fc339cf058bd22176771b3e3029975", 
      "created": "2011-09-14T19:27:59.453528", 
      "text": "", 
      "_rev": "1-3fa5446235a5be6434553611caf0811b", 
      "tags": [], 
      "uri": "http://okfn.github.com/annotator/demo/bookmarklet/", 
      "ranges": [
        {
          "start": "/div/article/section/div/p", 
          "end": "/div/article/section/div/p", 
          "startOffset": 271, 
          "endOffset": 301
        }
      ], 
      "annotator_schema_version": "v1.0", 
      "user": {
        "id": "annotator-demo", 
        "name": "Annotator Demo"
      }, 
      "quote": "bookmarklet\n             again", 
      "_id": "39fc339cf058bd22176771b3e339ee7b", 
      "type": "Annotation", 
      "id": "39fc339cf058bd22176771b3e339ee7b", 
      "permissions": {
        "read": [
          "annotator-demo"
        ], 
        "admin": [
          "annotator-demo"
        ], 
        "update": [
          "annotator-demo"
        ], 
        "delete": [
          "annotator-demo"
        ]
      }
    }, 
    {
      "account_id": "39fc339cf058bd22176771b3e3029975", 
      "created": "2011-09-08T13:02:13.617211", 
      "text": "ererer", 
      "_rev": "1-ac22acd0ac73b94c39ae6013a580492d", 
      "tags": [], 
      "uri": "http://okfn.github.com/annotator/demo/bookmarklet/", 
      "ranges": [
        {
          "start": "/div/article/section[2]/p", 
          "end": "/div/article/section[2]/p", 
          "startOffset": 63, 
          "endOffset": 93
        }
      ], 
      "annotator_schema_version": "v1.0", 
      "user": {
        "id": "annotator-demo", 
        "name": "Annotator Demo"
      }, 
      "quote": "pretty quickly. Once it's load", 
      "_id": "39fc339cf058bd22176771b3e338992d", 
      "type": "Annotation", 
      "id": "39fc339cf058bd22176771b3e338992d", 
      "permissions": {
        "read": [
          "annotator-demo"
        ], 
        "admin": [
          "annotator-demo"
        ], 
        "update": [
          "annotator-demo"
        ], 
        "delete": [
          "annotator-demo"
        ]
      }
    }
  ], 
  "total": 3
}	
	
	
	
	<%
}else{ 	
	%> [ <%=storedAnn  %>]<%
}%>
 