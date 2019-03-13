
package com.pmsadmin.giveattandence.listattandencemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Application {

    @SerializedName("mmr_module")
    @Expose
    private MmrModule mmrModule;
    @SerializedName("mmr_role")
    @Expose
    private MmrRole mmrRole;
    @SerializedName("mmr_permissions")
    @Expose
    private MmrPermissions mmrPermissions;

    public MmrModule getMmrModule() {
        return mmrModule;
    }

    public void setMmrModule(MmrModule mmrModule) {
        this.mmrModule = mmrModule;
    }

    public MmrRole getMmrRole() {
        return mmrRole;
    }

    public void setMmrRole(MmrRole mmrRole) {
        this.mmrRole = mmrRole;
    }

    public MmrPermissions getMmrPermissions() {
        return mmrPermissions;
    }

    public void setMmrPermissions(MmrPermissions mmrPermissions) {
        this.mmrPermissions = mmrPermissions;
    }

}
