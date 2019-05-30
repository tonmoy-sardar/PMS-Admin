
package com.pmsadmin.seconddashboard.project_list_model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tender")
    @Expose
    private Integer tender;
    @SerializedName("tender_g_id")
    @Expose
    private String tenderGId;
    @SerializedName("project_g_id")
    @Expose
    private String projectGId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site_location")
    @Expose
    private Integer siteLocation;
    @SerializedName("site_location_name")
    @Expose
    private String siteLocationName;
    @SerializedName("tender_bidder_type")
    @Expose
    private String tenderBidderType;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;
    @SerializedName("machinary_list")
    @Expose
    private List<MachinaryList> machinaryList = null;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("updated_by")
    @Expose
    private Integer updatedBy;
    @SerializedName("owned_by")
    @Expose
    private Integer ownedBy;
    @SerializedName("status")
    @Expose
    private Boolean status;
    private final static long serialVersionUID = 2095341159354755163L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTender() {
        return tender;
    }

    public void setTender(Integer tender) {
        this.tender = tender;
    }

    public String getTenderGId() {
        return tenderGId;
    }

    public void setTenderGId(String tenderGId) {
        this.tenderGId = tenderGId;
    }

    public String getProjectGId() {
        return projectGId;
    }

    public void setProjectGId(String projectGId) {
        this.projectGId = projectGId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(Integer siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getSiteLocationName() {
        return siteLocationName;
    }

    public void setSiteLocationName(String siteLocationName) {
        this.siteLocationName = siteLocationName;
    }

    public String getTenderBidderType() {
        return tenderBidderType;
    }

    public void setTenderBidderType(String tenderBidderType) {
        this.tenderBidderType = tenderBidderType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<MachinaryList> getMachinaryList() {
        return machinaryList;
    }

    public void setMachinaryList(List<MachinaryList> machinaryList) {
        this.machinaryList = machinaryList;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
