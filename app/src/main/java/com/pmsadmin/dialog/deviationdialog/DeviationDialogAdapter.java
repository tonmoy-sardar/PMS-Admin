package com.pmsadmin.dialog.deviationdialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pmsadmin.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviationDialogAdapter extends RecyclerView.Adapter<DeviationDialogAdapter.DeviationDialogViewHolder> {
    Activity activity;
    public DeviationDialogAdapter(Activity activity){
        this.activity=activity;
    }
    @NonNull
    @Override
    public DeviationDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_deviation, parent, false);

        return new DeviationDialogAdapter.DeviationDialogViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviationDialogViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class DeviationDialogViewHolder extends RecyclerView.ViewHolder{

        public DeviationDialogViewHolder(@NonNull View itemView,Activity activity) {
            super(itemView);
        }
    }
}
