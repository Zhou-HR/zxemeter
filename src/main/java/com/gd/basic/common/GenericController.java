package com.gd.basic.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.gd.util.PropertiesUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author yff
 *
 */
@Controller
@Slf4j
public abstract class GenericController{
	
	/*public static String HEAD;
	
	static {
		UaiCriteria<DataDict> criteria = DynamicQueryUtil.getCriteria(DataDict.class);
		criteria.andEquals("dicCode", "BACK_URL");
		DataDict dict=criteria.getSingleResult();
		HEAD=dict.getDicValue();
	}
	
    public static String getSourceUserId(){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TenantUser user = (TenantUser) authentication.getPrincipal();
    	if("0".equals(user.getSource()))
    		return user.getId();
    	else
    		return user.getSource();
    }
*/
	/**
	 * 获取当前登录用户ID
	 * @param request
	 * @return
	 */
    public static String getUserId(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	return String.valueOf(session.getAttribute("userId"));
    }
    
    public static String getInterfaceMediaUrl(){
    	return (String) PropertiesUtil.getInstance().getProperty("interface.properties", "INTERFACE_MEDIA_URL");
    }
    
    public static String getInterfaceImgUrl(){
    	return (String) PropertiesUtil.getInstance().getProperty("interface.properties", "INTERFACE_IMG_URL");
    }
    
    /**
     * 获取当前微信openid
     * @param request
     * @return
     */
    public static String getCurrentOpenid(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	return (String) session.getAttribute("currentOpenid");
    }
    
    public static String getCurrentType(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	return (String) session.getAttribute("currentType");
    }
	
	public static String getInnerImgUrl(){
    	return (String) PropertiesUtil.getInstance().getProperty("interface.properties", "INNER_IMG_URL");
    }
	
	public static String getInterfaceMassPreview(){
    	return (String) PropertiesUtil.getInstance().getProperty("interface.properties", "INTERFACE_MASS_PREVIEW");
    }
	
	public static String getInterfaceMediaDel(){
    	return (String) PropertiesUtil.getInstance().getProperty("interface.properties", "INTERFACE_MEDIA_DEL");
    }
	
	public static String getInterfaceMediaUpdate(){
    	return (String) PropertiesUtil.getInstance().getProperty("interface.properties", "INTERFACE_MEDIA_UPDATE");
    }
	
}
