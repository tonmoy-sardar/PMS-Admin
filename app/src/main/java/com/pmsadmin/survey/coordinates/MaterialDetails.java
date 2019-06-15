package com.pmsadmin.survey.coordinates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.zelory.compressor.FileUtil;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.ExternalMappingUserList;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.Result;
import com.pmsadmin.survey.resource.AddVendroActivity;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;
import com.pmsadmin.survey.resource.adpater.MaterialDetailsAdapter;
import com.pmsadmin.survey.resource.hydrological_data.AddHydrologicalData;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.pmsadmin.apilist.ApiList.BASE_URL;

public class MaterialDetails extends BaseActivity implements MaterialDetailsAdapter.OnItemClickListener {

    private View view;
    private TextView tv_universal_header;
    private LoadingData loader;
    String Tag = "vendor";
    int mat_id = 0;
    String external_user_type = "";
    List<Result> resultList = new ArrayList<>();
    MaterialDetailsAdapter materialDetailsAdapter;

    private RecyclerView rvItems;
    private TextView tvAdd;

    private Integer external_user_type_id = 0;

    private static final int STORAGE_PERMISSION_CODE = 123;
    public static String a_token;

    private int PICK_PDF_REQUEST = 1;
    int external_user_mappingg;
    int external_userr=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_material_details, null);
        addContentView(view);
        //setContentView(R.layout.activity_material_details);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Material Details");
        loader = new LoadingData(MaterialDetails.this);

        rvItems = findViewById(R.id.rvItems);

        tvAdd = findViewById(R.id.tvAdd);


        System.out.println("watSize: "+
                LoginShared.getExternalUserType(MaterialDetails.this,"external_user_type").size());


        for (int i = 0;
             i < LoginShared.getExternalUserType(MaterialDetails.this,"external_user_type").size();
             i++){

            /*System.out.println("qqq: "+LoginShared.getExternalUserType(RawMaterialsActivity.this,"external_user_type")
                    .get(i).getTypeName());*/
            if (LoginShared.getExternalUserType(MaterialDetails.this,"external_user_type")
                    .get(i).getTypeName().equalsIgnoreCase(Tag)){

                System.out.println("externalID: "+LoginShared.getExternalUserType(MaterialDetails.this,
                        "external_user_type")
                        .get(i).getId());

                external_user_type = String.valueOf(LoginShared.getExternalUserType(MaterialDetails.this,
                        "external_user_type")
                        .get(i).getId());
                external_user_type_id = LoginShared.getExternalUserType(MaterialDetails.this,
                        "external_user_type").get(i).getId();
            }
        }

        a_token = LoginShared.getLoginDataModel(this).getToken();

        Intent mIntent = getIntent();
        mat_id = mIntent.getIntExtra("mat_id", 0);

        System.out.println("mat_id: "+mat_id+" tenderId: "+ MethodUtils.tender_id);


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("newOOO: "+resultList.size());
                Intent intent = new Intent(MaterialDetails.this, AddVendroActivity.class);
                intent.putExtra("external_user_type_id", external_user_type_id);
                intent.putExtra("mat_id", mat_id);
                intent.putExtra("myList", (Serializable) resultList);

                /*if (resultList.size() >0){


                }*/
                startActivity(intent);
            }
        });


        requestStoragePermission();

    }

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


    @Override
    protected void onResume() {
        super.onResume();

        getExternalMappingUserList();
        setAdapter();
    }

    private void setAdapter() {


        materialDetailsAdapter = new MaterialDetailsAdapter(MaterialDetails.this, resultList);
        materialDetailsAdapter.setOnItemClickListener(this);
        rvItems.setAdapter(materialDetailsAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(MaterialDetails.this, RecyclerView.VERTICAL, false);

        rvItems.setLayoutManager(horizontalLayoutManager);

        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(MaterialDetails.this, 2);
        rvItems.addItemDecoration(itemOffset);


    }

    private void getExternalMappingUserList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_external_mapping_list("Token " +
                        LoginShared.getLoginDataModel(MaterialDetails.this).getToken(), "application/json",
                MethodUtils.tender_id, external_user_type, String.valueOf(mat_id));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {

                    if (resultList!= null){
                        resultList.clear();
                    }
                    try {
                        String responseString = response.body().string();
                        System.out.println("mapping: "+responseString);

                        ExternalMappingUserList externalMappingUserList;
                        Gson gson = new Gson();

                        externalMappingUserList = gson.fromJson(responseString,ExternalMappingUserList.class);
                        resultList.addAll(externalMappingUserList.getResult());

                        System.out.println("resltSize: "+resultList.size());
                        //materialDetailsAdapter.notifyDataSetChanged();

                        if (resultList.size() > 0){
                            materialDetailsAdapter.notifyDataSetChanged();


                        }else {
                            MethodUtils.errorMsg(MaterialDetails.this,"No Data found");
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


    String docname = "";

    @Override
    public void OnItemClick(int position, String str) {


        System.out.println("======> "+ resultList.get(position).getId()+" "+ str);
        docname = str;

        external_user_mappingg = resultList.get(position).getId();
        external_userr = resultList.get(position).getExternalUser();

        if (!docname.equals("")) {

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
        }else {
            MethodUtils.errorMsg(MaterialDetails.this,"Please enter Document name first");
        }
    }


    private String pdfFilePath = "";
    File file;



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
                    UploadDocuments(file);
                }

                String str = String.valueOf(file);

                //UploadDocuments(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void UploadDocuments(File file) {


        System.out.println("=================================");
//        File file = new File(pdf_path);
        /*RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part pdf = MultipartBody.Part.createFormData("document", file.getName(), requestFile);
        RequestBody tender = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(MethodUtils.tender_id));
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), "Arghya Doc");*/

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RequestBody external_user = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(external_userr));
        RequestBody external_user_mapping = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(external_user_mappingg));
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), docname);
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
                        Toast.makeText(MaterialDetails.this, "Data uploaded sucessfully", Toast.LENGTH_SHORT).show();
                        getExternalMappingUserList();

                        pdfFilePath = "";
                        /*ivSelect.setVisibility(View.VISIBLE);
                        ivPdf.setVisibility(View.GONE);*/

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
