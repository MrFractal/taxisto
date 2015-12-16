package ru.trendtech.services.wifi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.Mission;

/**
 * Created by petr on 06.07.2015.
 */
@Service(value = "beeline")
@Transactional
public class BeelineWiFiConnection implements WiFiConnection {

    @Override
    public String openConnection(Mission mission) {
        return null;
    }

    @Override
    public String closeConnection(Mission mission) {
        return null;
    }
}
