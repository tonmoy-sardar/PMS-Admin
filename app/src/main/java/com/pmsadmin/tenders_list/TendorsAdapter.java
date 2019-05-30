package com.pmsadmin.tenders_list;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.StartSurveyHome;
import com.pmsadmin.tenderdashboard.adapter.TenderItemAdapter;
import com.pmsadmin.tenders_list.tendors_pojo.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TendorsAdapter extends RecyclerView.Adapter<TendorsAdapter.MyViewHolder> {

    private Activity activity;
    List<Result> tendorsResultList;

    public TendorsAdapter(Activity activity, List<Result> tendorsResultList) {

        this.activity = activity;
        this.tendorsResultList = tendorsResultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tendors_item, parent, false);

        return new TendorsAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvTendorItem.setText(tendorsResultList.get(position).getTenderGId().toString());
        holder.llTendorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Intent intent = new Intent(activity, StartSurveyHome.class);
                    intent.putExtra("tenderGID", tendorsResultList.get(position).getTenderGId());
                    intent.putExtra("tender_id", tendorsResultList.get(position).getId());
                    activity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return tendorsResultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTendorItem;
        LinearLayout llTendorItem;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            tvTendorItem = (TextView) itemView.findViewById(R.id.tvTendorItem);
            tvTendorItem.setTypeface(MethodUtils.getNormalFont(activity));

            llTendorItem = (LinearLayout) itemView.findViewById(R.id.llTendorItem);
        }
    }
}
