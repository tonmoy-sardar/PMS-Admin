package com.pmsadmin.dashboard.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.model.DashboardItemsModel;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    Activity activity;
    public List<DashboardItemsModel> list;

    public ItemsAdapter(Activity activity, List<DashboardItemsModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_items, viewGroup, false);

        return new ItemsViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, int i) {
        itemsViewHolder.btn_item.setText(list.get(i).getItem());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        public Button btn_item;

        public ItemsViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            btn_item = itemView.findViewById(R.id.btn_item);
            btn_item.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
