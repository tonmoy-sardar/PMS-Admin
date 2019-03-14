package com.pmsadmin.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.login.model.LoginModel;
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

public class ForgotPasswordDialog extends Dialog implements View.OnClickListener {
    RadioGroup radio_group;
    RelativeLayout rl_email, rl_phone;
    EditText et_email, et_phone;
    Button btn_submit, btn_cancel;
    Activity activity;
    private LoadingData loader;

    public ForgotPasswordDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        loader = new LoadingData(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        setContentView(view);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        viewBind();
        setView();
        setFont();
        clickEvent();

        WindowManager.LayoutParams wmParams = getWindow().getAttributes();
        wmParams.gravity = Gravity.CENTER;

    }

    private void clickEvent() {
        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    private void setView() {
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    if (rb.getText().equals("Email")) {
                        rl_email.setVisibility(View.VISIBLE);
                        rl_phone.setVisibility(View.GONE);
                        et_email.setText("");
                        et_phone.setText("");
                    } else {
                        rl_email.setVisibility(View.GONE);
                        rl_phone.setVisibility(View.VISIBLE);
                        et_email.setText("");
                        et_phone.setText("");
                    }
                }

            }
        });
    }

    private void viewBind() {
        radio_group = findViewById(R.id.radio_group);
        rl_email = findViewById(R.id.rl_email);
        rl_phone = findViewById(R.id.rl_phone);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    private void setFont() {
        et_email.setTypeface(MethodUtils.getNormalFont(activity));
        et_phone.setTypeface(MethodUtils.getNormalFont(activity));
        btn_submit.setTypeface(MethodUtils.getNormalFont(activity));
        btn_cancel.setTypeface(MethodUtils.getNormalFont(activity));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                closeKeyboard();
                dismiss();
                break;
            case R.id.btn_submit:
                closeKeyboard();
                if (rl_email.getVisibility() == View.VISIBLE) {
                    if (et_email.getText().toString().trim().equals("")) {
                        MethodUtils.errorMsg(activity, "Please enter your email address");
                    }else {
                        callForgotApi();
                    }
                } else if (et_phone.getText().toString().trim().equals("")) {
                    MethodUtils.errorMsg(activity, "Please enter your phone number");
                } else {
                    callForgotApi();
                }
                break;
        }
    }

    private void callForgotApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        if (rl_email.getVisibility() == View.VISIBLE) {
            object.addProperty("mail_id", et_email.getText().toString().trim());
        } else {
            object.addProperty("cu_phone_no", et_phone.getText().toString().trim());
        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        final Call<ResponseBody> register = apiInterface.call_forgotPasswordApi(object);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 200) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            et_email.setText("");
                            et_phone.setText("");
                            closeKeyboard();
                            dismiss();
                            MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        et_email.setText("");
                        et_phone.setText("");
                        MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    et_email.setText("");
                    et_phone.setText("");
                    MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                et_email.setText("");
                et_phone.setText("");
                MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
            }
        });

    }

    private void closeKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
