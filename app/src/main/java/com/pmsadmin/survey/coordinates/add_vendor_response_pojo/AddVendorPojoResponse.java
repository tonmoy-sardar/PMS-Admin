
package com.pmsadmin.survey.coordinates.add_vendor_response_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVendorPojoResponse implements Serializable
{

    @SerializedName("tender")
    @Expose
    private Integer tender;
    @SerializedName("external_user_type")
    @Expose
    private Integer externalUserType;
    @SerializedName("tender_survey_material")
    @Expose
    private Integer tenderSurveyMaterial;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("mapping_details")
    @Expose
    private MappingDetails mappingDetails;
    private final static long serialVersionUID = 541876046725724854L;

    public Integer getTender() {
        return tender;
    }

    public void setTender(Integer tender) {
        this.tender = tender;
    }

    public Integer getExternalUserType() {
        return externalUserType;
    }

    public void setExternalUserType(Integer externalUserType) {
        this.externalUserType = externalUserType;
    }

    public Integer getTenderSurveyMaterial() {
        return tenderSurveyMaterial;
    }

    public void setTenderSurveyMaterial(Integer tenderSurveyMaterial) {
        this.tenderSurveyMaterial = tenderSurveyMaterial;
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

    public MappingDetails getMappingDetails() {
        return mappingDetails;
    }

    public void setMappingDetails(MappingDetails mappingDetails) {
        this.mappingDetails = mappingDetails;
    }

}
