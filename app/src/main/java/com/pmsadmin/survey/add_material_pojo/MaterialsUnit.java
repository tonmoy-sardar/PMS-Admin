
package com.pmsadmin.survey.add_material_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaterialsUnit implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("material_id")
    @Expose
    private Integer materialId;
    @SerializedName("unit_id")
    @Expose
    private String unitId;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("created_by_id")
    @Expose
    private Integer createdById;
    @SerializedName("owned_by_id")
    @Expose
    private Integer ownedById;
    @SerializedName("updated_by_id")
    @Expose
    private Object updatedById;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 2569996934488336746L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public Integer getOwnedById() {
        return ownedById;
    }

    public void setOwnedById(Integer ownedById) {
        this.ownedById = ownedById;
    }

    public Object getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Object updatedById) {
        this.updatedById = updatedById;
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

}
