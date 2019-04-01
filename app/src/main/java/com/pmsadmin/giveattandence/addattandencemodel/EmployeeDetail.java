
package com.pmsadmin.giveattandence.addattandencemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cu_emp_code")
    @Expose
    private String cuEmpCode;
    @SerializedName("cu_phone_no")
    @Expose
    private String cuPhoneNo;
    @SerializedName("cu_alt_phone_no")
    @Expose
    private Object cuAltPhoneNo;
    @SerializedName("cu_dob")
    @Expose
    private Object cuDob;
    @SerializedName("cu_super_set")
    @Expose
    private String cuSuperSet;
    @SerializedName("cu_user")
    @Expose
    private CuUser cuUser;
    @SerializedName("applications")
    @Expose
    private List<Application> applications = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCuEmpCode() {
        return cuEmpCode;
    }

    public void setCuEmpCode(String cuEmpCode) {
        this.cuEmpCode = cuEmpCode;
    }

    public String getCuPhoneNo() {
        return cuPhoneNo;
    }

    public void setCuPhoneNo(String cuPhoneNo) {
        this.cuPhoneNo = cuPhoneNo;
    }

    public Object getCuAltPhoneNo() {
        return cuAltPhoneNo;
    }

    public void setCuAltPhoneNo(Object cuAltPhoneNo) {
        this.cuAltPhoneNo = cuAltPhoneNo;
    }

    public Object getCuDob() {
        return cuDob;
    }

    public void setCuDob(Object cuDob) {
        this.cuDob = cuDob;
    }

    public String getCuSuperSet() {
        return cuSuperSet;
    }

    public void setCuSuperSet(String cuSuperSet) {
        this.cuSuperSet = cuSuperSet;
    }

    public CuUser getCuUser() {
        return cuUser;
    }

    public void setCuUser(CuUser cuUser) {
        this.cuUser = cuUser;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

}
