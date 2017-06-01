package com.song.util;


import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


public class HttpUtil
{
    
    
    static PoolingHttpClientConnectionManager cm =null;
    static CloseableHttpClient httpClient =null;
    static {
        cm = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        cm.setMaxTotal(200);
        // 设置每个路由默认最大连接数
        cm.setDefaultMaxPerRoute(200);
        
        httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .build();
    }

    public static String httpPostForString(List<NameValuePair> nvps, String url,String charset,boolean isLog)
        throws Exception
    {
        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(nvps,charset));
        httpPost.setHeader("Connection","keep-alive");
        // 发送请求
        HttpResponse response = httpClient.execute(httpPost);
        StringBuffer logString = new StringBuffer("");
        for (NameValuePair nvp : nvps)
        {
            logString.append("\r" + nvp.getName() + "：" + nvp.getValue());
        }
        String result = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200)
        {
            if(isLog){
                System.out.println("\rPost地址:" + url + "\r参数:\r---------------------------" + logString.toString()
                    + "\r---------------------------\r返回值:\r" + result);
            }
            return result;
        }
        else
        {
            if(isLog){
            	System.out.println("\rPost地址:" + url + "\r参数:\r---------------------------" + logString.toString()
                      + "\r---------------------------\r状态码:" + response.getStatusLine().getStatusCode() + "\r返回值:"
                      + result);
            }
            throw new Exception("\rPost地址:" + url + "\r参数:\r---------------------------" + logString.toString()
                                + "\r---------------------------\r状态码:" + response.getStatusLine().getStatusCode()
                                + "\r返回值:" + result);
        }
    }
   
    public static JSONObject httpPostForJson(List<NameValuePair> nvps, String url)
        throws Exception
    {

        return httpPostForJson(nvps, url,"utf-8");
    }

    public static JSONObject httpPostForJson(String queryString, String url)
        throws Exception
    {
        HttpEntity entity = new ByteArrayEntity(queryString.getBytes());

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        // 发送请求
        HttpResponse response;
        response = httpClient.execute(httpPost);
        StringBuffer logString = new StringBuffer("");
        String result = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200)
        {

            JSONObject requestObject = JSONObject.fromObject(result);
            System.out.println("\rPost地址:" + url + "\r参数:\r---------------------------" + logString.toString()
                     + "\r---------------------------\r返回值:\r" + requestObject.toString());
            return requestObject;
        }
        else
        {
        	System.out.println("\rPost地址:" + url + "\r参数:\r---------------------------" + logString.toString()
                      + "\r---------------------------\r状态码:" + response.getStatusLine().getStatusCode() + "\r返回值:"
                      + result);
            throw new Exception("\rPost地址:" + url + "\r参数:\r---------------------------" + logString.toString()
                                + "\r---------------------------\r状态码:" + response.getStatusLine().getStatusCode()
                                + "\r返回值:" + result);
        }
    }

    
    public static JSONObject httpPostForJson(List<NameValuePair> nvps, String url,String charset )
        throws Exception
    {

        return JSONObject.fromObject(httpPostForString(nvps, url,charset,true));
    }
    
    
    public static JSONObject httpPostForJson(List<NameValuePair> nvps, String url,String charset ,boolean isLog)
        throws Exception
    {

        return JSONObject.fromObject(httpPostForString(nvps, url,charset,isLog));
    }
}
