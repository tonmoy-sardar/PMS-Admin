package com.pmsadmin.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GiveReasonDialog extends Dialog implements View.OnClickListener {

    GiveAttendanceActivity activity;
    private LoadingData loader;
    EditText et_reason;
    Button btn_submit,btn_cancel;
    TextView tv_reason;
    public GiveReasonDialog(GiveAttendanceActivity activity) {
        super(activity);
        this.activity = activity;
        loader = new LoadingData(activity);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_give_reason, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        viewBind(view);
        setFont();
        setClickEvent();
    }

    private void setFont() {
        tv_reason.setTypeface(MethodUtils.getNormalFont(activity));
        btn_submit.setTypeface(MethodUtils.getNormalFont(activity));
        btn_cancel.setTypeface(MethodUtils.getNormalFont(activity));
    }

    private void setClickEvent() {
        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    private void viewBind(View view) {
        et_reason=view.findViewById(R.id.et_reason);
        btn_submit=view.findViewById(R.id.btn_submit);
        btn_cancel=view.findViewById(R.id.btn_cancel);
        tv_reason=view.findViewById(R.id.tv_reason);
    }


    private void logoutApi() {
        //loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("logout_time", activity.getCurrentTimeUsingDate());
        object.addProperty("logout_latitude", activity.gpsTracker.getLatitude());
        object.addProperty("logout_longitude", activity.gpsTracker.getLongitude());
        if (activity.addresses.size() > 0) {
            object.addProperty("logout_address", activity.addresses.get(0).getLocality() + "," + activity.addresses.get(0).getAdminArea());
        }else{
            object.addProperty("logout_address", "");
        }

        object.addProperty("approved_status",1);
        object.addProperty("justification","");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceLogoutApi("Token "
                        + LoginShared.getLoginDataModel(activity).getToken(),
                LoginShared.getAttendanceAddDataModel(activity).getResult().getId().toString(),
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code()==200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        AttendanceAddModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            LoginShared.setAttendanceFirstLoginTime(activity, "0");
                            activity.stopLocationUpdates();
                            dismiss();
                            Intent i=new Intent(activity, LoginActivity.class);
                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                logoutApi();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
