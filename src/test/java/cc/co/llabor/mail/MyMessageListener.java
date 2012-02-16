package cc.co.llabor.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.subethamail.smtp.MessageListener;
import org.subethamail.smtp.TooMuchDataException;
 
public class MyMessageListener implements MessageListener {

	public boolean accept(String arg0, String arg1) {
		System.out.println("FROM:"+arg0);
		System.out.println("TO:"+arg1);
		return true;
	}

	public void deliver(String arg0, String arg1, InputStream arg2)
			throws TooMuchDataException, IOException {
		System.out.println("FROM:"+arg0);
		System.out.println("TO:"+arg1);
		System.out.println("sended  ["+arg2.available()+"] bytes."); 
		BufferedReader in = new BufferedReader(new InputStreamReader(arg2));
		for ( String nextLine = in .readLine();nextLine != null; nextLine = in .readLine() ){
			System.out.println(nextLine);
		} 
	} 
}


 