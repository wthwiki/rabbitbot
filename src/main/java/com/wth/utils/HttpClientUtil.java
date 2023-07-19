package com.wth.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HttpClientUtil {
    public HttpClientUtil() {
    }
    private static CloseableHttpClient defaultHttpClient= HttpClientBuilder.create().build();
    public static CloseableHttpClient getHttpClient(){
        return defaultHttpClient;
    }
    public static CloseableHttpClient getHttpClient(int timeout){

        CloseableHttpClient httpClient = httpClientCache.get(timeout);
        if(httpClient != null){
            return httpClient;
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setStaleConnectionCheckEnabled(true)
                .build();
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        httpClientCache.put(timeout,httpClient);
        return httpClient;
    }
    private  static Map<Integer,CloseableHttpClient>  httpClientCache= new ConcurrentHashMap<>();

    /**
     * 调用doget方法
     * @param client
     * @param url
     * @param urlParams
     * @return
     */
    public static String doGet(CloseableHttpClient client,String url, Map<String,Object> urlParams){
         String s=encode(getUrl(url,urlParams));
        HttpGet httpGet = new HttpGet(s);
        try {
            return request(client,httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String request(CloseableHttpClient httpClient, HttpUriRequest httpUriRequest) throws IOException {
        CloseableHttpResponse response = null;
        response = httpClient.execute(httpUriRequest);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity,"UTF-8");

    }

    private static String encode(String url) {
        return url.replace("\\","%5C")
                .replace("+","%2B")
                .replace(" ","%20")
//                .replace("%","%25")
                .replace("#","%23")
                .replace("$","%24")
                .replace("^","%5E")
                .replace("{","%7B")
                .replace("}","%7D")
                .replace("|","%7C")
                .replace("[","%5B")
                .replace("]","%5D");


    }

    private static String getUrl(String s, Map<String, Object> urlParams) {
        String url = "";
        if(!CollectionUtils.isEmpty(urlParams)){
            url = RestUtil.urlSplicing(s,urlParams);
        }else{
            url = s;
        }
        return url;
    }


    public static String doPost(CloseableHttpClient client,String url, Map<String,Object> urlParams){
        String s = encode(getUrl(url, urlParams));
        HttpPost httpPost = new HttpPost(s);
        try {
            return request(client,httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

