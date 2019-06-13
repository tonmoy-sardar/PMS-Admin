package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.MaterialDetails;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.Result;
import com.pmsadmin.survey.resource.hydrological_data.HydroAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialDetailsAdapter extends RecyclerView.Adapter<MaterialDetailsAdapter.MyViewHolder> {

    Activity activity;
    List<Result> resultList;


    public MaterialDetailsAdapter(Activity activity, List<Result> resultList) {
        this.activity = activity;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_material_details, parent, false);

        return new MaterialDetailsAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (resultList.get(position).getExternal_user_name()!= null) {
            holder.tvVendorName.setText(resultList.get(position).getExternal_user_name());
        }

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvVendorName;
        TextView tvRateValue;
        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            tvVendorName = itemView.findViewById(R.id.tvVendorName);
        }
    }
}
