package com.temp.myself.until;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源映射服务器地址
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${web.uploadPath}")
    private String baseUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/static/**").addResourceLocations("file:"+baseUploadPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
