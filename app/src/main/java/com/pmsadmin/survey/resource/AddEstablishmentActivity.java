package com.pmsadmin.survey.resource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import id.zelory.compressor.FileUtil;
import im.delight.android.location.SimpleLocation;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.LogoutDialogue;
import com.pmsadmin.dialog.AddChkInDialogue;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.CheckInActivity;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.pmsadmin.apilist.ApiList.BASE_URL;

public class AddEstablishmentActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header;

    TextView tvSubmit;
    private EditText etEstablishmentName,etDescription,etDocumentName;

    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;
    private LoadingData loader;

    ImageView ivSelect,iv_upload_file,ivPdf;
    private int PICK_PDF_REQUEST = 1;
    File file;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static String a_token;
    int tender_id=0;
    int id= 0;
    String is_file_added="";
    LinearLayout ll_add_document_fields;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_establishment, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        a_token = LoginShared.getLoginDataModel(this).getToken();
        System.out.println("token=======>>>"+a_token);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Add Establishment");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(AddEstablishmentActivity.this));

        etEstablishmentName = findViewById(R.id.etEstablishmentName);
        etDescription = findViewById(R.id.etDescription);
        tvSubmit = findViewById(R.id.tvSubmit);
        ivSelect = findViewById(R.id.ivSelect);
        etDocumentName = findViewById(R.id.etDocumentName);
        iv_upload_file = findViewById(R.id.iv_upload_file);
        ivPdf = findViewById(R.id.ivPdf);
        ll_add_document_fields = findViewById(R.id.ll_add_document_fields);


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        iv_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    if (is_file_added.equalsIgnoreCase("1")){
                        UploadDocuments(file);
                    } else {
                        Toast.makeText(AddEstablishmentActivity.this,"Please add your PDF document.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        loader = new LoadingData(AddEstablishmentActivity.this);
        geocoder = new Geocoder(AddEstablishmentActivity.this, Locale.getDefault());
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

        requestStoragePermission();
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                file = FileUtil.from(this, data.getData());
                System.out.println("filePath: "+file.getName());
                ivSelect.setVisibility(View.GONE);
                ivPdf.setVisibility(View.VISIBLE);
                is_file_added = "1";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICK_PDF_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }


    private void  validation() {
        if (etEstablishmentName.getText().toString().equals("")){
            etEstablishmentName.setError("Enter establishment name");
            etEstablishmentName.requestFocus();
        }else if (etDescription.getText().toString().equals("")){
            etDescription.setError("Enter establishment details");
            etDescription.requestFocus();
        }else {
            addEstablishMent();
        }
    }


    public boolean checkValidation() {
        if (etDocumentName.getText().toString().trim().length() < 1) {
            etDocumentName.setError("Enter your Document name.");
            etDocumentName.requestFocus();
            return false;
        }  else {
            return true;
        }
    }


    private void addEstablishMent() {

        loader.show_with_label("Please wait");
        JsonObject object = new JsonObject();
        object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
        object.addProperty("name", etEstablishmentName.getText().toString().trim());
        object.addProperty("details", etDescription.getText().toString().trim());
        object.addProperty("latitude", String.valueOf(currentLat));
        object.addProperty("longitude", String.valueOf(currentLng));
        object.addProperty("address", addresses.get(0).getAddressLine(0));

        System.out.println("Esta: "+object.toString());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_establishment_add("Token "
                        + LoginShared.getLoginDataModel(AddEstablishmentActivity.this).getToken(),
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
                        System.out.println("add establishment response==========>>>"+responseString);
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject.getBoolean("status")){
                                tvSubmit.setText("ADDED");
                                tvSubmit.setEnabled(false);
                                etEstablishmentName.setEnabled(false);
                                etDescription.setEnabled(false);
                                tender_id = jsonObject.getInt("tender");
                                id = jsonObject.getInt("id");
                                ll_add_document_fields.setVisibility(View.VISIBLE);
                                Toast.makeText(AddEstablishmentActivity.this,"Establishment Added successfully.",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddEstablishmentActivity.this,"Something went wrong, Please try again.",Toast.LENGTH_LONG).show();
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



    public void UploadDocuments(final File file) {
        // loader.show_with_label("Loading");
//        loader.show();
        System.out.println("=================================");
//        File file = new File(pdf_path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part pdf = MultipartBody.Part.createFormData("document", file.getName(),requestFile);
        RequestBody tender = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(tender_id));
        RequestBody module_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), etDocumentName.getText().toString());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient())
                .build();
//        Retrofit retrofit = AppConfig.getRetrofit(BASE_URL);
//        Retrofit retrofit = AppConfig.getRetrofit("http://192.168.24.243:8000/tender_survey_site_photos_add/");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_establishment_document(pdf,tender,module_id,document_name);

        register.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (loader != null && loader.isShowing())
//                    loader.dismiss();
//                System.out.println("///////////////////////");
                try{
                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    if (response.code() == 200 || response.code()==201) {
                        String responseString = response.body().string();
                        System.out.println("respons_save_data===========>>>"+responseString);
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject.getBoolean("status")){
                                is_file_added = "";
                                etDocumentName.setText("");
                                ivSelect.setVisibility(View.VISIBLE);
                                ivPdf.setVisibility(View.GONE);
                                Toast.makeText(AddEstablishmentActivity.this,"Document Added sucessfully",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddEstablishmentActivity.this,"Something went wrong, Please try again.",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    // errorMsg(getActivity(), getActivity().getString(R.string.error_occurred));
                }


//                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                if (loader != null && loader.isShowing())
//                    loader.dismiss();
            }
        });






    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2,TimeUnit.MINUTES).build();
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            //   TODO: Passing headers here
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request originalRequest = chain.request();

                    okhttp3.Request.Builder builder = originalRequest.newBuilder().header("Authorization","Token "+a_token);

                    okhttp3.Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            }).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
