package com.moving.admin.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class ConfigAdapter extends WebMvcConfigurerAdapter {
    @Autowired
    public RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 关于 registry，可进行添加拦截路径，以及免拦截路径
        registry.addInterceptor(requestInterceptor);
        super.addInterceptors(registry);
    }
}
