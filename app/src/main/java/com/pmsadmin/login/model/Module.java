
package com.pmsadmin.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Module {

    @SerializedName("cm_url")
    @Expose
    private String cmUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cm_icon")
    @Expose
    private String cmIcon;
    @SerializedName("cm_name")
    @Expose
    private String cmName;
    @SerializedName("permissions")
    @Expose
    private Permissions permissions;

    public String getCmUrl() {
        return cmUrl;
    }

    public void setCmUrl(String cmUrl) {
        this.cmUrl = cmUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCmIcon() {
        return cmIcon;
    }

    public void setCmIcon(String cmIcon) {
        this.cmIcon = cmIcon;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

}
