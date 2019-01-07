package com.moving.admin.filter;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration<T> {
    @Bean
    public FilterRegistrationBean<Filter> filterRegister() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OriginFilter());
        registration.setName("OriginFilter");
        registration.setOrder(1);
        return registration;
    }
}
