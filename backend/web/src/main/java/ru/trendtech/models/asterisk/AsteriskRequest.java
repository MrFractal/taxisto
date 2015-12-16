package ru.trendtech.models.asterisk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

/**
 * Created by max on 12.04.2014.
 */
public class AsteriskRequest {

    private static final String ASTERISK_URL = "http://109.120.150.51/api.php";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("YYYYMMdd").withZone(DateTimeZone.UTC);
    private String requestId = "-1";
    private String action;
    private String phone;
    private String date; // YYYYMMDD

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @JsonIgnore
    public ActionType getActionType() {
        return ActionType.getByValue(action);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @JsonIgnore
    public LocalDate getTripDate() {
        LocalDate result = null;
        if (!StringUtils.isEmpty(date)) {
            result = DATE_TIME_FORMATTER.parseLocalDate(date);
        }
        return result;
    }

    public enum ActionType {
        CLIENT_NAME("get_client_name"),
        DETAILED_BILLING("get_detailed_calc"),
        DETAILED_BILLING_INIT("send_calc", ASTERISK_URL),
        DRIVER_ARRIVING_TIME("get_time_to_start"),
        SEARCH_DRIVER_ID("get_driver_id"),
        TRIP_COST("get_trip_cost"),
        DRIVER_REGISTERED("driver_registrer", ASTERISK_URL),
        CLIENT_REGISTERED("client_registrer", ASTERISK_URL),
        TEST_SERVER_CALLS("test_server_calls"),;

        private final String action;
        private String requestUrl = "";

        ActionType(String action) {
            this.action = action;
        }

        ActionType(String action, String requestUrl) {
            this(action);
            this.requestUrl = requestUrl;
        }

        public static ActionType getByValue(String value) {
            ActionType result = null;
            for (ActionType actionType : ActionType.values()) {
                if (actionType.getAction().equals(value)) {
                    result = actionType;
                    break;
                }
            }
            return result;
        }

        public String getAction() {
            return action;
        }

        public String getRequestUrl() {
            return requestUrl;
        }
    }
}
