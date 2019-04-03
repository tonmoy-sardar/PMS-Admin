
package com.pmsadmin.attendancelist.reportlistmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_project")
    @Expose
    private UserProject userProject;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("login_time")
    @Expose
    private String loginTime;
    @SerializedName("logout_time")
    @Expose
    private String logoutTime;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("justification")
    @Expose
    private String justification;
    @SerializedName("employee_details")
    @Expose
    private List<EmployeeDetail> employeeDetails = null;
    @SerializedName("log_details")
    @Expose
    private List<LogDetail> logDetails = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserProject getUserProject() {
        return userProject;
    }

    public void setUserProject(UserProject userProject) {
        this.userProject = userProject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
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

    public List<EmployeeDetail> getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(List<EmployeeDetail> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    public List<LogDetail> getLogDetails() {
        return logDetails;
    }

    public void setLogDetails(List<LogDetail> logDetails) {
        this.logDetails = logDetails;
    }

}
