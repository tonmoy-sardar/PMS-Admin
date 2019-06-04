package com.pmsadmin.survey.coordinates.raw_material_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.RawMaterialsActivity;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CheckInAdapter;
import com.pmsadmin.survey.coordinates.raw_materials_pojo.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RawMaterialAdapter extends RecyclerView.Adapter<RawMaterialAdapter.MyViewHolder> {

    Activity activity;
    List<Result> rawMaterialsResultList;

    public RawMaterialAdapter(Activity activity, List<Result> rawMaterialsResultList) {

        this.activity = activity;
        this.rawMaterialsResultList = rawMaterialsResultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_materials_item, parent, false);

        return new RawMaterialAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvMaterial.setText(rawMaterialsResultList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return rawMaterialsResultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMaterial;
        TextView tvMaterial;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            rlMaterial = itemView.findViewById(R.id.rlMaterial);
            tvMaterial = itemView.findViewById(R.id.tvMaterial);
            tvMaterial.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
