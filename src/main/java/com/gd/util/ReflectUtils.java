package com.gd.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * @author haiyan.li
 *
 */
public class ReflectUtils {
	/**
     * 通过对象和具体的字段名字获得字段的值
     * @param obj
     * @param filed
     */
    public static Object getFiledValue(Object obj, String filed) {  
        try {  
            Class<? extends Object> clazz = obj.getClass();  
            PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);  
            Method getMethod = pd.getReadMethod();//获得get方法  
            if (pd != null) {  
                return getMethod.invoke(obj);//执行get方法返回一个Object  
            }  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IntrospectionException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
    
}
