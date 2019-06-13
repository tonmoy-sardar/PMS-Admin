package com.pmsadmin.survey.coordinates;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.ExternalMappingUserList;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.Result;
import com.pmsadmin.survey.resource.AddVendroActivity;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;
import com.pmsadmin.survey.resource.adpater.MaterialDetailsAdapter;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDetails extends BaseActivity {

    private View view;
    private TextView tv_universal_header;
    private LoadingData loader;
    String Tag = "vendor";
    int mat_id = 0;
    String external_user_type = "";
    List<Result> resultList = new ArrayList<>();
    MaterialDetailsAdapter materialDetailsAdapter;

    private RecyclerView rvItems;
    private TextView tvAdd;

    private Integer external_user_type_id = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_material_details, null);
        addContentView(view);
        //setContentView(R.layout.activity_material_details);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Material Details");
        loader = new LoadingData(MaterialDetails.this);

        rvItems = findViewById(R.id.rvItems);

        tvAdd = findViewById(R.id.tvAdd);


        System.out.println("watSize: "+
                LoginShared.getExternalUserType(MaterialDetails.this,"external_user_type").size());


        for (int i = 0;
             i < LoginShared.getExternalUserType(MaterialDetails.this,"external_user_type").size();
             i++){

            /*System.out.println("qqq: "+LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type")
                    .get(i).getTypeName());*/
            if (LoginShared.getExternalUserType(MaterialDetails.this,"external_user_type")
                    .get(i).getTypeName().equalsIgnoreCase(Tag)){

                System.out.println("externalID: "+LoginShared.getExternalUserType(MaterialDetails.this,
                        "external_user_type")
                        .get(i).getId());

                external_user_type = String.valueOf(LoginShared.getExternalUserType(MaterialDetails.this,
                        "external_user_type")
                        .get(i).getId());
                external_user_type_id = LoginShared.getExternalUserType(MaterialDetails.this,
                        "external_user_type").get(i).getId();
            }
        }


        Intent mIntent = getIntent();
        mat_id = mIntent.getIntExtra("mat_id", 0);

        System.out.println("mat_id: "+mat_id+" tenderId: "+ MethodUtils.tender_id);


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MaterialDetails.this, AddVendroActivity.class);
                intent.putExtra("external_user_type_id", external_user_type_id);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

        getExternalMappingUserList();
        setAdapter();
    }

    private void setAdapter() {


        materialDetailsAdapter = new MaterialDetailsAdapter(MaterialDetails.this, resultList);
        rvItems.setAdapter(materialDetailsAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(MaterialDetails.this, RecyclerView.VERTICAL, false);

        rvItems.setLayoutManager(horizontalLayoutManager);

        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(MaterialDetails.this, 2);
        rvItems.addItemDecoration(itemOffset);


    }

    private void getExternalMappingUserList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_external_mapping_list("Token " +
                        LoginShared.getLoginDataModel(MaterialDetails.this).getToken(), "application/json",
                MethodUtils.tender_id, external_user_type, String.valueOf(mat_id));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        System.out.println("mapping: "+responseString);

                        ExternalMappingUserList externalMappingUserList;
                        Gson gson = new Gson();

                        externalMappingUserList = gson.fromJson(responseString,ExternalMappingUserList.class);
                        resultList.addAll(externalMappingUserList.getResult());

                        System.out.println("resltSize: "+resultList.size());
                        //materialDetailsAdapter.notifyDataSetChanged();

                        if (resultList.size() > 0){
                            materialDetailsAdapter.notifyDataSetChanged();


                        }else {
                            MethodUtils.errorMsg(MaterialDetails.this,"No Data found");
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
