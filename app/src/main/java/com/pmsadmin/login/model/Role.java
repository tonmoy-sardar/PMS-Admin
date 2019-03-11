
package com.pmsadmin.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName("cr_name")
    @Expose
    private String crName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cr_parent_id")
    @Expose
    private Integer crParentId;

    public String getCrName() {
        return crName;
    }

    public void setCrName(String crName) {
        this.crName = crName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCrParentId() {
        return crParentId;
    }

    public void setCrParentId(Integer crParentId) {
        this.crParentId = crParentId;
    }

}
