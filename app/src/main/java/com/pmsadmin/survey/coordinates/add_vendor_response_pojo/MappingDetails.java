
package com.pmsadmin.survey.coordinates.add_vendor_response_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingDetails implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("external_user")
    @Expose
    private Integer externalUser;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    private final static long serialVersionUID = -6836052367281792112L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(Integer externalUser) {
        this.externalUser = externalUser;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
