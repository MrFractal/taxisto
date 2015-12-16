package ru.trendtech.controllers;

import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.*;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderPayment;
import ru.trendtech.domain.courier.TypeWindow;
import ru.trendtech.integration.PayOnlineHelper;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.OrderPaymentRepository;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.billing.CourierBillingService;
import ru.trendtech.services.client.ClientCourierService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

/**
 * File created by petr on 29/05/2014 23:43.
 */

@Controller
@RequestMapping("/payment")
@Transactional
public class BillingController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BillingController.class);
    @Autowired
    private BillingService billingService;
    @Autowired
    private CourierBillingService courierBillingService;
    @Autowired
    private ClientCourierService clientCourierService;
    @Autowired
    private OrderPaymentRepository orderPaymentRepository;

    // token +
    @RequestMapping(value = "/storeCardData", method = RequestMethod.POST)
    public
    @ResponseBody
    StoreCardDataResponse storeCardData(@RequestBody StoreCardDataRequest request) {
        StoreCardDataResponse response = new StoreCardDataResponse();
          response = billingService.storeCardData(request.getCardId(), request.getClientId(), request.getBindingId(), request.getMdOrder(), request.getActive(), request.getCardholderName(), request.getPan(), request.getExpirationYear(), request.getExpirationMonth(), request.getMrchOrder(), request.getSecurity_token());
            return response;
    }



    // убрать!
    @RequestMapping(value = "/getIdCard", method = RequestMethod.POST)
    public
    @ResponseBody
    GetIdCardResponse getIdCard(@RequestBody GetIdCardRequest request) {
        GetIdCardResponse response = billingService.getIdCard(request.getClientId(), request.getMdOrder());
          return response;
    }



    @RequestMapping(value = "/registerOrderDriver/v2", method = RequestMethod.POST)
    public
    @ResponseBody
    RegisterOrderDriver_V2_Response registerOrderDriver_V2(@RequestBody RegisterOrderDriverRequest request) {
           return billingService.sentOrderEventFromDriverToClient_V2(request.getDriverId(), request.getMissionId(), request.getSum());
    }



    @RequestMapping(value = "/registerOrderDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    RegisterOrderDriverResponse registerOrderDriver(@RequestBody RegisterOrderDriverRequest request) {
        RegisterOrderDriverResponse response = new RegisterOrderDriverResponse();
        boolean res = billingService.sentOrderEventFromDriverToClient(request.getDriverId(), request.getMissionId(), request.getSum());
        response.setResult(res);
        return response;
    }




    @RequestMapping(value = "/registerOrderClient", method = RequestMethod.POST)
    public
    @ResponseBody
    RegisterOrderClientResponse registerOrderClient(@RequestBody RegisterOrderClientRequest request) {
        RegisterOrderClientResponse response = new RegisterOrderClientResponse();
        billingService.sentOrderEventFromClientToDriver(request.isAnswer(), request.getClientId(), request.getMissionId(), request.getSecurity_token());
        return response;
    }




    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    PayOrderResponse payOrder(@RequestBody PayOrderRequest request) throws IOException, JSONException {
        PayOrderResponse response = new PayOrderResponse();
         boolean result = billingService.payOrder(request.getMissionId());
            response.setSuccess(result);
              return response;
    }






    @RequestMapping(value = "/payonline/termInit", method = { RequestMethod.GET, RequestMethod.POST }) // { RequestMethod.GET, RequestMethod.POST }  | , headers = "content-type=application/x-www-form-urlencoded"
    public String termInit(HttpServletRequest servletRequest) {
        String PaRes = servletRequest.getParameter("PaRes");
        String MD = servletRequest.getParameter("MD");
        String result = "redirect:fail_payment.jsp";
        if (!StringUtils.isEmpty(MD)) {
            String splitResult[] = MD.split(";");
            String transactionID = splitResult[0];
            String PD = splitResult[1];

            LOGGER.info("PD: " + PD + " transactionID: " + transactionID);

            PayOnlineHelper payOnlineHelper = courierBillingService.completeThreeDS(transactionID, PaRes, PD);

            if(payOnlineHelper.getErrorCode().equals("0")){
                result = "redirect:good_payment.jsp";

                OrderPayment orderPayment = orderPaymentRepository.findByTransactionId(transactionID);
                if(orderPayment != null){
                    ClientCourierService.NotifiedHelper notifiedHelper = clientCourierService.isRequireNotifiedClient(orderPayment.getOrder());
                    if(notifiedHelper.isRequireStartSearch()){
                        /* если заказ без курьера - стартуем поиск */
                        clientCourierService.startSearch(orderPayment.getOrder());
                    }
                }
            }
        }
            LOGGER.info("IN termInit: result =========== " + result);
            return  result;
    }



    /*
    try {
        InputStream inputStream = servletRequest.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader((inputStream)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            LOGGER.info("line = " +line);
            sb.append(line);
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    */





    /*
        5463 card: IUybtUMHH9rap-d7u-YG80FBn-0bRUMhAOEMgyCepBs%3d
    */
    @RequestMapping(value = "/payonline/success", method = RequestMethod.GET)
    public String success(@RequestParam Map<String,String> requestParams, RedirectAttributes redirectAttributes){
        String redirectUrl = "redirect:good_payment.jsp";
        if(CollectionUtils.isEmpty(requestParams)){
            redirectAttributes.addFlashAttribute("result", "Invalid parameter");
            return redirectUrl;
        }
             courierBillingService.parseSuccessfullyPaymentAnswer(requestParams);
             return redirectUrl;
    }





    @RequestMapping(value = "/payonline/fail")
    public String fail(@RequestParam Map<String,String> requestParams, RedirectAttributes redirectAttributes){
        String redirectUrl = "redirect:fail_payment.jsp";
        if(CollectionUtils.isEmpty(requestParams)){
            redirectAttributes.addFlashAttribute("result", "Invalid parameter");
            return redirectUrl;
        }
            courierBillingService.parseFailPaymentAnswer(requestParams);
            return redirectUrl;
    }



    @RequestMapping(value = "bad_payment.jsp", method = RequestMethod.GET)
    public String bad(){
        return "bad_payment";
    }


    @RequestMapping(value = "good_payment.jsp", method = RequestMethod.GET)
    public String good(){
        return "good_payment";
    }


    @RequestMapping(value = "/payonline/onsite")
    public String openSite(){
        String result = "";
        result = buildSiteUrl();
        return result;
    }

    private String buildSiteUrl() {
        String result = null;
        return result;
    }
}
