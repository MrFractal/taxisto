package ru.trendtech.integration.payonline;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by petr on 25.09.2015.
 */
/*
  <threedSecure>
    <pareq>eJxVUl1TwjAQ/CtMH52x+WgLlDnCFBmV8Qu1Iq8ljW3VpJi2Cv56k1oE33b3ks3dXmCyle+9T6GrolRjh7jY6QnFy7RQ2dh5is9Ph86EQZxrIWaPgjdaMLgRVZVkolekY2cRPYiP0TJbXg0W0Td+yeXFYkfVRRCvModBW2bQPcCMv0sB7alx0jxPVM0g4R/T+S3z+wHFfUAdBSn0fNapmHpDEmBMAP3KoBIp2NX59CSOVvPH+A5QqwAvG1XrHev7HqA9gUa/s7yuNyOEvLRypVwn6s3VDQJkS4AO3SwaiypjtS1S9qXq6/vwuZRb/Xa9jpXU9LUM51nE+RiQPQFpUgtGsWkvpEGPeCPqjQIzR6tDIm0Ppo7NZL8YNvaJ6KhwLIAJWps97EfYMxDbTamEvQLoD0MqKs6oyaUFgA7tn13aaHlt0iI+JaFHfRK6Qxr0gxB7wQB7lLZxt0esfWGiIgOCW39LAFkT1G0Sdcs36N+n+AHRAr0j</pareq>
    <acsurl>https://acs.alfabank.ru/acs/PAReq</acsurl>
    <pd>zC6vgK9bbX0S5WZr/Sq6jNONjIx5pSDaP+E82Mx9l9Pz5fFzdvXynn2t0Kd3k7LF</pd>
  </threedSecure>
 */


@Root(name = "threedSecure")
public class ThreedSecure {
    @Element(name = "pareq", required = true)
    private String paReq;

    @Element(name = "pd", required = true)
    private String pd;

    @Element(name = "acsurl", required = true)
    private String acsUrl;

    public String getPaReq() {
        return paReq;
    }

    public void setPaReq(String paReq) {
        this.paReq = paReq;
    }

    public String getAcsUrl() {
        return acsUrl;
    }

    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

}
