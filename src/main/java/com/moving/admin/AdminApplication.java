package com.moving.admin;

import com.moving.admin.bean.TokenInformation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableJpaRepositories(basePackages = "com.moving.admin.dao")
@EntityScan(basePackages = "com.moving.admin.entity")
@EnableScheduling
@SpringBootApplication
//public class AdminApplication extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(AdminApplication.class);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(AdminApplication.class, args);
//    }
//
//}
public class AdminApplication extends SpringBootServletInitializer {

    public static List<TokenInformation> logins = new ArrayList<>();

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdminApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
