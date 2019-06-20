package com.pmsadmin.survey.coordinates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.seconddashboard.Dashboard2Activity;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CheckBoxAdapter;
import com.pmsadmin.survey.resource.contractor_vendor.ContractVendorReplica;
import com.pmsadmin.survey.resource.hydrological_data.AddHydrologicalData;
import com.pmsadmin.survey.unit_pojo.UnitPojo;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddMaterialActivity extends BaseActivity {

    private View view;

    private LinearLayout llDropDown;

    private TextView tv_universal_header,tvSelectUnit;
    private LoadingData loader;
    private RecyclerView rvUnit;
    private Button btAdd;

    private EditText etMaterialName,etItemCode,etDescription;
    private Button btSave;

    JsonObject jsonObject1 = new JsonObject();
    JsonArray jsonArray = new JsonArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_material, null);
        addContentView(view);
        //setContentView(R.layout.activity_add_material);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Add Material");
        loader = new LoadingData(AddMaterialActivity.this);

        rvUnit = findViewById(R.id.rvUnit);
        btAdd = findViewById(R.id.btAdd);
        btAdd.setTypeface(MethodUtils.getNormalFont(AddMaterialActivity.this));


        llDropDown = findViewById(R.id.llDropDown);
        etMaterialName = findViewById(R.id.etMaterialName);
        etMaterialName.setTypeface(MethodUtils.getNormalFont(AddMaterialActivity.this));
        etItemCode = findViewById(R.id.etItemCode);
        etItemCode.setTypeface(MethodUtils.getNormalFont(AddMaterialActivity.this));
        tvSelectUnit = findViewById(R.id.tvSelectUnit);
        tvSelectUnit.setTypeface(MethodUtils.getNormalFont(AddMaterialActivity.this));
        etDescription = findViewById(R.id.etDescription);
        etDescription.setTypeface(MethodUtils.getNormalFont(AddMaterialActivity.this));
        btSave = findViewById(R.id.btSave);
        btSave.setTypeface(MethodUtils.getNormalFont(AddMaterialActivity.this));

        getUnit();

        clickListners();

    }

    private void clickListners() {

        tvSelectUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llDropDown.setVisibility(View.VISIBLE);
                /*if (tvSelectUnit.getVisibility() == View.VISIBLE){
                    llDropDown.setVisibility(View.GONE);
                }else if (tvSelectUnit.getVisibility() == View.GONE){
                    llDropDown.setVisibility(View.VISIBLE);
                }*/
            }
        });





    }

    private void getUnit() {

        loader.show_with_label("Please wait");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register = apiInterface.call_get_unit("Token "
                        + LoginShared.getLoginDataModel(AddMaterialActivity.this).getToken(),
                "application/json");


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        UnitPojo unitPojo;
                        Gson gson = new Gson();
                        ArrayList<UnitPojo> unitPojoList = new Gson().fromJson(responseString,
                                new TypeToken<List<UnitPojo>>() {
                                }.getType());
                        System.out.println("unitPojoList: " + String.valueOf(unitPojoList.size()));

                        setAdapter(unitPojoList);


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

    private void setAdapter(final ArrayList<UnitPojo> unitPojoList) {


        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(AddMaterialActivity.this, unitPojoList);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(AddMaterialActivity.this, RecyclerView.VERTICAL, false);
        rvUnit.setHasFixedSize(true);
        rvUnit.setLayoutManager(linearLayoutManager);
        rvUnit.setAdapter(checkBoxAdapter);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDropDown.setVisibility(View.GONE);
                setUnit(unitPojoList);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //showUnit(unitPojoList);

                if (etItemCode.getText().toString().equals("")){

                    MethodUtils.errorMsg(AddMaterialActivity.this, "Please enter Item Code");

                }else if (etDescription.getText().toString().equals("")){

                    MethodUtils.errorMsg(AddMaterialActivity.this, "Please enter Description");
                }else if (etMaterialName.getText().toString().equals("")){
                    MethodUtils.errorMsg(AddMaterialActivity.this, "Please enter Material name");

                }else if (tvSelectUnit.getText().toString().equalsIgnoreCase("Select Unit")){
                    MethodUtils.errorMsg(AddMaterialActivity.this, "Please select unit");
                }else {
                    post(unitPojoList);
                }

            }
        });
    }

    private void setUnit(ArrayList<UnitPojo> unitPojoList) {

        String data = "";
        for (int i = 0; i < unitPojoList.size(); i++){

            if (unitPojoList.get(i).isSelected() == true) {
                /*JsonObject job = new JsonObject();
                job.addProperty("unit_id", String.valueOf(unitPojoList.get(i).getId()));
                jsonArray.add(job);*/

                data = data + "," + unitPojoList.get(i).getCName();
            }
            //jsonArray.add(job);

        }


        String niceString = "";

        niceString = data.replaceFirst("^,", "");
        if (niceString.equals("")){
            tvSelectUnit.setText("Select Unit");
        }else {
            tvSelectUnit.setText(niceString);
        }

    }


    private void post(ArrayList<UnitPojo> unitPojoList){

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();


        for (int i = 0; i < unitPojoList.size(); i++){

            if (unitPojoList.get(i).isSelected() == true) {
                JsonObject job = new JsonObject();
                job.addProperty("unit_id", String.valueOf(unitPojoList.get(i).getId()));
                jsonArray.add(job);
            }
            //jsonArray.add(job);

        }


        loader.show_with_label("Please wait");

        JsonObject object = new JsonObject();
        object.addProperty("mat_code", etItemCode.getText().toString().trim());
        object.addProperty("name", etMaterialName.getText().toString().trim());
        object.addProperty("description", etDescription.getText().toString());
        object.add("materials_unit",jsonArray );

        System.out.println("text: "+String.valueOf(object));

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_materials("Token "
                        + LoginShared.getLoginDataModel(AddMaterialActivity.this).getToken(),
                "application/json",
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201 || response.code()==200) {
                    try {
                        if (loader != null && loader.isShowing())
                            loader.dismiss();

                        String responseString = response.body().string();
                        MethodUtils.errorMsg(AddMaterialActivity.this, "Material added successfully.");
                        finish();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                    if (loader != null && loader.isShowing())
                        loader.dismiss();

                    MethodUtils.errorMsg(AddMaterialActivity.this, "Something went wrong!");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                MethodUtils.errorMsg(AddMaterialActivity.this, "Something went wrong!");
            }
        });



    }

    /*private void showUnit(ArrayList<UnitPojo> unitPojoList) {

        String data = "";
        *//*JSONObject jsonObject = new JSONObject();

        JSONArray jsonArr = new JSONArray();*//*




        for (int i = 0; i < unitPojoList.size(); i++){

            if (unitPojoList.get(i).isSelected() == true){
                Toast.makeText(AddMaterialActivity.this,unitPojoList.get(i).getCName().toString(),Toast.LENGTH_SHORT).show();
                //System.out.println("nameUnit: "+unitPojoList.get(i).getCName());
                data = data + "," + unitPojoList.get(i).getCName();

                //JSONObject job = new JSONObject();

                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("unit_id",String.valueOf(unitPojoList.get(i).getId()));
                *//*try {
                    job.put("unit_id",unitPojoList.get(i).getCName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArr.put(job);*//*
                jsonArray.add(jsonObject2);
            }
        }

        String jsonFormattedString = String.valueOf(jsonArray).replaceAll("\\\\", "");

        System.out.println("converted: "+jsonFormattedString);

        //jsonObject1.addProperty("materials_unit", String.valueOf(jsonArray));
        jsonObject1.addProperty("materials_unit", jsonFormattedString);
        System.out.println("job: "+String.valueOf(jsonObject1));

        *//*try {
            jsonObject.put("materials_unit", String.valueOf(jsonArr));

            System.out.println("job: "+String.valueOf(jsonObject));

        } catch (JSONException e) {
            e.printStackTrace();
        }*//*

        String niceString = "";

        niceString = data.replaceFirst("^,", "");

        System.out.println("nameUnit: "+niceString);


        JsonObject object = new JsonObject();
        object.addProperty("mat_code", "M0006");
        object.addProperty("name", "60 mm stone chips");
        object.addProperty("description", "60 mm stone chips");
        object.addProperty("materials_unit", jsonFormattedString);

        System.out.println("mat_add: "+String.valueOf(object));
        Gson gson = new Gson();
        String bodyJson = gson.toJson(object);

        String jsonFormatted = String.valueOf(bodyJson).replaceAll("\\\\", "");
        System.out.println("Hola: "+jsonFormatted);




    }*/

    private void showUnit(ArrayList<UnitPojo> unitPojoList) {

        String data = "";
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArr = new JSONArray();




        for (int i = 0; i < unitPojoList.size(); i++){

            if (unitPojoList.get(i).isSelected() == true){
                Toast.makeText(AddMaterialActivity.this,unitPojoList.get(i).getCName().toString(),Toast.LENGTH_SHORT).show();
                //System.out.println("nameUnit: "+unitPojoList.get(i).getCName());
                data = data + "," + unitPojoList.get(i).getCName();

                JSONObject job = new JSONObject();

                /*JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("unit_id",String.valueOf(unitPojoList.get(i).getId()));*/
                try {
                    job.put("unit_id",String.valueOf(unitPojoList.get(i).getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArr.put(job);
                //jsonArray.add(jsonObject2);
            }
        }


        try {
        //JsonObject object = new JsonObject();
        jsonObject.put("mat_code", "M0008");
        jsonObject.put("name", "80 mm stone chips");
        jsonObject.put("description", "80 mm stone chips");
        jsonObject.put("materials_unit", jsonArr);

        System.out.println("Hello: "+String.valueOf(jsonObject));

            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_add_material("Token "
                            + LoginShared.getLoginDataModel(AddMaterialActivity.this).getToken(),
                    "application/json",
                    jsonObject);

            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 201 || response.code()==200) {
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








        } catch (JSONException e) {
            e.printStackTrace();
        }




    }






}
