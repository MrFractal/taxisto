package ru.trendtech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.courier.web.*;
import ru.trendtech.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.adminCourierUrl;

/**
 * Created by petr on 01.09.2015.
 */
public class AdminCourierControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCourierControllerTest.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final RestTemplate TEMPLATE = Utils.template();



    @BeforeMethod
    public void forseSetProfile() throws Exception {
        ProfilesUtils.setProfileForce("fractal");
    }






    @Test
    public void updateItem() throws Exception {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setSecurity_token("zzz");

        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setId(8);
        itemInfo.setItemName("Сладовар");
        itemInfo.setActive(true);
        itemInfo.setDefaultItemPrice(100);
        itemInfo.setItemType("PRODUCT");

        List<ItemPropertyInfo> itemPropertyInfos = new ArrayList<>();
        ItemPropertyInfo itemPropertyInfo = new ItemPropertyInfo();
        itemPropertyInfo.setId(1);
        itemPropertyInfo.setAlcohol(false);
        itemPropertyInfo.setNamePoperty("Бухло");

        itemPropertyInfos.add(itemPropertyInfo);

        itemInfo.setItemPropertyInfos(itemPropertyInfos);

        request.getItemInfoList().add(itemInfo);

        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateItemResponse response = TEMPLATE.postForObject(adminCourierUrl("/updateItem"), request, UpdateItemResponse.class);
        response.getItemInfoList();
        response.getErrorCode();
        response.getErrorMessage();
    }



    @Test
    public void items() throws Exception {
        ItemRequest request = new ItemRequest();
        request.setSecurity_token("zzz");
        //request.setMask("пиво");
        request.setNumberPage(1);
        request.setSizePage(10);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ItemResponse response = TEMPLATE.postForObject(adminCourierUrl("/items"), request, ItemResponse.class);
        response.getItemInfos();
        response.getLastPageNumber();
        response.getTotalItems();
    }






    @Test
    public void activateCourierService() throws Exception {
        ActivateCourierServiceRequest request = new ActivateCourierServiceRequest();
        List<Long> ids = Arrays.asList(14L);
        request.setSecurity_token("d02e4d1529afbb1efd34b1c8bba21c7f");
        request.setClientIds(ids);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ActivateCourierServiceResponse response = TEMPLATE.postForObject(adminCourierUrl("/activateCourierService"), request, ActivateCourierServiceResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }






    @Test
    public void orders() throws Exception {
        OrdersRequest request = new OrdersRequest();
        request.setSecurity_token("995d96d40d65dc8e45a5d5b1be779dea"); // 23e99219508a8cdeb449ea41e9f6bf0d
        //request.setState("CANCELED");
        //request.setClientId(24);
        //request.setStoreId(10);
        //request.setItemId(7);
        request.setNumberPage(1);
        request.setSizePage(20);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        OrdersResponse response = TEMPLATE.postForObject(adminCourierUrl("/orders"), request, OrdersResponse.class);
        response.getOrderInfoList();
        response.getLastPageNumber();
        response.getTotalItems();
    }





    @Test
    public void itemPrices() throws Exception {
        ItemPriceRequest request = new ItemPriceRequest();
        request.setSecurity_token("adffbfa8979fa3f15565c01010c9c4dd");
        request.setNumberPage(1);
        request.setSizePage(20);
        request.setOrderId(97);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        ItemPriceResponse response = TEMPLATE.postForObject(adminCourierUrl("/itemPrices"), request, ItemPriceResponse.class);
        response.getItemPriceInfoList();
        response.getLastPageNumber();
        response.getTotalItems();
    }




    @Test
    public void storeAddresses() throws Exception {
        StoreAddressRequest request = new StoreAddressRequest();
        request.setSecurity_token("adffbfa8979fa3f15565c01010c9c4dd");
        request.setNumberPage(1);
        request.setSizePage(20);
        request.setOrderId(97);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        StoreAddressResponse response = TEMPLATE.postForObject(adminCourierUrl("/storeAddresses"), request, StoreAddressResponse.class);
        response.getStoreAddressInfos();
        response.getLastPageNumber();
        response.getTotalItems();
    }



    @Test
    public void updateItemPrices() throws Exception {
        UpdateItemPriceRequest request = new UpdateItemPriceRequest();
        request.setSecurity_token("zzz");

        List<ItemPriceInfo> itemPriceInfos = new ArrayList<>();
        ItemPriceInfo itemPriceInfo = new ItemPriceInfo();
        itemPriceInfo.setId(3L);

        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setId(0L);
        itemInfo.setItemName("Банька");
        itemInfo.setItemType("SERVICE");
        itemInfo.setActive(true);


        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setId(0L);
        storeInfo.setActive(true);
        storeInfo.setStoreName("Магазин 1 777");

        StoreAddressInfo storeAddressInfo = new StoreAddressInfo();
        storeAddressInfo.setId(0L);
        storeAddressInfo.setLatitude(55.0044);
        storeAddressInfo.setLongitude(82.9034);
        storeAddressInfo.setAddress("Адрес магазина 55");
        storeAddressInfo.setStoreInfo(storeInfo);


        itemPriceInfo.setPrice(9000);
        itemPriceInfo.setTimeOfFinishPricing(DateTimeUtils.nowNovosib_GMT6().getMillis());
        itemPriceInfo.setActive(false);
        itemPriceInfo.setStoreAddressInfo(storeAddressInfo);
        itemPriceInfo.setItemInfo(itemInfo);

        itemPriceInfos.add(itemPriceInfo);

        request.setItemPriceInfoList(itemPriceInfos);
        String asString = OBJECT_MAPPER.writeValueAsString(request);
        UpdateItemPriceResponse response = TEMPLATE.postForObject(adminCourierUrl("/updateItemPrices"), request, UpdateItemPriceResponse.class);
        response.getItemPriceInfoList();
    }

}
