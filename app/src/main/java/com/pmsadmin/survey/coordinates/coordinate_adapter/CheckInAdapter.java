package com.pmsadmin.survey.coordinates.coordinate_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.CheckInActivity;
import com.pmsadmin.survey.coordinates.RawMaterialsActivity;
import com.pmsadmin.survey.coordinates.survey_location_model.Result;
import com.pmsadmin.tenders_list.TendorsAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.MyViewHolder> {

    private Activity activity;
    private List<Result> surveyResultResponse;

    public CheckInAdapter(Activity activity, List<Result> surveyResultResponse) {

        this.activity = activity;
        this.surveyResultResponse = surveyResultResponse;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_survey_items, parent, false);

        return new CheckInAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvAddSurveyName.setText(surveyResultResponse.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return surveyResultResponse.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlChkIn;
        TextView tvAddSurveyName;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            rlChkIn = (RelativeLayout) itemView.findViewById(R.id.rlChkIn);
            tvAddSurveyName = (TextView) itemView.findViewById(R.id.tvAddSurveyName);
            tvAddSurveyName.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
