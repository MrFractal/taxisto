package ru.trendtech.services.push;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.domain.DeviceInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DevicesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DevicesUtils.class);

    private static Collection<DeviceInfo> findByType(List<DeviceInfo> devices, Predicate<DeviceInfo> deviceInfoPredicate) {
        return Collections2.filter(devices, deviceInfoPredicate);
    }

    public static List<DeviceInfo> findValidAndroidDevices(List<DeviceInfo> devices) {
        Collection<DeviceInfo> androids = findByType(devices, new HasTypePredicate(DeviceInfo.Type.ANDROID));
        return Collections.unmodifiableList(new ArrayList<>(androids));
    }

    public static List<DeviceInfo> findValidAppleDevices(List<DeviceInfo> devices) {
        Collection<DeviceInfo> apples = findByType(devices, new HasTypePredicate(DeviceInfo.Type.APPLE));
        Collection<DeviceInfo> validApples = Collections2.filter(apples, new PredicateIsValidApple());
        return Collections.unmodifiableList(new ArrayList<>(validApples));
    }

    public static List<DeviceInfo> findInvalidAppleDevices(List<DeviceInfo> devices) {
        Collection<DeviceInfo> apples = findByType(devices, new HasTypePredicate(DeviceInfo.Type.APPLE));
        Collection<DeviceInfo> invalidApples = Collections2.filter(apples, new PredicateIsInvalidApple());
        return Collections.unmodifiableList(new ArrayList<>(invalidApples));
    }

    private static boolean isValidToken(DeviceInfo deviceInfo) {
        boolean result = false;
        try {
            BasicDevice.validateTokenFormat(deviceInfo.getToken());
            result = true;
        } catch (InvalidDeviceTokenFormatException e) {
            LOGGER.error("Problem with building device for notifications!! Skip it!! Device info: " + deviceInfo, e);
        }
        return result;
    }

    private static class HasTypePredicate implements Predicate<DeviceInfo> {
        private DeviceInfo.Type type;

        private HasTypePredicate(DeviceInfo.Type type) {
            this.type = type;
        }

        @Override
        public boolean apply(DeviceInfo deviceInfo) {
            return type.equals(deviceInfo.getType());
        }
    }

    private static class PredicateIsInvalidApple extends PredicateIsValidApple {
        @Override
        public boolean apply(DeviceInfo deviceInfo) {
            return !super.apply(deviceInfo);
        }
    }

    private static class PredicateIsValidApple implements Predicate<DeviceInfo> {
        @Override
        public boolean apply(DeviceInfo deviceInfo) {
            return isValidToken(deviceInfo);
        }
    }
}
