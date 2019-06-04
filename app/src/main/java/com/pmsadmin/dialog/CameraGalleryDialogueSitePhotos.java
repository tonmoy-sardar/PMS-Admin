package com.pmsadmin.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.forgot.ForgotPasswordActivity;
import com.pmsadmin.interfaces.OnImageSet;
import com.pmsadmin.survey.SitePhotoSurvey;

import androidx.appcompat.app.AppCompatDialog;

public class CameraGalleryDialogueSitePhotos extends AppCompatDialog {

    private Activity mActivity;
    private OnImageSet onImageSet;
    String choose;

    private TextView tv_header, tv_openCamera, tv_openGallery, tvCancel;

    public CameraGalleryDialogueSitePhotos(Activity mActivity, OnImageSet onImageSet, String choose) {
        super(mActivity);
        this.mActivity = mActivity;
        this.onImageSet = onImageSet;
        this.choose = choose;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = mActivity.getLayoutInflater().inflate(R.layout.open_camera_open_gallery_dialog, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.height = ViewGroup.LayoutParams.MATCH_PARENT;

        wmlp.gravity = Gravity.BOTTOM;

        wmlp.windowAnimations = R.style.DialogAnimation;

        initView(view);

        //clickEvent();
    }


    private void initView(View view) {
        tv_header = view.findViewById(R.id.tv_header);
        tv_openCamera = view.findViewById(R.id.tv_openCamera);
        tv_openGallery = view.findViewById(R.id.tv_openGallery);
        tvCancel = view.findViewById(R.id.tvCancel);


        tv_openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (choose.equals("0")) {
                    ((SitePhotoSurvey) mActivity).choiceMedia(ForgotPasswordActivity.GALLERY, onImageSet);
                }else{
                    //((ProfileActivity) mActivity).choiceMedia(RegistrationActivity.GALLERY, onImageSet);
                }
                dismiss();
            }
        });


        tv_openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (choose.equals("0")) {
                    ((SitePhotoSurvey) mActivity).choiceMedia(ForgotPasswordActivity.CAMERA, onImageSet);
                }else{
                    //((ProfileActivity) mActivity).choiceMedia(RegistrationActivity.CAMERA, onImageSet);
                }
                dismiss();
            }
        });
    }
}
