package ru.trendtech.utils;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import ru.trendtech.domain.DeviceInfo;

import java.util.List;
import java.util.Set;

/**
 * Created by petr on 07.11.2014.
 */
public class DeviceUtil {
      public static String getClientType(Set<DeviceInfo> deviceInfoSet){
          String clientType = "";
          if(deviceInfoSet!=null && !deviceInfoSet.isEmpty()) {
              for (DeviceInfo deviceInfo : deviceInfoSet) {
                  if (deviceInfo != null) {
                      if (deviceInfo.getType() != null) {
                          if (deviceInfo.getType().equals(DeviceInfo.Type.ANDROID_CLIENT)) {
                              clientType="ANDROID_CLIENT";
                          } else if (deviceInfo.getType().equals(DeviceInfo.Type.APPLE)) {
                              clientType="APPLE";
                          }
                      }
                  }
              }
          }
            return clientType;
      }



    public static long getDeviceId(Set<DeviceInfo> deviceInfoSet) {
        long deviceId=0;
        if (deviceInfoSet != null) {
            List<DeviceInfo> myList = Lists.newArrayList(deviceInfoSet.iterator());
            if (!myList.isEmpty()) {
                DeviceInfo di = myList.get(0);
                if (di != null) {
                    deviceId = di.getId();
                }
            }
        }
          return deviceId;
    }


    public static DeviceInfo getDeviceInfo(Set<DeviceInfo> deviceInfoSet) {
        DeviceInfo deviceInfo = null;
        if (deviceInfoSet != null) {
            List<DeviceInfo> myList = Lists.newArrayList(deviceInfoSet.iterator());
            if (!myList.isEmpty()) {
                 deviceInfo = myList.get(0);
            }
        }
        return deviceInfo;
    }


}
