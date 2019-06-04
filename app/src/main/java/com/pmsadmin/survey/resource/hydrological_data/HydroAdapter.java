package com.pmsadmin.survey.resource.hydrological_data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.adpater.EstablishmentAdapter;
import com.pmsadmin.survey.resource.hydrological_data.hydro_pojo.Result;

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
    public void onBindViewHolder(@NonNull HydroAdapter.MyViewHolder holder, int position) {

        holder.tvMaterial.setText(resultHydroList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return resultHydroList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaterial;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            tvMaterial = itemView.findViewById(R.id.tvMaterial);
        }
    }
}
