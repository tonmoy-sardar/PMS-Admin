
package com.pmsadmin.survey.resource.contractor_vendor.machinery_pojo;

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
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("hire")
    @Expose
    private String hire;
    @SerializedName("khoraki")
    @Expose
    private String khoraki;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("document_details")
    @Expose
    private List<Object> documentDetails = null;
    private final static long serialVersionUID = -3878344156227098839L;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getHire() {
        return hire;
    }

    public void setHire(String hire) {
        this.hire = hire;
    }

    public String getKhoraki() {
        return khoraki;
    }

    public void setKhoraki(String khoraki) {
        this.khoraki = khoraki;
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

    public List<Object> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<Object> documentDetails) {
        this.documentDetails = documentDetails;
    }

}
