package com.pmsadmin.survey.coordinates;

import androidx.appcompat.app.AppCompatActivity;
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

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.AddChkInDialogue;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;

import java.io.IOException;

public class CheckInActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tvAdd;
    private RecyclerView rv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_check_in, null);
        addContentView(view);
        //setContentView(R.layout.activity_check_in);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Check In");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(CheckInActivity.this));

        System.out.println("tenderID: "+String.valueOf(MethodUtils.tender_id));

        initLayout();

        callTenderSurveyLocationList();
    }

    private void callTenderSurveyLocationList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_tender_survey_location_list("Token "
                        + LoginShared.getLoginDataModel(CheckInActivity.this).getToken(),
                String.valueOf(MethodUtils.tender_id));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
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
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvAdd.setTypeface(MethodUtils.getNormalFont(CheckInActivity.this));


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddChkInDialogue.class);
                startActivity(intent);

            }
        });
    }
}
