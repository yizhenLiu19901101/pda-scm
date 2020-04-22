package com.zhixuanda.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zhixuanda.zuul.conf.RewriteURIRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(FileFilter.class);

    /**
     *  重定向的规则,根据key来重定向到val.
     */
    @Value("${file-service.fileter-pattern}")
    private String filterPattern;
    @Value("${file-service.replace-url}")
    private String relpaceUrl;
    @Value("${file-service.fileter-app-pattern}")
    private String filterAppPattern;

    private static List<String> replaceServiceList = new ArrayList<>();
    static {
        replaceServiceList.add("business");
        replaceServiceList.add("businesserqi");
        replaceServiceList.add("wgb");
    }
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if(request.getMethod().equals("GET")){
            String requestURI = request.getRequestURI();

            //app 文件服务器id.文件后缀 格式
            Pattern pattern = Pattern.compile(filterAppPattern,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(requestURI);
            if(matcher.find()){
                //匹配路由重定向
                String[] split = requestURI.split("/");
                if(null == split || split.length < 3){
                    return null;
                }
                String fileServiceName = split[split.length - 2];
                // 根据配置好的去将url替换掉
                requestURI = requestURI.replaceFirst("/"+fileServiceName+"/", relpaceUrl);

                requestURI = requestURI.substring(0,requestURI.indexOf("."));

                // 将替换掉的url set进去,在对应的转发请求的url就会使用这个url
                ctx.setRequest(new RewriteURIRequestWrapper(request,requestURI));

                logger.info("路由重置 包含.文件后缀过滤 {}",requestURI);
                return null;
            }

            //普通 文件服务器id 格式
            pattern = Pattern.compile(filterPattern,Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(requestURI);
            if(matcher.find()){
                //匹配路由重定向
                String[] split = requestURI.split("/");
                if(null == split || split.length < 3){
                    return null;
                }
                String fileServiceName = split[split.length - 2];
                // 根据配置好的去将url替换掉
                requestURI = requestURI.replaceFirst("/"+fileServiceName+"/", relpaceUrl);
                // 将替换掉的url set进去,在对应的转发请求的url就会使用这个url
                ctx.setRequest(new RewriteURIRequestWrapper(request,requestURI));

                logger.info("路由重置 {}",requestURI);
            }

        }
        return null;
    }
}
