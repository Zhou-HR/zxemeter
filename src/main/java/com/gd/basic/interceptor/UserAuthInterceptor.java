package com.gd.basic.interceptor;

import com.gd.model.po.User;
import com.gd.model.po.UserLog1;
import com.gd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
public class UserAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ThreadUtil threadUtil;

    public UserAuthInterceptor() {
        System.out.println("======================UserAuthInterceptor=============================");
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //获取项目名
        String projectName = request.getContextPath();
        //请求带的参数
        String queryString = request.getQueryString();
        //请求的当前资源
        //bancinsurance
        String requestUri = request.getRequestURI().replaceAll(projectName, "");

        UserLog1 userLog1 = new UserLog1();
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && requestUri.indexOf("saveOpType") == -1) {
            Map<String, String[]> pramMap = request.getParameterMap();
            StringBuffer sbf = new StringBuffer();
            int count = 0;
            String forCunt = "";
            for (Map.Entry<String, String[]> entry : pramMap.entrySet()) {
                forCunt = "[" + count + "]" + " : ";
                sbf.append("paramName" + forCunt + entry.getKey() + " - " + "paramValue" + forCunt + request.getParameter(entry.getKey()) + "\n");
                count++;
            }
            String ip = getIpAddr(request);
            userLog1.setIp(ip);
            userLog1.setUserId(user.getName());
            userLog1.setUserName(user.getRealname());
            userLog1.setMethod(requestUri);
            userLog1.setParam(sbf.toString());
            threadUtil.insertUserLog(userLog1);
        }

        Map<String, Integer> mapTopMenuPermission = (Map<String, Integer>) request.getSession().getAttribute("mapTopMenuPermission");
        if (mapTopMenuPermission == null) {
            return true;//admin???
        }
        if (requestUri.indexOf("toList") != -1) {
            requestUri = requestUri.substring(1);
            if (mapTopMenuPermission.get(requestUri) != null) {
                return true;
            } else {
                response.sendRedirect("../errors/access-denied.jsp");
                return false;
            }
        }

        if (requestUri.indexOf("station/toListMap") != -1) {
            response.sendRedirect("../errors/access-denied.jsp");
            return false;
        }

        return true;
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
    }

}
