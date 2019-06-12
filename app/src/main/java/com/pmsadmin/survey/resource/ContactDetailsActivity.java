package com.pmsadmin.survey.resource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.adpater.ContactDesignationListAdapter;
import com.pmsadmin.survey.resource.adpater.EstablishmentDocumentListAdapter;
import com.pmsadmin.survey.resource.establishment_pojo.EstablishmentPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactDetailsActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tvAddContact;
    RecyclerView rvDesignationList;
    ContactDesignationListAdapter contactDesignationListAdapter;
    ArrayList<JSONObject> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_contact_details, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        rvDesignationList = findViewById(R.id.rvDesignationList);
        tvAddContact = findViewById(R.id.tvAddContact);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Contact Details");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(ContactDetailsActivity.this));

        tvAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailsActivity.this, AddContactActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        arrayList = new ArrayList<JSONObject>();
        contactDesignationListAdapter = new ContactDesignationListAdapter(ContactDetailsActivity.this, arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDesignationList.setLayoutManager(layoutManager);
        rvDesignationList.setHasFixedSize(true);
        rvDesignationList.setAdapter(contactDesignationListAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getContactDesignationList();
    }

    private void getContactDesignationList() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_contact_designations("Token " +
                        LoginShared.getLoginDataModel(ContactDetailsActivity.this).getToken(), "application/json", MethodUtils.tender_id);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        try {
                            arrayList.clear();
                            String responseString = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("contact designation list=================>>>"+jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                arrayList.add(jsonArray.getJSONObject(i));
                            }
                            contactDesignationListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
