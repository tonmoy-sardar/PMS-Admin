
package com.pmsadmin.giveattandence.listattandencemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceListModel {

    @SerializedName("request_status")
    @Expose
    private Integer requestStatus;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
