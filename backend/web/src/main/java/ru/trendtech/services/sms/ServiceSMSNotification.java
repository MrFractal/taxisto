package ru.trendtech.services.sms;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.SMSMessage;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.services.sms.impl.SMSCGatewaySMS;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.HTTPUtil;
import ru.trendtech.utils.PhoneUtils;

import java.io.IOException;

/**
 * Created by ivanenok on 5/9/2014.
 */
@Service
public class ServiceSMSNotification {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSMSNotification.class);

    private static final String SPECIAL_PHONE_NUMBER = "+79130000000";

    @Value("${sms.active}")
    private boolean useSMSGate = false;

    @Autowired
    private SMSCGatewaySMS gatewaySMS;

    @Autowired
    private SMSaero smsAero;

    @Autowired
    private ClientRepository clientRepository;


    public boolean registrationConfirm(String phone, String code, String messageId) {
        return sendCustomSMS(phone, "Код подтверждения вашего аккаунта Таксисто: " + code + ".", messageId);
    }

    public boolean terminalStartMission(String phone, String code, String messageId) {
        return sendCustomSMS(phone, "Код подтверждения Таксисто: " + code + ".", messageId);
    }

    public boolean repeateSmsCodeTerminal(String phone, String code, String messageId) {
        return sendCustomSMS(phone, "Повторный код подтверждения Таксисто: " + code + ".", messageId);
    }


//Здравствуйте, Петр! Ваш таксисто: Белый Volkswagen polo в031кв54 - подъ
    public boolean cancelBookedMission(String phone, long missionId, String messageId) {
        return sendCustomSMS(phone, "Извините, Ваша бронь не подтверждена, №"+missionId, messageId);
    }

    public boolean cancelMission(String phone, long missionId, String messageId) {
        return sendCustomSMS(phone, "Извините, мы не нашли водителя на Ваш заказ, №"+missionId, messageId);
    }

//    public boolean cancelAssignMission(String phone) {
//        return sendCustomSMS(phone, "Извините, Ваша бронь не подтверждена");
//    }


    public boolean passwordRecovery(String phone, String smsCode, String messageId) {
        return sendCustomSMS(phone, "Код для подтверждения смены пароля в Таксисто: " + smsCode + ".", messageId);
    }


    public boolean temporaryPassword(String phone, String smsCode, String messageId) {
        return sendCustomSMS(phone, "Ваш временный пароль: "+smsCode+". Смените его пожалуйста при первой возможности!", messageId);
    }


    public boolean phoneDriverUpdate(String phone, String smsCode, String messageId) {
        return sendCustomSMS(phone, "Код для подтверждения смены номера телефона в Таксисто: " + smsCode + ".", messageId);
    }


    public boolean serviceSuccessfullyActivated(String phone, String message){
        return sendCustomSMS(phone, message, "");
    }


    public boolean paymentInfoShort(String phone, PaymentInfo paymentInfo, String messageId) {
        double totalPrice = paymentInfo.getTotalPrice();
        String message = "formatted custom payment message";
        return sendCustomSMS(phone, message, messageId);
    }

    public boolean paymentInfoFull(String phone, PaymentInfo paymentInfo, String messageId) {
        double totalPrice = paymentInfo.getTotalPrice();
        String message = "formatted custom payment message";
        return sendCustomSMS(phone, message, messageId);
    }


    //Ваш заказ № 1111. Ваш Таксисто: Темно-коричневый Lada Largus е103вн54. Водитель: Вадим +79231931303


    public boolean smsMissionDetailsInfo(String phone, DriverInfo driverInfo, Mission mission, String messageId) {
        String message = String.format(
                "Ваш заказ № %s! Ваш Таксисто: %s %s %s. Водитель: %s %s",
                mission.getId(),
                driverInfo.getAutoColor(),
                driverInfo.getAutoModel(),
                driverInfo.getAutoNumber(),
                driverInfo.getFirstName(),
                driverInfo.getPhone());
        return sendCustomSMS(phone, message, messageId);
    }


    public boolean driverArrived(Client client, Driver driverInfo, String messageId, int cost) {
        String message = String.format(
                "Здравствуйте, %s! Ваш Таксисто: %s %s %s - подъехал. Водитель: %s %s Стоимость: %s руб",
                client.getFirstName(),
                driverInfo.getAutoColor(),
                driverInfo.getAutoModel(),
                driverInfo.getAutoNumber(),
                driverInfo.getFirstName(),
                driverInfo.getPhone(),
                cost);
        return sendCustomSMS(client.getPhone(), message, messageId);
    }


    public boolean driverBookedArrived(Client client, Driver driverInfo, String messageId) {
        String message = String.format(
                "Здравствуйте, %s! Ваш Таксисто подъехал и ждет вас в назначенное время. Водитель: %s %s, машина:",
                client.getFirstName(),
                driverInfo.getFirstName(),
                driverInfo.getPhone(),
                driverInfo.getAutoColor(),
                driverInfo.getAutoModel(),
                driverInfo.getAutoNumber());
        return sendCustomSMS(client.getPhone(), message, messageId);
    }


    //"Здравствуйте! Вам назначен Таксисто: "+d_auto_color+" "+d_auto_model+" "+d_auto_number+". Водитель: "+d_name+" "+d_phone+". Номер заказа: "+missionId;

    public boolean driverAssigned(Client client, Driver driverInfo, String messageId) {
        String message = String.format(
                "Здравствуйте, %s! Вам назначен Таксисто: %s %s %s. Водитель: %s %s. Номер заказа: %s",
                client.getFirstName(),
                driverInfo.getAutoColor(),
                driverInfo.getAutoModel(),
                driverInfo.getAutoNumber(),
                driverInfo.getFirstName(),
                driverInfo.getPhone(),
                driverInfo.getCurrentMission().getId());
        return sendCustomSMS(client.getPhone(), message, messageId);
    }




    public boolean courierAssigned(Client client, Driver driverInfo, String orderId) {
        /*
        String message = String.format(
                "Здравствуйте, %s! Вам назначен Курьер: %s %s. Номер заказа: %s",
                client.getFirstName(),
                driverInfo.getFirstName(),
                driverInfo.getPhone(),
                orderId);
        */
        String message = "На Ваш заказ назначен курьер. Откройте приложение, чтобы посмотреть данные о курьере.";
        return sendCustomSMS(client.getPhone(), message, orderId);
    }



    public boolean driverBookedAssigned(Client client) {
        String message = String.format(
                "Ваша бронь подтверждена. Откройте приложение, чтобы посмотреть данные о Таксисто.");
        return sendCustomSMS(client.getPhone(), message, null);
    }


    public boolean courierOrderConfirm(Client client){
        String message = String.format("Ваш заказ принят. Чтобы мы начали выполнять ваш заказ, подтвердите его в разделе Zavezu \"Текущие заказы\".");
        return sendCustomSMS(client.getPhone(), message, "");
    }


    public boolean driverWaitingTooLong(String phone, Driver driverInfo, String messageId) {
        String message = String.format(
                "Здравствуйте, %s! Извините за беспокойство, это Ваш Таксисто. Я подъехал. Жду Вас.",
                driverInfo.getFirstName());
        return sendCustomSMS(phone, message, messageId);
    }

    public boolean inviteFriend(String phone) {
        return sendCustomSMS(phone, "Привет! Я использую приложение Таксисто, отличный способ заказа такси. Для установки приложения нажми на ссылку http://taxisto.ru", "");
    }


    public boolean missionTransfer(String phone, String messageId) {
        return sendCustomSMS(phone, "Ваш заказ переведен на другого водителя", messageId);
    }




    public boolean sendCustomSMS(String phone, String message, String messageId) {
        /* сюда впилить отправку смс через get */
        boolean result = useSMSGate;
        String number = PhoneUtils.normalizeNumber(phone);

        if (result) {
            try {
                gatewaySMS.send(number, message, messageId);
            } catch(Exception ex){
                Client client = clientRepository.findByPhone(number);
                SMSMessage sms = new SMSMessage();
                sms.setSmsText(message);
                sms.setClient(client);
                sms.setCountTry(1);
                sms.setTimeOfCreate(DateTimeUtils.nowNovosib_GMT6());
                try {
                    smsAero.sendBySMSBAero(sms);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }




    public void sendSMS(String phone, TypeOfSMS typeOfSMS, String messageId) {
        switch (typeOfSMS) {
            case DRIVER_WAIT:
                sendCustomSMS(phone, "Вас ожидает таксист!", messageId);
                break;
            case DRIVER_WAIT_TOO_LONG:
                sendCustomSMS(phone, "Вас ожидает таксист уже очень давно!", messageId);
                break;
            case DRIVER_NOT_FOUND:
                sendCustomSMS(phone, "Клиент не может вас найти!", messageId);
                break;
            default:
                LOGGER.error("Unknown: SMS kind");
                break;
        }
    }
}
