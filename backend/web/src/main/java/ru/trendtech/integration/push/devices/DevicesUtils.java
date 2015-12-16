package ru.trendtech.integration.push.devices;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class DevicesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DevicesUtils.class);

    private static Collection<MobileDevice> findByType(List<MobileDevice> devices, Predicate<MobileDevice> deviceInfoPredicate) {
        return Collections2.filter(devices, deviceInfoPredicate);
    }

    public static List<MobileDevice> findValidAndroidDevices(List<MobileDevice> devices) {
        Collection<MobileDevice> androids = findByType(devices, new PredicateHasType(AndroidDevice.class));
        return Collections.unmodifiableList(new ArrayList<>(androids));
    }

    public static List<MobileDevice> findValidAppleDevices(List<MobileDevice> devices) {
        Collection<MobileDevice> apples = findByType(devices, new PredicateHasType(AppleDevice.class));
        Collection<MobileDevice> validApples = Collections2.filter(apples, new PredicateIsValid());
        return Collections.unmodifiableList(new ArrayList<>(validApples));
    }

    public static List<MobileDevice> findInvalidAppleDevices(List<MobileDevice> devices) {
        Collection<MobileDevice> apples = findByType(devices, new PredicateHasType(AppleDevice.class));
        Collection<MobileDevice> invalidApples = Collections2.filter(apples, new PredicateIsInvalid());
        return Collections.unmodifiableList(new ArrayList<>(invalidApples));
    }

    private static class PredicateHasType implements Predicate<MobileDevice> {
        private final Class<?> type;

        private PredicateHasType(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean apply(MobileDevice deviceInfo) {
            return deviceInfo.getClass().equals(type);
        }
    }

    private static class PredicateIsInvalid extends PredicateIsValid {
        @Override
        public boolean apply(MobileDevice device) {
            return !super.apply(device);
        }
    }

    private static class PredicateIsValid implements Predicate<MobileDevice> {
        @Override
        public boolean apply(MobileDevice device) {
            return device.isValid();
        }
    }
}
