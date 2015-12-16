package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 27.10.2014.
 */

public class QueryDetails {
   private String nameParam;
   private String operationQuery;  // <, >, =, y < r < x
   private long start;
   private long end;
   private Object equal;

    public String getNameParam() {
        return nameParam;
    }

    public void setNameParam(String nameParam) {
        this.nameParam = nameParam;
    }

    public String getOperationQuery() {
        return operationQuery;
    }

    public void setOperationQuery(String operationQuery) {
        this.operationQuery = operationQuery;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Object getEqual() {
        return equal;
    }

    public void setEqual(Object equal) {
        this.equal = equal;
    }
}
