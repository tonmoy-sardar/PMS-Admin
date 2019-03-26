package com.pmsadmin.filter.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pmsadmin.R;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    Activity activity;
    public ProjectAdapter(Activity activity){
        this.activity=activity;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_project_adapter, viewGroup, false);

        return new ProjectAdapter.ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
