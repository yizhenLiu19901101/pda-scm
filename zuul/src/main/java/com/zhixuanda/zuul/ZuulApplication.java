package com.zhixuanda.zuul;

import com.fasterxml.jackson.core.filter.TokenFilter;
import com.zhixuanda.zuul.fadeBack.PdaFadeBack;
import com.zhixuanda.zuul.filter.PdaFilter;
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
@EnableZuulProxy
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

    //将过滤器交给Spring管理
    @Bean
    public PdaFilter pdaFilter(){
        return new PdaFilter();
    }

    //将fadeBackProvider交给Spring管理
    @Bean
    public PdaFadeBack pdaFadeBack(){
        return new PdaFadeBack();
    }

}
