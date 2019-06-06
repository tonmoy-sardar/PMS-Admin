package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.contractor_vendor.ContractorsVendorsActivity;
import com.pmsadmin.survey.resource.contractor_vendor.contract_vendor_pojo.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContractVendorAdapter extends RecyclerView.Adapter<ContractVendorAdapter.MyViewHolder> {

    Activity activity;
    List<Result> resultContract;


    public ContractVendorAdapter(Activity activity, List<Result> resultContract) {

        this.activity = activity;
        this.resultContract = resultContract;

    }

    @NonNull
    @Override
    public ContractVendorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_materials_item, parent, false);

        return new ContractVendorAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractVendorAdapter.MyViewHolder holder, int position) {
        holder.tvMaterial.setText(resultContract.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return resultContract.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaterial;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            tvMaterial= itemView.findViewById(R.id.tvMaterial);
        }
    }
}
