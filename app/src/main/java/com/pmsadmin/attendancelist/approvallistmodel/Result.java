
package com.pmsadmin.attendancelist.approvallistmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("employee")
    @Expose
    private Object employee;
    @SerializedName("user_project")
    @Expose
    private Object userProject;
    @SerializedName("date")
    @Expose
    private Object date;
    @SerializedName("login_time")
    @Expose
    private String loginTime;
    @SerializedName("login_latitude")
    @Expose
    private Object loginLatitude;
    @SerializedName("login_longitude")
    @Expose
    private Object loginLongitude;
    @SerializedName("login_address")
    @Expose
    private Object loginAddress;
    @SerializedName("logout_time")
    @Expose
    private String logoutTime;
    @SerializedName("logout_latitude")
    @Expose
    private Object logoutLatitude;
    @SerializedName("logout_longitude")
    @Expose
    private Object logoutLongitude;
    @SerializedName("logout_address")
    @Expose
    private Object logoutAddress;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("justification")
    @Expose
    private String justification;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("employee_details")
    @Expose
    private List<EmployeeDetail> employeeDetails = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getEmployee() {
        return employee;
    }

    public void setEmployee(Object employee) {
        this.employee = employee;
    }

    public Object getUserProject() {
        return userProject;
    }

    public void setUserProject(Object userProject) {
        this.userProject = userProject;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public Object getLoginLatitude() {
        return loginLatitude;
    }

    public void setLoginLatitude(Object loginLatitude) {
        this.loginLatitude = loginLatitude;
    }

    public Object getLoginLongitude() {
        return loginLongitude;
    }

    public void setLoginLongitude(Object loginLongitude) {
        this.loginLongitude = loginLongitude;
    }

    public Object getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(Object loginAddress) {
        this.loginAddress = loginAddress;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Object getLogoutLatitude() {
        return logoutLatitude;
    }

    public void setLogoutLatitude(Object logoutLatitude) {
        this.logoutLatitude = logoutLatitude;
    }

    public Object getLogoutLongitude() {
        return logoutLongitude;
    }

    public void setLogoutLongitude(Object logoutLongitude) {
        this.logoutLongitude = logoutLongitude;
    }

    public Object getLogoutAddress() {
        return logoutAddress;
    }

    public void setLogoutAddress(Object logoutAddress) {
        this.logoutAddress = logoutAddress;
    }

    public Integer getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(Integer approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public List<EmployeeDetail> getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(List<EmployeeDetail> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

}
