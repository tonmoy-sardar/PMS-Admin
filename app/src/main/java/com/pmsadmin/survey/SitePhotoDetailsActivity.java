package com.pmsadmin.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;

public class SitePhotoDetailsActivity extends BaseActivity {

    String document = "";
    String additional_info = "";

    private TextView tvAdditionalInfo;
    private ImageView ivDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_photo_details);

        Intent intent = getIntent();
        document = intent.getStringExtra("document");
        additional_info = intent.getStringExtra("additional_info");

        initLayout();

    }

    private void initLayout() {

        tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);
        ivDocument = findViewById(R.id.ivDocument);

        setValue();
    }

    private void setValue() {
        tvAdditionalInfo.setText(additional_info);


        String url = document;

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(SitePhotoDetailsActivity.this).load(url).apply(options).into(ivDocument);
    }
}
