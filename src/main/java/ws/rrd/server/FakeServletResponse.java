package ws.rrd.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  08.07.2010::20:16:35<br> 
 */
public class FakeServletResponse implements javax.servlet.http.HttpServletResponse {
	ByteArrayOutputStream baos = new ByteArrayOutputStream(111);
	private String ct = "text/html";
	private Locale l = Locale.JAPANESE;
	private int cl = 0;
	private String ce = "UTF-8";
	private int bs= 1111;
	
	public String toString(){
		return baos.toString();
	}

	@Override
	public void flushBuffer() throws IOException { 
	}

	@Override
	public int getBufferSize() {
			return bs ; 
	}

	@Override
	public String getCharacterEncoding() { 
			return ce; 
	}

	@Override
	public String getContentType() { 
			return ct ; 
	}

	@Override
	public Locale getLocale() { 
			return l ; 
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStream(){ 
			public void write(int b) throws IOException {
				baos.write(b);
				cl++;
			}};
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(baos, true);
	}

	@Override
	public boolean isCommitted() { 
			return 1==2;
	}

	@Override
	public void reset() {}

	@Override
	public void resetBuffer() {}

	@Override
	public void setBufferSize(int arg0) {bs = arg0;}

	@Override
	public void setCharacterEncoding(String arg0) {ce =arg0; }

	@Override
	public void setContentLength(int arg0) {cl =arg0; }

	@Override
	public void setContentType(String arg0) {ct=arg0;}

	@Override
	public void setLocale(Locale arg0) {this.l = arg0;}

	@Override
	public void addCookie(Cookie arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void addHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public boolean containsHeader(String arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		return false;
		}
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		return null;
		}
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		return null;
		}
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		return null;
		}
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		return null;
		}
	}

	@Override
	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void setHeader(String arg0, String arg1) {
 
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void setStatus(int arg0) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

	@Override
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub
		if (1==2)throw new RuntimeException("not yet implemented since 08.07.2010");
		else {
		}
	}

}


 