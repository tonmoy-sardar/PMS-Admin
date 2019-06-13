package com.pmsadmin.survey.resource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.StartSurveyHome;
import com.pmsadmin.survey.coordinates.AddMaterialActivity;
import com.pmsadmin.survey.coordinates.MaterialDetails;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CheckBoxAdapter;
import com.pmsadmin.survey.resource.add_vendor_list_pojo.AddVendorList;
import com.pmsadmin.survey.resource.adpater.VendorDropdownAdapter;
import com.pmsadmin.survey.unit_pojo.UnitPojo;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddVendroActivity extends BaseActivity {

    private View view;
    private TextView tv_universal_header;
    private LoadingData loader;
    private Integer external_user_type_id = 0;

    private TextView tvSelectVendor;
    private LinearLayout llDropDown;
    private RecyclerView rvVendor;
    private Button btAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_vendro, null);
        addContentView(view);
        //setContentView(R.layout.activity_add_vendro);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Add Vendor");
        loader = new LoadingData(AddVendroActivity.this);

        Intent mIntent = getIntent();
        external_user_type_id = mIntent.getIntExtra("external_user_type_id", 0);

        System.out.println("external_user_type_id: "+external_user_type_id);

        tvSelectVendor = findViewById(R.id.tvSelectVendor);
        llDropDown = findViewById(R.id.llDropDown);
        rvVendor = findViewById(R.id.rvVendor);
        btAdd = findViewById(R.id.btAdd);

        getVendorList();
    }

    private void getVendorList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_external_user_add("Token "
                        + LoginShared.getLoginDataModel(AddVendroActivity.this).getToken(),
                "application/json");


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    String responseString = null;
                    try {
                        responseString = response.body().string();

                        ArrayList<AddVendorList> addVendorLists = new Gson().fromJson(responseString,
                                new TypeToken<List<AddVendorList>>() {
                                }.getType());
                        System.out.println("addVendorLists: " + String.valueOf(addVendorLists.size()));

                        setAdapter(addVendorLists);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("add_vendor "+responseString);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setAdapter(ArrayList<AddVendorList> addVendorLists) {

        VendorDropdownAdapter vendorDropdownAdapter = new VendorDropdownAdapter(AddVendroActivity.this, addVendorLists,external_user_type_id);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(AddVendroActivity.this, RecyclerView.VERTICAL, false);
        rvVendor.setHasFixedSize(true);
        rvVendor.setLayoutManager(linearLayoutManager);
        rvVendor.setAdapter(vendorDropdownAdapter);

        for(int i = 0; i < addVendorLists.size(); i++){

            if (addVendorLists.get(i).isSelected() == true){

                tvSelectVendor.setText(addVendorLists.get(i).getContactPersonName());
            }
        }

    }
}
