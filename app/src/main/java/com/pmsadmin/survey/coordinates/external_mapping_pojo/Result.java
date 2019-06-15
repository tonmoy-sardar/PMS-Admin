
package com.pmsadmin.survey.coordinates.external_mapping_pojo;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("updated_by")
    @Expose
    private Object updatedBy;
    @SerializedName("document_details")
    @Expose
    private List<DocumentDetail> documentDetails = null;
    @SerializedName("mapping_document_details")
    @Expose
    private List<MappingDocumentDetail> mappingDocumentDetails = null;
    @SerializedName("external_user_name")
    @Expose
    private String externalUserName;
    @SerializedName("external_user_contact")
    @Expose
    private String externalUserContact;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("tender")
    @Expose
    private Integer tender;
    @SerializedName("external_user_type")
    @Expose
    private Integer externalUserType;
    @SerializedName("external_user")
    @Expose
    private Integer externalUser;
    @SerializedName("tender_survey_material")
    @Expose
    private Integer tenderSurveyMaterial;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("owned_by")
    @Expose
    private Integer ownedBy;
    private final static long serialVersionUID = 3130773030334716431L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<DocumentDetail> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
    }

    public List<MappingDocumentDetail> getMappingDocumentDetails() {
        return mappingDocumentDetails;
    }

    public void setMappingDocumentDetails(List<MappingDocumentDetail> mappingDocumentDetails) {
        this.mappingDocumentDetails = mappingDocumentDetails;
    }

    public String getExternalUserName() {
        return externalUserName;
    }

    public void setExternalUserName(String externalUserName) {
        this.externalUserName = externalUserName;
    }

    public String getExternalUserContact() {
        return externalUserContact;
    }

    public void setExternalUserContact(String externalUserContact) {
        this.externalUserContact = externalUserContact;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

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

    public Integer getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(Integer externalUser) {
        this.externalUser = externalUser;
    }

    public Integer getTenderSurveyMaterial() {
        return tenderSurveyMaterial;
    }

    public void setTenderSurveyMaterial(Integer tenderSurveyMaterial) {
        this.tenderSurveyMaterial = tenderSurveyMaterial;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
    }

}
