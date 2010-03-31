<%@page import="java.util.List"%>
<%@page import="javax.jdo.Query"%>
 
<%@page import="javax.jdo.PersistenceManager"%>
<%@page import="ws.rdd.jdo.RRD_JDOHelper"%>
<%@page import="ws.rdd.jdo.Blob"%> 
<%@page import="ws.rrd.Chk"%>
<%@page import="org.jrobin.cmd.RrdGraphCmdTest"%>
<%@page import="org.jrobin.cmd.RrdCommander"%>


<%="<pre>"+System.getProperties()+"</pre>"%>
 
 <form  method="post" action="/rrd/rrd.jsp?" >
 <input name="cmd" size="100"/>
 <input type="submit"/>
</form>

<%
System.out.println("== Rrd4j's RRDTool commander ==");
String cmdTmp = request.getParameter("cmd");
System.out.println(cmdTmp);
RrdCommander.setRrdDbPoolUsed(false);
RrdCommander.execute(cmdTmp);
%>
<% 
 
if (1==2)
try {
	
	PersistenceManager   pm =RRD_JDOHelper.getInstance().getPMF().getPersistenceManager();

    Query query =   pm.newQuery(Blob.class);
    //pm.newQuery("select from Blob ");
      //      "where lastName == lastNameParam " +
        //    "parameters String lastNameParam " +
          //  "order by hireDate desc");

	List<Blob> results = (List<Blob>) query.execute();//query.execute("Smith");	
	for ( Object oTmp: results ){
		out.append("stored  >> "+((Blob)oTmp).getName()  +" ["+ ((Blob)oTmp).getData().length +"]<br>");
	}
	
} finally {
	out.append("!!!!!!!!!!!!!!!@!");
    //pm.close();
} 
%>
<%
try{
		if (1==2)(new RrdGraphCmdTest()).testExecute();
	}catch(Throwable e){
		Chk.chk(e);
		e.printStackTrace();
}
%>