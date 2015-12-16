package ru.trendtech.services.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by www.propertyminder.com.
 * User: Maxim S. Ivanov
 * Date: 4/6/11
 * Time: 11:29 AM
 */
class UserInfo extends User {

    private static final long NO_PROFILE = -1;

    private final long userId;

    public UserInfo(long accountId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        this(accountId, NO_PROFILE, username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    private UserInfo(long userId, long profileId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public long getId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userId != userInfo.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }
}
