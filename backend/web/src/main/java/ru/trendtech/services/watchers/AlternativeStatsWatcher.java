package ru.trendtech.services.watchers;

import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.AlternativeStatistics;
import ru.trendtech.repositories.AlternativeStatisticsRepository;
import ru.trendtech.services.xml.WeatherService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.HTTPUtil;

/**
 * Created by petr on 05.02.2015.
 */

@Service("alternativeStatsWatcher")
public class AlternativeStatsWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlternativeStatsWatcher.class);
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private AlternativeStatisticsRepository alternativeStatisticsRepository;

    @Scheduled(fixedRate = 3600000) // every hour
    @Transactional
    public void alternativeStatsReceive() {

        /* get weather */
        String weather = weatherService.getTemperatureNow();
        if(!StringUtil.isEmpty(weather)){
           alternativeStatisticsRepository.save(new AlternativeStatistics("weather_nsk", weather, DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6())));
        }
        /* end */

        /* get traffic */
        String traffic = getTraffic();
        if(!StringUtil.isEmpty(traffic)){
            alternativeStatisticsRepository.save(new AlternativeStatistics("traffic_nsk", traffic, DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6())));
        }
        /* end */
    }

    private String getTraffic(){
        String url = "http://traffic7.maps.2gis.com/novosibirsk/meta/score/0/";
        String answer = HTTPUtil.senPostQuery(url, null);
        return answer.replaceAll("'","");
    }
}
