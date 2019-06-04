package com.pmsadmin.forgot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dialog.ErrorMessageDialog;
import com.pmsadmin.dialog.OpenCameraOrGalleryDialog;
import com.pmsadmin.interfaces.OnImageSet;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.GeneralToApp;
import com.pmsadmin.utils.MediaUtils;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.M;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PICTURE_NAME = "PMSAdminPicture";
    public static final String FOLDER_NAME = "PMSAdmin";
    private File mFile = null;
    private Uri fileUri = null;
    public File mCompressedFile = null;
    private OnImageSet onImageSet;
    public static final int CAMERA = 1, GALLERY = 2;
    ImageView iv_edit;
    private String filePath = "";
    de.hdodenhof.circleimageview.CircleImageView iv_profile;
    Button btn_save;
    EditText et_old, et_new, et_confirm;
    private LoadingData loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        MethodUtils.setStickyBar(ForgotPasswordActivity.this);
        loader = new LoadingData(ForgotPasswordActivity.this);
        viewBind();
        clickEvent();
        setFont();
    }

    private void setFont() {
        et_old.setTypeface(MethodUtils.getNormalFont(ForgotPasswordActivity.this));
        et_new.setTypeface(MethodUtils.getNormalFont(ForgotPasswordActivity.this));
        et_confirm.setTypeface(MethodUtils.getNormalFont(ForgotPasswordActivity.this));
        btn_save.setTypeface(MethodUtils.getBoldFont(ForgotPasswordActivity.this));
    }

    private void clickEvent() {
        iv_edit.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    private void viewBind() {
        iv_edit = findViewById(R.id.iv_edit);
        iv_profile = findViewById(R.id.iv_profile);
        btn_save = findViewById(R.id.btn_save);
        et_old = findViewById(R.id.et_old);
        et_new = findViewById(R.id.et_new);
        et_confirm = findViewById(R.id.et_confirm);
    }

    public void choiceMedia(final int currentChoice, OnImageSet onImageSet) {
        this.onImageSet = onImageSet;
        TedPermission permission = new TedPermission(this)
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
                        Toast.makeText(ForgotPasswordActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                })
                .setDeniedMessage(getResources().getString(R.string.permission_not_given));
        switch (currentChoice) {
            case CAMERA:
                permission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA);
                break;
            case GALLERY:
                permission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }

        permission.check();
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

    private void generateFile() {
        String fileName = PICTURE_NAME + new SimpleDateFormat("mm_dd_yyyy_HH_mm_ss").format(new Date());
        mFile = MediaUtils.getOutputMediaFile(ForgotPasswordActivity.this, MediaUtils.MEDIA_TYPE_IMAGE, FOLDER_NAME, fileName);

        System.out.println("mFile---"+String.valueOf(mFile));
    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri() {
        Uri photoURI;

        if (Build.VERSION.SDK_INT > M) {
            photoURI = FileProvider.getUriForFile(ForgotPasswordActivity.this, getApplicationContext().getPackageName() +
                    ".provider", mFile);

        } else {
            photoURI = Uri.fromFile(mFile);
        }
        return photoURI;
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        switch (requestCode) {
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

            case GALLERY:

                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    // display it in image view

                    try {
                        mFile = FileUtil.from(this, data.getData());
                        System.out.println("gallerYFilie: "+String.valueOf(data.getData()));
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

            default:
                break;

        }

    }

    /**
     * compressing the image
     */
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
                        Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.image_compression_error_text), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Display image from a path to ImageView
     */
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                new OpenCameraOrGalleryDialog(ForgotPasswordActivity.this, new OnImageSet() {
                    @Override
                    public void onSuccess(String path) {
                        filePath = path;
                    }
                }, "0").show();
                break;
            case R.id.btn_save:
                validateFields();
                break;
        }
    }

    private void validateFields() {
        if (et_old.getText().toString().trim().equals("")) {
            errorMsg(ForgotPasswordActivity.this, "Please enter your old password");
        } else if (et_new.getText().toString().trim().equals("")) {
            errorMsg(ForgotPasswordActivity.this, "Please enter your new password");
        } else if (et_confirm.getText().toString().trim().equals("")) {
            errorMsg(ForgotPasswordActivity.this, "Please enter your confirm password");
        } else if (!et_new.getText().toString().trim().equals(et_confirm.getText().toString().trim())) {
            errorMsg(ForgotPasswordActivity.this, "Confirm password should be same with new password");
        } else {
           callChangePasswordApi();
        }
    }

    private void callChangePasswordApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("old_password",et_old.getText().toString().trim());
        object.addProperty("new_password",et_new.getText().toString().trim());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_changePasswordApi("Token "
                        + LoginShared.getLoginToken(ForgotPasswordActivity.this),
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                try {
                    if (response.code() == 200) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            et_confirm.setText("");
                            et_new.setText("");
                            et_old.setText("");
                            MethodUtils.errorMsg(ForgotPasswordActivity.this, jsonObject.optString("msg"));
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToDashboard();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(ForgotPasswordActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(ForgotPasswordActivity.this, getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        et_confirm.setText("");
                        et_new.setText("");
                        et_old.setText("");
                        MethodUtils.errorMsg(ForgotPasswordActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    et_confirm.setText("");
                    et_new.setText("");
                    et_old.setText("");
                    MethodUtils.errorMsg(ForgotPasswordActivity.this, getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                et_confirm.setText("");
                et_new.setText("");
                et_old.setText("");
                MethodUtils.errorMsg(ForgotPasswordActivity.this, getString(R.string.error_occurred));
            }
        });
    }

    private void navigateToDashboard() {
        Intent profileIntent = new Intent(ForgotPasswordActivity.this, DashBoardActivity.class);
        startActivity(profileIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void errorMsg(Context context, String msg) {
        ErrorMessageDialog.getInstant(context).show(msg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
