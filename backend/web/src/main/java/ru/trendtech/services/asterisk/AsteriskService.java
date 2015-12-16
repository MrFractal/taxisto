package ru.trendtech.services.asterisk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.common.mobileexchange.model.web.UserIdInfo;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.services.administration.AdministrationService;

/**
 * File created by max on 21/04/2014 1:34.
 */

@Service
@Transactional
public class AsteriskService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AdministrationService administrationService;

    @Transactional(readOnly = true)
    public String getName(String phone) {
        String result = "";
        UserIdInfo userInfo = administrationService.resolveUserInfo(phone);
        if (UserIdInfo.UserType.CLIENT.equals(userInfo.getType())) {
            Client client = clientRepository.findOne(userInfo.getId());
            result = client.getFirstName() + " " + client.getLastName();
        } else if (UserIdInfo.UserType.CLIENT.equals(userInfo.getType())) {
            Driver driver = driverRepository.findOne(userInfo.getId());
            result = driver.getFirstName() + " " + driver.getLastName();
        }
        return result;
    }
}
