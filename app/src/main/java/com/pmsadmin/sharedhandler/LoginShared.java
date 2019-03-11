package com.pmsadmin.sharedhandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pmsadmin.login.model.LoginModel;

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

    public static void destroySessionTypePreference() {
        prefs = context.getSharedPreferences(
                SharedUtils.TYPE_DEAD_ON_LOGOUT_SHARED, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
