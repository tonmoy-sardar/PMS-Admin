package com.pmsadmin.filter.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pmsadmin.R;

public class DesignationAdapter extends RecyclerView.Adapter<DesignationAdapter.DesignationViewHolder> {
    Activity activity;
    public DesignationAdapter(Activity activity){
        this.activity=activity;
    }
    @NonNull
    @Override
    public DesignationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_project_adapter, viewGroup, false);

        return new DesignationAdapter.DesignationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignationViewHolder designationViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class DesignationViewHolder extends RecyclerView.ViewHolder {

        public DesignationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
