package com.pmsadmin.survey.resource.contractor_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.adpater.MachineryAdapter;
import com.pmsadmin.survey.resource.contractor_vendor.machinery_pojo.GetMachineryListPojo;
import com.pmsadmin.survey.resource.contractor_vendor.machinery_pojo.Result;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContractVendorReplica extends BaseActivity {

    public View view;
    private TextView tv_universal_header, tvAdd, tvPM, tvContractors;
    private RecyclerView rvItems;

    public static int contractor = 1;
    public static int pAndm = 0;

    private LoadingData loader;

    List<Result> resultList = new ArrayList<>();

    MachineryAdapter machineryAdapter;

    ImageView ivAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(this, R.layout.activity_contract_vendor_replica, null);
        addContentView(view);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Contractors/Vendor");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(ContractVendorReplica.this));
        //setContentView(R.layout.activity_contract_vendor_replica);

        loader = new LoadingData(ContractVendorReplica.this);

        initLayout();
    }

    private void initLayout() {

        tvAdd = findViewById(R.id.tvAdd);
        rvItems = findViewById(R.id.rvItems);
        tvPM = findViewById(R.id.tvPM);
        tvContractors = findViewById(R.id.tvContractors);
        ivAdd = findViewById(R.id.ivAdd);

        clickListners();
    }

    private void clickListners() {

        tvContractors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*tvPM.setBackgroundColor(getResources().getColor(R.color.link_color));
                tvContractors.setBackgroundColor(getResources().getColor(R.color.inactive_button_color));*/
                finish();

            }
        });


        /*tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMachinery();

            }
        });*/

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMachinery();

            }
        });
    }

    private void addMachinery() {


        Intent intent = new Intent(getApplicationContext(), MachineryAddActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getMachinerylist();

        setAdapter();
    }

    private void setAdapter() {

        machineryAdapter = new MachineryAdapter(ContractVendorReplica.this, resultList);
        rvItems.setAdapter(machineryAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(ContractVendorReplica.this, RecyclerView.VERTICAL, false);

        rvItems.setLayoutManager(horizontalLayoutManager);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(ContractVendorReplica.this, 2);
        rvItems.addItemDecoration(itemOffset);
    }

    private void getMachinerylist() {

        loader.show_with_label("Please wait");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register = apiInterface.call_get_machinery("Token "
                        + LoginShared.getLoginDataModel(ContractVendorReplica.this).getToken(),
                "application/json", MethodUtils.tender_id);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {

                    String responseString = "";

                    if (resultList != null) {
                        resultList.clear();
                    }

                    try {
                        responseString = response.body().string();
                        Gson gson = new Gson();
                        GetMachineryListPojo getMachineryListPojo;
                        getMachineryListPojo = gson.fromJson(responseString, GetMachineryListPojo.class);
                        resultList.addAll(getMachineryListPojo.getResult());
                        machineryAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("responseStringMachinery: " + responseString);
                    Gson gson = new Gson();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
