
package com.pmsadmin.attendancelist.approvallistmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovalListModel {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("request_status")
    @Expose
    private Integer requestStatus;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("per_page_count")
    @Expose
    private Integer perPageCount;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getPerPageCount() {
        return perPageCount;
    }

    public void setPerPageCount(Integer perPageCount) {
        this.perPageCount = perPageCount;
    }

}
