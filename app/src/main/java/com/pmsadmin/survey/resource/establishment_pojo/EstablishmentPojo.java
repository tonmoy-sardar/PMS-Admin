
package com.pmsadmin.survey.resource.establishment_pojo;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EstablishmentPojo implements Serializable
{

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("request_status")
    @Expose
    private Integer requestStatus;
    @SerializedName("msg")
    @Expose
    private String msg;
    private final static long serialVersionUID = 4619010593918025173L;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
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

}
