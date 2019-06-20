
package com.pmsadmin.apply_local_conveyance.my_conveyance_pojo;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("project")
    @Expose
    private Integer project;
    @SerializedName("employee")
    @Expose
    private Integer employee;
    @SerializedName("eligibility_per_day")
    @Expose
    private String eligibilityPerDay;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("from_place")
    @Expose
    private String fromPlace;
    @SerializedName("to_place")
    @Expose
    private String toPlace;
    @SerializedName("vechicle_type")
    @Expose
    private String vechicleType;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("job_alloted_by")
    @Expose
    private Integer jobAllotedBy;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("ammount")
    @Expose
    private String ammount;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("owned_by")
    @Expose
    private Integer ownedBy;
    @SerializedName("employee_details")
    @Expose
    private List<EmployeeDetail> employeeDetails = null;
    private final static long serialVersionUID = -126164796489136552L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }

    public String getEligibilityPerDay() {
        return eligibilityPerDay;
    }

    public void setEligibilityPerDay(String eligibilityPerDay) {
        this.eligibilityPerDay = eligibilityPerDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getVechicleType() {
        return vechicleType;
    }

    public void setVechicleType(String vechicleType) {
        this.vechicleType = vechicleType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Integer getJobAllotedBy() {
        return jobAllotedBy;
    }

    public void setJobAllotedBy(Integer jobAllotedBy) {
        this.jobAllotedBy = jobAllotedBy;
    }

    public Integer getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(Integer approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
    }

    public List<EmployeeDetail> getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(List<EmployeeDetail> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

}
