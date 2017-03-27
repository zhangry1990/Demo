package com.zhangry.demo.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangry on 2017/3/17.
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private HttpUtil() {
    }

    public static String get(String url, String params) {
        return StringUtil.isNullOrEmpty(params)?get(url):get(url, CollectionUtil.queryStringToMap(params));
    }

    public static String get(String url, Map<String, Object> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            Iterator var3 = params.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                uriBuilder.setParameter(key, String.valueOf(params.get(key)));
            }

            URI uri = uriBuilder.build();
            return get(uri.toString());
        } catch (URISyntaxException var5) {
            throw ExceptionUtil.unchecked(var5);
        }
    }

    public static String get(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        return httpInternelExecute(httpget);
    }

    public static String post(String url, String params) {
        HttpPost httppost = new HttpPost(url);
        if(StringUtil.isNullOrEmpty(params)) {
            return httpInternelExecute(httppost);
        } else {
            StringEntity entity = new StringEntity(params, ContentType.create("application/json", Consts.UTF_8));
            entity.setChunked(true);
            httppost.setEntity(entity);
            return httpInternelExecute(httppost);
        }
    }

    public static String post(String url, Map<String, Object> mapParams) {
        List<NameValuePair> formparams = new ArrayList();
        Iterator var3 = mapParams.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            formparams.add(new BasicNameValuePair(key, String.valueOf(mapParams.get(key))));
        }

        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(formparams, Consts.UTF_8));
        return httpInternelExecute(httppost);
    }

    public static String post(String url, JSONObject jsonObject) {
        return post(url, JSON.toJSONString(jsonObject));
    }

    private static String getReponseHeaders(CloseableHttpResponse response) {
        Header[] headers = response.getAllHeaders();
        StringBuilder sb = new StringBuilder((int)((double)headers.length * 1.5D));
        HeaderIterator it = null;
        Header[] var4 = headers;
        int var5 = headers.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Header item = var4[var6];
            it = response.headerIterator(item.getName());

            while(it.hasNext()) {
                sb.append(it.next());
            }
        }

        return sb.toString();
    }

    private static String httpInternelExecute(HttpUriRequest httpUriRequest) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        BufferedReader reader = null;

        try {
            response = httpclient.execute(httpUriRequest);
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if(statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            } else if(entity == null) {
                throw new ClientProtocolException("Response contains no content");
            } else {
                reader = new BufferedReader(new InputStreamReader(entity.getContent(), Charset.forName("utf-8")));
                StringBuilder sb = new StringBuilder(1024);

                String item;
                while(!StringUtil.isNullOrEmpty(item = reader.readLine())) {
                    sb.append(item);
                }

                String result = sb.toString();
                logger.debug("\nurl[{}]\nresponse Headers[{}]\nresponse Status[ protocolVersion: {}, statusCode: {}, reasonPhrase: {}, statusLine: {}]\nresult[{}]\ncost time[{} mills.]", new Object[]{httpUriRequest.getRequestLine().getUri(), getReponseHeaders(response), response.getProtocolVersion(), Integer.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase(), response.getStatusLine().toString(), result});
                logger.info("\nurl[{}]\nresponse Status[ protocolVersion: {}, statusCode: {}, reasonPhrase: {}, statusLine: {}]\nresponse[{}]", new Object[]{httpUriRequest.getRequestLine().getUri(), response.getProtocolVersion(), Integer.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase(), response.getStatusLine().toString(), result});
                String var9 = result;
                return var9;
            }
        } catch (Exception var18) {
            throw ExceptionUtil.unchecked(var18);
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }

                if(response != null) {
                    response.close();
                }
            } catch (IOException var17) {
                throw ExceptionUtil.unchecked(var17);
            }

        }
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        if(request == null) {
            throw new RuntimeException("request is null", (Throwable)null);
        } else {
            String accept = request.getHeader("accept");
            return accept != null && accept.length() > 0 && accept.indexOf("application/json") > -1 || request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest");
        }
    }

    public static String getCurrentUrl(HttpServletRequest request) {
        return request.getPathInfo() == null?request.getServletPath():request.getServletPath() + request.getPathInfo();
    }
}

