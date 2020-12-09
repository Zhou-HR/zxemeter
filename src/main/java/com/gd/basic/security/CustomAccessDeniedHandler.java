package com.gd.basic.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在ExceptionTranslationFilter中，默认使用的是AccessDeniedHandlerImpl
 * <p>
 * 在AccessDeniedHandlerImpl的实现中，采取的是forward的方式进行错误页面的跳转，
 * 而这个forward方式的跳转存在个问题，它会再次访问原来的路径，这个问题暂时还没明白为什么。
 * （猜测：可能和返回码与浏览器有关，它除了进行forward外，还会返回一个403的错误码，可能不同的浏览器处理这个403的方式不同，我用火狐浏览器时，用这个类就未出现问题）
 * <p>
 * 为了解决上面的问题这里更改它的处理方式，一是改为redirect跳转，另外是增加ajax请求时的处理
 *
 * @author ZhouHR
 * @see org.springframework.security.web.access.ExceptionTranslationFilter
 * @see org.springframework.security.web.access.AccessDeniedHandlerImpl
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            String s = request.getHeader("x-requested-with");
            if (s == null) {
                response.sendRedirect(errorPage);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            }
        }
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
