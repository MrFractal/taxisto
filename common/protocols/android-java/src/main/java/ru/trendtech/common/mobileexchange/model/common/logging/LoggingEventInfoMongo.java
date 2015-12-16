package ru.trendtech.common.mobileexchange.model.common.logging;

import ru.trendtech.common.mobileexchange.model.common.ClientInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.web.WebUserModel;

/**
 * Created by petr on 23.10.2014.
 */

public class LoggingEventInfoMongo {
    private String id;
    private String security_token;
    private DriverInfo driverInfo;
    private WebUserModel webUserModel;
    private Long missionId;
    private String jsonQuery;
    private String queryType;
    private String city;
    private Long dateTime;
    private boolean success;
    private String errorMessage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public WebUserModel getWebUserModel() {
        return webUserModel;
    }

    public void setWebUserModel(WebUserModel webUserModel) {
        this.webUserModel = webUserModel;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getJsonQuery() {
        return jsonQuery;
    }

    public void setJsonQuery(String jsonQuery) {
        this.jsonQuery = jsonQuery;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
