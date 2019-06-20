package com.pmsadmin.apply_local_conveyance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.apply_local_conveyance.dialog_fragment.Dialog_Fragment_job_alloted_by_list;
import com.pmsadmin.apply_local_conveyance.dialog_fragment.Dialog_Fragment_project_list;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.AddChkInDialogue;
import com.pmsadmin.dialog.AddContractorVendorDialogue;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.networking.NetworkCheck;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.CrusherDetailsActivity;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocalConveyance extends BaseActivity {

    private View view;
    private TextView tv_universal_header,tv_conveyance_name,tv_conveyance_department,tv_submit_for_approval,tv_conveyance_amount,tv_conveyance_job_alloted_by,
            tv_conveyance_purpose,tv_vehicle_type,tv_conveyance_to_place,tv_conveyance_date,tv_conveyance_project;
    ArrayList<JSONObject> arrayList_project_list;
    ArrayList<JSONObject> arrayList_manpower_list;

    TextView tvSearchLocation;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 4;
    private double placesLat;
    private double placesLng;
    private LoadingData loader;
    public List<Address> addresses;
    Geocoder geocoder;
    private double currentLat;
    private double currentLng;
    private SimpleLocation location;
    String is_selected = "",date="";
    DatePickerDialog picker;
    private static int SPLASH_TIME_OUT = 3000;

    private ImageView ivViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_local_conveyance, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        arrayList_project_list = new ArrayList<JSONObject>();
        arrayList_manpower_list = new ArrayList<JSONObject>();
        loader = new LoadingData(LocalConveyance.this);

        tvSearchLocation = findViewById(R.id.tvSearchLocation);
        tv_conveyance_name = findViewById(R.id.tv_conveyance_name);
        tv_conveyance_department = findViewById(R.id.tv_conveyance_department);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_submit_for_approval = findViewById(R.id.tv_submit_for_approval);
        tv_conveyance_amount = findViewById(R.id.tv_conveyance_amount);
        tv_conveyance_job_alloted_by = findViewById(R.id.tv_conveyance_job_alloted_by);
        tv_conveyance_purpose = findViewById(R.id.tv_conveyance_purpose);
        tv_vehicle_type = findViewById(R.id.tv_vehicle_type);
        tv_conveyance_to_place = findViewById(R.id.tv_conveyance_to_place);
        tv_conveyance_date = findViewById(R.id.tv_conveyance_date);
        tv_conveyance_project = findViewById(R.id.tv_conveyance_project);
        ivViewList = findViewById(R.id.ivViewList);


        tv_universal_header.setText("New Conveyance");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(LocalConveyance.this));
        tv_conveyance_name.setText(LoginShared.getLoginDataModel(LocalConveyance.this).getFirstName()+" "+
                LoginShared.getLoginDataModel(LocalConveyance.this).getLastName());
        tv_conveyance_department.setText("Department : ");


        tv_submit_for_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_employee_conveyance_add();
            }
        });


        tv_conveyance_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });


        tv_conveyance_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog_Fragment_project_list dialog_Fragment_project_list = new Dialog_Fragment_project_list();
                dialog_Fragment_project_list.setData(arrayList_project_list);
                dialog_Fragment_project_list.setOnDialogListener(new Dialog_Fragment_project_list.OnItemClickDialog() {
                    @Override
                    public void onItemClick(int position) {
                        try {
                            tv_conveyance_project.setText(arrayList_project_list.get(position).getString("name"));
                            tv_conveyance_project.setTag(arrayList_project_list.get(position).getString("id"));
                            dialog_Fragment_project_list.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog_Fragment_project_list.show(getSupportFragmentManager(), "dialog");
            }
        });

        tv_conveyance_job_alloted_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog_Fragment_job_alloted_by_list dialog_fragment_job_alloted_by_list = new Dialog_Fragment_job_alloted_by_list();
                dialog_fragment_job_alloted_by_list.setData(arrayList_manpower_list);
                dialog_fragment_job_alloted_by_list.setOnDialogListener(new Dialog_Fragment_job_alloted_by_list.OnItemClickDialog() {
                    @Override
                    public void onItemClick(int position) {
                        try {
                            tv_conveyance_job_alloted_by.setText(arrayList_manpower_list.get(position).getJSONObject("mmr_user").getString("employee_name"));
                            tv_conveyance_job_alloted_by.setTag(arrayList_manpower_list.get(position).getJSONObject("mmr_user").getString("employee_id"));
                            dialog_fragment_job_alloted_by_list.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog_fragment_job_alloted_by_list.show(getSupportFragmentManager(), "dialog");
            }
        });


        tvSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_selected = "1";
                openAutocompleteActivity();
            }
        });

        tv_conveyance_to_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_selected = "2";
                openAutocompleteActivity();
            }
        });


        get_projects_list();
        get_manpower_list_wo_pagination();


        clickListners();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                geocoder = new Geocoder(LocalConveyance.this, Locale.getDefault());
                location = new SimpleLocation(LocalConveyance.this, false, false, 10000);
                if (!location.hasLocationEnabled()) {
                    SimpleLocation.openSettings(LocalConveyance.this);
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
            }
        }, SPLASH_TIME_OUT);

    }

    private void clickListners() {

        ivViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkCheck.getInstant(LocalConveyance.this).isConnectingToInternet()) {
                    Intent intent = new Intent(LocalConveyance.this, MyConveyanceHistory.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else {
                    MethodUtils.errorMsg(LocalConveyance.this, "Please check Network connection");
                }

            }
        });
    }


    private void get_projects_list() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_get_projects_list("Token "
                + LoginShared.getLoginDataModel(LocalConveyance.this).getToken(), "application/json");

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        try {
                            arrayList_project_list.clear();
                            String responseString = response.body().string();
                            System.out.println("get_projects_list==========>>>" + responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONArray result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                arrayList_project_list.add(result.getJSONObject(i));
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


    private void get_manpower_list_wo_pagination() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_get_manpower_list_wo_pagination("Token "
                + LoginShared.getLoginDataModel(LocalConveyance.this).getToken(), "application/json");

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        try {
                            arrayList_manpower_list.clear();
                            String responseString = response.body().string();
                            System.out.println("get_manpower_list==========>>>" + responseString);
                            JSONArray result = new JSONArray(responseString);
                            for (int i = 0; i < result.length(); i++) {
                                arrayList_manpower_list.add(result.getJSONObject(i));
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


    private void call_employee_conveyance_add() {
        if (checkValidation()){
            try {
                loader.show_with_label("Please wait");

                JsonObject object = new JsonObject();
                object.addProperty("project", tv_conveyance_project.getTag().toString());
                object.addProperty("eligibility_per_day", "200.00");
                object.addProperty("date", tv_conveyance_date.getText().toString().trim()+ "T" + getCurrentTimeUsingDate());
                object.addProperty("from_place", tvSearchLocation.getText().toString());
                object.addProperty("to_place", tv_conveyance_to_place.getText().toString());
                object.addProperty("vechicle_type", tv_vehicle_type.getText().toString());
                object.addProperty("purpose", tv_conveyance_purpose.getText().toString());
                object.addProperty("job_alloted_by", tv_conveyance_job_alloted_by.getTag().toString());
                object.addProperty("ammount", tv_conveyance_amount.getText().toString());
                System.out.println("object======>>>>" + object.toString());


                Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                final Call<ResponseBody> register = apiInterface.call_employee_conveyance_add("Token " +
                        LoginShared.getLoginDataModel(LocalConveyance.this).getToken(), "application/json", object);


                register.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (loader != null && loader.isShowing())
                            loader.dismiss();
                        System.out.println("Service URL==>" + response.raw().request().url());
                        if (response.code() == 201 || response.code() == 200) {
                            System.out.println("Service URL==>" + response.raw().request().url());
                            try {
                                String responseString = response.body().string();
                                System.out.println("response output==========>>>" + responseString);
                                JSONObject jsonObject = new JSONObject(responseString);
                                if (jsonObject != null) {
                                    Toast.makeText(LocalConveyance.this, "Conveyance added successfully.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LocalConveyance.this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (loader != null && loader.isShowing())
                            loader.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void openAutocompleteActivity() {

        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("Exception", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("OK", "Place Selected: " + place.getName());
                if (is_selected.equalsIgnoreCase("1")){
                    tvSearchLocation.setText(place.getName().toString());
                    placesLat = place.getLatLng().latitude;
                    placesLng = place.getLatLng().longitude;
                    System.out.println("latLng: "+String.valueOf(placesLat)+" "+String.valueOf(placesLng));
                } else if (is_selected.equalsIgnoreCase("2")){
                    tv_conveyance_to_place.setText(place.getName().toString());
                    placesLat = place.getLatLng().latitude;
                    placesLng = place.getLatLng().longitude;
                    System.out.println("latLng: "+String.valueOf(placesLat)+" "+String.valueOf(placesLng));
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("ERROR", "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }


    public boolean checkValidation() {
        if (tv_conveyance_date.getText().toString().length() < 1) {
            Toast.makeText(LocalConveyance.this, "Enter conveyance date.", Toast.LENGTH_LONG).show();
            return false;
        }  else if (tvSearchLocation.getText().toString().length() < 1) {
            Toast.makeText(LocalConveyance.this, "Enter conveyance address.", Toast.LENGTH_LONG).show();
            return false;
        }   else if (tv_conveyance_to_place.getText().toString().length() < 1) {
            Toast.makeText(LocalConveyance.this, "Enter conveyance address.", Toast.LENGTH_LONG).show();
            return false;
        }  else if (tv_vehicle_type.getText().toString().length() < 1) {
            tv_vehicle_type.setError("Enter conveyance vehicle type.");
            tv_vehicle_type.requestFocus();
            return false;
        }  else if (tv_conveyance_purpose.getText().toString().length() < 1) {
            tv_conveyance_purpose.setError("Enter conveyance purpose.");
            tv_conveyance_purpose.requestFocus();
            return false;
        }  else if (tv_conveyance_job_alloted_by.getText().toString().length() < 1) {
            Toast.makeText(LocalConveyance.this, "Select conveyance job alloted by.", Toast.LENGTH_LONG).show();
            return false;
        }  else if (tv_conveyance_project.getText().toString().length() < 1) {
            Toast.makeText(LocalConveyance.this, "Select conveyance project.", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_conveyance_amount.getText().toString().length() < 1) {
            tv_conveyance_amount.setError("Enter conveyance amount.");
            tv_conveyance_amount.requestFocus();
            return false;
        }  else {
            return true;
        }
    }


    private void openCalendar() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(LocalConveyance.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_conveyance_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        //fromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();
    }
}
