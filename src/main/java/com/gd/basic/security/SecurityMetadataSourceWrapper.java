package com.gd.basic.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.gd.basic.common.BeanFactoryUtil;
import com.gd.basic.crud.CrudService;
import com.gd.mapper.RoleMapper;
import com.gd.model.po.Permission;
import com.gd.model.po.Role;

/**
 * 继承FilterInvocationSecurityMetadataSource，目的是，自定义安全资源元数据的获取方式和提供刷新方法
 *
 */
@Log4j2
@Component
public class SecurityMetadataSourceWrapper implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RoleMapper roleMapper;

    private static DefaultFilterInvocationSecurityMetadataSource defaultFilterInvocationSecurityMetadataSource;

    /**
     * 刷新安全资源元数据
     */
    @PostConstruct
    private void refreshSecurityMetadataSource() {
        log.info("加载安全元数据资源");

        CrudService service = CrudService.of(Permission.class);
        List<Permission> permissions = service.list();
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

        for (Permission permission : permissions) {
            if (StringUtils.isNotBlank(permission.getPath())) {
                List<Role> roles = roleMapper.getRolesByPermissionId(permission.getId());
                if (roles == null) {
                    continue;
                }

                List<String> listAttribute=new ArrayList<String>();
                
                for (int i = 0; i < roles.size(); i++) {
                    Role role = roles.get(i);
                    if (role.isValid()) {
                    	listAttribute.add(role.getName());
                    }
                }
                String[] attributeNames = new String[listAttribute.size()];
                listAttribute.toArray(attributeNames);
                
                if(permission.getPath().indexOf("jsp")==0)
                	permission.setPath("/"+permission.getPath());
                requestMap.put(new RegexRequestMatcher(permission.getPath(), null, true), SecurityConfig.createList(attributeNames));
            }
        }

        log.info("安全配置：{}", requestMap);

        defaultFilterInvocationSecurityMetadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
    }

    public static void refresh() {
        BeanFactoryUtil.getBean(SecurityMetadataSourceWrapper.class).refreshSecurityMetadataSource();
    }

    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return defaultFilterInvocationSecurityMetadataSource.getAttributes(object);
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return defaultFilterInvocationSecurityMetadataSource.getAllConfigAttributes();
    }

    public boolean supports(Class<?> clazz) {
        return defaultFilterInvocationSecurityMetadataSource.supports(clazz);
    }
}
