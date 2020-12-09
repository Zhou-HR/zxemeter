package com.gd.user.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gd.basic.crud.CrudService;
import com.gd.basic.security.SecurityMetadataSourceWrapper;
import com.gd.mapper.PermissionMapper;
import com.gd.mapper.RoleMapper;
import com.gd.model.po.Permission;
import com.gd.model.po.Role;
import com.gd.model.po.RoleMenu;

/**
 */
@Service
@Log4j2
public class RoleMenuService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private SessionRegistry sessionRegistry;

    private CrudService crudService = CrudService.of(RoleMenu.class);

    @Transactional
    public void save(Role role, String permissions) {
//        int roleId = crudService.insertAndRtnKey(role);
    	int roleId=roleMapper.insertMenu(role);
        saveRolePermission(role.getId(), permissions);

        SecurityMetadataSourceWrapper.refresh();
    }

    @Transactional
    public void edit(RoleMenu role, String permissions) {
        roleMapper.removeMenuRolePermissionByRoleId(role.getId());
        crudService.update(role);
        saveRolePermission(role.getId(), permissions);

        SecurityMetadataSourceWrapper.refresh();
    }

    @Transactional
    public void remove(int roleId) {
        RoleMenu role = findById(roleId);

        crudService.delete(roleId);
        roleMapper.removeMenuRolePermissionByRoleId(roleId);
        roleMapper.removeMenuUserRoleByRoleId(roleId);

        SecurityMetadataSourceWrapper.refresh();
        refreshUserSession(role.getName());
    }

    @Transactional
    public void saveRolePermission(int roleId, String permissionIds) {
        String[] strings = StringUtils.split(permissionIds, ",");
        
        if (ArrayUtils.isNotEmpty(strings)) {
            roleMapper.saveMenuRolePermission(roleId, Arrays.asList(strings));
        }
    }

    public RoleMenu findById(int roleId) {
        return crudService.findById(roleId);
    }

    public List<Permission> getPermissionByRoleId(int roleId) {
        return permissionMapper.getMenuPermissionsByRoleId(roleId);
    }

    public void refreshUserSession(String roleName) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            User user = (User) principal;
            Collection<GrantedAuthority> authorities = user.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equalsIgnoreCase(roleName)) {
                    log.info("角色：{}信息发生变动，用户：{}需重新登陆", roleName, user.getUsername());
                    // 如果该用户已经登陆，使其过期
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }

                    break;
                }
            }
        }
    }
}
