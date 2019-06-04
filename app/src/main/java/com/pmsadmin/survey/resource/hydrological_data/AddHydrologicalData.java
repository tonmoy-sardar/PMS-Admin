package com.pmsadmin.survey.resource.hydrological_data;

import androidx.appcompat.app.AppCompatActivity;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.AddEstablishmentActivity;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddHydrologicalData extends Activity {

    private EditText etName, etDescription;
    private Button btSubmit;

    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;
    private LoadingData loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hydrological_data);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        btSubmit = findViewById(R.id.btSubmit);




        loader = new LoadingData(AddHydrologicalData.this);


        geocoder = new Geocoder(AddHydrologicalData.this, Locale.getDefault());

        location = new SimpleLocation(this, false, false, 10000);


        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);


        }


        location.setListener(new SimpleLocation.Listener() {

            public void onPositionChanged() {
                // new location data has been received and can be accessed

                currentLat = location.getLatitude();
                currentLng = location.getLongitude();

            }

        });

        if (currentLat == 0.0 && currentLng == 0.0) {
            int i = 0;
            while (i < 5) {
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                if (currentLat == 0.0 && currentLng == 0.0) {
                    i++;
                } else {
                    break;
                }
            }
        }


        try {
            addresses = geocoder.getFromLocation(currentLat, currentLng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("adress: "+ addresses.get(0).getAddressLine(0));


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
            }
        });


    }

    private void validation() {

        if (etName.getText().toString().equals("")){

        }else if (etDescription.getText().toString().equals("")){

        }else {
            addHydrologicalData();
        }
    }

    private void addHydrologicalData() {

        loader.show_with_label("Please wait");
        JsonObject object = new JsonObject();
        object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
        object.addProperty("name", etName.getText().toString().trim());
        object.addProperty("details", etDescription.getText().toString().trim());
        object.addProperty("latitude", String.valueOf(currentLat));
        object.addProperty("longitude", String.valueOf(currentLng));
        object.addProperty("address", addresses.get(0).getAddressLine(0));

        System.out.println("Hydro: "+object.toString());


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_hydrological("Token "
                        + LoginShared.getLoginDataModel(AddHydrologicalData.this).getToken(),
                "application/json",
                object);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code()==200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("hyroSys: "+responseString);
                        finish();
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


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }
}
