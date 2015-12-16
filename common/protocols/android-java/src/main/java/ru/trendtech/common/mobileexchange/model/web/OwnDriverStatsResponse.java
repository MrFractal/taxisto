package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.OwnDriverStatsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 09.02.2015.
 */
public class OwnDriverStatsResponse extends ErrorCodeHelper{
    private List<OwnDriverStatsInfo> ownDriverStatsInfos = new ArrayList<OwnDriverStatsInfo>();
    private GeneralStatsInfo generalStatsInfo = new GeneralStatsInfo();

    public GeneralStatsInfo getGeneralStatsInfo() {
        return generalStatsInfo;
    }

    public void setGeneralStatsInfo(GeneralStatsInfo generalStatsInfo) {
        this.generalStatsInfo = generalStatsInfo;
    }

    public List<OwnDriverStatsInfo> getOwnDriverStatsInfos() {
        return ownDriverStatsInfos;
    }

    public void setOwnDriverStatsInfos(List<OwnDriverStatsInfo> ownDriverStatsInfos) {
        this.ownDriverStatsInfos = ownDriverStatsInfos;
    }



    public class GeneralStatsInfo{
        private int generalCountCompletedMission;
        private int generalSumCompletedMission;
        private String generalFreeRest;
        private String generalPayRest;
        private String generalCountWorkflow;
        private int generalSalary;
        private int countDrivers;

        public int getCountDrivers() {
            return countDrivers;
        }

        public void setCountDrivers(int countDrivers) {
            this.countDrivers = countDrivers;
        }

        public int getGeneralCountCompletedMission() {
            return generalCountCompletedMission;
        }

        public void setGeneralCountCompletedMission(int generalCountCompletedMission) {
            this.generalCountCompletedMission = generalCountCompletedMission;
        }

        public int getGeneralSumCompletedMission() {
            return generalSumCompletedMission;
        }

        public void setGeneralSumCompletedMission(int generalSumCompletedMission) {
            this.generalSumCompletedMission = generalSumCompletedMission;
        }

        public String getGeneralFreeRest() {
            return generalFreeRest;
        }

        public void setGeneralFreeRest(String generalFreeRest) {
            this.generalFreeRest = generalFreeRest;
        }

        public String getGeneralPayRest() {
            return generalPayRest;
        }

        public void setGeneralPayRest(String generalPayRest) {
            this.generalPayRest = generalPayRest;
        }

        public String getGeneralCountWorkflow() {
            return generalCountWorkflow;
        }

        public void setGeneralCountWorkflow(String generalCountWorkflow) {
            this.generalCountWorkflow = generalCountWorkflow;
        }

        public int getGeneralSalary() {
            return generalSalary;
        }

        public void setGeneralSalary(int generalSalary) {
            this.generalSalary = generalSalary;
        }
    }
}
