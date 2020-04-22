package com.zhixuanda.zuul.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "auth")
public class AccessTokenFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    public List<String> getOpenUrls() {
        return openUrls;
    }

    public void setOpenUrls(List<String> openUrls) {
        this.openUrls = openUrls;
    }

    private List<String> openUrls;

    /**
     * 过滤器类型
     * pre 路由前被调用
     * routing 路由时被调用
     * post 在routing和error后被调用
     * error 处理请求发生错误时被调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 通过int值来定义过滤器的执行顺序，数值越小优先级越高
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }

    /**
     * 返回一个boolean值来判断该过滤器是否要执行
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if ("OPTIONS".equals(request.getMethod())) {
            ctx.setSendZuulResponse(true);// 对该请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
            return null;
        }
        if (isOpenUrl(request.getRequestURI())) {
            ctx.setSendZuulResponse(true);// 对该请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
            return null;
        }
        final String token = request.getHeader("token");
        final String uuid = "";
        logger.info("method {} ,url {}", request.getMethod(), request.getRequestURL().toString());
        if (uuid == null) {
            logger.info("uuid 为空");
            return null;
        } else {
            //判断用户当前状态是否为正常状态
            logger.info(uuid);
            Map user = new HashMap<>();
            if(user == null ){
                logger.info("用户不存在");
            }else{
                logger.info("用户状态正常");
            }
            ctx.setSendZuulResponse(true);// 对该请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
            return null;
        }
    }


    boolean isOpenUrl(String userAccessUrl) {

        for (String url : openUrls
                ) {
            if (userAccessUrl.matches(url)) {
                return true;
            }
        }
        return false;
    }

}

