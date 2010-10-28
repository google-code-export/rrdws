<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Session"%>
<%@page import="java.util.Properties"%>
<html><head>mail</head><body>
<%
try{
		
        Properties props = new Properties();
        props.put("mail.debug", "true"  );
        Session mailSession = Session.getDefaultInstance(props, null);
	
        String strTO = ""+request.getAttribute("_to");
        String strFROM = ""+request.getAttribute("_from");
        String msgBody = "..."+request.getAttribute("_body");
        String msgSubj = "..."+request.getAttribute("_subj");
        String file = ""+request.getAttribute("_file");
         
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(strFROM));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strTO));
        msg.setSubject(msgSubj);
        msg.setText(msgBody);
        Transport.send(msg);
}catch(Throwable e){
%><%=e.getMessage() %><% e.printStackTrace(response.getWriter()); %>
<form  method="post" enctype="multipart/form-data">
  <p>Select <b>PI3</b> File for update:<br>
   FROM:<br> <input name="_from" type="text" value="vasiliij.pupkin@googlemail.com">
   TO:<br> <input name="_to" type="text" value="vasiliij.pupkin@googlemail.com">
   SUBJECT:<br> <input name="_subj" type="text" value="test">
    <input name="_body" type="textarea"  value="vasiliij.pupkin@googlemail.com">
   ATTACHE 
    <input name="_file" type="file" size="95%" maxlength="100000" accept="text/*">
    <br>
    <input type="submit"/>
  </p>
</form>
<%
}
%>

<form  method="post" enctype="multipart/form-data">
  <p>Select <b>PI3</b> File for update:<br>
   FROM:<br> <input name="_from" type="text" value="vasiliij.pupkin@googlemail.com">
   TO:<br> <input name="_to" type="text" value="vasiliij.pupkin@googlemail.com">
   SUBJECT:<br> <input name="_subj" type="text" value="test">
    <input name="_body" type="textarea"  value="vasiliij.pupkin@googlemail.com">
   ATTACHE 
    <input name="_file" type="file" size="95%" maxlength="100000" accept="text/*">
    <br>
    <input type="submit"/>
  </p>
</form>
</body></html>