package com.zhixuanda.zuul.service.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 用户业务调用
 */
@FeignClient(value = "pda-service")
public interface UserService {
    @GetMapping("/findById/{id}")
    public Map findById(@PathVariable("id") String id);
}
