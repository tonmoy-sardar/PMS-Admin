
package com.pmsadmin.survey.unit_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitPojo implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("c_name")
    @Expose
    private String cName;
    @SerializedName("c_created_by")
    @Expose
    private Object cCreatedBy;
    @SerializedName("c_owned_by")
    @Expose
    private Object cOwnedBy;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;

    private final static long serialVersionUID = -6373745519026402328L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public Object getCCreatedBy() {
        return cCreatedBy;
    }

    public void setCCreatedBy(Object cCreatedBy) {
        this.cCreatedBy = cCreatedBy;
    }

    public Object getCOwnedBy() {
        return cOwnedBy;
    }

    public void setCOwnedBy(Object cOwnedBy) {
        this.cOwnedBy = cOwnedBy;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
