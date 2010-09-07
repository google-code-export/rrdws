// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpHeader.java

package org.htmlparser.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;

public class HttpHeader
{

    private HttpHeader()
    {
    }

    public static String getRequestHeader(HttpURLConnection connection)
    {
        StringBuffer buffer = new StringBuffer(1024);
        buffer.append(connection.getRequestMethod());
        buffer.append(" ");
        buffer.append(connection.getURL());
        buffer.append(" HTTP/1.1\n");
        Map map = connection.getRequestProperties();
        for(Iterator iter = map.keySet().iterator(); iter.hasNext(); buffer.append("\n"))
        {
            String key = (String)iter.next();
            List items = (List)map.get(key);
            buffer.append(key);
            buffer.append(": ");
            for(int i = 0; i < items.size(); i++)
            {
                if(0 != i)
                    buffer.append(", ");
                buffer.append(items.get(i));
            }

        }

        return buffer.toString();
    }

    public static String getResponseHeader(HttpURLConnection conn)
    {
        StringBuffer buffer = new StringBuffer(1024);
        try
        {
            int code = conn.getResponseCode();
            if(-1 != code)
            {
                String message = conn.getResponseMessage();
                String value;
                for(int i = 0; null != (value = conn.getHeaderField(i)); i++)
                {
                    String key = conn.getHeaderFieldKey(i);
                    if(null == key && 0 == i)
                    {
                        buffer.append("HTTP/1.1 ");
                        buffer.append(code);
                        buffer.append(" ");
                        buffer.append(message);
                        buffer.append("\n");
                    } else
                    {
                        if(null != key)
                        {
                            buffer.append(key);
                            buffer.append(": ");
                        }
                        buffer.append(value);
                        buffer.append("\n");
                    }
                }

            }
        }
        catch(IOException ioe)
        {
            buffer.append(ioe.toString());
        }
        return buffer.toString();
    }
}
