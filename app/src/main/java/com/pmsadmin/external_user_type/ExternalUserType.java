
package com.pmsadmin.external_user_type;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalUserType implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type_name")
    @Expose
    private String typeName;
    @SerializedName("type_desc")
    @Expose
    private String typeDesc;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("owned_by")
    @Expose
    private Object ownedBy;
    private final static long serialVersionUID = 5111834496941918583L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
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

}
