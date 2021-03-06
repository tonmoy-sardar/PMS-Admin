
package com.pmsadmin.attendancelist.markergetmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("attandance")
    @Expose
    private Integer attandance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("justification")
    @Expose
    private Object justification;
    @SerializedName("is_checkout")
    @Expose
    private Boolean isCheckout;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAttandance() {
        return attandance;
    }

    public void setAttandance(Integer attandance) {
        this.attandance = attandance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(Integer approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public Object getJustification() {
        return justification;
    }

    public void setJustification(Object justification) {
        this.justification = justification;
    }

    public Boolean getIsCheckout() {
        return isCheckout;
    }

    public void setIsCheckout(Boolean isCheckout) {
        this.isCheckout = isCheckout;
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

}
