package com.pmsadmin.survey.vemdor_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorPojo implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("contact_person_name")
    @Expose
    private String contactPersonName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }
}
