
package com.pmsadmin.survey.resource.add_vendor_list_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentDetail implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("external_user")
    @Expose
    private Integer externalUser;
    @SerializedName("document_name")
    @Expose
    private String documentName;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    private final static long serialVersionUID = 3526890965146520033L;

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

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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

}
