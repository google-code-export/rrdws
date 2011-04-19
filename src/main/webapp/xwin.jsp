<%@page import="java.text.SimpleDateFormat"%><%@page import="java.util.Date"%>
<%
	//acm721
	String _t = request.getParameter("_t");
	_t  = _t  == null? "-":_t ;
	_t  = _t.replace(" ", "_");
	_t  = _t.replace(":", "_");
	_t  = _t.replace("\"", "_");
	_t  = _t.replace("\'", "_");
	_t  = _t.replace("\t", "_");
	_t  = _t.replace("\b", "_");
	_t  = _t.replace("\n", "_");
	String _v = request.getParameter("_v");
	_v  = _v  == null? "- ":_v ; 	
	String dbTmp = "X-2113039516.rrd";
	dbTmp = 	request.getParameter("db")  == null? dbTmp : request.getParameter("db");
	String _h = "320";
	_h = 	request.getParameter("_h")  == null? _h : request.getParameter("_h");
	String _w = "640";
	_w = 	request.getParameter("_w")  == null? _w : request.getParameter("_w");
	String _end = "now";
	_end = 	request.getParameter("_end")  == null? _end : request.getParameter("_end");
	String _start = "end-1day";
	_start = 	request.getParameter("_start")  == null? _start : request.getParameter("_start");
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	String startDate = sdf.format(new Date());
%>
<HTML><HEAD>
<TITLE>WebStatistik :<%=_t %></TITLE>
<DIV ID="testdiv1" STYLE="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;">
</DIV>
<SCRIPT LANGUAGE="JavaScript"  SRC="js/CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">  
	var cal = new CalendarPopup(" testdiv1" );
	var cal = new CalendarPopup( );
		cal.setMonthNames('Januar','Februar','März','April','Mag','Juni','Juli','August','September','Oktober','November','Dezember');
	cal.setDayHeaders('S','M','D','M','D','F','S');
	cal.setWeekStartDay(1);
	cal.setTodayText("Heute");
</SCRIPT>
</HEAD>
<body>
	<img src="xgen.jsp?db=<%=dbTmp%>&_h=<%=_h%>&_w=<%=_w%>&_start=<%=_start%>&_end=<%=_end%>&_v=<%=_v%>&_t=<%=_t%>"/>.
	<form name= "RRD" method="post">
		 <input class="input" type=text name=_start value="<%=startDate%>">
		 
			<A HREF="#" 
				onClick="cal.select(document.forms['RRD']._start,'anchor4','dd.MM.yyyy'); return false;" 
				TITLE="start" NAME="anchor4" ID="anchor4"><img src="img/icon-calendar.gif"/></A>
		 <input class="input" type=text name=_end  value="<%=startDate%>">
		 	<A HREF="#" 
		 		onClick="cal.select(document.forms['RRD']._end,'tfEndtermin_A','dd.MM.yyyy'); return false;" 
				TITLE="end" 
				NAME="anchor5" ID="tfEndtermin_A">
				<img src="img/icon-calendar.gif"/></A>
		 <input class="input" type=text name=db value="<%=dbTmp%>">
		 <input type="submit">
	 </form>
	
	<img src="xgen.jsp?db=<%=dbTmp%>&_start=end-2day&_t=end-2day"/>.
	<img src="xgen.jsp?db=<%=dbTmp%>&_start=end-1week&_t=end-1week"/>.
	<img src="xgen.jsp?db=<%=dbTmp%>&_start=end-2week&_t=end-2week"/>. 
	 
 </body>
</HTML>