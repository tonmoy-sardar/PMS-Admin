package com.pmsadmin.survey.resource.contractor_vendor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.adpater.ContractorsDocumentListAdapter;
import com.pmsadmin.survey.resource.adpater.PandMDocumentListAdapter;
import com.pmsadmin.survey.resource.contractor_vendor.machinery_pojo.Result;
import com.pmsadmin.survey.resource.dialog_fragment.Dialog_Fragment_add_document_name;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

import static com.pmsadmin.apilist.ApiList.BASE_URL;

public class PandMDetails extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tv_details,tv_add_documents,tv_hire,tv_khoraki;
    RecyclerView rv_p_and_m_document_list;
    PandMDocumentListAdapter pandMDocumentListAdapter;
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static String a_token;
    File file;
    int tender_id=0;
    int id= 0;
    String doc_no="";
    JSONArray jsonArray;
    ArrayList<Result> arrayList_documents;
    ArrayList<JSONObject> arrayList_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_p_and_m_details, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        a_token = LoginShared.getLoginDataModel(this).getToken();
        System.out.println("token=======>>>"+a_token);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_hire = findViewById(R.id.tv_hire);
        tv_khoraki = findViewById(R.id.tv_khoraki);
        tv_details = findViewById(R.id.tv_details);
        rv_p_and_m_document_list = findViewById(R.id.rv_p_and_m_document_list);
        tv_add_documents = findViewById(R.id.tv_add_documents);
        rv_p_and_m_document_list.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        tv_universal_header.setText(intent.getStringExtra("pm_name"));
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(PandMDetails.this));
        tv_details.setText(intent.getStringExtra("pm_make"));
        tv_hire.setText(intent.getStringExtra("pm_hire"));
        tv_khoraki.setText(intent.getStringExtra("pm_khoraki"));
        tender_id = intent.getIntExtra("pm_tender",0);
        id = intent.getIntExtra("pm_id",0);
        try {
            arrayList_obj = new ArrayList<JSONObject>();
            Bundle bundle = getIntent().getExtras();
            arrayList_documents = (ArrayList<Result>) bundle.getSerializable("pm_document_details");
            System.out.println("arrayList_documents================>>>>"+arrayList_documents);
            JSONArray jsonArray = new JSONArray(arrayList_documents);
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList_obj.add(jsonArray.getJSONObject(i));
            }
            System.out.println("arrayList_obj================>>>>"+arrayList_obj.toString());
            doc_no = "DOC "+ (arrayList_obj.size()+1);
            System.out.println("doc_no================>>>>"+doc_no);
        } catch (Exception e) {
            e.printStackTrace();
        }


        tv_add_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        if (!ConnectionDetector.isConnectingToInternet(PandMDetails.this)) {

        }else {

        }


        pandMDocumentListAdapter = new PandMDocumentListAdapter(PandMDetails.this, arrayList_obj);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,4);
        rv_p_and_m_document_list.setLayoutManager(layoutManager);
        rv_p_and_m_document_list.setHasFixedSize(true);
        rv_p_and_m_document_list.setAdapter(pandMDocumentListAdapter);

        requestStoragePermission();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                file = FileUtil.from(this, data.getData());
                System.out.println("filePath: "+file.getName());
                final Dialog_Fragment_add_document_name dialog_Fragment_add_document_name= new Dialog_Fragment_add_document_name();
                dialog_Fragment_add_document_name.setOnDialogListener(new Dialog_Fragment_add_document_name.OnItemClickDialog() {
                    @Override
                    public void onItemClick(String document_name) {
                        UploadDocuments(file,document_name);
                        dialog_Fragment_add_document_name.dismiss();
                    }
                });
                dialog_Fragment_add_document_name.show(getSupportFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


    /*private void showFileChooser() {
        Intent intent = new Intent();
        //intent.setType("application/pdf");
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }*/

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
    protected void onResume() {
        super.onResume();

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


    public void UploadDocuments(final File file,String doc_name) {
        // loader.show_with_label("Loading");
//        loader.show();
        System.out.println("=================================");
//        File file = new File(pdf_path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part pdf = MultipartBody.Part.createFormData("document", file.getName(),requestFile);
        RequestBody tender = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(tender_id));
        RequestBody module_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), doc_name);

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

        final Call<ResponseBody> register = apiInterface.call_add_p_and_m_type_document(pdf,tender,module_id,document_name);

        register.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Service URL==>" + response.raw().request().url());
//                if (loader != null && loader.isShowing())
//                    loader.dismiss();
//                System.out.println("///////////////////////");
                try{
                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    if (response.code() == 200 || response.code()==201) {
                        String responseString = response.body().string();
                        Log.d("responseString",responseString);
                        System.out.println("respons_save_data===========>>>"+responseString);
                        Toast.makeText(PandMDetails.this,"Document Added sucessfully",Toast.LENGTH_SHORT).show();
                        arrayList_obj.add(new JSONObject(responseString));
                        pandMDocumentListAdapter.notifyDataSetChanged();
                        doc_no = "DOC "+ (arrayList_obj.size()+1);
                        System.out.println("doc_no================>>>>"+doc_no);
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

}
