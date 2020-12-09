package com.gd.basic.security;

import lombok.extern.log4j.Log4j2;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 */
@Log4j2
public class CustomExceptionTranslationFilter extends ExceptionTranslationFilter {

    private RequestCache requestCache = new HttpSessionRequestCache();

    public CustomExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationEntryPoint);
    }

    public CustomExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint, RequestCache requestCache) {
        super(authenticationEntryPoint, requestCache);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (Exception e) {
            log.error(e);

            //todo 异常信息描述
            
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            if (request.getHeader("x-requested-with") != null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
            } else {
                response.sendRedirect("http://47.103.102.210:7081/emeter/errors/error.jsp");
            }
        }
    }

    
    @Override
    protected void sendStartAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, AuthenticationException reason) throws ServletException, IOException {
//        * 这个方法的目的就是让用户重新去登录，默认如下：
//        * 一种情况是，未存在用户信息时，让用户去重新登录
//        * 还有种情况是，存在用户信息，但是这个用户是匿名用户或者是通过remember-me方式进入系统的，当其出现访问拒绝时，让其去重新登录，并且删除用户信息
//        *
//        * 但是对于AccessDeniedException，匿名用户我不想让其重新登录，不能访问就显示不能访问就行了，所以这里重写了这个方法，并注释掉了清除用户信息的代码
        // SEC-112: Clear the SecurityContextHolder's Authentication, as the
        // existing Authentication is no longer considered valid
        requestCache.saveRequest(request, response);
        logger.debug("Calling Authentication entry point.");
        //20190709yxl 林安康的外链不支持https，导致https不可用，并且session失效，导致http登录也失败的严重问题
        //所以basePath只能写死在配置文件里，不能从request获取
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        
        //删除cookie 根本没用
        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
        	for(Cookie cookie:cookies){
            	System.out.println(cookie.getName()+"|"+cookie.getValue());
            }
        }
        
        
        if (reason instanceof InsufficientAuthenticationException) {
            // 这种情况代表当前用户是匿名用户或者remember-me用户，出现了拒绝访问的情况
            if (request.getHeader("x-requested-with") != null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            } else {
                response.sendRedirect(basePath+"errors/access-denied.jsp");
            }
        } else {
            // 跳转到登录页面
            SecurityContextHolder.getContext().setAuthentication(null);
            if (request.getHeader("x-requested-with") != null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no login");
            } else {
                response.sendRedirect(basePath+"login.jsp");
            }
        }
    }

}
