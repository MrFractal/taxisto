package ru.trendtech.services.logging;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/12/12
 * Time: 12:35 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Log {
}