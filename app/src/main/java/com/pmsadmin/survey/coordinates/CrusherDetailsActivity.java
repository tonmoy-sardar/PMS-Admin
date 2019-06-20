package com.pmsadmin.survey.coordinates;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.SitePhotoSurvey;
import com.pmsadmin.survey.coordinates.coordinate_adapter.AssignedCrusherListAdapter;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CrusherAddedDocumentListAdapter;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CrusherListAdapter;
import com.pmsadmin.survey.coordinates.dialog_fragment.Dialog_Fragment_external_user_list;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;
import com.pmsadmin.survey.resource.dialog_fragment.Dialog_Fragment_add_new_contact;
import com.pmsadmin.survey.resource.hydrological_data.HydrologicalDetails;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static com.pmsadmin.apilist.ApiList.BASE_URL;

public class CrusherDetailsActivity extends BaseActivity implements AssignedCrusherListAdapter.OnItemClickListener {

    public View view;
    private LoadingData loader;
    private TextView tv_universal_header, tv_meterial_title_name, tv_add_crusher_documents;
    RecyclerView rv_assigned_crusher_list, rv_crusher_document_list;
    ArrayList<JSONObject> arrayList;
    ArrayList<JSONObject> arrayList_external_user_list;
    ArrayList<JSONObject> arrayList_added_document_list;
    AssignedCrusherListAdapter assignedCrusherListAdapter;
    CrusherAddedDocumentListAdapter crusherAddedDocumentListAdapter;
    int user_type = 0;
    TextView tv_contact_person, tv_save_crusher, tv_contact_person_no;
    String external_user = "", tender_survey_material = "", external_user_mapping = "";
    String external_user_from_list = "", external_user_mapping_from_list = "";

    public List<Address> addresses;
    Geocoder geocoder;
    private double currentLat;
    private double currentLng;
    public GPSTracker gpsTracker;
    private SimpleLocation location;

    private int PICK_PDF_REQUEST = 1;
    private int ADD_MORE_FILE = 222;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static String a_token;
    File file;
    JSONArray array_add_documents = new JSONArray();
    int count = 0;
    NestedScrollView nsv_assign_crusher;
    String is_check = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_crusher_details, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        a_token = LoginShared.getLoginDataModel(this).getToken();
        System.out.println("token=======>>>" + a_token);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        rv_assigned_crusher_list = findViewById(R.id.rv_assigned_crusher_list);
        tv_meterial_title_name = findViewById(R.id.tv_meterial_title_name);
        tv_contact_person = findViewById(R.id.tv_contact_person);
        tv_save_crusher = findViewById(R.id.tv_save_crusher);
        tv_contact_person_no = findViewById(R.id.tv_contact_person_no);
        rv_crusher_document_list = findViewById(R.id.rv_crusher_document_list);
        tv_add_crusher_documents = findViewById(R.id.tv_add_crusher_documents);
        nsv_assign_crusher = findViewById(R.id.nsv_assign_crusher);

        rv_assigned_crusher_list.setNestedScrollingEnabled(false);
        rv_crusher_document_list.setNestedScrollingEnabled(false);

        tv_universal_header.setText("Crusher");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(CrusherDetailsActivity.this));
        loader = new LoadingData(CrusherDetailsActivity.this);

        Intent intent = getIntent();
        tv_meterial_title_name.setText(intent.getStringExtra("name"));
        tender_survey_material = intent.getStringExtra("id");


        arrayList_external_user_list = new ArrayList<JSONObject>();

        arrayList = new ArrayList<JSONObject>();
        assignedCrusherListAdapter = new AssignedCrusherListAdapter(CrusherDetailsActivity.this, arrayList);
        assignedCrusherListAdapter.setOnItemClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_assigned_crusher_list.setLayoutManager(layoutManager);
        rv_assigned_crusher_list.setHasFixedSize(true);
        rv_assigned_crusher_list.setAdapter(assignedCrusherListAdapter);


        arrayList_added_document_list = new ArrayList<JSONObject>();
        crusherAddedDocumentListAdapter = new CrusherAddedDocumentListAdapter(CrusherDetailsActivity.this, arrayList_added_document_list);
        RecyclerView.LayoutManager layoutManagerGrid = new GridLayoutManager(this, 3);
        rv_crusher_document_list.setLayoutManager(layoutManagerGrid);
        rv_crusher_document_list.setHasFixedSize(true);
        rv_crusher_document_list.setAdapter(crusherAddedDocumentListAdapter);


        tv_contact_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog_Fragment_external_user_list dialog_Fragment_external_user_list = new Dialog_Fragment_external_user_list();
                dialog_Fragment_external_user_list.setData(arrayList_external_user_list);
                dialog_Fragment_external_user_list.setOnDialogListener(new Dialog_Fragment_external_user_list.OnItemClickDialog() {
                    @Override
                    public void onItemClick(int position) {
                        try {
                            tv_contact_person.setText(arrayList_external_user_list.get(position).getString("contact_person_name"));
                            tv_contact_person_no.setText(arrayList_external_user_list.get(position).getString("contact_no"));
                            external_user = arrayList_external_user_list.get(position).getString("id");
                            dialog_Fragment_external_user_list.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog_Fragment_external_user_list.show(getSupportFragmentManager(), "dialog");
            }
        });


        tv_save_crusher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_contact_person.getText().toString().length() < 1) {
                    Toast.makeText(CrusherDetailsActivity.this, "Please select contact person.", Toast.LENGTH_LONG).show();
                } else {
                    add_external_user_mapping_add();
                }
            }
        });


        tv_add_crusher_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        getExternalUserType();

        getCurrentLatLongAddress();

        requestStoragePermission();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                file = FileUtil.from(this, data.getData());
                System.out.println("filePath: " + file.getName());
                JSONObject object = new JSONObject();
                object.put("file_path", file);
                object.put("file_name", file.getName().substring(0, file.getName().indexOf(".")));
                array_add_documents.put(object);
                arrayList_added_document_list.clear();
                for (int i = 0; i < array_add_documents.length(); i++) {
                    arrayList_added_document_list.add(array_add_documents.getJSONObject(i));
                }
                crusherAddedDocumentListAdapter.notifyDataSetChanged();
                count = arrayList_added_document_list.size() - 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == ADD_MORE_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                file = FileUtil.from(this, data.getData());
                System.out.println("filePath: " + file.getName());
                AddMoreDocuments(file,file.getName().substring(0, file.getName().indexOf(".")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }


    private void getExternalUserType() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_get_external_user_type("Token "
                        + LoginShared.getLoginDataModel(CrusherDetailsActivity.this).getToken(),
                "application/json");

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        System.out.println("external_user==========>>>" + responseString);
                        try {
                            JSONArray jsonArray = new JSONArray(responseString);
                            user_type = jsonArray.getJSONObject(0).getInt("id");
                            getExternalUserList();
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


    private void getExternalUserList() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_external_user_list("Token "
                + LoginShared.getLoginDataModel(CrusherDetailsActivity.this).getToken(), "application/json", user_type);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        try {
                            arrayList_external_user_list.clear();
                            String responseString = response.body().string();
                            System.out.println("external_user_list==========>>>" + responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONArray result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                arrayList_external_user_list.add(result.getJSONObject(i));
                            }
                            get_materials_external_user_mapping_list();
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


    private void get_materials_external_user_mapping_list() {
        loader.show_with_label("Please wait");

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_get_materials_external_user_mapping_list("Token "
                        + LoginShared.getLoginDataModel(CrusherDetailsActivity.this).getToken(), "application/json", MethodUtils.tender_id,
                user_type, Integer.valueOf(tender_survey_material));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        try {
                            arrayList.clear();
                            String responseString = response.body().string();
                            System.out.println("get_materials_external_user_mapping_list==========>>>" + responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONArray result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                arrayList.add(result.getJSONObject(i));
                            }
                            assignedCrusherListAdapter.notifyDataSetChanged();
                            if (is_check.equalsIgnoreCase("1")) {
                                nsv_assign_crusher.fullScroll(View.FOCUS_DOWN);
                                is_check = "";
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
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });
    }


    private void add_external_user_mapping_add() {
        try {
            loader.show_with_label("Please wait");

            JsonObject object = new JsonObject();
            object.addProperty("tender", String.valueOf(MethodUtils.tender_id));
            object.addProperty("external_user_type", user_type);
            object.addProperty("tender_survey_material", tender_survey_material);
            JsonObject mapping_details_obj = new JsonObject();
            mapping_details_obj.addProperty("external_user", external_user);
            mapping_details_obj.addProperty("latitude", String.valueOf(currentLat));
            mapping_details_obj.addProperty("longitude", String.valueOf(currentLng));
            mapping_details_obj.addProperty("address", addresses.get(0).getAddressLine(0));
            object.add("mapping_details", mapping_details_obj);
            System.out.println("object======>>>>" + object.toString());


            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_materials_external_user_mapping_add("Token " +
                    LoginShared.getLoginDataModel(CrusherDetailsActivity.this).getToken(), "application/json", object);


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
                                external_user_mapping = jsonObject.getJSONObject("mapping_details").getString("id");
                                if (arrayList_added_document_list.size() > 0) {
                                    UploadDocuments();
                                } else {
                                    tv_contact_person.setText("");
                                    tv_contact_person_no.setText("");
                                    external_user = "";
                                    get_materials_external_user_mapping_list();
                                    arrayList_added_document_list.clear();
                                    array_add_documents = new JSONArray();
                                    crusherAddedDocumentListAdapter.notifyDataSetChanged();
                                    Toast.makeText(CrusherDetailsActivity.this, "Crusher Assigned successfully.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(CrusherDetailsActivity.this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void UploadDocuments() {

        try {
            loader.show_with_label("Please wait");

            System.out.println("=================================");
            RequestBody requestFile = null;
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(arrayList_added_document_list.get(count).getString("file_path")));
            RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), arrayList_added_document_list.get(count).getString("file_name"));
            MultipartBody.Part pdf = MultipartBody.Part.createFormData("document",
                    new File(arrayList_added_document_list.get(count).getString("file_path")).getName(), requestFile);
            RequestBody external_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), external_user);
            RequestBody external_user_mapping_id = RequestBody.create(MediaType.parse("multipart/form-data"), external_user_mapping);


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            //creating retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getUnsafeOkHttpClient())
                    .build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_add_external_user_mapping_document(pdf, external_user_id, external_user_mapping_id, document_name);

            register.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();
                    try {
                        if (response.code() == 200 || response.code() == 201) {
                            String responseString = response.body().string();
                            Log.d("responseString", responseString);
                            System.out.println("respons_save_data===========>>>" + responseString);
                            if (is_check.equalsIgnoreCase("")) {
                                count--;
                                is_check = "1";
                            }
                            if (count != -1) {
                                UploadDocuments();
                                System.out.println("count===========>>>" + count);
                                System.out.println("file_path===========>>>" + arrayList_added_document_list.get(count).getString("file_path"));
                                count--;
                            } else {
                                tv_contact_person.setText("");
                                tv_contact_person_no.setText("");
                                external_user = "";
                                get_materials_external_user_mapping_list();
                                arrayList_added_document_list.clear();
                                array_add_documents = new JSONArray();
                                crusherAddedDocumentListAdapter.notifyDataSetChanged();
                                Toast.makeText(CrusherDetailsActivity.this, "Crusher Assigned successfully.", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                loader.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void AddMoreDocuments(final File file, String file_name) {
            loader.show_with_label("Please wait");

            System.out.println("=================================");
            RequestBody requestFile = null;
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part pdf = MultipartBody.Part.createFormData("document", file.getName(), requestFile);
            RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), file_name);
            RequestBody external_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), external_user_from_list);
            RequestBody external_user_mapping_id = RequestBody.create(MediaType.parse("multipart/form-data"), external_user_mapping_from_list);


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            //creating retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getUnsafeOkHttpClient())
                    .build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_add_external_user_mapping_document(pdf, external_user_id, external_user_mapping_id, document_name);

            register.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();
                    try {
                        if (response.code() == 200 || response.code() == 201) {
                            String responseString = response.body().string();
                            Log.d("responseString", responseString);
                            System.out.println("respons_save_data===========>>>" + responseString);
                            get_materials_external_user_mapping_list();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                loader.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();
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


    public void getCurrentLatLongAddress() {
        gpsTracker = new GPSTracker(CrusherDetailsActivity.this);
        geocoder = new Geocoder(CrusherDetailsActivity.this, Locale.getDefault());
        location = new SimpleLocation(this, false, false, 10000);
        if (!location.hasLocationEnabled()) {
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
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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


    private void AddMoreFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), ADD_MORE_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
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
    public void OnItemClick(int position) {
        try {
            System.out.println("one object====>>" + arrayList.get(position).toString());
            external_user_from_list = arrayList.get(position).getString("external_user");
            external_user_mapping_from_list = arrayList.get(position).getString("id");
            AddMoreFile();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
