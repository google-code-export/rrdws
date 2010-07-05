/***************************************************************************
 * Copyright 2001-2008 The VietSpider         All rights reserved.  		 *
 **************************************************************************/

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vietspider.browser.HttpSessionUtils;
import org.vietspider.common.io.DataWriter;
import org.vietspider.net.client.HttpResponseReader;
import org.vietspider.net.client.WebClient;

/**
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Nov 2, 2008
 */
public class JavaNetLogin {

  public static void main(String[] args) throws Exception {
    WebClient webClient = new WebClient();

    String homepage  = "http://java.net/";
    webClient.setURL(homepage, new URL(homepage));

    HttpHost httpHost = webClient.createHttpHost(homepage);
    HttpGet httpGet = webClient.createGetMethod(homepage, "http://java.net/");

    HttpResponse response = webClient.execute(httpHost, httpGet);
    HttpEntity entity = response.getEntity();

    System.out.println("Login form get: " + response.getStatusLine());
    if (entity != null) entity.consumeContent();

    System.out.println("Initial set of cookies:");
    DefaultHttpClient httpClient = (DefaultHttpClient) webClient.getHttpClient();
    List<Cookie> cookies = httpClient.getCookieStore().getCookies();
    if (cookies.isEmpty()) {
      System.out.println("None");
    } else {
      for (int i = 0; i < cookies.size(); i++) {
        System.out.println("- " + cookies.get(i).toString());
      }
    }

    HttpSessionUtils httpSession = new HttpSessionUtils(webClient, "ERROR");

    StringBuilder builder = new StringBuilder(homepage);
    builder.append('\n').append("your_username:your_password");

    httpSession.login(builder.toString(), "utf-8", new URL(homepage), homepage);

    httpGet = webClient.createGetMethod("http://java.net/", "http://java.net/");
    response = webClient.execute(httpHost, httpGet);
    entity = response.getEntity();

    HttpResponseReader httpResponseReader = new HttpResponseReader();
    byte [] bytes = httpResponseReader.readBody(response);
    new DataWriter().save(new File("java_net.html"), bytes);

    System.out.println("Login form get: " + response.getStatusLine());
    if (entity != null) entity.consumeContent();

    System.out.println("Post logon cookies:");
    cookies = httpClient.getCookieStore().getCookies();
    if (cookies.isEmpty()) {
      System.out.println("None");
    } else {
      for (int i = 0; i < cookies.size(); i++) {
        System.out.println("- " + cookies.get(i).toString());
      }
    }

  }
}
