package ru.trendtech.services.sms;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.trendtech.domain.SMSMessage;
import ru.trendtech.repositories.SMSMessageRepository;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.HTTPUtil;
import ru.trendtech.utils.TokenUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 31.10.2014.
 */

@Service
public class SMSaero {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSaero.class);

    @Value("${smsAero.username}")
    private String login;

    @Value("${smsAero.password}")
    private String password;

    @Autowired
    SMSMessageRepository smsMessageRepository;


//    public static void main(String[] args) throws IOException, JSONException {
//        //checkBalance();
//        //availableSendersName();
//        SMSMessage smsMessage = new SMSMessage();
//        Client client =new Client();
//        client.setPhone("+79538695889");
//
//        smsMessage.setClient(client);
//        smsMessage.setSmsText("Hello world");
//
//        //sendBySMSBAero(smsMessage);
//        //send();
//
//        smsMessage.setSmsAeroMessId("25419277");
//
//        statusSMS_Aero(smsMessage);
//    }


     public void send(){
         //SMSMessage smsMessage = createSMSMessage(message, client, "taxisto");

         /*
         SMSC smsc = new SMSC("ttehnolodgis","KrexQ8b");
         String[] strings = smsc.send("1953889","sfsdfsdf", 0, "","1", 0, "", "");

             for(String str :strings){
                 LOGGER.info("str = "+str);
             }

         если второй параметр >0 значит все хорошо, если меньше 0, значит ошибка

str = 1
str = 1
str = 0.52
str = 5643.38
         LOGGER.info("strings= "+strings);
         */

         SMSC smsc = new SMSC("ttehnolodgis","KrexQ8b");

         //List<SMSMessage> smsMessageList = smsMessageRepository.findByTimeOfDeliveryIsNull();



             //gatewaySMS.
             String[] status = smsc.status(-23, "+79538695889", 0); //  status(int id, String phone, int all) {
             //   LOGGER.info("statusSMS = "+status);

             for(Object ob :status){
                 LOGGER.info("ob="+ob);
             }


     }



    public boolean checkBalance()  throws IOException, JSONException{

        String phone = "+79538695889";
        String[] res = phone.split("\\+");
        LOGGER.info(res[1]);

        boolean result = false;
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("answer", "json"));
        urlParameters.add(new BasicNameValuePair("user", "admin@taxisto.ru"));
        urlParameters.add(new BasicNameValuePair("password", TokenUtil.getMD5("HqGbdy7Qklg")));
        String answer = HTTPUtil.senPostQuery("https://gate.smsaero.ru/balance/", urlParameters);
        LOGGER.debug("SMSAero checkBalance result: "+result);

          JSONObject jObject  = new JSONObject(answer);
            if(jObject.has("balance")){
                result = true;
            }
             return result;
    }





//    public String senPostQuery(String url, List<NameValuePair> urlParameters) throws IOException {
//        String result = "";
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost postRequest = new HttpPost(url);
//           if(urlParameters!=null && !urlParameters.isEmpty()){
//               postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
//           }
//        HttpResponse response = httpClient.execute(postRequest);
//        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//          return result=sb.toString();
//    }


    public SMSMessage sendBySMSBAero(SMSMessage smsMessage) throws IOException, JSONException {
        // https://gate.smsaero.ru/send/?to=71234567890&text=test

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("user", "admin@taxisto.ru"));
        urlParameters.add(new BasicNameValuePair("password", TokenUtil.getMD5("HqGbdy7Qklg")));
        urlParameters.add(new BasicNameValuePair("from", "taxisto"));
        String phone = smsMessage.getClient().getPhone();
        String[] phoneCut = phone.split("\\+");
        urlParameters.add(new BasicNameValuePair("to", phoneCut[1]));
        urlParameters.add(new BasicNameValuePair("text", URLEncoder.encode(smsMessage.getSmsText(), "UTF-8").replace("+", "%20")));
        urlParameters.add(new BasicNameValuePair("answer", "json"));

        String answer = HTTPUtil.senPostQuery("https://gate.smsaero.ru/send/", urlParameters);

                LOGGER.debug("SMSAero send sms result: "+answer);
                JSONObject jObject  = new JSONObject(answer);

                String res = jObject.get("result").toString();

                         if(res.equals("accepted")){
                             String idMessageAeroSMS = jObject.get("id").toString();
                             smsMessage.setSmsAeroMessId(idMessageAeroSMS);
                             smsMessage.setErrorMessageSMS_Aero("Сообщение отправлено");
                             //smsMessage.setTimeOfDelivery(DateTimeUtils.now().getMillis());
                         }else{
                             smsMessage.setErrorMessageSMS_Aero("Сообщение не отправлено");
                                if(jObject.has("reason")){
                             smsMessage.setErrorMessageSMS_Aero(jObject.get("reason").toString());
                                }
                         }
                             smsMessage.setCountTry(2);
                             smsMessageRepository.save(smsMessage);

                   /*{"result":"accepted","id":25412678}    {"result":"accepted","id":25419277}*/

                   /*
accepted	Сообщение принято сервисом
empty field. reject	Не все обязательные поля заполнены
incorrect user or password. reject	Ошибка авторизации
no credits	Недостаточно sms на балансе
incorrect sender name. reject	Неверная (незарегистрированная) подпись отправителя
incorrect destination adress. reject	Неверно задан номер телефона (формат 71234567890)
incorrect date. reject	Неправильный формат даты
in blacklist. reject	Телефон находится в черном списке
                    */
          return smsMessage;
    }



    public void availableSendersName() throws IOException {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("user", "admin@taxisto.ru"));
        urlParameters.add(new BasicNameValuePair("password", TokenUtil.getMD5("HqGbdy7Qklg")));
        urlParameters.add(new BasicNameValuePair("answer", "json"));
        String result = HTTPUtil.senPostQuery("https://gate.smsaero.ru/senders/", urlParameters);
        LOGGER.debug("SMSAero availableSendersName result: "+result);
    }





    public SMSMessage statusSMS_Aero(SMSMessage smsMessage) throws IOException, JSONException {
        // {"result":"wait status","id":25419277}

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("user", "admin@taxisto.ru"));
        urlParameters.add(new BasicNameValuePair("password", TokenUtil.getMD5("HqGbdy7Qklg")));
        urlParameters.add(new BasicNameValuePair("id", smsMessage.getSmsAeroMessId()));
        urlParameters.add(new BasicNameValuePair("answer", "json"));

        String answer = HTTPUtil.senPostQuery("https://gate.smsaero.ru/status/", urlParameters);

        JSONObject jObject  = new JSONObject(answer);

        LOGGER.debug("SMSAero statusSMS_Aero result: "+answer);

              if(jObject.has("result")){
                   String res = jObject.get("result").toString();
                      if(res.equals("delivery success")){
                          smsMessage.setDeliveryBy("sms_aero");
                          smsMessage.setErrorMessageSMS_Aero("Успешно доставлено");
                          smsMessage.setTimeOfDelivery(DateTimeUtils.nowNovosib().getMillis());
                      }else if(res.equals("wait status")){
                          smsMessage.setErrorMessageSMS_Aero("Ожидание статуса");
                          smsMessage.setTimeOfDelivery(0);
                      }else if(res.equals("delivery failure")){
                          smsMessage.setErrorMessageSMS_Aero("Ошибка доставки SMS");
                          smsMessage.setTimeOfDelivery(-1);
                      }else
                      if(res.equals("smsc submit")){
                          smsMessage.setErrorMessageSMS_Aero("Сообщение доставлено в sms aero");
                          smsMessage.setDeliveryBy("sms_aero");
                          smsMessage.setTimeOfDelivery(DateTimeUtils.nowNovosib().getMillis());
                      }else
                      if(res.equals("queue")){
                          smsMessage.setErrorMessageSMS_Aero("Ожидает отправки");
                          smsMessage.setTimeOfDelivery(0);
                      }else
                      if(res.equals("reject")){
                          String reason = jObject.get("reason").toString();
                            if(reason.equals("incorrect id")){
                                smsMessage.setErrorMessageSMS_Aero("Неверный идентификатор сообщения");
                            }else
                            if(reason.equals("smsc")){
                                smsMessage.setErrorMessageSMS_Aero("Отвергнуто SMSC");
                            }else
                            if(reason.equals("incorrect user or password")){
                                smsMessage.setErrorMessageSMS_Aero("Ошибка авторизации");
                            }
                            else
                            if(reason.equals("empty field")){
                                smsMessage.setErrorMessageSMS_Aero("Не все обязательные поля заполнены");
                            }
                          smsMessage.setTimeOfDelivery(-1);
                      }
                          smsMessageRepository.save(smsMessage);
              }
/*
delivery success	Сообщение доставлено
delivery failure	Ошибка доставки SMS (абонент в течение времени доставки находился вне зоны действия сети или номер абонента заблокирован)
smsc submit	Сообщение доставлено в SMSC
smsc reject	отвергнуто SMSC
queue	Ожидает отправки
wait status	Ожидание статуса (запросите позднее)
incorrect id. reject	Неверный идентификатор сообщения
empty field. reject	Не все обязательные поля заполнены
incorrect user or password. reject	Ошибка авторизации
 */

           return smsMessage;
    }



}
