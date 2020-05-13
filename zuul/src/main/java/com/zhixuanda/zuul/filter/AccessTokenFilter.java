package com.zhixuanda.zuul.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zhixuanda.zuul.service.feign.UserService;
import com.zhixuanda.zuul.util.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserService userService;

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
        logger.info("method {} ,url {}", request.getMethod(), request.getRequestURL().toString());
        if (token == null) {
            logger.info("token 为空");
            ctx.setSendZuulResponse(false);// 对该请求进行路由
            ctx.setResponseStatusCode(400);
            ctx.set("isSuccess", false);// 设值，让下一个Filter看到上一个Filter的状态
            return null;
        } else {
            //判断用户当前状态是否为正常状态
            logger.info(token);
            String userId = JWT.unsign(token,String.class);
            Map userResult = userService.findById(userId);
            Map user = (Map) userResult.get("userVo");
            if(user == null ){
                logger.info("用户不存在");
            }else{
                logger.info("用户状态正常");
            }
            request.setAttribute("id",user.get("id"));
            request.setAttribute("userId",user.get("userId"));
            ctx.setRequest(request);
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

