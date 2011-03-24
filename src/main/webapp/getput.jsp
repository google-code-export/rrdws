<%@page import="java.util.List"%><%@page 
import="java.util.ArrayList"%><%@page 
import="java.lang.reflect.Array"%><%@page 
import="java.util.Properties"%><%@page 
import="net.sf.jsr107cache.Cache"%><%@page 
import="cc.co.llabor.cache.MemoryFileCache"%><%@page 
import="cc.co.llabor.cache.MemoryFileItem"%><%@page 
import="cc.co.llabor.cache.MemoryFileItemFactory"%><%@page 
import="org.apache.http.Header"%><%@page import="java.io.InputStream"%><%@page 
import="java.io.OutputStream"%><%@page 
import="org.apache.http.message.BasicHttpResponse"%><%@page 
import="org.apache.http.HttpEntity"%><%@page 
import="org.apache.http.HttpResponse"%><%@page 
import="ws.rdd.net.UrlFetchTest"%><%
// copy content of given URL_from to URL_to
// like follow curl-sequence:
// 1)curl http://www.google.de/search?q=html2xhtml  -k -o search.dat
// 2)curl http://www.it.uc3m.es/jaf/cgi-bin/html2xhtml.cgi -k --form type=auto --form tablength=2 submit=Convert -F Datei=@search.dat	
// returns  content of upload-response to 2nd-URL  
// @param: url :: fromURL, toURL

UrlFetchTest o =  new UrlFetchTest();
String refTmp =  request.getQueryString(); 

String urlFROM = request.getParameter("urlfrom");
System.out.println( "1]");
String urlTO = request.getParameter("urlto");
System.out.println( "2]");
if (urlFROM!= null && urlTO!=null){
	System.out.println( "3]");	
	//1)
	HttpResponse respTmp =o.fetchGetResp(urlFROM);
	HttpEntity eTmp = ((BasicHttpResponse )respTmp).getEntity();
	MemoryFileItemFactory factory = MemoryFileItemFactory.getInstance();
	Header ctTmp = eTmp.getContentType();
	String contentTypeTmp = ctTmp.getValue();
	String name2Tmp =  "GET_dat";
	// capacity ~ 100 seconds X 1000 requests
	name2Tmp += (""+System.currentTimeMillis()).substring(8);
	MemoryFileItem dataTmp = factory.createItem(name2Tmp, contentTypeTmp, false, name2Tmp);
	eTmp.writeTo(  dataTmp .getOutputStream() );
	dataTmp.flush();
	MemoryFileCache cacheTmp = MemoryFileCache.getInstance("DEFAULT.BAK");
	String nameTmp = cacheTmp. put( dataTmp  );
	//2)
	String [][]headersTmp = new String[][]{
			{"Accept","text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1"} 
			,{"Accept-Language","en,uk;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6,uz;q=0.5,pl;q=0.4"} 
			,{"Accept-Charset","iso-8859-1, utf-8, utf-16, *;q=0.1"} 
			,{"Accept-Encoding","deflate, gzip, x-gzip, identity, *;q=0"} 	
			,{"User-Agent","Opera/9.80 (Windows NT 5.1; U; de) Presto/2.7.62 Version/11.01"}
			//,{"Content-Type","multipart/form-data"}//; boundary=---------------------------7487282977518"+System.currentTimeMillis()
			//Content-Type: multipart/form-data; boundary=---------------------------16828772729426

			//,			{"Content-Length",""+dataTmp.getSize()} 
			
			};
	Properties paramsTmp = new Properties();
	paramsTmp .putAll( request.getParameterMap());
	
	List<MemoryFileItem> itemsTmp = new ArrayList<MemoryFileItem>();
	itemsTmp.add(cacheTmp.get(nameTmp));
	HttpResponse respToTmp =o.fetchPostResp(urlTO, headersTmp, paramsTmp, itemsTmp);
	HttpEntity eToTmp = ((BasicHttpResponse )respToTmp).getEntity();
	System.out.println( "6]"+contentTypeTmp);
	contentTypeTmp = eToTmp.getContentType().getValue();
	System.out.println( "7]"+contentTypeTmp);
	response.setContentType(contentTypeTmp);
	InputStream contentTmp = eToTmp.getContent();
	 
	if(ctTmp.getValue().indexOf("html") >= 0){
		System.out.println( "8]");
		// load all data for HTML ,and replace header
		int sizeTmp = Math.max(  (int)eToTmp.getContentLength(), contentTmp.available());
		System.out.println( "9]"+sizeTmp);
		byte buf[] = new byte[sizeTmp];
		int readedTmp = contentTmp.read(buf);
		String wData = new String( buf,0,readedTmp ); 
		System.out.println( "9]"+readedTmp);
		%><%=wData%><%
		System.out.println( "A]");
	}else{
		System.out.println( "4]");
	 	// plain copy 
		int sizeTmp = Math.max(  (int)eToTmp.getContentLength(), contentTmp.available());
		byte buf[] = new byte[sizeTmp];
		int readedTmp = contentTmp.read(buf);
		String outStr = new String(buf,0,readedTmp);
		%><%=outStr%><% 
	}
}else{
	urlFROM =urlFROM ==null?"http://www.google.de/search?q=html2xhtml":urlFROM;
	urlTO =urlTO ==null?"http://www.it.uc3m.es/jaf/cgi-bin/html2xhtml.cgi":urlTO;
%>
<form  enctype="multipart/form-data"> <!-- method="post"  -->
  <p>Select {a} AND {b}:<br>
    urlfrom<input name="urlfrom" id="urlfrom" type="text" size="350" value="<%=urlFROM%>"/>
    urlto<input name="urlto" id="urlto" type="text"  size="350" value="<%=urlTO%>"/>
    <input type="submit"/>
  </p>
</form>
<%
}
%>