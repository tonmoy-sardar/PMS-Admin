
package com.pmsadmin.survey.resource.contractor_vendor.machinery_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentDetail implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tender")
    @Expose
    private Integer tender;
    @SerializedName("module_id")
    @Expose
    private Integer moduleId;
    @SerializedName("document_name")
    @Expose
    private String documentName;
    @SerializedName("document")
    @Expose
    private String document;
    private final static long serialVersionUID = -2340110622498696538L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTender() {
        return tender;
    }

    public void setTender(Integer tender) {
        this.tender = tender;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
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

}
