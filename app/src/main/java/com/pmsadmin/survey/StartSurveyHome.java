package com.pmsadmin.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.gson.reflect.TypeToken;
import com.pmsadmin.GridSpanSizeLookUp.GridSpanSizeLookupForListDetailsAdapter;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.external_user_type.ExternalUserType;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.survey.coordinates.RawMaterialsActivity;
import com.pmsadmin.survey.resource.EstablishmentActivity;
import com.pmsadmin.survey.resource.adpater.EstablishmentAdapter;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartSurveyHome extends BaseActivity {

    private String tenderGID = "";
    private TextView tvTenderGID;
    private RecyclerView rv_items;
    StartSurveyStaticAdapter adapter;
    public View view;

    private TextView tv_universal_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_start_survey_home2, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Start Survey");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(StartSurveyHome.this));
        tvTenderGID = findViewById(R.id.tvTenderGID);
        rv_items = (RecyclerView) findViewById(R.id.rvItems);

        Intent intent = getIntent();
        tenderGID = intent.getStringExtra("tenderGID");
        System.out.println("tenderGID======>>>"+tenderGID);
        MethodUtils.tender_id = intent.getIntExtra("tender_id",0);
        tvTenderGID.setText("Tender ID: "+tenderGID);
        System.out.println("tenderID: "+String.valueOf(MethodUtils.tender_id));

        adapter = new StartSurveyStaticAdapter(StartSurveyHome.this,MethodUtils.getItemsSurvey());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        rv_items.setLayoutManager(layoutManager);
        rv_items.setHasFixedSize(true);
        rv_items.setAdapter(adapter);

        getExternalUserType();

    }

    private void getExternalUserType() {


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_external_user_type("Token "
                        + LoginShared.getLoginDataModel(StartSurveyHome.this).getToken(),
                "application/json");


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("external_user: "+responseString);
                        ArrayList<ExternalUserType> externalUserTypes = new Gson().fromJson(responseString,
                                new TypeToken<List<ExternalUserType>>() {
                                }.getType());

                        LoginShared.setExternalUserType(StartSurveyHome.this, externalUserTypes, "external_user_type");


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

    private void initLayout() {



        setAdapter();

    }

    private void setAdapter() {

        /*StartSurveyStaticAdapter adapter = new StartSurveyStaticAdapter(StartSurveyHome.this, MethodUtils.getItemsSurvey());
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(StartSurveyHome.this, 2);
        rv_items.addItemDecoration(itemOffset);
        *//*GridSpanSizeLookupForListDetailsAdapter headerSpanSizeLookup = new GridSpanSizeLookupForListDetailsAdapter(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);*/



    }
}
