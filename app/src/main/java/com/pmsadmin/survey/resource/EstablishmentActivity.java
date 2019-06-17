package com.pmsadmin.survey.resource;

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
import com.pmsadmin.giveattandence.JustificationActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.CheckInActivity;
import com.pmsadmin.survey.resource.adpater.EstablishmentAdapter;
import com.pmsadmin.survey.resource.establishment_pojo.EstablishmentPojo;
import com.pmsadmin.survey.resource.establishment_pojo.Result;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EstablishmentActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tvAdd;
    private ImageView ivAdd;

    List<Result> resultEstablishment = new ArrayList<>();

    private RecyclerView rvEstablishment;

    private EstablishmentAdapter establishmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_establishment, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        //setContentView(R.layout.activity_establishment);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        //tvAdd = findViewById(R.id.tvAdd);
        ivAdd = findViewById(R.id.ivAdd);
        tv_universal_header.setText("Establishment");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(EstablishmentActivity.this));

        rvEstablishment = findViewById(R.id.rvEstablishment);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EstablishmentActivity.this, AddEstablishmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
        getEstablishmentList();
    }

    private void setAdapter() {



        establishmentAdapter = new EstablishmentAdapter(EstablishmentActivity.this,resultEstablishment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvEstablishment.setLayoutManager(layoutManager);
        rvEstablishment.setHasFixedSize(true);
        rvEstablishment.setAdapter(establishmentAdapter);

    }

    private void getEstablishmentList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register=apiInterface.call_get_establishment("Token "
                        + LoginShared.getLoginDataModel(EstablishmentActivity.this).getToken(),
                "application/json", MethodUtils.tender_id);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    resultEstablishment.clear();
                    try {
                        String responseString = response.body().string();
                        EstablishmentPojo establishmentPojo;
                        Gson gson = new Gson();
                        establishmentPojo = gson.fromJson(responseString, EstablishmentPojo.class);
                        //tendorsResultList = tendorsListingPojo.getResult();
                        resultEstablishment.addAll(establishmentPojo.getResult());
                        establishmentAdapter.notifyDataSetChanged();
                        System.out.println("establishment list=================>>>"+responseString);

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
