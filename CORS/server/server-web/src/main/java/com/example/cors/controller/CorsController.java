package com.example.cors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by chang on 2017/3/18.
 */
@Controller
@RequestMapping("/server")
public class CorsController {

    @RequestMapping(value="/cors", method= RequestMethod.GET)
    @ResponseBody
    public String ajaxCors(HttpServletRequest request) throws Exception{
        return "SUCCESS";
    }

    @RequestMapping(value="/options", method= RequestMethod.POST)
    @ResponseBody
    public String options(HttpServletRequest request) throws Exception{
        return "SUCCESS";
    }

    @RequestMapping(value="/testCookie", method= RequestMethod.GET)
    @ResponseBody
    public String testCookie(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String str = "SUCCESS";
        Cookie[] cookies = request.getCookies();
        String school = getSchool(cookies);
        if(school == null || school.length() == 0){
            addCookie(response);
        }
        return str + buildText(cookies);
    }

    private String buildText(Cookie[] cookies){
        String text = "";
        if(cookies!=null){
            for(int i=0; i<cookies.length;i++){
                text = text + new StringBuilder().append("<br/>")
                        .append(cookies[i].getName())
                        .append("=")
                        .append((cookies[i].getValue())).toString();
            }
        }
        return text;
    }

    private static String getSchool(Cookie[] cookies) {
        String school = "";
        if(cookies == null){
            return school;
        }

        int len = cookies.length;
        for(int i = 0; i < len; i++) {
            Cookie cookie = cookies[i];
            if(cookie != null && "school".equals(cookie.getName())) {
                school = cookie.getValue();
                break;
            }
        }
        return school;
    }


    private static void addCookie(HttpServletResponse response) {
        int maxAge = -1;
        Cookie cookie = new Cookie("school", "seu");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private static void removeCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
