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
import com.pmsadmin.dialog.AddContractorVendorDialogue;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.EstablishmentActivity;
import com.pmsadmin.survey.resource.ResourceActivity;
import com.pmsadmin.survey.resource.adpater.ContractVendorAdapter;
import com.pmsadmin.survey.resource.contractor_vendor.contract_vendor_pojo.ContractVendorPojo;
import com.pmsadmin.survey.resource.contractor_vendor.contract_vendor_pojo.Result;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContractorsVendorsActivity extends BaseActivity {
    public View view;
    private TextView tv_universal_header, tvAdd, tvPM, tvContractors;
    private RecyclerView rvItems;

    public static int contractor = 1;
    public static int pAndm = 0;

    List<Result> resultContract = new ArrayList<>();

    ContractVendorAdapter contractVendorAdapter;
    private ImageView ivAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_contractors_vendors, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Contractors/Vendor");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(ContractorsVendorsActivity.this));
        //setContentView(R.layout.activity_contractors_vendors);

        initLayout();
    }

    private void initLayout() {

        ivAdd = findViewById(R.id.ivAdd);
        rvItems = findViewById(R.id.rvItems);
        tvPM = findViewById(R.id.tvPM);
        tvPM.setTypeface(MethodUtils.getNormalFont(ContractorsVendorsActivity.this));
        tvContractors = findViewById(R.id.tvContractors);
        tvContractors.setTypeface(MethodUtils.getNormalFont(ContractorsVendorsActivity.this));

        clickListners();
    }

    private void clickListners() {

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addContractors();

            }
        });

        tvPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*tvPM.setBackgroundColor(getResources().getColor(R.color.link_color));
                tvContractors.setBackgroundColor(getResources().getColor(R.color.inactive_button_color));*/
                Intent intent = new Intent(getApplicationContext(), ContractVendorReplica.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


    }

    private void addContractors() {


        Intent intent = new Intent(getApplicationContext(), AddContractorVendorDialogue.class);
        intent.putExtra("flag", "contractor");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }


    @Override
    protected void onResume() {
        super.onResume();

        getContractVendor();

    }

    private void getContractVendor() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register = apiInterface.call_get_contract_vendor("Token "
                        + LoginShared.getLoginDataModel(ContractorsVendorsActivity.this).getToken(),
                "application/json", MethodUtils.tender_id);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        System.out.println("getContract: " + responseString);
                        Gson gson = new Gson();
                        ContractVendorPojo contractVendorPojo;
                        contractVendorPojo = gson.fromJson(responseString, ContractVendorPojo.class);
                        resultContract = contractVendorPojo.getResult();

                        setAdapter(resultContract);
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

    private void setAdapter(List<Result> resultContract) {

        contractVendorAdapter = new ContractVendorAdapter(ContractorsVendorsActivity.this, resultContract);

        rvItems.setAdapter(contractVendorAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(ContractorsVendorsActivity.this, RecyclerView.VERTICAL, false);

        rvItems.setLayoutManager(horizontalLayoutManager);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(ContractorsVendorsActivity.this, 2);
        rvItems.addItemDecoration(itemOffset);

    }
}
