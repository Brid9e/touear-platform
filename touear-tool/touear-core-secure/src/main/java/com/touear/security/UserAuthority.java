package com.touear.security;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * @Title: UserAuthority.java
 * @Description: TODO
 * @author chenl
 * @date 2020-06-09 15:26:01
 * @version 1.0
 */
public class UserAuthority implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<Map> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<OpenAuthority> authorities = Lists.newArrayList();


    public Collection<OpenAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<OpenAuthority> authorities) {
        this.authorities = authorities;
    }

    public Collection<Map> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Map> roles) {
        this.roles = roles;
    }
}
