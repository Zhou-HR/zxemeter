package com.gd.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Web层工具类
 * @author buzhidao
 *
 */
public class WebUtil {
	

	/**
	 * 获取登录人的微信公众号ID
	 * @param request
	 * @return
	 */
	public static String getOpenid(HttpServletRequest request){
		HttpSession session = request.getSession();
		Object obj=session.getAttribute("currentOpenid");
		if(obj!=null) {
			return (String) session.getAttribute("currentOpenid");
		}
		return null;
	}
	
	
	
	/**
	 * 获取当前登录用户ID
	 * @param request
	 * @return
	 */
    public static String getUserId(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	return String.valueOf(session.getAttribute("userId"));
    }
}
