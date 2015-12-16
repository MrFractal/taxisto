package ru.trendtech.common.mobileexchange.model.common.scores;

/**
 * File created by max on 01/05/2014 12:48.
 */

/**
 * I'ts is class with scores for mission by client
 * it's contains general score in interval [1..5] for mission and details if general score is in [1..3]
 * each field can be "0" value and it's means it's not scored by client
 */

public class Scores {
    private int general = 0;

    private  Details details = new Details(); // было final

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public Details getDetails() {
        return details;
    }
    public void setDetails(Details details) {
        this.details=details;
    }

    public static class Details{
        private int cleanlinessInCar = 0;
        private int waitingTime = 0;
        private int driverCourtesy = 0;
        private int applicationConvenience = 0;
        private int wifiQuality = 0;
        private String estimate_comment;

        public String getEstimate_comment() {
            return estimate_comment;
        }

        public void setEstimate_comment(String estimate_comment) {
            this.estimate_comment = estimate_comment;
        }

        public int getCleanlinessInCar() {
            return cleanlinessInCar;
        }

        public void setCleanlinessInCar(int cleanlinessInCar) {
            this.cleanlinessInCar = cleanlinessInCar;
        }

        public int getWaitingTime() {
            return waitingTime;
        }

        public void setWaitingTime(int waitingTime) {
            this.waitingTime = waitingTime;
        }

        public int getDriverCourtesy() {
            return driverCourtesy;
        }

        public void setDriverCourtesy(int driverCourtesy) {
            this.driverCourtesy = driverCourtesy;
        }

        public int getApplicationConvenience() {
            return applicationConvenience;
        }

        public void setApplicationConvenience(int applicationConvenience) {
            this.applicationConvenience = applicationConvenience;
        }

        public int getWifiQuality() {
            return wifiQuality;
        }

        public void setWifiQuality(int wifiQuality) {
            this.wifiQuality = wifiQuality;
        }
    }
}
