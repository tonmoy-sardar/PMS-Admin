package com.pmsadmin.sharedhandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pmsadmin.attendancelist.approvallistmodel.ApprovalListModel;
import com.pmsadmin.attendancelist.leavelistmodel.LeaveListModel;
import com.pmsadmin.attendancelist.reportlistmodel.ReportListModel;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.giveattandence.listattandencemodel.AttendanceListModel;
import com.pmsadmin.giveattandence.updatedattandenceListModel.UpdatedAttendanceListModel;
import com.pmsadmin.leavesection.leavehistorymodel.LeaveHistoryModel;
import com.pmsadmin.login.model.LoginModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginShared {
    private static Context context;
    private static SharedPreferences prefs;

    private static void activateShared(Context context) {
        LoginShared.context = context;
        LoginShared.prefs = context.getSharedPreferences(
                SharedUtils.TYPE_DEAD_ON_LOGOUT_SHARED, context.MODE_PRIVATE);
    }

    /**
     * Set User Data Model
     */
    public static void setLoginDataModel(Context context, LoginModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_USER_DATA_MODEL, json);
        editor.commit();
    }

    //
    public static LoginModel getLoginDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        LoginModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_USER_DATA_MODEL, "");

        if (userDataModelJson.equals(""))
            loginModel = new LoginModel();
        else
            loginModel = gson.fromJson(userDataModelJson, LoginModel.class);

        return loginModel;
    }

    public static void setApprovalListModel(Context context, ApprovalListModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_APPROVAL_LIST_DATA_MODEL, json);
        editor.commit();
    }

    //
    public static ApprovalListModel getApprovalListModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        ApprovalListModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_APPROVAL_LIST_DATA_MODEL, "");

        if (userDataModelJson.equals(""))
            loginModel = new ApprovalListModel();
        else
            loginModel = gson.fromJson(userDataModelJson, ApprovalListModel.class);

        return loginModel;
    }

    public static void setAttendanceListDataModel(Context context, AttendanceListModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_ATTENDANCE_LIST_MODEL, json);
        editor.commit();
    }

    //
    public static AttendanceListModel getAttendanceListDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        AttendanceListModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_ATTENDANCE_LIST_MODEL, "");

        if (userDataModelJson.equals(""))
            loginModel = new AttendanceListModel();
        else
            loginModel = gson.fromJson(userDataModelJson, AttendanceListModel.class);

        return loginModel;
    }

    public static void setReportListDataModel(Context context, ReportListModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_UPDATED_REPORT_LIST_MODEL, json);
        editor.commit();
    }

    //
    public static ReportListModel getReportListDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        ReportListModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_UPDATED_REPORT_LIST_MODEL, "");

        if (userDataModelJson.equals(""))
            loginModel = new ReportListModel();
        else
            loginModel = gson.fromJson(userDataModelJson, ReportListModel.class);

        return loginModel;
    }

    public static void setUpdatedAttendanceListDataModel(Context context, UpdatedAttendanceListModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_UPDATED_ATTENDANCE_LIST_MODEL, json);
        editor.commit();
    }

    //
    public static UpdatedAttendanceListModel getUpdatedAttendanceListDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        UpdatedAttendanceListModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_UPDATED_ATTENDANCE_LIST_MODEL, "");

        if (userDataModelJson.equals(""))
            loginModel = new UpdatedAttendanceListModel();
        else
            loginModel = gson.fromJson(userDataModelJson, UpdatedAttendanceListModel.class);

        return loginModel;
    }

    public static void setAttendanceAddDataModel(Context context, AttendanceAddModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_ATTENDANCE_ADD, json);
        editor.commit();
    }

    //
    public static AttendanceAddModel getAttendanceAddDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        AttendanceAddModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_ATTENDANCE_ADD, "");

        if (userDataModelJson.equals(""))
            loginModel = new AttendanceAddModel();
        else
            loginModel = gson.fromJson(userDataModelJson, AttendanceAddModel.class);

        return loginModel;
    }

    public static void setLeaveListDataModel(Context context, LeaveListModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_LEAVE_LIST, json);
        editor.commit();
    }

    //
    public static LeaveListModel getLeaveListDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        LeaveListModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_LEAVE_LIST, "");

        if (userDataModelJson.equals(""))
            loginModel = new LeaveListModel();
        else
            loginModel = gson.fromJson(userDataModelJson, LeaveListModel.class);

        return loginModel;
    }

    public static void setLeaveHistoryDataModel(Context context, LeaveHistoryModel loginModel) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        editor.putString(SharedUtils.KEY_SHARED_LEAVE_HISTORY_LIST, json);
        editor.commit();
    }

    //
    public static LeaveHistoryModel getLeaveHistoryDataModel(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        LeaveHistoryModel loginModel = null;
        Gson gson = new Gson();
        String userDataModelJson = LoginShared.prefs.getString(SharedUtils.KEY_SHARED_LEAVE_HISTORY_LIST, "");

        if (userDataModelJson.equals(""))
            loginModel = new LeaveHistoryModel();
        else
            loginModel = gson.fromJson(userDataModelJson, LeaveHistoryModel.class);

        return loginModel;
    }

    public static String getLoginToken(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);
        return prefs.getString(SharedUtils.KEY_SHARED_LOGIN_TOKEN, SharedUtils.KEY_SHARED_NO_DATA);
    }

    public static void setLoginToken(Context context, String value) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedUtils.KEY_SHARED_LOGIN_TOKEN, value);
        editor.commit();
    }
    public static String getLoginTime(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);
        return prefs.getString(SharedUtils.KEY_SHARED_LOGIN_TIME, SharedUtils.KEY_SHARED_NO_DATA);
    }

    public static void setLoginTime(Context context, String value) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedUtils.KEY_SHARED_LOGIN_TIME, value);
        editor.commit();
    }

    public static String getAttendanceFirstLoginTime(Context context) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);
        return prefs.getString(SharedUtils.KEY_SHARED_ATTENDANCE_LOGIN_TIME, SharedUtils.KEY_SHARED_NO_DATA);
    }

    public static void setAttendanceFirstLoginTime(Context context, String value) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedUtils.KEY_SHARED_ATTENDANCE_LOGIN_TIME, value);
        editor.commit();
    }

    //////////////////SAVE IMPORTANT//////
    public static void saveResultList(Context context, List<com.pmsadmin.attendancelist.reportlistmodel.Result> list, String key) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public static List<com.pmsadmin.attendancelist.reportlistmodel.Result> getResultList(Context context, String key) {
        if (LoginShared.context == null || LoginShared.prefs == null)
            activateShared(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<com.pmsadmin.attendancelist.reportlistmodel.Result>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public static void destroySessionTypePreference() {
        prefs = context.getSharedPreferences(
                SharedUtils.TYPE_DEAD_ON_LOGOUT_SHARED, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
