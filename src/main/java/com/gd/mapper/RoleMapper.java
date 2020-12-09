package com.gd.mapper;

import com.gd.model.po.Role;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZhouHR
 */
public interface RoleMapper {

    List<Role> getRolesByUserId(int userId);

    List<Role> getMenuRolesByUserId(int userId);

    List<Role> getRolesByPermissionId(int permissionId);

    int removeRolePermissionByRoleId(int roleId);

    int removeMenuRolePermissionByRoleId(int roleId);

    int insert(Role role);

    int insertMenu(Role role);

    int removeUserRoleByRoleId(int roleId);

    int removeMenuUserRoleByRoleId(int roleId);

    void saveRolePermission(@Param("roleId") int roleId, @Param("permissionIds") List<String> permissionIds);

    void saveMenuRolePermission(@Param("roleId") int roleId, @Param("permissionIds") List<String> permissionIds);
}
