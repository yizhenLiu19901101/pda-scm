package com.zhixuanda.zuul;

import com.zhixuanda.zuul.fallBack.PdaFallBack;
import com.zhixuanda.zuul.filter.AccessTokenFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * zuul启动类
 * @author milo
 */
@SpringBootApplication
@EnableEurekaClient
// 开启网关
@EnableZuulProxy
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
