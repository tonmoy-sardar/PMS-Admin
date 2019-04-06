package com.pmsadmin.addsite;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.addsite.siteType.SiteTypeModel;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.universalpopup.UniversalPopup;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddSiteActivity extends BaseActivity implements View.OnClickListener {
    public View view;
    private LoadingData loader;
    private UniversalPopup leavePopup;
    EditText et_type,et_site,et_address,et_description,et_company,et_gst,et_area;
    RelativeLayout rl_type;
    Button btn_apply;
    List<SiteTypeModel> siteTypeModels=new ArrayList<>();
    List<String> siteTypeName=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_site, null);
        addContentView(view);
        bindView();
        setClickEvent();
        loader = new LoadingData(AddSiteActivity.this);

        if (!ConnectionDetector.isConnectingToInternet(AddSiteActivity.this)) {
            MethodUtils.errorMsg(AddSiteActivity.this, AddSiteActivity.this.getString(R.string.no_internet));
        }else {
            callSiteListApi();
        }

    }

    private void setClickEvent() {
        et_type.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
    }

    private void bindView() {
        et_type=view.findViewById(R.id.et_type);
        rl_type=view.findViewById(R.id.rl_type);
        btn_apply=view.findViewById(R.id.btn_apply);
        et_site=view.findViewById(R.id.et_site);
        et_address=view.findViewById(R.id.et_address);
        et_description=view.findViewById(R.id.et_description);
        et_company=view.findViewById(R.id.et_company);
        et_gst=view.findViewById(R.id.et_gst);
        et_area=view.findViewById(R.id.et_area);
    }

    private void addSiteListAndCall() {
        leavePopup = new UniversalPopup(AddSiteActivity.this, siteTypeName, et_type);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.et_type:
                hideSoftKeyBoard();
                if (leavePopup != null && leavePopup.isShowing()) {
                    leavePopup.dismiss();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showAndDismissTypePopup();
                        }
                    }, 100);
                }
                break;
            case R.id.btn_apply:
                callValidationforSite();
                break;
        }
    }

    private void callValidationforSite() {
        if(et_site.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(AddSiteActivity.this,"Please enter Site name");
        }else if(et_address.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(AddSiteActivity.this,"Please enter Site address");
        }else if(et_type.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(AddSiteActivity.this,"Please enter Site type");
        }else if(et_description.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(AddSiteActivity.this,"Please enter Site description");
        }else if(et_gst.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(AddSiteActivity.this,"Please enter GST number");
        }else if(et_area.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(AddSiteActivity.this,"Please enter geo fencing area");
        }else{
            callSiteAddApi();
        }
    }

    private void callSiteAddApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("name", et_site.getText().toString().trim());
        object.addProperty("address", et_address.getText().toString().trim());
        object.addProperty("description", et_description.getText().toString().trim());
        object.addProperty("gst_no", et_gst.getText().toString().trim());
        object.addProperty("geo_fencing_area", et_area.getText().toString().trim()+"Mtr");
        object.addProperty("company_name", et_company.getText().toString().trim());
        for (int i = 0; i <siteTypeModels.size() ; i++) {
            if(siteTypeModels.get(i).getName().equals(et_type.getText().toString().trim())){
                object.addProperty("type", siteTypeModels.get(i).getId());
            }

        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_siteAddApi("Token "
                + LoginShared.getLoginDataModel(AddSiteActivity.this).getToken(),"application/json",object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(responseString);
                        et_site.setText("");
                        et_address.setText("");
                        et_type.setText("");
                        et_description.setText("");
                        et_company.setText("");
                        et_gst.setText("");
                        et_area.setText("");

                        if (jsonObject.optInt("request_status") == 1) {
                        MethodUtils.errorMsg(AddSiteActivity.this, jsonObject.optString("msg"));
                        } else if (jsonObject.optInt("request_status") == 0) {
                        MethodUtils.errorMsg(AddSiteActivity.this, jsonObject.optString("msg"));
                         } else {
                        MethodUtils.errorMsg(AddSiteActivity.this, AddSiteActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(AddSiteActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(AddSiteActivity.this, AddSiteActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAndDismissTypePopup() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                leavePopup.showAsDropDown(rl_type);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void hideSoftKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void callSiteListApi() {
        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_siteTypeApi("Token "
                        + LoginShared.getLoginDataModel(AddSiteActivity.this).getToken());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code()==200) {
                        String responseString = response.body().string();
                        JSONObject jsnobject = new JSONObject(responseString);
                        JSONArray jsonArray = jsnobject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject explrObject = jsonArray.getJSONObject(i);
                            SiteTypeModel siteTypeModel=new SiteTypeModel();
                            siteTypeModel.setId(explrObject.optInt("id"));
                            siteTypeModel.setName(explrObject.optString("name"));
                            if (explrObject.optString("created_by")==null){
                                siteTypeModel.setCreated_by("");
                            }else {
                                siteTypeModel.setCreated_by(explrObject.optString("created_by"));
                            }
                            if (explrObject.optString("owned_by")==null){
                                siteTypeModel.setCreated_by("");
                            }else {
                                siteTypeModel.setCreated_by(explrObject.optString("owned_by"));
                            }
                            siteTypeModels.add(siteTypeModel);
                        }
                        for (int i = 0; i < siteTypeModels.size(); i++) {
                            siteTypeName.add(siteTypeModels.get(i).getName());
                        }
                        addSiteListAndCall();
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(AddSiteActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(AddSiteActivity.this, AddSiteActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(AddSiteActivity.this, AddSiteActivity.this.getString(R.string.error_occurred));
            }
        });
    }
}
