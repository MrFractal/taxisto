package ru.trendtech.services.watchers;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.SMSMessage;
import ru.trendtech.repositories.SMSMessageRepository;
import ru.trendtech.services.sms.SMSC;
import ru.trendtech.services.sms.SMSaero;
import ru.trendtech.utils.DateTimeUtils;

import java.io.IOException;
import java.util.List;


/**
 * File created by max on 07/05/2014 19:39.
 */

@Service("smsWatcher")
public class SMSWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSWatcher.class);
    @Autowired
    private SMSMessageRepository smsMessageRepository;
    @Autowired
    private SMSC smsc;
    @Autowired
    private SMSaero smsAero;



    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkCanceled() throws IOException, JSONException {
         //smsSentCheck();
    }





    private void smsSentCheck() throws IOException, JSONException {
        /*
           0 - отправлено
           -1 - не доставлено
           time - время доставки (когда все прошло успешно)
           -2 - смс осталась в статусе ожидания
         */
       List<SMSMessage> smsMessageList = smsMessageRepository.findByTimeOfDeliveryIn(0);

        for(SMSMessage smsMessage :smsMessageList){
            DateTime timeOfSend = smsMessage.getTimeOfCreate();
            Seconds seconds = Seconds.secondsBetween(timeOfSend, DateTimeUtils.nowNovosib_GMT6());
            int sec = Math.abs(seconds.getSeconds());

            // проверяем статус сообщения
               if(smsMessage.getCountTry()==1){ // sec<=90 &&
                   smsMessage = smsc.smsStatusSendSMSC(smsMessage);
               }else if(smsMessage.getCountTry()==2){ // sec>90 && sec<=180 // sec>90 && sec<=360 &&
                   smsMessage = smsAero.statusSMS_Aero(smsMessage);
               }
               if(sec>90 && smsMessage.getTimeOfDelivery()==0 && smsMessage.getCountTry()==1){
                    smsAero.sendBySMSBAero(smsMessage);
               }

                       /*
-3	Сообщение не найдено	Возникает при множественном запросе статусов, если для указанного номера телефона и ID сообщение не найдено.
-1	Ожидает отправки	Если при отправке сообщения было задано время получения абонентом, то до этого времени сообщение будет находиться в данном статусе, в других случаях сообщение в этом статусе находится непродолжительное время перед отправкой на SMS-центр.
0	Передано оператору	Сообщение было передано на SMS-центр оператора для доставки.
1	Доставлено	Сообщение было успешно доставлено абоненту.
3	Просрочено	Возникает, если время "жизни" сообщения истекло, а оно так и не было доставлено получателю, например, если абонент не был доступен в течение определенного времени или в его телефоне был переполнен буфер сообщений.
20	Невозможно доставить	Попытка доставить сообщение закончилась неудачно, это может быть вызвано разными причинами, например, абонент заблокирован, не существует, находится в роуминге без поддержки обмена SMS, или на его телефоне не поддерживается прием SMS-сообщений.
22	Неверный номер	Неправильный формат номера телефона.
23	Запрещено	Возникает при срабатывании ограничений на отправку дублей, на частые сообщения на один номер (флуд), на номера из черного списка, на запрещенные спам фильтром тексты или имена отправителей (Sender ID).
24	Недостаточно средств	На счете Клиента недостаточная сумма для отправки сообщения.
25	Недоступный номер	Телефонный номер не принимает SMS-сообщения, или на этого оператора нет рабочего маршрута.
                       */

                       /*
                      err
1	Ошибка в параметрах.
2	Неверный логин или пароль.
3	Сообщение не найдено. При множественном запросе для данной ошибки возвращается статус с кодом "-3".
4	IP-адрес временно заблокирован.
9	Попытка отправки более пяти запросов на получение статуса одного и того же сообщения в течение минуты.
                       */
       }
    }
}

