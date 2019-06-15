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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.add_material_dialogue.AddMaterialDialogue;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CheckInAdapter;
import com.pmsadmin.survey.coordinates.raw_material_adapter.RawMaterialAdapter;
import com.pmsadmin.survey.coordinates.raw_materials_pojo.RawMaterialsPojo;
import com.pmsadmin.survey.coordinates.raw_materials_pojo.Result;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RawMaterialsActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header;

    List<Result> rawMaterialsResultList = new ArrayList<>();

    private RecyclerView rvMaterials;
    private RawMaterialAdapter rawMaterialAdapter;

    private LoadingData loader;

    private ImageView ivAdd;

    String Tag = "vendor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_raw_materials, null);
        addContentView(view);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Materials");
        loader = new LoadingData(RawMaterialsActivity.this);
        //setContentView(R.layout.activity_raw_materials);

        /*LoginShared.getLeaveHistoryDataModel()
        for (int i = 0; i < LoginShared.getExternalUserType().size())*/



        /*System.out.println("watSize: "+
                LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type").size());


        for (int i = 0;
             i < LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type").size();
             i++){

            *//*System.out.println("qqq: "+LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type")
                    .get(i).getTypeName());*//*
            if (LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type")
                    .get(i).getTypeName().equalsIgnoreCase(Tag)){

                System.out.println("externalID: "+LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type")
                        .get(i).getId());
            }
        }*/

        initLayout();



    }

    @Override
    protected void onResume() {
        super.onResume();
        getRawMaterialsList();

        setAdapter();
    }

    private void initLayout() {

        rvMaterials = findViewById(R.id.rvMaterials);

        ivAdd = findViewById(R.id.ivAdd);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RawMaterialsActivity.this, AddMaterialActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

    }

    private void setAdapter() {

        rawMaterialAdapter = new RawMaterialAdapter(RawMaterialsActivity.this, rawMaterialsResultList);
        rvMaterials.setAdapter(rawMaterialAdapter);
        rvMaterials.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(RawMaterialsActivity.this, RecyclerView.VERTICAL, false);

        rvMaterials.setLayoutManager(horizontalLayoutManager);

        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(RawMaterialsActivity.this, 2);
        rvMaterials.addItemDecoration(itemOffset);

    }

    private void getRawMaterialsList() {

        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_raw_materials_listing("Token "
                        + LoginShared.getLoginDataModel(RawMaterialsActivity.this).getToken(),
                "application/json");

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {
                    try {

                        if (rawMaterialsResultList!= null){
                            rawMaterialsResultList.clear();
                        }

                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.optInt("request_status") == 1) {

                            Gson gson = new Gson();
                            RawMaterialsPojo rawMaterialsPojo;
                            rawMaterialsPojo = gson.fromJson(responseString, RawMaterialsPojo.class);
                            rawMaterialsResultList.addAll(rawMaterialsPojo.getResult());

                            System.out.println("rawMaterialsResultList: " + rawMaterialsResultList.size());

                            rawMaterialAdapter.notifyDataSetChanged();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
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
