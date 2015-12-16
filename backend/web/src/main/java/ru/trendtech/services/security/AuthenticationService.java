package ru.trendtech.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trendtech.services.TimeService;

import java.util.HashSet;
import java.util.UUID;

/**
 * File created by max on 10/06/2014 7:08.
 */

@Service
public class AuthenticationService {
    private static final HashSet<SecurityToken> SECURITY_TOKENS = new HashSet<>();

    @Autowired
    private TimeService timeService;

    public SecurityToken register(long userId){
        SecurityToken token = new SecurityToken();
        token.setCreationTime(timeService.nowDateTime());
        token.setUserId(userId);
        token.setToken(UUID.randomUUID().toString());
        SECURITY_TOKENS.add(token);
        return token;
    }

    public boolean isValid(String token){
        boolean result = false;
        for (SecurityToken securityToken : SECURITY_TOKENS) {
            if (securityToken.getToken().equals(token)){
                result = true;
                break;
            }
        }
        return result;
    }

}
