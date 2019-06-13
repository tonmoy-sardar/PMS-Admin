package com.pmsadmin.survey.resource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.adpater.ContactDesignationListAdapter;
import com.pmsadmin.survey.resource.adpater.DesignationWiseContactListAdapter;
import com.pmsadmin.survey.resource.adpater.DesignationWiseMainContactListAdapter;
import com.pmsadmin.survey.resource.dialog_fragment.Dialog_Fragment_add_more_info;
import com.pmsadmin.survey.resource.dialog_fragment.Dialog_Fragment_add_new_contact;

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

public class DesignationWiseContactListActivity extends BaseActivity implements DesignationWiseMainContactListAdapter.OnItemClickListener{

    public View view;
    private TextView tv_universal_header,tv_add_contact;
    RecyclerView rv_designation_wise_contact_list;
    DesignationWiseMainContactListAdapter designationWiseMainContactListAdapter;
    ArrayList<JSONObject> arrayList;
    String designation_id="",designation = "";
    String id="",tender_id="",contact_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_designation_wise_contact, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        rv_designation_wise_contact_list = findViewById(R.id.rv_designation_wise_contact_list);
        tv_add_contact = findViewById(R.id.tv_add_contact);

        tv_universal_header = findViewById(R.id.tv_universal_header);

        Intent intent = getIntent();
        tv_universal_header.setText(intent.getStringExtra("designation"));
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(DesignationWiseContactListActivity.this));
        designation_id = intent.getStringExtra("designation_id");

        tv_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog_Fragment_add_new_contact dialog_fragment_add_new_contact= new Dialog_Fragment_add_new_contact();
                dialog_fragment_add_new_contact.setData(id,tender_id);
                dialog_fragment_add_new_contact.setOnDialogListener(new Dialog_Fragment_add_new_contact.OnItemClickDialog() {
                    @Override
                    public void onItemClick() {
                        getDesignationWiseContactList();
                        dialog_fragment_add_new_contact.dismiss();
                    }
                });
                dialog_fragment_add_new_contact.show(getSupportFragmentManager(), "dialog");
            }
        });

        arrayList = new ArrayList<JSONObject>();
        designationWiseMainContactListAdapter = new DesignationWiseMainContactListAdapter(DesignationWiseContactListActivity.this, arrayList);
        designationWiseMainContactListAdapter.setOnItemClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_designation_wise_contact_list.setLayoutManager(layoutManager);
        rv_designation_wise_contact_list.setHasFixedSize(true);
        rv_designation_wise_contact_list.setAdapter(designationWiseMainContactListAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getDesignationWiseContactList();
    }

    private void getDesignationWiseContactList() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_designation_wise_contact_list("Token " +
                        LoginShared.getLoginDataModel(DesignationWiseContactListActivity.this).getToken(), "application/json",
                MethodUtils.tender_id, Integer.valueOf(designation_id));

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
                            JSONArray result = jsonObject.getJSONArray("result");
                            id = result.getJSONObject(0).getString("designation");
                            tender_id = result.getJSONObject(0).getString("tender");
                            for (int i = 0; i < result.length(); i++) {
                                //field_details.getJSONObject(i).put("designation_id", id);
                                arrayList.add(result.getJSONObject(i));
                            }
                            System.out.println("arrayList============>>>"+arrayList);
                            designationWiseMainContactListAdapter.notifyDataSetChanged();
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

    @Override
    public void OnItemClick(int position,String contact_id) {
        System.out.println("position============>>>"+position);
        System.out.println("contact============>>>"+contact_id);
        final Dialog_Fragment_add_more_info dialog_Fragment_add_more_info= new Dialog_Fragment_add_more_info();
        dialog_Fragment_add_more_info.setData(id,tender_id,contact_id);
        dialog_Fragment_add_more_info.setOnDialogListener(new Dialog_Fragment_add_more_info.OnItemClickDialog() {
            @Override
            public void onItemClick() {
                getDesignationWiseContactList();
                dialog_Fragment_add_more_info.dismiss();
            }
        });
        dialog_Fragment_add_more_info.show(getSupportFragmentManager(), "dialog");
    }
}
