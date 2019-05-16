package com.pmsadmin.giveattandence;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.giveattandence.adapter.DeviationListAdapter;
import com.pmsadmin.giveattandence.deviation_list_model.DeviationListModel;
import com.pmsadmin.giveattandence.deviation_list_model.Result;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JustificationActivity extends BaseActivity {

    //TextView tvDeviationValue;
    public View view;

    private LoadingData loader;
    Integer attendence_id = 0;

    private RecyclerView rvDeviationList;

    List<Result> deviationResultList = new ArrayList<Result>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_justification, null);
        addContentView(view);
        MethodUtils.setStickyBar(JustificationActivity.this);
        //setContentView(R.layout.activity_justification);
        //setContentView(R.layout.items_justification_attendence);

        //tvDeviationValue = (TextView) findViewById(R.id.tvDeviationValue);

        loader = new LoadingData(JustificationActivity.this);

        Intent intent = getIntent();
        attendence_id = intent.getIntExtra("attendence_id",1);

        System.out.print("attendence_id: "+String.valueOf(attendence_id));

        //tvDeviationValue.setText(String.valueOf(attendence_id));

        initLayout();

        callDeviationListApi();
    }

    private void initLayout() {

        rvDeviationList = (RecyclerView) findViewById(R.id.rvDeviationList);

    }

    private void callDeviationListApi() {

        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register=apiInterface.call_deviation_listApi("Token "
                        + LoginShared.getLoginDataModel(JustificationActivity.this).getToken(),
                "application/json", attendence_id);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("responseStringDeviation: "+responseString);

                        DeviationListModel deviationListModel;
                        Gson gson = new Gson();

                        try {
                            JSONObject jsonObject = new JSONObject(responseString);

                            if (jsonObject.optInt("request_status") == 1) {
                                deviationListModel = gson.fromJson(responseString,DeviationListModel.class);
                                //List<Result> clinicListingResponseArrayList = new ArrayList<>();

                                deviationResultList = deviationListModel.getResult();

                                System.out.println("deviationResult: "+deviationResultList.size());

                                setAdapter(deviationResultList);

                            }

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

    private void setAdapter(List<Result> deviationResultList) {


        DeviationListAdapter deviationListAdapter = new DeviationListAdapter(JustificationActivity.this,
                deviationResultList);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(JustificationActivity.this, RecyclerView.VERTICAL, false);

        rvDeviationList.setLayoutManager(linearLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rvDeviationList.addItemDecoration(decoration);
        rvDeviationList.setAdapter(deviationListAdapter);
    }
}
