package com.zhixuanda.zuul.conf;

import com.netflix.zuul.http.HttpServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 重写URL
 */
public class RewriteURIRequestWrapper extends HttpServletRequestWrapper {
    private String rewriteURI;

    public RewriteURIRequestWrapper(HttpServletRequest request, String rewriteURI) {
        super(request);
        this.rewriteURI = rewriteURI;
    }

    @Override
    public String getRequestURI() {
        return rewriteURI;
    }

}
