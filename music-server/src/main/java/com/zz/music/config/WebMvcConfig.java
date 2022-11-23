package com.zz.music.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/*
解决跨域问题
 */

////springboot启动的时候会自动加载里面的方法
////声明
//@Configuration
////继承
//public class WebMvcConfig extends WebMvcConfigurerAdapter {
//    public void addCorsMappings(CorsRegistry registry) {
////        对哪些目录可以使用跨域访问：所有目录
////        允许哪些网站对这里的方法进行跨域访问：所有
////        允许哪些方法进行跨域访问：所有
////        访问时是否需要验证：是的
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowCredentials(true);
//    }
//}

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}