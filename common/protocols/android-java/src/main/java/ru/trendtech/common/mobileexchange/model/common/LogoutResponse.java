package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 09.04.2014.
 */
public class LogoutResponse {
    private Details details = new Details();
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public static class Details {
        private boolean completed;
        private int reason;
        private String reasonMessage;

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public int getReason() {
            return reason;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }

        public String getReasonMessage() {
            return reasonMessage;
        }

        public void setReasonMessage(String reasonMessage) {
            this.reasonMessage = reasonMessage;
        }
    }
}
