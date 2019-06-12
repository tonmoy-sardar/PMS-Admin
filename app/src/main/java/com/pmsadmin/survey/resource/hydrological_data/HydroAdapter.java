package com.pmsadmin.survey.resource.hydrological_data;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.EstablishmentDetails;
import com.pmsadmin.survey.resource.adpater.EstablishmentAdapter;
import com.pmsadmin.survey.resource.hydrological_data.hydro_pojo.Result;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HydroAdapter extends RecyclerView.Adapter<HydroAdapter.MyViewHolder> {

    Activity activity;
    List<Result> resultHydroList;


    public HydroAdapter(Activity activity, List<Result> resultHydroList) {
        this.activity = activity;
        this.resultHydroList = resultHydroList;

    }

    @NonNull
    @Override
    public HydroAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_materials_item, parent, false);

        return new HydroAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull HydroAdapter.MyViewHolder holder, final int position) {
        holder.tvMaterial.setText(resultHydroList.get(position).getName());

        holder.rlMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HydrologicalDetails.class);
                intent.putExtra("hydrological_name",resultHydroList.get(position).getName());
                intent.putExtra("hydrological_details",resultHydroList.get(position).getDetails());
                intent.putExtra("hydrological_tender",resultHydroList.get(position).getTender());
                intent.putExtra("hydrological_id",resultHydroList.get(position).getId());
                intent.putExtra("hydrological_document_details",(Serializable) resultHydroList.get(position).getHyderologicalDocumentDetails());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultHydroList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaterial;
        RelativeLayout rlMaterial;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            tvMaterial = itemView.findViewById(R.id.tvMaterial);
            rlMaterial = itemView.findViewById(R.id.rlMaterial);
        }
    }
}
