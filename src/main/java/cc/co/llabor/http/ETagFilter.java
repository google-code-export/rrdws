package cc.co.llabor.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Referenced classes of package com.cj.etag:
//            ETagResponseWrapper

public class ETagFilter
    implements Filter
{

    public ETagFilter()
    {
        no_init = true;
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
        no_init = false;
        config = filterconfig;
        setFilterConfig(filterconfig);

    }

    public void destroy()
    {
        config = null;
    }

    public FilterConfig getFilterConfig()
    {
        return config;
    }

    public void setFilterConfig(FilterConfig filterconfig)
    {
        if(!no_init) return;
        no_init = false;
        config = filterconfig;
        String excludedTmp = filterconfig.getInitParameter(EXCLUDE);
		if(excludedTmp  == null){
        	//
        }else{
        	excluded +=",";
        	excluded += excludedTmp;
        }
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
        throws IOException, ServletException
    {
        HttpServletRequest httpservletrequest = (HttpServletRequest)servletrequest;
        HttpServletResponse httpservletresponse = (HttpServletResponse)servletresponse;
        String s = httpservletrequest.getRequestURI();
        if(excluded(s))
        {
            filterchain.doFilter(servletrequest, servletresponse);
        } else
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            ETagResponseWrapper etagresponsewrapper = new ETagResponseWrapper(httpservletresponse, bytearrayoutputstream);
            filterchain.doFilter(httpservletrequest, etagresponsewrapper);
            byte abyte0[] = bytearrayoutputstream.toByteArray();
            if(abyte0 == null)
                return;
            if(abyte0.length == 0)
                return;
            String s1 = etagresponsewrapper.getContentType();
            String s2 = (new StringBuilder()).append('"').append(getMd5Digest(abyte0)).append('"').toString();
            httpservletresponse.setHeader("ETag", s2);
            httpservletresponse.setHeader("Cache-Control", "must-revalidate");
            httpservletresponse.setHeader("Pragma", "token=\"ETag\"");
            httpservletresponse.setHeader("Expires", "Thu, 01 Jan 2070 00:00:00 GMT");
            
            
            //Cache-Control	no-cache
            //Pragma	no-cache
            String s3 = httpservletrequest.getHeader("If-None-Match");
            if(s3 != null && s3.equals(s2))
            {
                httpservletresponse.sendError(304);
                httpservletresponse.setHeader("Last-Modified", httpservletrequest.getHeader("If-Modified-Since"));
            } else
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(14, 0);
                Date date = calendar.getTime();
                httpservletresponse.setDateHeader("Last-Modified", date.getTime());
                if(s1 != null)
                    httpservletresponse.setContentType(s1);
                httpservletresponse.setContentLength(abyte0.length);
                ServletOutputStream servletoutputstream = httpservletresponse.getOutputStream();
                servletoutputstream.write(abyte0);
                servletoutputstream.flush();
                servletoutputstream.close();
            }
        }
    }

    private boolean excluded(String s)
    {
        if(s == null || excluded == null)
            return false;
        else
            return excluded.indexOf((new StringBuilder()).append(s).append(",").toString()) >= 0;
    }

    private String getMd5Digest(byte abyte0[])
    {
        MessageDigest messagedigest;
        try
        {
            messagedigest = MessageDigest.getInstance("MD5");
        }
        catch(Exception exception)
        {
            throw new RuntimeException("MD5 cryptographic algorithm is not available.", exception);
        }
        byte abyte1[] = messagedigest.digest(abyte0);
        BigInteger biginteger = new BigInteger(1, abyte1);
        StringBuffer stringbuffer = new StringBuffer(48);
        stringbuffer.append(biginteger.toString(16));
        return stringbuffer.toString();
    }

    private FilterConfig config;
    private boolean no_init;
    private String excluded;
    private static final String EXCLUDE = "exclude";
}
