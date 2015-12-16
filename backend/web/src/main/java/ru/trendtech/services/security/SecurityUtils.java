package ru.trendtech.services.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by www.propertyminder.com.
 * User: Maxim S. Ivanov
 * Date: 4/11/11
 * Time: 5:02 PM
 */
class SecurityUtils {
    public static UserInfo currentUserInfo() {
        UserInfo result = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            if (principal instanceof UserInfo) {
                result = (UserInfo) principal;
            }
        }
        return result;
    }
}
