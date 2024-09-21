package com.aaa.commondevelop.httpclient;

import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CloseableHttpClient使用示例
 * @author liuzhen.tian
 * @version 1.0 CloseableHttpClientTest.java  2020/10/22 17:57
 */
public class CloseableHttpClientTest {

    public static void main(String[] args) {

        /**
         * 使用帮助类HttpClients创建CloseableHttpClient对象.
         * 基于要发送的HTTP请求类型创建HttpGet或者HttpPost实例.
         * 使用addHeader方法添加请求头部,诸如User-Agent, Accept-Encoding等参数.
         * 可调用HttpGet、HttpPost共同的setParams(HetpParams params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
         * 通过执行此HttpGet或者HttpPost请求获取CloseableHttpResponse实例
         * 从此CloseableHttpResponse实例中获取状态码,错误信息,以及响应页面等等.
         * 释放连接。无论执行方法是否成功，都必须释放连接
         *
         */

        // doGet();
        // doPost();

    }

    @SneakyThrows
    public static void doGet() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("http://www.thepaper.cn");
        // 很奇怪，使用CloseableHttpClient来请求澎湃新闻的首页，GTE请求也必须加上下面这个Header，但是使用HTTPClient则不需要
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        HttpResponse response = client.execute(get);
        String res = EntityUtils.toString(response.getEntity());
    }

    @SneakyThrows
    public static void doPost() {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://www.thepaper.cn/www/commentPraise.msp");
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        // 表单传参
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("commentId", "18718372"));
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse response = client.execute(post);
        String res = EntityUtils.toString(response.getEntity());
        System.out.println(res);
        client.close();

    }

    @SneakyThrows
    public static void doPost2() {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://www.thepaper.cn/www/commentPraise.msp");

        // json传参
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        String js = "{\"scene\":\"id=1\"}";
        StringEntity se = new StringEntity(js);
        post.setEntity(se);

        HttpResponse response = client.execute(post);
        String res = EntityUtils.toString(response.getEntity());
        System.out.println(res);
        client.close();

    }

}
