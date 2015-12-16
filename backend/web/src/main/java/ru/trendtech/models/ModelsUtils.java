package ru.trendtech.models;

import com.google.common.collect.Lists;
import org.joda.money.Money;
import org.joda.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.client.corporate.LimitInfo;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.corporate.*;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfo;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;
import ru.trendtech.common.mobileexchange.model.common.logging.LoggingEventInfoMongo;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfo;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.driver.DriverRequisiteInfoV2;
import ru.trendtech.common.mobileexchange.model.driver.DriverSettingInfo;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.*;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.billing.AutoClassPrice;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.*;
import ru.trendtech.domain.mongo.LoggingEventMongo;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MoneyUtils;
import ru.trendtech.utils.PhoneUtils;
import java.util.*;


public class ModelsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsUtils.class);


    public static ItemLocation toModel(Location location) {
        return toModel(0, location);
    }

    public static ItemLocation toModel(long driverId, Location location) {
        return toModel(driverId, location.getLatitude(), location.getLongitude());
    }

    public static ItemLocation toModel(long driverId, Location location, int angle) {
        return toModel(driverId, location.getLatitude(), location.getLongitude(), angle);
    }


    public static ItemLocation toModel(double latitude, double longitude) {
        return toModel(0, latitude, longitude, false);
    }

    public static ItemLocation toModel(long id, double latitude, double longitude) {
        return toModel(id, latitude, longitude, false);
    }

    public static ItemLocation toModel(long id, double latitude, double longitude, int angle) {
        return toModel(id, latitude, longitude, false, angle);
    }


    public static FantomInfo toModel(Driver driver, Location location) {
        FantomInfo info = new FantomInfo();
        if(driver.getState().equals(Driver.State.AVAILABLE)){
            info.setActive(true);
        } else if(driver.getState().equals(Driver.State.OFFLINE)){
            info.setActive(false);
        }
        info.setItemLocation(ModelsUtils.toModel(location));
        return info;
    }




    public static ItemPropertyInfo toModel(ItemProperty itemProperty){
        ItemPropertyInfo info = new ItemPropertyInfo();
        info.setAlcohol(itemProperty.isAlcohol());
        info.setNamePoperty(itemProperty.getNamePoperty());
        info.setId(itemProperty.getId());
        return info;
    }



    public static ItemProperty fromModel(ItemProperty itemProperty, ItemPropertyInfo info){
        if(itemProperty == null){
            itemProperty = new ItemProperty();
        }
        itemProperty.setAlcohol(info.isAlcohol());
        itemProperty.setNamePoperty(info.getNamePoperty());
            return itemProperty;
    }


    public static CourierClientCardInfo toModel(CourierClientCard card) {
        CourierClientCardInfo info = new CourierClientCardInfo();
        info.setId(card.getId());
        info.setActive(card.isActive());
        info.setCardNumber(card.getCardNumber());
        info.setDelete(card.isDelete());
        return info;
    }



    public static CommentInfo toMode(Comment comment){
        CommentInfo info = new CommentInfo();
        info.setComment(comment.getComment()!=null ? comment.getComment() : "");
        return info;
    }



    public static ActivationQueueInfo toModel(ActivationQueue activationQueue){
        ActivationQueueInfo info = new ActivationQueueInfo();
        info.setId(activationQueue.getId());
        info.setClientInfoARM(ModelsUtils.toModelClientInfoARM(activationQueue.getClient(), null));
        info.setTimeOfActivation(activationQueue.getTimeOfActivation() != null ? activationQueue.getTimeOfActivation().getMillis() : 0);
        info.setTimeOfRequest(activationQueue.getTimeOfRequest() !=null ? activationQueue.getTimeOfRequest().getMillis() : 0);
        return info;
    }


    public static Location fromModel(String address, double latitude, double longitude, String region, String city) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        location.setCity(city);
        location.setRegion(region);
        return location;
    }



    public static DefaultPriceInfo toModel(DefaultPrice defaultPrice){
        DefaultPriceInfo info = new DefaultPriceInfo();
        info.setId(defaultPrice.getId());
        info.setActive(defaultPrice.isActive());
        info.setMinimalPrice(defaultPrice.getMinimalPrice().getAmountMinorInt());
        info.setOrderProcessingPrice(defaultPrice.getOrderProcessingPrice().getAmountMinorInt());
        info.setOrderType(defaultPrice.getOrderType().getValue());
        info.setPerKmPrice(defaultPrice.getPerKmPrice().getAmountMinorInt());
        info.setKmIncluded(defaultPrice.getKmIncluded());
        info.setFineAmount(defaultPrice.getFineAmount().getAmountMinorInt());
        return info;
    }




    public static CustomWindowInfo toModel(CustomWindow customWindow){
        CustomWindowInfo info = new CustomWindowInfo();
        info.setId(customWindow.getId());
        info.setButtonText(customWindow.getButtonText());
        info.setContentText(customWindow.getContentText());
        info.setImgUrl(customWindow.getImgUrl());
        info.setTitle(customWindow.getTitle());
        info.setTypeWindow(TypeWindowInfo.valueOf(customWindow.getTypeWindow().name()));
        return info;
    }



    public static OrderItemPrice fromModel(OrderItemPriceInfo info, ItemPrice itemPrice, Order order, OrderItemPrice orderItemPrice, int orderNumber){
        if(orderItemPrice == null){
            orderItemPrice = new OrderItemPrice();
        }
            orderItemPrice.setCountItem(info.getCountItem());
            orderItemPrice.setItemPrice(itemPrice);
            orderItemPrice.setOrder(order);
            orderItemPrice.setPriceOnDayOfOrder(itemPrice.getPrice());
            orderItemPrice.setOrderNumber(orderNumber);
            return orderItemPrice;
    }



    public static OrderAddress fromModel(OrderAddressInfo info, Order order, OrderAddress orderAddress, int orderNumber){
        if(info != null ){
            if(orderAddress == null){
                orderAddress = new OrderAddress();
            }
            orderAddress.setContactPerson(info.getContactPerson());
            orderAddress.setContactPersonPhone(info.getContactPersonPhone());
            orderAddress.setComment(info.getComment());
            orderAddress.setAddressLocation(ModelsUtils.fromModel(info.getAddress(), info.getLatitude(), info.getLongitude(), info.getRegion(), info.getCity()));
            orderAddress.setStreet(info.getStreet());
            orderAddress.setHouse(info.getHouse());
            orderAddress.setHousing(info.getHousing());
            orderAddress.setApartment(info.getApartment());
            orderAddress.setOrderNumber(orderNumber);
            orderAddress.setOrder(order);
            orderAddress.setIsToAddress(info.isToAddress());
            orderAddress.setTo(info.isTo());
            orderAddress.setTargetAddressState(TargetAddressState.UNKNOWN);
        }
        return orderAddress;
    }


    public static ClientItem fromModel(ClientItemInfo info, Order order, Item item, int orderNumber){
        ClientItem clientItem = new ClientItem();
        clientItem.setCountItem(info.getCountItem());
        clientItem.setItem(item);
        clientItem.setOrder(order);
        clientItem.setUndefinedItemName(info.getItemInfo().getItemName());
        clientItem.setOrderNumber(orderNumber);
        return clientItem;
    }



    public static ItemLocation toModel(long id, double latitude, double longitude, boolean occupied, int angle) {
        ItemLocation location = new ItemLocation();
        location.setDriverId(id);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setOccupied(occupied);
        location.setAngle(angle);
        return location;
    }


    public static ItemInfo toModel(Item item) {
        ItemInfo info = null;
        if(item != null){
            info = new ItemInfo();
            info.setId(item.getId());
            info.setActive(item.isActive());
            info.setItemType(item.getItemType().name());
            info.setItemName(item.getItemName());
            info.setDefaultItemPrice(item.getDefaultItemPrice()!=null ? item.getDefaultItemPrice().getAmountMinorInt(): 0);
        }
        return info;
    }



    public static ItemPriceInfo toModel(ItemPrice itemPrice) {
        ItemPriceInfo info = new ItemPriceInfo();
        info.setId(itemPrice.getId());
        info.setItemInfo(ModelsUtils.toModel(itemPrice.getItem()));
        info.setStoreAddressInfo(ModelsUtils.toModel(itemPrice.getStoreAddress()));
        info.setTimeOfFinishPricing(itemPrice.getTimeOfFinishPricing().getMillis());
        info.setPrice(itemPrice.getPrice().getAmountMinorInt());
        info.setActive(itemPrice.isActive());
        return info;
    }



    public static ItemPrice fromModel(ItemPriceInfo info, ItemPrice itemPrice, StoreAddress storeAddress, Item item) {
        if(itemPrice == null){
            itemPrice = new ItemPrice();
        }
        itemPrice.setItem(item);
        itemPrice.setStoreAddress(storeAddress);
        //Money.ofMinor(MoneyUtils.DEFAULT_CURRENCY, info.getPrice());
        itemPrice.setPrice(MoneyUtils.getMoney(info.getPrice()/100));
        itemPrice.setTimeOfFinishPricing(DateTimeUtils.toDateTime(info.getTimeOfFinishPricing()));
        itemPrice.setActive(info.isActive());
        return itemPrice;
    }



    public static StoreAddressInfo toModel(StoreAddress storeAddress) {
        StoreAddressInfo info = new StoreAddressInfo();
        if(storeAddress != null){
            info.setId(storeAddress.getId());
            info.setAddress(storeAddress.getStoreLocation().getAddress());
            info.setLatitude(storeAddress.getStoreLocation().getLatitude());
            info.setLongitude(storeAddress.getStoreLocation().getLongitude());
            info.setStoreInfo(ModelsUtils.toModel(storeAddress.getStore()));
            info.setCity(storeAddress.getStoreLocation().getCity());
            info.setRegion(storeAddress.getStoreLocation().getRegion());
            info.setDistance(storeAddress.getDistance());
        }
        return info;
    }




    public static StoreAddress fromModel(StoreAddressInfo info, Store store, StoreAddress storeAddress) {
        if(storeAddress == null){
            storeAddress = new StoreAddress();
        }
        storeAddress.setStoreLocation(ModelsUtils.fromModel(info.getAddress(), info.getLatitude(), info.getLongitude(), info.getRegion(), info.getCity()));
        storeAddress.setStore(store);
        return storeAddress;
    }




    public static StoreInfo toModel(Store store) {
        StoreInfo info = new StoreInfo();
        if(store != null){
            info.setId(store.getId());
            info.setStoreName(store.getStoreName());
            info.setActive(store.isActive());
        }
        return info;
    }




    public static List<ItemPriceInfo> toModelItemPriceInfoList(List<ItemPrice> itemPriceList) {
        List<ItemPriceInfo> itemPriceInfos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(itemPriceList)){
            for(ItemPrice itemPrice: itemPriceList){
                ItemPriceInfo info = new ItemPriceInfo();
                info.setId(itemPrice.getId());
                info.setTimeOfFinishPricing(itemPrice.getTimeOfFinishPricing().getMillis());
                info.setPrice(itemPrice.getPrice().getAmountMinorInt());
                info.setStoreAddressInfo(ModelsUtils.toModel(itemPrice.getStoreAddress()));
                info.setItemInfo(ModelsUtils.toModel(itemPrice.getItem()));
                info.setActive(itemPrice.isActive());
                itemPriceInfos.add(info);
            }
        }
        return itemPriceInfos;
    }




    public static List<StoreAddressInfo> toModelStoreAddressInfoList(List<StoreAddress> storeAddresses) {
        List<StoreAddressInfo> storeAddressInfos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(storeAddresses)){
            for(StoreAddress storeAddress: storeAddresses){
                StoreAddressInfo info = new StoreAddressInfo();
                info.setStoreInfo(ModelsUtils.toModel(storeAddress.getStore()));
                info.setId(storeAddress.getId());
                info.setLatitude(storeAddress.getStoreLocation().getLatitude());
                info.setLongitude(storeAddress.getStoreLocation().getLongitude());
                info.setAddress(storeAddress.getStoreLocation().getAddress());
                storeAddressInfos.add(info);
            }
        }
        return storeAddressInfos;
    }




    public static List<ItemInfo> toModelItemInfoList(List<Item> items) {
        List<ItemInfo> itemInfos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(items)){
            for(Item item: items){
                ItemInfo info = new ItemInfo();
                info.setId(item.getId());
                info.setActive(item.isActive());
                info.setItemType(item.getItemType().name());
                info.setItemName(item.getItemName());
                info.setDefaultItemPrice(item.getDefaultItemPrice().getAmountMinorInt());
                itemInfos.add(info);
            }
        }
        return itemInfos;
    }



    public static Item fromModel(ItemInfo info, Item item) {
        if(item == null){
           item = new Item();
        }
            item.setActive(info.isActive());
            item.setItemName(info.getItemName());
            item.setItemType(Item.ItemType.valueOf(info.getItemType()));
            item.setDefaultItemPrice(MoneyUtils.getMoney(info.getDefaultItemPrice() / 100));
            return item;
    }




    public static Store fromModel(StoreInfo info, Store store) {
        if(store == null){
            store = new Store();
        }
        store.setActive(info.isActive());
        store.setStoreName(info.getStoreName());
        return store;
    }



    public static List<StoreInfo> toModelStoreInfoList(List<Store> stores) {
        List<StoreInfo> storeInfos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stores)){
            for(Store store: stores){
                StoreInfo info = new StoreInfo();
                info.setId(store.getId());
                info.setActive(store.isActive());
                info.setStoreName(store.getStoreName());
                storeInfos.add(info);
            }
        }
        return storeInfos;
    }



    public static OrderAddressInfo toModel(OrderAddress orderAddress){
        OrderAddressInfo info = new OrderAddressInfo();
        info.setLatitude(orderAddress.getAddressLocation().getLatitude());
        info.setLongitude(orderAddress.getAddressLocation().getLongitude());
        info.setStreet(orderAddress.getStreet());
        info.setRegion(orderAddress.getAddressLocation().getRegion());
        info.setCity(orderAddress.getAddressLocation().getCity());
        info.setHouse(orderAddress.getHouse());
        info.setHousing(orderAddress.getHousing());
        info.setApartment(orderAddress.getApartment());
        info.setContactPerson(orderAddress.getContactPerson());
        info.setComment(orderAddress.getComment());
        info.setContactPersonPhone(orderAddress.getContactPersonPhone());
        info.setAddress(orderAddress.getAddressLocation().getAddress());
        info.setId(orderAddress.getId());
        info.setIsToAddress(orderAddress.isToAddress());
        info.setTo(orderAddress.isTo());
        info.setTargetAddressState(orderAddress.getTargetAddressState() != null ? orderAddress.getTargetAddressState().getValue() : 0);
        return info;
    }




    public static OrderInfo toModel(Order order,  int generalTimeLateCourier){
       OrderInfo info = new OrderInfo();
       if(order == null){
           return info;
       }
       info.setId(order.getId());
       info.setExpectedArrivalTime(order.getExpectedArrivalTime());
       info.setCourierLateMinutes(generalTimeLateCourier);
       info.setOrderType(order.getOrderType().getValue());
       info.setPaymentType(order.getPaymentType() != null ? order.getPaymentType().getValue() : 1);
       info.setClientInfo(ModelsUtils.toModel(order.getClient()));
       info.setDriverInfo(order.getDriver() != null ? ModelsUtils.toModel(order.getDriver()) : null);
       info.setPriceExpected(order.getPriceExpected().getAmountMinorInt());
       info.setPriceInFact(order.getPriceInFact().getAmountMinorInt());
       info.setPriceDelivery(order.getPriceDelivery().getAmountMinorInt());
       info.setDistanceExpected(order.getDistanceExpected());
       info.setPriceItems(order.getPriceOfItems() != null ? order.getPriceOfItems().getAmountMinorInt() : 0);
       info.setPriceItemsExpected(order.getPriceOfItemsExpected() != null ? order.getPriceOfItemsExpected().getAmountMinorInt() : 0);
       //info.setPriceItemsInFact(order.getPriceOfItemsInFact() != null ? order.getPriceOfItemsInFact().getAmountMinorInt() : 0);
       info.setDistanceExpectedToStore(order.getDistanceExpectedToStore());
       info.setDistanceInFact(order.getDistanceInFact());
       info.setDistanceInFactToStore(order.getDistanceInFactToStore());
       info.setState(order.getState().name());
       info.setTimeOfFinishing(order.getTimeOfFinishing().toString());
       info.setTimeOfFinishingExpected(order.getTimeOfFinishingExpected() != null ? order.getTimeOfFinishingExpected().getMillis() : 0);
       info.setTimeOfFinishingInFact(order.getTimeOfFinishingInFact() != null ? order.getTimeOfFinishingInFact().getMillis() : 0);
       info.setNameOfGroup(order.getTimeOfFinishing().getDayOfMonth() + "." + order.getTimeOfFinishing().getMonthOfYear());
       info.setTimeOfFinishingInFact(order.getTimeOfFinishingInFact() != null ? order.getTimeOfFinishingInFact().getMillis() : 0);
       info.setTimeOfReadyToGo(order.getTimeOfReadyToGo() != null ? order.getTimeOfReadyToGo().getMillis() : 0);
       info.setTimeOfReadyToProgress(order.getTimeOfReadyToProgress() != null ? order.getTimeOfReadyToProgress().getMillis() : 0);
       info.setTimeOfRequesting(order.getTimeOfRequesting() != null ? order.getTimeOfRequesting().getMillis() : 0);
       info.setTimeOfAssigning(order.getTimeOfAssigning() != null ? order.getTimeOfAssigning().getMillis() : 0);
       info.setIncreasePercent(order.getIncreasePercent());
       info.setPriceOfInsurance(order.getPriceOfInsurance() != null ? order.getPriceOfInsurance().getAmountMinorInt(): 0);
       info.setPercentInsuranceOnDayOfOrder(order.getPercentInsuranceOnDayOfOrder());
       info.setIsBooked(order.isBooked());
       //info.setOrderItemPriceInfos(ModelsUtils.toModelOrderItemPriceInfo(order.getOrderItemPrices()));

       if(!CollectionUtils.isEmpty(order.getOrderItemPrices())){
           for(OrderItemPrice orderItemPrice :order.getOrderItemPrices()){
               info.getOrderItemPriceInfos().add(ModelsUtils.toModel(orderItemPrice));
           }
       }
       if(!CollectionUtils.isEmpty(order.getTargetAddresses())){
           for(OrderAddress orderAddress : order.getTargetAddresses()){
              info.getTargetAddressesInfo().add(ModelsUtils.toModel(orderAddress));
           }
       }
       if(!CollectionUtils.isEmpty(order.getClientItems())){
           for(ClientItem clientItem : order.getClientItems()){
               info.getClientItemInfos().add(ModelsUtils.toModel(clientItem));
           }
       }
       return info;
    }


    public static ClientItemInfo toModel(ClientItem clientItem) {
        ClientItemInfo info = new ClientItemInfo();
        if (clientItem != null) {
            info.setId(clientItem.getId());
            info.setItemInfo(ModelsUtils.toModel(clientItem.getItem()));
            info.setCountItem(clientItem.getCountItem());
            info.setUndefinedItemName(clientItem.getUndefinedItemName());
        }
            return info;
    }


    public static OrderItemPriceInfo toModel(OrderItemPrice orderItemPrice) {
        OrderItemPriceInfo info = new OrderItemPriceInfo();
        info.setId(orderItemPrice.getId());
        info.setItemPriceInfo(ModelsUtils.toModel(orderItemPrice.getItemPrice()));
        info.setCountItem(orderItemPrice.getCountItem());
        info.setPriceOnDayOfOrder(orderItemPrice.getPriceOnDayOfOrder().getAmountMinorInt());
        return info;
    }




    public static List<OrderItemPriceInfo> toModelOrderItemPriceInfo(Set<OrderItemPrice> orderItemPrices){
        List<OrderItemPriceInfo> orderItemPriceInfos = new ArrayList<>();
        for(OrderItemPrice orderItemPrice: orderItemPrices){
            orderItemPriceInfos.add(ModelsUtils.toModel(orderItemPrice));
        }
        return orderItemPriceInfos;
    }



    public static ImageSourceInfo toModel(ImageSource imageSource){
        ImageSourceInfo info = new ImageSourceInfo();
        info.setUrlImage(imageSource.getUrl());
        info.setDimension(imageSource.getDimension());
        info.setToPost(imageSource.isToPost());
        return info;
    }




    public static ItemLocation toModel(long id, double latitude, double longitude, boolean occupied) {
        ItemLocation location = new ItemLocation();
            location.setDriverId(id);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setOccupied(occupied);
        return location;
    }


    public static MissionCanceledInfo toModel(MissionCanceled missionCanceled){
        MissionCanceledInfo info = new MissionCanceledInfo();
        info.setCancelBy(missionCanceled.getCancelBy());
        info.setCancelById(missionCanceled.getCancelById());
        info.setId(missionCanceled.getId());
        info.setMissionId(missionCanceled.getMissionId());
        info.setComment(missionCanceled.getReason());
        info.setStateBeforeCanceled(missionCanceled.getStateBeforeCanceled());
        info.setTimeOfCanceled(missionCanceled.getTimeOfCanceled().getMillis());
        info.setReasonInfo(missionCanceled.getReasonInfo() != null ? ModelsUtils.toModel(missionCanceled.getReasonInfo()) : null);
        return info;
    }



    public static ReasonInfo toModel(Reason reason){
        ReasonInfo info = new ReasonInfo();
        info.setReasonId(reason.getId());
        info.setReason(reason.getReason());
        info.setFine(reason.getFine());
        info.setClientBonus(reason.getClientBonus());
        info.setToDriver(reason.isToDriver());
        return info;
    }


    public static Reason fromModel(ReasonInfo info, Reason reason){
        if(reason == null){
            reason = new Reason();
        }
        if(info.getReason()!=null){
            reason.setReason(info.getReason());
        }
        reason.setFine(info.getFine());
        reason.setClientBonus(info.getClientBonus());
        reason.setToDriver(info.isToDriver());
            return reason;
    }


    public static Tablet fromModel(TabletInfo info, Tablet tablet){
        if(tablet == null){
            tablet = new Tablet();
        }
        if(info.getImeiNumber()!=null){
            tablet.setImeiNumber(info.getImeiNumber());
        }
        if(info.getModel()!=null){
            tablet.setModel(info.getModel());
        }
        if(info.isOwn()!=null){
            tablet.setOwn(info.isOwn());
        }
        if(info.getTabletState()!=null){
            tablet.setTabletState(TabletState.getByValue(info.getTabletState()));
        }
        if(info.getPhone()!=null){
            tablet.setPhone(info.getPhone());
        }
            tablet.setTimeOfUpdate(DateTimeUtils.nowNovosib_GMT6());
        return tablet;
    }



    public static AdditionalService fromModel(AdditionalServiceInfo info, AdditionalService service){
        if(service == null){
            service = new AdditionalService();
        }
        if(info.getNameService()!=null){
            service.setNameService(info.getNameService());
        }
        return service;
    }




    public static AdditionalServiceInfo toModel(AdditionalService service){
        AdditionalServiceInfo info  =new AdditionalServiceInfo();
        info.setId(service.getId());
        info.setNameService(service.getNameService());
        return info;
    }


    public static EstimateInfoARM toModel(Estimate estimate, Mission mission){
        EstimateInfoARM info = new EstimateInfoARM();
        info.setMissionInfoARM(ModelsUtils.toModel_MissionInfoARM(mission));
        info.setApplicationConvenience(estimate.getApplicationConvenience());
        info.setCleanlinessInCar(estimate.getCleanlinessInCar());
        info.setDriverCourtesy(estimate.getDriverCourtesy());
        info.setEstimateDate(DateTimeUtils.toDate(estimate.getEstimateDate()));
        info.setWifiQuality(estimate.getWifiQuality());
        info.setGeneral(estimate.getGeneral());
        info.setEstimateComment(estimate.getEstimateComment());
        info.setVisible(estimate.isVisible());
        info.setId(estimate.getId());
        info.setDriverInfoARM(ModelsUtils.toModelARM(estimate.getDriver()));
        info.setClientInfoARM(ModelsUtils.toModelClientInfoARM(estimate.getClient(), null));
           return info;
    }



    public static TabletInfo toModel(Tablet tablet, Driver driver){
        TabletInfo info = new TabletInfo();
        info.setId(tablet.getId());
        info.setImeiNumber(tablet.getImeiNumber());
        info.setModel(tablet.getModel());
        info.setPhone(tablet.getPhone());
        info.setOwn(tablet.isOwn());
        info.setTimeOfUpdate(tablet.getTimeOfUpdate() != null ? tablet.getTimeOfUpdate().getMillis() : 0);
        info.setTabletState(tablet.getTabletState().getValue());
        if(driver!=null){
            info.setDriverInfoARM(ModelsUtils.toModelARM(driver));
        }
        return info;
    }




    public static Router fromModel(RouterInfo info, Router router){
        if(router == null){
            router = new Router();
        }
        if(info.getModel()!=null){
            router.setModel(info.getModel());
        }
        if(info.getMacAddress()!=null){
            router.setMacAddress(info.getMacAddress());
        }
        if(info.getSimNumber()!=null){
            router.setSimNumber(info.getSimNumber());
        }
        if(info.getIp()!=null){
            router.setIp(info.getIp());
        }
        if(info.getTimeOfPurchase()!=0){
            router.setTimeOfPurchase(DateTimeUtils.toDateTime(info.getTimeOfPurchase()));
        }
        return router;
    }



    public static RouterInfo toModel(Router router, Driver driver){
        RouterInfo info = new RouterInfo();
        info.setId(router.getId());
        info.setIp(router.getIp());
        info.setMacAddress(router.getMacAddress());
        info.setModel(router.getModel());
        info.setSimNumber(router.getSimNumber());
        if(router.getTimeOfPurchase()!=null){
            info.setTimeOfPurchase(router.getTimeOfPurchase().getMillis());
        }
        if(driver!=null){
            info.setDriverInfoARM(ModelsUtils.toModelARM(driver));
        }
        return info;
    }



    public static DriverCarPhotosInfo toModel(DriverCarPhotos driverCarPhotos){
        DriverCarPhotosInfo info = new DriverCarPhotosInfo();
        info.setId(driverCarPhotos.getId());
        info.setPhotoUrl(driverCarPhotos.getPhotoUrl());
//        info.setAndroidMinVersion(ModelsUtils.toModel(driverCarPhotos.getAndroidMinVersion()));
//        info.setAppleMinVersion(ModelsUtils.toModel(driverCarPhotos.getAppleMinVersion()));
        return info;
    }




    public static AutoClassCostExampleInfo toModel(AutoClassCostExample autoClassCostExample){
         AutoClassCostExampleInfo info = new AutoClassCostExampleInfo();
         info.setAddressFrom(autoClassCostExample.getAddressFrom());
         info.setAddressTo(autoClassCostExample.getAddressTo());
         info.setCost(autoClassCostExample.getCost());
         return info;
    }


    public static DriverCarPhotos fromModel(DriverCarPhotosInfo info, Driver driver){
        DriverCarPhotos driverCarPhotos = new DriverCarPhotos();
        driverCarPhotos.setPhotoUrl(info.getPhotoUrl());
        driverCarPhotos.setDriver(driver);
        return driverCarPhotos;
    }


//    public static ReasonInfo toModel(Reason reason) {
//        ReasonInfo info = new ReasonInfo();
//        info.setReasonId(reason.getId());
//        info.setReason(reason.getReason());
//        return info;
//    }



    public static TariffRestrictionInfo toModel(TariffRestriction tariffRestriction) {
        TariffRestrictionInfo info = new TariffRestrictionInfo();
        info.setId(tariffRestriction.getId());
        info.setStartHours(tariffRestriction.getStartHours());
        info.setStartMinutes(tariffRestriction.getStartMinutes());
        info.setEndHours(tariffRestriction.getEndHours());
        info.setEndMinutes(tariffRestriction.getEndMinutes());
        info.setHoliday(tariffRestriction.isHoliday());
        return info;
    }




    public static DriverCarPhotos fromModel(String url, Driver driver){ // VersionsApp android, VersionsApp apple,
        DriverCarPhotos driverCarPhotos = new DriverCarPhotos();
        driverCarPhotos.setPhotoUrl(url);
//        driverCarPhotos.setAndroidMinVersion(android);
//        driverCarPhotos.setAppleMinVersion(apple);
        driverCarPhotos.setDriver(driver);
        return driverCarPhotos;
    }




    public static DriverCarPhotos fromModelUpdate(String url, VersionsApp android, VersionsApp apple, DriverCarPhotos driverCarPhotos){
        driverCarPhotos.setPhotoUrl(url);
//        if(android!=null){
//            driverCarPhotos.setAndroidMinVersion(android);
//        }
//        if(apple!=null){
//            driverCarPhotos.setAppleMinVersion(apple);
//        }
        return driverCarPhotos;
    }




    public static TariffRestriction fromModel(TariffRestrictionInfo info, TariffRestriction tariffRestriction) {
        if(tariffRestriction == null){
            tariffRestriction = new TariffRestriction();
        }
        tariffRestriction.setStartHours(info.getStartHours());
        tariffRestriction.setStartMinutes(info.getStartMinutes());
        tariffRestriction.setEndHours(info.getEndHours());
        tariffRestriction.setEndMinutes(info.getEndMinutes());
        tariffRestriction.setTariffName(AutoClass.LOW_COSTER.name());
        tariffRestriction.setActive(true);
        tariffRestriction.setHoliday(info.isHoliday());
        return tariffRestriction;
    }



    public static LocationInfo toModel(ru.trendtech.domain.mongo.Location location) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setDriverId(location.getDriverId());
        locationInfo.setMissionId(location.getMissionId());
        locationInfo.setId(location.getId());
        locationInfo.setLatitude(location.getLatitude());
        locationInfo.setLongitude(location.getLongitude());
        locationInfo.setType(location.getType());
        locationInfo.setWhen_seen(location.getWhen_seen());
        return locationInfo;
    }



    public static PeriodWorkInfo toModel(PeriodWork periodWork){
         PeriodWorkInfo info = new PeriodWorkInfo();
         info.setId(periodWork.getId());
         info.setStartPeriod(DateTimeUtils.toDate(periodWork.getStartPeriod()));
         info.setEndPeriod(DateTimeUtils.toDate(periodWork.getEndPeriod()));
           return info;
    }


    public static RegionInfo toModel(Region region, boolean showCoordinates){
        RegionInfo info = new RegionInfo();
        if(region==null){
            return info;
        }
        info.setNameRegion(region.getNameRegion());
        info.setId(region.getId());
        info.setCoast(region.getCoast());
        info.setMarkup(region.getMarkup());
        info.setActive(region.isActive());
        info.setTypeRegion(region.getTypeRegion());
        info.setToMarkup(region.getToMarkup());
        info.setFromMarkup(region.getFromMarkup());
        info.setRadius(region.getRadius());
        info.setBase64Coord(showCoordinates ? region.getRegionCoordinates().toString() : "");
        return info;
    }

    public static MissionStateStatisticInfo toModelMissionStateStatisticInfo(MissionStateStatistic missionStateStat) {
        MissionStateStatisticInfo info = new MissionStateStatisticInfo();
        info.setId(missionStateStat.getId());
        info.setState(missionStateStat.getState());
        info.setDateTime(missionStateStat.getDateTime().getMillis());
        info.setMissionInfoARM(toModel_MissionInfoARM(missionStateStat.getMission()));
        return info;
    }



    public static DriverCorrectionInfo toModelDriverCorrectionInfo(DriverCorrections driverCorrection){
        DriverCorrectionInfo info = new DriverCorrectionInfo();
        info.setId(driverCorrection.getId());
        if(driverCorrection.getArticleAdjustments()!=null){
            info.setArticle(driverCorrection.getArticleAdjustments().getName());
        }
        if(driverCorrection.getDriverCashFlow().getDate_operation()!=null){
            info.setDateCorrection(driverCorrection.getDriverCashFlow().getDate_operation().getMillis());
        }
        info.setBalanceBefore(driverCorrection.getBalanceBefore());
        info.setBalanceAfter(driverCorrection.getBalanceAfter());
        info.setComments(driverCorrection.getComments());
        info.setDriverCashFlowId(driverCorrection.getDriverCashFlow().getId());
        info.setWebUserId(driverCorrection.getWebUser().getId());
        info.setDriverId(driverCorrection.getDriverCashFlow().getDriver().getId());
        info.setFirstName(driverCorrection.getDriverCashFlow().getDriver().getFirstName());
        info.setLastName(driverCorrection.getDriverCashFlow().getDriver().getLastName());
        info.setFirstNameWeb(driverCorrection.getWebUser().getFirstName());
        info.setLastNameWeb(driverCorrection.getWebUser().getLastName());
        info.setCorrectionAmount(driverCorrection.getDriverCashFlow().getSum());
        return info;
    }


    public static CorporateClientCashFlowInfo toModelCorporateClientCashFlow(CorporateClientCashFlow cashFlow) {
        CorporateClientCashFlowInfo info = new CorporateClientCashFlowInfo();
        info.setId(cashFlow.getId());
        if(cashFlow.getMission()!=null){
            info.setMissionInfo(toModel_MissionInfoARM(cashFlow.getMission()));
        }
        if(cashFlow.getDateOperation()!=null){
            info.setDateOperation(cashFlow.getDateOperation().getMillis());
        }
        info.setOperation(cashFlow.getOperation());
        info.setArticleInfo(ModelsUtils.toModel(cashFlow.getArticle()));
        info.setClient(cashFlow.getClient().getId());
        info.setMainClient(cashFlow.getMainClient().getId());
        info.setSum(cashFlow.getSum() / 100);
        return info;
    }



    public static TaxoparkCashFlowInfo toModel(TaxoparkCashFlow cashFlow) {
        TaxoparkCashFlowInfo info = new TaxoparkCashFlowInfo();
        info.setId(cashFlow.getId());
        info.setMissionInfo(cashFlow.getMission() != null ? toModel_MissionInfoARM(cashFlow.getMission()) : null);
        info.setDateOperation(cashFlow.getDateOperation() != null ? cashFlow.getDateOperation().getMillis() : 0);
        info.setOperation(cashFlow.getOperation());
        info.setTaxoparkPartnersInfo(ModelsUtils.toModel(cashFlow.getTaxopark()));
        info.setSum(cashFlow.getSum() / 100);
        info.setArticleInfo(ModelsUtils.toModel(cashFlow.getArticle()));
        info.setComment(cashFlow.getComment());
        return info;
    }



    public static ArticleAdjustmentsInfo toModel(ArticleAdjustments article){
        ArticleAdjustmentsInfo info = new ArticleAdjustmentsInfo();
        if(article!=null){
            info.setId(article.getId());
            info.setName(article.getName());
        }
        return info;
    }


    public static DriverCashFlowInfo toModelDriverCashFlowInfo(DriverCashFlow driverCashFlow) {
        DriverCashFlowInfo info = new DriverCashFlowInfo();
        info.setId(driverCashFlow.getId());
        info.setDriverInfoARM(ModelsUtils.toModelARM_Short(driverCashFlow.getDriver()));
        if(driverCashFlow.getDriverPeriodWork()!=null){
            info.setDriverPeriodWorkId(driverCashFlow.getDriverPeriodWork().getId());
        }
        if(driverCashFlow.getMission()!=null){
            info.setMissionInfoARM(toModel_MissionInfoARM(driverCashFlow.getMission()));
        }
        if(driverCashFlow.getDate_operation()!=null){
            info.setDateOperation(driverCashFlow.getDate_operation().getMillis());
        }
        info.setOperation(driverCashFlow.getOperation());
        info.setSum(driverCashFlow.getSum());
        return info;
    }



    public static DriverStartWorkInfo toModel(DriverPeriodWork driverPeriodWork){
        DriverStartWorkInfo info = new DriverStartWorkInfo();
        info.setId(driverPeriodWork.getId());
        info.setActive(driverPeriodWork.isActive());
        info.setDriverId(driverPeriodWork.getDriver().getId());
        info.setStartWork(driverPeriodWork.getStartWork().getMillis());
        info.setEndWork(driverPeriodWork.getEndWork().getMillis());
        info.setTimeSecPayRest(driverPeriodWork.getTimeSecPayRest());
        info.setTimeSecWork(driverPeriodWork.getTimeSecWork());
        info.setTimeSecRest(driverPeriodWork.getTimeSecRest());

        if(driverPeriodWork.getUpdateTime()!=null) {
            info.setUpdateTime(driverPeriodWork.getUpdateTime().getMillis());
        }

        if(driverPeriodWork.getStartWork().isAfter(DateTimeUtils.nowNovosib_GMT6())){ // driverPeriodWork.getStartWork().isAfterNow()
            // время старта смены лежит в будущем
            info.setFuture(true);
            info.setMsg("Ваша будущая смена");
        }else{
            info.setFuture(false);
            info.setMsg("Ваша текущая смена");
        }
        if(driverPeriodWork.getTimeWorkInFactOfStarting()!=null){
            info.setTimeWorkInFactOfStarting(driverPeriodWork.getTimeWorkInFactOfStarting().getMillis());
        }
        if(driverPeriodWork.getTimeWorkInFactOfEnding()!=null){
            info.setTimeWorkInFactOfEnding(driverPeriodWork.getTimeWorkInFactOfEnding().getMillis());
        }
        return info;
    }



    public static DriverPeriodWorkInfo toModelDriverPeriodWorkInfo(DriverPeriodWork driverPeriodWork){
        DriverPeriodWorkInfo info = new DriverPeriodWorkInfo();
        info.setId(driverPeriodWork.getId());
        info.setActive(driverPeriodWork.isActive());
        info.setDriverId(driverPeriodWork.getDriver().getId());
        info.setStartWork(driverPeriodWork.getStartWork().getMillis());
        info.setEndWork(driverPeriodWork.getEndWork().getMillis());
        info.setTimeSecPayRest(driverPeriodWork.getTimeSecPayRest());
        info.setTimeSecWork(driverPeriodWork.getTimeSecWork());
        info.setTimeSecRest(driverPeriodWork.getTimeSecRest());
        info.setUpdateTime(driverPeriodWork.getUpdateTime() != null ? driverPeriodWork.getUpdateTime().getMillis() : 0);
        info.setRequisiteId(driverPeriodWork.getDriverRequisite() != null ? driverPeriodWork.getDriverRequisite().getId() : 0);

        if(driverPeriodWork.getStartWork().getMillis()>DateTimeUtils.nowNovosib_GMT6().getMillis()){
            // время старта смены лежит в будущем
            info.setFuture(true);
            info.setMsg("Ваша будущая смена");
        }else if(driverPeriodWork.getStartWork().getMillis()<DateTimeUtils.nowNovosib_GMT6().getMillis() && driverPeriodWork.getEndWork().getMillis()>DateTimeUtils.nowNovosib_GMT6().getMillis()){
            info.setFuture(false);
            info.setMsg("Ваша текущая смена");
        }else{
            info.setFuture(false);
            info.setMsg("Смена просрочена");
        }
        if(driverPeriodWork.getTimeWorkInFactOfStarting()!=null){
            info.setTimeWorkInFactOfStarting(driverPeriodWork.getTimeWorkInFactOfStarting().getMillis());
        }
        if(driverPeriodWork.getTimeWorkInFactOfEnding()!=null){
            info.setTimeWorkInFactOfEnding(driverPeriodWork.getTimeWorkInFactOfEnding().getMillis());
        }
        return info;
    }



    public static DriverSettingInfo toModel(DriverSetting driverSetting){
        DriverSettingInfo info = new DriverSettingInfo();
        info.setDriverId(driverSetting.getDriver().getId());
//        info.setToLat();
//        info.setToLon();
//        info.setToAddress();
//        info.set
        return info;
    }



    public static DriverSetting fromModelUpdate(DriverSettingInfo info, DriverSetting driverSetting, List<AdditionalService> additionalServices){
        driverSetting.setAeroport(info.isAeroport());
        driverSetting.setDefaultRadius(info.getDefaultRadius());
        if(info.getDriverAutoClass()!=null) {
                driverSetting.getDriverAutoClass().clear();
            for (Integer autoClass : info.getDriverAutoClass()) {
                driverSetting.getDriverAutoClass().add(AutoClass.getByValue(autoClass));
            }
        }
        if(info.getDriverServices()!=null){
            driverSetting.getDriverServices().clear();
            for(Integer services:info.getDriverServices()){
                driverSetting.getDriverServices().add(MissionService.getByValue(services));
            }
        }
        if(!CollectionUtils.isEmpty(additionalServices)){
            for(AdditionalService service: additionalServices){
                driverSetting.getAdditionalServices().add(service);
            }
        }
        driverSetting.setNextOffer(info.isNextOffer());
        driverSetting.setRobotActive(info.isRobotActive());
        driverSetting.setToAddress(info.getToAddress());
        driverSetting.setToLat(info.getToLat());
        driverSetting.setToLon(info.getToLon());
        driverSetting.setSumIncrease(info.getSumIncrease());
        driverSetting.setCardNumber(info.getCardNumber());
        driverSetting.setPedestrian(info.isPedestrian());
        driverSetting.setDepositeAmount(MoneyUtils.getMoney(info.getDepositeAmount()));
        if(info.getDriverTypeInfo() != null){
            driverSetting.setDriverType(!StringUtils.isEmpty(info.getDriverTypeInfo().name()) ? DriverSetting.DriverType.valueOf(info.getDriverTypeInfo().name()) : DriverSetting.DriverType.NONE);
        }
        return driverSetting;
    }



    public static DriverSetting fromModel(DriverSettingInfo info, Driver driver, List<AdditionalService> additionalServices){
        DriverSetting driverSetting = new DriverSetting();
        driverSetting.setAeroport(info.isAeroport());
        driverSetting.setDriver(driver);
        driverSetting.setDefaultRadius(info.getDefaultRadius());
        if(info.getDriverAutoClass()!=null) {
            for (Integer autoClass : info.getDriverAutoClass()) {
                driverSetting.getDriverAutoClass().add(AutoClass.getByValue(autoClass));
            }
        }
        if(info.getDriverServices()!=null){
            for(Integer services:info.getDriverServices()){
                driverSetting.getDriverServices().add(MissionService.getByValue(services));
            }
        }
        if(!CollectionUtils.isEmpty(additionalServices)){
            for(AdditionalService service: additionalServices){
                driverSetting.getAdditionalServices().add(service);
            }
        }
        driverSetting.setNextOffer(info.isNextOffer());
        driverSetting.setRobotActive(info.isRobotActive());
        driverSetting.setToAddress(info.getToAddress());
        driverSetting.setToLat(info.getToLat());
        driverSetting.setToLon(info.getToLon());
        driverSetting.setSumIncrease(info.getSumIncrease());
        driverSetting.setCourier(info.isCourier());
        driverSetting.setCardNumber(info.getCardNumber());
        driverSetting.setDepositeAmount(MoneyUtils.getMoney(info.getDepositeAmount()));
        driverSetting.setPedestrian(info.isPedestrian());
        if(info.getDriverTypeInfo() != null){
            driverSetting.setDriverType(!StringUtils.isEmpty(info.getDriverTypeInfo().name()) ? DriverSetting.DriverType.valueOf(info.getDriverTypeInfo().name()) : DriverSetting.DriverType.NONE);
        }
        return driverSetting;
    }





    public static DriverSettingInfo toModelDriverSettingInfo(DriverSetting setting){
        DriverSettingInfo info = new DriverSettingInfo();
        info.setAeroport(setting.isAeroport());
        info.setDriverId(setting.getDriver().getId());
        info.setDefaultRadius(setting.getDefaultRadius());
        info.setId(setting.getId());
        if(setting.getDriverAutoClass()!=null) {
            for (AutoClass autoClass : setting.getDriverAutoClass()) {
                info.getDriverAutoClass().add(autoClass.getValue());
            }
        }
        if(setting.getDriverServices()!=null){
            for(MissionService service : setting.getDriverServices()){
                info.getDriverServices().add(service.getValue());
            }
        }
        if(!CollectionUtils.isEmpty(setting.getAdditionalServices())) {
            for(AdditionalService service : setting.getAdditionalServices()){
                info.getAdditionalServices().add(ModelsUtils.toModel(service));
            }
        }
        info.setNextOffer(setting.isNextOffer());
        info.setRobotActive(setting.isRobotActive());
        info.setToAddress(setting.getToAddress());
        info.setToLat(setting.getToLat());
        info.setToLon(setting.getToLon());
        info.setSumIncrease(setting.getSumIncrease());
        info.setCourier(setting.isCourier());
        info.setCardNumber(setting.getCardNumber());
        info.setDepositeAmount(setting.getDepositeAmount() != null ? setting.getDepositeAmount().getAmount().intValue() : 0);
        info.setPedestrian(setting.isPedestrian());
        info.setDriverTypeInfo(setting.getDriverType() != null ? DriverTypeInfo.valueOf(setting.getDriverType().name()) : DriverTypeInfo.NONE);
          return info;
    }





    public static PeriodWork fromModelToPeriodWork(DriverPeriodWorkInfo info){
        PeriodWork periodWork = new PeriodWork();
        periodWork.setStartPeriod(DateTimeUtils.toDateTime(info.getStartWork() + 21600));
        periodWork.setEndPeriod(DateTimeUtils.toDateTime(info.getEndWork() + 21600));
        LOGGER.info("START period: " + DateTimeUtils.toDateTime(info.getStartWork() + 21600) + " END period: " + DateTimeUtils.toDateTime(info.getEndWork() + 21600));
        return periodWork;
    }




    public static DriverPeriodWork fromModel(DriverPeriodWorkInfo info){
        DriverPeriodWork dpw = new DriverPeriodWork();
        dpw.setActive(info.isActive());
        dpw.setStartWork(DateTimeUtils.toDateTime(info.getStartWork()));
        dpw.setEndWork(DateTimeUtils.toDateTime(info.getEndWork()));
        //dpw.setTimeSecPayRest(info.getTimeSecPayRest());
        //dpw.setTimeSecWork(info.getTimeSecWork());
        //dpw.setTimeSecRest(info.getTimeSecRest());
        //dpw.setTimeWorkInFactOfStarting(DateTimeUtils.toDateTime(info.getTimeWorkInFactOfStarting()));
        //dpw.setTimeWorkInFactOfEnding(DateTimeUtils.toDateTime(info.getTimeWorkInFactOfEnding()));
        return dpw;
    }


    public static DriverPeriodWork fromModelUpd(DriverPeriodWorkInfo info, DriverPeriodWork dpw){
        dpw.setActive(info.isActive());
        dpw.setStartWork(DateTimeUtils.toDateTime(info.getStartWork()));
        dpw.setEndWork(DateTimeUtils.toDateTime(info.getEndWork()));
        //dpw.setTimeSecPayRest(info.getTimeSecPayRest());
        //dpw.setTimeSecWork(info.getTimeSecWork());
        //dpw.setTimeSecRest(info.getTimeSecRest());
        //dpw.setTimeWorkInFactOfStarting(DateTimeUtils.toDateTime(info.getTimeWorkInFactOfStarting()));
        //dpw.setTimeWorkInFactOfEnding(DateTimeUtils.toDateTime(info.getTimeWorkInFactOfEnding()));
        return dpw;
    }




    public static TipPercentInfo toModelTipPercentInfo(TipPercent tipPercent, int sum){
        TipPercentInfo info = new TipPercentInfo();
        info.setId(tipPercent.getId());
        info.setPercent(tipPercent.getPercent());
        double resultSum = (sum + (sum*(tipPercent.getPercent()/100.0)));
        double z = resultSum % 1;
        if (z != 0) {
            z = 1 - z;
            resultSum += z;
        }
        info.setSumByPercent((int)resultSum*100);
        return info;
    }



    public static DriverRequisiteInfoV2 toModelDRVreqV2(DriverRequisite driverRequisite){
        DriverRequisiteInfoV2 info = new DriverRequisiteInfoV2();
        info.setId(driverRequisite.getId());
        info.setActive(driverRequisite.isActive());
        info.setCountMinutesOfRest(driverRequisite.getCountMinutesOfRest());
        info.setDismissalTime(driverRequisite.getDismissalTime().getMillis());
        info.setDriverId(driverRequisite.getDriver().getId());
        info.setEndHours(driverRequisite.getEndHours());
        info.setEndMinutes(driverRequisite.getEndMinutes());
        info.setSalaryPerDay(driverRequisite.getSalaryPerDay());
        info.setStaffer(driverRequisite.isStaffer());
        info.setStartHours(driverRequisite.getStartHours());
        info.setStartMinutes(driverRequisite.getStartMinutes());
        info.setTypeDismissal(driverRequisite.getTypeDismissal());
        info.setTypeSalary(driverRequisite.getTypeSalary());
        info.setSalaryPriority(driverRequisite.getSalaryPriority());
        if(driverRequisite.getUpdateTime()!=null)
            info.setUpdateTime(driverRequisite.getUpdateTime().getMillis());
        return info;
    }




    public static DriverRequisiteInfo toModel(DriverRequisite driverRequisite){
        DriverRequisiteInfo info = new DriverRequisiteInfo();
        info.setId(driverRequisite.getId());
        info.setActive(driverRequisite.isActive());
        info.setCountMinutesOfRest(driverRequisite.getCountMinutesOfRest());
        info.setDismissalTime(driverRequisite.getDismissalTime().getMillis());
        info.setDriverId(driverRequisite.getDriver().getId());
        info.setEndHours(driverRequisite.getEndHours());
        info.setEndMinutes(driverRequisite.getEndMinutes());
        info.setSalaryPerDay(driverRequisite.getSalaryPerDay());
        info.setStaffer(driverRequisite.isStaffer());
        info.setStartHours(driverRequisite.getStartHours());
        info.setStartMinutes(driverRequisite.getStartMinutes());
        info.setTypeDismissal(driverRequisite.getTypeDismissal());
        info.setTypeSalary(driverRequisite.getTypeSalary());

        if(driverRequisite.getUpdateTime()!=null)
        info.setUpdateTime(driverRequisite.getUpdateTime().getMillis());
        return info;
    }


    public static FantomStatInfo toModel(MissionFantomDriver missionFantomDriver){
        FantomStatInfo info = new FantomStatInfo();
        info.setDriver(ModelsUtils.toModelARM(missionFantomDriver.getDiver()));
        info.setFantomDriver(ModelsUtils.toModelARM(missionFantomDriver.getFantomDiver()));
        info.setClientInfoARM(ModelsUtils.toModelClientInfoARM(missionFantomDriver.getMission().getClientInfo(), null));
        info.setFromAddress(missionFantomDriver.getMission().getLocationFrom().getAddress());
        info.setGeneralTimeSearch(missionFantomDriver.getGeneralTimeSecSearch());
        info.setRegionInfo(ModelsUtils.toModel(missionFantomDriver.getMission().getRegion(), false));
        info.setMissionId(missionFantomDriver.getMission().getId());
        info.setTimeOfAssigned(missionFantomDriver.getTimeOfAssigning().getMillis());
        info.setSumMission(missionFantomDriver.getMission().getStatistics().getPriceInFact().getAmount().intValue());
        info.setSumIncrease(missionFantomDriver.getSumIncrease());
        return info;
    }

    public static DriverRequisiteInfoARM toModelDriverRequisiteInfoARM(DriverRequisite driverRequisite){
        DriverRequisiteInfoARM info = new DriverRequisiteInfoARM();
        info.setId(driverRequisite.getId());
        info.setActive(driverRequisite.isActive());
        info.setCountMinutesOfRest(driverRequisite.getCountMinutesOfRest());
        info.setDismissalTime(driverRequisite.getDismissalTime().getMillis());
        info.setDriverId(driverRequisite.getDriver().getId());
        info.setEndHours(driverRequisite.getEndHours());
        info.setEndMinutes(driverRequisite.getEndMinutes());
        info.setSalaryPerDay(driverRequisite.getSalaryPerDay());
        info.setStaffer(driverRequisite.isStaffer());
        info.setStartHours(driverRequisite.getStartHours());
        info.setStartMinutes(driverRequisite.getStartMinutes());
        info.setTypeDismissal(driverRequisite.getTypeDismissal());
        info.setTypeSalary(driverRequisite.getTypeSalary());
        info.setSalaryPriority(driverRequisite.getSalaryPriority());
        if(driverRequisite.getUpdateTime()!=null)
            info.setUpdateTime(driverRequisite.getUpdateTime().getMillis());
        return info;
    }



    public static BanPeriodRestDriverInfo toModel(BanPeriodRestDriver banPeriodRestDriver){
        BanPeriodRestDriverInfo info = new BanPeriodRestDriverInfo();
        info.setId(banPeriodRestDriver.getId());
        info.setActive(banPeriodRestDriver.isActive());
        info.setTimeOfStarting(banPeriodRestDriver.getTimeOfStarting().getMillis());
        info.setTimeOfEnding(banPeriodRestDriver.getTimeOfEnding().getMillis());
        return info;
    }


    public static EventPartner fromModel(EventPartnerSiteInfo info, PartnerAccount partnerAccount){
        EventPartner ep = new EventPartner();
        ep.setAddress(info.getAddress());
        ep.setPhotosEventsUrl(info.getPhotosEventsUrl());
        ep.setTimeOfEvent(DateTimeUtils.toDateTime(info.getTimeOfEvent()));
        ep.setAnnotation(info.getAnnotation());
        ep.setToCost(info.getToCost());
        ep.setCost(info.getFromCost());
        ep.setEventName(info.getEventName());
        ep.setPartner(partnerAccount);
        ep.setPublished(info.isPublished());
        return ep;
    }


    public static EventPartnerSiteInfo toModelEventPartnerSiteInfo(EventPartner eventPartner){
        EventPartnerSiteInfo info = new EventPartnerSiteInfo();
        info.setEventId(eventPartner.getId());
        info.setAddress(eventPartner.getAddress());
        info.setPhotosEventsUrl(eventPartner.getPhotosEventsUrl());
        info.setTimeOfEvent(eventPartner.getTimeOfEvent().getMillis());
        info.setAnnotation(eventPartner.getAnnotation());
        info.setToCost(eventPartner.getToCost());
        info.setFromCost(eventPartner.getFromCost());
        info.setEventName(eventPartner.getEventName());
        info.setPartnerId(eventPartner.getPartner().getId());
        info.setPublished(eventPartner.isPublished());
          // 1 - будущее, 2 - прошедшее
          if(eventPartner.getTimeOfEvent().isAfter(DateTimeUtils.nowNovosib_GMT6())){
              info.setTypeStateEvent(1);
          }else{
              info.setTypeStateEvent(2);
          }
        return info;
    }


    public static EventPartner fromModel(EventPartnerInfo info){
        EventPartner ep = new EventPartner();
        ep.setAddress(info.getAddress());
        ep.setPhone(info.getPhone());
        ep.setUrlEvent(info.getUrlEvent());
        ep.setTimeOfEvent(DateTimeUtils.toDateTime(info.getTimeOfEvent()));
        ep.setAnnotation(info.getAnnotation());
        ep.setFullAddress(info.getFullAddress());
        ep.setUrlSmallPic(info.getUrlSmallPic());
        ep.setCost(info.getCost());
        ep.setEventName(info.getEventName());
        return ep;
    }



    public static EventPartner fromModelUpd(EventPartnerSiteInfo info, EventPartner ep){
        ep.setAddress(info.getAddress());
        ep.setTimeOfEvent(DateTimeUtils.toDateTime(info.getTimeOfEvent()));
        ep.setAnnotation(info.getAnnotation());
        ep.setToCost(info.getToCost());
        ep.setCost(info.getFromCost());
        ep.setEventName(info.getEventName());
        ep.setPublished(info.isPublished());
        return ep;
    }




    public static EventPartner fromModelUpd(EventPartnerInfo info, EventPartner ep){
        ep.setAddress(info.getAddress());
        ep.setPhone(info.getPhone());
        ep.setUrlEvent(info.getUrlEvent());
        ep.setTimeOfEvent(DateTimeUtils.toDateTime(info.getTimeOfEvent()));
        ep.setAnnotation(info.getAnnotation());
        ep.setFullAddress(info.getFullAddress());
        ep.setUrlSmallPic(info.getUrlSmallPic());
        ep.setCost(info.getCost());
        ep.setEventName(info.getEventName());
        return ep;
    }


    public static EventPartnerInfo toModel(EventPartner eventPartner){
        EventPartnerInfo info = new EventPartnerInfo();
        info.setId(eventPartner.getId());
        info.setAddress(eventPartner.getAddress());
        info.setPhone(eventPartner.getPhone());
        info.setUrlEvent(eventPartner.getUrlEvent());
        info.setTimeOfEvent(eventPartner.getTimeOfEvent().getMillis());
        info.setAnnotation(eventPartner.getAnnotation());
        info.setFullAddress(eventPartner.getFullAddress());
        info.setUrlSmallPic(eventPartner.getUrlSmallPic());
        info.setCost(eventPartner.getCost());
        info.setEventName(eventPartner.getEventName());
        return info;
    }


    public static BanPeriodRestDriver fromModel(BanPeriodRestDriverInfo info){
        BanPeriodRestDriver ban = new BanPeriodRestDriver();
        ban.setActive(info.isActive());
        ban.setTimeOfStarting(DateTimeUtils.toDateTime(info.getTimeOfStarting()));
        ban.setTimeOfEnding(DateTimeUtils.toDateTime(info.getTimeOfEnding()));
        return ban;
    }


    public static BanPeriodRestDriver fromModelUpd(BanPeriodRestDriverInfo info, BanPeriodRestDriver ban){
        ban.setActive(info.isActive());
        ban.setTimeOfStarting(DateTimeUtils.toDateTime(info.getTimeOfStarting()));
        ban.setTimeOfEnding(DateTimeUtils.toDateTime(info.getTimeOfEnding()));
        return ban;
    }



    public static VersionsAppInfo toModel(VersionsApp versionApp){
        VersionsAppInfo info = new VersionsAppInfo();
        info.setClientType(versionApp.getClientType());
        info.setClientType(versionApp.getClientType());
        info.setShowTarif(versionApp.getShowTarif());
        info.setStatusVersion(versionApp.getStatusVersion());
        info.setVersion(versionApp.getVersion());
        info.setId(versionApp.getId());
        return info;
    }



    public static VersionsApp fromModel(VersionsAppInfo info, VersionsApp versionsApp){
        versionsApp.setClientType(info.getClientType());
        versionsApp.setClientType(info.getClientType());
        versionsApp.setShowTarif(info.getShowTarif());
        versionsApp.setStatusVersion(info.getStatusVersion());
        versionsApp.setVersion(info.getVersion());
        return versionsApp;
    }



    public static NewsVersionAppInfo toModel(NewsVersionApp newsVersionApp){
        NewsVersionAppInfo info = new NewsVersionAppInfo();
        info.setActive(newsVersionApp.isActive());
        info.setId(newsVersionApp.getId());
        info.setUrl(newsVersionApp.getUrl());
        info.setVersionsAppInfo(toModel(newsVersionApp.getVersionsApp()));
        return info;
    }


    public static NewsVersionApp fromModel(NewsVersionAppInfo info, NewsVersionApp newsVersionApp, VersionsApp versionsApp){
        newsVersionApp.setUrl(info.getUrl());
        newsVersionApp.setActive(info.isActive());
        newsVersionApp.setVersionsApp(versionsApp);
        return newsVersionApp;
    }



    public static PayTerminalLocation toModelPayTerminal(double latitude, double longitude) {
        PayTerminalLocation location = new PayTerminalLocation();
        location.setLa(latitude);
        location.setLo(longitude);
        return location;
    }




    public static AssistantInfo toModel(Assistant assistant){
        AssistantInfo assistantInfo = new AssistantInfo();
        assistantInfo.setId(assistant.getId());
        assistantInfo.setName(assistant.getName());
        assistantInfo.setTabletCount(assistant.getTabletCount());
          return assistantInfo;
    }


    public static AutoClassRateInfoV2 toModel(AutoClassPrice autoClassPrice, boolean isCorporateClient){
        AutoClassRateInfoV2 autoClassRateInfo = new AutoClassRateInfoV2();
        autoClassRateInfo.setFreeWaitMinutes(autoClassPrice.getFreeWaitMinutes());
        autoClassRateInfo.setIntercity(autoClassPrice.getIntercity());
        autoClassRateInfo.setKmIncluded(autoClassPrice.getKmIncluded());
        autoClassRateInfo.setPerHourAmount(autoClassPrice.getPerHourAmount());
        autoClassRateInfo.setPerMinuteWaitAmount(autoClassPrice.getPerMinuteWaitAmount());
        autoClassRateInfo.setPrice(autoClassPrice.getPrice().getAmount().intValue());
        autoClassRateInfo.setPriceHour(autoClassPrice.getPriceHour());
        if(isCorporateClient){
            autoClassRateInfo.setPriceKm(autoClassPrice.getPriceKmCorporate());
        }else{
            autoClassRateInfo.setPriceKm(autoClassPrice.getPriceKm());
        }
        autoClassRateInfo.setAutoClass(autoClassPrice.getAutoClass().getValue());
        autoClassRateInfo.setAutoClassStr(ModelsUtils.autoTypeStr(autoClassPrice.getAutoClass().getValue()));
           /*
           if(autoClassPrice.getAutoClass().getValue()==1){
               autoClassRateInfo.setAutoClassStr("STANDARD");
           }else if(autoClassPrice.getAutoClass().getValue()==2){
               autoClassRateInfo.setAutoClassStr("COMFORT");
           }else if(autoClassPrice.getAutoClass().getValue()==3){
               autoClassRateInfo.setAutoClassStr("BUSINESS");
           }else if(autoClassPrice.getAutoClass().getValue()==4){
               autoClassRateInfo.setAutoClassStr("LOW_COSTER");
           }
           */
        autoClassRateInfo.setActive(autoClassPrice.isActive());
        autoClassRateInfo.setAutoExample(autoClassPrice.getAutoExample());
        autoClassRateInfo.setDescription(autoClassPrice.getDescription());
        autoClassRateInfo.setActivePicUrl(autoClassPrice.getActivePicUrl());
        autoClassRateInfo.setNotActivePicUrl(autoClassPrice.getNotActivePicUrl());
        return autoClassRateInfo;
    }


    public static NewsInfo toModel(News news){
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setId(news.getId());
        newsInfo.setTitle(news.getTitle());
        newsInfo.setUrlNews(news.getUrlNews());
        newsInfo.setTextNews(news.getTextNews());
        newsInfo.setTimeOfStarting(DateTimeUtils.toDate(news.getTimeOfStarting()));
        newsInfo.setTimeOfFinishing(DateTimeUtils.toDate(news.getTimeOfFinishing()));
          return newsInfo;
    }


    public static DriverProfileInfo toModelDriverProfileInfo(Driver driver){
        DriverProfileInfo info = new DriverProfileInfo();
        info.setChildrens(driver.isChildrens());
        info.setDreem(driver.getDream());
        info.setFamilyStatus(driver.getFamilyStatus());
        info.setGrowth(driver.getGrowth());
        info.setHobby(driver.getHobby());
        info.setWeight(driver.getWeight());
          if(driver.getBirthDate()!=null){
              Period p = new Period(new DateTime(driver.getBirthDate().getYear(), 1, 1, 0, 0, 0, 0), DateTimeUtils.nowNovosib_GMT6(), PeriodType.years());
              info.setAge(p.getYears());
          }
        return info;
    }


    public static EstimateInfoClient toModel(Estimate estimate){
        EstimateInfoClient estimateInfo = new EstimateInfoClient();
        estimateInfo.setApplicationConvenience(estimate.getApplicationConvenience());
        estimateInfo.setCleanlinessInCar(estimate.getCleanlinessInCar());
        estimateInfo.setDriverCourtesy(estimate.getDriverCourtesy());
        estimateInfo.setEstimateComment(estimate.getEstimateComment());
        estimateInfo.setEstimateDate(DateTimeUtils.toDate(estimate.getEstimateDate())); //   estimate.getEstimateDate().toDate().getTime()/1000
        estimateInfo.setGeneral(estimate.getGeneral());
        estimateInfo.setWaitingTime(estimate.getWaitingTime());
        estimateInfo.setWifiQuality(estimate.getWifiQuality());
        estimateInfo.setId(estimate.getId());
        estimateInfo.setMissionInfo(ModelsUtils.toModel(estimate.getMission()));
        estimateInfo.setVisible(estimate.isVisible());
        return estimateInfo;
    }


    public static EstimateInfo toModelEstimateInfo(Estimate estimate){
        EstimateInfo estimateInfo = new EstimateInfo();
        estimateInfo.setApplicationConvenience(estimate.getApplicationConvenience());
        estimateInfo.setCleanlinessInCar(estimate.getCleanlinessInCar());
        estimateInfo.setDriverCourtesy(estimate.getDriverCourtesy());
        estimateInfo.setEstimateComment(estimate.getEstimateComment());
        estimateInfo.setEstimateDate(DateTimeUtils.toDate(estimate.getEstimateDate())); //   estimate.getEstimateDate().toDate().getTime()/1000
        estimateInfo.setGeneral(estimate.getGeneral());
        estimateInfo.setWaitingTime(estimate.getWaitingTime());
        estimateInfo.setWifiQuality(estimate.getWifiQuality());
        estimateInfo.setId(estimate.getId());
        estimateInfo.setClientId(estimate.getClient().getId());
        estimateInfo.setDriverId(estimate.getDriver().getId());
        if(estimate.getMission()!=null)
        estimateInfo.setMissionId(estimate.getMission().getId());
        estimateInfo.setVisible(estimate.isVisible());
        estimateInfo.setClientName(estimate.getClient().getFirstName());
        return estimateInfo;
    }


    public static EstimateInfoARM toModelARM(Estimate estimate){
        EstimateInfoARM estimateInfo = new EstimateInfoARM();
        estimateInfo.setApplicationConvenience(estimate.getApplicationConvenience());
        estimateInfo.setCleanlinessInCar(estimate.getCleanlinessInCar());
        estimateInfo.setDriverCourtesy(estimate.getDriverCourtesy());
        estimateInfo.setEstimateComment(estimate.getEstimateComment());
        estimateInfo.setEstimateDate(DateTimeUtils.toDate(estimate.getEstimateDate())); //   estimate.getEstimateDate().toDate().getTime()/1000
        estimateInfo.setGeneral(estimate.getGeneral());
        estimateInfo.setWaitingTime(estimate.getWaitingTime());
        estimateInfo.setWifiQuality(estimate.getWifiQuality());
        estimateInfo.setId(estimate.getId());
        estimateInfo.setMissionInfoARM(ModelsUtils.toModel_MissionInfoARM(estimate.getMission()));
        estimateInfo.setVisible(estimate.isVisible());
        return estimateInfo;
    }




//    public static WatchMissionInfo toModel(WatchMission watchMission){
//        WatchMissionInfo watchMissionInfo = new WatchMissionInfo();
//        watchMissionInfo.setClientId(watchMission.getClient().getId());
//        watchMissionInfo.setMissionId(watchMission.getMission().getId());
//        watchMissionInfo.setSecurity_token(watchMission.getToken());
//        return watchMissionInfo;
//    }


//    public static WatchMission fromModel(WatchMissionInfo watchMissionInfo, WatchMission watchMission){
//          if(watchMissionInfo!=null){
//              watchMission.setId(watchMission.getId());
//          }
//        watchMission.setClient(ModelsUtils.fromModel(watchMissionInfo.getClientInfo()));
//        watchMission.setDriver(ModelsUtils.fromModelARM(watchMissionInfo.getDriverInfoARM()));
//        watchMission.setMission(ModelsUtils.fromModel(watchMissionInfo.getMissionInfo()));
//        watchMission.setUrl(watchMissionInfo.getUrl());
//        watchMission.setToken(watchMissionInfo.getSecurity_token());
//        return watchMission;
//    }


    public static Assistant fromModel(AssistantInfo assistantInfo, Assistant assistant){
        if(assistantInfo.getId()!=null){
            assistant.setId(assistantInfo.getId());
        }
        assistant.setTabletCount(assistantInfo.getTabletCount());
        assistant.setName(assistantInfo.getName());
          return assistant;
    }






    public static Comission fromModel(ComissionInfo comissionInfo, Comission comission){
        comission.setComissionType(comissionInfo.getComissionType());
        comission.setObjectId(comissionInfo.getObjectId());
        comission.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
        comission.setComissionAmount(comissionInfo.getComissionAmount());
        comission.setStartTime(DateTimeUtils.toDateTime(comissionInfo.getStartTime()));
        comission.setEndTime(DateTimeUtils.toDateTime(comissionInfo.getEndTime()));
        return comission;
    }


    public static Comission fromModel(ComissionInfo comissionInfo){
        Comission comission = new Comission();
        comission.setComissionType(comissionInfo.getComissionType());
        comission.setObjectId(comissionInfo.getObjectId());
        comission.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
        comission.setComissionAmount(comissionInfo.getComissionAmount());
        comission.setStartTime(DateTimeUtils.toDateTime(comissionInfo.getStartTime()));
        comission.setEndTime(DateTimeUtils.toDateTime(comissionInfo.getEndTime()));
        return comission;
    }


    public static ComissionInfo toModel(Comission comission){
        ComissionInfo comissionInfo = new ComissionInfo();
        if(comission!=null){
            comissionInfo.setComissionType(comission.getComissionType());
            comissionInfo.setObjectId(comission.getObjectId());
            comissionInfo.setId(comission.getId());
            comissionInfo.setComissionAmount(comission.getComissionAmount());
            comissionInfo.setStartTime(comission.getStartTime().getMillis());
            comissionInfo.setEndTime(comission.getEndTime().getMillis());
            comissionInfo.setUpdateTime(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6()));
        }
        return comissionInfo;
    }




    public static News fromModel(NewsInfo newsInfo, News news){
        if(newsInfo.getId()!=null){
            news.setId(newsInfo.getId());
        }
        news.setTextNews(newsInfo.getTextNews());
        news.setTitle(newsInfo.getTitle());
        news.setUrlNews(newsInfo.getUrlNews());
        news.setTimeOfStarting(DateTimeUtils.toDateTime(newsInfo.getTimeOfStarting()));
        news.setTimeOfFinishing(DateTimeUtils.toDateTime(newsInfo.getTimeOfFinishing()));
          return news;
    }




    public static TaxoparkPartnersInfo toModel(TaxoparkPartners taxoparkPartners){
        TaxoparkPartnersInfo info = new TaxoparkPartnersInfo();
        info.setId(taxoparkPartners.getId() != null ? taxoparkPartners.getId() : 0);
        info.setNameTaxopark(taxoparkPartners.getNameTaxopark());
        info.setOfficeAddress(taxoparkPartners.getOfficeAddress());
        info.setOfficePhone(taxoparkPartners.getOfficePhone());
        info.setPriority(taxoparkPartners.getPriority());
        info.setResponsibilityFio(taxoparkPartners.getResponsibilityFio());
        info.setResponsibilityPhone(taxoparkPartners.getResponsibilityPhone());
        info.setMoneyAmount(taxoparkPartners.getMoneyAmount() != null ? taxoparkPartners.getMoneyAmount().getAmount().intValue() : 0);
        info.setIncreaseToDriver(taxoparkPartners.isIncreaseToDriver());
          return info;
    }



    public static TaxoparkPartners fromModel(TaxoparkPartnersInfo taxoparkPartnersInfo, TaxoparkPartners taxoparkPartners){
         if(taxoparkPartners == null){
             taxoparkPartners = new TaxoparkPartners();
         }
         taxoparkPartners.setNameTaxopark(taxoparkPartnersInfo.getNameTaxopark());
         taxoparkPartners.setOfficeAddress(taxoparkPartnersInfo.getOfficeAddress());
         taxoparkPartners.setOfficePhone(taxoparkPartnersInfo.getOfficePhone());
         taxoparkPartners.setPriority(taxoparkPartnersInfo.getPriority());
         taxoparkPartners.setResponsibilityFio(taxoparkPartnersInfo.getResponsibilityFio());
         taxoparkPartners.setResponsibilityPhone(taxoparkPartnersInfo.getResponsibilityPhone());
         taxoparkPartners.setIncreaseToDriver(taxoparkPartnersInfo.isIncreaseToDriver());
           return taxoparkPartners;
    }


    public static Comment fromModel(CommentInfo commentInfo, Order order){
       Comment comment = new Comment();
       comment.setComment(commentInfo.getComment());
       comment.setTimeOfComment(DateTimeUtils.nowNovosib_GMT6());
       comment.setOrder(order);
       return comment;
    }


    public static Order fromModel(OrderInfo info){
        Order order = new Order();
        order.setOrderType(OrderType.getByValue(info.getOrderType()));
        order.setState(Order.State.NEW);
        order.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
        return order;
    }




    public static MissionInfoARM toModel_MissionInfoARM(Mission mission){
        MissionInfoARM missionInfo = new MissionInfoARM();
        if (mission != null) {
            List<MissionAddresses> missionAddressesList = mission.getMissionAddresses();
            if(!missionAddressesList.isEmpty()){
                for(MissionAddresses missionAddresses: missionAddressesList){
                    missionInfo.getMissionAddressesInfos().add(toModel(missionAddresses));
                }
            }
            missionInfo.setId(mission.getId());
            missionInfo.setAddressFrom(mission.getLocationFrom().getAddress());
            missionInfo.setAddressTo(mission.getLocationTo().getAddress());
            missionInfo.setComment(mission.getComments());
            missionInfo.setPorchNumber(mission.getPorchNumber());
            missionInfo.setAutoType(mission.getAutoClass().getValue());

            missionInfo.setRating(((double)mission.getScore().getGeneral()));

            missionInfo.setLocationFrom(ModelsUtils.toModel(mission.getLocationFrom()));

            missionInfo.setCityFrom(mission.getLocationFrom().getCity());
            missionInfo.setRegionFrom(mission.getLocationFrom().getRegion());
            missionInfo.setExpectedDistance(mission.getStatistics().getDistanceExpected());
            missionInfo.setDistance(mission.getStatistics().getDistanceInFact());

            if (mission.getLocationTo() != null) {
                missionInfo.setLocationTo(ModelsUtils.toModel(mission.getLocationTo()));
                missionInfo.setCityTo(mission.getLocationTo().getCity());
                missionInfo.setRegionTo(mission.getLocationTo().getRegion());
            }

            missionInfo.setPaymentType(mission.getPaymentType().getValue());

            for (MissionService service : mission.getStatistics().getServicesExpected()) {
                if (!MissionService.UNKNOWN.equals(service)) {
                    missionInfo.getOptions().add(service.getValue());
                }
            }
            Client clientInfo = mission.getClientInfo();
            if (clientInfo != null) {
                missionInfo.setClientInfo(ModelsUtils.toModel(clientInfo));
            }
            Driver driverInfo = mission.getDriverInfo();
            if (driverInfo != null) {
                missionInfo.setDriverInfoARM(ModelsUtils.toModelARM(driverInfo));
            }

            missionInfo.setBooked(Mission.State.BOOKED.equals(mission.getState()));
            missionInfo.setMissionState(mission.getState().toString());
            if(mission.getTimeOfRequesting()!=null){
                missionInfo.setTimeOfRequesting(DateTimeUtils.toDate(mission.getTimeOfRequesting()));
            }
            if(mission.getTimeOfAssigning()!= null){
                missionInfo.setTimeOfAssigning(DateTimeUtils.toDate(mission.getTimeOfAssigning()));
            }
            if(mission.getTimeOfSeating()!=null){
                missionInfo.setTimeOfSeating(DateTimeUtils.toDate(mission.getTimeOfSeating()));
            }
            if(mission.getTimeOfStarting()!=null){
                missionInfo.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()));
            }
            if(mission.getTimeOfArriving()!= null){
                missionInfo.setTimeOfArriving(DateTimeUtils.toDate(mission.getTimeOfArriving()));
            }
            if(mission.getTimeOfFinishing() != null) {
                missionInfo.setTimeOfFinishing(DateTimeUtils.toDate(mission.getTimeOfFinishing()));
            }
            missionInfo.setPrice(mission.getStatistics().getPriceInFact().getAmount().doubleValue());
            if(mission.getStatistics().getSumIncrease().getAmount()!=null){
                missionInfo.setSumIncrease(mission.getStatistics().getSumIncrease().getAmount().doubleValue());
            }
            missionInfo.setExpectedPrice(mission.getStatistics().getPriceExpected().getAmount().doubleValue());
            missionInfo.setLateDriverBookedMin(mission.getLateDriverBookedMin());
            missionInfo.setAutoTypeStr(autoTypeStr(mission.getAutoClass().getValue()));
            missionInfo.setBooked(mission.isBooked()!=null ? mission.isBooked():false);
            if(mission.isTimeIsAfter()){
                if(mission.getTimeOfRequesting()!=null && mission.getTimeOfStarting()!=null){
                    Minutes minutes = Minutes.minutesBetween(mission.getTimeOfRequesting(), mission.getTimeOfStarting());
                    missionInfo.setTimeAfterMin(Math.abs(minutes.getMinutes()));
                }
            }
        }
        return missionInfo;
    }




    public static MissionInfo toModelClientARM(Mission mission){
        MissionInfo missionInfo = new MissionInfo();
        if (mission != null) {

            List<MissionAddresses> missionAddressesList = mission.getMissionAddresses();
            if(!missionAddressesList.isEmpty()){
                for(MissionAddresses missionAddresses: missionAddressesList){
                    missionInfo.getMissionAddressesInfos().add(toModel(missionAddresses));
                }
            }

            missionInfo.setId(mission.getId());
            missionInfo.setAddressFrom(mission.getLocationFrom().getAddress());
            missionInfo.setAddressTo(mission.getLocationTo().getAddress());
            missionInfo.setComment(mission.getComments());
            missionInfo.setPorchNumber(mission.getPorchNumber());
            missionInfo.setAutoType(mission.getAutoClass().getValue());

            missionInfo.setRating(((double)mission.getScore().getGeneral()));

            missionInfo.setLocationFrom(ModelsUtils.toModel(mission.getLocationFrom()));

            missionInfo.setCityFrom(mission.getLocationFrom().getCity());
            missionInfo.setRegionFrom(mission.getLocationFrom().getRegion());


            if (mission.getLocationTo() != null) {
                missionInfo.setLocationTo(ModelsUtils.toModel(mission.getLocationTo()));
                missionInfo.setCityTo(mission.getLocationTo().getCity());
                missionInfo.setRegionTo(mission.getLocationTo().getRegion());
            }

            missionInfo.setPaymentType(mission.getPaymentType().getValue());

            for (MissionService service : mission.getStatistics().getServicesExpected()) {
                if (!MissionService.UNKNOWN.equals(service)) {
                    missionInfo.getOptions().add(service.getValue());
                }
            }

            Client clientInfo = mission.getClientInfo();
            if (clientInfo != null) {
                missionInfo.setClientInfo(ModelsUtils.toModel(clientInfo));
            }

            Driver driverInfo = mission.getDriverInfo();
            if (driverInfo != null) {
                missionInfo.setDriverInfo(ModelsUtils.toModel(driverInfo));
            }

            missionInfo.setBooked(Mission.State.BOOKED.equals(mission.getState()));
            missionInfo.setMissionState(mission.getState().toString());

            if(mission.getTimeOfRequesting()!=null){
                missionInfo.setTimeOfRequesting(DateTimeUtils.toDate(mission.getTimeOfRequesting()));
            }

            if(mission.getTimeOfAssigning()!=null){
                missionInfo.setTimeOfAssigning(DateTimeUtils.toDate(mission.getTimeOfAssigning()));
            }

            if(mission.getTimeOfSeating()!=null){
                missionInfo.setTimeOfSeating(DateTimeUtils.toDate(mission.getTimeOfSeating()));
            }
            if(mission.getTimeOfStarting()!=null){
                missionInfo.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()));
            }
            if(mission.getTimeOfArriving()!=null){
                missionInfo.setTimeOfArriving(DateTimeUtils.toDate(mission.getTimeOfArriving()));
            }
            if(mission.getTimeOfFinishing()!=null){
                missionInfo.setTimeOfFinishing(DateTimeUtils.toDate(mission.getTimeOfFinishing()));
            }

            missionInfo.setPrice(mission.getStatistics().getPriceInFact().getAmount().doubleValue());
            if(mission.getStatistics().getSumIncrease().getAmount()!=null){
                missionInfo.setSumIncrease(mission.getStatistics().getSumIncrease().getAmount().doubleValue());
            }

            missionInfo.setExpectedPrice(mission.getStatistics().getPriceExpected().getAmount().doubleValue());
            missionInfo.setLateDriverBookedMin(mission.getLateDriverBookedMin());

        }
        return missionInfo;
    }




    public static MdOrderInfo toModel(MDOrder mdOrder) {
        MdOrderInfo mdOrderInfo = new MdOrderInfo();
         mdOrderInfo.setId(mdOrder.getId());
         mdOrderInfo.setBindingId(mdOrder.getBindingId());
         mdOrderInfo.setMdOrderNumber(mdOrder.getMdOrderNumber());
         mdOrderInfo.setPaymentDate(mdOrder.getPaymentDate());
         mdOrderInfo.setPaymentDescription(mdOrder.getPaymentDescription());
         mdOrderInfo.setPaymentStatus(mdOrder.getPaymentStatus());
         mdOrderInfo.setRefundDate(mdOrder.getRefundDate());
         mdOrderInfo.setRefundDescription(mdOrder.getRefundDescription());
         mdOrderInfo.setSum(mdOrder.getSum());
           if(mdOrder.getTimeOfInsert()!=null){
               mdOrderInfo.setTimeOfInsert(mdOrder.getTimeOfInsert().getMillis());
           }
           if(mdOrder.getClientCard()!=null){
               mdOrderInfo.setClientCardId(mdOrder.getClientCard().getId());
           }
           if(mdOrder.getMission()!=null){
               mdOrderInfo.setMissionId(mdOrder.getMission().getId());
           }
          return  mdOrderInfo;
    }







    public static MissionInfo toModelClient(Mission mission) {
        MissionInfo missionInfo = new MissionInfo();
        if (mission != null) {
            List<MissionAddresses> missionAddressesList = mission.getMissionAddresses();
            if(!missionAddressesList.isEmpty()){
                for(MissionAddresses missionAddresses: missionAddressesList){
                    missionInfo.getMissionAddressesInfos().add(toModel(missionAddresses));
                }
            }

            missionInfo.setId(mission.getId());
            missionInfo.setAddressFrom(mission.getLocationFrom().getAddress());
            missionInfo.setAddressTo(mission.getLocationTo().getAddress());
            missionInfo.setComment(mission.getComments());
            missionInfo.setPorchNumber(mission.getPorchNumber());
            missionInfo.setAutoType(mission.getAutoClass().getValue());

            // старый вариант - оценка берется непосредственно из mission
            missionInfo.setRating(((double)mission.getScore().getGeneral()));

            missionInfo.setLocationFrom(ModelsUtils.toModel(mission.getLocationFrom()));

            missionInfo.setCityFrom(mission.getLocationFrom().getCity());
            missionInfo.setRegionFrom(mission.getLocationFrom().getRegion());


            if (mission.getLocationTo() != null) {
                missionInfo.setLocationTo(ModelsUtils.toModel(mission.getLocationTo()));
                missionInfo.setCityTo(mission.getLocationTo().getCity());
                missionInfo.setRegionTo(mission.getLocationTo().getRegion());
            }

            missionInfo.setPaymentType(mission.getPaymentType().getValue());

            for (MissionService service : mission.getStatistics().getServicesExpected()) {
                if (!MissionService.UNKNOWN.equals(service)) {
                    missionInfo.getOptions().add(service.getValue());
                }
            }

            Client clientInfo = mission.getClientInfo();
            if (clientInfo != null) {
                missionInfo.setClientInfo(ModelsUtils.toModel(clientInfo));
            }

            Driver driverInfo = mission.getDriverInfo();
            if (driverInfo != null) {
                missionInfo.setDriverInfo(ModelsUtils.toModel(driverInfo));
            }

            missionInfo.setBooked(Mission.State.BOOKED.equals(mission.getState()));
            missionInfo.setMissionState(mission.getState().toString());

            if(mission.getTimeOfRequesting()!=null){
                missionInfo.setTimeOfRequesting(DateTimeUtils.toDate(mission.getTimeOfRequesting().plusHours(2)));
            }

            if(mission.getTimeOfAssigning()!=null){
               missionInfo.setTimeOfAssigning(DateTimeUtils.toDate(mission.getTimeOfAssigning().plusHours(2)));
            }

            if(mission.getTimeOfSeating()!=null){
                missionInfo.setTimeOfSeating(DateTimeUtils.toDate(mission.getTimeOfSeating().plusHours(2)));
            }
            if(mission.getTimeOfStarting()!=null){
                missionInfo.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting().plusHours(2)));
            }
            if(mission.getTimeOfArriving()!=null){
                missionInfo.setTimeOfArriving(DateTimeUtils.toDate(mission.getTimeOfArriving().plusHours(2)));
            }
            if(mission.getTimeOfFinishing()!=null){
                missionInfo.setTimeOfFinishing(DateTimeUtils.toDate(mission.getTimeOfFinishing().plusHours(2)));
            }

            missionInfo.setPrice(mission.getStatistics().getPriceInFact().getAmount().doubleValue());
            if(mission.getStatistics().getSumIncrease().getAmount()!=null){
                missionInfo.setSumIncrease(mission.getStatistics().getSumIncrease().getAmount().doubleValue());
            }

            missionInfo.setExpectedPrice(mission.getStatistics().getPriceExpected().getAmount().doubleValue());
            missionInfo.setLateDriverBookedMin(mission.getLateDriverBookedMin());

        }
        return missionInfo;
    }




    public static PartnersGroupInfo toModel(PartnersGroup partnersGroup) {
        PartnersGroupInfo partnersGroupInfo = new PartnersGroupInfo();
            if(partnersGroup!=null){
                  List<ItemPartnersGroup> itemPartnersGroupList = partnersGroup.getGroupItems();
                      for(ItemPartnersGroup itemPartnersGroup :itemPartnersGroupList){
                            partnersGroupInfo.getItemPartnersGroupInfo().add(toModel(itemPartnersGroup));
                      }
                partnersGroupInfo.setGroupName(partnersGroup.getGroupName());
                partnersGroupInfo.setIdGroup(partnersGroup.getId());
                partnersGroupInfo.setSection(partnersGroup.getSection());
            }
          return partnersGroupInfo;
    }



    public static PartnersGroup fromModel(PartnersGroupInfo partnersGroupInfo) {
        PartnersGroup partnersGroup = new PartnersGroup();
        if(partnersGroupInfo!=null){
            partnersGroup.setGroupName(partnersGroupInfo.getGroupName());
            partnersGroup.setSection(partnersGroupInfo.getSection());
            //partnersGroup.setId(partnersGroupInfo.getIdGroup());
        }
        return partnersGroup;
    }

    public static PartnersGroup fromModel(PartnersGroupInfo partnersGroupInfo, PartnersGroup partnersGroup) {
        if(partnersGroupInfo!=null){
            partnersGroup.setGroupName(partnersGroupInfo.getGroupName());
            partnersGroup.setSection(partnersGroupInfo.getSection());
        }
        return partnersGroup;
    }



    public static ItemPartnersGroup fromModel(ItemPartnersGroupInfo itemPartnersGroupInfo, PartnersGroup partnersGroup) {
        ItemPartnersGroup itemPartnersGroup = new ItemPartnersGroup();
        if(itemPartnersGroupInfo!=null){
            itemPartnersGroup.setId(itemPartnersGroupInfo.getItemId());
            itemPartnersGroup.setCity(itemPartnersGroupInfo.getCity());
            itemPartnersGroup.setHouse(itemPartnersGroupInfo.getHouse());
            itemPartnersGroup.setKorpus(itemPartnersGroupInfo.getKorpus());
            itemPartnersGroup.setRegion(itemPartnersGroupInfo.getRegion());
            itemPartnersGroup.setStreet(itemPartnersGroupInfo.getStreet());
            itemPartnersGroup.setName(itemPartnersGroupInfo.getName());
            itemPartnersGroup.setPhone(itemPartnersGroupInfo.getPhone());
            itemPartnersGroup.setPartnersGroup(partnersGroup);
        }
        return itemPartnersGroup;
    }



    public static ItemPartnersGroupInfo toModel(ItemPartnersGroup itemPartnersGroup) {
        ItemPartnersGroupInfo itemPartnersGroupInfo = new ItemPartnersGroupInfo();
        if(itemPartnersGroup!=null){
            itemPartnersGroupInfo.setGroupId(itemPartnersGroup.getPartnersGroup().getId());
            itemPartnersGroupInfo.setStreet(itemPartnersGroup.getStreet());
            itemPartnersGroupInfo.setRegion(itemPartnersGroup.getRegion());
            itemPartnersGroupInfo.setKorpus(itemPartnersGroup.getKorpus());
            itemPartnersGroupInfo.setHouse(itemPartnersGroup.getHouse());
            itemPartnersGroupInfo.setCity(itemPartnersGroup.getCity());
            itemPartnersGroupInfo.setName(itemPartnersGroup.getName());
            itemPartnersGroupInfo.setPhone(itemPartnersGroup.getPhone());
            itemPartnersGroupInfo.setItemId(itemPartnersGroup.getId());
        }
        return itemPartnersGroupInfo;
    }





    public static MissionInfoARM toModelARM(Mission mission) {
        MissionInfoARM missionInfoARM = new MissionInfoARM();

        if (mission != null) {
        List<MissionAddresses> missionAddressesList = mission.getMissionAddresses();
         if(!missionAddressesList.isEmpty()){
           for(MissionAddresses missionAddresses: missionAddressesList){
              missionInfoARM.getMissionAddressesInfos().add(toModel(missionAddresses));
             }
         }
            missionInfoARM.setExpectedArrivalTimes(mission.getExpectedArrivalTimes());
            missionInfoARM.setId(mission.getId());
            missionInfoARM.setAddressFrom(mission.getLocationFrom().getAddress());
            missionInfoARM.setAddressTo(mission.getLocationTo().getAddress());
            missionInfoARM.setComment(mission.getComments());
            missionInfoARM.setPorchNumber(mission.getPorchNumber());
            missionInfoARM.setAutoType(mission.getAutoClass().getValue());

            // старый вариант
            //missionInfo.setRating(((double)mission.getScore().getGeneral()));

            missionInfoARM.setLocationFrom(ModelsUtils.toModel(mission.getLocationFrom()));
            missionInfoARM.setCityFrom(mission.getLocationFrom().getCity());
            missionInfoARM.setRegionFrom(mission.getLocationFrom().getRegion());
            missionInfoARM.setExpectedDistance(mission.getStatistics().getDistanceExpected());
            missionInfoARM.setDistance(mission.getStatistics().getDistanceInFact());

            if (mission.getLocationTo() != null) {
                missionInfoARM.setLocationTo(ModelsUtils.toModel(mission.getLocationTo()));
                missionInfoARM.setCityTo(mission.getLocationTo().getCity());
                missionInfoARM.setRegionTo(mission.getLocationTo().getRegion());
            }

            missionInfoARM.setPaymentType(mission.getPaymentType().getValue());
            missionInfoARM.setPaymentStateCard(mission.getPaymentStateCard().toString());

            if(mission.getStatistics().getServicesInFact()!=null && !mission.getStatistics().getServicesInFact().isEmpty()){
                for (MissionService service : mission.getStatistics().getServicesInFact()) {
                    if (!MissionService.UNKNOWN.equals(service)) {
                        missionInfoARM.getOptions().add(service.getValue());
                    }
                }
            }else{
                for (MissionService service : mission.getStatistics().getServicesExpected()) {
                    if (!MissionService.UNKNOWN.equals(service)) {
                        missionInfoARM.getOptions().add(service.getValue());
                    }
                }
            }
            Client clientInfo = mission.getClientInfo();
            if (clientInfo != null) {
                missionInfoARM.setClientInfo(ModelsUtils.toModel(clientInfo));
            }
            Driver driverInfo = mission.getDriverInfo();
            if (driverInfo != null) {
                missionInfoARM.setDriverInfoARM(ModelsUtils.toModelARM(driverInfo));
            }
            missionInfoARM.setBooked(Mission.State.BOOKED.equals(mission.getState()));
            missionInfoARM.setTimeOfAssigning(DateTimeUtils.toDate(mission.getTimeOfAssigning()));
            if(mission.getTimeOfRequesting()!=null){
                missionInfoARM.setTimeOfRequesting(DateTimeUtils.toDate(mission.getTimeOfRequesting()));
            }
            if(mission.getTimeOfSeating()!=null){
                missionInfoARM.setTimeOfSeating(DateTimeUtils.toDate(mission.getTimeOfSeating()));
            }
            if(mission.getTimeOfStarting()!=null){
                missionInfoARM.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()));
            }
            missionInfoARM.setTimeOfArriving(DateTimeUtils.toDate(mission.getTimeOfArriving()));
            missionInfoARM.setTimeOfFinishing(DateTimeUtils.toDate(mission.getTimeOfFinishing()));
            missionInfoARM.setPrice(mission.getStatistics().getPriceInFact().getAmount().doubleValue());
            if(mission.getStatistics().getSumIncrease()!=null){
                missionInfoARM.setSumIncrease(mission.getStatistics().getSumIncrease().getAmount().doubleValue());
            }
            missionInfoARM.setExpectedPrice(mission.getStatistics().getPriceExpected().getAmount().doubleValue());
            missionInfoARM.setLateDriverBookedMin(mission.getLateDriverBookedMin());
            missionInfoARM.setMissionState(mission.getState().toString());
            missionInfoARM.setIsLate(mission.getIsLate());
        }
        return missionInfoARM;
    }



    public static MissionInfoCorporate toModelMissionInfoCorporate(Mission mission) {
        MissionInfoCorporate missionInfo = new MissionInfoCorporate();
        if (mission != null) {
            List<MissionAddresses> missionAddressesList = mission.getMissionAddresses();
            if(!missionAddressesList.isEmpty()){
                for(MissionAddresses missionAddresses: missionAddressesList){
                    missionInfo.getMissionAddressesInfos().add(toModel(missionAddresses));
                }
            }
            missionInfo.setId(mission.getId());
            missionInfo.setAddressFrom(mission.getLocationFrom().getAddress());
            missionInfo.setAddressTo(mission.getLocationTo().getAddress());
            missionInfo.setDistance(mission.getStatistics().getDistanceInFact());
            missionInfo.setPaymentType(mission.getPaymentType().getValue());

            Client clientInfo = mission.getClientInfo();
            if (clientInfo != null) {
                missionInfo.setClientInfo(ModelsUtils.toModelCorporate(clientInfo, null, false));
            }
            Driver driverInfo = mission.getDriverInfo();
            if (driverInfo != null) {
                missionInfo.setDriverInfo(ModelsUtils.toModelCorporate(driverInfo));
            }
            if(mission.getTimeOfRequesting()!=null){
                missionInfo.setTimeOfRequesting(mission.getTimeOfRequesting().getMillis());
            }
            missionInfo.setTimeOfArriving(DateTimeUtils.toDate(mission.getTimeOfArriving()));
            missionInfo.setTimeOfFinishing(DateTimeUtils.toDate(mission.getTimeOfFinishing()));
            missionInfo.setPrice(mission.getStatistics().getPriceInFact().getAmount().doubleValue());
            missionInfo.setAutoTypeStr(autoTypeStr(mission.getAutoClass().getValue()));
            if(mission.getTimeOfSeating()!=null){
                missionInfo.setTimeOfSeating(DateTimeUtils.toDate(mission.getTimeOfSeating()));
            }
            if(mission.getTimeOfStarting()!=null){
                missionInfo.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()));
            }
            missionInfo.setExpectedDistance(mission.getStatistics().getDistanceExpected());
        }
        return missionInfo;
    }



    public static String autoTypeStr(int type){
        String result = "";
        switch(type){
            case 1: {
               result = "Стандарт";
               break;
            }
            case 2: {
                result = "Комфорт";
                break;
            }
            case 3: {
                result = "Бизнес";
                break;
            }
            case 4: {
                result = "ЛоуКостер";
                break;
            }
            case 5: {
                result = "Бонус";
                break;
            }
            default: break;
        }
        return result;
    }







    public static MissionInfo toModel(Mission mission) {
        MissionInfo missionInfo = new MissionInfo();
        if (mission != null) {

            List<MissionAddresses> missionAddressesList = mission.getMissionAddresses();
            if(!missionAddressesList.isEmpty()){
                for(MissionAddresses missionAddresses: missionAddressesList){
                    missionInfo.getMissionAddressesInfos().add(toModel(missionAddresses));
                }
            }
            missionInfo.setId(mission.getId());
            missionInfo.setAddressFrom(mission.getLocationFrom().getAddress());
            missionInfo.setAddressTo(mission.getLocationTo().getAddress());
            missionInfo.setComment(mission.getComments());
            missionInfo.setPorchNumber(mission.getPorchNumber());

            missionInfo.setAutoType(mission.getAutoClass().getValue());

            // старый вариант
            missionInfo.setRating(((double)mission.getScore().getGeneral()));

            missionInfo.setLocationFrom(ModelsUtils.toModel(mission.getLocationFrom()));
            missionInfo.setCityFrom("  "+mission.getLocationFrom().getCity());
            missionInfo.setRegionFrom(mission.getLocationFrom().getRegion());
            missionInfo.setExpectedDistance(mission.getStatistics().getDistanceExpected());

            if (mission.getLocationTo() != null) {
                missionInfo.setLocationTo(ModelsUtils.toModel(mission.getLocationTo()));
                missionInfo.setCityTo(mission.getLocationTo().getCity());
                missionInfo.setRegionTo(mission.getLocationTo().getRegion());
            }

            missionInfo.setPaymentType(mission.getPaymentType().getValue());

            missionInfo.setPaymentStateCard(mission.getPaymentStateCard().toString());

            if(mission.getStatistics().getServicesInFact()!=null && !mission.getStatistics().getServicesInFact().isEmpty()){
                for (MissionService service : mission.getStatistics().getServicesInFact()) {
                    if (!MissionService.UNKNOWN.equals(service)) {
                        missionInfo.getOptions().add(service.getValue());
                    }
                }
            }else{
                for (MissionService service : mission.getStatistics().getServicesExpected()) {
                    if (!MissionService.UNKNOWN.equals(service)) {
                        missionInfo.getOptions().add(service.getValue());
                    }
                }
            }
            Client clientInfo = mission.getClientInfo();
            if (clientInfo != null) {
                //missionInfo.setClientInfo(ModelsUtils.toModel(clientInfo));
                missionInfo.setClientInfo(ModelsUtils.toModelClientInfoLite(clientInfo));
            }
            Driver driverInfo = mission.getDriverInfo();
            if (driverInfo != null) {
                missionInfo.setDriverInfo(ModelsUtils.toModel(driverInfo));
            }
            missionInfo.setBooked(mission.isBooked() != null ? mission.isBooked() : Mission.State.BOOKED.equals(mission.getState())); // Mission.State.BOOKED.equals(mission.getState())
            missionInfo.setTimeOfAssigning(DateTimeUtils.toDate(mission.getTimeOfAssigning()));
            if(mission.getTimeOfRequesting()!=null){
                 missionInfo.setTimeOfRequesting(DateTimeUtils.toDate(mission.getTimeOfRequesting().minusHours(6))); // DateTimeUtils.toDateTime(missionInfo.getTimeOfStart())
            }
            if(mission.getTimeOfSeating()!=null){
                 missionInfo.setTimeOfSeating(DateTimeUtils.toDate(mission.getTimeOfSeating().minusHours(6))); // -7
            }
            if(mission.getTimeOfStarting()!=null){
                missionInfo.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting().minusHours(6)));// -7 // missionInfo.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()))
            }
            missionInfo.setTimeOfArriving(DateTimeUtils.toDate(mission.getTimeOfArriving()));
            missionInfo.setTimeOfFinishing(DateTimeUtils.toDate(mission.getTimeOfFinishing()));
            missionInfo.setPrice(mission.getStatistics().getPriceInFact().getAmount().doubleValue());
            if(mission.getStatistics().getSumIncrease()!=null){
                missionInfo.setSumIncrease(mission.getStatistics().getSumIncrease().getAmount().doubleValue());
            }
            missionInfo.setExpectedPrice(mission.getStatistics().getPriceExpected().getAmount().doubleValue());
            missionInfo.setLateDriverBookedMin(mission.getLateDriverBookedMin());
            missionInfo.setMissionState(mission.getState().toString());
            /* КОСТЫЛЬ ЧТОБЫ СООБЩИТЬ ВОДИЛЕ О ТОМ, ЧТО ЗАКАЗ НА ЧЕРЕЗ х МИНУТ */
            if(mission.isTimeIsAfter()){
                missionInfo.setFixedMission(Boolean.TRUE);
            }
        }
        return missionInfo;
    }



    public static DriverCash toModel(DriverCashFlow driverCashFlow){
          DriverCash driverCash = new DriverCash();
            if(driverCashFlow!=null){
                driverCash.setId(driverCashFlow.getId());
                driverCash.setDate(DateTimeUtils.toDate(driverCashFlow.getDate_operation()));
                driverCash.setDriver_id(driverCashFlow.getDriver().getId());
                   if(driverCashFlow.getMission()!=null)
                driverCash.setMission_id(driverCashFlow.getMission().getId());
                driverCash.setOperation(driverCashFlow.getOperation());
                driverCash.setSum(driverCashFlow.getSum());
            }
            return driverCash;
    }



//
//    public static DriverCashFlow fromModel(DriverCash driverCash){
//        DriverCashFlow driverCashFlow = new DriverCashFlow();
//        if(driverCash!=null){
//            driverCashFlow.setId(driverCash.getId());
//            driverCashFlow.setDate_operation(DateTimeUtils.toDateTime(driverCash.getDate()));
//            driverCashFlow.setdsetDriver_id(driverCash.getDriver_id());
//            driverCashFlow.setMission_id(driverCash.getMission_id());
//            driverCashFlow.setOperation(driverCash.getOperation());
//            driverCashFlow.setSum(driverCash.getSum());
//        }
//        return driverCash;
//    }



    public static DriverInfoCorporate toModelCorporate(Driver driver) {
        DriverInfoCorporate driverInfo = new DriverInfoCorporate();
        if (driver != null) {
            driverInfo.setFirstName(driver.getFirstName());
            driverInfo.setLastName(driver.getLastName());
            driverInfo.setAutoModel(driver.getAutoModel());
            driverInfo.setAutoColor(driver.getAutoColor());
            driverInfo.setAutoNumber(driver.getAutoNumber());
            driverInfo.setPhone(driver.getPhone());
        }
        return driverInfo;
    }


    public static DriverInfo toModel(Driver driver) {
        DriverInfo driverInfo = new DriverInfo();
        if (driver != null) {
            driverInfo.setId(driver.getId());
            driverInfo.setFirstName(driver.getFirstName());
            driverInfo.setLastName(driver.getLastName());
            driverInfo.setAutoModel(driver.getAutoModel());
            driverInfo.setAutoClass(driver.getAutoClass().getValue());
            driverInfo.setAutoColor(driver.getAutoColor());
            driverInfo.setAutoNumber(driver.getAutoNumber());
            driverInfo.setAutoYear(driver.getAutoYear());
            driverInfo.setBalance(driver.getAccount().getMoney().getAmount().doubleValue());
            driverInfo.setTotalRating(driver.getRating());
            //driverInfo.setRatingPoints(driver.getRatingPoints());
            driverInfo.setPhone(driver.getPhone());
            driverInfo.setPhotoUrl(driver.getPhotoUrl());
            driverInfo.getPhotosCarsUrl().addAll(driver.getPhotosCarsUrl());
//            driverInfo.getDriverCashFlow().addAll(driver.getDriverCash());
            driverInfo.setPassword(driver.getPassword());
            driverInfo.setLogin(driver.getLogin());
            driverInfo.setAdministrativeStratus(driver.getAdministrativeStatus().name());
            driverInfo.setBirthDate(driver.getBirthDate().toDate().getTime() / 1000);
            driverInfo.setVersionApp(driver.getVersionApp());
            driverInfo.setEntrepreneur(driver.isEntrepreneur());
            driverInfo.setWifi(driver.getRouter()!=null ? true : false);
            driverInfo.setRatingPoints(driver.getRatedMissions());
        }
        return driverInfo;
    }




    public static DriverInfoARM toModelARM(Driver driver) {
        DriverInfoARM driverInfoARM = new DriverInfoARM();
        if (driver != null) {
            driverInfoARM.setId(driver.getId());
            driverInfoARM.setFirstName(driver.getFirstName());
            driverInfoARM.setLastName(driver.getLastName());
            driverInfoARM.setAutoModel(driver.getAutoModel());
            driverInfoARM.setAutoClass(driver.getAutoClass().getValue());
            driverInfoARM.setAutoColor(driver.getAutoColor());
            driverInfoARM.setAutoNumber(driver.getAutoNumber());
            driverInfoARM.setAutoYear(driver.getAutoYear());
            driverInfoARM.setBalance(driver.getAccount().getMoney().getAmount().doubleValue());
            driverInfoARM.setTotalRating(driver.getRating());
            driverInfoARM.setRatingPoints(driver.getRatingPoints());
            driverInfoARM.setPhone(driver.getPhone());
            driverInfoARM.setPhotoUrl(driver.getPhotoUrl());
            driverInfoARM.setPhotoUrlByVersion(driver.getPhotoUrlByVersion());
            driverInfoARM.getPhotosCarsUrl().addAll(driver.getPhotosCarsUrl());
//            driverInfo.getDriverCashFlow().addAll(driver.getDriverCash());
            driverInfoARM.setPassword(driver.getPassword());
            driverInfoARM.setLogin(driver.getLogin());
            driverInfoARM.setAdministrativeStratus(driver.getAdministrativeStatus().name());
            driverInfoARM.setBirthDate(driver.getBirthDate().toDate().getTime()/1000);
            driverInfoARM.setVersionApp(driver.getVersionApp());
            driverInfoARM.setEntrepreneur(driver.isEntrepreneur());
            driverInfoARM.setWifi(driver.isWifi());
            driverInfoARM.setEmail(driver.getEmail());
               if(driver.getTaxoparkPartners()!=null){
                   driverInfoARM.setTaxoparkPartnersInfo(ModelsUtils.toModel(driver.getTaxoparkPartners()));
               }
               if(driver.getAssistant()!=null){
                   driverInfoARM.setAssistantInfo(ModelsUtils.toModel(driver.getAssistant()));
               }
            driverInfoARM.setTimeOfRegistration(DateTimeUtils.toDate(driver.getAccount().getTimeCreated()));

            driverInfoARM.setDream(driver.getDream());
            driverInfoARM.setFamilyStatus(driver.getFamilyStatus());
            driverInfoARM.setGrowth(driver.getGrowth());
            driverInfoARM.setHobby(driver.getHobby());
            driverInfoARM.setChildrens(driver.isChildrens());
            driverInfoARM.setWeight(driver.getWeight());

            if(!CollectionUtils.isEmpty(driver.getDriverRequisites())){
                List<DriverRequisite> requisiteList = Lists.newArrayList(driver.getDriverRequisites());
                DriverRequisite requisite = requisiteList.get(0);
                driverInfoARM.setSalaryPriority(requisite.getSalaryPriority());
                driverInfoARM.setTypeSalary(requisite.getTypeSalary());
            }
            driverInfoARM.setTypeX(driver.isTypeX());
            if(driver.getState().equals(Driver.State.AVAILABLE)){
                driverInfoARM.setActive(true);
            } else{
                driverInfoARM.setActive(false);
            }
            if(driver.getTablet()!=null){
                driverInfoARM.setTabletInfo(ModelsUtils.toModel(driver.getTablet(), null));
            }
            if(driver.getRouter()!=null){
                driverInfoARM.setRouterInfo(ModelsUtils.toModel(driver.getRouter(), null));
            }
            if(driver.getDriverSetting() != null){
                driverInfoARM.setDriverSettingInfo(ModelsUtils.toModelDriverSettingInfo(driver.getDriverSetting()));
            }
        }
        return driverInfoARM;
    }


    // обрезанная версия DriverInfoARM для метода просмотра миссии
    public static DriverInfoARM toModelARM_Short(Driver driver) {
        DriverInfoARM driverInfoARM = new DriverInfoARM();
        if (driver != null) {
            driverInfoARM.setId(driver.getId());
            driverInfoARM.setFirstName(driver.getFirstName());
            driverInfoARM.setLastName(driver.getLastName());
            driverInfoARM.setAutoModel(driver.getAutoModel());
            driverInfoARM.setAutoClass(driver.getAutoClass().getValue());
            driverInfoARM.setAutoColor(driver.getAutoColor());
            driverInfoARM.setAutoNumber(driver.getAutoNumber());
            driverInfoARM.setAutoYear(driver.getAutoYear());
            driverInfoARM.setPhone(driver.getPhone());
            driverInfoARM.setBirthDate(driver.getBirthDate().toDate().getTime()/1000);
        }
        return driverInfoARM;
    }




/*
    public static DeviceInfoModel toModel(DeviceInfo deviceInfo, DeviceInfoModel deviceInfoModel) {
        deviceInfoModel.set
        deviceInfoModel.setToken(deviceInfoModel.getNewToken());
        deviceInfoModel.setType(DeviceInfo.Type.getDeviceType(deviceInfoModel.getDeviceType()));
        return deviceInfoModel;
    }
*/


    public static DeviceInfo fromModel(DeviceInfoModel deviceInfoModel, DeviceInfo deviceInfo) {
        deviceInfo.setToken(deviceInfoModel.getNewToken());
        deviceInfo.setType(DeviceInfo.Type.getDeviceType(deviceInfoModel.getDeviceType()));
        return deviceInfo;
    }



    public static Driver fromModel(DriverInfo driverInfo) {
        Driver driver = new Driver();
        driver.setFirstName(driverInfo.getFirstName());
        driver.setLastName(driverInfo.getLastName());
        driver.setAutoModel(driverInfo.getAutoModel());
        driver.setAutoClass(AutoClass.getByValue(driverInfo.getAutoClass()));
        driver.setAutoColor(driverInfo.getAutoColor());
        driver.setAutoNumber(driverInfo.getAutoNumber());
        driver.setAutoYear(driverInfo.getAutoYear());
        driver.setRating(driverInfo.getTotalRating());
        driver.setPhone(PhoneUtils.normalizeNumber(driverInfo.getPhone()));
        driver.setPassword(driverInfo.getPassword());
        if (driverInfo.getBirthDate() != 0){
            driver.setBirthDate(DateTimeUtils.toDateTime(driverInfo.getBirthDate()).toLocalDate());
        }
        driver.setVersionApp(driverInfo.getVersionApp());
        driver.setEntrepreneur(driverInfo.isEntrepreneur());
        driver.setWifi(driverInfo.isWifi());
        return driver;
    }




    public static Driver fromModelARM(DriverInfoARM driverInfoARM) {
        Driver driver = new Driver();
              driver.setFirstName(driverInfoARM.getFirstName());
              driver.setLastName(driverInfoARM.getLastName());
              driver.setAutoModel(driverInfoARM.getAutoModel());
              driver.setAutoClass(AutoClass.getByValue(driverInfoARM.getAutoClass()));
              driver.setAutoColor(driverInfoARM.getAutoColor());
              driver.setAutoNumber(driverInfoARM.getAutoNumber());
              driver.setAutoYear(driverInfoARM.getAutoYear());
              driver.setRating(driverInfoARM.getTotalRating());
              driver.setPhone(PhoneUtils.normalizeNumber(driverInfoARM.getPhone()));
              driver.setPassword(driverInfoARM.getPassword());
              if (driverInfoARM.getBirthDate() != 0) {
                  driver.setBirthDate(DateTimeUtils.toDateTime(driverInfoARM.getBirthDate()).toLocalDate());
              }
              driver.setVersionApp(driverInfoARM.getVersionApp());
              driver.setEntrepreneur(driverInfoARM.isEntrepreneur());
              driver.setWifi(driverInfoARM.isWifi());
              driver.setEmail(driverInfoARM.getEmail());

              driver.setDream(driverInfoARM.getDream());
              driver.setFamilyStatus(driverInfoARM.getFamilyStatus());
              driver.setGrowth(driverInfoARM.getGrowth());
              driver.setHobby(driverInfoARM.getHobby());
              driver.setChildrens(driverInfoARM.isChildrens());
              driver.setWeight(driverInfoARM.getWeight());
              driver.setTypeX(driverInfoARM.isTypeX());
        return driver;
    }


    public static Driver fromModelUpdateDriver(DriverInfoARM driverInfoARM, Driver driver) {
        driver.setLogin(driverInfoARM.getLogin());
        driver.setFirstName(driverInfoARM.getFirstName());
        driver.setLastName(driverInfoARM.getLastName());
        driver.setAutoModel(driverInfoARM.getAutoModel());
        driver.setAutoClass(AutoClass.getByValue(driverInfoARM.getAutoClass()));
        driver.setAutoColor(driverInfoARM.getAutoColor());
        driver.setAutoNumber(driverInfoARM.getAutoNumber());
        driver.setAutoYear(driverInfoARM.getAutoYear());
        //driver.setRating(driverInfo.getTotalRating());
        driver.setPhone(PhoneUtils.normalizeNumber(driverInfoARM.getPhone()));
        driver.setPassword(driverInfoARM.getPassword());
        driver.setEntrepreneur(driverInfoARM.isEntrepreneur());
        driver.setWifi(driverInfoARM.isWifi());
        driver.setEmail(driverInfoARM.getEmail());
        if (driverInfoARM.getBirthDate() != 0){
            driver.setBirthDate(DateTimeUtils.toDateTime(driverInfoARM.getBirthDate()*1000).toLocalDate());
        }
        driver.setDream(driverInfoARM.getDream());
        driver.setFamilyStatus(driverInfoARM.getFamilyStatus());
        driver.setGrowth(driverInfoARM.getGrowth());
        driver.setHobby(driverInfoARM.getHobby());
        driver.setChildrens(driverInfoARM.isChildrens());
        driver.setWeight(driverInfoARM.getWeight());
        driver.setTypeX(driverInfoARM.isTypeX());
        return driver;
    }





/*
     MissionAddressesInfo {
        private long id;
        private long missionId;
        private String address;
        private String latitude;
        private String longitude;
*/


    public static MissionAddresses fromModel(MissionAddressesInfo missionAddressesInfo, Mission mission) {
        // для создания миссии, сделать отдельно для обновления
        MissionAddresses missionAddresses = new MissionAddresses();
        Location location = new Location();
        location.setLatitude(missionAddressesInfo.getLatitude());
        location.setLongitude(missionAddressesInfo.getLongitude());
        location.setAddress(missionAddressesInfo.getAddress().replaceAll("[{}]", ""));
        missionAddresses.setLocation(location);
        missionAddresses.setMission(mission);
          return missionAddresses;
    }




    public static PrivateTariffInfo toModel(PrivateTariff privateTariff){
        PrivateTariffInfo info = new PrivateTariffInfo();
        info.setActivationDate(privateTariff.getActivationDate()!=null?privateTariff.getActivationDate().getMillis():0);
        info.setActive(privateTariff.getActive());
        info.setClientInfoARM(ModelsUtils.toModelClientInfoARM(privateTariff.getClient(), null));
        info.setExpirationDate(privateTariff.getExpirationDate() != null ? privateTariff.getExpirationDate().getMillis() : 0);
        info.setActivated(privateTariff.isActivated());
        info.setPromo(privateTariff.getPromoExclusive() != null ? privateTariff.getPromoExclusive().getPromoCode() : "");
        info.setTariffId(AutoClass.valueOf(privateTariff.getTariffName()).getValue());
        info.setFreeWaitMinutesByTariff(privateTariff.getFreeWaitMinutes());
        return info;
    }



    public static MissionAddressesInfo toModel(MissionAddresses missionAddresses) {
        MissionAddressesInfo result = new MissionAddressesInfo();

        Location location = missionAddresses.getLocation();
        if(location!=null){
            result.setAddress(location.getAddress());
            result.setLatitude(location.getLatitude());
            result.setLongitude(location.getLongitude());
        }
        result.setMissionId(missionAddresses.getMission().getId());
        result.setId(missionAddresses.getId());
        return result;
    }




    public static DriverLocks fromModel(DriverLocksInfo driverLocksInfo) {
        DriverLocks driverLocks = new DriverLocks();

        driverLocks.setDriverId(driverLocksInfo.getDriverId());
        driverLocks.setMissionId(driverLocksInfo.getMissionId());
        driverLocks.setReason(driverLocksInfo.getReason());
        driverLocks.setTimeOfLock(driverLocksInfo.getTimeOfLock());
        driverLocks.setTimeOfUnlock(driverLocksInfo.getTimeOfUnlock());

        return driverLocks;
    }



    public static ClientLocksInfo toModel(ClientLocks clientLocks) {
        ClientLocksInfo result = new ClientLocksInfo();
        if (clientLocks != null) {
            result.setId(clientLocks.getId());
            result.setTimeOfUnlock(clientLocks.getTimeOfUnlock());
            result.setTimeOfLock(clientLocks.getTimeOfLock());
            result.setReason(clientLocks.getReason());
            result.setClientId(clientLocks.getClientId());
        }
        return result;
    }


    public static DriverLocksInfo toModel(DriverLocks driverLocks) {
        DriverLocksInfo result = new DriverLocksInfo();
        if (driverLocks != null) {
            result.setId(driverLocks.getId());
            result.setTimeOfUnlock(driverLocks.getTimeOfUnlock());
            result.setTimeOfLock(driverLocks.getTimeOfLock());
            result.setReason(driverLocks.getReason());
            result.setMissionId(driverLocks.getMissionId());
            result.setDriverId(driverLocks.getDriverId());
        }
        return result;
    }


       /*
    1. Статус заказа

    4. Адрес отправления (с lat и lon)
    5. Адрес назначения (с lat и lon)
    6. Сумма заказа

    8. Комментарий +
    9. Тип заказа (CASH, CARD) +
        */


    public static Mission fromModelUpdateMission(MissionInfoARM missionInfoARM, Mission mission) {
       /*
       Добавить потом здесь:
       if(!missionAddressesInfoList.isEmpty()){
            for(MissionAddressesInfo missionAddressesInfo: missionAddressesInfoList){
                mission.getMissionAddresses().add(fromModel(missionAddressesInfo));
            }
        }
        */

      // убрать пока проверку на бронь, даты мы не меняем!!!!
//        if (missionInfo.getTimeOfStart() > 0) {
//            boolean booked =false;
//            DateTime now = DateTimeUtils.now();
//            DateTime timeOfStart = DateTimeUtils.toDateTime(missionInfo.getTimeOfStart());
//            Minutes minutesBetween = Minutes.minutesBetween(now, timeOfStart);
//            booked = minutesBetween.getMinutes() >= 60;
//            if (booked) {
//                mission.setState(Mission.State.BOOKED);
//                mission.setBookingState(Mission.BookingState.WAITING);
//            }
//        }

        mission.setComments(missionInfoARM.getComment());
        mission.setState(Mission.State.valueOf(missionInfoARM.getMissionState()));
        //mission.setAutoClass(AutoClass.getByValue(missionInfoARM.getAutoType()));

        //Location locationFrom = new Location(missionInfoARM.getAddressFrom(), missionInfoARM.getLocationFrom().getLatitude(), missionInfoARM.getLocationFrom().getLongitude(), missionInfoARM.getCityFrom(), missionInfoARM.getRegionFrom());
        //mission.setLocationFrom(locationFrom);


        //if (missionInfoARM.getLocationTo() != null) {
        //    Location locationTo = new Location(missionInfoARM.getAddressTo(), missionInfoARM.getLocationTo().getLatitude(), missionInfoARM.getLocationTo().getLongitude(), missionInfoARM.getCityTo(), missionInfoARM.getRegionTo());
        //    mission.setLocationTo(locationTo);
        //}

        mission.setPaymentType(PaymentType.getByValue(missionInfoARM.getPaymentType()));

        double expectedPrice = missionInfoARM.getExpectedPrice();
        mission.getStatistics().setPriceExpected(MoneyUtils.getRubles(expectedPrice));

        double price = missionInfoARM.getPrice();
        mission.getStatistics().setPriceInFact(MoneyUtils.getRubles(price));

        return mission;
    }



    /*
    ClientCardInfo {
    private Long id;
    private Long clientId;
    private String bindingId;
    private Boolean active;
    private String mdOrderNumber;
    private String cardholderName;
    private String pan;
    private int expiration;
     */




    public static ClientCardInfo toModel(ClientCard clientCard) {
        ClientCardInfo clientCardInfo = new ClientCardInfo();
            if(clientCard!=null){
                clientCardInfo.setCardholderName(clientCard.getCardholderName());
                clientCardInfo.setExpirationYear(clientCard.getExpirationYear());
                clientCardInfo.setExpirationMonth(clientCard.getExpirationMonth());
                clientCardInfo.setPan(clientCard.getPan());
                clientCardInfo.setActive(clientCard.getActive());
                clientCardInfo.setBindingId(clientCard.getBindingId());
                clientCardInfo.setClientId(clientCard.getClient().getId());
                clientCardInfo.setId(clientCard.getId());
                clientCardInfo.setMdOrderNumber(clientCard.getMdOrderNumber());
                clientCardInfo.setStatusDelete(clientCard.isStatusDelete());
                clientCardInfo.setMrchOrder(clientCard.getMrchOrder());
            }
        return clientCardInfo;
    }



    public static ClientCardInfoAndroid toModelAndroid(ClientCard clientCard, ClientCardInfoAndroid clientCardInfo) {
        if(clientCard!=null){
            clientCardInfo.setCardholderName(clientCard.getCardholderName());
            clientCardInfo.setExpirationYear(clientCard.getExpirationYear());
            clientCardInfo.setExpirationMonth(clientCard.getExpirationMonth());
            clientCardInfo.setPan(clientCard.getPan());
            clientCardInfo.setActive(clientCard.getActive());
            clientCardInfo.setBindingId(clientCard.getBindingId());
            clientCardInfo.setClientId(clientCard.getClient().getId());
            clientCardInfo.setId(clientCard.getId());
            clientCardInfo.setMdOrderNumber(clientCard.getMdOrderNumber());
            clientCardInfo.setStatusDelete(clientCard.isStatusDelete());
            clientCardInfo.setMrchOrder(clientCard.getMrchOrder());
        }
        return clientCardInfo;
    }




    public static ClientCard fromModel(ClientCardInfo clientCardInfo, ClientCard clientCard) {
        if(clientCardInfo!=null){
            clientCard.setCardholderName(clientCardInfo.getCardholderName());
            clientCard.setExpirationYear(clientCardInfo.getExpirationYear());
            clientCard.setExpirationMonth(clientCardInfo.getExpirationMonth());
            clientCard.setPan(clientCardInfo.getPan());
            clientCard.setActive(clientCardInfo.getActive());
            clientCard.setBindingId(clientCardInfo.getBindingId());
            clientCard.setId(clientCardInfo.getId());
            clientCard.setMdOrderNumber(clientCardInfo.getMdOrderNumber());
            clientCard.setStatusDelete(clientCardInfo.isStatusDelete());
            clientCard.setMrchOrder(clientCardInfo.getMrchOrder());
        }
        return clientCard;
    }






    public static Mission fromModel(MissionInfo missionInfo) {
        Mission mission = new Mission();
        mission.setComments(missionInfo.getComment());
        mission.setPorchNumber(missionInfo.getPorchNumber());

        for (Integer val : missionInfo.getOptions()) {
            MissionService option = MissionService.getByValue(val);
            if (!MissionService.UNKNOWN.equals(option)) {
                mission.getStatistics().getServicesExpected().add(option);
            }
        }

        LOGGER.warn("Address from: " + missionInfo.getAddressFrom(), missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude(), missionInfo.getCityFrom(), missionInfo.getRegionFrom());

        Location locationFrom = new Location(missionInfo.getAddressFrom(), missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude(), missionInfo.getCityFrom(), missionInfo.getRegionFrom());
        mission.setLocationFrom(locationFrom);

        if (missionInfo.getLocationTo() != null) {
            Location locationTo = new Location(missionInfo.getAddressTo(), missionInfo.getLocationTo().getLatitude(), missionInfo.getLocationTo().getLongitude(), missionInfo.getCityTo(), missionInfo.getRegionTo());
            LOGGER.warn("Address to: " + missionInfo.getAddressTo(), missionInfo.getLocationTo().getLatitude(), missionInfo.getLocationTo().getLongitude(), missionInfo.getCityTo(), missionInfo.getRegionTo());
            mission.setLocationTo(locationTo);
        }else{
            Location locationTo = new Location("", 0, 0, null, null);
            mission.setLocationTo(locationTo);
        }
        mission.setPaymentType(PaymentType.getByValue(missionInfo.getPaymentType()));
        mission.setAutoClass(AutoClass.getByValue(missionInfo.getAutoType()));
        double expectedPrice = missionInfo.getExpectedPrice();
        mission.getStatistics().setPriceExpected(MoneyUtils.getRubles(expectedPrice));
        mission.getStatistics().setDistanceExpected(missionInfo.getExpectedDistance());
        double price = missionInfo.getPrice();
        mission.getStatistics().setPriceInFact(MoneyUtils.getRubles(price));
        mission.setLateDriverBookedMin(missionInfo.getLateDriverBookedMin());
        double sumIncrease = missionInfo.getSumIncrease();
        mission.getStatistics().setSumIncrease(MoneyUtils.getRubles(sumIncrease));

        return mission;
    }




    public static Mission fromModel(MissionInfo missionInfo, Mission mission) {
        //mission.setPaymentStateCard(Mission.PaymentStateCard.valueOf(missionInfo.getPaymentStateCard()));

        mission.setComments(missionInfo.getComment());
        mission.setPorchNumber(missionInfo.getPorchNumber());

        for (Integer val : missionInfo.getOptions()) {
            MissionService option = MissionService.getByValue(val);
            if (!MissionService.UNKNOWN.equals(option)) {
                mission.getStatistics().getServicesExpected().add(option);
            }
        }

        Location locationFrom = new Location("", 0, 0, null, null);
        if (missionInfo.getLocationFrom() != null) {
            locationFrom = new Location(missionInfo.getAddressFrom().replaceAll("[{}]", ""), missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude(), missionInfo.getCityFrom(), missionInfo.getRegionFrom());
        }
        mission.setLocationFrom(locationFrom);

        Location locationTo = new Location("", 0, 0, null, null);
        if (missionInfo.getLocationTo() != null) {
            locationTo = new Location(missionInfo.getAddressTo().replaceAll("[{}]", ""), missionInfo.getLocationTo().getLatitude(), missionInfo.getLocationTo().getLongitude(), missionInfo.getCityTo(), missionInfo.getRegionTo());
        }
        mission.setLocationTo(locationTo);

        mission.setPaymentType(PaymentType.getByValue(missionInfo.getPaymentType()));
        mission.setAutoClass(AutoClass.getByValue(missionInfo.getAutoType()));
        double expectedPrice = missionInfo.getExpectedPrice();
        mission.getStatistics().setPriceExpected(MoneyUtils.getRubles(expectedPrice));
        mission.getStatistics().setDistanceExpected(missionInfo.getExpectedDistance());
        double price = missionInfo.getPrice();
        mission.getStatistics().setPriceInFact(MoneyUtils.getRubles(price));
        mission.setLateDriverBookedMin(missionInfo.getLateDriverBookedMin());
        double sumIncrease = missionInfo.getSumIncrease();
        mission.getStatistics().setSumIncrease(MoneyUtils.getRubles(sumIncrease));

        return mission;
    }



    public static Client fromModel(ClientInfoARM info){
        Client client = new Client();
        client.setEmail(info.getEmail());
        client.setFirstName(info.getFirstName());
        client.setLastName(info.getLastName());
        client.setPassword(info.getPassword());
        client.setPhone(info.getPhone());
        client.setDeviceType(DeviceInfo.Type.UNKNOWN.name());
        client.setCorporateLogin(info.getCorporateLogin());
        client.setCorporatePassword(info.getCorporatePassword());
        return client;
    }




    public static Client fromModelUpd(ClientInfoARM info, Client client){
        if(info.getEmail()!=null){
            client.setEmail(info.getEmail());
        }
        if(info.getFirstName()!=null){
            client.setFirstName(info.getFirstName());
        }
        if(info.getLastName()!=null){
            client.setLastName(info.getLastName());
        }
        if(info.getPhone()!=null){
            client.setPhone(info.getPhone());
        }
        if(info.getPassword()!=null){
            client.setPassword(info.getPassword());
        }
        if(info.getCorporateLogin()!=null){
            client.setCorporateLogin(info.getCorporateLogin());
        }
        if(info.getCorporatePassword()!=null){
            client.setCorporatePassword(info.getCorporatePassword());
        }
        return client;
    }



    public static Client fromModel(ClientInfo clientInfo, Client client) {
        Client.Gender gender = Client.Gender.UNDEFINED;
        if (clientInfo.getGender() != null) {
            gender = clientInfo.getGender() ? Client.Gender.MALE : Client.Gender.FEMALE;
        }
        client.setGender(gender);
        client.setCountry(clientInfo.getCountry());
        client.setCity(clientInfo.getCity());
        client.setPhone(PhoneUtils.normalizeNumber(clientInfo.getPhone()));
        client.setEmail(clientInfo.getEmail());
        client.setFirstName(clientInfo.getFirstName());
        if(clientInfo.getLastName() != null){
            client.setLastName(clientInfo.getLastName());
        }

        if (clientInfo.getBirthdayYear() > 0 && clientInfo.getBirthdayMonth() >= 0 && clientInfo.getBirthdayDay() > 0) {
            client.setBirthday(getLocalDate(clientInfo));
        }
        //client.setVersionApp(clientInfo.getVersionApp()); - убрал т.к. где-то происходит затирание
            return client;
    }




    public static Client fromModel(ClientInfo clientInfo) {
        Client client = new Client();
        Client.Gender gender = Client.Gender.UNDEFINED;
        if (clientInfo.getGender() != null) {
            gender = clientInfo.getGender() ? Client.Gender.MALE : Client.Gender.FEMALE;
        }
        client.setGender(gender);
        client.setCountry(clientInfo.getCountry());
        client.setCity(clientInfo.getCity());
        client.setPhone(PhoneUtils.normalizeNumber(clientInfo.getPhone()));
        client.setEmail(clientInfo.getEmail());
        client.setFirstName(clientInfo.getFirstName());
        if(clientInfo.getLastName() != null){
            client.setLastName(clientInfo.getLastName());
        }
        if (clientInfo.getBirthdayYear() > 0 && clientInfo.getBirthdayMonth() >= 0 && clientInfo.getBirthdayDay() > 0) {
            client.setBirthday(getLocalDate(clientInfo));
        }
        return client;
    }






    public static WebUser fromModel(WebUserModel webUserModel, WebUser webUser) {
        webUser.setEmail(webUserModel.getEmail());
        webUser.setRole(webUserModel.getRole());
        webUser.setLastName(webUserModel.getLastName());
        webUser.setFirstName(webUserModel.getFirstName());
        webUser.setPassword(webUserModel.getPassword());
        webUser.setCity(webUserModel.getCity());
        webUser.setPhone(webUserModel.getPhone());
        webUser.setPhoneOfManager(webUserModel.getPhoneOfManager());
        webUser.setCountry(webUserModel.getCountry());
        webUser.setSipUser(webUserModel.getSipUser());
        webUser.setSipPassword(webUserModel.getSipPassword());
        return webUser;
    }

    private static LocalDate getLocalDate(ClientInfo clientInfo) {
        return DateTimeUtils.getLocalDate(clientInfo.getBirthdayYear(), clientInfo.getBirthdayMonth(), clientInfo.getBirthdayDay());
    }




    public static LoggingEventInfoMongo toModel(LoggingEventInfoMongo result, LoggingEventMongo loggingEventMongo) {
            result.setDateTime(loggingEventMongo.getDateTime());
            result.setQueryType(loggingEventMongo.getQueryType());
            result.setSuccess(loggingEventMongo.isSuccess());
            result.setSecurity_token(loggingEventMongo.getSecurity_token());
            result.setMissionId(loggingEventMongo.getMissionId());
            result.setCity(loggingEventMongo.getCity());
            result.setId(loggingEventMongo.getId());
            result.setJsonQuery(loggingEventMongo.getJsonQuery());
            result.setErrorMessage(loggingEventMongo.getErrorMessage());
        return result;
    }



    private static List<LimitInfo> toModel(List<CorporateClientLimit> corporateClientLimits){
        List<LimitInfo> limitInfos = new ArrayList<>();
        if(corporateClientLimits!=null){
            for(CorporateClientLimit limit:corporateClientLimits){
                LimitInfo info = new LimitInfo();
                info.setLimitId(limit.getId());
                info.setMainClientId(limit.getClient().getMainClient()!=null?limit.getClient().getMainClient().getId():0);
                info.setClientId(limit.getClient().getId());
                info.setLimitAmount(limit.getLimitAmount());
                info.setTypePeriod(limit.getPeriod().getValue());
                limitInfos.add(info);
            }
        }
        return  limitInfos;
    }



    public static ClientInfoCorporate toModelCorporate(Client client, List<CorporateClientLimit> corporateClientLimits, boolean block) {
        ClientInfoCorporate result = new ClientInfoCorporate();
        if (client != null) {
            Client mainClient = client.getMainClient();
            result.setClientId(client.getId());
            result.setFirstName(client.getFirstName());
            result.setLastName(client.getLastName());
            result.setEmail(client.getEmail());
            if(mainClient!=null){
                result.setMainClientId(mainClient.getId());
                result.setFirstNameMain(mainClient.getFirstName());
                result.setLastNameMain(mainClient.getLastName());
            }
            result.setPhone(client.getPhone());
            result.setBlock(block);
            result.setLimitInfos(toModel(corporateClientLimits));

        }
        return result;
    }



    public static ClientInfoCorporateARM toModelClientInfoCorporateARM(Client client, List<CorporateClientLimit> corporateClientLimits, boolean block) {
        ClientInfoCorporateARM result = new ClientInfoCorporateARM();
        if (client != null) {
            result.setClientId(client.getId());
            result.setFirstName(client.getFirstName());
            result.setLastName(client.getLastName());
            result.setEmail(client.getEmail());
            result.setMainClientId(client.getMainClient() !=null ? client.getMainClient().getId(): null);
            result.setPhone(client.getPhone());
            result.setBlock(block);
            result.setMonthLimit(toModelLimitInfo(corporateClientLimits, 1));
            result.setWeekLimit(toModelLimitInfo(corporateClientLimits, 2));
        }
        return result;
    }



    private static LimitInfo toModelLimitInfo(List<CorporateClientLimit> corporateClientLimits, int type){
        LimitInfo info = null;
        if(corporateClientLimits!=null){
            for(CorporateClientLimit limit:corporateClientLimits){
                if(limit.getPeriod().getValue()==type){
                    info = new LimitInfo();
                    info.setLimitId(limit.getId());
                    info.setMainClientId(limit.getClient().getMainClient() != null ? limit.getClient().getMainClient().getId():0);
                    info.setClientId(limit.getClient().getId());
                    info.setLimitAmount(limit.getLimitAmount());
                    info.setTypePeriod(limit.getPeriod().getValue());
                    break;
                }
            }
        }
        return  info;
    }





    public static ClientInfo toModelClientInfoLite(Client client) {
        ClientInfo result = new ClientInfo();
        if (client != null) {
            if (client.getGender() != Client.Gender.UNDEFINED) {
                result.setGender(client.getGender() == Client.Gender.MALE);
            }
            result.setId(client.getId());
            result.setCountry(client.getCountry());
            result.setCity(client.getCity());
            result.setPhone(client.getPhone());
            result.setEmail(client.getEmail());
            result.setFirstName(client.getFirstName());
            result.setLastName(client.getLastName());
            result.setPicureUrl(client.getPicureUrl());
            if (client.getBirthday() != null) {
                LocalDate date = client.getBirthday().toDateTimeAtStartOfDay().toLocalDate();
                result.setBirthdayYear(date.getYear());
                result.setBirthdayMonth(date.getMonthOfYear());
                result.setBirthdayDay(date.getDayOfMonth());
            }
            result.setVersionApp(client.getVersionApp());
        }
        return result;
    }





    public static ClientInfo toModel(Client client) {
        ClientInfo result = new ClientInfo();
        if (client != null) {
            if (client.getGender() != Client.Gender.UNDEFINED) {
                result.setGender(client.getGender() == Client.Gender.MALE);
            }
            result.setId(client.getId());
            result.setCountry(client.getCountry());
            result.setCity(client.getCity());
            result.setPhone(client.getPhone());
            result.setEmail(client.getEmail());
            result.setFirstName(client.getFirstName());
            result.setLastName(client.getLastName());
            result.setLastName(client.getLastName());
            result.setPicureUrl(client.getPicureUrl());
            result.setBonuses(client.getAccount().getBonuses().getAmount().doubleValue());
            result.setAmount(0); // client.getAccount().getMoney().getAmount().doubleValue()
            result.setAdministrativeState(client.getAdministrativeState().getValue());
            if (client.getRegistrationTime() != null){
                result.setRegistrationDate(client.getRegistrationTime().getMillis()/1000);
            }
            if (client.getBlockingTime() != null){
                result.setBlockingDate(client.getBlockingTime().getMillis());
            }
            if (client.getBirthday() != null) {
                LocalDate date = client.getBirthday().toDateTimeAtStartOfDay().toLocalDate();
                result.setBirthdayYear(date.getYear());
                result.setBirthdayMonth(date.getMonthOfYear());
                result.setBirthdayDay(date.getDayOfMonth());
            }
            result.setVersionApp(client.getVersionApp());
        }
        return result;
    }




    public static ClientInfoARM toModelClientInfoARM(Client client, List<CorporateClientLimit> limits) {
        ClientInfoARM result = new ClientInfoARM();
        if (client != null) {
            if (client.getGender() != Client.Gender.UNDEFINED) {
                result.setGender(client.getGender() == Client.Gender.MALE);
            }
            result.setId(client.getId());
            result.setCountry(client.getCountry());
            result.setCity(client.getCity());
            result.setPhone(client.getPhone());
            result.setEmail(client.getEmail());
            result.setFirstName(client.getFirstName());
            result.setLastName(client.getLastName());
            result.setLastName(client.getLastName());
            result.setPicureUrl(client.getPicureUrl());
            result.setBonuses(client.getAccount().getBonuses().getAmount().doubleValue());
            result.setAmount(0); // client.getAccount().getMoney().getAmount().doubleValue()
            result.setAdministrativeState(client.getAdministrativeState().getValue());
            if (client.getRegistrationTime() != null){
                result.setRegistrationDate(client.getRegistrationTime().getMillis() / 1000);
            }
            if (client.getBlockingTime() != null){
                result.setBlockingDate(client.getBlockingTime().getMillis());
            }
            if (client.getBirthday() != null) {
                LocalDate date = client.getBirthday().toDateTimeAtStartOfDay().toLocalDate();
                result.setBirthdayYear(date.getYear());
                result.setBirthdayMonth(date.getMonthOfYear());
                result.setBirthdayDay(date.getDayOfMonth());
            }
            result.setVersionApp(client.getVersionApp());
            if(client.getMainClient()!=null){
                result.setMainClientId(client.getMainClient().getId());
                 if(client.getId().equals(client.getMainClient().getId())){
                     result.setRoot(true);
                 }
            }
            if(client.getAccount().getCorporateBalance()!=null){
                result.setCorporateBalance(client.getAccount().getCorporateBalance().getAmount().intValue());
            }
            if(!CollectionUtils.isEmpty(limits)){
                result.setMonthLimit(toModelLimitInfo(limits, 1));
                result.setWeekLimit(toModelLimitInfo(limits, 2));
            }
            result.setCorporateLogin(client.getCorporateLogin());
            result.setCorporatePassword(client.getCorporatePassword());
            result.setCourierActivated(client.isCourierActivated());
        }
        return result;
    }



    public static RatingItem buildRatingItem(long totalPositions, long driverPosition, long driverId, String firstName, String lastName, double rating) {
        RatingItem ratingItem = new RatingItem();
        ratingItem.setDriverId(driverId);
        ratingItem.setFirstName(firstName);
        ratingItem.setLastName(lastName);
        ratingItem.setDriverRating(rating);
        ratingItem.setPosition(0);
        ratingItem.setTotalPositions(totalPositions);
        ratingItem.setPosition(driverPosition);
        return ratingItem;
    }



    public static WebUserModel toModel(WebUser user, List<ru.trendtech.domain.courier.Order> currentOrder) {
        WebUserModel result = new WebUserModel();
        result.setId(user.getId());
        result.setPhone(user.getPhone());
        result.setCity(user.getCity());
        result.setCountry(user.getCountry());
        result.setEmail(user.getEmail());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setPhoneOfManager(user.getPhoneOfManager());
        result.setRole(user.getRole());
        result.setSipPassword(user.getSipPassword());
        result.setSipUser(user.getSipUser());
        if(user.getAssistant()!=null) {
            result.setAssistantInfo(ModelsUtils.toModel(user.getAssistant()));
        }
        result.setTaxoparkId(user.getTaxoparkId());
        result.setWaPhone(user.getWaPhone());
        result.setWaPassword(user.getWaPassword());
        if(!CollectionUtils.isEmpty(currentOrder)){
            for(Order order : currentOrder){
                 result.getOrderInfos().add(ModelsUtils.toModel(order, 0));
            }
        }
        return result;
    }




    public static WebUserInfo toModelWebUserInfo(WebUser user) {
        WebUserInfo info = new WebUserInfo();
        info.setId(user.getId());
        info.setFirstName(user.getFirstName());
        info.setLastName(user.getLastName());
        return info;
    }



    public static ArrayList<ServicePriceInfo> toModel(Iterable<ServicePrice> services, Boolean isNewVersionAppClient) {
        ArrayList<ServicePriceInfo> servicePriceInfos = new ArrayList<>();
        for(ServicePrice servicePrice : services){
            // было: MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount()
            ServicePriceInfo servicePriceInfo = new ServicePriceInfo(MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount(), servicePrice.getActivePicUrl(),servicePrice.getNotActivePicUrl(),servicePrice.getDescription(),servicePrice.getName());;
            /* Добавил это ограничение чтобы  услуга кондиционер не отображалась на клиенте! */

            if(isNewVersionAppClient!=null) {
                if (isNewVersionAppClient) {
                    //if(!servicePrice.getService().name().equals(MissionService.SMALL_ANIMAL.name())){
                    servicePriceInfo = new ServicePriceInfo(MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount(), servicePrice.getActivePicUrlV2(), "", servicePrice.getDescriptionV2(), servicePrice.getNameV2());
                    //}
                    //}else{
                    //if(!servicePrice.getService().name().equals(MissionService.CONDITIONER.name()) && !servicePrice.getService().name().equals(MissionService.SMALL_CHILDREN.name())){
                    // servicePriceInfo = new ServicePriceInfo(MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount(), servicePrice.getActivePicUrl(),servicePrice.getNotActivePicUrl(),servicePrice.getDescription(),servicePrice.getName());
                    //}
                    //}
                }// else {
                // servicePriceInfo = new ServicePriceInfo(MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount(), servicePrice.getActivePicUrl(),servicePrice.getNotActivePicUrl(),servicePrice.getDescription(),servicePrice.getName());
                //}
            } else{
                servicePriceInfo = new ServicePriceInfo(MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount(), servicePrice.getActivePicUrlV2(), "", servicePrice.getDescriptionV2(), servicePrice.getNameV2());
            }
                    //if(servicePriceInfo!=null) {
                       servicePriceInfos.add(servicePriceInfo);
                    //}

        }
        return servicePriceInfos;
    }



//    public static ArrayList<ServicePriceInfoV2> toModelServicePricesV2(Iterable<ServicePrice> services, boolean isNewVersionAppClient) {
//        ArrayList<ServicePriceInfoV2> servicePriceInfosV2 = new ArrayList<>();
//        for(ServicePrice servicePrice : services){
//            /* Добавил это ограничение чтобы  услуга кондиционер не отображалась на клиенте! */
//            if(!servicePrice.getService().name().equals(MissionService.CONDITIONER.name())){
//                //int optionId, String service, double price, String activePicUrl, String notActivePicUrl, String description, String name, String activePicUrlV2, String descriptionV2
//                ServicePriceInfoV2 servicePriceInfo = null;
//                if(isNewVersionAppClient){
//                    servicePriceInfo = new ServicePriceInfoV2(servicePrice.getService().getValue(), servicePrice.getService().name(), (int)servicePrice.getMoney_amount(), servicePrice.getActivePicUrl(),servicePrice.getNotActivePicUrl(),servicePrice.getDescription(),servicePrice.getName(),servicePrice.getActivePicUrlV2(), servicePrice.getDescriptionV2());
//                }else{
//                    servicePriceInfo = new ServicePriceInfo(MissionService.getValueStr(servicePrice.getService()), servicePrice.getMoney_amount(), servicePrice.getActivePicUrl(),servicePrice.getNotActivePicUrl(),servicePrice.getDescription(),servicePrice.getName());
//                }
//                servicePriceInfosV2.add(servicePriceInfo);
//            }
//        }
//        return servicePriceInfosV2;
//    }

}
