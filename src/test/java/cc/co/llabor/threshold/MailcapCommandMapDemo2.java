package cc.co.llabor.threshold;

import javax.activation.CommandInfo;
import javax.activation.MailcapCommandMap;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  09.11.2011::11:31:40<br> 
 */
public class MailcapCommandMapDemo2 {
	  public static void main(String[] args) {
	    MailcapCommandMap mailcapCommandMap = new MailcapCommandMap();
	    String mailcap = "text/plain;; " + "x-java-content-handler=beans.TextHandler;"
	        + "x-java-view=beans.TextViewer;" + "x-java-edit=beans.TextEditor";
	    mailcapCommandMap.addMailcap(mailcap);
	    // Get all MIME types
	    String[] mimeTypes = mailcapCommandMap.getMimeTypes();
	    for (String mimeType : mimeTypes) {
	      System.out.println(mimeType);
	      CommandInfo[] commandInfos = mailcapCommandMap.getAllCommands(mimeType);
	      for (CommandInfo info : commandInfos) {
	        System.out.println(" " + info.getCommandName() + " : "
	            + info.getCommandClass());
	      }
	    }
	  }
}

 