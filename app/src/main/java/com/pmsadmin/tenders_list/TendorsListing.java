package com.pmsadmin.tenders_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.tenderdashboard.TenderDashboardActivity;
import com.pmsadmin.tenders_list.tendors_pojo.Result;
import com.pmsadmin.tenders_list.tendors_pojo.TendorsListingPojo;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TendorsListing extends BaseActivity {

    public View view;
    private LoadingData loader;

    List<Result> tendorsResultList = new ArrayList<>();

    private RecyclerView rvTendors;

    private TendorsAdapter tendorsAdapter;
    private GridLayoutManager mLayoutManager;

    private TextView tv_universal_header;

    //StaggeredGridLayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_tendors_listing, null);
        addContentView(view);
        loader = new LoadingData(TendorsListing.this);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Start Survey");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(TendorsListing.this));
        bindView();
        fontSet();

        callTendersAddApi();
        setRecyclerView();
    }

    private void setRecyclerView() {


        tendorsAdapter = new TendorsAdapter(TendorsListing.this,tendorsResultList);
        rvTendors.setAdapter(tendorsAdapter);
        rvTendors.setItemAnimator(new DefaultItemAnimator());
        //mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        mLayoutManager = new GridLayoutManager(this, 2);
        rvTendors.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rvTendors.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(TendorsListing.this, 2);
        rvTendors.addItemDecoration(itemOffset);
    }

    private void fontSet() {


    }

    private void bindView() {


        rvTendors=view.findViewById(R.id.rvTendors);
        /*tv_universal_header = view.findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Start Survey");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(TendorsListing.this));*/

    }

    private void callTendersAddApi() {


        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_tenders_add("Token "
                        + LoginShared.getLoginDataModel(TendorsListing.this).getToken(),
                "application/json");


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("tenderRes: "+responseString);

                        JSONObject jsonObject = new JSONObject(responseString);

                        //if (jsonObject.optInt("request_status") == 1) {

                            Gson gson = new Gson();
                            TendorsListingPojo tendorsListingPojo;
                            tendorsListingPojo = gson.fromJson(responseString, TendorsListingPojo.class);
                            //tendorsResultList = tendorsListingPojo.getResult();
                            tendorsResultList.addAll(tendorsListingPojo.getResult());

                            System.out.println("tendorsResultList: " + tendorsResultList.size());

                            tendorsAdapter.notifyDataSetChanged();
                        //}


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
