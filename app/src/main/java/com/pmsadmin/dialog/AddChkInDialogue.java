package com.pmsadmin.dialog;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

public class AddChkInDialogue extends Activity {

    private TextView tvSearchLocation;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 4;

    private double placesLat;
    private double placesLng;

    private Button btn_submit;
    private EditText etProject;

    private LoadingData loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue_add_chkin);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader = new LoadingData(AddChkInDialogue.this);


        initLayout();
    }

    private void initLayout() {

        tvSearchLocation = (TextView) findViewById(R.id.tvSearchLocation);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        etProject = (EditText) findViewById(R.id.etProject);

        tvSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callAddSurveyApi();
            }
        });
    }

    private void callAddSurveyApi() {

        loader.show_with_label("Please wait");
        JsonObject object = new JsonObject();
        object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
        object.addProperty("name", etProject.getText().toString().trim());
        object.addProperty("latitude", String.valueOf(placesLat));
        object.addProperty("longitude", String.valueOf(placesLng));
        object.addProperty("address", tvSearchLocation.getText().toString().trim());


        System.out.println("objectsurvey: "+object.toString());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_tender_survey_location_add("Token "
                + LoginShared.getLoginDataModel(AddChkInDialogue.this).getToken(),"application/json",object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (loader != null && loader.isShowing())
                    loader.dismiss();

                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();
                        System.out.println("Submit: "+responseString);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optBoolean("status")== true){

                            etProject.setText("");
                            tvSearchLocation.setText("");
                            finish();
                        }





                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        //MethodUtils.errorMsg(AddChkInDialogue.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(AddChkInDialogue.this, AddChkInDialogue.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

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
                tvSearchLocation.setText(place.getName().toString());
                placesLat = place.getLatLng().latitude;
                placesLng = place.getLatLng().longitude;

                System.out.println("latLng: "+String.valueOf(placesLat)+" "+String.valueOf(placesLng));


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("ERROR", "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }




}
