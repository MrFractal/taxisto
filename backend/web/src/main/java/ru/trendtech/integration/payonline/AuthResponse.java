package ru.trendtech.integration.payonline;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * File created by petr on 11/09/2015 14:00.
 */

/*
 <transaction>
          <id>51208773</id>
          <operation>Auth</operation>
          <result>Ok</result>
          <code>200</code>
          <status>PreAuthorized</status>
          <MerchantId>59291</MerchantId>
          <ipCountry>RU</ipCountry>
          <binCountry>US</binCountry>
      </transaction>
 */


    /*

       <transaction>
  <id>52178039</id>
  <operation>Rebill</operation>
  <result>Error</result>
  <status>Awaiting3DAuthentication</status>
  <code>6001</code>
  <errorCode>4</errorCode>
  <threedSecure>
    <pareq>eJxVUl1TwjAQ/CtMH52x+WgLlDnCFBmV8Qu1Iq8ljW3VpJi2Cv56k1oE33b3ks3dXmCyle+9T6GrolRjh7jY6QnFy7RQ2dh5is9Ph86EQZxrIWaPgjdaMLgRVZVkolekY2cRPYiP0TJbXg0W0Td+yeXFYkfVRRCvModBW2bQPcCMv0sB7alx0jxPVM0g4R/T+S3z+wHFfUAdBSn0fNapmHpDEmBMAP3KoBIp2NX59CSOVvPH+A5QqwAvG1XrHev7HqA9gUa/s7yuNyOEvLRypVwn6s3VDQJkS4AO3SwaiypjtS1S9qXq6/vwuZRb/Xa9jpXU9LUM51nE+RiQPQFpUgtGsWkvpEGPeCPqjQIzR6tDIm0Ppo7NZL8YNvaJ6KhwLIAJWps97EfYMxDbTamEvQLoD0MqKs6oyaUFgA7tn13aaHlt0iI+JaFHfRK6Qxr0gxB7wQB7lLZxt0esfWGiIgOCW39LAFkT1G0Sdcs36N+n+AHRAr0j</pareq>
    <acsurl>https://acs.alfabank.ru/acs/PAReq</acsurl>
    <pd>zC6vgK9bbX0S5WZr/Sq6jNONjIx5pSDaP+E82Mx9l9Pz5fFzdvXynn2t0Kd3k7LF</pd>
  </threedSecure>
</transaction>

     */


@Root(name = "transaction")
public class AuthResponse extends PayOnlineResponse {
    @Element(name = "id", required = true)
    private String id;

    @Element(name = "operation", required = true)
    private String operation;

    @Element(name = "result", required = false)
    private String result;

    @Element(name = "code", required = false)
    private String code;

    @Element(name = "status", required = false)
    private String status;

    @Element(name = "MerchantId", required = false)
    private String MerchantId;

    @Element(name = "ipCountry", required = false)
    private String ipCountry;

    @Element(name = "binCountry", required = false)
    private String binCountry;

    @Element(name = "dateTime", required = false)
    private String dateTime;

    @Element(name = "amount", required = false)
    private String amount;

    // ---------------

    @Element(name = "ErrorCode", required = false)
    private String errorCode;

    @Element(name = "RebillAnchor", required = false)
    private String rebillAnchor;

    @Element(name = "SpecialConditions", required = false)
    private String SpecialConditions;

    @Element(name = "Message", required = false)
    private String message;

    @Element(name = "threedSecure", required = false)
    private ThreedSecure threedSecure;

    public ThreedSecure getThreedSecure() {
        return threedSecure;
    }

    public void setThreedSecure(ThreedSecure threedSecure) {
        this.threedSecure = threedSecure;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getIpCountry() {
        return ipCountry;
    }

    public void setIpCountry(String ipCountry) {
        this.ipCountry = ipCountry;
    }

    public String getBinCountry() {
        return binCountry;
    }

    public void setBinCountry(String binCountry) {
        this.binCountry = binCountry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getRebillAnchor() {
        return rebillAnchor;
    }

    public void setRebillAnchor(String rebillAnchor) {
        this.rebillAnchor = rebillAnchor;
    }

    public String getSpecialConditions() {
        return SpecialConditions;
    }

    public void setSpecialConditions(String specialConditions) {
        SpecialConditions = specialConditions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
