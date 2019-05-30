package com.pmsadmin.login.model;

public class SiteList {
    int id,type;
    String sitename;
    public SiteList() {




    }


    public SiteList(int id, String sitename,int type) {



        this.id = id;
        this.sitename = sitename;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }
}
