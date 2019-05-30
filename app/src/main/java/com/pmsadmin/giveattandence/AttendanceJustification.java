package com.pmsadmin.giveattandence;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;

import java.io.IOException;

public class AttendanceJustification extends Activity {

    EditText etJustification;
    Button btSubmit;
    Integer attendence_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_justification_attendence);
        etJustification = (EditText) findViewById(R.id.etJustification);

        btSubmit = (Button) findViewById(R.id.btSubmit);

        Intent intent = getIntent();
        attendence_id = intent.getIntExtra("attendence_id",1);

        System.out.print("attendence_id: "+String.valueOf(attendence_id));


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitJustification();
            }
        });

    }

    private void submitJustification() {

        JsonObject object = new JsonObject();
        object.addProperty("justification", etJustification.getText().toString().trim());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        //object.addProperty("deviation_type", deviation_type);
        System.out.println("object: " + object.toString());
        final Call<ResponseBody> register=apiInterface.call_attandance_justification("Token "
                        + LoginShared.getLoginDataModel(AttendanceJustification.this).getToken(), attendence_id,object);


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
}
