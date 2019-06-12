package com.pmsadmin.survey.resource.hydrological_data;

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
import com.pmsadmin.survey.resource.EstablishmentActivity;
import com.pmsadmin.survey.resource.hydrological_data.hydro_pojo.HydroLogicalPojo;
import com.pmsadmin.survey.resource.hydrological_data.hydro_pojo.Result;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HydrologicalActivity extends BaseActivity {

    private TextView tvAdd,tv_universal_header;
    private RecyclerView rvItems;
    List<Result> resultHydroList = new ArrayList<>();
    private HydroAdapter hydroAdapter;
    public View view;
    ImageView ivAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_hydrological, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        tvAdd = findViewById(R.id.tvAdd);
        ivAdd = findViewById(R.id.ivAdd);
        rvItems = findViewById(R.id.rvItems);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Hydrological Data");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(HydrologicalActivity.this));

        /*tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHydrologicalData.class);
                startActivity(intent);
            }
        });*/


        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHydrologicalData.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        getHydrologicalData();

        setAdapter();
    }

    private void setAdapter() {

        hydroAdapter = new HydroAdapter(HydrologicalActivity.this,resultHydroList);
        rvItems.setAdapter(hydroAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        //mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //mLayoutManager = new GridLayoutManager(this, 2);

        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(HydrologicalActivity.this, RecyclerView.VERTICAL, false);

        rvItems.setLayoutManager(horizontalLayoutManager);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(HydrologicalActivity.this, 2);
        rvItems.addItemDecoration(itemOffset);
    }

    private void getHydrologicalData() {


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register=apiInterface.call_get_hydro("Token "
                        + LoginShared.getLoginDataModel(HydrologicalActivity.this).getToken(),
                "application/json", MethodUtils.tender_id);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201 || response.code() == 200) {
                    try {

                        if (resultHydroList!=null){
                            resultHydroList.clear();
                        }
                        String responseString = response.body().string();
                        System.out.println("resHydro: "+responseString);

                        HydroLogicalPojo hydroLogicalPojo;
                        Gson gson = new Gson();

                        hydroLogicalPojo = gson.fromJson(responseString, HydroLogicalPojo.class);

                        resultHydroList.addAll(hydroLogicalPojo.getResult());
                        hydroAdapter.notifyDataSetChanged();

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
