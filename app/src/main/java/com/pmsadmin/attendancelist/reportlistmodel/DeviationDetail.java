
package com.pmsadmin.attendancelist.reportlistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviationDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("from_time")
    @Expose
    private String fromTime;
    @SerializedName("to_time")
    @Expose
    private String toTime;
    @SerializedName("deviation_time")
    @Expose
    private String deviationTime;
    @SerializedName("deviation_type")
    @Expose
    private String deviationType;
    @SerializedName("justification")
    @Expose
    private Object justification;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("remarks")
    @Expose
    private Object remarks;
    @SerializedName("justified_at")
    @Expose
    private String justifiedAt;
    @SerializedName("approved_at")
    @Expose
    private String approvedAt;
    @SerializedName("attandance")
    @Expose
    private Integer attandance;
    @SerializedName("justified_by")
    @Expose
    private Object justifiedBy;
    @SerializedName("approved_by")
    @Expose
    private Object approvedBy;
    @SerializedName("owned_by")
    @Expose
    private Integer ownedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDeviationTime() {
        return deviationTime;
    }

    public void setDeviationTime(String deviationTime) {
        this.deviationTime = deviationTime;
    }

    public String getDeviationType() {
        return deviationType;
    }

    public void setDeviationType(String deviationType) {
        this.deviationType = deviationType;
    }

    public Object getJustification() {
        return justification;
    }

    public void setJustification(Object justification) {
        this.justification = justification;
    }

    public Integer getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(Integer approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getJustifiedAt() {
        return justifiedAt;
    }

    public void setJustifiedAt(String justifiedAt) {
        this.justifiedAt = justifiedAt;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Integer getAttandance() {
        return attandance;
    }

    public void setAttandance(Integer attandance) {
        this.attandance = attandance;
    }

    public Object getJustifiedBy() {
        return justifiedBy;
    }

    public void setJustifiedBy(Object justifiedBy) {
        this.justifiedBy = justifiedBy;
    }

    public Object getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Object approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
    }

}
