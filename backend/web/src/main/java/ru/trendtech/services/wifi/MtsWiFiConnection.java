package ru.trendtech.services.wifi;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.utils.HTTPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 06.07.2015.
 */
@Service(value = "mts")
@Transactional
public class MtsWiFiConnection implements WiFiConnection {
    //https://81.201.240.178/fw?action=open&ip=172.30.0.1&MSISDN=79*********
    private static final String MTS_URL_CONNECTION = "https://81.201.240.178/fw?";
    private List<NameValuePair> urlParameters = new ArrayList<>();

    @Override
    public String openConnection(Mission mission) {
        return HTTPUtil.sendHttpQuery(MTS_URL_CONNECTION, fillAddressParameters(true, mission.getDriverInfo()));
    }

    @Override
    public String closeConnection(Mission mission) {
        return HTTPUtil.sendHttpQuery(MTS_URL_CONNECTION, fillAddressParameters(false, mission.getDriverInfo()));
    }


    private List<NameValuePair> fillAddressParameters(boolean isOpen, Driver driver){
        urlParameters.clear();
        urlParameters.add(new BasicNameValuePair("action", isOpen ? "open": "close"));
        urlParameters.add(new BasicNameValuePair("ip", driver.getRouter().getIp()));
        urlParameters.add(new BasicNameValuePair("MSISDN", driver.getRouter().getSimNumber()));
        return urlParameters;
    }



}
