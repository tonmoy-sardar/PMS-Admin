package com.pmsadmin.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.model.DashboardItemsModel;
import com.pmsadmin.dialog.ErrorMessageDialog;
import com.pmsadmin.dialog.ForgotPasswordDialog;
import com.pmsadmin.login.model.LoginModel;
import com.pmsadmin.netconnection.ConnectionDetector;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_copyright, tv_tender, tv_forgot;
    EditText et_login, et_password;
    Button btn_login;
    private LoadingData loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MethodUtils.setStickyBar(LoginActivity.this);
        loader = new LoadingData(LoginActivity.this);
        viewBind();
        clickEvent();
        setFont();
        et_login.setText("santanu.pal@shyamfuture.com");
        et_password.setText("123456");
    }

    private void clickEvent() {
        btn_login.setOnClickListener(this);
        tv_copyright.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
    }

    private void setValidation() {
        if (et_login.getText().toString().equals("")) {
            errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.enter_email));
        } else if (et_password.getText().toString().equals("")) {
            errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.enter_password));
        } else if (!ConnectionDetector.isConnectingToInternet(LoginActivity.this)) {
            errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.no_internet));
        } else {
            callLoginApi();
        }
    }

    private void callLoginApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("username", et_login.getText().toString().trim());
        object.addProperty("password", et_password.getText().toString().trim());
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_loginApi(object);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        LoginModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            LoginShared.setLoginToken(LoginActivity.this, jsonObject.optString("token"));
                            loginModel = gson.fromJson(responseString, LoginModel.class);
                            LoginShared.setLoginDataModel(LoginActivity.this, loginModel);

                            navigateToHome();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            errorMsg(LoginActivity.this, jsonObject.optString("msg"));
                            et_login.setText("");
                            et_password.setText("");
                        } else {
                            errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        et_login.setText("");
                        et_password.setText("");
                        errorMsg(LoginActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    et_login.setText("");
                    et_password.setText("");
                    errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                et_login.setText("");
                et_password.setText("");
                errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
            }
        });
    }

    private void navigateToHome() {
         //Toast.makeText(LoginActivity.this,LoginShared.getLoginDataModel(LoginActivity.this).getToken().toString(), Toast.LENGTH_LONG).show();
        Intent profileIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(profileIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void setFont() {
        tv_copyright.setLinkTextColor(getResources().getColor(R.color.link_color));
        tv_copyright.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        tv_tender.setTypeface(MethodUtils.getBoldFont(LoginActivity.this));
        tv_forgot.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        et_login.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        et_password.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        btn_login.setTypeface(MethodUtils.getBoldFont(LoginActivity.this));
    }

    private void viewBind() {
        tv_copyright = findViewById(R.id.tv_copyright);
        tv_tender = findViewById(R.id.tv_tender);
        tv_forgot = findViewById(R.id.tv_forgot);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
    }

    public void errorMsg(Context context, String msg) {
        ErrorMessageDialog.getInstant(context).show(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                setValidation();
                break;
            case R.id.tv_copyright:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shyamsteel.com/"));
                startActivity(browserIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_forgot:
               new ForgotPasswordDialog(LoginActivity.this).show();
                break;
        }
    }
}
