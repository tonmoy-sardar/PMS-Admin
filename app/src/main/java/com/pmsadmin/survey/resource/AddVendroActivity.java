package com.pmsadmin.survey.resource;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.pmsadmin.survey.coordinates.MaterialDetails;
import com.pmsadmin.survey.coordinates.add_vendor_response_pojo.AddVendorPojoResponse;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.Result;
import com.pmsadmin.survey.resource.add_vendor_list_pojo.AddVendorList;
import com.pmsadmin.survey.resource.adpater.VendorDropdownAdapter;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
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

    int external_user = 0;

    Button btSave;

    TextView tvAddress;

    ArrayList<AddVendorList> vendorPojoList = new ArrayList<AddVendorList>();
    VendorDropdownAdapter vendorDropdownAdapter;

    String external_userr = "";
    String external_user_mappingg = "";

    private LinearLayout ll_add_document_fields;

    private static final int STORAGE_PERMISSION_CODE = 123;


    private EditText etDocumentName;

    private ImageView ivSelect, ivPdf, iv_upload_file;

    File file;
    String is_file_added="";
    public static String a_token;



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

        System.out.println("external_user_type_id: " + external_user_type_id + " Size: " + resultList.size());

        a_token = LoginShared.getLoginDataModel(this).getToken();


        tvSelectVendor = findViewById(R.id.tvSelectVendor);
        tvSelectVendor.setTypeface(MethodUtils.getNormalFont(AddVendroActivity.this));
        llDropDown = findViewById(R.id.llDropDown);
        rvVendor = findViewById(R.id.rvVendor);
        btAdd = findViewById(R.id.btAdd);
        btSave = findViewById(R.id.btSave);
        tvAddress = findViewById(R.id.tvAddress);
        ll_add_document_fields = findViewById(R.id.ll_add_document_fields);
        etDocumentName = findViewById(R.id.etDocumentName);
        ivSelect = findViewById(R.id.ivSelect);
        ivPdf = findViewById(R.id.ivPdf);
        iv_upload_file = findViewById(R.id.iv_upload_file);


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


        System.out.println("adress: " + addresses.get(0).getAddressLine(0));


        tvAddress.setText(addresses.get(0).getAddressLine(0));


        if (NetworkCheck.getInstant(AddVendroActivity.this).isConnectingToInternet()) {

            getVendorList();
        } else {
            MethodUtils.errorMsg(AddVendroActivity.this, "Please check your phone's network connection");
        }


        clickListners();


        vendorDropdownAdapter = new VendorDropdownAdapter(AddVendroActivity.this, vendorPojoList, external_user_type_id, resultList);
        vendorDropdownAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(AddVendroActivity.this, RecyclerView.VERTICAL, false);
        rvVendor.setHasFixedSize(true);
        rvVendor.setLayoutManager(linearLayoutManager);
        rvVendor.setAdapter(vendorDropdownAdapter);

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

    private int PICK_PDF_REQUEST = 1;


    private void clickListners() {


        tvSelectVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llDropDown.getVisibility() == View.GONE) {
                    llDropDown.setVisibility(View.VISIBLE);
                } else {
                    llDropDown.setVisibility(View.GONE);
                }
            }
        });


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvSelectVendor.getText().toString().trim().equals("Select")) {

                    MethodUtils.errorMsg(AddVendroActivity.this, "Please enter valid vendor");

                } else {
                    if (NetworkCheck.getInstant(AddVendroActivity.this).isConnectingToInternet()) {
                        addVendor();
                    } else {
                        MethodUtils.errorMsg(AddVendroActivity.this, "Please check your internet connection");
                    }
                }


            }
        });


        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            PICK_PDF_REQUEST);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(AddVendroActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        iv_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file!= null){

                    if (!etDocumentName.getText().toString().equals("")) {
                        uploadFile(file);
                    }else {
                        MethodUtils.errorMsg(AddVendroActivity.this, "Please enter Document name");
                    }
                }else {
                    MethodUtils.errorMsg(AddVendroActivity.this, "Please select a Document");
                }
            }
        });


    }

    private void uploadFile(File file) {

        System.out.println("=================================");


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RequestBody external_user = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(external_userr));
        RequestBody external_user_mapping = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(external_user_mappingg));
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), etDocumentName.getText().toString());
        MultipartBody.Part pdf = MultipartBody.Part.createFormData("document", file.getName(), requestFile);

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

        final Call<ResponseBody> register = apiInterface.call_add_Materials_doc(external_user,external_user_mapping,document_name,pdf );

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
                        Toast.makeText(AddVendroActivity.this, "Data uploaded sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                        /*getExternalMappingUserList();

                        pdfFilePath = "";*/


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






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                file = FileUtil.from(this, data.getData());
                System.out.println("filePath: "+file.getName());

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
                is_file_added = "1";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        object.add("mapping_details", job);

        System.out.println("mapping2: " + object);


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_add_vendor("Token "
                        + LoginShared.getLoginDataModel(AddVendroActivity.this).getToken(),
                "application/json",
                object);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("wtf: " + responseString);
                        AddVendorPojoResponse addVendorPojoResponse;
                        Gson gson = new Gson();
                        addVendorPojoResponse = gson.fromJson(responseString, AddVendorPojoResponse.class);
                        external_userr = String.valueOf(addVendorPojoResponse.getMappingDetails().getExternalUser());
                        external_user_mappingg = String.valueOf(addVendorPojoResponse.getMappingDetails().getId());
                        btSave.setText("Saved");
                        btSave.setEnabled(false);
                        tvSelectVendor.setEnabled(false);
                        ll_add_document_fields.setVisibility(View.VISIBLE);
                        MethodUtils.errorMsg(AddVendroActivity.this, "Vendor added successfully. Now you can upload Document");
                        //finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
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

        final Call<ResponseBody> register = apiInterface.call_get_external_user_add("Token "
                        + LoginShared.getLoginDataModel(AddVendroActivity.this).getToken(),
                "application/json");


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    String responseString = null;
                    try {

                        if (addVendorLists.size() > 0) {
                            addVendorLists.clear();
                            if (vendorPojoList.size() > 0) {
                                vendorPojoList.clear();
                            }
                        }
                        responseString = response.body().string();
                        System.out.println("ppp: "+responseString);

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


                                    if (resultList.get(j).getExternalUser().equals(addVendorLists.get(i).getId())) {
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

                                if (temp == true) {
                                    vendorPojoList.add(addVendorLists.get(i));
                                }
                            }
                        } else {
                            vendorPojoList.addAll(addVendorLists);
                        }
                        vendorDropdownAdapter.notifyDataSetChanged();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("add_vendor " + responseString);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    @Override
    public void OnItemClick(int position, String name, int id) {

        //System.out.println("pos: "+position+" "+name);
        System.out.println("pos: " + position + " " + name + addVendorLists.get(position).getId());


        llDropDown.setVisibility(View.GONE);
        //tvSelectVendor.setText(addVendorLists.get(position).getContactPersonName());
        tvSelectVendor.setText(name);
        external_user = id;




    }
}
