package ru.trendtech.services.wifi;

import ru.trendtech.domain.Mission;

/**
 * Created by petr on 06.07.2015.
 */
public interface WiFiConnection {
    String openConnection(Mission mission);
    String closeConnection(Mission mission);
}
