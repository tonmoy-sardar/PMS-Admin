package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CheckBoxAdapter;
import com.pmsadmin.survey.resource.AddVendroActivity;
import com.pmsadmin.survey.resource.add_vendor_list_pojo.AddVendorList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VendorDropdownAdapter extends RecyclerView.Adapter<VendorDropdownAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<AddVendorList> addVendorLists;
    Integer external_user_type_id;



    public VendorDropdownAdapter(Activity activity, ArrayList<AddVendorList> addVendorLists, Integer external_user_type_id) {

        this.activity = activity;
        this.addVendorLists = addVendorLists;
        this.external_user_type_id = external_user_type_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_vendors_dropdown, parent, false);

        return new VendorDropdownAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if (external_user_type_id == addVendorLists.get(position).getUserType()){
            holder.tvVendorName.setVisibility(View.VISIBLE);
            holder.tvVendorName.setText(addVendorLists.get(position).getContactPersonName());
        } else {
            holder.tvVendorName.setVisibility(View.GONE);
        }


        holder.tvVendorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addVendorLists.get(position).setSelected(true);
                /*if (holder.tvVendorName.isChecked()){
                    addVendorLists.get(position).setSelected(true);
                }else {
                    unitPojoList.get(position).setSelected(false);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return addVendorLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvVendorName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVendorName = itemView.findViewById(R.id.tvVendorName);
            tvVendorName.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
