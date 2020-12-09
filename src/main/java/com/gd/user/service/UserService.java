package com.gd.user.service;

import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gd.basic.crud.CrudService;
import com.gd.mapper.RoleMapper;
import com.gd.mapper.UserMapper;
import com.gd.model.po.Role;
import com.gd.model.po.User;

/**
 */
@Service
@Log4j2
public class UserService {

    private CrudService crudService = CrudService.of(User.class);

    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    @SuppressWarnings("unchecked")
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SaltSource saltSource;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Transactional
    public void save(User user, String roleIds,String wechats) {
    	user.setPassword(getEncodePassword(user.getPassword()));
        int id = crudService.insertAndRtnKey(user);
        saveUserRole(id, roleIds);
        saveUserWechat(id, wechats);
    }

    @Transactional
    public void edit(User user, String roleIds,String roleMenuIds) {
        userMapper.removeUserRoleByUserId(user.getId());
        userMapper.removeMenuUserRoleByUserId(user.getId());
        //userMapper.removeUserWechatByUserId(user.getId());
        int length = user.getPassword().length();
        // 长度等于32，代表不修改密码
        if (length != 32) {
        	user.setPassword(getEncodePassword(user.getPassword()));
        }
        crudService.update(user);
        saveUserRole(user.getId(), roleIds);
        saveMenuUserRole(user.getId(), roleMenuIds);
        //saveUserWechat(user.getId(), wechats);

//        refreshUserSession(user.getName());
    }
    
    public void changePwd(User user){
    	
    	crudService.update(user);
    }

    @Transactional
    public void remove(int id) {
    	User user = findById(id);
        crudService.delete(id);
        userMapper.removeUserRoleByUserId(id);
        userMapper.removeUserWechatByUserId(id);
        refreshUserSession(user.getName());
    }

    @Transactional
    private void saveUserRole(int UserId, String roleIds) {
        String[] strings = StringUtils.split(roleIds, ",");
        if (ArrayUtils.isNotEmpty(strings)) {
            userMapper.saveUserRole(UserId, Arrays.asList(strings));
        }
    }
    
    @Transactional
    private void saveMenuUserRole(int UserId, String roleIds) {
        String[] strings = StringUtils.split(roleIds, ",");
        if (ArrayUtils.isNotEmpty(strings)) {
            userMapper.saveMenuUserRole(UserId, Arrays.asList(strings));
        }
    }
    
    @Transactional
    private void saveUserWechat(int UserId, String wechats) {
        String[] strings = StringUtils.split(wechats, ",");
        if (ArrayUtils.isNotEmpty(strings)) {
            userMapper.saveUserWechat(UserId, Arrays.asList(strings));
        }
    }

    public User findById(int id) {
        return crudService.findById(id);
    }

    public String getRoleIdsByUser(int userId) {
        StringBuilder roleIds = new StringBuilder();
        List<Role> roles = roleMapper.getRolesByUserId(userId);
        for (Role role : roles) {
            roleIds.append(role.getId()).append(",");
        }
        return roleIds.toString();
    }
    
    public String getMenuRoleIdsByUser(int userId) {
        StringBuilder roleIds = new StringBuilder();
        List<Role> roles = roleMapper.getMenuRolesByUserId(userId);
        for (Role role : roles) {
            roleIds.append(role.getId()).append(",");
        }
        return roleIds.toString();
    }
    

    public String getEncodePassword(String password) {
        return passwordEncoder.encodePassword(password, saltSource.getSalt(null));
    }

    /**
     * 刷新某个用户的登陆状态
     *
     * @param name 用户名
     */
    private void refreshUserSession(String name) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
        	org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
            if (!user.getUsername().equals(name)) {
                continue;
            }

            log.info("用户：{}信息发生变动，需重新登陆", name);
            // 如果该用户已经登陆，使其过期
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            for (SessionInformation session : sessions) {
                session.expireNow();
            }
        }
    }
}
