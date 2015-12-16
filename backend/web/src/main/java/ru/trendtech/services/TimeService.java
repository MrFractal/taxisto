package ru.trendtech.services;

/**
 * Created by max on 07.02.14.
 */

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import ru.trendtech.utils.DateTimeUtils;

import java.util.Date;

@Service
public class TimeService {
    public Date now() {
        return new Date();
    }

    public DateTime nowDateTime() {
        return DateTimeUtils.now();
    }

    public long nowDateTimeMillis() {
        return nowDateTime().getMillis();
    }

    public long nowMillis() {
        return now().getTime();
    }
}
