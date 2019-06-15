package com.pmsadmin.survey.resource.hydrological_data;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.AddEstablishmentActivity;
import com.pmsadmin.survey.resource.contractor_vendor.MachineryAddActivity;
import com.pmsadmin.survey.resource.hydrological_data.hydro_pojo.GetAddHydroDataResponse;
import com.pmsadmin.utils.progressloader.LoadingData;

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

public class AddHydrologicalData extends BaseActivity {

    private EditText etName, etDescription;
    private Button btSubmit;

    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;
    private LoadingData loader;
    private View view;

    EditText etDocumentName;

    Integer moduleID = 0;


    private String pdfFilePath = "";
    File file;

    ImageView ivSelect, ivPdf;

    private int PICK_PDF_REQUEST = 1;

    TextView tvUpload;
    public static String a_token;
    private static final int STORAGE_PERMISSION_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_add_hydrological_data, null);
        addContentView(view);
        //setContentView(R.layout.activity_add_hydrological_data);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        btSubmit = findViewById(R.id.btSubmit);
        etDocumentName = findViewById(R.id.etDocumentName);

        tvUpload = findViewById(R.id.tvUpload);
        ivSelect = findViewById(R.id.ivSelect);
        ivPdf = findViewById(R.id.ivPdf);

        //etDocumentName = findViewById(R.id.etDocumentName);




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

        a_token = LoginShared.getLoginDataModel(this).getToken();


        requestStoragePermission();

        clickListners();

    }

    private void clickListners() {

        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });


        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Uploadfile: " + String.valueOf(file) + " id: " + String.valueOf(moduleID));

                if (moduleID != 0) {

                    if (pdfFilePath.equals("")) {
                        MethodUtils.errorMsg(AddHydrologicalData.this, "Please select any Document");
                    } else if (etDocumentName.getText().toString().equals("")) {
                        MethodUtils.errorMsg(AddHydrologicalData.this, "Please enter Document name");
                    } else {

                        UploadDocuments(file);
                    }

                }
            }
        });



    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            try {
                file = FileUtil.from(this, data.getData());
                System.out.println("filePath: " + String.valueOf(file));
                pdfFilePath = String.valueOf(file);
                if (file != null) {
                    ivSelect.setVisibility(View.GONE);
                    ivPdf.setVisibility(View.VISIBLE);
                }

                String str = String.valueOf(file);

                //UploadDocuments(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    private void validation() {

        if (etName.getText().toString().equals("")){

            MethodUtils.errorMsg(AddHydrologicalData.this, "Please enter Hydrological Data Factor");
        }else if (etDescription.getText().toString().equals("")){

            MethodUtils.errorMsg(AddHydrologicalData.this, "Please enter Details");
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

                        GetAddHydroDataResponse getAddHydroDataResponse;
                        Gson gson = new Gson();
                        getAddHydroDataResponse = gson.fromJson(responseString,GetAddHydroDataResponse.class);
                        MethodUtils.errorMsg(AddHydrologicalData.this, "Now you can upload Document.");

                        moduleID = getAddHydroDataResponse.getId();
                        btSubmit.setClickable(false);
                        btSubmit.setVisibility(View.INVISIBLE);
                        tvUpload.setVisibility(View.VISIBLE);

                        //finish();
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


    public void UploadDocuments(final File file) {
        // loader.show_with_label("Loading");
//        loader.show();
        System.out.println("=================================");
//        File file = new File(pdf_path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part pdf = MultipartBody.Part.createFormData("document", file.getName(), requestFile);
        RequestBody tender = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(MethodUtils.tender_id));
        RequestBody module_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(moduleID));
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

        final Call<ResponseBody> register = apiInterface.call_add_Hydro_doc(pdf, tender, module_id, document_name);

        register.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (loader != null && loader.isShowing())
//                    loader.dismiss();
//                System.out.println("///////////////////////");
                try {
                    Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    if (response.code() == 200 || response.code() == 201) {
                        String responseString = response.body().string();
                        Log.d("responseString", responseString);
                        System.out.println("respons_save_data===========>>>" + responseString);
                        Toast.makeText(AddHydrologicalData.this, "Data uploaded sucessfully", Toast.LENGTH_SHORT).show();

                        pdfFilePath = "";
                        ivSelect.setVisibility(View.VISIBLE);
                        ivPdf.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
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
            final TrustManager[] trustAllCerts = new TrustManager[]{
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
                    .readTimeout(2, TimeUnit.MINUTES).build();
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

                    okhttp3.Request.Builder builder = originalRequest.newBuilder().header("Authorization", "Token " + a_token);

                    okhttp3.Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            }).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
