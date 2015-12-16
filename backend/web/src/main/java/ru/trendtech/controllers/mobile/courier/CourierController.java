package ru.trendtech.controllers.mobile.courier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.GetOrderRequest;
import ru.trendtech.common.mobileexchange.model.common.GetOrderResponse;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderRequest;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderResponse;
import ru.trendtech.common.mobileexchange.model.courier.client.OrderHistoryRequest;
import ru.trendtech.common.mobileexchange.model.courier.client.OrderHistoryResponse;
import ru.trendtech.common.mobileexchange.model.courier.driver.*;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.driver.CourierService;

import java.util.EnumSet;

/**
 * Created by petr on 10.09.2015.
 */

@Controller
@RequestMapping("/courier")
public class CourierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourierController.class);
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private CourierService courierService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderService orderService;




    @RequestMapping(value = "/updateTargetAddressState", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateTargetAddressStateResponse updateTargetAddressState(@RequestBody UpdateTargetAddressStateRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return orderService.updateTargetAddressState(request.getTargetAddressId(), request.getTargetAddressState());
    }




    @RequestMapping(value = "/prepaireComplete", method = RequestMethod.POST)
    public
    @ResponseBody
    PrepaireCompleteOrderResponse prepaireComplete(@RequestBody PrepaireCompleteOrderRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return courierService.prepaireComplete(driver, request.getOrderId());
    }




    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    CompleteOrderResponse completeOrder(@RequestBody CompleteOrderRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return orderService.completeOrder(request.getOrderId());
    }




    @RequestMapping(value = "/completeOrderCard", method = RequestMethod.POST)
    public
    @ResponseBody
    CompleteOrderResponse completeOrderCard(@RequestBody CompleteOrderRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return orderService.completeOrderCard(request.getOrderId());
    }





    @RequestMapping(value = "/configuration", method = RequestMethod.POST)
    public
    @ResponseBody
    CourierConfigurationResponse configuration(@RequestBody CourierConfigurationRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        LOGGER.info("/courier/configuration/ token = " + request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return courierService.configuration(driver);
    }




    @RequestMapping(value = "/remindConfirm", method = RequestMethod.POST)
    public
    @ResponseBody
    RemindConfirmResponse remindConfirm(@RequestBody RemindConfirmRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
            return courierService.remindConfirm(order);
    }



    // todo: готов выполнять заказ - поехал по магазинам или в "откуда забрать"
    @RequestMapping(value = "/readyToProgress", method = RequestMethod.POST)
    public
    @ResponseBody
    ReadyToProgressResponse readyToProgress(@RequestBody ReadyToProgressRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return courierService.readyToProgress(request.getOrderId(), driver);
    }




    // todo: все забрал (купил) и едет к клиенту
    @RequestMapping(value = "/readyToGo", method = RequestMethod.POST)
    public
    @ResponseBody
    ReadyToGoResponse readyToGo(@RequestBody ReadyToGoRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
            return courierService.readyToGo(request.getOrderId(), driver);
    }




    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public
    @ResponseBody
    CourierLocationResponse setLocation(@RequestBody CourierLocationRequest request) {
        CourierLocationResponse response = new CourierLocationResponse();
        courierService.saveCourierLocation(request.getRequesterId(), request.getLocation(), request.getDistance());
           return response;
    }




    @RequestMapping(value = "/lateOrder", method = RequestMethod.POST)
    @ResponseBody
    public LateOrderResponse lateOrder(@RequestBody LateOrderRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен или завершен");
        }
        if(order.getDriver()!= null && order.getDriver().getId() != driver.getId()){
            throw new CustomException(4, "Нарушение безопасности");
        }
            return courierService.lateOrder(order, request.getMinutesLate());
    }






    @RequestMapping(value = "/takeOrder", method = RequestMethod.POST)
    @ResponseBody
    public TakeOrderResponse takeOrder(@RequestBody TakeOrderRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
        if(driver.getCurrentOrder() != null){
            throw new CustomException(2, "У вас есть текущий заказ");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(3, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен, завершен или не подтвержден клиентом");
        }
        if(!order.getState().equals(Order.State.CONFIRMED)){
            if(order.getDriver() != null){
                if(!order.getDriver().getId().equals(driver.getId())){
                    throw new CustomException(4, "Заказ не подтвержден или взят другим водителем");
                } else {
                    throw new CustomException(5, "Вы уже выполняете данный заказ");
                }
            }
        }
            return courierService.takeOrder(order, driver);
    }




    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public GetOrderResponse getOrder(@RequestBody GetOrderRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Водитель не найден");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
            return commonService.getOrder(order);
    }





    @RequestMapping(value = "/orderHistory", method = RequestMethod.POST)
    @ResponseBody
    public OrderHistoryResponse orderHistory(@RequestBody OrderHistoryRequest request) {
        Driver driver = driverRepository.findByToken(request.getSecurity_token());
        if(driver == null) {
            throw new CustomException(1, "Курьер не найден");
        }
            return courierService.orderHistory(driver);
    }
}
