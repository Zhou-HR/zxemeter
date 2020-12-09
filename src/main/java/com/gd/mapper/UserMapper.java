package com.gd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.User;
import com.gd.model.po.UserLog1;

/**
 */
public interface UserMapper {
	
    User getUserByName(String name);
    
    List<User> getUserEmailNotNull();

    int removeUserRoleByUserId(int id);
    
    int removeMenuUserRoleByUserId(int id);
    
    int removeUserWechatByUserId(int id);

    void saveUserRole(@Param("userId") int userId, @Param("roleIds") List<String> roleIds);
    
    void saveMenuUserRole(@Param("userId") int userId, @Param("roleIds") List<String> roleIds);
    
    void saveUserWechat(@Param("userId") int userId, @Param("wechatIds") List<String> wechatIds);
    
    void insertUserLog(UserLog1 userLog1);
}
