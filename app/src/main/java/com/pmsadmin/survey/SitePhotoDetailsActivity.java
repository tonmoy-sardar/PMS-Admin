package com.pmsadmin.survey;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;

import com.pmsadmin.dialog.CameraGalleryDialogueSitePhotosDetails;
import com.pmsadmin.interfaces.OnImageSet;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.MediaUtils;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import id.zelory.compressor.Compressor;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.M;
import static com.pmsadmin.apilist.ApiList.BASE_URL;

public class SitePhotoDetailsActivity extends BaseActivity {

    String document = "";
    String additional_info = "";
    int site_photo_id = 0;
    private EditText tvAdditionalInfo;
    private ImageView ivDocument;
    public View view;
    private LoadingData loader;
    private TextView tv_universal_header,tvEdit,tv_submit;
    public String filePath = "";
    private OnImageSet onImageSet;
    public static final int CAMERA = 1, GALLERY = 2;
    private File mFile = null;
    public static final String PICTURE_NAME = "PMSSurveyPicture";
    public static final String FOLDER_NAME = "PMSSurevey";
    private Uri fileUri = null;
    public static String a_token;
    Geocoder geocoder;
    private double currentLat;
    private double currentLng;
    public GPSTracker gpsTracker;
    private SimpleLocation location;
    ImageView iv_icon_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_site_photo_details, null);
        addContentView(view);
        loader = new LoadingData(SitePhotoDetailsActivity.this);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tvEdit = findViewById(R.id.tvEdit);
        tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);
        tv_submit = findViewById(R.id.tv_submit);
        ivDocument = findViewById(R.id.ivDocument);
        iv_icon_edit = findViewById(R.id.iv_icon_edit);

        tv_universal_header.setText("Photo Details");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(SitePhotoDetailsActivity.this));
        tvAdditionalInfo.setEnabled(false);
        ivDocument.setEnabled(false);

        Intent intent = getIntent();
        document = intent.getStringExtra("document");
        additional_info = intent.getStringExtra("additional_info");
        site_photo_id = (int) intent.getIntExtra("id",0);

        a_token = LoginShared.getLoginDataModel(this).getToken();
        gpsTracker = new GPSTracker(SitePhotoDetailsActivity.this);
        geocoder = new Geocoder(SitePhotoDetailsActivity.this, Locale.getDefault());
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

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("String.valueOf(mFile)==========>>>>"+String.valueOf(mFile));
                if (mFile == null){
                    UploadData();
                } else {
                    UploadImageData(String.valueOf(mFile));
                }
                //UploadImageData(filePath);
            }
        });



        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAdditionalInfo.setEnabled(true);
                ivDocument.setEnabled(true);
                tvEdit.setVisibility(View.GONE);
                tv_submit.setVisibility(View.VISIBLE);
                iv_icon_edit.setVisibility(View.VISIBLE);
            }
        });


        ivDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CameraGalleryDialogueSitePhotosDetails(SitePhotoDetailsActivity.this, new OnImageSet() {
                    @Override
                    public void onSuccess(String path) {
                        filePath = path;

                        System.out.println("filePath: " + filePath);
                    }
                }, "0").show();
            }
        });


        setValue();

    }


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }



    public void choiceMedia(final int currentChoice, OnImageSet onImageSet) {
        this.onImageSet = onImageSet;
        final TedPermission tedPermission = new TedPermission(SitePhotoDetailsActivity.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        switch (currentChoice) {
                            case CAMERA:
                                openCamera();
                                break;
                            case GALLERY:
                                openGallery();
                                break;
                        }
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                }).setDeniedMessage(getResources().getString(R.string.permission_not_given));
        switch (currentChoice) {
            case CAMERA:
                tedPermission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA);
                break;
            case GALLERY:
                tedPermission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }

        tedPermission.check();
    }


    private void openGallery() {

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }


    private void openCamera() {
        generateFile();
        captureImage();
    }


    public Uri getOutputMediaFileUri() {
        Uri photoURI;

        if (Build.VERSION.SDK_INT > M) {
            photoURI = FileProvider.getUriForFile(SitePhotoDetailsActivity.this, getApplicationContext().getPackageName() +
                    ".provider", mFile);

        } else {
            photoURI = Uri.fromFile(mFile);
        }
        return photoURI;
    }



    private void captureImage() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA);
    }

    private void generateFile() {

        String fileName = PICTURE_NAME + new SimpleDateFormat("mm_dd_yyyy_HH_mm_ss").format(new Date());
        mFile = MediaUtils.getOutputMediaFile(SitePhotoDetailsActivity.this, MediaUtils.MEDIA_TYPE_IMAGE, FOLDER_NAME, fileName);

        System.out.println("mFile---"+String.valueOf(mFile));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GALLERY:

                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    // display it in image view

                    try {
                        mFile = FileUtil.from(this, data.getData());

                        System.out.println("mfile: "+String.valueOf(mFile));

                        compressImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_capture_cancel_text), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_to_capture_image_text), Toast.LENGTH_SHORT)
                            .show();
                }

                break;


            case CAMERA:

                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    // display it in image view
                    compressImage();
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_capture_cancel_text), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_to_capture_image_text), Toast.LENGTH_SHORT)
                            .show();
                }

                break;


            default:
                break;

        }
    }


    public File mCompressedFile = null;

    private void compressImage() {
        Compressor.getDefault(this)
                .compressToFileAsObservable(mFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        mCompressedFile = file;
                        previewCapturedImage();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SitePhotoDetailsActivity.this, getResources().getString(R.string.image_compression_error_text), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void previewCapturedImage() {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;

            final Bitmap bitmap = BitmapFactory.decodeFile(mCompressedFile.getPath(), options);

            ivDocument.setImageBitmap(bitmap);
            if (onImageSet != null)
                onImageSet.onSuccess(mCompressedFile.getPath());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void UploadImageData(final String imagePath) {
        // loader.show_with_label("Loading");
//        loader.show();
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("document", file.getName(),requestFile);
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(currentLat));
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(currentLng));
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), addresses.get(0).getAddressLine(0));
        RequestBody additional_notes = RequestBody.create(MediaType.parse("multipart/form-data"), tvAdditionalInfo.getText().toString());
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), "Site Photo");

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

        final Call<ResponseBody> register = apiInterface.call_site_photos_edit(site_photo_id,image,latitude,longitude,address,additional_notes,document_name);

        register.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (loader != null && loader.isShowing())
//                    loader.dismiss();
                try{

                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    if (response.code() == 200 || response.code()==201) {
                        System.out.println("Service URL==>" + response.raw().request().url());
                        String responseString = response.body().string();
                        System.out.println("respons_save_data===========>>>"+responseString);
                        Toast.makeText(SitePhotoDetailsActivity.this,"Data uploaded sucessfully",Toast.LENGTH_SHORT).show();

                        tvAdditionalInfo.setEnabled(false);
                        ivDocument.setEnabled(false);
                        tvEdit.setVisibility(View.VISIBLE);
                        tv_submit.setVisibility(View.GONE);
                        iv_icon_edit.setVisibility(View.GONE);
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


    private void UploadData() {
        try {
            loader.show_with_label("Please wait");

            JsonObject object = new JsonObject();
            object.addProperty("additional_notes", tvAdditionalInfo.getText().toString());
            System.out.println("object======>>>>"+object.toString());


            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_put_site_photos_edit("Token " +a_token,
                    site_photo_id, object);


            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();

                    if (response.code() == 201 || response.code()==200) {
                        System.out.println("Service URL==>" + response.raw().request().url());
                        try {
                            String responseString = response.body().string();
                            System.out.println("response output==========>>>"+responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            Toast.makeText(SitePhotoDetailsActivity.this,"Data uploaded sucessfully",Toast.LENGTH_SHORT).show();

                            tvAdditionalInfo.setEnabled(false);
                            ivDocument.setEnabled(false);
                            tvEdit.setVisibility(View.VISIBLE);
                            tv_submit.setVisibility(View.GONE);
                            iv_icon_edit.setVisibility(View.GONE);
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



    private void setValue() {
        tvAdditionalInfo.setText(additional_info);


        String url = document;

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(SitePhotoDetailsActivity.this).load(url).apply(options).into(ivDocument);
    }
}
