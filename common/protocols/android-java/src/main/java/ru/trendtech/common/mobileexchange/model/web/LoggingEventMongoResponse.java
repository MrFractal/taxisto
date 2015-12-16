package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.logging.LoggingEventInfoMongo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 28.10.2014.
 */
public class LoggingEventMongoResponse {
    private List<LoggingEventInfoMongo> loggingEventInfoMongoList = new ArrayList<LoggingEventInfoMongo>();
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public List<LoggingEventInfoMongo> getLoggingEventInfoMongoList() {
        return loggingEventInfoMongoList;
    }

    public void setLoggingEventInfoMongoList(List<LoggingEventInfoMongo> loggingEventInfoMongoList) {
        this.loggingEventInfoMongoList = loggingEventInfoMongoList;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
