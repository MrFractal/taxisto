package ru.trendtech.services.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.trendtech.domain.SMSMessage;
import ru.trendtech.repositories.SMSMessageRepository;
import ru.trendtech.utils.DateTimeUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by petr on 09.10.2014.
 */
@Service
public class SMSC {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSC.class);

    @Value("${smsc.username}")
    private String login;

    @Value("${smsc.password}")
    private String password;

    @Value("${smsc.debug}")
    private boolean debug = false;

    private static final String SMSC_CHARSET = "utf-8";       // кодировка сообщения: koi8-r, windows-1251 или utf-8 (по умолчанию)

    @Autowired
    SMSMessageRepository smsMessageRepository;

    //private boolean debug = true;         // флаг отладки

    //private String login;

    //private String password;

    public SMSC() {
    }

    public SMSC(String login, String password) {
        this(login, password, false);
    }

    public SMSC(String login, String password, boolean debug) {
        this.login = login;
        this.password = password;
        this.debug = debug;
    }


    public SMSMessage smsStatusSendSMSC(SMSMessage smsMessage){
        String[] statusStr = status(smsMessage.getId(), smsMessage.getClient().getPhone(), 0);

           //for(String str :statusStr){
           //    LOGGER.info("STATUS SMSC = "+str);
           //}

        if (!statusStr[1].equals("") && Integer.parseInt(statusStr[1]) >= 0) {
            // отправлено
            smsMessage.setErrorMessageSMS_C("Сообщение доставлено");
            smsMessage.setDeliveryBy("smsc");
            long timeDelivery = Long.parseLong(statusStr[1]);
            smsMessage.setTimeOfDelivery(timeDelivery*1000);
            smsMessageRepository.save(smsMessage);
        } else {
            switch(Integer.parseInt(statusStr[1])){
                case -3:{
                    smsMessage.setErrorMessageSMS_C("Сообщение не найдено");
                    //smsMessage.setTimeOfDelivery(-1);
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case -1:{
                    smsMessage.setErrorMessageSMS_C("Ожидает отправки");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 0:{
                    smsMessage.setErrorMessageSMS_C("Передано оператору");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 1:{
                    smsMessage.setErrorMessageSMS_C("Доставлено");
                    smsMessage.setDeliveryBy("smsc");
                    smsMessage.setTimeOfDelivery(DateTimeUtils.nowNovosib().getMillis());
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 3:{
                    smsMessage.setErrorMessageSMS_C("Просрочено");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 20:{
                    smsMessage.setErrorMessageSMS_C("Невозможно доставить");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 22:{
                    smsMessage.setErrorMessageSMS_C("Неверный номер");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 23:{
                    smsMessage.setErrorMessageSMS_C("Запрещено");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 24:{
                    smsMessage.setErrorMessageSMS_C("Недостаточно средств");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                case 25:{
                    smsMessage.setErrorMessageSMS_C("Недоступный номер");
                    smsMessageRepository.save(smsMessage);
                    break;
                }
                default: break;
            }
        }

        return smsMessage;
    }




    private static String _implode(String[] ary, String delim) {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < ary.length; i++) {
            if (i != 0) {
                builder.append(delim);
            }
            builder.append(ary[i]);
        }
        return builder.toString();
    }

    /**
     * Отправка SMS
     *
     * @param phones   - список телефонов через запятую или точку с запятой
     * @param message  - отправляемое сообщение
     * @param translit - переводить или нет в транслит (1,2 или 0)
     * @param time     - необходимое время доставки в виде строки (DDMMYYhhmm, h1-h2, 0ts, +m)
     * @param id       - идентификатор сообщения. Представляет собой 32-битное число в диапазоне от 1 до 2147483647.
     * @param format   - формат сообщения (0 - обычное sms, 1 - flash-sms, 2 - wap-push, 3 - hlr, 4 - bin, 5 - bin-hex, 6 - ping-sms)
     * @param sender   - имя отправителя (Sender ID). Для отключения Sender ID по умолчанию необходимо в качестве имени передать пустую строку или точку.
     * @param query    - строка дополнительных параметров, добавляемая в URL-запрос ("valid=01:00&maxsms=3&tz=2")
     * @return array (<id>, <количество sms>, <стоимость>, <баланс>) в случае успешной отправки
     * или массив (<id>, -<код ошибки>) в случае ошибки
     */

    public String[] send(String phones, String message, int translit, String time, String id, int format, String sender, String query) {
        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1"};
        String[] m = {};

        try {
            m = _smsc_send_cmd("send", "cost=3&phones=" + URLEncoder.encode(phones, SMSC_CHARSET)
                    + "&mes=" + URLEncoder.encode(message, SMSC_CHARSET)
                    + "&translit=" + translit + "&id=" + id + (format > 0 ? "&" + formats[format] : "")
                    + (sender.equals("") ? "" : "&sender=" + URLEncoder.encode(sender, SMSC_CHARSET))
                    + (time.equals("") ? "" : "&time=" + URLEncoder.encode(time, SMSC_CHARSET))
                    + (query.equals("") ? "" : "&" + query));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Problem on sending SMS", e);
        }

        if (debug) {
            if (Integer.parseInt(m[1]) > 0) {
                LOGGER.info("Сообщение отправлено успешно. ID: " + m[0] + ", всего SMS: " + m[1] + ", стоимость: " + m[2] + ", баланс: " + m[3]);
            } else {
                System.out.print("Ошибка №" + Math.abs(Integer.parseInt(m[1])));
                LOGGER.info(Integer.parseInt(m[0]) > 0 ? (", ID: " + m[0]) : "");
            }
        }
        return m;
    }

    /**
     * Получение стоимости SMS
     *
     * @param phones   - список телефонов через запятую или точку с запятой
     * @param message  - отправляемое сообщение.
     * @param translit - переводить или нет в транслит (1,2 или 0)
     * @param format   - формат сообщения (0 - обычное sms, 1 - flash-sms, 2 - wap-push, 3 - hlr, 4 - bin, 5 - bin-hex, 6 - ping-sms)
     * @param sender   - имя отправителя (Sender ID)
     * @param query    - строка дополнительных параметров, добавляемая в URL-запрос ("list=79999999999:Ваш пароль: 123\n78888888888:Ваш пароль: 456")
     * @return array(стоимость, количество sms) либо (0, -<код ошибки>) в случае ошибки
     */

    public String[] cost(String phones, String message, int translit, int format, String sender, String query) {
        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1"};
        String[] m = {};

        try {
            m = _smsc_send_cmd("send", "cost=1&phones=" + URLEncoder.encode(phones, SMSC_CHARSET)
                    + "&mes=" + URLEncoder.encode(message, SMSC_CHARSET)
                    + "&translit=" + translit + (format > 0 ? "&" + formats[format] : "")
                    + (sender.equals("") ? "" : "&sender=" + URLEncoder.encode(sender, SMSC_CHARSET))
                    + (query.equals("") ? "" : "&" + query));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Problem on sending SMS", e);
        }
        // (cost, cnt) или (0, -error)

        if (debug) {
            if (Integer.parseInt(m[1]) > 0) {
                LOGGER.info("Стоимость рассылки: " + m[0] + ", Всего SMS: " + m[1]);
            } else {
                System.out.print("Ошибка №" + Math.abs(Integer.parseInt(m[1])));
            }
        }

        return m;
    }

    /**
     * Проверка статуса отправленного SMS или HLR-запроса
     *
     * @param id    - ID cообщения
     * @param phone - номер телефона
     * @param all   - дополнительно возвращаются элементы в конце массива:
     *              (<время отправки>, <номер телефона>, <стоимость>, <sender id>, <название статуса>, <текст сообщения>)
     * @return array
     * для отправленного SMS (<статус>, <время изменения>, <код ошибки sms>)
     * для HLR-запроса (<статус>, <время изменения>, <код ошибки sms>, <код страны регистрации>, <код оператора абонента>,
     * <название страны регистрации>, <название оператора абонента>, <название роуминговой страны>, <название роумингового оператор
     * <код IMSI SIM-карты>, <номер сервис-центра>)
     * либо array(0, -<код ошибки>) в случае ошибки
     */

    public String[] status(int id, String phone, int all) {
        String[] m = {};
        String tmp;

        try {
            m = _smsc_send_cmd("status", "phone=" + URLEncoder.encode(phone, SMSC_CHARSET) + "&id=" + id + "&all=" + all);

            if (debug) {
                if (!m[1].equals("") && Integer.parseInt(m[1]) >= 0) {
//                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Integer.parseInt(m[1]));
                    LOGGER.info("Статус SMS = " + m[0]);
                } else
                    LOGGER.info("Ошибка №" + Math.abs(Integer.parseInt(m[1])));
            }

            if (all == 1 && m.length > 9 && (m.length < 14 || !m[14].equals("HLR"))) {
                tmp = _implode(m, ",");
                m = tmp.split(",", 9);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Problem on sending SMS", e);
        }
        return m;
    }

    /**
     * Получения баланса
     *
     * @return String баланс или пустую строку в случае ошибки
     */

    public String balance() {
        String[] m;

        m = _smsc_send_cmd("balance", ""); // (balance) или (0, -error)

        if (debug) {
            if (m.length == 1)
                LOGGER.info("Сумма на счете: " + m[0]);
            else
                LOGGER.info("Ошибка №" + Math.abs(Integer.parseInt(m[1])));
        }

        return m.length == 2 ? "" : m[0];
    }

    /**
     * Формирование и отправка запроса
     *
     * @param cmd - требуемая команда
     * @param arg - дополнительные параметры
     */

    private String[] _smsc_send_cmd(String cmd, String arg) {
        String ret = ",";
        try {
            String url = "https" + "://smsc.ru/sys/" + cmd + ".php?login=" + URLEncoder.encode(login, SMSC_CHARSET) + "&psw=" + URLEncoder.encode(password, SMSC_CHARSET) + "&fmt=1&charset=" + SMSC_CHARSET + "&" + arg;

            int i = 0;
            do {
                if (i > 0)
                    Thread.sleep(2000);

                if (i == 2)
                    url = url.replace("://smsc.ru/", "://www2.smsc.ru/");

                ret = _smsc_read_url(url);
            }
            while (ret.equals("") && ++i < 3);
        } catch (UnsupportedEncodingException | InterruptedException e) {
            LOGGER.warn("Problem on sending SMS", e);
        }

        return ret.split(",");
    }

    /**
     * Чтение URL
     *
     * @param url - ID cообщения
     * @return line - ответ сервера
     */
    private String _smsc_read_url(String url) {

        String line = "";
        String real_url = url;
        String[] param;
//            boolean is_post = true;

        param = url.split("\\?", 2);
        real_url = param[0];

        try {
            URL u = new URL(real_url);
            InputStream is;

            URLConnection conn = u.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), SMSC_CHARSET);
            os.write(param[1]);
            os.flush();
            os.close();
            LOGGER.info("post");
            is = conn.getInputStream();

            InputStreamReader reader = new InputStreamReader(is, SMSC_CHARSET);

            int ch;

            StringBuilder builder = new StringBuilder("");
            while ((ch = reader.read()) != -1) {
                builder.append((char) ch);
            }
            line = builder.toString();

            reader.close();
        } catch (Exception e) {
            LOGGER.warn("Problem on sending SMS ---------------------------------------------"+e.getMessage());
        }

        return line;
    }
}