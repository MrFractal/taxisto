package ru.trendtech.common.mobileexchange.model.common.corporate;

import ru.trendtech.common.mobileexchange.model.common.MissionInfoARM;
import ru.trendtech.common.mobileexchange.model.web.ArticleAdjustmentsInfo;

/**
 * Created by petr on 07.04.2015.
 */
public class CorporateClientCashFlowInfo {
    private Long id;
    private long dateOperation;
    private long client;
    private long mainClient;
    private int operation;
    private MissionInfoARM missionInfo;
    private ArticleAdjustmentsInfo articleInfo;
    private int sum;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(long dateOperation) {
        this.dateOperation = dateOperation;
    }

    public long getClient() {
        return client;
    }

    public void setClient(long client) {
        this.client = client;
    }

    public long getMainClient() {
        return mainClient;
    }

    public void setMainClient(long mainClient) {
        this.mainClient = mainClient;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public MissionInfoARM getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfoARM missionInfo) {
        this.missionInfo = missionInfo;
    }

    public ArticleAdjustmentsInfo getArticleInfo() {
        return articleInfo;
    }

    public void setArticleInfo(ArticleAdjustmentsInfo articleInfo) {
        this.articleInfo = articleInfo;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
