
package com.pmsadmin.survey.resource.add_vendor_list_pojo;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVendorList implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("document_details")
    @Expose
    private List<DocumentDetail> documentDetails = null;
    @SerializedName("user_type_name")
    @Expose
    private String userTypeName;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("organisation_name")
    @Expose
    private String organisationName;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("trade_licence_doc")
    @Expose
    private Object tradeLicenceDoc;
    @SerializedName("gst_no")
    @Expose
    private String gstNo;
    @SerializedName("gst_doc")
    @Expose
    private Object gstDoc;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("pan_doc")
    @Expose
    private Object panDoc;
    @SerializedName("bank_ac_no")
    @Expose
    private String bankAcNo;
    @SerializedName("cancelled_cheque_doc")
    @Expose
    private Object cancelledChequeDoc;
    @SerializedName("adhar_no")
    @Expose
    private String adharNo;
    @SerializedName("adhar_doc")
    @Expose
    private String adharDoc;
    @SerializedName("contact_person_name")
    @Expose
    private String contactPersonName;
    @SerializedName("salary")
    @Expose
    private String salary;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("updated_by")
    @Expose
    private Integer updatedBy;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;

    private final static long serialVersionUID = 694673916156393546L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<DocumentDetail> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getTradeLicenceDoc() {
        return tradeLicenceDoc;
    }

    public void setTradeLicenceDoc(Object tradeLicenceDoc) {
        this.tradeLicenceDoc = tradeLicenceDoc;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public Object getGstDoc() {
        return gstDoc;
    }

    public void setGstDoc(Object gstDoc) {
        this.gstDoc = gstDoc;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public Object getPanDoc() {
        return panDoc;
    }

    public void setPanDoc(Object panDoc) {
        this.panDoc = panDoc;
    }

    public String getBankAcNo() {
        return bankAcNo;
    }

    public void setBankAcNo(String bankAcNo) {
        this.bankAcNo = bankAcNo;
    }

    public Object getCancelledChequeDoc() {
        return cancelledChequeDoc;
    }

    public void setCancelledChequeDoc(Object cancelledChequeDoc) {
        this.cancelledChequeDoc = cancelledChequeDoc;
    }

    public String getAdharNo() {
        return adharNo;
    }

    public void setAdharNo(String adharNo) {
        this.adharNo = adharNo;
    }

    public String getAdharDoc() {
        return adharDoc;
    }

    public void setAdharDoc(String adharDoc) {
        this.adharDoc = adharDoc;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
