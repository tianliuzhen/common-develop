package com.aaa.commondevelop.httpclient;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 TestLoginByToken.java  2020/10/22 19:21
 */
public class TestLoginByToken {

    public static void main(String[] args) {
        // 获取token
        login();
    }

    @SneakyThrows
    public static void login(){
        //1. 模拟登录
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionTimeToLive(6000, TimeUnit.MILLISECONDS).build();
        HttpPost httpPost = new HttpPost("http://mgr.zhibi365.cn/cb/user/login");
        httpPost.setHeader(new BasicHeader("Content-type", "application/json;charset=UTF-8"));
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        // post传json 参数
        StringEntity requestEntity = new StringEntity("{\"username\":\"tianliuzhen\",\"password\":\"123456\"}","utf-8");
        httpPost.setEntity(requestEntity);
        HttpResponse response = httpClient.execute(httpPost);

        //2. 找到需要返回的 名为  cbToken 的
        String res = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(res);
        String cbToken = jsonObject.getJSONObject("result").getString("cbToken");

        //3. 利用token 传递，请求别的接口
        // get表单传参（url拼参）
        URI uri = new URIBuilder("http://mgr.zhibi365.cn/cb/main/dicts?keys=country").setParameter("keys", "country").build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(new BasicHeader("cbToken", cbToken));
        // httpGet.setHeader("cbToken",cbToken);
        HttpResponse response2 = httpClient.execute(httpGet);
        String res2 = EntityUtils.toString(response2.getEntity());
        System.out.println(res2);


        //关闭流
        httpClient.close();

    }
}
