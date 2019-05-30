package com.pmsadmin.survey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.survey.SitePhotoSurvey;
import com.pmsadmin.survey.StartSurveyHome;
import com.pmsadmin.survey.SurveyStaticModel;
import com.pmsadmin.survey.coordinates.CoordinatesActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StartSurveyStaticAdapter extends RecyclerView.Adapter<StartSurveyStaticAdapter.MyViewHolder> {

    private Activity activity;
    private List<SurveyStaticModel> itemsSurvey;

    public StartSurveyStaticAdapter(Activity activity, List<SurveyStaticModel> itemsSurvey) {

        this.activity = activity;
        this.itemsSurvey = itemsSurvey;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_start_survey_home, parent, false);

        return new StartSurveyStaticAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvItem.setText(itemsSurvey.get(position).getItem());
        holder.ivTenderItem.setImageResource(itemsSurvey.get(position).getImageId());


        holder.llStartSurveyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemsSurvey.get(position).getItem().equals("SITE PHOTOS")) {
                    Intent intent = new Intent(activity, SitePhotoSurvey.class);
                    activity.startActivity(intent);
                    //intent.putExtra()
                }else if (itemsSurvey.get(position).getItem().equals("COORDINATES")){
                    Intent intent = new Intent(activity, CoordinatesActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsSurvey.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        ImageView ivTenderItem;
        LinearLayout llStartSurveyItem;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            ivTenderItem = (ImageView) itemView.findViewById(R.id.ivTenderItem);
            tvItem.setTypeface(MethodUtils.getNormalFont(activity));
            llStartSurveyItem = (LinearLayout) itemView.findViewById(R.id.llStartSurveyItem);
        }
    }
}
