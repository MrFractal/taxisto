package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.DeviceInfo;

import java.util.List;

/**
 * Created by max on 06.02.14.
 */
@Repository
public interface DevicesRepository extends CrudRepository<DeviceInfo, Long> {
    List<DeviceInfo> findByTokenAndType(String token, DeviceInfo.Type type);

    @Query("select count(d) from DeviceInfo d where d.type = ?1")
    public int findCountByDeviceType(DeviceInfo.Type deviceType);
}