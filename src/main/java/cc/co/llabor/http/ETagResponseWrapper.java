package cc.co.llabor.http;

import java.io.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

// Referenced classes of package com.cj.etag:
//            ETagResponseStream

public class ETagResponseWrapper extends HttpServletResponseWrapper
{

    public ETagResponseWrapper(HttpServletResponse httpservletresponse, OutputStream outputstream)
    {
        super(httpservletresponse);
        response = null;
        stream = null;
        writer = null;
        buffer = null;
        response = httpservletresponse;
        buffer = outputstream;
    }

    public ServletOutputStream getOutputStream()
        throws IOException
    {
        if(stream == null)
            stream = new ETagResponseStream(buffer);
        return stream;
    }

    public PrintWriter getWriter()
        throws IOException
    {
        if(writer == null)
            writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), "UTF-8"));
        return writer;
    }

    public void flushBuffer()
        throws IOException
    {
        if(stream != null)
            stream.flush();
    }

    public void setContentLength(int i)
    {
        contentLength = i;
        super.setContentLength(i);
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public void setContentType(String s)
    {
        contentType = s;
        super.setContentType(s);
    }

    public String getContentType()
    {
        return contentType;
    }

    private HttpServletResponse response;
    private ServletOutputStream stream;
    private PrintWriter writer;
    private OutputStream buffer;
    private int contentLength;
    private String contentType;
}
