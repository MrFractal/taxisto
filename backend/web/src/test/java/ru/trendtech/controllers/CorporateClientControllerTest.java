package ru.trendtech.controllers;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.client.CreateMultipleMissionRequest;
import ru.trendtech.common.mobileexchange.model.client.CreateMultipleMissionResponse;
import ru.trendtech.common.mobileexchange.model.client.LoginClientResponse;
import ru.trendtech.common.mobileexchange.model.client.corporate.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.models.ModelsUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.corporateClientUrl;

/**
 * Created by petr on 16.03.2015.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CorporateClientControllerTest {
    private final RestTemplate template = Utils.template();




    @Test
    public void testLogin() throws Exception {
        CorporateLoginRequest request = new CorporateLoginRequest();
        request.setLogin("+79538695889");
        request.setPassword("java");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CorporateLoginResponse response = template.postForObject(corporateClientUrl("/login"), request, CorporateLoginResponse.class);
        response.getSecurity_token();
    }



    @Test
    public void testClients() throws Exception {
        CorporateClientRequest request = new CorporateClientRequest();
        request.setMainClientId(24);
        request.setSecurity_token("zzz");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CorporateClientResponse response = template.postForObject(corporateClientUrl("/clients"), request, CorporateClientResponse.class);
        response.getClientInfoCorporateList();
    }




    @Test
    public void testBlockClient() throws Exception {
        BlockCorporateClientRequest request = new BlockCorporateClientRequest();
        request.setMainClientId(24);
        request.setSecurity_token("9c41b88468947c894a82397ab209ae34");
        request.setClientId(7);
        request.setReason("asdasddadad");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        BlockCorporateClientResponse response = template.postForObject(corporateClientUrl("/blockClient"), request, BlockCorporateClientResponse.class);
        response.getErrorMessage();
    }




    @Test
    public void updateClientLimit() throws Exception {
        UpdateCorporateClientLimitRequest request = new UpdateCorporateClientLimitRequest();
        request.setSecurity_token("9c41b88468947c894a82397ab209ae34");
        LimitInfo limit = new LimitInfo();
        limit.setTypePeriod(2);
        limit.setClientId(24);
        limit.setMainClientId(24);
        limit.setLimitAmount(2000);
        request.setLimitInfo(limit);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        UpdateCorporateClientLimitResponse response = template.postForObject(corporateClientUrl("/updateClientLimit"), request, UpdateCorporateClientLimitResponse.class);
        response.getErrorMessage();
    }




    @Test
    public void testStat() throws Exception {
        CorporateStatRequest request = new CorporateStatRequest();
        request.setMainClientId(302);
        request.setSizePage(8);
        request.setNumberPage(0);
        request.setSecurity_token("dc54556d53bc3746e713a5cd23b33e6e");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CorporateStatResponse response = template.postForObject(corporateClientUrl("/stat"), request, CorporateStatResponse.class);
        response.getTotalItems();
        response.getLastPageNumber();
        response.getMissionInfos();
        response.getSum();
    }

}
