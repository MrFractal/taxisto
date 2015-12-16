package ru.trendtech.utils.test;

/**
 * Created by petr on 22.10.2014.
 */

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.CourierCalculatePriceResponse;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutocompleteInfo;
import ru.trendtech.common.mobileexchange.model.courier.ItemPriceInfo;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;
import ru.trendtech.common.mobileexchange.model.courier.OrderItemPriceInfo;
import ru.trendtech.common.mobileexchange.model.courier.StoreAddressInfo;
import ru.trendtech.domain.ClientCard;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.integration.payonline.AuthResponse;
import ru.trendtech.integration.payonline.ErrorResponse;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.utils.*;
import sun.java2d.loops.CustomComponent;


public class App {
    private static final ContentInfoUtil CONTENT_INFO_UTIL = new ContentInfoUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }



    public static String autoComplete2GIS(String address) throws UnsupportedEncodingException {
        String bodyAnswer;
        String point = "";
        try {
            String url = "http://catalog.api.2gis.ru/2.0/geo/search?fields=items.geometry.centroid,items.adm_div,items.address&key=ruoblg7173&radius=250&type=street,building,attraction&region_id=1&q="+ URLEncoder.encode(address, "UTF-8");

            bodyAnswer = HTTPUtil.sendHttpQuery(url, null);

            //LOGGER.info("bodyAnswer: "+bodyAnswer);

            JSONObject answerJson = new JSONObject(bodyAnswer);

            /*
              body answer example: {"meta":{"code":200},"result":{"total":1,
              "items":[{"id":"141373143519667","name":"?? ????????????, ??????-?????","full_name":"???????????, ?? ????????????, ??????-?????","building_name":"?? ????????????, ??????-?????","purpose_name":"??????-?????","address_name":"????????????, 58","geometry":{"centroid":"POINT(82.938324 55.047353)"},"type":"building"}]}}
            */

            if(answerJson.has("result")){
                return answerJson.toString();
                /*
                JSONObject resultJson = (JSONObject) answerJson.get("result");
                JSONArray itemsArray = (JSONArray) resultJson.get("items");
                JSONObject item = (JSONObject) itemsArray.get(0);
                JSONObject geometry = (JSONObject) item.get("geometry");
                point = geometry.get("centroid").toString();
                */
            }
        } catch(JSONException n){
            n.printStackTrace();
        }

        return point;
    }




    public static void calculateDistanceWithTimeDuration(String query)  {
        String answer = "";

        try {

            /*
                Допустимые значения:
                shortest - кратчайший маршрут
                optimal_statistic - оптимальный маршрут с учетом статистики
                optimal_jams - оптимальный маршрут с учетом пробок
                Значение по умолчанию - optimal_statistic
             */
            String url = "http://catalog.api.2gis.ru/2.0/transport/calculate_directions";
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("waypoints", query));
            urlParameters.add(new BasicNameValuePair("routing_type", "shortest")); // кратчайшие
            urlParameters.add(new BasicNameValuePair("key", "ruoblg7173"));

            answer = HTTPUtil.senPostQuery(url, urlParameters);

            JSONObject answerJson = new JSONObject(answer);

            //all result = {"meta":{"code":200},"result":{"total":1,"items":[{"type":"optimal_statistic","total_duration":697,"total_distance":3613,

            if(answerJson.has("result")){
                JSONObject resultJson = (JSONObject) answerJson.get("result");
                JSONArray itemsArray = (JSONArray) resultJson.get("items");
                JSONObject totalDistance = (JSONObject) itemsArray.get(0);

                String totalStr = totalDistance.get("total_distance").toString();
                String totalTimeDuration = totalDistance.get("total_duration").toString();

                LOGGER.info("totalStr: " + totalStr + " | totalTimeDuration)/60: " + totalTimeDuration);

            }else{
                LOGGER.info("2GIS answer: В json ответе 2gis не найден параметр result ~~~~~");
                //directionHelper.setTimeDuration(-1);
            }
            //result = Integer.parseInt(totalStr);
        } catch(JSONException n){
            LOGGER.debug("2GIS: routeBuildingFromLatLon2GIS exception: "+n.getMessage()+" answer: "+answer);
            n.printStackTrace();
        }

    }


    /*
    for (int i = 0; i < latLonList.size(); i++) {
        if (i == 0) {
            builder.append(latLonList.get(i));
        } else {
            builder.append("," + latLonList.get(i));
        }
    }
    */


    public static CourierCalculatePriceResponse calculatePrice(OrderInfo orderInfo){
        CourierCalculatePriceResponse response = new CourierCalculatePriceResponse();
        StringBuilder waypoints = new StringBuilder();
        switch(orderInfo.getOrderType()){
            case 1: {
               /* BUY_AND_DELIVER */
                List<OrderItemPriceInfo> orderItemPriceInfos = orderInfo.getOrderItemPriceInfos();
                if(!CollectionUtils.isEmpty(orderItemPriceInfos)){
                    int idx =0;
                    for(OrderItemPriceInfo orderItemPriceInfo: orderItemPriceInfos){
                        ItemPriceInfo itemPriceInfo = orderItemPriceInfo.getItemPriceInfo();
                        if(itemPriceInfo != null){
                            StoreAddressInfo storeAddressInfo = itemPriceInfo.getStoreAddressInfo();
                            if(storeAddressInfo !=null){
                                LOGGER.info("point: "+storeAddressInfo.getLongitude()+" "+ storeAddressInfo.getLatitude());
                                if(idx==0){
                                    waypoints.append(storeAddressInfo.getLongitude()+" "+ storeAddressInfo.getLatitude());
                                } else{
                                    waypoints.append(","+ storeAddressInfo.getLongitude()+" "+ storeAddressInfo.getLatitude());
                                }

                            }
                        }
                        idx++;
                    }
                   LOGGER.info("points: "+waypoints.toString());
                   calculateDistanceWithTimeDuration(waypoints.toString());

                }
                break;
            }
            case 2: {
               /* TAKE_AND_DELIVER */
                break;
            }
            case 3: {
               /* OTHER */
                break;
            }
            default: break;
        }
        return response;
    }







    public static void main( String[] args ) throws Exception {

        String qqq = "Id=54364224&Operation=Auth&Result=Ok&Code=200&Status=PreAuthorized&rebillAnchor=ORE03kSNWI1FLjn3pZn7Vl8HMfBjsL";
        //String[] splitRes = qqq.split("[\\s&=]+");
        //for(String res : splitRes){
        boolean containsString = org.apache.commons.lang3.StringUtils.containsIgnoreCase(qqq, "Result=OK");
        LOGGER.info("res: " + containsString);


        /*
        String data = "Name der Strase 25a 88489 Teststadt";
        String regexp = "([ a-zA-z]+) ([\\w]+) (\\d+) ([a-zA-Z]+)";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(data);
        boolean matchFound = matcher.find();
        if (matchFound) {
            // Get all groups for this match
            for (int i=0; i<= matcher.groupCount(); i++) {
                String groupStr = matcher.group(i);
                LOGGER.info("groupStr: " + groupStr);
            }
        }
        */


        /*
        Document document = Jsoup.connect("https://www.desco.org.bd/ebill/authentication.php")
                .data("cookieexists", "false")
                .data("username", "32007702")
                .data("login", "Login")
                //.cookies(loginForm.cookies())
                .post();

        Document doc = Jsoup.parse("<html>\n" +
                "<body>\n" +
                "<form action=\"/acs/PAReq\" mthod=\"post\" class=\"b-confirm-form__form\">\n" +
                "        <input type=\"hidden\" name=\"ACCT_ID\" value=\"1421932419.4453774094827049\">\n" +
                "\t<input  id=\"iPwd_field\" type=\"text\" name=\"PWD\" class=\"b-confirm-form__input\" size=\"3\"  maxlength=\"6\" autocomplete=\"off\">\n" +
                "\t<input type=\"submit\" value=\"Подтвердить\" name=\"SEND\" class=\"b-confirm-form__submit\">\n" +
                "</form>\n" +
                "</html>\n" +
                "</body>");
        if(doc!=null) {
            Element txtArea = doc.getElementById("iPwd_field");
            txtArea.val("sms code");
            Elements f = doc.getElementsByAttributeStarting("action");
            f.attr("action", "http://bbbbb.ru");
        }
        LOGGER.info("doc.outerHtml(): " + doc.outerHtml());
        */



        //LOGGER.info("DATE PARSE: " + DateTimeUtils.nowNovosib_GMT6().getDayOfMonth()+ " | "+DateTimeUtils.nowNovosib_GMT6().getMonthOfYear());

//
//        Range<Integer> test = Range.between(1, 3);
//        System.out.println(test.contains(3));
//        System.out.println(test.contains(4));
//
//        Integer r = 3;
//        String s = r.toString();
//        LOGGER.info("s: "+s);
//
//
        String xml = "<transaction>\n" +
                "          <id>51208773</id>\n" +
                "          <operation>Auth</operation>\n" +
                "          <result>Ok</result>\n" +
                "          <code>200</code>\n" +
                "          <status>PreAuthorized</status>\n" +
                "          <MerchantId>59291</MerchantId>\n" +
                "          <ipCountry>RU</ipCountry>\n" +
                "          <binCountry>US</binCountry>\n" +
                "      </transaction>";


        String secondAnswer = "<transaction>\n" +
                "  <id>52178039</id>\n" +
                "  <operation>Rebill</operation>\n" +
                "  <result>Error</result>\n" +
                "  <status>Awaiting3DAuthentication</status>\n" +
                "  <code>6001</code>\n" +
                "  <errorCode>4</errorCode>\n" +
                "  <threedSecure>\n" +
                "    <pareq>eJxVUl1TwjAQ/CtMH52x+WgLlDnCFBmV8Qu1Iq8ljW3VpJi2Cv56k1oE33b3ks3dXmCyle+9T6GrolRjh7jY6QnFy7RQ2dh5is9Ph86EQZxrIWaPgjdaMLgRVZVkolekY2cRPYiP0TJbXg0W0Td+yeXFYkfVRRCvModBW2bQPcCMv0sB7alx0jxPVM0g4R/T+S3z+wHFfUAdBSn0fNapmHpDEmBMAP3KoBIp2NX59CSOVvPH+A5QqwAvG1XrHev7HqA9gUa/s7yuNyOEvLRypVwn6s3VDQJkS4AO3SwaiypjtS1S9qXq6/vwuZRb/Xa9jpXU9LUM51nE+RiQPQFpUgtGsWkvpEGPeCPqjQIzR6tDIm0Ppo7NZL8YNvaJ6KhwLIAJWps97EfYMxDbTamEvQLoD0MqKs6oyaUFgA7tn13aaHlt0iI+JaFHfRK6Qxr0gxB7wQB7lLZxt0esfWGiIgOCW39LAFkT1G0Sdcs36N+n+AHRAr0j</pareq>\n" +
                "    <acsurl>https://acs.alfabank.ru/acs/PAReq</acsurl>\n" +
                "    <pd>zC6vgK9bbX0S5WZr/Sq6jNONjIx5pSDaP+E82Mx9l9Pz5fFzdvXynn2t0Kd3k7LF</pd>\n" +
                "  </threedSecure>\n" +
                "</transaction>";

        String error = "<error>\n" +
                "        <code>1000</code>\n" +
                "        <message>Internal</message>\n" +
                "     </error>";

        Reader reader = new StringReader(secondAnswer);
        Persister serializer = new Persister();
        try {
            AuthResponse authResponse = serializer.read(AuthResponse.class, reader, false);
            LOGGER.info("status: " + authResponse.getStatus() + " | transactionalId: " + authResponse.getId()+" | threedSecure: " + authResponse.getThreedSecure() + " | " + (authResponse.getThreedSecure()!=null?authResponse.getThreedSecure().getPaReq() :""));

            //if(StringUtils.isEmpty(authResponse.getMerchantId())){
                //reader = new StringReader(secondAnswer);
                //ErrorResponse errorResponse = serializer.read(ErrorResponse.class, reader, false);
                //LOGGER.info("errorResponse: errorMessage = "+errorResponse.getMessage()+" errorCode = "+errorResponse.getCode());
            //}
        }
        catch (Exception e) {
            e.printStackTrace();

            reader = new StringReader(error);
            ErrorResponse errorResponse = serializer.read(ErrorResponse.class, reader, false);
            LOGGER.info("errorResponse : "+errorResponse.getMessage()+" code: "+errorResponse.getCode());
            LOGGER.info(e.getMessage());
        }
//
//
//        Money money1 = MoneyUtils.getMoney(10000);
//
//        //money1.
//
//
//        LOGGER.info("money1: "+money1.getAmount().floatValue()/100);

        //Money money = MoneyUtils.getMoney(100);
        //LOGGER.info("money: "+money.getAmountMinorInt());

        //LOGGER.info("roundedDistance: "+MathUtil.convertAnRoundUpdMetersToKm(6836));

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderType(1);

        List<OrderItemPriceInfo> orderItemPriceInfos = new ArrayList<>();

        OrderItemPriceInfo one = new OrderItemPriceInfo();
        ItemPriceInfo itemPriceInfoOne = new ItemPriceInfo();
        StoreAddressInfo storeAddressInfoOne = new StoreAddressInfo();
        storeAddressInfoOne.setAddress("Саввы Кожевникова, 1");
        storeAddressInfoOne.setLatitude(54.984014);
        storeAddressInfoOne.setLongitude(82.872696);
        itemPriceInfoOne.setStoreAddressInfo(storeAddressInfoOne);

        one.setItemPriceInfo(itemPriceInfoOne);
        orderItemPriceInfos.add(one);


        OrderItemPriceInfo two = new OrderItemPriceInfo();
        ItemPriceInfo itemPriceInfoTwo = new ItemPriceInfo();
        StoreAddressInfo storeAddressInfoTwo = new StoreAddressInfo();
        storeAddressInfoTwo.setAddress("Ольги жилиной, 60");

        storeAddressInfoTwo.setLatitude(55.029152);
        storeAddressInfoTwo.setLongitude(82.913654);
        itemPriceInfoTwo.setStoreAddressInfo(storeAddressInfoTwo);

        two.setItemPriceInfo(itemPriceInfoTwo);
        orderItemPriceInfos.add(two);


        orderInfo.setOrderItemPriceInfos(orderItemPriceInfos);

        //calculatePrice(orderInfo);

        List<WebAutocompleteInfo> webAutocompleteInfos = new ArrayList<>();
        for(WebAutocompleteInfo info: webAutocompleteInfos){
           // LOGGER.info(""+ info);
        }


        /*
        {"meta":{"code":200},
        "result":{"total":20,"items":[{"id":"141476222738299","full_name":"??????????????????????, ??????????????????????????","name":"??????????????????????????",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(82.952106 55.015666)"},"type":"street"},{"id":"141476222739054","full_name":"??????????????????????, ??????????????????????????","name":"??????????????????????????",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(83.117012 54.957154)"},"type":"street"},{"id":"141476222741037","full_name":"??????????????????????, ??????????????","name":"??????????????",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(82.907377 55.046271)"},"type":"street"},{"id":"141476222741093","full_name":"??????????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(82.896182 55.038817)"},"type":"street"},{"id":"141476222736979","full_name":"????????????, ????????????","name":"???????????? ????????????????",
        "adm_div":[{"id":"141360258613257","type":"city","name":"??.??????????????"}],"geometry":{"centroid":"POINT(83.094057 54.764479)"},"type":"street"},{"id":"141476222737993","full_name":"????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613257","type":"city","name":"??.??????????????"}],"geometry":{"centroid":"POINT(83.09624 54.762983)"},"type":"street"},{"id":"141476222743450","full_name":"??????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613261","type":"settlement","name":"??.????????????????"}],"geometry":{"centroid":"POINT(82.63858 54.692258)"},"type":"street"},{"id":"141476222738228","full_name":"????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613267","type":"settlement","name":"??????.??????????????"}],"geometry":{"centroid":"POINT(83.050446 55.085258)"},"type":"street"},{"id":"141476222742847","full_name":"??????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613295","type":"settlement","name":"??.????????????????????"}],"geometry":{"centroid":"POINT(83.233834 54.668324)"},"type":"street"},{"id":"141476222737860","full_name":"??????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613297","type":"settlement","name":"??.????????????????????"}],"geometry":{"centroid":"POINT(82.83697 54.81349)"},"type":"street"},{"id":"141476222738173","full_name":"????????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613320","type":"settlement","name":"??.??????????????????????"}],"geometry":{"centroid":"POINT(83.125489 55.059863)"},"type":"street"},{"id":"141476222743842","full_name":"????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613328","type":"settlement","name":"??.??????????????????"}],"geometry":{"centroid":"POINT(82.561057 55.202712)"},"type":"street"},{"id":"141476222742386","full_name":"??????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613334","type":"settlement","name":"??.????????????????????"}],"geometry":{"centroid":"POINT(83.281673 54.706342)"},"type":"street"},{"id":"141476222743312","full_name":"??????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613338","type":"settlement","name":"??.????????????????"}],"geometry":{"centroid":"POINT(82.923493 54.575265)"},"type":"street"},{"id":"141476222736804","full_name":"????????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613349","type":"settlement","name":"??.??????????????????"}],"geometry":{"centroid":"POINT(83.188508 54.96393)"},"type":"street"},{"id":"141476222742993","full_name":"??????????????, ????????????","name":"????????????",
        "adm_div":[{"id":"141360258613350","type":"city","name":"??.????????????????"}],"geometry":{"centroid":"POINT(83.308991 54.645084)"},"type":"street"},{"id":"141476222743227","full_name":"??????????????, ??????????????????????????","name":"??????????????????????????",
        "adm_div":[{"id":"141360258613350","type":"city","name":"??.????????????????"}],"geometry":{"centroid":"POINT(83.288636 54.633768)"},"type":"street"},{"id":"141476222740266","full_name":"??????????????????????, ?????????????????? 2-??","name":"?????????????????? 2-??",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(82.965531 54.961957)"},"type":"street"},{"id":"141476222740513","full_name":"??????????????????????, ????????????","name":"???????????? ??????????????",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(82.919813 55.030421)"},"type":"street"},{"id":"141476222745007","full_name":"??????????????????????, ???????? ??????????????","name":"???????? ??????????????",
        "adm_div":[{"id":"141360258613345","type":"city","name":"??.????????????????????????"}],"geometry":{"centroid":"POINT(82.96928 54.95416)"},"type":"street"}]}}
         */

        //LOGGER.info("map: "+map);



        //String[] split =
        //int idxStart = point.indexOf("(");
        //point.substring(idxStart+1, point.length()-1);



        //LOGGER.info("result: "+point.substring(idxStart+1, point.length()-1));

        String host = "192.168.1.1";

        if(!StringUtils.isEmpty(host)){
            String ss[] = host.split(",");
            host = ss[0].trim();
            if(ss.length > 1){
                host = ss[1].trim();
            }
        }
        //LOGGER.info("host: "+host+ " DateTimeUtils.nowNovosib_GMT6().dayOfWeek().get(): "+DateTimeUtils.nowNovosib_GMT6().dayOfWeek().get());


        JSONObject json = new JSONObject("{\"app42Fault\":{\"httpErrorCode\":401,\"appErrorCode\":1401,\"message\":\"UnAuthorized Access\",\"details\":\"Client is not authorized\"}}");

        BigDecimal roundThreeCalc = new BigDecimal("0");
        BigDecimal myremainder = new BigDecimal("0");

        BigDecimal var3600 = new BigDecimal("3600");
        BigDecimal var60 = new BigDecimal("60");

        myremainder = roundThreeCalc.remainder(var3600);

        String format = "2015-04-13 16:36:03";
// Format for input
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
// Parsing the date
        DateTime jodatime = dtf.parseDateTime(format);
// Format for output
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd.MM '?' HH:mm");
// Printing the date

    }
}