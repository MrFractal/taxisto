package ru.trendtech.utils;

import com.google.common.collect.Lists;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.billing.AutoClassPrice;
import ru.trendtech.domain.billing.MissionRate;

import java.util.List;
import java.util.Set;

/**
 * Created by petr on 08.07.2015.
 */



public class MissionRateUtils {


    public static AutoClassPrice getAutoClassPrice(MissionRate missionRate, AutoClass autoClass){
        AutoClassPrice result = null;
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
        List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
        for(AutoClassPrice autoClassPriceDB :autoClassPriceList){
            if(autoClass.getValue()==autoClassPriceDB.getAutoClass().getValue()){
                result = autoClassPriceDB;
            }
        }
          return result;
    }


}

