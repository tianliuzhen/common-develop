package com.aaa.commondevelop.httpclient;

import lombok.SneakyThrows;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.concurrent.TimeUnit;

/**
 * 通过 登录lazada卖家平台获取 cookie 查到 json数据
 * @author liuzhen.tian
 * @version 1.0 TestLoginByToken.java  2020/10/22 19:21
 */
public class TestLoginByCookie {

    public static void main(String[] args) {
        // 获取token
        login();
    }

    @SneakyThrows
    public static void login(){
        //1. 模拟登录  通过卖家 https://gsp.lazada-seller.cn/ 点击登录分析出 登陆的url
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(60000)
                        .setConnectionRequestTimeout(5000)
                        .setSocketTimeout(60000).build())
                .setConnectionTimeToLive(5, TimeUnit.MINUTES)
                .setDefaultCookieStore(cookieStore).build();
        String account = "lazada3@malling.co";
        String password = "Dingcheng65j43";
        String authorizedCountry = "my";
        String appId="113675";
        String authUrl = "https://uac.lazada.com/seller/login?" +
                "account=" + account +
                "&password=" + password +
                "&country=" + authorizedCountry +
                // "&redirect_url=https%3A%2F%2Fauth.lazada.com%2Foauth%2Fauthorize%3Fresponse_type%3Dcode%26redirect_uri%3Dhttps%253A%252F%252Flong201805.vicp.cc%253A9800%252Fcb%252Flazada%252Fcallback%26force_auth%3Dtrue%26client_id%3D" + appId + "%26redirect_auth%3Dtrue" +
                "&redirect_url=/" +
                "&from=lazop_auth" +
                "&callback=__jp1";
        HttpGet authHttp = new HttpGet(authUrl);
        CloseableHttpResponse response = httpClient.execute(authHttp);
        if (response.getStatusLine().getStatusCode() == 200) {
            String s = EntityUtils.toString(response.getEntity(), "UTF-8").replaceAll("[ \n\t\r]", "");
            boolean success = s.contains("\"success\":true");
            if (success) {
                //进 我的页面获得 访问其他接口的权限（累计cookie）
                httpClient.execute(new HttpGet("https://sellercenter.lazada.com.my"));
            }
        }

        //2. 访问数据  httpClient4.0 后会自动保存cookie
        String url = "https://sellercenter.lazada.com.my/ba/sycm/lazada/dashboard/key/overview.json";
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.setParameter("dateType", "day");
        uriBuilder.setParameter("dateRange", "2020-10-22|2020-10-22");
        HttpGet dashboardGet = new HttpGet(uriBuilder.build());
        //GET https://sellercenter.lazada.com.my/ba/sycm/lazada/dashboard/key/overview.json?dateType=day&dateRange=2020-10-22%7C2020-10-22 HTTP/1.1
        CloseableHttpResponse response2 = httpClient.execute(dashboardGet);
        if (response2.getStatusLine().getStatusCode() == 200) {
            String pageContent = EntityUtils.toString(response2.getEntity(), "UTF-8");
            System.out.println(pageContent);
        }
        //关闭流
        httpClient.close();
    }
}
