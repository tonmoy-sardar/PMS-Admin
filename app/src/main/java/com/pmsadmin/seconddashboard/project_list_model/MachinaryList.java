
package com.pmsadmin.seconddashboard.project_list_model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachinaryList implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("machinary")
    @Expose
    private Integer machinary;
    @SerializedName("project")
    @Expose
    private Integer project;
    @SerializedName("machinary_s_d_req")
    @Expose
    private String machinarySDReq;
    @SerializedName("machinary_e_d_req")
    @Expose
    private String machinaryEDReq;
    private final static long serialVersionUID = -7487181879880500125L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMachinary() {
        return machinary;
    }

    public void setMachinary(Integer machinary) {
        this.machinary = machinary;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getMachinarySDReq() {
        return machinarySDReq;
    }

    public void setMachinarySDReq(String machinarySDReq) {
        this.machinarySDReq = machinarySDReq;
    }

    public String getMachinaryEDReq() {
        return machinaryEDReq;
    }

    public void setMachinaryEDReq(String machinaryEDReq) {
        this.machinaryEDReq = machinaryEDReq;
    }

}
