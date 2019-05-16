
package com.pmsadmin.giveattandence.updatedattandenceListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("employee")
    @Expose
    private String employee;
    @SerializedName("is_deviation")
    @Expose
    private Integer is_deviation;
    @SerializedName("is_ten_hrs")
    @Expose
    private Integer is_ten_hrs;
    @SerializedName("user_project")
    @Expose
    private String userProject;
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
    @SerializedName("week_day")
    @Expose
    private String weekDay;
    @SerializedName("present")
    @Expose
    private Integer present;
    @SerializedName("holiday")
    @Expose
    private Integer holiday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getUserProject() {
        return userProject;
    }

    public void setUserProject(String userProject) {
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

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getPresent() {
        return present;
    }

    public void setPresent(Integer present) {
        this.present = present;
    }

    public Integer getHoliday() {
        return holiday;
    }

    public void setHoliday(Integer holiday) {
        this.holiday = holiday;
    }

    public Integer getIs_deviation() {
        return is_deviation;
    }

    public void setIs_deviation(Integer is_deviation) {
        this.is_deviation = is_deviation;
    }

    public Integer getIs_ten_hrs() {
        return is_ten_hrs;
    }

    public void setIs_ten_hrs(Integer is_ten_hrs) {
        this.is_ten_hrs = is_ten_hrs;
    }
}
