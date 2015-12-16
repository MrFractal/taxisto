package ru.trendtech.services.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.WebUserRepository;

/**
 * Created by petr on 23.10.2015.
 */
@Component
public class ValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorService.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private WebUserRepository webUserRepository;



    public boolean validateUser(long requesterId, String security_token, int whoIs){
        boolean result = false;
        if(whoIs==1){
            // client
            Client client = clientRepository.findOne(requesterId);
            if (client != null && client.getToken() != null && client.getToken().equals(security_token)) {
                result = true;
            }
        }else if(whoIs==2){
            // driver
            Driver driver = driverRepository.findOne(requesterId);
            if (driver != null && driver.getToken() != null && driver.getToken().equals(security_token)) {
                result = true;
            }
        }else if(whoIs==3){
            // operator
            WebUser webUser = webUserRepository.findOne(requesterId);
            if (webUser != null && webUser.getToken() != null && webUser.getToken().equals(security_token)) {
                result = true;
            }
        }
        return result;
    }



    public boolean isOkSecurityToken_old(Object obj, String security_token, int whoIs){
        boolean result = false;
        if(whoIs==1){
            // client
            Client client = (Client)obj;
            if (client != null && client.getToken() != null && client.getToken().equals(security_token)) {
                result = true;
            }
        }else if(whoIs==2){
            // driver
            Driver driver = (Driver)obj;
            if (driver != null && driver.getToken() != null && driver.getToken().equals(security_token)) {
                result = true;
            }
        }else if(whoIs==3){
            // operator
            WebUser webUser = (WebUser)obj;
            if (webUser != null && webUser.getToken() != null && webUser.getToken().equals(security_token)) {
                result = true;
            }
        }
        return result;
    }
}
