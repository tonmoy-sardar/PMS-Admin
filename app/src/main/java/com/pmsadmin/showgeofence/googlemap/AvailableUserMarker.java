package com.pmsadmin.showgeofence.googlemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pmsadmin.R;
import com.pmsadmin.application.MyApplication;
import com.pmsadmin.attendancelist.markergetmodel.Result;
import com.pmsadmin.attendancelist.reportlistmodel.LogDetail;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.showgeofence.GeoFenceActivity;

import java.util.List;


public class AvailableUserMarker {

    GPSTracker gpsTracker;
    String lat;
    String lng;
    private GeoFenceActivity activity;
    private ImageLoader imageLoader;
    private int counterMarker = -1;
    private int counterParentMarker = -1;
    private int counterShowData = -1;
    private View mCustomMarkerView;
    private de.hdodenhof.circleimageview.CircleImageView profile_image;
    public List<Result> list;

    public AvailableUserMarker(GeoFenceActivity activity, String lat, String lng) {
        this.activity = activity;
        this.lat = lat;
        this.lng = lng;
        this.list= LoginShared.getMarkerListDataModel(activity).getResults();
        gpsTracker = new GPSTracker(activity);
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).defaultDisplayImageOptions(opts)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        mCustomMarkerView = ((LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.custom_marker, null);

        initView(mCustomMarkerView);


        callAvailableUserData();
    }

    private void initView(View mCustomMarkerView) {
        //profile_image = mCustomMarkerView.findViewById(R.id.profile_image);
    }

    private void callAvailableUserData() {
        plotAvailableUser();
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        profile_image.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;

    }

    private void plotAvailableUser() {
        markerUpdate();
    }

    private void markerUpdate() {
        counterMarker++;
        if(list!=null) {
            if (counterMarker < list.size()) {
                new LoadMarker().execute("");
            }
        }

    }

    public Bitmap resizeMapIcons(Bitmap bitmap, int width, int height) {
        int cornerRadius = 1000;
        Bitmap mBitmap = getRoundedBitmap(bitmap, cornerRadius);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, false);
        return resizedBitmap;
    }

    // Custom method to create rounded bitmap from a rectangular bitmap
    protected Bitmap getRoundedBitmap(Bitmap srcBitmap, int cornerRadius) {
        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth(), // Width
                srcBitmap.getHeight(), // Height

                Bitmap.Config.ARGB_8888 // Config
        );
        // Initialize a new Canvas to draw rounded bitmap
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // Initialize a new Rect instance
        Rect rect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        // Initialize a new RectF instance
        RectF rectF = new RectF(rect);
        // Draw a rounded rectangle object on canvas
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Make a rounded image by copying at the exact center position of source image
        canvas.drawBitmap(srcBitmap, 0, 0, paint);

        // Free the native object associated with this bitmap.
        srcBitmap.recycle();

        // Return the circular bitmap
        return dstBitmap;
    }

    public class LoadMarker extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // Get bitmap from server
            Bitmap overlay;
            /*try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                overlay = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }*/
            return null;
        }

        protected void onPostExecute(Bitmap bitmap) {

                LatLng latLng = new LatLng(Double.parseDouble(list.get(counterMarker).getLatitude()),
                        Double.parseDouble(list.get(counterMarker).getLongitude()));

                Marker marker = MyApplication.getInstance().googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setTag(list.get(counterMarker));

            try {

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (counterMarker < list.size()) {
                    markerUpdate();
                } else {
                    System.out.print("nothing");
                }
            }
                /*if (counterMarker < LoginShared.getReportListDataModel(activity).getResults().get(0).getLogDetails().size()) {
                    markerUpdate();
                } else {
                    System.out.print("nothing");
                }*/
        }
    }
}
