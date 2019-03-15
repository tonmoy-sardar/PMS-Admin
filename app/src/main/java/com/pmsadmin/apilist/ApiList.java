package com.pmsadmin.apilist;

public class ApiList {

    //Staging server
    public static final String BASE_URL = "http://192.168.24.129:8002/";

    //Live Server


    public static final String LOGIN = "login/";
    public static final String FORGOT = "forgot_password/";
    public static final String CHANGEPASSWORD = "change_password/";
    public static final String LOGOUT = "logout/";
    public static final String ATTANDENCEADD = "attandance_add/";
    public static final String ATTENDANCELISTING = "attandance_add/";
    public static final String ATTENDANCELOCATIONUPDATE = "attandance_log_add/";
    public static final String ATTANDENCELOGOUT = "attandance_edit/{attendance_id}/";
    public static final String EMPLOYEELIST = "attandance_list_by_employee/{employee_id}/";
    public static final String APPROVALLIST="attandance_approval_list/?";
}
