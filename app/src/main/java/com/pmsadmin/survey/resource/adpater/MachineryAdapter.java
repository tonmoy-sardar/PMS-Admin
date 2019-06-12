package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.contractor_vendor.ContractVendorReplica;
import com.pmsadmin.survey.resource.contractor_vendor.ContractorsDetails;
import com.pmsadmin.survey.resource.contractor_vendor.PandMDetails;
import com.pmsadmin.survey.resource.contractor_vendor.machinery_pojo.Result;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MachineryAdapter extends RecyclerView.Adapter<MachineryAdapter.MyViewHolder> {

    Activity activity;
    List<Result> resultList;

    public MachineryAdapter(Activity activity, List<Result> resultList) {

        this.activity = activity;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MachineryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machinery_list_item, parent, false);

        return new MachineryAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineryAdapter.MyViewHolder holder, final int position) {
        holder.tvItem.setText(resultList.get(position).getName());

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PandMDetails.class);
                intent.putExtra("pm_name",resultList.get(position).getName());
                intent.putExtra("pm_make",resultList.get(position).getMake());
                intent.putExtra("pm_hire",resultList.get(position).getHire());
                intent.putExtra("pm_khoraki",resultList.get(position).getKhoraki());
                intent.putExtra("pm_tender",resultList.get(position).getTender());
                intent.putExtra("pm_id",resultList.get(position).getId());
                intent.putExtra("pm_document_details",(Serializable) resultList.get(position).getDocumentDetails());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlItem;
        TextView tvItem;
        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            rlItem = itemView.findViewById(R.id.rlItem);
            tvItem = itemView.findViewById(R.id.tvItem);
        }
    }
}
