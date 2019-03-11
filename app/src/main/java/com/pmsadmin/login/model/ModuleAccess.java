
package com.pmsadmin.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModuleAccess {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("module")
    @Expose
    private Module module;
    @SerializedName("role")
    @Expose
    private Role role;
    @SerializedName("permissions")
    @Expose
    private Permissions_ permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permissions_ getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions_ permissions) {
        this.permissions = permissions;
    }

}
