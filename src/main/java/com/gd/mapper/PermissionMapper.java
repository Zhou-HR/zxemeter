package com.gd.mapper;

import com.gd.model.po.Permission;

import java.util.List;

/**
 */
public interface PermissionMapper {
	
	void insert(Permission permission);

    int delete(String ids);

    int deleteRolePermission(String ids);

    List<Permission> getPermissionsByRoleId(int roleId);
    
    List<Permission> getMenuPermissionsByRoleId(int roleId);

    List<Permission> getPermissionsByUserId(int userId);
    
    List<Permission> getMenuPermissionsByUserId(int userId);
}
