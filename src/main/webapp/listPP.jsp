<%@page import="ws.rrd.json.Tool"%><%@page 
import="java.util.HashMap"%><%@page 
import="java.util.Map"%><%@page 
import="ws.rrd.csv.Registry"%><%@page 
import="net.sf.jsr107cache.Cache"%><%@page 
import="cc.co.llabor.cache.Manager"%><% 
response.setContentType("text/plain;charset=UTF-8");
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
//response.setHeader("Content-Disposition", "inline;filename=xxx.json");
%><% 
// 
Cache cache = Manager.getCache();
Registry reg = (Registry) cache.get("REGISTRY");
if (null != reg){
int i=0;
String prefix = "";
int iDisplayLength	= 10;
try{iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));}catch(Exception e){}
int iDisplayStart	= 10;
try{iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));}catch(Exception e){}
String sSearch	 = request.getParameter("sSearch");
sSearch = sSearch==null?"":sSearch;
int sEcho	 = 0;
try{sEcho = Integer.parseInt(request.getParameter("sEcho"));}catch(Exception e){}
int iTotalRecords = 1000;
int iTotalDisplayRecords = 1000; // filtered records
// calcTable
Map<String, String>  keysTmp = reg.getPath2db();
iTotalRecords = keysTmp.size();iTotalDisplayRecords = keysTmp.size();
//calcSearch, if any
if (!"".equals(sSearch)){
	Map<String, String>  filteredTmp = new HashMap<String, String>();
	// ++
	for (String key:keysTmp.keySet() ){
		for (String tagTmp :sSearch.split(" ,")){ 
			if (!tagTmp.startsWith("-")){
				if (key.toUpperCase().indexOf(tagTmp.toUpperCase())>-1){
					filteredTmp.put(key,keysTmp .get(key));
				} 
			}
		}
	}
	keysTmp = new HashMap<String, String>();
	keysTmp.putAll(filteredTmp);
	// --
	for (String key:keysTmp.keySet() ){
		for (String tagTmp :sSearch.split(" ,")){
			if (tagTmp.startsWith("-")){
				if (key.toUpperCase().indexOf(tagTmp.substring(1).toUpperCase())>-1){
					filteredTmp.remove(key);
				} 
			}
		}
	}
	keysTmp = filteredTmp;
	iTotalDisplayRecords = keysTmp.size();
} 
System.out.println("iDisplayLength="+iDisplayLength+"&iDisplayStart="+iDisplayStart+"sSearch&='"+sSearch+"'&sEcho="+sEcho+"//||iTotalRecords::"+iTotalRecords+"iTotalDisplayRecords:"+iTotalDisplayRecords);
%>{ 
  "sEcho": "<%=sEcho %>",
  "iTotalRecords": "<%=iTotalRecords %>",
  "iTotalDisplayRecords": "<%=iTotalDisplayRecords %>",
"aaData": [
<%

for (String key:keysTmp.keySet() ){
i++;
if (i<iDisplayStart)continue;
if (i>(iDisplayStart+iDisplayLength))break;

String iTmp =reg.getPath2db().get(key);
String titleTmp = "`" + Tool.escapeJavaScript( key ) +"`";
String aVal = "xwin.jsp?db="+ iTmp+"&_t="+titleTmp;
String aTxt = ""+(i%3);
String imgSrc = "gen.jsp?db="+ iTmp;
String aHrefVal = "<a target='_blank' href='xwin.jsp?db="+ iTmp+"&_t="+key+"'>"+key+"</a>";
aHrefVal =  Tool.escapeJavaScript(  aHrefVal );
%> <%=prefix%>[ "<%=i %>" , "<a target='_blank' href='<%=aVal%>'><%=aTxt%></a>" , "<%=iTmp%>" , "<%=aHrefVal%>" , "<img src='<%=imgSrc%>'/>" ]

<%
prefix = ",";
} // for (String key:reg.getPath2db().keySet() ){
} // if (null != reg){
%>
] }