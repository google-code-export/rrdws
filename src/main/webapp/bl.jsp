<%@page import="cc.co.llabor.cache.Manager"%>
<%@page import="net.sf.jsr107cache.Cache"%>
<%
try{
	String toPrint ="nothing to do..";
	Cache blTmp = Manager.getCache("BlackList");
	// LIST-repo contain all items
	String newList = (String) blTmp.get("list");
	newList  = newList ==null?"/":newList ;
	
	
	
	String cmd = null;
	cmd = cmd !=null?cmd:request.getParameter("insert");
	cmd = cmd !=null?cmd:request.getParameter("update");
	cmd = cmd !=null?cmd:request.getParameter("ban");
	cmd = cmd !=null?cmd:request.getParameter("add");

	if (cmd !=null){
		String val = (String)blTmp.get(cmd);
		blTmp.put(cmd, ""+System.currentTimeMillis());
		if (val != null && val != cmd){
			toPrint  = "-/+ ["+val+"]->{"+cmd+"}.";
		}else{
			toPrint  = "+ {"+cmd+"}.";
		}
		newList  = newList .replace(cmd+"/", "");
		newList += cmd +"/";
		blTmp.put("list", ""+newList);			

	}
	cmd = null;
	cmd = cmd !=null?cmd:request.getParameter("remove");
	cmd = cmd !=null?cmd:request.getParameter("delete");
	cmd = cmd !=null?cmd:request.getParameter("del");
	cmd = cmd !=null?cmd:request.getParameter("rm");

	if (cmd !=null){
		String val = (String)blTmp.get(cmd);
		if (val != null){
			toPrint  = "-- ["+val+"]->{"+cmd+"}.";
			newList  = newList .replace(cmd+"/", "");		
			blTmp.put("list", ""+newList);
			System.out.println(newList);
		}else{
			toPrint  = "??? {"+cmd+"}.";
			 
		}
	}

	cmd = null;
	cmd = cmd !=null?cmd:request.getParameter("list");
	if (cmd !=null){
		toPrint  = "list>>>"+newList;
	}
	
	
	
%>
<pre>
<%=toPrint.replace("/","\n") %>
</pre>
<%
}catch(Throwable e){
	e.printStackTrace();
	e.printStackTrace( response.getWriter());
 

}
%>