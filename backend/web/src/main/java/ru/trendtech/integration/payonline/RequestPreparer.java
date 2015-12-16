package ru.trendtech.integration.payonline;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;

/**
 * File created petr on 09/09/2015 10:46.
 */


public class RequestPreparer {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RequestPreparer.class);
    private static final String SECURITY_KEY = "zzz";
    private static final String MERCHANT_ID = "111";
    private static final String PREFIX_URL = "https://secure.payonlinesystem.com/payment/transaction/auth/";
    public static final String COMPLETE_URL = "https://secure.payonlinesystem.com/payment/transaction/complete/";
    public static final String VOID_URL = "https://secure.payonlinesystem.com/payment/transaction/void/";
    public static final String REFUND_URL = "https://secure.payonlinesystem.com/payment/transaction/refund/";
    public static final String CHECK_URL = "https://secure.payonlinesystem.com/payment/transaction/check/";
    public static final String SEARCH_URL ="https://secure.payonlinesystem.com/payment/search/";

    private static final String PAYMENT_URL = "https://secure.payonlinesystem.com/ru/payment/form-mobile?";
    private static final String REBILL_URL = "https://secure.payonlinesystem.com/payment/transaction/rebill/";
    private static final String THREE_DS_URL = "https://secure.payonlinesystem.com/payment/transaction/auth/3ds/";
    private static final String TERM_URL = "http://dev.taxisto.ru/payment/payonline/termInit";



    /*
    MerchantId Идентификатор сайта. Обязательный параметр. Целое число.
2 TransactionId Идентификатор транзакции в системе PayOnline
Обязательный параметр.
Целое число.
3 PARes Значение, полученное страницей TermUrl от банка-
Строка
4 PD Значение, полученное при вызове метода Auth с кодом ошибки
6001
5 SecurityKey Открытый ключ, подтверждающий целостность параметров
6 ContentType Формат вывода результата – текст или xml. Необязательный

     */



    public static PayOnlineRequest completeThreeDS(String transactionId, String pARes, String pd) {
        PayOnlineRequest result = new PayOnlineRequest(THREE_DS_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("TransactionId", transactionId);
        result.addParameter("PaRes", pARes);
        result.addParameter("PD", pd);
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }



    public static PayOnlineRequest search(String transactionId, String orderId) {
        PayOnlineRequest result = new PayOnlineRequest(SEARCH_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        //result.addParameter("OrderId", orderId);
        result.addParameter("TransactionId", transactionId);
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }



    public static PayOnlineRequest rebill(String orderId, double amount, String currency, String rebillAnchor) {
        System.out.println("in the rebill");
        PayOnlineRequest result = new PayOnlineRequest(REBILL_URL);
        try {
            result.addParameter("MerchantId", MERCHANT_ID);
            result.addParameter("RebillAnchor", rebillAnchor); // "16RCWIHGTUiuFgDQHBTEMnNUzcJtzmh2FlRccZGFn2g="
            result.addParameter("OrderId", orderId);
            result.addParameter("Amount", String.format(Locale.US, "%.2f", amount));
            result.addParameter("Currency", currency);
            result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


    public static PayOnlineRequest payment(String orderId, double amount, String currency) {
        PayOnlineRequest result = new PayOnlineRequest(PAYMENT_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("OrderId", orderId);
        result.addParameter("Amount", String.format(Locale.US, "%.2f", amount));
        result.addParameter("Currency", currency);
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }



    public static PayOnlineRequest auth(String orderId, double amount, String currency) {
        PayOnlineRequest result = new PayOnlineRequest(PREFIX_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("OrderId", orderId);
        result.addParameter("Amount", String.format(Locale.US, "%.2f", amount));
        result.addParameter("Currency", currency);
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }



    /*
     <form method='post' action='https://dropit.3dsecure.net:9443/PIT/ACS'>
    <input type='hidden' name='PaReq'
    value='eJxVUdFuwjAM/BXEB+AkLYMhY6mjk+hDEdpg711r0W5rC2m6wb5+SSkwHiL5zvbZOeMm18zhK6et
    ZsKYmybZ8aDI5kMp5Nh78IQQaki4Dl74QPjNuinqiuRIjBTCBdpGneZJZQiT9PAUrchXyldjhB5i
    yToKSSrPt+QZYJWUTIYbUzJCBzCt28roE4mJj3AB2Oovyo3ZzwAu5Y5CuI1dty5qrMSxyChaBLu7
    Fz7/xJvt7+ojniO4CswSw6SElGIipwMpZmM5U48IHY9J6WaTGglh/3AGuHczgj7jEv8JtA5qrtIT
    TX2buiLk476u2LUgXGOE28KLpXMtNdYSHfMq36i37WdWHpbvQRHFay+Y2537AqdWWEuk3byTcwDB
    SUB/ImtKd0Qb3R33D+2QoeE=' />
    <input type='hidden' name='TermUrl' value='http://yoursite.com/3ds.aspx' />
    <input type='hidden' name='MD'
    value='1015368;OXf4nrsM4Oi0N7TbFRrQZdbaFQ8M0Dc0WGZUOdBPZ3C2NXIrKlKObWBLTtzeknQY' />
    <input type='submit' value='Submit' />
    </form>
     */

    public static PayOnlineRequest secureInitialize(String ASC_URL, String pareq, String mD) {
        PayOnlineRequest result = new PayOnlineRequest(ASC_URL);
        result.addParameter("PaReq", pareq);
        result.addParameter("MD", mD);
        result.addParameter("TermUrl", TERM_URL);
        return result;
    }


    public static PayOnlineRequest completeTransaction(String transactionId, double amount) {
        PayOnlineRequest result = new PayOnlineRequest(COMPLETE_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("TransactionId", transactionId);
        result.addParameter("Amount", String.format(Locale.US, "%.2f", amount));
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }

    public static PayOnlineRequest refundTransaction(String transactionId, double amount) {
        PayOnlineRequest result = new PayOnlineRequest(REFUND_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("TransactionId", transactionId);
        result.addParameter("Amount", String.format(Locale.US, "%.2f", amount));
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }

    public static PayOnlineRequest voidTransactions(String transactionId) {
        PayOnlineRequest result = new PayOnlineRequest(VOID_URL);
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("TransactionId", transactionId);
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }

    public static PayOnlineRequest addCardInfo(PayOnlineRequest request, String cardHolder, String number, String expYear, String expMonth, String cvv) {
        request.addParameter("CardHolderName", cardHolder);
        request.addParameter("CardNumber", number);
        request.addParameter("CardExpDate", String.format("%s%s", expYear, expMonth));
        request.addParameter("CardCvv", cvv);
        return request;
    }


    public static PayOnlineRequest check(String orderId, double amount, String currency) {
        PayOnlineRequest result = new PayOnlineRequest(CHECK_URL); // PREFIX_URL
        result.addParameter("MerchantId", MERCHANT_ID);
        result.addParameter("OrderId", orderId);
        result.addParameter("Amount", String.format(Locale.US, "%.2f", amount));
        result.addParameter("Currency", currency);
        result.addParameter("SecurityKey", buildMD5(result.buildParams()));
        return result;
    }



    // https://dev.taxisto.ru/payment/payonline/success?DateTime=2015-09-25+13%3a17%3a40&TransactionID=52177535&OrderId=3%3ac&Amount=1.00&Currency=RUB&SecurityKey=816714c0e5ba22e8b10096f84df1d813&lang=ru&Provider=Card&PaymentAmount=1.00&PaymentCurrency=RUB&CardHolder=ANDREY+SAMSONOV&CardNumber=************6378&Country=&City=&ECI=5&RebillAnchor=Zei.2H7SlbGD-XAtgYqGLp1MFh76vxb0hDih.OrqoqM%3d

    static PayOnlineRequest addCardInfoReal(PayOnlineRequest request){
        request.addParameter("CardHolderName", "ANDREY SAMSONOV");
        request.addParameter("CardNumber", "4154816967496378"); //
        request.addParameter("CardExpDate", String.format("%s%s", "04", "17"));
        request.addParameter("CardCvv", "981");
        /*
        request.addParameter("CardHolderName", "DMITRIY SMIRNOV");
        request.addParameter("CardNumber", "5463390035401540"); //
        request.addParameter("CardExpDate", String.format("%s%s", "09", "17"));
        request.addParameter("CardCvv", "829");
        */
        return request;
    }


    public static PayOnlineRequest addCardInfoTest(PayOnlineRequest request) {
        request.addParameter("CardHolderName", "Petr Rudenko");
        request.addParameter("CardNumber", "4111111111111111");
        request.addParameter("CardExpDate", String.format("%s%s", "12", "16"));
        request.addParameter("CardCvv", "123");
        return request;
    }

    public static PayOnlineRequest addContentType(PayOnlineRequest request, boolean contentXml) {
        request.addParameter("ContentType", contentXml ? "xml" : "text");
        return request;
    }

    private static String buildMD5(String preparedRequest) {
        System.out.println("preparedRequest = " + preparedRequest + " SECURITY_KEY " + SECURITY_KEY);
        String str = preparedRequest + "&PrivateSecurityKey=" + SECURITY_KEY ;
        //String str = String.format(preparedRequest + "&PrivateSecurityKey=%s", SECURITY_KEY);
        Hasher hasher = Hashing.md5().newHasher();
        HashCode hashCode = hasher.putString(str, Charsets.UTF_8).hash();
        return hashCode.toString();
    }

    public static PayOnlineRequest addEmail(PayOnlineRequest request) {
        request.addParameter("Email", "fr@bekker.com.ua"); //
        return request;
    }

    public static PayOnlineRequest addIp(PayOnlineRequest request, String ip) {
        //request.addParameter("Ip", "109.202.18.234");
        request.addParameter("Ip", ip);
        return request;
    }
}
