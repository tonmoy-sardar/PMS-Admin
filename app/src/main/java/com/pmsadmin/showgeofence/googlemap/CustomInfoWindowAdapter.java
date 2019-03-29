package com.pmsadmin.showgeofence.googlemap;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pmsadmin.designhelper.ResolutionHelper;
import com.pmsadmin.showgeofence.GeoFenceActivity;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    TextView tv_name, tv_percentage;
    de.hdodenhof.circleimageview.CircleImageView profile_image;
    RelativeLayout rl_main;
    private GeoFenceActivity mActivity;
    private View myContentsView;
    private ImageLoader imageLoader;
    ResolutionHelper resolutionHelper;

    public CustomInfoWindowAdapter(GeoFenceActivity activity) {
        mActivity = activity;
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).defaultDisplayImageOptions(opts)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        resolutionHelper=new ResolutionHelper(activity);
    }

    @Override
    public View getInfoWindow(Marker marker) {

        /*if (marker != null) {
            try {
                myContentsView = mActivity.getLayoutInflater().inflate(R.layout.map_custom_info_window, null);
                Profile profile = (Profile) marker.getTag();
                initView();
                responsiveDesign();
                if (profile.getImage().equals("")) {
                    profile_image.setImageResource(R.drawable.placeholder);
                } else {
                    String url = profile.getImage();
                    url = url.replace(" ", "20%");
                    imageLoader.displayImage(url, profile_image);
                    *//*String url = profile.getImage();
                    url = url.replace(" ", "20%");
                    Picasso.with(mActivity).load(url).into(profile_image);*//*
                }

                tv_name.setText(profile.getName());
                tv_percentage.setText(profile.getPercentage() + "%");

                return myContentsView;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return null;
    }

    private void responsiveDesign() {
        /*RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(resolutionHelper.getWidth(0.08),
                resolutionHelper.getWidth(0.08));
        rlParams.setMargins(resolutionHelper.getWidth(0.02),
                resolutionHelper.getHeight(0.01), 0,
                resolutionHelper.getHeight(0.04));
        profile_image.setLayoutParams(rlParams);

        tv_name.setPadding(resolutionHelper.getWidth(0.02),
                resolutionHelper.getHeight(0.02),
                0,0);
        tv_percentage.setPadding(resolutionHelper.getWidth(0.015),
                resolutionHelper.getHeight(0.02),
                0,0);

        tv_name.setTextSize(resolutionHelper.getTextSize(2.4));
        tv_percentage.setTextSize(resolutionHelper.getTextSize(2.4));*/

        //tv_name.setTypeface(resolutionHelper.typefaceBook);
        //tv_percentage.setTypeface(resolutionHelper.typefaceBook);
    }

    private void initView() {
       /* profile_image = myContentsView.findViewById(R.id.profile_image);
        tv_name = myContentsView.findViewById(R.id.tv_name);
        tv_percentage = myContentsView.findViewById(R.id.tv_percentage);
        rl_main = myContentsView.findViewById(R.id.rl_main);*/
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
