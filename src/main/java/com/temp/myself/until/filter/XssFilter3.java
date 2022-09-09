package com.temp.myself.until.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.temp.myself.until.XssAndSqlHttpServletRequestWrapper;
import com.temp.myself.until.XssHttpServletRequestWrapper3;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Xss漏洞过滤器
 */
public class XssFilter3 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        //获取请求数据
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        //获取请求的url路径
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        //声明要被忽略请求的数组
        String[] exclusionsUrls = {".js", ".gif", ".jpg", ".jpeg", ".png", ".css", ".ico", "health", "uploadPic", "file", "savePrintTemp", "goods/create", "goods/edit", "goodsBatchImport", "goodsBatchExport","platformtestreport"};
        //声明带有富文本的接口数组
        String[] richTextUrls = {"addarticles", "editarticles", "advert"};
        //第一种xss过滤
        XssAndSqlHttpServletRequestWrapper XssAndSqlHttpServletRequestWrapper = new XssAndSqlHttpServletRequestWrapper(req);
        //遍历忽略的请求数组，若该接口url为忽略的就调用原本的过滤器，不走xss过滤
        for (String str : exclusionsUrls) {
            if (path.contains(str)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        //若为带有富文本的接口，走第一种xss过滤
        for (String rtu : richTextUrls) {
            if (path.contains(rtu)) {
                filterChain.doFilter(XssAndSqlHttpServletRequestWrapper, servletResponse);
                return;
            }
        }
        //将请求放入XSS请求包装器中，返回过滤后的值
        /*XssHttpServletRequestWrapper3 xssRequestWrapper3 = new XssHttpServletRequestWrapper3(req);
        filterChain.doFilter(xssRequestWrapper3, servletResponse);*/

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
