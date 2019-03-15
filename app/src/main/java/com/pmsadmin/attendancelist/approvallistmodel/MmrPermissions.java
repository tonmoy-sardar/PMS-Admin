
package com.pmsadmin.attendancelist.approvallistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MmrPermissions {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cp_o")
    @Expose
    private Integer cpO;
    @SerializedName("cp_u")
    @Expose
    private Integer cpU;
    @SerializedName("cp_g")
    @Expose
    private Integer cpG;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCpO() {
        return cpO;
    }

    public void setCpO(Integer cpO) {
        this.cpO = cpO;
    }

    public Integer getCpU() {
        return cpU;
    }

    public void setCpU(Integer cpU) {
        this.cpU = cpU;
    }

    public Integer getCpG() {
        return cpG;
    }

    public void setCpG(Integer cpG) {
        this.cpG = cpG;
    }

}
