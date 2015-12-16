package ru.trendtech.controllers;

import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverInfoARM;
import ru.trendtech.common.mobileexchange.model.driver.RegisterDriverRequest;
import ru.trendtech.common.mobileexchange.model.driver.RegisterDriverResponse;

import java.util.Arrays;

import static ru.trendtech.controllers.ProfilesUtils.driverUrl;

/**
 * Created by petr on 3/20/14.
 */
@Test
public class BuildDefaultUsers {
    static {
        ProfilesUtils.loadProfiles();
    }

    @Test
    public void createDriverSveta() throws Exception {
        RegisterDriverRequest request = new RegisterDriverRequest();

        DriverInfoARM driverInfo = buildDriverSveta();

        request.setDriverInfoARM(driverInfo);
        RegisterDriverResponse response = Utils.template().postForObject(driverUrl("/register"), request, RegisterDriverResponse.class);
        long driverId = response.getDriverId();
    }

    private DriverInfoARM buildDriverSveta() {
        DriverInfoARM driverInfo = new DriverInfoARM();
        driverInfo.setFirstName("Брынкова");
        driverInfo.setLastName("Светлана");
        driverInfo.setAutoModel("Nissan Sunny");
        driverInfo.setAutoClass(1);
        driverInfo.setAutoColor("Вишневый");
        driverInfo.setAutoNumber("А166НР 54");
        driverInfo.setBalance(1000);
        driverInfo.setTotalRating(0);
        driverInfo.setPassword("123456");
        driverInfo.setPhone("+79139003179");
        driverInfo.setAutoClass(1);
        driverInfo.setPhotoPicture(ProfilesUtils.getPicture("driver.jpg"));
        driverInfo.getPhotosCarsPictures().addAll(Arrays.asList(ProfilesUtils.getPicture("car1.jpg")));
        return driverInfo;
    }


}
