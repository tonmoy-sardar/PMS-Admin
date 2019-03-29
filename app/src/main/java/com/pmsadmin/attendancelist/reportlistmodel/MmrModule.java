
package com.pmsadmin.attendancelist.reportlistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MmrModule {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cm_name")
    @Expose
    private String cmName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

}
