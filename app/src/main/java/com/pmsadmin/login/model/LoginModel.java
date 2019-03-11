
package com.pmsadmin.login.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("is_superuser")
    @Expose
    private Boolean isSuperuser;
    @SerializedName("cu_super_set")
    @Expose
    private String cuSuperSet;
    @SerializedName("cu_phone_no")
    @Expose
    private String cuPhoneNo;
    @SerializedName("cu_profile_img")
    @Expose
    private String cuProfileImg;
    @SerializedName("cu_change_pass")
    @Expose
    private Boolean cuChangePass;
    @SerializedName("module_access")
    @Expose
    private List<ModuleAccess> moduleAccess = null;
    @SerializedName("request_status")
    @Expose
    private Integer requestStatus;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsSuperuser() {
        return isSuperuser;
    }

    public void setIsSuperuser(Boolean isSuperuser) {
        this.isSuperuser = isSuperuser;
    }

    public String getCuSuperSet() {
        return cuSuperSet;
    }

    public void setCuSuperSet(String cuSuperSet) {
        this.cuSuperSet = cuSuperSet;
    }

    public String getCuPhoneNo() {
        return cuPhoneNo;
    }

    public void setCuPhoneNo(String cuPhoneNo) {
        this.cuPhoneNo = cuPhoneNo;
    }

    public String getCuProfileImg() {
        return cuProfileImg;
    }

    public void setCuProfileImg(String cuProfileImg) {
        this.cuProfileImg = cuProfileImg;
    }

    public Boolean getCuChangePass() {
        return cuChangePass;
    }

    public void setCuChangePass(Boolean cuChangePass) {
        this.cuChangePass = cuChangePass;
    }

    public List<ModuleAccess> getModuleAccess() {
        return moduleAccess;
    }

    public void setModuleAccess(List<ModuleAccess> moduleAccess) {
        this.moduleAccess = moduleAccess;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
