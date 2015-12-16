package ru.trendtech.utils;

//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.html.simpleparser.HTMLWorker;
//import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.mail.EmailException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.w3c.tidy.Tidy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.ClientCard;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//import com.pdfcrowd.*;

/**
 * Created by petr on 01.09.14.
 */


public class TokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);
    private String cookies;
    private static HttpClient client = HttpClientBuilder.create().build();
    private final static String USER_AGENT = "Mozilla/5.0";




    public static void main(String[] args) throws InterruptedException, ParseException, JSONException, IOException, EmailException {


         getOrderStatus("5dce3c10-2f12-48ac-9498-6c54782829f2");








  /*
        double distanceInKm = 200000/1000d;
        LOGGER.info("distanceInKm = "+distanceInKm);
        BigDecimal x = new BigDecimal(16.308);

        //Double dRaznMinF = Math.ceil(2); //вернет 3.0
        //LOGGER.info(dRaznMinF.intValue());

       //BigDecimal rounded = x.round(new MathContext(1, RoundingMode.UP));
       //LOGGER.info(x + " -> " + rounded.intValue());
        BigInteger digits = x.toBigInteger();
        BigInteger ten = BigInteger.valueOf(10);
        int count = 0;
        do {
            digits = digits.divide(ten);
            count++;
        } while (!digits.equals(BigInteger.ZERO));
        LOGGER.info("count = "+count);
        BigDecimal roundedDistance = x.round(new MathContext(count, RoundingMode.UP));
        LOGGER.info("dsf= "+roundedDistance.intValue());
*/



//            int result= 10000;
//            Random rand = new Random();
//            result = rand.nextInt(result+1);
//            LOGGER.info("result = "+result);

        //getOrderStatus("87f8415f-08d0-4a09-8995-82c4737d6d8d");

//        try {
//            automaticRegisterCard();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /*
        try {
            phpStart();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        ВЫЧИСЛЕНИЕ РАССТОЯНИЯ
        ArrayList<String> adressesList = new ArrayList<>();
        List<String> latLonList = new ArrayList();
        adressesList.add("Саввы Кожевникова 1");
        //adressesList.add("Титова 1");
        adressesList.add("Достоевского 58");
        //adressesList.add("Достоевского 12");

        for(String address: adressesList) {
            latLonList.add(fromAddressToLatLon(address));
        }

            int allDistance = 0;

            int k=0;
            for(int i=0; i<latLonList.size();i++){
                k++;
                String addressFrom = latLonList.get(i);
                String addressTo = "";
                 if(k<latLonList.size()){
                    addressTo = latLonList.get(k);
                     LOGGER.info(k+" от: "+addressFrom+" до: "+addressTo);
                     String[] latLonAddressFrom = addressFrom.split("\\s");
                     String[] latLonAddressTo = addressTo.split("\\s");
                     allDistance+= routeBuildingFromLatLon(latLonAddressFrom[1], latLonAddressFrom[0], latLonAddressTo[1], latLonAddressTo[0]);
                 }
            }

        BigDecimal x = new BigDecimal(allDistance/1000);
        BigDecimal roundedDistance = x.round(new MathContext(1, RoundingMode.HALF_UP));
                     LOGGER.info("Полная дистанция в метрах: "+allDistance+" в км: "+roundedDistance);
                     */

        //registerOrder("1", "1");
        //getOrderStatus("8797baf3-3a39-44ed-ae10-8966070898f5");


        Client client = new Client();
        client.setFirstName("Василисто");
        client.setEmail("fr@bekker.com.ua");
        //sendRegistrationTextOnEmail(client);

        //Document doc = Jsoup.connect("http://taxisto.ru/mail/reg/index.html").get();

        String str = "+79538695889";
        String[] stok = str.split("_t");
        LOGGER.info("str = " + stok[0]);

//        Document doc = Jsoup.connect("http://taxisto.ru/mail/reg/index.html").get();
//
//        // Create a buffer to hold the cleaned up HTML
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        // Clean up the HTML to be well formed
//        HtmlCleaner cleaner = new HtmlCleaner();
//        CleanerProperties props = cleaner.getProperties();
//        TagNode node = cleaner.clean( new File("C:\\register.html"));
//        // Instead of writing to System.out we now write to the ByteArray buffer
//        new PrettyXmlSerializer(props).writeToStream(node, out);
//
//        // Create the PDF
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(new String(out.toByteArray()));
//        renderer.layout();
//        OutputStream outputStream = new FileOutputStream("C:\\HTMLasPDF.pdf");
//        renderer.createPDF(outputStream);
//
//        // Finishing up
//        renderer.finishPDF();
//        out.flush();
//        out.close();
//
//        try {
//            generateToken();
//        } catch (DateParseException e1) {
//            e1.printStackTrace();
//        }


    }




    public static void generateToken() throws InterruptedException, ParseException, DateParseException {


        Random generator = new Random();

        //int code= 100000 + generator.nextInt(900000);

        // 14.10.2014 23:00:00
        //String strDate= "14.10.2014 23:00:00";
        String strDate= "2014-06-17";
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd"); // 2014-09-04 21:53:54 // SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
        Date date = form.parse(strDate);
        DateTime dt2 = DateTimeUtils.toDateTime(date.getTime());


        //Date date = DateUtil.parseDate("14.10.2014 23:00:00");

        //String res = code+""+sdf.format(DateTimeUtils.now().toDate())+"fractal";

        //LOGGER.info("GenerateCode: "+res);


        String smsCode = StrUtils.generateSMSCode();
        //LOGGER.info("smsCode = "+smsCode);
        //LOGGER.info("IN MD5 = "+getMD5(res));


        //byte[] encodedBytes = Base64.encodeBase64(res.getBytes());
        //LOGGER.info("In Base64: " + new String(encodedBytes));
        //byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
        //LOGGER.info("From Base64 " + new String(decodedBytes));


        //LOGGER.info("date = "+DateTimeUtils.now()+" t = "+ DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Novosibirsk"))));

           /*
           Long gmtTime =1409747908000L; // 2.32pm NZDT
           Long timezoneAlteredTime = 0L;

           if (offset != 0L) {
               int multiplier = (offset*60)*(60*1000);
               timezoneAlteredTime = gmtTime + multiplier;
           } else {
               timezoneAlteredTime = gmtTime;
           }
           Calendar calendar = new GregorianCalendar();
           calendar.setTimeInMillis(timezoneAlteredTime);

           DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
           formatter.setCalendar(calendar);
           formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

           String newZealandTime = formatter.format(calendar.getTime());
           */

        //String stGen = StrUtils.generateAlphaNumString(6);
        //LOGGER.info("stGen="+stGen);


        // 2014-09-04 02:06:47
           /*
               GMT: Wed, 03 Sep 2014 12:38:28 GMT
               Your time zone: 3.9.2014 19:38:28 GMT+7
            */

        DateTime dateTime = new DateTime(1409718445000L);// 1409747908000L

        //DateTime curr = new DateTime(System.currentTimeMillis());

        DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        //LOGGER.info("date = "+curr+" t = "+ DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC"))));

        LOGGER.info("my date = " + formatter.format(dateTime.toDate()));

        Date d = formatter.parse(formatter.format(dateTime.toDate()));
        LOGGER.info("d= " + d);



        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+6"); //TimeZone.getDefault();
        calendar.setTimeInMillis(1409718445000L);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        LOGGER.info("cal = " + DateTimeUtils.toDateTime(calendar.getTime().getTime()));


        Calendar calendar2 = Calendar.getInstance();
        TimeZone tz2 = TimeZone.getTimeZone("GMT+6"); //TimeZone.getDefault();
        calendar2.setTimeInMillis(DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+6"))).getMillis());
        //calendar2.add(Calendar.MILLISECOND, calendar2.getTimeInMillis());

        LOGGER.info("cal2 = " + calendar2.getTime());

//           formatter.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
//           LOGGER.info(formatter.format(instance2.getTime()))

        //LOGGER.info("Base64 by MD5 = "+getBase64ByMD5(res));

        //Thread.sleep(3000);
        //}

    }


    public static void sendRegistrationTextOnEmail(Client client){
        Document doc = null;
        try {
            doc = Jsoup.connect("http://taxisto.ru/mail/reg/index.html").get();
            Element divContentHeader = doc.select("div.content_header").first();
            divContentHeader.html(client.getFirstName() + ",<br/> приветствуем Вас!");

            LOGGER.info(doc.outerHtml());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static PaymentHelper registerOrder(String orderId, String amount){
        PaymentHelper paymentHelper =new PaymentHelper();
        try{

            DefaultHttpClient httpClient = new DefaultHttpClient();

            //HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/register.do");
            HttpPost postRequest = new HttpPost("https://test.paymentgate.ru/testpayment/rest/register.do");

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            //urlParameters.add(new BasicNameValuePair("password", "Hb52FtgQlPb!{_a"));
            urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));
            urlParameters.add(new BasicNameValuePair("orderNumber", orderId));
            urlParameters.add(new BasicNameValuePair("amount", amount));
            urlParameters.add(new BasicNameValuePair("clientId", "7"));
            //urlParameters.add(new BasicNameValuePair("returnUrl", "finish.html?login=taxisto-api?password=taxisto&userName=taxisto-api&jsonParams={\"orderNumber\":"+orderNumberStr+"}"));
            urlParameters.add(new BasicNameValuePair("returnUrl", "finish.html"));
            urlParameters.add(new BasicNameValuePair("pageView", "MOBILE"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            LOGGER.info("postRequest url with param : " + postRequest.getURI() + " param : " + urlParameters);
            HttpResponse response = httpClient.execute(postRequest);
            LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.info("answer = " + sb);


            // output = {"orderId":"258570a3-de11-4830-b826-1da23569bee4","formUrl":"https://test.paymentgate.ru/testpayment/merchants/taxisto/mobile_payment_ru.html?mdOrder=258570a3-de11-4830-b826-1da23569bee4&pageView=MOBILE"}
            JSONObject jObject  = new JSONObject(sb.toString()); // json

            if(jObject.has("orderId")){
                String orderIdStr = jObject.getString("orderId"); // get the name from data.
                if(orderIdStr!=null){
                    paymentHelper.setOrderId(orderIdStr);
                    paymentHelper.setAmount(amount);
                    LOGGER.info("Сгенерированный orderId = " + orderIdStr + " amount = " + amount);
                }else{
                    paymentHelper.setOrderId("-1");
                }
            }

            return paymentHelper;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.JSONException r) {
            r.printStackTrace();
        }
        return paymentHelper;
    }



    public static int routeBuildingFromLatLon(String latitude_from, String longitude_from, String latitude_to, String longitude_to){
        int result = 0;
        String answer = "";
        String url="http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude_from + "," + longitude_from + "&destination=" + latitude_to + "," + longitude_to + "&sensor=true&alternatives=true&units=metric&mode=driving";

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
             String s;
            while ((s = buffer.readLine()) != null) {
                answer += s;
            }
              result = parseGoogleAnswer(answer);
                return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }



    private static int parseGoogleAnswer(String result) {
        int distance = 0;
        try {
            final JSONObject json = new JSONObject(result);
            final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
            final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
            final JSONArray steps = leg.getJSONArray("steps");
            final int numSteps = steps.length();
            for (int i = 0; i < numSteps; i++) {
                final JSONObject step = steps.getJSONObject(i);
                final int length = step.getJSONObject("distance").getInt("value");
                  distance += length;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distance;//distance / 1000;
    }



    protected static String fromAddressToLatLon(String address){
         //String url=http://geocode-maps.yandex.ru/1.x/?format=json&geocode=Москва, улица Новый Арбат, дом 24
         String result = "";
         String answer = "";
         address = address.replaceAll("\\s", "");
            String url="http://geocode-maps.yandex.ru/1.x/?format=json&geocode=Новосибирск,"+address;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    answer += s;
                }
                result = parseYandexAnswer(answer);
                  return result;
            } catch (Exception e) {
                e.printStackTrace();
                  return result;
            }
    }



    protected static String parseYandexAnswer(String result) {
        String coordinates = "";
        JSONArray jArray;
        JSONObject jObject;
        try {
            jObject = new JSONObject(result);
            JSONObject jsonObject = jObject.getJSONObject("response");
            JSONObject jsonObject2 = jsonObject.getJSONObject("GeoObjectCollection");
            jArray = jsonObject2.getJSONArray("featureMember");
            if(jArray.length() > 0) {
                jsonObject = jArray.getJSONObject(0);
                jsonObject2 = jsonObject.getJSONObject("GeoObject");
                jsonObject = jsonObject2.getJSONObject("Point");
                coordinates = jsonObject.getString("pos");
            }
               return coordinates;
        } catch (JSONException e) {
            e.printStackTrace();
              return coordinates;
        }
    }






   public static void phpStart() throws JSONException, IOException {
       String url = "http://stg.taxisto.ru:81/index.php/client/GetMissionDetails";
       JSONObject json = new JSONObject();
       json.put("missionId", 12);
       DefaultHttpClient httpClient = new DefaultHttpClient();
       String answer = postToURL(url, json , httpClient); // message
       JSONObject jsonObj = new JSONObject(answer);
       httpClient.getConnectionManager().shutdown();
       String result = jsonObj.get("MissionDetailsInfo").toString();
       LOGGER.info("result=" + result);
   }



    private static String postToURL(String url, JSONObject message, DefaultHttpClient httpClient) throws IOException, RuntimeException {
        HttpPost postRequest = new HttpPost(url);

        StringEntity input = new StringEntity(message.toString());
        input.setContentType("application/json");
        input.setContentEncoding("UTF-8");
        postRequest.setEntity(input);

        HttpResponse response = httpClient.execute(postRequest);

//        if (response.getStatusLine().getStatusCode() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                    + response.getStatusLine().getStatusCode());
//        }

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String output;
        StringBuffer totalOutput = new StringBuffer();
        //LOGGER.info("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            LOGGER.info("answer=" + output);
            totalOutput.append(output);
        }
        return totalOutput.toString();
    }



   public static void automaticRegisterCard() throws Exception {
       String url = "https://test.paymentgate.ru/testpayment/merchants/taxisto/mobile_payment_ru.html?mdOrder=e40f95f7-7dcb-4aae-8783-2d53b1176477";
       String payURL = "https://test.paymentgate.ru/testpayment/merchants/taxisto/mobile_payment_ru.html?mdOrder=e40f95f7-7dcb-4aae-8783-2d53b1176477";

       // make sure cookies is turn on
       //CookieHandler.setDefault(new CookieManager());

       String page = GetPageContent(url);

       //public List<NameValuePair> getFormParams(String html, String iPan, String iText, String iCVC, String month, String year)
       List<NameValuePair> postParams = getFormParams(page, "639002000000000003", "Fr", "123", "12","2015");

          /*
pan: 6390 0200 0000 000003
exp date: 2015/12
cvv2: 123(необязательный параметр)
3dsecure: veres=y, pares=a

         */

       sendPost(url, postParams);

       String pageAddCard = GetPageContent(payURL);


       sendPost(url, postParams);
       //pageAddCard


       LOGGER.info("After");
       LOGGER.info(pageAddCard);

       //LOGGER.info("Done");
    }



    private static String GetPageContent(String url) throws Exception {

        HttpGet request = new HttpGet(url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Language", "en-US,en;q=0.5");

        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();

        LOGGER.info("\nSending 'GET' request to URL : " + url);
        LOGGER.info("Response Code : " + responseCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
            LOGGER.info("" + line);
        }

        // set cookies
        //setCookies(response.getFirstHeader("Set-Cookie") == null ? "" :response.getFirstHeader("Set-Cookie").toString());
        //LOGGER.info("result ="+result);
        return result.toString();

    }





//    paramList.add(new BasicNameValuePair("expiry", ""));
//    paramList.add(new BasicNameValuePair("language", "RU"));
//    paramList.add(new BasicNameValuePair("location", "/testpayment/"));
//    paramList.add(new BasicNameValuePair("mdOrder", "")); // missionId
//    paramList.add(new BasicNameValuePair("iPan", "")); // номер карты
//    paramList.add(new BasicNameValuePair("iTEXT", "")); // имя владельца карты
//    paramList.add(new BasicNameValuePair("iCVC", "")); // имя владельца карты
    public static List<NameValuePair> getFormParams(String html, String iPan, String iText, String iCVC, String month, String year)
            // 01 - month
            throws UnsupportedEncodingException {

        LOGGER.info("Extracting form's data...");

        Document doc = Jsoup.parse(html);

        // Google form id
        Element formPayment = doc.getElementById("formPayment");
        Elements inputElements = formPayment.getElementsByTag("input");

        //Element expiry = formPayment.getElementById("expiry");


        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        //paramList.add(new BasicNameValuePair("expiry", ""));
        //paramList.add(new BasicNameValuePair("language", "RU"));
        //paramList.add(new BasicNameValuePair("location", "/testpayment/"));
        //paramList.add(new BasicNameValuePair("mdOrder", "")); // missionId
        paramList.add(new BasicNameValuePair("iPan", iPan)); // номер карты
        paramList.add(new BasicNameValuePair("iTEXT", iText)); // имя владельца карты
        paramList.add(new BasicNameValuePair("iCVC", iCVC)); // имя владельца карты


//        id="year">
//        <option value='2012' selected>2012</option>
//        <option value='2013'>2013</option>
//        <option value='2014'>2014</option>
//        <option value='2015'>2015


        Elements optionsYear = doc.getElementsByTag("year");
        for (Element option : optionsYear) {
            if (option.attr("value").equals(year)) {
                option.attr("selected", "selected");
            } else {
                option.removeAttr("selected");
            }
        }

        Elements optionsMonth = doc.getElementsByTag("month");
        for (Element option : optionsYear) {
            if (option.attr("value").equals(month)) {
                option.attr("selected");
            } else {
                option.removeAttr("selected");
            }
        }



        /*
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");
            if (key.equals("Email"))
                value = username;
            else if (key.equals("Passwd"))
                value = password;
            paramList.add(new BasicNameValuePair(key, value));
        }
        */

        return paramList;
    }


    private static void sendPost(String url, List<NameValuePair> postParams)
            throws Exception {

        HttpPost post = new HttpPost(url);

        // add header
        //post.setHeader("Host", "accounts.google.com");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Language", "en-US,en;q=0.5");
        //post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        //post.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(postParams));

        HttpResponse response = client.execute(post);

        int responseCode = response.getStatusLine().getStatusCode();

        LOGGER.info("\nSending 'POST' request to URL : " + url);
        LOGGER.info("Post parameters : " + postParams);
        LOGGER.info("Response Code : " + responseCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
    }



    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }



    public List<ClientCard> delete(ClientCard clientCard, List<ClientCard> list){
          if(clientCard.getActive().equals(Boolean.TRUE)){
              // active card
              list.remove(clientCard);


          }
          return list;
    }



    public static void getBindings(){
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://test.paymentgate.ru/testpayment/rest/getBindings.do");

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));

            urlParameters.add(new BasicNameValuePair("clientId","1"));


            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            LOGGER.info("postRequest url with param : " + postRequest.getURI() + " param : " + urlParameters);
            HttpResponse response = httpClient.execute(postRequest);
            LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.info("answer = " + sb);

//            JSONObject jObject  = new JSONObject(sb.toString()); // json
//
//            String errorCode = jObject.getString("errorCode"); // get the name from data.
//
//            if(errorCode.equals("0")){
//                LOGGER.info("Обработка запроса прошла без системных ошибок");
//            }else if(errorCode.equals("5")){
//                LOGGER.info("Ошибка значение параметра запроса");
//            }else if(errorCode.equals("6")){
//                LOGGER.info("Незарегистрированный OrderId");
//            }else if(errorCode.equals("7")){
//                LOGGER.info("Системная ошибка");
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (org.json.JSONException r) {
//            r.printStackTrace();
//        }

    }


    /*
    answer = {"redirect":"finish.html?login=taxisto-api?password=taxisto&userName=taxisto-api&orderId=e40f95f7-7dcb-4aae-8783-2d53b1176477","info":"Операция отклонена. Обратитесь в магазин<br>Происходит переадресация...","errorCode":0}
     */



    /*
    active = 1;
    bindingId = "dd9c7912-b54b-49fe-aa8e-94012728e202";
    cardId = 0;
    cardholderName = "Fed s";
    clientId = 1;
    expirationMonth = 12;
    expirationYear = 2015;
    mdOrder = "5bb31b7a-72bf-4323-9f7c-17f1db2138b0";
    mrchOrder = "card_1141102632444";
    pan = "411111**1111";
     */






    public static void paymentOrderBinding(String orderId, String bindingId){
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://test.paymentgate.ru/testpayment/rest/paymentOrderBinding.do");

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));
            urlParameters.add(new BasicNameValuePair("mdOrder","b479e0a2-bc15-4c97-b12f-1353df7b5be2"));
            urlParameters.add(new BasicNameValuePair("bindingId","dd9c7912-b54b-49fe-aa8e-94012728e202"));


            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            LOGGER.info("postRequest url with param : " + postRequest.getURI() + " param : " + urlParameters);
            HttpResponse response = httpClient.execute(postRequest);
            LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.info("answer = " + sb);

//            JSONObject jObject  = new JSONObject(sb.toString()); // json
//
//            String errorCode = jObject.getString("errorCode"); // get the name from data.
//
//            if(errorCode.equals("0")){
//                LOGGER.info("Обработка запроса прошла без системных ошибок");
//            }else if(errorCode.equals("5")){
//                LOGGER.info("Ошибка значение параметра запроса");
//            }else if(errorCode.equals("6")){
//                LOGGER.info("Незарегистрированный OrderId");
//            }else if(errorCode.equals("7")){
//                LOGGER.info("Системная ошибка");
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (org.json.JSONException r) {
//            r.printStackTrace();
//        }

    }


    public static void getOrderStatus(String orderId){
            try{

                DefaultHttpClient httpClient = new DefaultHttpClient();

                HttpPost postRequest = new HttpPost("https://engine.paymentgate.ru/payment/rest/getOrderStatusExtended.do"); // Extended

                List<NameValuePair> urlParameters = new ArrayList<>();

                urlParameters.add(new BasicNameValuePair("userName", "taxisto_auto-api"));
                urlParameters.add(new BasicNameValuePair("password", "test"));
                urlParameters.add(new BasicNameValuePair("orderId", orderId));
                //urlParameters.add(new BasicNameValuePair("orderNumber", "888888"));

                postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
                LOGGER.info("postRequest url with param : " + postRequest.getURI() + " param : " + urlParameters);
                HttpResponse response = httpClient.execute(postRequest);
                LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                StringBuilder sb = new StringBuilder();
                String line ;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                LOGGER.info("answer = " + sb.toString());
                JSONObject jsonObj = new JSONObject(sb.toString());
                httpClient.getConnectionManager().shutdown();


                if(jsonObj.has("OrderStatus")){
                    LOGGER.info(jsonObj.get("OrderStatus").toString());
                    //resp.setOrderStatus(jsonObj.get("OrderStatus").toString());
                }
                if(jsonObj.has("expiration")){
                    LOGGER.info(jsonObj.get("expiration").toString());
                    //resp.setExpiration(jsonObj.get("expiration").toString());
                }
                if(jsonObj.has("cardholderName")){
                    LOGGER.info(jsonObj.get("cardholderName").toString());
                    //resp.setCardholderName(jsonObj.get("cardholderName").toString());
                }
                if(jsonObj.has("depositAmount")){
                    LOGGER.info(jsonObj.get("depositAmount").toString());
                    //resp.setDepositAmount(jsonObj.get("depositAmount").toString());
                }
                if(jsonObj.has("bindingId")){
                    LOGGER.info(jsonObj.get("bindingId").toString());
                    //resp.setBindingId(jsonObj.get("bindingId").toString());
                }
                if(jsonObj.has("pan")){
                    LOGGER.info(jsonObj.get("pan").toString());
                    //resp.setPan(jsonObj.get("pan").toString());
                }
                if(jsonObj.has("expiration")){
                    LOGGER.info(jsonObj.get("expiration").toString());
                    //resp.setExpiration(jsonObj.get("expiration").toString());
                }
                if(jsonObj.has("ErrorCode")){
                    LOGGER.info(jsonObj.get("ErrorCode").toString());
                    //resp.setErrorCode(jsonObj.get("ErrorCode").toString());
                }
                if(jsonObj.has("ErrorMessage")){
                    LOGGER.info(jsonObj.get("ErrorMessage").toString());
                    //resp.setErrorMessage(jsonObj.get("ErrorMessage").toString());
                }

                  //String result = jsonObj.get("OrderStatus").toString();
                  //LOGGER.info("result = "+result);

            } catch (IOException e) {
                e.printStackTrace();
            } catch(JSONException j){
                j.printStackTrace();
            }
    }


    public static void startPaymentOperation(String orderId){
        try{

            // ffa96944-02dd-4300-b13c-2f1193c0f3b4

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://test.paymentgate.ru/testpayment/rest/paymentotherway.do");


             //StringEntity input = new StringEntity("{\"password\":\"taxisto\",\"userName\":\"taxisto\",\"language\":\"ru\"\"MDORDER\":\"9999\",\"username\":\"demo\"}");
             //input.setContentType("application/json");
             //postRequest.setEntity(input);
             // password=111111&userName=987&language=ru&MDORDER=c96a734c-e2c9-429c-8fda-aaa0030c8a92&paymentWay=ALFA_ALFACLICK



            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));
            urlParameters.add(new BasicNameValuePair("language", "ru"));
            urlParameters.add(new BasicNameValuePair("MDORDER", orderId));
            urlParameters.add(new BasicNameValuePair("paymentWay", "ALFA_ALFACLICK"));

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

            LOGGER.info("postRequest url : " + postRequest.getURI() + " param : " + urlParameters);

            HttpResponse response = httpClient.execute(postRequest);

            LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

            //if (response.getStatusLine().getStatusCode() != 201) {
            //throw new RuntimeException("Failed : HTTP error code : "
            //	+ response.getStatusLine().getStatusCode());
            //}

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.info("answer 2 = " + sb);

//            JSONObject json = null;
//            try {
//                json = (JSONObject)new JSONParser().parse("{\"name\":\"MyNode\", \"width\":200, \"height\":100}");
//                LOGGER.info("name=" + json.get("name"));
//                LOGGER.info("width=" + json.get("width"));
//            } catch (org.json.simple.parser.ParseException e) {
//                e.printStackTrace();
//            }  catch (org.json.JSONException r) {
//                r.printStackTrace();
//            }


            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void depositDo(PaymentHelper paymentHelper){
        // amount=100&currency=810&language=ru&orderId=e5b59d3d-746b-4828-9da4-06f126e01b68
        //https://test.paymentgate.ru/testpayment/rest/deposit.do?
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost postRequest = new HttpPost("https://test.paymentgate.ru/testpayment/rest/deposit.do");

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("password", "taxisto"));
            urlParameters.add(new BasicNameValuePair("userName", "taxisto-api"));
            urlParameters.add(new BasicNameValuePair("orderId","62a95f88-9373-4e47-ae4e-117a43ca7efe")); //paymentHelper.getOrderId())
            //LOGGER.info("amount = "+paymentHelper.getAmount());
            urlParameters.add(new BasicNameValuePair("amount", paymentHelper.getAmount())); // paymentHelper.getAmount()

            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            LOGGER.info("postRequest url with param : " + postRequest.getURI() + " param : " + urlParameters);
            HttpResponse response = httpClient.execute(postRequest);
            LOGGER.info("Response status code : " + response.getStatusLine().getStatusCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            LOGGER.info("answer = " + sb);

            JSONObject jObject  = new JSONObject(sb.toString()); // json

            String errorCode = jObject.getString("errorCode"); // get the name from data.

            if(errorCode.equals("0")){
                LOGGER.info("Обработка запроса прошла без системных ошибок");
            }else if(errorCode.equals("5")){
                LOGGER.info("Ошибка значение параметра запроса");
            }else if(errorCode.equals("6")){
                LOGGER.info("Незарегистрированный OrderId");
            }else if(errorCode.equals("7")){
                LOGGER.info("Системная ошибка");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.JSONException r) {
            r.printStackTrace();
        }

    }


    public static class PaymentHelper{
        private String orderId;
        private String amount;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }








/*
    another solution: мне кажется оно лучше
    public static String getMd5Hash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(str.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < thedigest.length; i++)
        {
            String hex = Integer.toHexString(0xFF & thedigest[i]);
            if (hex.length() == 1)
                hexString.append('0');

            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
*/




    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



}
