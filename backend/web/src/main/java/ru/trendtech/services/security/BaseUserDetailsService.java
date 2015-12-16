package ru.trendtech.services.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: max
 * Date: 1/23/13
 * Time: 4:01 PM
 */

public abstract class BaseUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseUserDetailsService.class);

    private boolean loginWithFakeAdmin = false;

    public boolean isLoginWithFakeAdmin() {
        return loginWithFakeAdmin;
    }

    public void setLoginWithFakeAdmin(boolean loginWithFakeAdmin) {
        this.loginWithFakeAdmin = loginWithFakeAdmin;
    }

    protected abstract SecureUser doLoadUserInfo(String username);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.debug("We will try login user: " + username);
        UserInfo details;
        if (!"admin".equals(username) || !loginWithFakeAdmin) {
            final SecureUser user = doLoadUserInfo(username);
            if (user != null) {
                Collection<? extends GrantedAuthority> authorityList = createAuthorityList(user.getRoleNames());
                details = new UserInfo(user.getId(), user.getLogin(), user.getPassword(), true, true, true, true, authorityList);
            } else {
                LOGGER.debug("Authentication FAILED for user: " + username);
                throw new UsernameNotFoundException("Username: " + username + " not found");
            }
        } else {
            details = new UserInfo(1, "admin", "admin", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
            LOGGER.debug("Fake admin account will be used: " + details);
        }
        LOGGER.debug("Authentication SUCCESSFULLY for user: " + username + " " + details);
        return details;
    }

    private Collection<? extends GrantedAuthority> createAuthorityList(Collection<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String roleName : roles) {
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        return authorities;
    }
}
