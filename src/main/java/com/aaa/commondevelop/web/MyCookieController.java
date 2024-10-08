package com.aaa.commondevelop.web;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 做了个简简单单的小例子去记录一下，怎么使用这个Cookie，
 * 虽然现在用了shiro去管控登录，里面用的是token，但是说不定以后得用上这个。
 * 参考：https://www.cnblogs.com/xichji/p/11793439.html
 *
 * @author liuzhen.tian
 * @version 1.0 MyGetCookieController.java  2020/12/1 9:58
 */
@RestController
@RequestMapping("cookies")
public class MyCookieController {


    /**
     * 通过请求设置cookie信息
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/setCookies", method = RequestMethod.GET)
    public String setCookies(HttpServletResponse response, HttpServletRequest request) {
        // CookieTestInfo 这里面可以塞，用户信息或者认证token等等
        Cookie cookie = new Cookie("sessionId", "CookieTestInfo");

        /**
         * 默认情况下,cookie是一个会话级别的,用户推出浏览器后被删除。
         */
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7天过期

        /**
         *
         * 我们需要了解一个概念：什么的安全的Cookies？
         * 安全的cookie是仅可以通过加密的HTTPS连接发送到服务器的cookie。
         * 无法通过未加密的HTTP连接将cookie发送到服务器。
         * 也就是说，如果设置了setSecure(true)，该Cookie将无法在Http连接中传输，只能是Https连接中传输。
         */
        // cookie.setSecure(true);  //Https 安全cookie

        /**
         * HttpOnly cookie用于防止跨站点脚本（XSS）攻击，
         * 也就是说设置了Http Only的Cookie不能通过JavaScript的Document.cookieAPI访问，
         * 仅能在服务端由服务器程序访问。
         */
        // cookie.setHttpOnly(true);  //不能被js访问的Cookie

        /**
         * cookie共享的路径，如果默认不填，
         * 会自动解析 http://localhost:8080/cookies/setCookies 中的  cookies   作为 Path
         * 会自动解析 http://localhost:8080/cookies/setCookies 中的  localhost 作为 Domain
         */
        cookie.setPath("/");
        /**
         *  todo 设置域名  domain参数必须以点(".")开始 ,
         *  很奇怪。后端这里如果是 . 开头，会直接报错 "An invalid domain [.local.cvb] was specified for this cookie
         *  不以 . 开头，浏览器保存cookie会默认 带上.开头
         */
        cookie.setDomain(".local.cvb");

        response.addCookie(cookie);
        return "添加cookies信息成功";
    }

    /**
     * 要删除Cookie，需要将Max-Age设置为0，并且将Cookie的值设置为null。
     * 不要将Max-Age指令值设置为-1负数。否则，浏览器会将其视为会话cookie。
     */
    @RequestMapping(value = "/delete")
    public void deleteCookie() {
        // 将Cookie的值设置为null
        Cookie cookie = new Cookie("username", null);
        //将`Max-Age`设置为0
        cookie.setMaxAge(0);
    }

    /**
     * 非注解方式获取cookie中对应的key值
     */
    @RequestMapping(value = "/getCookies", method = RequestMethod.GET)
    public String getCookies(HttpServletRequest request) {
        //   Cookie cookie=new Cookie("sessionId","CookieTestInfo");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    return cookie.getValue();
                }
            }
        }
        return "无法获取到cookie";
    }

    /**
     * 注解方式获取cookie中对应的key值
     */
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("sessionId") String sessionId) {
        //前提是已经创建了或者已经存在cookie了，那么下面这个就直接把对应的key值拿出来了。
        System.out.println("testCookieValue,sessionId=" + sessionId);
        return "SUCCESS";
    }

}
