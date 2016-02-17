package ru.trendtech.controllers.corporate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.client.corporate.*;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.client.ClientService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.validate.ValidatorService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by petr on 13.03.2015.
 */

@Controller
@RequestMapping("/corporate")
@Transactional
public class CorporateClientController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CorporateClientController.class);
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ValidatorService validatorService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    CorporateLoginResponse corporateLogin(@RequestBody CorporateLoginRequest request) {
         return clientService.corporateLogin(request.getLogin(), request.getPassword());
    }



    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public
    @ResponseBody
    CorporateProfileResponse corporateProfile(@RequestBody CorporateProfileRequest request) {
         return clientService.corporateProfile(request.getClientId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public
    @ResponseBody
    CorporateClientResponse corporateProfile(@RequestBody CorporateClientRequest request) {
        if(!validatorService.validateUser(request.getMainClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equal");
        }
        Client mainClient = clientRepository.findOne(request.getMainClientId());
        if(mainClient.getAccount().getState().equals(Account.State.BLOCKED)){
            throw new CustomException(2, "Ваша учетная запись заблокирована");
        }

        if(mainClient.getMainClient() == null){
            throw new CustomException(4, "You are not corporate client");
        }
        if(!mainClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(5, "Permission denied");
        }
        return clientService.corporateClient(mainClient);
    }



    @RequestMapping(value = "/stat", method = RequestMethod.POST)
    public
    @ResponseBody
    CorporateStatResponse corporateStat(@RequestBody CorporateStatRequest request) {
        return clientService.corporateStat(request.getMainClientId(), request.getSecurity_token(), request.getClientId(), request.getStartTime(), request.getEndTime(), request.getNumberPage(), request.getSizePage());
    }



    @RequestMapping(value = "/blockClient", method = RequestMethod.POST)
    public
    @ResponseBody
    BlockCorporateClientResponse blockClient(HttpServletRequest commonRequest, @RequestBody BlockCorporateClientRequest request) {
        if(!validatorService.validateUser(request.getMainClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equal");
        }
        Client mainClient = clientRepository.findOne(request.getMainClientId());
        if(mainClient.getAccount().getState().equals(Account.State.BLOCKED)){ //   corporateClientIsLock(mainClient)
            throw new CustomException(2, "Ваша учетная запись заблокирована");
        }
        if(mainClient.getMainClient() == null){
            throw new CustomException(4, "You are not corporate client");
        }
        if(!mainClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(5, "Permission denied");
        }

        Client subClient = clientRepository.findOne(request.getClientId());

        if(subClient == null){
            throw new CustomException(6, "Sub client not found");
        }
        if(subClient.getMainClient()==null){
            throw new CustomException(7, "You are not corporate client");
        }
        if(!subClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(8, String.format("Несоответствие mainClient"));
        }
        return clientService.blockClient(subClient, request.getReason());
    }





    @RequestMapping(value = "/updateClientLimit", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateCorporateClientLimitResponse updateCorporateClientLimit(@RequestBody UpdateCorporateClientLimitRequest request) {
        return clientService.updateCorporateClientLimit(request.getSecurity_token(), request.getLimitInfo());
    }




//    @RequestMapping(value = "/updateClientLimit", method = RequestMethod.POST)
//    public
//    @ResponseBody
//    CorporateClientLimitResponse updateCorporateClientLimit(@RequestBody CorporateClientLimitRequest request) {
//        return clientService.updateCorporateClientLimit(request.getMainClientId(), request.getSecurity_token(), request.getCorporateClientLimitInfo());
//    }
}
