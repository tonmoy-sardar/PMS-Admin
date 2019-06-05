package com.pmsadmin.survey;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pmsadmin.dialog.CameraGalleryDialogueSitePhotos;
import com.pmsadmin.dialog.OpenCameraOrGalleryDialog;
import com.pmsadmin.forgot.ForgotPasswordActivity;
import com.pmsadmin.interfaces.OnImageSet;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.CheckInActivity;
import com.pmsadmin.survey.site_photo_pojo.Result;
import com.pmsadmin.survey.site_photo_pojo.SitePhotoPojo;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.MediaUtils;
import com.pmsadmin.utils.SpacesItemDecoration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.os.Build.VERSION_CODES.M;
import static com.pmsadmin.apilist.ApiList.BASE_URL;

public class SitePhotoSurvey extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tvSave;

    private EditText etAdditionalInformation;

    private OnImageSet onImageSet;

    private de.hdodenhof.circleimageview.CircleImageView iv_profile;
    public static final int CAMERA = 1, GALLERY = 2;
    private static  int CAMERA_REQUEST = 133;

    private File mFile = null;

    public String filePath = "";

    public static final String PICTURE_NAME = "PMSSurveyPicture";
    public static final String FOLDER_NAME = "PMSSurevey";

    private Uri fileUri = null;

    public static String a_token;

    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;
    public GPSTracker gpsTracker;

    private SimpleLocation location;

    List<Result> sitePhotoResultList = new ArrayList<>();

    SitePhotoAdapter sitePhotoAdapter;

    private RecyclerView rvSiteImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_site_photo_survey, null);
        addContentView(view);


//        a_token = LoginShared.getLoginToken(this);
        a_token = LoginShared.getLoginDataModel(this).getToken();

        gpsTracker = new GPSTracker(SitePhotoSurvey.this);
        geocoder = new Geocoder(SitePhotoSurvey.this, Locale.getDefault());

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tvSave = findViewById(R.id.tvSave);
        tv_universal_header.setText("Site Photos");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(SitePhotoSurvey.this));

        etAdditionalInformation = findViewById(R.id.etAdditionalInformation);

        rvSiteImages = findViewById(R.id.rvSiteImages);


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



        bindView();
        //setContentView(R.layout.activity_site_photo_survey);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("jkajk",filePath);
                Log.d("jkajk",String.valueOf(mFile));
               UploadImageData(String.valueOf(mFile));
               //UploadImageData(filePath);
            }
        });




        /*getSitePhotoList();


        setAdapter();*/

    }






    private void setAdapter() {

        sitePhotoAdapter = new SitePhotoAdapter(SitePhotoSurvey.this,sitePhotoResultList);

        rvSiteImages.setAdapter(sitePhotoAdapter);
        rvSiteImages.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvSiteImages.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rvSiteImages.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(SitePhotoSurvey.this, 2);
        rvSiteImages.addItemDecoration(itemOffset);
    }

    private void getSitePhotoList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_get_site_photo_list("Token "
                        + LoginShared.getLoginDataModel(SitePhotoSurvey.this).getToken(),
                String.valueOf(MethodUtils.tender_id));




        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {

                    if (sitePhotoResultList!= null){
                        sitePhotoResultList.clear();
                    }

                    try {
                        String responseString = response.body().string();
                        Gson gson = new Gson();

                        SitePhotoPojo sitePhotoPojo;
                        sitePhotoPojo = gson.fromJson(responseString, SitePhotoPojo.class);

                        sitePhotoResultList.addAll(sitePhotoPojo.getResult());
                        sitePhotoAdapter.notifyDataSetChanged();

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


        getSitePhotoList();
        setAdapter();

    }




    private void bindView() {

        iv_profile = findViewById(R.id.iv_profile);

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CameraGalleryDialogueSitePhotos(SitePhotoSurvey.this, new OnImageSet() {
                    @Override
                    public void onSuccess(String path) {
                        filePath = path;

                        System.out.println("filePath: " + filePath);
                    }
                }, "0").show();

                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


            }
        });

    }

    public void choiceMedia(final int currentChoice, OnImageSet onImageSet) {

        this.onImageSet = onImageSet;
        final TedPermission tedPermission = new TedPermission(SitePhotoSurvey.this)
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
            photoURI = FileProvider.getUriForFile(SitePhotoSurvey.this, getApplicationContext().getPackageName() +
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
        mFile = MediaUtils.getOutputMediaFile(SitePhotoSurvey.this, MediaUtils.MEDIA_TYPE_IMAGE, FOLDER_NAME, fileName);

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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv_profile.setVisibility(View.VISIBLE); //imageview name
            iv_profile.setImageBitmap(photo);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + ".pdcimage");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder.getAbsolutePath(),System.currentTimeMillis()+".png");

            FileOutputStream fileOutputStream;
            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            filePath = file.getAbsolutePath();
            Log.d("imagepath","::::::::::"+filePath);

        }
    }*/





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
                        Toast.makeText(SitePhotoSurvey.this, getResources().getString(R.string.image_compression_error_text), Toast.LENGTH_SHORT).show();
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

            iv_profile.setImageBitmap(bitmap);
            if (onImageSet != null)
                onImageSet.onSuccess(mCompressedFile.getPath());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }



    public void UploadImageData(final String imagePath)
    {

        // loader.show_with_label("Loading");
//        loader.show();

        File file = new File(imagePath);

//        JsonObject object = new JsonObject();
//        object.addProperty("tender", "1");
//        object.addProperty("latitude", "23.34");
//        object.addProperty("longitude", "45.56");
//        object.addProperty("address", "kolkata");
//        object.addProperty("additional_notes", "test");
//        object.addProperty("document_name", "Test Data");

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("document", file.getName(),requestFile);

        RequestBody tender = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(MethodUtils.tender_id));
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(currentLat));
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(currentLng));
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), addresses.get(0).getAddressLine(0));
        RequestBody additional_notes = RequestBody.create(MediaType.parse("multipart/form-data"), etAdditionalInformation.getText().toString());
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), "Site Photo");

       /* RequestBody tender = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), "23.34");
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), "45.56");
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), "kolkata");
        RequestBody additional_notes = RequestBody.create(MediaType.parse("multipart/form-data"), "test");
        RequestBody document_name = RequestBody.create(MediaType.parse("multipart/form-data"), "Test Data");*/

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

        final Call<ResponseBody> register = apiInterface.call_add_site_photos(image,tender,latitude,longitude,address,additional_notes,document_name);

        register.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (loader != null && loader.isShowing())
//                    loader.dismiss();
                try{

                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    if (response.code() == 200 || response.code()==201)
                    {

                        String responseString = response.body().string();
                        Log.d("responssavedata",responseString);
                        Toast.makeText(getApplicationContext(),"Data uploaded sucessfully",Toast.LENGTH_SHORT).show();
                        finish();
//                        clearvalue();




                    }
                }
                catch (Exception e)
                {
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
