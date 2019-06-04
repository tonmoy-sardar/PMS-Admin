
package com.pmsadmin.survey.coordinates.raw_materials_pojo;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("materials_unit_details")
    @Expose
    private List<MaterialsUnitDetail> materialsUnitDetails = null;
    @SerializedName("mat_code")
    @Expose
    private String matCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("owned_by")
    @Expose
    private Object ownedBy;
    @SerializedName("updated_by")
    @Expose
    private Object updatedBy;
    private final static long serialVersionUID = -6584490660879099887L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MaterialsUnitDetail> getMaterialsUnitDetails() {
        return materialsUnitDetails;
    }

    public void setMaterialsUnitDetails(List<MaterialsUnitDetail> materialsUnitDetails) {
        this.materialsUnitDetails = materialsUnitDetails;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
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

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Object ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

}
