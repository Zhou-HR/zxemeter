package com.gd.basic.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * 对Spring Security提供的AccessDecisionManager进行包装
 *
 * @author ZhouHR
 */
@Log4j2
public class AccessDecisionManagerWrapper implements AccessDecisionManager {

    private AccessDecisionManager accessDecisionManager;

    public AccessDecisionManagerWrapper(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        try {
            accessDecisionManager.decide(authentication, object, configAttributes);
        } catch (Exception e) {
            log.error("需要授权：{},当前用户拥有授权：{}", configAttributes, authentication.getAuthorities());
            throw e;
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return accessDecisionManager.supports(attribute);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return accessDecisionManager.supports(clazz);
    }
}
