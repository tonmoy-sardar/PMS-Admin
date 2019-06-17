package com.pmsadmin.survey.coordinates.coordinate_adapter;

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
import com.pmsadmin.survey.SurveyStaticModel;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.survey.coordinates.CheckInActivity;
import com.pmsadmin.survey.coordinates.CoordinatesActivity;
import com.pmsadmin.survey.coordinates.CrusherActivity;
import com.pmsadmin.survey.coordinates.RawMaterialsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoordinateStaticAdapter extends RecyclerView.Adapter<CoordinateStaticAdapter.MyViewHolder> {

    Activity activity;
    List<SurveyStaticModel> itemCoordinates;


    public CoordinateStaticAdapter(Activity activity, List<SurveyStaticModel> itemCoordinates) {

        this.activity = activity;
        this.itemCoordinates = itemCoordinates;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_start_survey_home, parent, false);

        return new CoordinateStaticAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvItem.setText(itemCoordinates.get(position).getItem());
        holder.ivTenderItem.setImageResource(itemCoordinates.get(position).getImageId());

        holder.llStartSurveyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemCoordinates.get(position).getItem().equals("CHECK IN")){

                    Intent intent = new Intent(activity, CheckInActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if (itemCoordinates.get(position).getItem().equals("RAW MATERIALS")){
                    Intent intent = new Intent(activity, RawMaterialsActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if (itemCoordinates.get(position).getItem().equals("CRUSHER")){
                    Intent intent = new Intent(activity, CrusherActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCoordinates.size();
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
