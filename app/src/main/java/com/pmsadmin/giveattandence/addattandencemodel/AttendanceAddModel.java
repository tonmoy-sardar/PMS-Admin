
package com.pmsadmin.giveattandence.addattandencemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceAddModel {

    @SerializedName("request_status")
    @Expose
    private Integer requestStatus;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
