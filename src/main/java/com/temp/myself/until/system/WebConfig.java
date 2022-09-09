package com.temp.myself.until.system;

import com.temp.myself.until.filter.XssFilter3;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加载过滤器
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean reqResFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        XssFilter3 xssFilter3 = new XssFilter3();
        filterRegistrationBean.setFilter(xssFilter3);
        //filterRegistrationBean.addUrlPatterns("*.json");//配置过滤规则
        //filterRegistrationBean.addInitParameter("name","xss");//设置init参数
        filterRegistrationBean.setName("xssFilter3");//设置过滤器名称
        filterRegistrationBean.setOrder(1);//执行次序
        return filterRegistrationBean;
    }


}
