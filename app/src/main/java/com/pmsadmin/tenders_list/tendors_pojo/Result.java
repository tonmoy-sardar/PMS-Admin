
package com.pmsadmin.tenders_list.tendors_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tender_g_id")
    @Expose
    private String tenderGId;
    @SerializedName("tender_final_date")
    @Expose
    private String tenderFinalDate;
    @SerializedName("tender_opened_on")
    @Expose
    private String tenderOpenedOn;
    @SerializedName("tender_added_by")
    @Expose
    private String tenderAddedBy;
    @SerializedName("tender_received_on")
    @Expose
    private String tenderReceivedOn;
    @SerializedName("tender_aasigned_to")
    @Expose
    private String tenderAasignedTo;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("status")
    @Expose
    private Boolean status;
    private final static long serialVersionUID = 2197601085982297491L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenderGId() {
        return tenderGId;
    }

    public void setTenderGId(String tenderGId) {
        this.tenderGId = tenderGId;
    }

    public String getTenderFinalDate() {
        return tenderFinalDate;
    }

    public void setTenderFinalDate(String tenderFinalDate) {
        this.tenderFinalDate = tenderFinalDate;
    }

    public String getTenderOpenedOn() {
        return tenderOpenedOn;
    }

    public void setTenderOpenedOn(String tenderOpenedOn) {
        this.tenderOpenedOn = tenderOpenedOn;
    }

    public String getTenderAddedBy() {
        return tenderAddedBy;
    }

    public void setTenderAddedBy(String tenderAddedBy) {
        this.tenderAddedBy = tenderAddedBy;
    }

    public String getTenderReceivedOn() {
        return tenderReceivedOn;
    }

    public void setTenderReceivedOn(String tenderReceivedOn) {
        this.tenderReceivedOn = tenderReceivedOn;
    }

    public String getTenderAasignedTo() {
        return tenderAasignedTo;
    }

    public void setTenderAasignedTo(String tenderAasignedTo) {
        this.tenderAasignedTo = tenderAasignedTo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
