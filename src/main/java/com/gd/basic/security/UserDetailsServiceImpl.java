package com.gd.basic.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.gd.mapper.RoleMapper;
import com.gd.mapper.UserMapper;
import com.gd.model.po.Role;
import com.gd.model.po.User;

/**
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper UserMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = UserMapper.getUserByName(username);
        if(user==null){
        	log.error("====================user==null======================");
        	throw new DisabledException("用户名或密码错误！");
        }else{
        	log.info("====================user=={}======================",user.getName()+" "+user.getPassword());
        }
//        if (!"0".equals(user.getStatus().getValue())) {
//            throw new DisabledException("当前用户状态不可用！");
//        }
        
        List<Role> roles = roleMapper.getRolesByUserId(user.getId());
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            for (Role role : roles) {
                if (role.isValid()) {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                }
            }
        }

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
