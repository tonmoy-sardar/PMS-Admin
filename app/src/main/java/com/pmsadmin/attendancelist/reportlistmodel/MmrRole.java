
package com.pmsadmin.attendancelist.reportlistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MmrRole {

    @SerializedName("cr_name")
    @Expose
    private String crName;

    public String getCrName() {
        return crName;
    }

    public void setCrName(String crName) {
        this.crName = crName;
    }

}
