package com.pmsadmin.survey;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pmsadmin.R;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.survey.site_photo_pojo.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SitePhotoAdapter extends RecyclerView.Adapter<SitePhotoAdapter.MyViewHolder> {


    Activity activity;
    List<Result> sitePhotoResultList;


    public SitePhotoAdapter(Activity activity, List<Result> sitePhotoResultList) {

        this.activity = activity;
        this.sitePhotoResultList = sitePhotoResultList;

    }

    @NonNull
    @Override
    public SitePhotoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_start_survey_home, parent, false);

        return new SitePhotoAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull SitePhotoAdapter.MyViewHolder holder, final int position) {


        if (sitePhotoResultList.get(position).getDocument()!= null){
            String url = sitePhotoResultList.get(position).getDocument();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(activity).load(url).apply(options).into(holder.ivTenderItem);
        }


        holder.llStartSurveyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SitePhotoDetailsActivity.class);
                intent.putExtra("document",sitePhotoResultList.get(position).getDocument());
                intent.putExtra("additional_info",sitePhotoResultList.get(position).getAdditionalNotes());
                intent.putExtra("id",sitePhotoResultList.get(position).getId());
                System.out.println("id===========>>>"+sitePhotoResultList.get(position).getId());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sitePhotoResultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivTenderItem;
        LinearLayout llStartSurveyItem;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            ivTenderItem = itemView.findViewById(R.id.ivTenderItem);
            llStartSurveyItem = itemView.findViewById(R.id.llStartSurveyItem);
        }
    }
}
