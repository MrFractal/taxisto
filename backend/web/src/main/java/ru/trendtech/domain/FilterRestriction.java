package ru.trendtech.domain;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

/**
 * Created by petr on 08.07.2015.
 */
public enum FilterRestriction {
    less{
        @Override
        public SimpleExpression apply(String property, Object value){
            return Restrictions.lt(property, value);
        }
    },
    great{
        @Override
        public SimpleExpression apply(String property, Object value){
            return Restrictions.gt(property, value);
        }
    }
    ;

    public abstract SimpleExpression apply(String property, Object value);
}
