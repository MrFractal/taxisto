package ru.trendtech.services.security;

import java.util.Collection;

/**
 * User: max
 * Date: 1/23/13
 * Time: 4:14 PM
 */

public interface SecureUser {
    public Collection<String> getRoleNames();

    public String getPassword();

    public Long getId();

    public String getLogin();
}
