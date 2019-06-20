package com.pmsadmin.survey.resource.contractor_vendor;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.pmsadmin.dialog.AddContractorVendorDialogue;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.adpater.MachineryAdapter;
import com.pmsadmin.survey.resource.contractor_vendor.contract_vendor_pojo.AddMachineryResponsePojo;
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

public class MachineryAddActivity extends BaseActivity {

    private View view;
    EditText etPMType, etHire, etKhoraki, etDocumentName,etPMmake;


    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;
    private LoadingData loader;

    public static String a_token;

    private static final int STORAGE_PERMISSION_CODE = 123;

    private TextView tvSubmit, tvUpload;

    Integer moduleID = 0;


    private String pdfFilePath = "";
    File file;

    ImageView ivSelect, ivPdf;

    private int PICK_PDF_REQUEST = 1;
    private TextView tv_universal_header;

    private LinearLayout ll_add_document_fields,llImage,llUploadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_machinery_add, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        tv_universal_header = findViewById(R.id.tv_universal_header);

        tv_universal_header.setText("Add P & M");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        //tvUpload = findViewById(R.id.tvUpload);
        //tvUpload.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        etPMType = findViewById(R.id.etPMType);
        etPMType.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        etHire = findViewById(R.id.etHire);
        etHire.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        etKhoraki = findViewById(R.id.etKhoraki);
        etKhoraki.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        etDocumentName = findViewById(R.id.etDocumentName);
        etDocumentName.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        etPMmake = findViewById(R.id.etPMmake);
        etPMmake.setTypeface(MethodUtils.getNormalFont(MachineryAddActivity.this));

        ivSelect = findViewById(R.id.ivSelect);
        ivPdf = findViewById(R.id.ivPdf);

        ll_add_document_fields = findViewById(R.id.ll_add_document_fields);
        llImage = findViewById(R.id.llImage);
        llUploadFile = findViewById(R.id.llUploadFile);

        loader = new LoadingData(MachineryAddActivity.this);


        geocoder = new Geocoder(MachineryAddActivity.this, Locale.getDefault());

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


        System.out.println("adress: " + addresses.get(0).getAddressLine(0));

        a_token = LoginShared.getLoginDataModel(this).getToken();




        requestStoragePermission();


        clickListners();
    }


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
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

    private void clickListners() {

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etPMType.getText().toString().equals("")){

                    MethodUtils.errorMsg(MachineryAddActivity.this, "Please enter P & M Type!");

                }else if (etPMmake.getText().toString().equals("")){

                    MethodUtils.errorMsg(MachineryAddActivity.this, "Please enter Make!");

                }else if (etHire.getText().toString().equals("")){

                    MethodUtils.errorMsg(MachineryAddActivity.this, "Please enter Hire!");

                }else if (etKhoraki.getText().toString().equals("")){

                    MethodUtils.errorMsg(MachineryAddActivity.this, "Please enter Khoraki!");

                }else {

                    postData();
                }
            }
        });


        llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });


        llUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Uploadfile: " + String.valueOf(file) + " id: " + String.valueOf(moduleID));

                if (moduleID != 0) {

                    if (pdfFilePath.equals("")) {
                        MethodUtils.errorMsg(MachineryAddActivity.this, "Please select any Document");
                    } else if (etDocumentName.getText().toString().equals("")) {
                        MethodUtils.errorMsg(MachineryAddActivity.this, "Please enter Document name");
                    } else {

                        UploadDocuments(file);
                    }

                }
            }
        });

    }

    private void postData() {

        loader.show_with_label("Please wait");
        JsonObject object = new JsonObject();
        object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
        object.addProperty("name", etPMType.getText().toString().trim());
        object.addProperty("make", etPMmake.getText().toString());
        object.addProperty("hire", etHire.getText().toString());
        object.addProperty("khoraki", etKhoraki.getText().toString());
        object.addProperty("description", "");
        object.addProperty("latitude", String.valueOf(currentLat));
        object.addProperty("longitude", String.valueOf(currentLng));
        object.addProperty("address", addresses.get(0).getAddressLine(0));

        System.out.println("objectMachinery: " + object.toString());


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_machinery("Token "
                        + LoginShared.getLoginDataModel(MachineryAddActivity.this).getToken(),
                "application/json",
                object);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        System.out.println("responseStringMachinery: " + responseString);
                        Gson gson = new Gson();

                        AddMachineryResponsePojo addMachineryResponsePojo;
                        addMachineryResponsePojo = gson.fromJson(responseString, AddMachineryResponsePojo.class);
                        MethodUtils.errorMsg(MachineryAddActivity.this, "Now you can upload Document.");

                        moduleID = addMachineryResponsePojo.getId();
                        /*tvSubmit.setClickable(false);
                        tvSubmit.setVisibility(View.INVISIBLE);
                        tvUpload.setVisibility(View.VISIBLE);*/



                        tvSubmit.setClickable(false);
                        tvSubmit.setText("Saved");
                        etPMType.setEnabled(false);
                        etPMmake.setEnabled(false);
                        etHire.setEnabled(false);
                        etKhoraki.setEnabled(false);
                        ll_add_document_fields.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                MethodUtils.errorMsg(MachineryAddActivity.this, "Something went wrong!");
            }
        });
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
                    /*ivSelect.setVisibility(View.GONE);
                    ivPdf.setVisibility(View.VISIBLE);*/

                    ivSelect.setVisibility(View.GONE);
                    ivPdf.setVisibility(View.VISIBLE);
                    String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+ 1);
                    System.out.println("extension: "+extension);


                    if (extension.equals("pdf")) {
                        ivSelect.setVisibility(View.GONE);
                        ivPdf.setVisibility(View.VISIBLE);
                    }else {
                        ivSelect.setVisibility(View.GONE);
                        ivPdf.setVisibility(View.VISIBLE);
                        ivPdf.setImageResource(R.drawable.ic_image_blue_24dp);
                    }


                }

                String str = String.valueOf(file);

                //UploadDocuments(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

        final Call<ResponseBody> register = apiInterface.call_add_machinery_doc(pdf, tender, module_id, document_name);

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
                        Toast.makeText(MachineryAddActivity.this, "Data uploaded sucessfully", Toast.LENGTH_SHORT).show();

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
