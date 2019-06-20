package com.pmsadmin.apply_local_conveyance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.pmsadmin.apply_local_conveyance.adapter.MyConveyanceHistoryAdapter;
import com.pmsadmin.apply_local_conveyance.my_conveyance_pojo.MyConveyanceHistoryPojo;
import com.pmsadmin.apply_local_conveyance.my_conveyance_pojo.Result;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.networking.NetworkCheck;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.contractor_vendor.ContractVendorReplica;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyConveyanceHistory extends BaseActivity {

    private View view;
    private TextView tv_universal_header;

    private int page1 = 1;
    private LoadingData loader;

    private List<Result> resultList = new ArrayList<>();

    MyConveyanceHistoryAdapter myConveyanceHistoryAdapter;

    private RecyclerView rvItems;
    LinearLayoutManager horizontalLayoutManager;

    private int totalItemCount, lastVisibleCount, totalItemCount1, lastVisibleCount1;
    private boolean loading1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_my_conveyance_history, null);
        addContentView(view);
        //setContentView(R.layout.activity_my_conveyance_history);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("My Conveyance History");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(MyConveyanceHistory.this));
        loader = new LoadingData(MyConveyanceHistory.this);

        rvItems = findViewById(R.id.rvItems);

        getConveyanceList();
        setAdapter();
    }

    private void setAdapter() {

        myConveyanceHistoryAdapter = new MyConveyanceHistoryAdapter(MyConveyanceHistory.this, resultList);
        rvItems.setAdapter(myConveyanceHistoryAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());


        horizontalLayoutManager =
                new LinearLayoutManager(MyConveyanceHistory.this, RecyclerView.VERTICAL, false);

        rvItems.setLayoutManager(horizontalLayoutManager);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(MyConveyanceHistory.this, 2);
        rvItems.addItemDecoration(itemOffset);


        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount1 = horizontalLayoutManager.getItemCount();
                lastVisibleCount1 = horizontalLayoutManager.findLastCompletelyVisibleItemPosition();


                if (lastVisibleCount1 == totalItemCount1 - 1) {

                    loader.show_with_label("Loading");
                    if (resultList.size() % 10 == 0) {
                        //   leaveList.add(null);
                        //        leaveListAdapter.notifyItemInserted(leaveList.size() - 1);
                        loading1 = true;
                        page1++;
                        if (NetworkCheck.getInstant(MyConveyanceHistory.this).isConnectingToInternet()) {
                            getConveyanceList();
                        } else {
                            MethodUtils.errorMsg(MyConveyanceHistory.this, "Please check your phone's network connection");
                        }

                    } else {
                        if (loader != null && loader.isShowing())
                            loader.dismiss();
                    }
                }


            }
        });


    }

    private void getConveyanceList() {

        if (!loading1) {
            loader.show_with_label("Loading");
        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        System.out.println("attendanceID: " + LoginShared.getLoginDataModel(MyConveyanceHistory.this).getUserId().toString());

        final Call<ResponseBody> register = apiInterface.call_get_conveyance_list("Token "
                        + LoginShared.getLoginDataModel(MyConveyanceHistory.this).getToken(),
                "application/json", LoginShared.getLoginDataModel(MyConveyanceHistory.this).getUserId(),
                String.valueOf(page1));


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("conveyance: " + responseString);
                        MyConveyanceHistoryPojo myConveyanceHistoryPojo;
                        Gson gson = new Gson();

                        myConveyanceHistoryPojo = gson.fromJson(responseString, MyConveyanceHistoryPojo.class);
                        resultList.addAll(myConveyanceHistoryPojo.getResults());
                        myConveyanceHistoryAdapter.notifyDataSetChanged();



                    } catch (IOException e) {
                        e.printStackTrace();
                        if (loader != null && loader.isShowing())
                            loader.dismiss();
                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(MyConveyanceHistory.this, "Something went wrong");

            }
        });
    }
}
