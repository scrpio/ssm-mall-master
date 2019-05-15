package com.mall.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        res.setHeader("Content-type", "text/html;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:8082");
        res.setHeader("Access-Control-Allow-Methods", "OPTIONS, HEAD, PATCH, PUT, POST, GET, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Origin,No-Cache,Pragma,Expires,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Last-Modified,Cache-Control,Content-Type,X-E4M-With,userId,token");
        res.addHeader("Access-Control-Max-Age", "18000");
        res.setHeader("Location", req.getRequestURI());
        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {

    }
}
