package ru.trendtech.services.logging;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.trendtech.utils.HTTPUtil;


/**
 * Created by petr on 14.08.2015.
 */
@Component
public class GrayLogService {
    @Value("${greylog.host}")
    private String greylogHost;


    public void sendToGrayLog(long clientId, long driverId, long webUserId, String mess, String controller, long missionId, String source, String text1, String text2, String text3) {
           HTTPUtil.senRestQuery(greylogHost, createJsonGelfMessage(clientId, driverId, webUserId, mess, controller, missionId, source, text1, text2, text3));
    }


    private JSONObject createJsonGelfMessage(long clientId, long driverId, long webUserId, String mess, String controller, long missionId, String host, String text1, String text2, String text3){
        JSONObject json = new JSONObject();
        try {

            if(!StringUtils.isEmpty(host)){
                String str[] = host.split(",");
            }

            json.put("clientId", clientId);
            json.put("driverId", driverId);
            json.put("webUserId", webUserId);
            json.put("short_message", mess);
            json.put("controller", controller);
            json.put("missionId", missionId);
            json.put("host", host);
            json.put("text1", text1);
            json.put("text2", text2);
            json.put("text3", text3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}
