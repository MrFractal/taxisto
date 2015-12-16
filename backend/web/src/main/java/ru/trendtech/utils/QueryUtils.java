package ru.trendtech.utils;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by petr on 15.06.2015.
 */
public class QueryUtils {


    public static int getCount(String query, HashMap<String, Object> params, EntityManager entityManager){
        Session session = entityManager.unwrap(Session.class);
        Query q1 = session.createSQLQuery(query);
        for(Map.Entry entry: params.entrySet()){
            String key = entry.getKey().toString();
            q1.setParameter(key, entry.getValue());
        }
        BigInteger resultAllCount = (BigInteger)q1.uniqueResult();
        return resultAllCount.intValue();
    }



    public static Criteria fillDateTimeParameter(Criteria criteria, String nameParameter, long startDateTime, long endDateTime){
        if(startDateTime != 0 && endDateTime != 0){
            criteria.add(Restrictions.ge(nameParameter, new DateTime(new Date(startDateTime * 1000))));
            criteria.add(Restrictions.lt(nameParameter, new DateTime(new Date(endDateTime * 1000))));
        }else if(startDateTime != 0 && endDateTime == 0){
            criteria.add(Restrictions.ge(nameParameter, new DateTime(new Date(startDateTime * 1000))));
        }else if(startDateTime == 0 && endDateTime != 0){
            criteria.add(Restrictions.lt(nameParameter, new DateTime(new Date(endDateTime * 1000))));
        }
        return criteria;
    }
}
