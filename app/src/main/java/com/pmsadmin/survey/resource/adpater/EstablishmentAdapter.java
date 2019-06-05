package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.EstablishmentActivity;
import com.pmsadmin.survey.resource.establishment_pojo.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EstablishmentAdapter extends RecyclerView.Adapter<EstablishmentAdapter.MyViewHolder> {
    Activity activity;
    List<Result> resultEstablishment;


    public EstablishmentAdapter(Activity activity, List<Result> resultEstablishment) {

        this.activity = activity;
        this.resultEstablishment = resultEstablishment;
    }

    @NonNull
    @Override
    public EstablishmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_materials_item, parent, false);



        return new EstablishmentAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull EstablishmentAdapter.MyViewHolder holder, int position) {


        holder.tvMaterial.setText(resultEstablishment.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return resultEstablishment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaterial;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            tvMaterial =  itemView.findViewById(R.id.tvMaterial);
        }
    }
}
