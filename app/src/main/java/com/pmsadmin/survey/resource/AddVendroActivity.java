package com.pmsadmin.survey.resource;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.networking.NetworkCheck;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.Result;
import com.pmsadmin.survey.resource.add_vendor_list_pojo.AddVendorList;
import com.pmsadmin.survey.resource.adpater.VendorDropdownAdapter;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddVendroActivity extends BaseActivity implements VendorDropdownAdapter.OnItemClickListener {

    private View view;
    private TextView tv_universal_header;
    private LoadingData loader;
    private Integer external_user_type_id = 0;

    private TextView tvSelectVendor;
    private LinearLayout llDropDown;
    private RecyclerView rvVendor;
    private Button btAdd;
    ArrayList<AddVendorList> addVendorLists = new ArrayList<>();
    ArrayList<Result> resultList;
    //ArrayList<AddVendorList> addVendorLists


    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;

    int mat_id = 0;

    int external_user=0;

    Button btSave;

    TextView tvAddress;

    ArrayList<AddVendorList> vendorPojoList = new ArrayList<AddVendorList>();
    VendorDropdownAdapter vendorDropdownAdapter;


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_vendro, null);
        addContentView(view);
        //setContentView(R.layout.activity_add_vendro);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Add Vendor");
        loader = new LoadingData(AddVendroActivity.this);

        Bundle bundle = getIntent().getExtras();
        resultList = (ArrayList<Result>) bundle.getSerializable("myList");

        Intent mIntent = getIntent();
        external_user_type_id = mIntent.getIntExtra("external_user_type_id", 0);
        mat_id = mIntent.getIntExtra("mat_id", 0);

        System.out.println("external_user_type_id: "+external_user_type_id+" Size: "+resultList.size());






        tvSelectVendor = findViewById(R.id.tvSelectVendor);
        tvSelectVendor.setTypeface(MethodUtils.getNormalFont(AddVendroActivity.this));
        llDropDown = findViewById(R.id.llDropDown);
        rvVendor = findViewById(R.id.rvVendor);
        btAdd = findViewById(R.id.btAdd);
        btSave = findViewById(R.id.btSave);
        tvAddress = findViewById(R.id.tvAddress);


        geocoder = new Geocoder(AddVendroActivity.this, Locale.getDefault());

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


        tvAddress.setText( addresses.get(0).getAddressLine(0));



        if (NetworkCheck.getInstant(AddVendroActivity.this).isConnectingToInternet()) {

            getVendorList();
        }else {
            MethodUtils.errorMsg(AddVendroActivity.this, "Please check your phone's network connection");
        }


        clickListners();


        vendorDropdownAdapter = new VendorDropdownAdapter(AddVendroActivity.this, vendorPojoList,external_user_type_id, resultList);
        vendorDropdownAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(AddVendroActivity.this, RecyclerView.VERTICAL, false);
        rvVendor.setHasFixedSize(true);
        rvVendor.setLayoutManager(linearLayoutManager);
        rvVendor.setAdapter(vendorDropdownAdapter);
    }

    private void clickListners() {


        tvSelectVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llDropDown.getVisibility() == View.GONE) {
                    llDropDown.setVisibility(View.VISIBLE);
                }else {
                    llDropDown.setVisibility(View.GONE);
                }
            }
        });


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvSelectVendor.getText().toString().trim().equals("Select")){

                    MethodUtils.errorMsg(AddVendroActivity.this, "Please enter valid vendor");

                }else {
                    if (NetworkCheck.getInstant(AddVendroActivity.this).isConnectingToInternet()) {
                        addVendor();
                    }else {
                        MethodUtils.errorMsg(AddVendroActivity.this,"Please check your internet connection");
                    }
                }



            }
        });


    }

    private void addVendor() {

        JsonObject object = new JsonObject();
        object.addProperty("tender", MethodUtils.tender_id);
        object.addProperty("external_user_type", external_user_type_id);
        object.addProperty("tender_survey_material", mat_id);
        //JsonArray jsonArray = new JsonArray();
        JsonObject job = new JsonObject();
        job.addProperty("external_user", external_user);
        job.addProperty("latitude", String.valueOf(currentLat));
        job.addProperty("longitude", String.valueOf(currentLng));
        job.addProperty("address", addresses.get(0).getAddressLine(0));
        object.add("mapping_details",job );

        System.out.println("mapping2: "+object);


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_vendor("Token "
                        + LoginShared.getLoginDataModel(AddVendroActivity.this).getToken(),
                "application/json",
                object);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201 || response.code()==200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("wtf: "+responseString);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    MethodUtils.errorMsg(AddVendroActivity.this, "Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                MethodUtils.errorMsg(AddVendroActivity.this, "Something went wrong");
            }
        });
    }

    private void getVendorList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_external_user_add("Token "
                        + LoginShared.getLoginDataModel(AddVendroActivity.this).getToken(),
                "application/json");


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    String responseString = null;
                    try {

                        if (addVendorLists.size() > 0){
                            addVendorLists.clear();
                            if (vendorPojoList.size() > 0) {
                                vendorPojoList.clear();
                            }
                        }
                        responseString = response.body().string();

                        addVendorLists = new Gson().fromJson(responseString,
                                new TypeToken<List<AddVendorList>>() {
                                }.getType());

                        System.out.println("addVendorLists: " + String.valueOf(addVendorLists.size()));


                        if (resultList.size() > 0) {

                            for (int i = 0; i < addVendorLists.size(); i++) {
                                boolean temp = true;
                                for (int j = 0; j < resultList.size(); j++) {
                                    System.out.println("resultList getExternalUser id==>" + resultList.get(j).getExternalUser());
                                    System.out.println("addVendorLists id  ==>" + addVendorLists.get(i).getId());


                                    if (resultList.get(j).getExternalUser().equals(addVendorLists.get(i).getId())){
                                        temp = false;
                                        break;
                                    }

                                    /*if (!resultList.get(j).getExternalUser().equals(addVendorLists.get(i).getId())) {
                                        vendorPojoList.add(addVendorLists.get(i));
                                        System.out.println("vendorPojoList id ======>>>" + vendorPojoList.get(i).getContactPersonName());
                                        //arrayList.get(i).put("isAvailabel", true);
                                        System.out.println("----------Matched---------");
                                    }*/
                                }

                                if (temp == true){
                                    vendorPojoList.add(addVendorLists.get(i));
                                }
                            }
                        }else {
                            vendorPojoList.addAll(addVendorLists);
                        }
                        vendorDropdownAdapter.notifyDataSetChanged();





                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("add_vendor "+responseString);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setAdapter(ArrayList<AddVendorList> addVendorLists) {


        /*for (int i = 0; i < addVendorLists.size(); i++){

            boolean flag = false;
            int j = 0;
            while (j < resultList.size()){

                if (addVendorLists.get(i).getId() == resultList.get(i).getExternalUser()){

                    flag =true;
                    break;
                }
            }

            if (flag == false) {

                vendorPojoList.add(addVendorLists.get(i));
                //System.out.println("vendorPojoContact: "+vendorPojoList.get(i).getContactPersonName());
            }

        }

        System.out.println("vendorPojoList: "+vendorPojoList.size());*/







        VendorDropdownAdapter vendorDropdownAdapter = new VendorDropdownAdapter(AddVendroActivity.this, addVendorLists,external_user_type_id, resultList);
        vendorDropdownAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(AddVendroActivity.this, RecyclerView.VERTICAL, false);
        rvVendor.setHasFixedSize(true);
        rvVendor.setLayoutManager(linearLayoutManager);
        rvVendor.setAdapter(vendorDropdownAdapter);

        /*for(int i = 0; i < addVendorLists.size(); i++){

            if (addVendorLists.get(i).isSelected() == true){

                tvSelectVendor.setText(addVendorLists.get(i).getContactPersonName());
            }
        }*/

    }

    @Override
    public void OnItemClick(int position, String name, int id) {

        //System.out.println("pos: "+position+" "+name);
        System.out.println("pos: "+position+" "+name+ addVendorLists.get(position).getId());


        llDropDown.setVisibility(View.GONE);
        tvSelectVendor.setText(addVendorLists.get(position).getContactPersonName());
        external_user = id;


        /*for (int i = 0; i < resultList.size(); i++){

            if (resultList.get(i).getExternalUser() == id){
                System.out.println("mileche1: "+id);
                MethodUtils.errorMsg(AddVendroActivity.this,"This vendor already exist! Please select another vendro");

                break;

            }else {
                tvSelectVendor.setText(addVendorLists.get(position).getContactPersonName());
                external_user = id;
            }

        }*/

    }
}
