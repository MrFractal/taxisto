package ru.trendtech.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.support.ClientAdministrationStatusResponse;
import ru.trendtech.common.mobileexchange.model.driver.*;
import ru.trendtech.common.mobileexchange.model.node.FantomDriverRequest;
import ru.trendtech.common.mobileexchange.model.node.FantomDriverResponse;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.common.mobileexchange.model.web.MissionCompleteRequest;
import ru.trendtech.common.mobileexchange.model.web.MissionCompleteResponse;
import ru.trendtech.common.mobileexchange.model.web.PropertyRequest;
import ru.trendtech.common.mobileexchange.model.web.PropertyResponse;
import ru.trendtech.common.mobileexchange.model.web.corporate.*;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.DriverSetting;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.FileUtil;
import ru.trendtech.utils.StrUtils;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static ru.trendtech.controllers.ProfilesUtils.adminUrl;

/**
 * File created by petr on 20/04/2014 23:38.
 */

public class AdministrativeControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrativeControllerTest.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final RestTemplate TEMPLATE = Utils.template();



    @BeforeMethod
    public void forseSetProfile() throws Exception {
        ProfilesUtils.setProfileForce("max");
    }




    @Test
    public void testResolveUserIdInfoClient() throws Exception {
        UserIdInfo info = sendRequest("+79137867136");//79833158962
        String asString = OBJECT_MAPPER.writeValueAsString(info);
    }




    private UserIdInfo sendRequest(String phone) throws JsonProcessingException {
        UserIdInfoRequest request = new UserIdInfoRequest();
        request.setPhone(phone);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UserIdInfoResponse response = TEMPLATE.postForObject(adminUrl("/userinfo"), request, UserIdInfoResponse.class);
        return response.getInfo();
    }


    @Test
    public void tablets() throws Exception {
        TabletRequest request = new TabletRequest();
        request.setSizePage(10);
        request.setNumberPage(0);
        //request.setTabletId(1);
        request.setSecurity_token("845b16592f8e4d6d38ab8b16a5bbce86");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TabletResponse response = Utils.template().postForObject(adminUrl("/tablet"), request, TabletResponse.class);
        response.getTabletInfoList();
        response.getLastPageNumber();
        response.getTotalItems();
    }








    @Test
    public void routerUpdate() throws Exception {
        RouterUpdateRequest request = new RouterUpdateRequest();
        request.setSecurity_token("zzz");
        RouterInfo info = new RouterInfo();
        info.setId(1L);
        info.setIp("77777777ll7");
        info.setTimeOfPurchase(1435832640000L);

        DriverInfoARM driver = new DriverInfoARM();
        driver.setId(8);


        info.setDriverInfoARM(driver);

        List<RouterInfo> infoList = new ArrayList<>();
        infoList.add(info);

        request.setRouterInfos(infoList);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RouterUpdateResponse response = Utils.template().postForObject(adminUrl("/routerUpdate"), request, RouterUpdateResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }




    @Test
    public void tabletUpdate() throws Exception {
        TabletUpdateRequest request = new TabletUpdateRequest();
        request.setSecurity_token("52fc0174605e0fb9bf47838fa2fa1d97");
        TabletInfo info = new TabletInfo();
        //info.setId(1);
        info.setOwn(false);


        List<TabletInfo> infoList = new ArrayList<>();
        infoList.add(info);

        request.setTabletInfos(infoList);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TabletUpdateResponse response = Utils.template().postForObject(adminUrl("/tabletUpdate"), request, TabletUpdateResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void banPeriodRestDriver() throws Exception {
        BanPeriodRestDriverRequest request = new BanPeriodRestDriverRequest();
        request.setSecurity_token("44871a0d0ff54625c2e7a5f059327e2e");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        BanPeriodRestDriverResponse response = TEMPLATE.postForObject(adminUrl("/banPeriodRestDriver"), request, BanPeriodRestDriverResponse.class);
        response.getBanPeriodRestDriverInfoList();
    }





        @Test
        public void activityDriver() throws Exception {
            ActivityDriverRequest request = new ActivityDriverRequest();
            request.setSecurity_token("e735c770988498e5a61f4e0884af0e9a");
            //request.setDriverId(8);
            request.setStartTime(1427976000);
            request.setEndTime(1427979600);
            String asString = OBJECT_MAPPER.writeValueAsString(request);
            ActivityDriverResponse response = TEMPLATE.postForObject(adminUrl("/activityDriver"), request, ActivityDriverResponse.class);
            response.getDriverActivityInfos();
        }




    @Test
    public void testListRegion() throws Exception {
        RegionRequest request = new RegionRequest();
        request.setSecurity_token("zzz");

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        RegionResponse response = TEMPLATE.postForObject(adminUrl("/region"), request, RegionResponse.class);
        response.getErrorMessage();
        response.getRegionInfos();
    }



    @Test
    public void missionCanceledByClient() throws Exception {
        MissionCanceledByClientRequest request = new MissionCanceledByClientRequest();
        request.setSecurity_token("zzz"); // 7f035a73303ecf35a2d42bcf72f59254
        request.setStartTime(1436137200);
        request.setEndTime(1436914799);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MissionCanceledByClientResponse response = Utils.template().postForObject(adminUrl("/missionCanceledByClient"), request, MissionCanceledByClientResponse.class);
        response.getMissionCanceledByClientInfos();
    }



    @Test
    public void missionByRegion() throws Exception {
        MissionByRegionRequest request = new MissionByRegionRequest();
        request.setSecurity_token("zzz");
        request.setGroupBy("region");
        request.setStartTime(1436137200);
        request.setEndTime(1436914799);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        MissionByRegionResponse response = TEMPLATE.postForObject(adminUrl("/missionByRegion"), request, MissionByRegionResponse.class);
        response.getErrorMessage();
        response.getMissionStatisticByRegionInfos();
    }



    @Test
    public void updateRegion() throws Exception {
        UpdateRegionRequest request = new UpdateRegionRequest();

        RegionInfo regionInfo = new RegionInfo();
        //regionInfo.setId(2);
        regionInfo.setNameRegion("TEST ZONE");
        regionInfo.setBase64Coord("ODIuOTI1OSw1NS4wNTMxDQo4Mi45MjU4LDU1LjA0MjgNCjgyLjk1NjgsNTUuMDQzMg0KODIuOTU4NSw1NS4wNDk5DQo4Mi45NTgxLDU1LjA1NTMNCjgyLjk1MTMsNTUuMDU2MA0KODIuOTQxMSw1NS4wNTYwDQo4Mi45MzE0LDU1LjA1NjANCjgyLjkyNTksNTUuMDUzMQ==");
        regionInfo.setActive(true);
        regionInfo.setTypeRegion(1);
        regionInfo.setCoast("right");

        List<RegionInfo> list = new ArrayList<>();
        list.add(regionInfo);

        request.setSecurity_token("6db824ebe779c9edcafff2b22818a590");
        request.setRegionInfos(list);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateRegionResponse response = TEMPLATE.postForObject(adminUrl("/updateRegion"), request, UpdateRegionResponse.class);
        response.getErrorMessage();
    }


    @Test
    public void checkInsidePolygon() throws Exception {
        CheckPointInsidePolygonRequest request = new CheckPointInsidePolygonRequest();
        request.setSecurity_token("2bbbbbf6cd03ba256b3b042c1ea60a99");
//        request.setLatitude(55.045603);
//        request.setLongitude(82.960436);
//
        request.setLatitude(55.00441833333333);
        request.setLongitude(82.669505);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        CheckPointInsidePolygonResponse response = TEMPLATE.postForObject(adminUrl("/checkInsidePolygon"), request, CheckPointInsidePolygonResponse.class);
        response.getRegionInfo();
    }



    @Test
    public void getOwnDriverStats() throws Exception {
        OwnDriverStatsRequest request = new OwnDriverStatsRequest();
        request.setSecurity_token("zzz");
        //request.setDriverId(8);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        OwnDriverStatsResponse response = TEMPLATE.postForObject(adminUrl("/own/driver/stats"), request, OwnDriverStatsResponse.class);
        response.getOwnDriverStatsInfos();
        response.getGeneralStatsInfo();
    }






    @Test
    public void corporateBalanceReport() throws Exception {
        CorporateBalanceReportRequest request = new CorporateBalanceReportRequest();
        request.setSecurity_token("6db824ebe779c9edcafff2b22818a590");
        //request.setMainClientId(24);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        CorporateBalanceReportResponse response = TEMPLATE.postForObject(adminUrl("/corporateBalanceReport"), request, CorporateBalanceReportResponse.class);
        response.getBalanceReportInfos();
    }



    @Test
    public void updateDriverPeriodWork() throws Exception {
        UpdateDriverPeriodWorkRequest request = new UpdateDriverPeriodWorkRequest();
        request.setSecurity_token("41620dc062752d7c517971a7e1c6a207");
        DriverPeriodWorkInfo info = new DriverPeriodWorkInfo();
        info.setActive(false);
        info.setStartWork(1425539340);
        info.setEndWork(1425625740);
        info.setDriverId(35);
        info.setRequisiteId(10);
        info.setId(28);
        request.getDriverPeriodWorkInfos().add(info);
        //request.setDriverId(23);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateDriverPeriodWorkResponse response = TEMPLATE.postForObject(adminUrl("/updateDriverPeriodWork"), request, UpdateDriverPeriodWorkResponse.class);
        response.getErrorCode();
        response.getErrorMessage();
    }



    @Test
    public void getMissionStateStatistic() throws Exception {
        MissionStateStatisticRequest request = new MissionStateStatisticRequest();
        request.setSecurity_token("36b3bc1e0d4e3eb69b31bd2756084556");
        request.setNumberPage(0);
        request.setSizePage(2);
        request.setState("AUTO_SEARCH");

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        MissionStateStatisticResponse response = TEMPLATE.postForObject(adminUrl("/missionStateStatistic"), request, MissionStateStatisticResponse.class);
        response.getMissionStateStatisticInfos();
        response.getTotalItems();
        response.getLastPageNumber();
    }





    @Test
    public void driverCashFlow() throws Exception {
        DriverCashFlowRequest request = new DriverCashFlowRequest();
        request.setSecurity_token("zzz");
        request.setNumberPage(0);
        request.setSizePage(100);
        request.setTaxoparkId(1);
        //request.setClientId(24);
        //request.setDriverId(8);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverCashFlowResponse response = TEMPLATE.postForObject(adminUrl("/driverCashFlow"), request, DriverCashFlowResponse.class);
        response.getDriverCashFlowInfos();
        response.getTotalItems();
        response.getLastPageNumber();
    }







    @Test
    public void updateBanPeriodRestDriver() throws Exception {
        UpdateBanPeriodRestDriverRequest request = new UpdateBanPeriodRestDriverRequest();
        request.setSecurity_token("44871a0d0ff54625c2e7a5f059327e2e");

        List<BanPeriodRestDriverInfo> list = new ArrayList<>();
        BanPeriodRestDriverInfo info1 = new BanPeriodRestDriverInfo();
        info1.setId(1);
        info1.setActive(false);


        BanPeriodRestDriverInfo info2 = new BanPeriodRestDriverInfo();
        info2.setId(0);
        info2.setActive(true);
        info2.setTimeOfEnding(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6().plusDays(1)));
        info2.setTimeOfStarting(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6()));

        list.add(info1);
        list.add(info2);

        request.setBanPeriodRestDriverInfoList(list);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateBanPeriodRestDriverResponse response = TEMPLATE.postForObject(adminUrl("/update/banPeriodRestDriver"), request, UpdateBanPeriodRestDriverResponse.class);
        response.getErrorMessage();
    }




    @Test
    public void testResolveUserIdInfoDriver() throws Exception {
        UserIdInfo info = sendRequest("222222");//219385
        String asString = OBJECT_MAPPER.writeValueAsString(info);
    }

    @Test
    public void testCreateDriver() throws Exception {
        DriverUpdateRequest request = new DriverUpdateRequest();
        DriverInfo driverInfo = buildDriverInfo();
        request.setDriverInfo(driverInfo);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverUpdateResponse response = TEMPLATE.postForObject(adminUrl("/driver/create"), request, DriverUpdateResponse.class);
    }





    @Test
    public void corporateMissionStat() throws Exception {
        CorporateMissionStatRequest request = new CorporateMissionStatRequest();
        request.setSecurity_token("85b66e6560fbe5cb8c5c53fff5fc48be");
        request.setPageSize(20);
        request.setNumPage(1);
        //request.setClientId(7);
        //request.setMainClientId(24);
        request.setStartTime(1440698400);
        request.setEndTime(1440784800);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        CorporateMissionStatResponse response = TEMPLATE.postForObject(adminUrl("/corporateMissionStat"), request, CorporateMissionStatResponse.class);
        response.getMissionInfos();
        response.getAllSum();
        response.getErrorMessage();
    }





    @Test
    public void testOnlyHistorymissionsDriver() throws Exception {
        TripsHistoryRequest request = new TripsHistoryRequest();
        request.setRequesterId(6);
        TripsHistoryResponse response = Utils.template().postForObject(adminUrl("/driver/lastmissions"), request, TripsHistoryResponse.class);
        List<MissionInfo> missionInfos = response.getHistory();
        missionInfos.size();
    }




    @Test
    public void bukanovReport() throws Exception {
        BukanovReportRequest request = new BukanovReportRequest();
        request.setSecurity_token("zzz");
        request.setStartTime(1439337600);
        request.setEndTime(1439424000);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        BukanovReportResponse response = Utils.template().postForObject(adminUrl("/bukanovReport"), request, BukanovReportResponse.class);
        response.getErrorMessage();
    }





    @Test
    public void sendEmailForClient() throws Exception {
        SendEmailRequest request = new SendEmailRequest();
        request.setSubject("Бонусы от Таксисто");
        List<Long> clientIds = FileUtil.read("c:\\6.txt"); // often.txt
        request.setClientIds(clientIds);
        request.setTypeEmail(9); // 4 - often, 5 - sometimes
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        
        SendEmailResponse response = Utils.template().postForObject(adminUrl("/sendEmailForClient"), request, SendEmailResponse.class);

        response.getErrorCodeHelper().getErrorCode();
        response.getErrorCodeHelper().getErrorMessage();
    }



    @Test
    public void sendEmailAllClient() throws Exception {
        SendEmailRequest request = new SendEmailRequest();
        request.setHtmlText("\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <title>Taxisto</title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0\" />\n" +
                "    <style type=\"text/css\">\n" +
                "        html {width:100%}\n" +
                "        body {background: #fff;padding:0;margin:0;}\n" +
                "        .warning {\n" +
                "            width: 700px;\n" +
                "            margin-left: -45px;\n" +
                "            margin-top: 10px;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"font: 12px/18px Arial, sans-serif; width: 100%;\">\n" +
                "\n" +
                "<div class=\"wrapper\" style=\"width:701px; margin: 0 auto;\">\n" +
                "\n" +
                "    <div class=\"header\">\n" +
                "        <img src=\"http://taxisto.ru/mail/sorry-about-canceled-order/images/top.png\" width=\"701\" height=\"264\" alt=\"Таксисто\" />\n" +
                "    </div><!-- .header-->\n" +
                "\n" +
                "    <div class=\"content\" style=\" padding-top: 50px; padding-left: 45px; padding-right: 45px;\">\n" +
                "\n" +
                "        <div class=\"content_header\" style=\"font-size: 30px; font-weight: bold; line-height: 33px;\">\n" +
                "            Дорогие друзья!\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"content_text2\" style=\"font-size: 16px;\">\n" +
                "\t\t\t<br/>\n" +
                "            <p>Последние несколько недель, мы очень много трудились чтобы перевести Таксисто на точные карты от 2Гис.</p>\n" +
                "            <p>Но при переходе на новые карты, к сожалению, в приложении произошел технический сбой, из-за которого не работала функция заказа на всех смартфонах Android и частично на iPhone .<br>\n" +
                "            При этом заказ как бы делался, но приложение выдавало сообщение, что машина не найдена. В ближайшее время все неполадки будут устранены, мы все для этого делаем!</p>\n" +
                "\n" +
                "            <p>Приносим свои глубочайшие извинения тем, кто по этой причине не смог  заказать такси через наше приложение.</p>\n" +
                "\n" +
                "\n" +
                "            <p>\n" +
                "                С уважением,<br/>\n" +
                "                Таксисто\n" +
                "            </p>\n" +
                "        </div>\n" +
                "\t</div><!-- .content-->\n" +
                "\n" +
                "    <div class=\"footer\" style=\"margin-top: 70px; margin-bottom: 30px;\">\n" +
                "        <table class=\"footer-table\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n" +
                "            <tr>\n" +
                "                <td colspan=\"3\" style=\"height: 2px; background: #fccb0b;\"></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td style=\"text-align: center\">\n" +
                "\t\t\t\t<br/>\n" +
                "                    <a id=\"ref1\" href=\"http://taxisto.ru\"><img class=\"footer_taxistoru\" src=\"http://taxisto.ru/mail/sorry-about-canceled-order/images/taxistoru_text.png\" style=\"display: inline-block; padding-bottom: 5px;\"></a>\n" +
                "                </td>\n" +
                "                <td style=\"text-align: center\">\n" +
                "                    <a id=\"ref2\" href=\"https://play.google.com/store/apps/details?id=ru.trendtech.client\" target=\"_blank\" style=\"text-decoration: none;\">\n" +
                "                        <img class=\"footer_google_play\" src=\"http://taxisto.ru/mail/sorry-about-canceled-order/images/bottom_google_play.png\" style=\"display: inline-block; margin-top: 20px;\">\n" +
                "                    </a>\n" +
                "\n" +
                "                    <a id=\"ref3\" href=\"https://itunes.apple.com/ru/app/taxisto/id892232921?mt=8\" target=\"_blank\" style=\"text-decoration: none;\">\n" +
                "                        <img class=\"footer_app_store\" src=\"http://taxisto.ru/mail/sorry-about-canceled-order/images/bottom_app_store.png\" style=\"margin-left: 15px; display: inline-block;\">\n" +
                "                    </a>\n" +
                "                </td>\n" +
                "                <td style=\"text-align: center\">\n" +
                "\t\t\t\t<br/>\n" +
                "                    <a id=\"ref4\" href=\"mailto:info@taxisto.ru\" style=\"text-decoration: none;\">\n" +
                "                        <img class=\"footer_info_text\" src=\"http://taxisto.ru/mail/sorry-about-canceled-order/images/infotaxisto_text.png\" style=\"display: inline-block; padding-bottom: 5px;\">\n" +
                "                    </a>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "    </div><!-- .footer -->\n" +
                "\n" +
                "</div><!-- .wrapper -->\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        request.setSubject("Таксисто приносит извинения!");
        SendEmailResponse response = Utils.template().postForObject(adminUrl("/sendEmailAllClient"), request, SendEmailResponse.class);

    }



    @Test
    public void sendEmail() throws Exception {
            SendEmailRequest request = new SendEmailRequest();
            request.setEmail("dsmirnov8080@gmail.com");
            request.setHtmlText("\n" +
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "    <title>Taxisto</title>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0\" />\n" +
                    "    <style type=\"text/css\">\n" +
                    "        html {width:100%}\n" +
                    "        body {background: #fff;padding:0;margin:0;}\n" +
                    "        .warning {\n" +
                    "            width: 700px;\n" +
                    "            margin-left: -45px;\n" +
                    "            margin-top: 10px;\n" +
                    "            margin-bottom: 10px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"font: 12px/18px Arial, sans-serif; width: 100%;\">\n" +
                    "Test mess</body>\n" +
                    "</html>");
            request.setSubject("Test mess");
            String asString = OBJECT_MAPPER.writeValueAsString(request);
            SendEmailResponse response = Utils.template().postForObject(adminUrl("/sendEmail"), request, SendEmailResponse.class);
            response.getErrorCodeHelper();
    }





    @Test
    public void testOnlyHistorymissionsClient() throws Exception {
        TripsHistoryRequest request = new TripsHistoryRequest();
        request.setRequesterId(14);
        TripsHistoryResponse response = Utils.template().postForObject(adminUrl("/client/lastmissions"), request, TripsHistoryResponse.class);
        List<MissionInfo> missionInfos = response.getHistory();
        missionInfos.size();
    }



    @Test
    public void setPropertyValueARM() throws  Exception{
        PropertyUpdateARMRequest request = new PropertyUpdateARMRequest();
        request.setPropName("booked_active");
        request.setPropValue("0");
        request.setSecurity_token("c26ac040c0d7284a4f8472b309dee94d");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        PropertyUpdateResponse response = TEMPLATE.postForObject(adminUrl("/setPropertyValue/arm"), request, PropertyUpdateResponse.class);
        response.getErrorCodeHelper();
    }



    @Test
    public void updateCorporateClientBalance() throws  Exception{
        UpdateCorporateClientBalanceRequest request = new UpdateCorporateClientBalanceRequest();
        request.setAmountOfMoney(100);
        request.setOperation(6);
        request.setArticleId(1L);
        request.setMainClientId(24L);
        request.setSecurity_token("f04e21d6b1b9a32aede975797724d15e");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateCorporateClientBalanceResponse response = TEMPLATE.postForObject(adminUrl("/corporateClient/updateBalance"), request, UpdateCorporateClientBalanceResponse.class);
        response.getErrorMessage();
    }



    @Test
    public void promoExclusive() throws  Exception{
        PromoCodeExclusiveRequest request = new PromoCodeExclusiveRequest();
        request.setClientId(24);
        request.setSecurity_token("1a55577c8908e58e17449470ce3bc3d7");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        PromoCodeExclusiveResponse response = TEMPLATE.postForObject(adminUrl("/promoExclusive"), request, PromoCodeExclusiveResponse.class);
        response.getPromo();
        response.getErrorMessage();
        response.getErrorCode();
    }




    @Test
    public void updatePartnersGroup() throws  Exception{
        UpdatePartnersGroupRequest request = new UpdatePartnersGroupRequest();

        PartnersGroupInfo pg = new PartnersGroupInfo();
        pg.setSection("other");
        pg.setGroupName("wwwww");
        pg.setIdGroup(5L);

        request.setPartnersGroupInfo(pg);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdatePartnersGroupResponse response = TEMPLATE.postForObject(adminUrl("/updatePartnersGroup"), request, UpdatePartnersGroupResponse.class);
        response.getPartnersGroupInfo();
    }



    @Test
    public void insertPartnersGroup() throws  Exception{
        InsertPartnersGroupRequest request = new InsertPartnersGroupRequest();

        PartnersGroupInfo pg = new PartnersGroupInfo();
        pg.setSection("24hour");
        pg.setGroupName("asasasasas");
        //pg.setIdGroup(2L);

        request.setPartnersGroupInfo(pg);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        InsertPartnersGroupResponse response = TEMPLATE.postForObject(adminUrl("/insertPartnersGroup"), request, InsertPartnersGroupResponse.class);
    }




    @Test
    public void insertItemPartnersGroup() throws  Exception{
        InsertItemPartnersGroupRequest request = new InsertItemPartnersGroupRequest();

        ItemPartnersGroupInfo ig = new ItemPartnersGroupInfo();
        ig.setPhone("1");
        ig.setName("1");
        ig.setCity("1");
        ig.setHouse("3");
        ig.setKorpus("5");
        ig.setRegion("Новосибирская область");
        ig.setStreet("Саввы Кожевникова");
        ig.setGroupId(2L);

        request.setItemPartnersGroupInfo(ig);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        InsertItemPartnersGroupResponse response = TEMPLATE.postForObject(adminUrl("/insertItemPartnersGroup"), request, InsertItemPartnersGroupResponse.class);
        response.isSuccess();
    }



    @Test
    public void updateItemPartnersGroup() throws  Exception{
        UpdateItemPartnersGroupRequest request = new UpdateItemPartnersGroupRequest();

        ItemPartnersGroupInfo ig = new ItemPartnersGroupInfo();
        ig.setPhone("209-25-90");
        ig.setName("Харакири");
        ig.setCity("Новосибирск");
        ig.setHouse("3");
        ig.setKorpus("5");
        ig.setRegion("Новосибирская область");
        ig.setStreet("Саввы Кожевникова");
        ig.setGroupId(2L);
        ig.setItemId(6L);

        request.setItemPartnersGroupInfo(ig);
        UpdateItemPartnersGroupResponse response = TEMPLATE.postForObject(adminUrl("/updateItemPartnersGroup"), request, UpdateItemPartnersGroupResponse.class);
    }






    @Test
    public void findGroupBySection() throws  Exception{
        FindGroupBySectionRequest request = new FindGroupBySectionRequest();
        request.setSection("24hour");
          FindGroupBySectionResponse response = TEMPLATE.postForObject(adminUrl("/findGroupBySection"), request, FindGroupBySectionResponse.class);
          response.getPartnersGroupInfoList();
    }





    @Test
    public void missionCostIncrease() throws  Exception{
        MissionCostIncreaseRequest request = new MissionCostIncreaseRequest();

        //request.setComment("поднять ставки!");
        request.setSumIncrease(200.0);
        request.setMissionId(1);

        MissionCostIncreaseResponse response = TEMPLATE.postForObject(adminUrl("/missionCostIncrease"), request, MissionCostIncreaseResponse.class);
    }



    @Test
    public void rebornMission() throws  Exception{
        RebornMissionRequest request = new RebornMissionRequest();

        request.setMissionId(33);
        request.setSumIncrease(150);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        RebornMissionResponse response = TEMPLATE.postForObject(adminUrl("/rebornMission"), request, RebornMissionResponse.class);

        response.getErrorCodeHelper().getErrorMessage();
    }




    //f:add
    @Test
    public void testGetDriverStats() throws Exception {
        DriverStatsRequest request = new DriverStatsRequest();
        request.setDriverId(8);
        request.setSecurity_token("zzz");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverStatsResponse response = TEMPLATE.postForObject(adminUrl("/driver/stats"), request,DriverStatsResponse.class);
        DriverStatsInfo driverStatsInfo = response.getDriverStatsInfo();
    }



    //f:add
    @Test
    public void testGetClientStats() throws Exception {
        ClientStatsRequest request = new ClientStatsRequest();
        request.setClientId(1);
        //String asString = OBJECT_MAPPER.writeValueAsString(request);
        ClientStatsResponse response = TEMPLATE.postForObject(adminUrl("/client/stats"), request, ClientStatsResponse.class);
        ClientStatsInfo clientStatsInfo = response.getClientStatsInfo();
    }




    //f:add
    @Test
    public void testDriversScores() throws Exception {
        RatesDriverRequest request = new RatesDriverRequest();
        request.setDriverId(8);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        RatesDriverResponse response = TEMPLATE.postForObject(adminUrl("/scores"), request, RatesDriverResponse.class);
        response.getDriverRates();
    }



    /*
    @RequestMapping(value = "/driver/adminstatus", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverAdministrationStatusResponse adminStatusDriver(@RequestBody DriverAdministrationStatusRequest request) {
        DriverAdministrationStatusResponse response = new DriverAdministrationStatusResponse();
        DriverLocksInfo driverLocksInfo = administrationService.getDriverAdministrativeStatus(request.getDriverId());
        response.setDriverLocksInfo(driverLocksInfo);

        LOGGER.debug("driverLocksInfo: adminStatus = "+driverLocksInfo.getAdministrativeStatus()+" timeOfLock ="+driverLocksInfo.getTimeOfLock());

        return response;
    }
    */



    //f:add
    @Test
    public void testGetStatusDriver() throws Exception {
        DriverAdministrationStatusRequest request = new DriverAdministrationStatusRequest();
        request.setDriverId(1);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverAdministrationStatusResponse response = TEMPLATE.postForObject(adminUrl("/driver/adminstatus"), request, DriverAdministrationStatusResponse.class);
        DriverLocksInfo driverLocksInfo = response.getDriverLocksInfo();
    }







    //f:add
    @Test
    public void adminStatusClient() throws Exception {
        ClientAdministrationStatusRequest request = new ClientAdministrationStatusRequest();
        request.setClientId(17);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ClientAdministrationStatusResponse response = TEMPLATE.postForObject(adminUrl("/client/adminstatus"), request, ClientAdministrationStatusResponse.class);
        response.getClientLocksInfo();
    }






    //f:add
    @Test
    public void blockClient() throws Exception {
        BlockClientRequest request = new BlockClientRequest();
        request.setComment("wwererer");
        request.setBlock(true);
        request.setClientId(7);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        BlockClientResponse response = TEMPLATE.postForObject(adminUrl("/client/block"), request, BlockClientResponse.class);
        response.getErrorCodeHelper().getErrorMessage();
    }



    //f:add
    @Test
    public void testUpdateStatusDriver() throws Exception {
        DriverAdministrationStatusRequest request = new DriverAdministrationStatusRequest();
        request.setDriverId(3);
        request.setAdminStatus("ACTIVE"); //BLOCKED

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverAdministrationStatusResponse response = TEMPLATE.postForObject(adminUrl("/driver/updadminstatus"), request, DriverAdministrationStatusResponse.class);
        DriverLocksInfo driverLocksInfo = response.getDriverLocksInfo();
    }




    @Test
    public void driverUpdateBalance() throws Exception {
        DriverUpdateBalanceRequest request = new DriverUpdateBalanceRequest();
        //clientAccount.setBonuses(Money.total(clientAccount.getBonuses(), money));
        //MoneyUtils.getRubles(45.67).sets;
        //Money.of(CurrencyUnit.of("RUB"),45.67);
        //LOGGER.info("MoneyUtils.getRubles(45.67) = "+Money.of(CurrencyUnit.of("RUB"),45.67));

        // 1- начисление штрафа
        request.setDriverId(new Long(8));
        request.setAmountOfMoney(-20); // минус, значит он нам должен
        request.setSecurity_token("qwerty");
        request.setOperation(7); // 1-штраф;
        request.setComment("asdasdasd");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverUpdateBalanceResponse response = Utils.template().postForObject(adminUrl("/driver/updateBalance"), request, DriverUpdateBalanceResponse.class);
    }





    @Test
    public void findLoggingEventMongo() throws Exception {
        LoggingEventMongoRequest request = new LoggingEventMongoRequest();
        request.setWebUserId(1L);
        request.setSecurity_token("zzz");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoggingEventMongoResponse response = Utils.template().postForObject(adminUrl("/loggingEventMongoList"), request, LoggingEventMongoResponse.class);
        response.getErrorCodeHelper();
        response.getLoggingEventInfoMongoList();
    }




    @Test
    public void updateTaxoparkPartner() throws Exception {
        TaxoparkPartnersUpdateRequest request = new TaxoparkPartnersUpdateRequest();
        TaxoparkPartnersInfo taxoparkPartnersInfo = new TaxoparkPartnersInfo();
        taxoparkPartnersInfo.setResponsibilityPhone("dsfdsf");
        taxoparkPartnersInfo.setResponsibilityFio("fsfsf");
        taxoparkPartnersInfo.setPriority(1);
        taxoparkPartnersInfo.setOfficePhone("22342342");
        taxoparkPartnersInfo.setNameTaxopark("xxxx");
        taxoparkPartnersInfo.setOfficeAddress("dfsfdsfdsf");
        taxoparkPartnersInfo.setId(1L);

        request.setTaxoparkPartnersInfo(taxoparkPartnersInfo);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);

        TaxoparkPartnersUpdateResponse response = Utils.template().postForObject(adminUrl("/taxopark/update"), request, TaxoparkPartnersUpdateResponse.class);
        response.getErrorCodeHelper();
    }




    @Test
    public void missionCancelByOperator() throws Exception {
            CancelMissionRequest request = new CancelMissionRequest();
            request.setMissionId(2);
            request.setComment("cancel by operator");
            request.setReason(3);
            //long missionId, String comment, int reason
            String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            CancelMissionResponse response = Utils.template().postForObject(adminUrl("/mission/cancel"), request, CancelMissionResponse.class);
    }






    @Test
    public void clientUpdateBonuses() throws Exception {
        ClientUpdateBonusesRequest request = new ClientUpdateBonusesRequest();
        request.setClientId(new Long(24));
        request.setAmountOfMoney(-45);
        request.setSecurity_token("qwerty");
        request.setComment("bla bla");
        request.setOperation(4);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientUpdateBonusesResponse response = Utils.template().postForObject(adminUrl("/client/updateBonuses"), request, ClientUpdateBonusesResponse.class);
        response.getErrorCodeHelper();
    }




    @Test
    public void findClientStatsListByMask() throws Exception {
        ClientStatListRequest request = new ClientStatListRequest();
        //request.setCountCanceledStart(0);
        //request.setCountCanceledEnd(5);
        //request.setAdminStatus("INACTIVE");

        //request.setCountMissionStart(1);
        //request.setCountMissionEnd(5);

        request.setNumberPage(1);
        request.setSizePage(10);

        //request.setCountMissionStart(0);
        //request.setCountMissionEnd(3);

        //request.setCountCanceledStart(0);
        //request.setCountCanceledEnd(2);

        //request.setNameMask("Халб");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientStatListResponse response = Utils.template().postForObject(adminUrl("/client/stat/list/"), request, ClientStatListResponse.class);
        response.getClientInfoStat();
        }




        @Test
        public void canceledMissionList() throws Exception {
            CanceledMissionListRequest request = new CanceledMissionListRequest();
            //request.setCancelBy("driver");
            //request.setCancelById(2);
            //request.setTaxoparkId(2);
            //request.setTimeOfCanceledStart(1412930520);
            //request.setTimeOfCanceledEnd(1417809540);
            request.setNumberPage(0);
            request.setSizePage(10);
            String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            CanceledMissionListResponse response = Utils.template().postForObject(adminUrl("/mission/canceled/list"), request,CanceledMissionListResponse.class);
            response.getCancelMissionInfoList();
        }





        @Test
        public void assistantList() throws Exception {
            AssistantRequest request = new AssistantRequest();
            request.setSecurity_token("fa0a2d6d6506b634f3e85f1a9a7c49fd");
            String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            AssistantResponse response = Utils.template().postForObject(adminUrl("/assistant/list"), request,AssistantResponse.class);
            response.getAssistantInfoList();
            response.getErrorCodeHelper();
        }



    @Test
    public void updateAssistant() throws Exception {
        AssistantUpdateRequest request = new AssistantUpdateRequest();
        AssistantInfo assistantInfo = new AssistantInfo();
        assistantInfo.setTabletCount(1);
        assistantInfo.setName("kol");
        assistantInfo.setId(1L);
        request.setAssistantInfo(assistantInfo);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        AssistantUpdateResponse response = Utils.template().postForObject(adminUrl("/assistant/update"), request, AssistantUpdateResponse.class);
        response.getAssistantInfo();
    }



        @Test
        public void countClient() throws Exception {
            CountClientRequest request = new CountClientRequest();
            //request.setTimeRegistrationStart(1415145600000L);
            //request.setTimeRegistrationEnd(1415231999000L);
            String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            CountClientResponse response = Utils.template().postForObject(adminUrl("/countClient"), request,CountClientResponse.class);
            response.getCountClientHelper();
        }



    @Test
    public void mdOrderListClient() throws Exception {
        MdOrderClientRequest request = new MdOrderClientRequest();
        request.setClientId(17);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MdOrderClientResponse response = Utils.template().postForObject(adminUrl("/mdOrderListByClient"), request, MdOrderClientResponse.class);
        response.getErrorCodeHelper();
    }



    @Test
    public void moneyRefuse() throws Exception {
        MoneyRefuseRequest request = new MoneyRefuseRequest();
        request.setMdOrderId(94);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MoneyRefuseResponse response = Utils.template().postForObject(adminUrl("/moneyRefuse"), request, MoneyRefuseResponse.class);
        response.getErrorCodeHelper();
    }




    // list
    @Test
    public void driverPenalizationList() throws Exception {
        DriverPenalizationListRequest request = new DriverPenalizationListRequest();
        request.setDriverId(new Long(1));
        //request.setStartTime(1380706434L);
        //request.setEndTime(1412242434L);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverPenalizationListResponse response = Utils.template().postForObject(adminUrl("/driver/penalizationList"), request, DriverPenalizationListResponse.class);
        response.getDriverPenalizationCashList();
    }




    @Test
    public void driverPeriodWork() throws Exception {
        DriverPeriodWorkRequest request = new DriverPeriodWorkRequest();
        request.setSecurity_token("41620dc062752d7c517971a7e1c6a207");
        //request.setActive(true);
        request.setDriverPeriodWorkId(4);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverPeriodWorkResponse response = Utils.template().postForObject(adminUrl("/driverPeriodWork"), request, DriverPeriodWorkResponse.class);
        response.getDriverPeriodWorkInfos();
    }




    @Test
    public void missionTransfer() throws Exception {
        MissionTransferRequest request = new MissionTransferRequest();
        request.setDriverIdTo(1);
        request.setMissionId(3);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MissionTransferResponse response = Utils.template().postForObject(adminUrl("/missionTransfer"), request, MissionTransferResponse.class);
    }




    @Test
    public void updateMissionTest() throws Exception{

        MissionsListRequest request = new MissionsListRequest();

        //request.setPhoneMask("+79538695880");

        request.setNumberPage(0);
        request.setSizePage(1);

        MissionsListResponse response = Utils.template().postForObject(adminUrl("/mission/list"), request, MissionsListResponse.class);

        MissionInfoARM missionInfoARM =  response.getMissionInfoARM().get(0);
        missionInfoARM.setMissionState("BOOKED");

        DriverInfoARM driverInfoARM = new DriverInfoARM();
        driverInfoARM.setId(11);

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setId(1);

        missionInfoARM.setDriverInfoARM(driverInfoARM);
        missionInfoARM.setClientInfo(clientInfo);

        /*
        1. Статус заказа
        2. Клиент
        3. Водитель
        4. Адрес отправления (с lat и lon)
        5. Адрес назначения (с lat и lon)
        6. Сумма заказа
        7. Время заказа - no
        8. Комментарий
        9. Тип заказа (CASH, CARD)
        */

        //missionInfo.s

        UpdateMissionRequest request1 = new UpdateMissionRequest();
        request1.setMissionInfoARM(missionInfoARM);

        UpdateMissionResponse response2 = Utils.template().postForObject(adminUrl("/updateMission"), request1, UpdateMissionResponse.class);
        MissionInfoARM missionInfo2 = response2.getMissionInfoARM();

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request1);
        LOGGER.info("asString = " + asString);
    }



    @Test
    public void tempPass() throws Exception{
        TemporaryPasswordRequest request = new TemporaryPasswordRequest();
        request.setSecurity_token("69aeb09f6deb54e7e085ce9824ded788");
        request.setWebUserId(18);
        //request.setClientId(17);
        request.setPhone("+79538695889");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TemporaryPasswordResponse response = Utils.template().postForObject(adminUrl("/tempPass"), request, TemporaryPasswordResponse.class);
        response.getErrorCodeHelper();
    }


    @Test
    public void checkBonus() throws Exception{
        BonusSumAmountRequest request = new BonusSumAmountRequest();
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        BonusSumAmountResponse response = Utils.template().postForObject(adminUrl("/checkBonus"), request, BonusSumAmountResponse.class);
        response.getDriverIdList();
        response.getErrorCodeHelper();
    }




        @Test
        public void updateAutoClassPrices() throws Exception{
            UpdateAutoClassPriceRequest request = new UpdateAutoClassPriceRequest();
            request.setSecurity_token("zzz");

            AutoClassRateInfoV2 autoClassInfo = new AutoClassRateInfoV2();
             /*STANDARD*/

//            autoClassInfo.setFreeWaitMinutes(10);
//            autoClassInfo.setIntercity(16);
//            autoClassInfo.setKmIncluded(3);
//            autoClassInfo.setPerHourAmount(0);
//            autoClassInfo.setPerMinuteWaitAmount(5);
//            autoClassInfo.setPrice(150);
//            autoClassInfo.setPriceHour(350);
//            autoClassInfo.setPriceKm(12);
//            autoClassInfo.setAutoClass(1); // 1 - STANDARD
//            autoClassInfo.setActive(true);
//            autoClassInfo.setActivePicUrl("http://stg.taxisto.ru/profiles/car_standart.png");
//            autoClassInfo.setNotActivePicUrl("http://stg.taxisto.ru/profiles/car_standart_unselected.png");
//            autoClassInfo.setAutoClassStr("STANDARD");
//            autoClassInfo.setAutoExample("Renault Logan, Ford Focus, Hyundai Solaris и т.п.");
//            autoClassInfo.setDescription("");



            // COMFORT

//            autoClassInfo.setFreeWaitMinutes(10);
//            autoClassInfo.setIntercity(20);
//            autoClassInfo.setKmIncluded(3);
//            autoClassInfo.setPerHourAmount(0);
//            autoClassInfo.setPerMinuteWaitAmount(5);
//            autoClassInfo.setPrice(200);
//            autoClassInfo.setPriceHour(450);
//            autoClassInfo.setPriceKm(15);
//            autoClassInfo.setAutoClass(2); // 2 - COMFORT
//            autoClassInfo.setActive(true);
//            autoClassInfo.setActivePicUrl("http://stg.taxisto.ru/profiles/car_comfort.png");
//            autoClassInfo.setNotActivePicUrl("http://stg.taxisto.ru/profiles/car_comfort_unselected.png");
//            autoClassInfo.setAutoClassStr("COMFORT");
//            autoClassInfo.setAutoExample("Nissan Teana, Skoda Octavia, Ford Mondeo и т.п.");
//            autoClassInfo.setDescription("");



             // BUSINESS

//            autoClassInfo.setFreeWaitMinutes(10);
//            autoClassInfo.setIntercity(25);
//            autoClassInfo.setKmIncluded(3);
//            autoClassInfo.setPerHourAmount(0);
//            autoClassInfo.setPerMinuteWaitAmount(10);
//            autoClassInfo.setPrice(300);
//            autoClassInfo.setPriceHour(700);
//            autoClassInfo.setPriceKm(20);
//            autoClassInfo.setAutoClass(3); // 3 - COMFORT
//            autoClassInfo.setActive(true);
//            autoClassInfo.setActivePicUrl("http://stg.taxisto.ru/profiles/car_business.png");
//            autoClassInfo.setNotActivePicUrl("http://stg.taxisto.ru/profiles/car_business_unselected.png");
//            autoClassInfo.setAutoClassStr("BUSINESS");
//            autoClassInfo.setAutoExample("Toyota Camry");
//            autoClassInfo.setDescription("");



//            //LOW_COSTER
            autoClassInfo.setFreeWaitMinutes(2);
            autoClassInfo.setIntercity(0);
            autoClassInfo.setKmIncluded(3);
            autoClassInfo.setPerHourAmount(0);
            autoClassInfo.setPerMinuteWaitAmount(5);
            autoClassInfo.setPrice(59);
            autoClassInfo.setPriceHour(350);
            autoClassInfo.setPriceKm(10);
            //autoClassInfo.setPriceKmCorporate(21);
            autoClassInfo.setAutoClass(4); // 4 - LOW_COSTER
            autoClassInfo.setActive(true);
            autoClassInfo.setActivePicUrl("http://stg.taxisto.ru/profiles/car_low_cost_new.png"); // car_low_cost
            autoClassInfo.setNotActivePicUrl("http://stg.taxisto.ru/profiles/car_low_cost_new.png"); // car_low_cost_unselected
            autoClassInfo.setAutoClassStr("LOW_COSTER");
            autoClassInfo.setAutoExample("Машины классов \"Стандарт\" и \"Комфорт\".");
            autoClassInfo.setDescription("Мы разработали этот тариф чтобы сделать поездку с Таксисто еще более доступной.\n" +
                    "\n"+
                    "Тариф Лоукостер обслуживают машины классов Стандарт и Комфорт, и мы как всегда, следим за тем, чтобы машины были красивые, чистые и водители вежливые");
            request.setAutoClassRateInfoV2(autoClassInfo);
            String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            UpdateAutoClassPriceResponse response = Utils.template().postForObject(adminUrl("/updateAutoClassPrice"), request, UpdateAutoClassPriceResponse.class);
            response.getErrorCodeHelper();
        }





    @Test
    public void setFantomDriver() throws Exception{
        FantomDriverRequest request = new FantomDriverRequest();
        request.setClientId(24L);
        request.setMissionId(118697L);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        FantomDriverResponse response = Utils.template().postForObject(adminUrl("/setDriverTypeX"), request, FantomDriverResponse.class);
        response.getErrorMessage();
    }



    @Test
    public void insertAutoClassPrices() throws Exception{
        InsertAutoClassPriceRequest request = new InsertAutoClassPriceRequest();
        request.setSecurity_token("357ca930422337836f6baada6cf93820");

        AutoClassRateInfoV2 autoClassInfo = new AutoClassRateInfoV2();
        autoClassInfo.setFreeWaitMinutes(2);
        autoClassInfo.setIntercity(0);
        autoClassInfo.setKmIncluded(3);
        autoClassInfo.setPerHourAmount(0);
        autoClassInfo.setPerMinuteWaitAmount(5);
        autoClassInfo.setPrice(59);
        autoClassInfo.setPriceHour(350);
        autoClassInfo.setPriceKm(20);
        //autoClassInfo.setPriceKmCorporate(21);
        autoClassInfo.setAutoClass(5); // 1 - LOW_COSTER
        autoClassInfo.setActive(true);
        autoClassInfo.setActivePicUrl("http://stg.taxisto.ru/profiles/car_standart.png");
        autoClassInfo.setNotActivePicUrl("http://stg.taxisto.ru/profiles/car_standart_unselected.png");
        autoClassInfo.setAutoClassStr(AutoClass.BONUS.name());
        autoClassInfo.setAutoExample("Renault Logan, Ford Focus, Hyundai Solaris и т.п.");
        //autoClassInfo.setDescription("Мы разработали этот тариф чтобы сделать поездку с Таксисто еще более доступной.\n" +
        //        "Тариф Лоукостер обслуживают машины классов Стандарт и Коморрт, и мы как всегда следим за тем чтобы машины были красивые, чистые и водители вежливые!");
        request.setAutoClassRateInfoV2(autoClassInfo);
/*
50 рублей посадка
2 минуты бесплатного ожидания
20 руб/км
Платное время ожидания и остановки - 5 руб./мин.
 */

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        InsertAutoClassPriceResponse response = Utils.template().postForObject(adminUrl("/insertAutoClassPrice"), request, InsertAutoClassPriceResponse.class);
        response.getErrorCodeHelper();
    }




    @Test
    public void autoClassPrices() throws Exception{
        AutoClassPriceRequest request = new AutoClassPriceRequest();
        request.setSecurity_token("60dfc8acc6567fb265008c9c57fa2235");
        //request.setAutoClass(1);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        AutoClassPriceResponse response = Utils.template().postForObject(adminUrl("/autoClassPrice"), request, AutoClassPriceResponse.class);
        response.getAutoClassRateInfoV2List();
    }




    @Test
    public void testDriverStatAuto() throws Exception{
        DriverTimeWorkStatisticRequest request = new DriverTimeWorkStatisticRequest();
        request.setSecurity_token("fbf62afef81fa57a9e8016d01ba07dd1");
        request.setDriverId(22);
        request.setStartTime("2015-04-18");
        request.setEndTime("2015-04-19");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverTimeWorkStatisticResponse response = Utils.template().postForObject(adminUrl("/driverTimeWorkStatistic"), request, DriverTimeWorkStatisticResponse.class);
        response.getDriverTimeWorkInfos();
    }





    @Test
    public void moneyWithdrawal() throws Exception {
        MoneyWithdrawalRequest request = new MoneyWithdrawalRequest();
        //Код подтверждения для выдачи наличных
        request.setCountSymbols(6);
        request.setDriverId(8);
        request.setSmsCode("213535");

        MoneyWithdrawalResponse response = Utils.template().postForObject(adminUrl("/moneyWithdrawal"), request, MoneyWithdrawalResponse.class);
        response.getErrorCodeHelper();
    }



    @Test
    public void testFindMissionListByMask() throws Exception {
        MissionsListRequest request = new MissionsListRequest();
        request.setSecurity_token("zzz"); // 7f035a73303ecf35a2d42bcf72f59254
        request.setSizePage(10);
        request.setNumberPage(0);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MissionsListResponse response = Utils.template().postForObject(adminUrl("/mission/list"), request, MissionsListResponse.class);
        response.getTotalItems();
        response.getMissionInfoARM();
    }




    @Test
    public void getClientGlobalStats() throws Exception {
        GlobalClientStatsRequest request = new GlobalClientStatsRequest();
        request.setRegistrationStart(1375194052);
        request.setRegistrationEnd(1438153556);
        request.setSecurity_token("zzz");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        Future<GlobalClientStatsResponse> response = TEMPLATE.postForObject(adminUrl("/client/global/stats"), request, Future.class);
        GlobalClientStatsResponse res = response.get();
        res.getGlobalClientStatsInfos();
    }



    @Test
    public void getDriverGlobalStats() throws Exception {
        GlobalDriverStatsRequest request = new GlobalDriverStatsRequest();
        request.setTaxoparkId(13);
        //request.setAssistantId(1);
        request.setRegistrationStart(1391250430);
        request.setRegistrationEnd(1424168830);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        GlobalDriverStatsResponse response = TEMPLATE.postForObject(adminUrl("/driver/global/stats"), request, GlobalDriverStatsResponse.class);
        response.getGlobalDriverStatsInfos();
    }








    @Test
    public void testFindDriverListByMask() throws Exception {
        DriversListRequest request = new DriversListRequest();
        request.setSecurity_token("zzz");
        //request.setDriverId(3);
        //request.setPhoneMask("913");
        //request.setNameMask("Юл");
        //request.setCarNumberMask("54");

        //request.setFromPosition(0);
        //request.setCountItems(4);
        request.setNumberPage(0);
        request.setSizePage(10);

        List<String> driverTypes = Arrays.asList("BOTH");
        //request.setDriverTypes(driverTypes);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriversListResponse response = Utils.template().postForObject(adminUrl("/driver/list"), request, DriversListResponse.class);
        response.getDriverInfoARM();
    }





    @Test
    public void ratings() throws Exception {
        DriverRatingARMRequest request = new DriverRatingARMRequest();
        request.setSecurity_token("zzz");
        request.setDriverId(1);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverRatingARMResponse response = Utils.template().postForObject(adminUrl("/driver/rating"), request, DriverRatingARMResponse.class);
        response.getErrorCodeHelper();
    }

    @Test
    public void testEstimateInfoAll() throws Exception {
        EstimateInfoRequest request = new EstimateInfoRequest();
        request.setSecurity_token("zzz");
        QueryDetails queryDetails = new QueryDetails();
        queryDetails.setNameParam("comment");
        queryDetails.setEqual(2);
        //queryDetails.setOperationQuery("between");
        //queryDetails.setStart(1418393580L); // 1417413360
        //queryDetails.setEnd(1418479980L); // 1418450160
        //queryDetails.setEqual(1);

        //QueryDetails queryDetails2 = new QueryDetails();
        //queryDetails2.setNameParam("applicationConvenience");
        //queryDetails2.setOperationQuery("between");
        //queryDetails2.setStart(0);
        //queryDetails2.setEnd(15);

        request.getQueryDetailsList().add(queryDetails);
        //request.getQueryDetailsList().add(queryDetails2);

        request.setNumberPage(1);
        request.setSizePage(10);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        EstimateInfoResponse response = Utils.template().postForObject(adminUrl("/estimateInfoAll"), request, EstimateInfoResponse.class);
        response.getEstimateInfoARMList();
        response.getTotalItems();
    }



    @Test
    public void transferEstimate() throws Exception {
        EstimateTransferRequest request = new EstimateTransferRequest();
        request.setStart(true);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        EstimateTransferResponse response = Utils.template().postForObject(adminUrl("/transferEstimate"), request, EstimateTransferResponse.class);
        response.isTransfer();
    }




    @Test
    public void transferDeviceType() throws Exception {
        DeviceTypeTransferRequest request = new DeviceTypeTransferRequest();
        request.setStart(true);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DeviceTypeTransferResponse response = Utils.template().postForObject(adminUrl("/transferDeviceType"), request, DeviceTypeTransferResponse.class);
        response.getTimestamp();
    }



    @Test
    public void getPropertyValue() throws Exception {
        PropertyRequest request = new PropertyRequest();
        request.setPropName("booked_active");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        PropertyResponse response = Utils.template().postForObject(adminUrl("/getPropertyValue"), request, PropertyResponse.class);
        String val = response.getPropValue();
    }



    @Test
    public void testFindClientListByMask() throws Exception {
        ClientsListRequest request = new ClientsListRequest();
        request.setSecurity_token("1e6c12f3c9c6a4e9658d8cab747d85b7");
        request.setCourierActivated(false);
        request.setNumberPage(1);
        request.setSizePage(10);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ClientsListResponse response = Utils.template().postForObject(adminUrl("/client/list"), request, ClientsListResponse.class);
        response.getClientInfo();
    }




    //f:add
    @Test
    public void testFindOnlyMissionBooked() throws Exception {

        BookedRequest request = new BookedRequest();
        request.setDriverId(8);

        BookedResponse response = Utils.template().postForObject(adminUrl("/mission/booked"), request, BookedResponse.class);
        List<MissionInfo> listMissionInfo = response.getMissions();
        //int bookedNew=response.getBookedNew();
        int bookedToMe=response.getBookedToMe();
        listMissionInfo.size();
    }





    @Test
    public void updateDriverSetting() throws Exception {
        UpdateDriverSettingRequest request = new UpdateDriverSettingRequest();
        request.setSecurity_token("zzz");

        DriverSettingInfo info =  new DriverSettingInfo();
        info.setId(72l);
        info.setToAddress("zzzsdsdsdzz");
        info.setDriverId(8);
        AdditionalServiceInfo additionalServiceInfo = new AdditionalServiceInfo();
        additionalServiceInfo.setId(2);

        AdditionalServiceInfo as = new AdditionalServiceInfo();
        as.setId(1);
        info.getAdditionalServices().add(additionalServiceInfo);
        //info.getAdditionalServices().add(as);


        //info.setAdditionalServices(null);


        request.setDriverSettingInfo(info);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateDriverSettingResponse response = TEMPLATE.postForObject(adminUrl("/updateDriverSetting"), request, UpdateDriverSettingResponse.class);
        response.getErrorMessage();
    }



    @Test
    public void driverNews() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setNewsId(11);
        request.setSecurity_token("28a7c743a9ed033528b21f5746bf98ce");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        NewsResponse response = TEMPLATE.postForObject(adminUrl("/driverNews"), request, NewsResponse.class);
        response.getNewsInfos();
    }





    @Test
    public void driverCorrection() throws Exception {
        DriverCorrectionRequest request = new DriverCorrectionRequest();
        request.setSecurity_token("4134a105d3f3548d3016629e52d90ed1");
        request.setDriverId(7);
        //request.setWebUserId(2);
        request.setArticleId(1);
        //request.setStartTime(1424217600000L);
        //request.setEndTime(1424303999000L);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverCorrectionResponse response = TEMPLATE.postForObject(adminUrl("/driverCorrection"), request, DriverCorrectionResponse.class);
        response.getDriverCorrectionInfos();
    }




    @Test
    public void testFindDriverARM() throws Exception {
        DriverFindARMRequest request = new DriverFindARMRequest();
        request.setDriverId(8);
        request.setSecurity_token("zzz");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverFindARMResponse response = TEMPLATE.postForObject(adminUrl("/driver/find/arm"), request, DriverFindARMResponse.class);
        response.getDriverInfoARM();
        response.getServerState();
    }



    @Test
    public void missionDetailsARM() throws Exception {
        MissionInfoARMRequest request = new MissionInfoARMRequest();
        request.setMissionId(144978);// dev - 5125 loc - 59897
        request.setSecurity_token("85b66e6560fbe5cb8c5c53fff5fc48be"); // 8c4153067afa1fec60272f133cf42cb7 loc - 6db824ebe779c9edcafff2b22818a590

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        MissionInfoARMResponse response = TEMPLATE.postForObject(adminUrl("/mission/details/arm"), request, MissionInfoARMResponse.class);
        response.getMissionInfoARM();
    }





    @Test
    public void updateEstimate() throws Exception {
        UpdateEstimateRequest request = new UpdateEstimateRequest();

        EstimateInfoARM e1 = new EstimateInfoARM();
        e1.setId(4L);
        e1.setVisible(false);

        EstimateInfoARM e2 = new EstimateInfoARM();
        e2.setId(5L);
        e2.setVisible(false);

        request.getEstimateInfoARMList().add(e1);
        request.getEstimateInfoARMList().add(e2);

        request.setSecurity_token("qwerty");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateEstimateResponse response = TEMPLATE.postForObject(adminUrl("/update/estimate"), request, UpdateEstimateResponse.class);
        response.getTimestamp();
    }




    @Test
    public void updateNews() throws Exception {
        UpdateNewsRequest request = new UpdateNewsRequest();
        request.setSecurity_token("ceca6d4cfcd5cc61773eecfbfbaf3824");
        NewsInfo info = new NewsInfo();
        info.setUrlNews("test");
        info.setTimeOfStarting(0);
        info.setTimeOfFinishing(0);
        info.setReading(true);
        info.setTitle("test");
        info.setTextNews("test");
        request.setNewsInfo(info);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateNewsResponse response = TEMPLATE.postForObject(adminUrl("/news/update"), request, UpdateNewsResponse.class);
        response.getNewsInfo();
    }


    @Test
    public void smsSendARM() throws Exception {
        SmsSendARMRequest request = new SmsSendARMRequest();
        request.setSecurity_token("zzz"); // 7be83ce647cc10ab5cac25d008af4ce4
        request.setPhone("+79538695889");
        request.setMessage("от пра вля ю");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        SmsSendARMResponse response = TEMPLATE.postForObject(adminUrl("/smsSendARM"), request, SmsSendARMResponse.class);
        response.getErrorCodeHelper();
    }



    @Test
    public void estimateInfoByMission() throws Exception {
        EstimateInfoByMissionRequest request = new EstimateInfoByMissionRequest();
        request.setMissionId(21);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        EstimateInfoByMissionResponse response = TEMPLATE.postForObject(adminUrl("/estimateInfoByMission"), request, EstimateInfoByMissionResponse.class);
        response.getEstimateInfoDetails();
    }



    @Test
    public void testMissionComplete() throws Exception {
        MissionCompleteRequest request = new MissionCompleteRequest();
        request.setMissionId(14573);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        MissionCompleteResponse response = TEMPLATE.postForObject(adminUrl("/mission/completed"), request, MissionCompleteResponse.class);
        response.getErrorCodeHelper();
    }




    @Test
    public void findLocationListMongo() throws Exception {
      LocationMongoRequest request = new LocationMongoRequest();
        request.setDriverId(125);
        //request.setType("ARRIVED"); // ARRIVED // type=FREE_VIEW
        request.setMissionId(4636);

        //request.setStartWhenSeen(1413284280000L); // 1413284280000
        //request.setEndWhenSeen(1415995140000L); // 1415995140000
        //request.setLatStart(52);
        //request.setLatEnd(60);
        //request.setLonStart(20);
        //request.setLonEnd(80);

        request.setNumberPage(1);
        request.setSizePage(100);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        LocationMongoResponse response = TEMPLATE.postForObject(adminUrl("/locationMongoList"), request, LocationMongoResponse.class);
        response.getListLocation();
    }


    @Test
    public void testFindCurrentStateDriver() throws Exception {
        DriverCurrentStateRequest request = new DriverCurrentStateRequest();
        request.setDriverId(3);
        //String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverCurrentStateResponse response = TEMPLATE.postForObject(adminUrl("/driver/currentstate"), request, DriverCurrentStateResponse.class);
        String state = response.getDriverState();
    }



    @Test
    public void testFindBookingMission() throws Exception {
        BookingMissionInfoRequest request = new BookingMissionInfoRequest();
        request.setMissionId(29);
        //String asString = OBJECT_MAPPER.writeValueAsString(request);
        BookingMissionInfoResponse response = TEMPLATE.postForObject(adminUrl("/mission/bookingmission"), request, BookingMissionInfoResponse.class);
    }




    @Test
    public void testFindOneDriver() throws Exception {
        DriverInfoRequest request = new DriverInfoRequest();
        request.setDriverId(3);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        AdministrativeDriverInfoResponse response = TEMPLATE.postForObject(adminUrl("/driver/find"), request, AdministrativeDriverInfoResponse.class);
        response.getDriverInfo();
    }




    @Test
    public void comissionUpdate() throws Exception {
        ComissionUpdateRequest request = new ComissionUpdateRequest();

        ComissionInfo comissionInfo = new ComissionInfo();
        comissionInfo.setComissionType(0);
        comissionInfo.setComissionAmount(10);
        comissionInfo.setObjectId(0);
        comissionInfo.setUpdateTime(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6()));
        comissionInfo.setStartTime(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6()));
        comissionInfo.setEndTime(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6().plusDays(100)));

        request.setComissionInfo(comissionInfo);
        request.setSecurity_token("36b3bc1e0d4e3eb69b31bd2756084556");

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ComissionUpdateResponse response = TEMPLATE.postForObject(adminUrl("/comissionUpdate"), request, ComissionUpdateResponse.class);
        response.getErrorMessage();
    }


    @Test
    public void testFindDriversAll() throws Exception {
        FindDriversRequest request = new FindDriversRequest();
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        FindDriversResponse response = TEMPLATE.postForObject(adminUrl("/driver/findall"), request, FindDriversResponse.class);
    }




    @Test
    public void driverRequisiteUpdate() throws Exception {
        DriverRequisiteUpdateRequest request = new DriverRequisiteUpdateRequest();
        DriverRequisiteInfoARM info = new DriverRequisiteInfoARM();
        info.setId(1);
        info.setCountMinutesOfRest(25);
        info.setDismissalTime(DateTimeUtils.nowNovosib_GMT6().getMillis());
        info.setDriverId(10);
        info.setEndHours(22);
        info.setEndMinutes(00);
        info.setSalaryPerDay(23000);
        info.setStaffer(false);
        info.setStartHours(11);
        info.setStartMinutes(00);
        info.setTypeDismissal(1);
        info.setSalaryPriority(2);
        info.setActive(true);

        request.setDriverRequisiteInfo(info);
        request.setSecurity_token("6db824ebe779c9edcafff2b22818a590");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        DriverRequisiteUpdateResponse response = TEMPLATE.postForObject(adminUrl("/driverRequisiteUpdate"), request, DriverRequisiteUpdateResponse.class);
        response.getErrorMessage();
    }




    @Test
    public void driverRequisite() throws Exception {
        DriverRequisiteRequest request = new DriverRequisiteRequest();
        request.setSecurity_token("6db824ebe779c9edcafff2b22818a590");
        request.setDriverId(8);
        //request.setDriverRequisiteId(1);
        //String asString = OBJECT_MAPPER.writeValueAsString(request);

        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //JSONObject projectVO = OBJECT_MAPPER.getForObject(adminUrl("/driverRequisite"), request, JSONObject.class);

        DriverRequisiteResponse response = TEMPLATE.postForObject(adminUrl("/driverRequisite"), request, DriverRequisiteResponse.class);
        response.getDriverRequisiteInfos();
    }




    @Test
    public void getComission() throws Exception {
        ComissionRequest request = new ComissionRequest();
        request.setSecurity_token("edaf963217d144e89735b9cefd3c50e8");
        request.setComissionType(1);
        request.setObjectId(8);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ComissionResponse response = TEMPLATE.postForObject(adminUrl("/comission"), request, ComissionResponse.class);
        response.getComissionInfoList();
    }



    @Test
    public void assigningDriverToBooking() throws Exception {
        AssigningDriverToBookingRequest request = new AssigningDriverToBookingRequest();
        request.setDriverId(8);
        request.setMissionId(3941);
        request.setSecurity_token("fc49be0508bf3156d278b11187440dd2");
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        AssigningDriverToBookingResponse response = TEMPLATE.postForObject(adminUrl("/assigningDriverToBooking"), request, AssigningDriverToBookingResponse.class);
    }


    @Test
    public void testFindDriver() throws Exception {
        FindDriversRequest request = new FindDriversRequest();
        List<Long> list = new ArrayList<>();
        list.add(8L);
        request.setIds(list);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        //FindDriversResponse r = OBJECT_MAPPER.readValue(asString, FindDriversResponse.class);

        FindDriversResponse response = TEMPLATE.postForObject(adminUrl("/driver/findall"), request, FindDriversResponse.class);
        response.getDrivers();
    }

    @Test
    public void testUpdateDriver() throws Exception {
        DriverUpdateRequest request = new DriverUpdateRequest();
        FindDriversRequest requestFind = new FindDriversRequest();
        requestFind.getIds().add((long) 1);
        String asString1 = OBJECT_MAPPER.writeValueAsString(request);
        FindDriversResponse responseFind = TEMPLATE.postForObject(adminUrl("/driver/findall"), request, FindDriversResponse.class);
        List<DriverInfo> drivers = responseFind.getDrivers();
        if (!drivers.isEmpty()){
            DriverInfo driverInfo = drivers.get(0);

            driverInfo.getPhotosCarsPictures().add(ProfilesUtils.getPicture("driver.jpg"));
            String encodeFromFile = ProfilesUtils.getPicture("client.png");
            String asString = OBJECT_MAPPER.writeValueAsString(request);
            DriverUpdateResponse response = TEMPLATE.postForObject(adminUrl("/driver/update"), request, DriverUpdateResponse.class);
        }
    }



        @Test
        public void findAssistantStatsListByMask() throws Exception {
            AssistantStatListRequest request = new AssistantStatListRequest();
            request.setNumberPage(1);
            request.setSizePage(10);

            List<Long> assistantIdList = new ArrayList<>();
            assistantIdList.add(1L);
            assistantIdList.add(2L);

            //request.setDriverId(1);
            //request.setAssistantId(assistantIdList);

            //request.setTimeRequestingStart(1389967344);
            //request.setTimeRequestingEnd(1416232944);

            String asString = OBJECT_MAPPER.writeValueAsString(request);
            AssistantStatListResponse response = TEMPLATE.postForObject(adminUrl("/assistant/stat/list"), request, AssistantStatListResponse.class);
            response.getAssistantStatsInfos();
        }





    @Test
    public void findAssistantStatsListByMaskExtended() throws Exception {
        AssistantStatListRequest request = new AssistantStatListRequest();
        request.setNumberPage(1);
        request.setSizePage(300);

        List<Long> assistantIdList = new ArrayList<>();
        assistantIdList.add(1L);
        //assistantIdList.add(2L);

        //request.setDriverId(1);
        //request.setTaxoparkId(1);
        request.setAssistantId(assistantIdList);

        request.setTimeRequestingStart(1406904093);
        request.setTimeRequestingEnd(1416232944);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        AssistantStatListResponse response = TEMPLATE.postForObject(adminUrl("/assistant/stat/list/ext"), request, AssistantStatListResponse.class);
        response.getAssistantStatsInfos();
    }



    @Test
    public void testUpdateDriverARM() throws Exception {
        DriverInfoRequest request = new DriverInfoRequest();
        request.setDriverId(6);

        String asString1 = OBJECT_MAPPER.writeValueAsString(request);
        DriverInfoARMResponse responseFind = TEMPLATE.postForObject(adminUrl("/driver/find/test"), request, DriverInfoARMResponse.class);
        DriverInfoARM driverInfoARM = responseFind.getDriverInfoARM();

        driverInfoARM.setFirstName("test333");

        AssistantInfo assistantInfo = new AssistantInfo();
        assistantInfo.setId(2L);

        TaxoparkPartnersInfo taxoparkPartnersInfo = new TaxoparkPartnersInfo();
        taxoparkPartnersInfo.setId(4L);

        driverInfoARM.setAssistantInfo(assistantInfo);
        driverInfoARM.setTaxoparkPartnersInfo(taxoparkPartnersInfo);

        driverInfoARM.setDream("sdsdsd");
        driverInfoARM.setGrowth(3);
        driverInfoARM.setChildrens(true);

        DriverChangeARMRequest driverChangeARMRequest = new DriverChangeARMRequest();
        driverChangeARMRequest.setDriverInfoARM(driverInfoARM);

        String asString = OBJECT_MAPPER.writeValueAsString(driverChangeARMRequest);
        DriverChangeARMResponse response = TEMPLATE.postForObject(adminUrl("/driver/update/arm"), driverChangeARMRequest, DriverChangeARMResponse.class);
        response.getDriverInfoARM();
    }



    public static DriverInfo buildDriverInfo() {
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setFirstName("Валентина");
        driverInfo.setLastName("Гордеева");
        driverInfo.setBirthDate(DateTimeUtils.now().getMillis());
        driverInfo.setAutoModel("ВАЗ");
        driverInfo.setAutoClass(AutoClass.BUSINESS.getValue());
        driverInfo.setAutoColor("Красный");
        driverInfo.setAutoNumber(StrUtils.generateRandomAutoNumber());
        driverInfo.setBalance(500);
        driverInfo.setTotalRating(0);
        driverInfo.setPassword("123456");
        driverInfo.setPhone(StrUtils.generateRandomPhone());
        return driverInfo;
    }

    @Test
    public void testResolveUserIdInfoInvalid() throws Exception {

    }

    @Test
    public void testCurrentMission() throws Exception {

    }

    @Test
    public void testCurrentMissionForClient() throws Exception {

    }

    @Test
    public void testCurrentMissionForDriver() throws Exception {

    }



    @Test
    public void listPrivateTariff() throws Exception{
        PrivateTariffRequest request = new PrivateTariffRequest();
        request.setSecurity_token("da629771818e32e586bbd85251a39850");
        request.setClientId(24);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        PrivateTariffResponse response = Utils.template().postForObject(adminUrl("/privateTariff"), request, PrivateTariffResponse.class);
        response.getPrivateTariffInfos();
    }






    @Test
    public void fantomStat() throws Exception{
        FantomStatRequest request = new FantomStatRequest();
        request.setSecurity_token("f2819bec9fffcc1ac39cabef85c2048a");
        request.setNumPage(0);
        request.setPageSize(15);
        //request.setState("COMPLETED");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        FantomStatResponse response = Utils.template().postForObject(adminUrl("/fantomStat"), request, FantomStatResponse.class);
        response.getFantomStatInfos();
        response.getGeneralMissionSum();
        response.getGeneralIncreaseSum();
    }



    @Test
    public void activateTariff() throws Exception{
        ActivateTariffRequest request = new ActivateTariffRequest();
        request.setSecurity_token("da629771818e32e586bbd85251a39850");
        request.setClientId(24);
        request.setActive(true);
        request.setActivated(true);
        request.setExpirationDate(DateTimeUtils.nowNovosib_GMT6().plusDays(7).getMillis());
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ActivateTariffResponse response = Utils.template().postForObject(adminUrl("/activateTariff"), request, ActivateTariffResponse.class);
        response.getErrorMessage();
    }



    @Test
    public void updateTariffRestriction() throws Exception{
        UpdateTariffRestrictionRequest request = new UpdateTariffRestrictionRequest();
        request.setSecurity_token("a2818d07d82ea97c3a35087d9a893a03");
        request.setOff(true);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        UpdateTariffRestrictionResponse response = Utils.template().postForObject(adminUrl("/updateTariffRestriction"), request, UpdateTariffRestrictionResponse.class);
        response.getErrorMessage();
    }



    @Test
    public void tariffRestriction() throws Exception{
        TariffRestrictionRequest request = new TariffRestrictionRequest();
        request.setSecurity_token("a2818d07d82ea97c3a35087d9a893a03");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TariffRestrictionResponse response = Utils.template().postForObject(adminUrl("/tariffRestriction"), request, TariffRestrictionResponse.class);
        response.getErrorMessage();
        response.getTariffRestrictionInfos();
    }




    @Test
    public void driverTimeWorkAndMissionCompleteStat() throws Exception{
        DriverTimeWorkAndMissionCompleteStatRequest request = new DriverTimeWorkAndMissionCompleteStatRequest();
        request.setSecurity_token("c6054553d78ef2ab4439275219e60a41");// a7823db1ea0b48b8d1b296cf5647d32b
        request.setStartTime("2015-06-19");
        request.setEndTime("2015-06-19");
        //request.setDriverId(8);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverTimeWorkAndMissionCompleteStatResponse response = Utils.template().postForObject(adminUrl("/driverTimeWorkAndMissionCompleteStat"), request, DriverTimeWorkAndMissionCompleteStatResponse.class);
        response.getDriverTimeWorkAndMissionCompleteInfos();
    }
}
