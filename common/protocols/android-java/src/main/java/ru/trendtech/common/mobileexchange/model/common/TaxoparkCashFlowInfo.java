package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.web.ArticleAdjustmentsInfo;

/**
 * Created by petr on 20.07.2015.
 */
public class TaxoparkCashFlowInfo {
    private Long id;
    private long dateOperation;
    private TaxoparkPartnersInfo taxoparkPartnersInfo;
    private MissionInfoARM missionInfo;
    private ArticleAdjustmentsInfo articleInfo;
    private int operation;
    private int sum;
    private String comment;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArticleAdjustmentsInfo getArticleInfo() {
        return articleInfo;
    }

    public void setArticleInfo(ArticleAdjustmentsInfo articleInfo) {
        this.articleInfo = articleInfo;
    }

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

    public TaxoparkPartnersInfo getTaxoparkPartnersInfo() {
        return taxoparkPartnersInfo;
    }

    public void setTaxoparkPartnersInfo(TaxoparkPartnersInfo taxoparkPartnersInfo) {
        this.taxoparkPartnersInfo = taxoparkPartnersInfo;
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
