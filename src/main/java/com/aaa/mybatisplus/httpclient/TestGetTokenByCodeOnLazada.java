package com.aaa.mybatisplus.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取lazada的后台访问的 code 和 token
 *
 * @author liuzhen.tian
 * @version 1.0 TestGetTokenByCodeOnLazada.java  2020/11/20 18:20
 */
public class TestGetTokenByCodeOnLazada {
    private final static Pattern SESSION_PATTERN = Pattern.compile("JSID=(\\w*?)(\\W|$)");
    private final static Pattern AUTH_CODE_PATTERN = Pattern.compile("code=(.*)(&|$)");

    public static void main(String[] args) throws IOException, InterruptedException {
        getLazadaAccessToken("lazada25@malling.co", "Guojj168168", "cb", "116907");
    }

    public static JSONObject getLazadaAccessToken(String account, String password, String authorizedCountry, String appId) throws IOException, InterruptedException, ApiException {

        HttpClient httpClient = HttpClient.newHttpClient();
        String sessionId = null;

        String loginUrl = "https://uac.lazada.com/seller/login?" +
                "account=" + URLEncoder.encode(account, StandardCharsets.UTF_8) +
                "&password=" + password +
                "&country=" + authorizedCountry +
                "&redirect_url=https%3A%2F%2Fauth.lazada.com%2Foauth%2Fauthorize%3Fresponse_type%3Dcode%26redirect_uri%3Dhttps%253A%252F%252Flong201805.vicp.cc%253A9800%252Fcb%252Flazada%252Fcallback%26force_auth%3Dtrue%26client_id%3D" + appId + "%26redirect_auth%3Dtrue" +
                "&from=lazop_auth";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        for (String cookie : response.headers().allValues("set-cookie")) {
            Matcher matcher = SESSION_PATTERN.matcher(cookie);
            if (matcher.find()) {
                sessionId = matcher.group(1);
            }
        }
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }
        JSONObject body = JSON.parseObject(response.body());
        if (body.getBooleanValue("success")) {
            JSONObject data = body.getJSONObject("data");
            String redirectUrl = data.getString("redirect_url");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(redirectUrl))
                    //设置登录后的cookie
                    .header("Cookie", "JSID=" + sessionId)
                    .build();
            HttpClient nonRedirectHttpClient = HttpClient.newHttpClient();
            response = nonRedirectHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            //确认 http 状态是转发
            if (response.statusCode() == 301) {
                String location = response.headers().allValues("location").get(0);
                Matcher matcher = AUTH_CODE_PATTERN.matcher(location);
                if (matcher.find()) {
                    String code = matcher.group(1);
                    System.out.println(code);
                }
            }
        }

        return null;
    }

}
