package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.resource.EstablishmentDetails;
import com.pmsadmin.survey.resource.contractor_vendor.ContractorsDetails;
import com.pmsadmin.survey.resource.contractor_vendor.ContractorsVendorsActivity;
import com.pmsadmin.survey.resource.contractor_vendor.contract_vendor_pojo.Result;

import java.io.Serializable;
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
    public void onBindViewHolder(@NonNull ContractVendorAdapter.MyViewHolder holder, final int position) {
        holder.tvMaterial.setText(resultContract.get(position).getName());

        holder.rlMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ContractorsDetails.class);
                intent.putExtra("contractors_name",resultContract.get(position).getName());
                intent.putExtra("contractors_details",resultContract.get(position).getDetails());
                intent.putExtra("contractors_tender",resultContract.get(position).getTender());
                intent.putExtra("contractors_id",resultContract.get(position).getId());
                intent.putExtra("contractors_document_details",(Serializable) resultContract.get(position).getContractorDocumentDetails());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultContract.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaterial;
        RelativeLayout rlMaterial;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            tvMaterial= itemView.findViewById(R.id.tvMaterial);
            tvMaterial.setTypeface(MethodUtils.getNormalFont(activity));
            rlMaterial =  itemView.findViewById(R.id.rlMaterial);
        }
    }
}
