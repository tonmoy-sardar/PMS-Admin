package com.pmsadmin.survey.resource;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddContactActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tv_add_designation,tv_add_contact;
    LinearLayout ll_add_new_contact;
    private LoadingData loader;
    EditText et_designation,et_name,et_contact_no,et_email;
    int tender_id =0,designation_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_contact, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        loader = new LoadingData(AddContactActivity.this);

        System.out.println("token=======>>>"+LoginShared.getLoginDataModel(this).getToken());

        ll_add_new_contact = findViewById(R.id.ll_add_new_contact);
        et_name = findViewById(R.id.et_name);
        et_contact_no = findViewById(R.id.et_contact_no);
        et_email = findViewById(R.id.et_email);
        tv_add_contact = findViewById(R.id.tv_add_contact);
        tv_add_designation = findViewById(R.id.tv_add_designation);
        et_designation = findViewById(R.id.et_designation);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Add Contact");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(AddContactActivity.this));


        tv_add_designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_designation.getText().length()<1){
                    et_designation.setError("Enter your designation.");
                    et_designation.requestFocus();
                } else {
                    add_designation();
                }
            }
        });


        tv_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    add_contact_details();
                }
            }
        });
    }


    private void add_designation() {

        loader.show_with_label("Please wait");
        JsonObject object = new JsonObject();
        object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
        object.addProperty("name", et_designation.getText().toString());

        System.out.println("object======>>>>"+object.toString());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_designation_add("Token " +
                LoginShared.getLoginDataModel(AddContactActivity.this).getToken(), "application/json", object);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code()==200) {
                    System.out.println("Service URL==>" + response.raw().request().url());
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        System.out.println("response output==========>>>"+jsonObject);
                        if (jsonObject.getBoolean("status")){
                            tv_add_designation.setText("ADDED");
                            tv_add_designation.setEnabled(false);
                            et_designation.setEnabled(false);
                            tender_id = jsonObject.getInt("tender");
                            designation_id = jsonObject.getInt("id");
                            ll_add_new_contact.setVisibility(View.VISIBLE);
                            Toast.makeText(AddContactActivity.this,"Designation Added successfully.",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddContactActivity.this,"Something went wrong, Please try again.",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    private void add_contact_details() {
        try {
            loader.show_with_label("Please wait");

            JsonObject object = new JsonObject();
            object.addProperty("tender", tender_id);
            object.addProperty("designation", designation_id);
            JsonArray field_details = new JsonArray();
            JsonObject field_details_obj = new JsonObject();
            field_details_obj.addProperty("field_label", et_name.getText().toString());
            field_details_obj.addProperty("field_value", et_contact_no.getText().toString());
            field_details_obj.addProperty("field_type", et_email.getText().toString());
            field_details.add(field_details_obj);
            object.add("field_details", field_details);
            System.out.println("object======>>>>"+object.toString());


            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_contact_details_add("Token " +
                    LoginShared.getLoginDataModel(AddContactActivity.this).getToken(), "application/json", object);


            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();

                    if (response.code() == 201 || response.code()==200) {
                        System.out.println("Service URL==>" + response.raw().request().url());
                        try {
                            String responseString = response.body().string();
                            System.out.println("response output==========>>>"+responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject.getBoolean("status")){
                                Toast.makeText(AddContactActivity.this,"Contact Added successfully.",Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AddContactActivity.this,"Something went wrong, Please try again.",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }




    }


    public boolean checkValidation() {
        if (et_name.getText().toString().length() < 1) {
            et_name.setError("Enter your name.");
            et_name.requestFocus();
            return false;
        } else if (et_contact_no.getText().toString().length() < 1) {
            et_contact_no.setError("Enter your contact no.");
            et_contact_no.requestFocus();
            return false;
        } else if (et_email.getText().toString().length() < 1) {
            et_email.setError("Enter your email.");
            et_email.requestFocus();
            return false;
        } else if (!emailValidator(et_email.getText().toString())) {
            et_email.setError("This email is not valid.");
            et_email.requestFocus();
            return false;
        }  else {
            return true;
        }
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
