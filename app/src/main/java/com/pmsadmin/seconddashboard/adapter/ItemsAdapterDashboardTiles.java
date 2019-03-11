package com.pmsadmin.seconddashboard.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.dashboard.model.DashBoardModelImage;

import java.util.List;

public class ItemsAdapterDashboardTiles extends RecyclerView.Adapter<ItemsAdapterDashboardTiles.ItemsAdapterTilesViewHolder> {
    Activity activity;
    public List<DashBoardModelImage> items;
    public ItemsAdapterDashboardTiles(Activity activity, List<DashBoardModelImage> items){
        this.activity=activity;
        this.items=items;
    }
    @NonNull
    @Override
    public ItemsAdapterDashboardTiles.ItemsAdapterTilesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_second_tiles, viewGroup, false);

        return new ItemsAdapterTilesViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapterDashboardTiles.ItemsAdapterTilesViewHolder itemsAdapterTiles, int i) {
        itemsAdapterTiles.tv_item.setText(items.get(i).getItem());
        itemsAdapterTiles.iv_item.setImageResource(items.get(i).getImageId());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemsAdapterTilesViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_item;
        TextView tv_item;
        public ItemsAdapterTilesViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            iv_item=itemView.findViewById(R.id.iv_item);
            tv_item=itemView.findViewById(R.id.tv_item);
        }
    }
}

