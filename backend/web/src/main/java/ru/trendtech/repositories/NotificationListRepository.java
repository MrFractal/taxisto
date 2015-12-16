package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.Notification;
import ru.trendtech.domain.PushType;

/**
 * Created by petr on 08.07.2015.
 */
@Repository
public interface NotificationListRepository extends CrudRepository<Notification, Long> {
     Notification findByMissionAndPushType(Mission mission, PushType pushType);
}
