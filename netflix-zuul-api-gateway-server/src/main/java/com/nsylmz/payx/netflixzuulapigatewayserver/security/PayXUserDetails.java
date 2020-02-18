package com.nsylmz.payx.netflixzuulapigatewayserver.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.nsylmz.payx.netflixzuulapigatewayserver.proxy.UserBean;

@SuppressWarnings("serial")
public class PayXUserDetails implements UserDetails {

	private String username;
    
	private String password;
    
	private Integer active;
    
	private boolean isLocked;
    
	private boolean isExpired;
    
	private boolean isEnabled;
    
	private List<GrantedAuthority> grantedAuthorities;

    public PayXUserDetails(String username, String password,Integer active, boolean isLocked, boolean isExpired, boolean isEnabled, String [] authorities) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.isLocked = isLocked;
        this.isExpired = isExpired;
        this.isEnabled = isEnabled;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public PayXUserDetails(String username,  String [] authorities) {
        this.username = username;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }
    
    public PayXUserDetails(UserBean userBean) {
        this.username = userBean.getEmail();
        this.password = userBean.getPassword();
        this.active = userBean.getActive();
        this.isLocked = userBean.isLoacked();
        this.isExpired = userBean.isExpired();
        this.isEnabled = userBean.isEnabled();
    }

    public PayXUserDetails() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active==1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
