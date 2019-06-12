package com.pmsadmin.dialog.add_material_dialogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.RawMaterialsActivity;
import com.pmsadmin.survey.coordinates.raw_materials_pojo.RawMaterialsPojo;
import com.pmsadmin.survey.coordinates.raw_materials_pojo.Result;
import com.pmsadmin.survey.resource.hydrological_data.AddHydrologicalData;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddMaterialDialogue extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spMaterial;

    List<Result> rawMaterialsResultList = new ArrayList<>();
    ArrayList<String> matrialArraylist = new ArrayList<>();
    private LoadingData loader;
    ArrayAdapter<String> adapter;
    String name,id;
    TextView tvSubmit;

    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;

    public static String a_token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_add_material_dialogue);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().hide();
        loader = new LoadingData(AddMaterialDialogue.this);

        getRawMaterialsList();

        spMaterial = findViewById(R.id.spMaterial);
        tvSubmit = findViewById(R.id.tvSubmit);
        spMaterial.setOnItemSelectedListener(this);


        //loader = new LoadingData(AddMaterialDialogue.this);


        geocoder = new Geocoder(AddMaterialDialogue.this, Locale.getDefault());

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



        a_token = LoginShared.getLoginDataModel(this).getToken();

        System.out.println("a_token: "+a_token);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spMaterial.getSelectedItem().equals("--Select--")) {
                    Result result = new Result();
                    result.setId(Integer.valueOf(getID(spMaterial.getSelectedItem().toString())));
                    id = String.valueOf(result.getId());

                    //addMaterial();

                }else{
                    Toast.makeText(AddMaterialDialogue.this, "Please select a name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void addMaterial() {

        loader.show_with_label("Please wait");
        JsonObject object = new JsonObject();
        object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
        object.addProperty("tender_survey_material", id);
        object.addProperty("supplier_name", "");
        object.addProperty("rate", "");
        object.addProperty("latitude", String.valueOf(currentLat));
        object.addProperty("longitude", String.valueOf(currentLng));
        object.addProperty("address", addresses.get(0).getAddressLine(0));

        System.out.println("object: "+object.toString());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_materials("Token "
                        + LoginShared.getLoginDataModel(AddMaterialDialogue.this).getToken(),
                "application/json",
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code()==200) {

                    String responseString = "";
                    try {
                        responseString = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("materialSys: "+responseString);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




    }


    private void getRawMaterialsList() {

        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_raw_materials_listing("Token "
                        + LoginShared.getLoginDataModel(AddMaterialDialogue.this).getToken(),
                "application/json");

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.optInt("request_status") == 1) {

                            Gson gson = new Gson();
                            RawMaterialsPojo rawMaterialsPojo;
                            rawMaterialsPojo = gson.fromJson(responseString, RawMaterialsPojo.class);
                            //rawMaterialsResultList.addAll(rawMaterialsPojo.getResult());
//                            rawMaterialsResultList = rawMaterialsPojo.getResult();
                            for (int i=0;i<rawMaterialsPojo.getResult().size(); i++){
                                Result result = new Result(rawMaterialsPojo.getResult().get(i).getId(),rawMaterialsPojo.getResult().get(i).getName());
                                rawMaterialsResultList.add(result);
                            }

                            for (int i=0;i<rawMaterialsResultList.size();i++) {
                                name = rawMaterialsResultList.get(i).getName();
                                matrialArraylist.add(name);
                            }
                            matrialArraylist.add(0, "--Select--");
                            adapter = new ArrayAdapter<String>(AddMaterialDialogue.this, R.layout.spinner_item, R.id.item, matrialArraylist);
                            spMaterial.setAdapter(adapter);


                            System.out.println("rawMaterialsResultList: "+rawMaterialsResultList.size());

                            //rawMaterialAdapter.notifyDataSetChanged();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + rawMaterialsResultList.get(Integer.parseInt(parent.getItemAtPosition(position).toString())).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getID(final String name){
        for(Result result : rawMaterialsResultList){
            if(result.getName().equals(name)){
                id = String.valueOf(result.getId());
                return id;
            }
        }
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }
}
