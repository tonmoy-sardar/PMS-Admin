package com.pmsadmin.apilist;

public class ApiList {

    //Staging server
    //public static final String BASE_URL = "http://192.168.24.129:8002/";
    public static final String BASE_URL = "http://166.62.54.122:8001/";

    //Live Server


    public static final String LOGIN = "login/";
    public static final String FORGOT = "forgot_password/";
    public static final String CHANGEPASSWORD = "change_password/";
    public static final String LOGOUT = "logout/";
    public static final String LOGOUTRELEASE = "logout/";
    public static final String ATTANDENCEADD = "attandance_add/";
    public static final String ATTENDANCELISTING = "attandance_list_by_employee/{employee_id}";
    public static final String REPORTLISTING = "attandance_approval_log_list/?";
    public static final String ATTENDANCELOCATIONUPDATE = "attandance_log_add/";
    public static final String ATTANDENCELOGOUT = "attandance_edit/{attendance_id}/";
    public static final String EMPLOYEELIST = "attandance_list_by_employee/{employee_id}/";
    public static final String APPROVALLIST="attandance_approval_list/?";
    public static final String ATTENDENCEEDIT="attandance_edit/{attendance_id}/";
    public static final String LEAVEAPPLY="advance_leave_apply/";
    public static final String LEAVEAPPLYLIST="advance_leave_apply/?";
    public static final String LEAVEHISTORY="leave_list_by_employee/{employee_id}/";
    public static final String GETSITETYPE="project_site_management_site_type_add/";
    public static final String ADDSITE="project_site_management_site_add/";
    public static final String MARKERGET="attandance_log_add/?";
}
