package com.pmsadmin.GridSpanSizeLookUp;


import android.support.v7.widget.GridLayoutManager;

import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;

/**
 * GridLayoutManager.SpanSizeLookup implementation used to show a header in a RecyclerView when the
 * LayoutManager used is a GridLayoutManager.
 */
public class GridSpanSizeLookupForListDetailsAdapter extends GridLayoutManager.SpanSizeLookup {

    private final String TAG = "LookupForShopSales";
    private final ItemsAdapterTiles adapter;
    private final GridLayoutManager layoutManager;

    public GridSpanSizeLookupForListDetailsAdapter(ItemsAdapterTiles adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        int returnSize = adapter.items.get(position) == null ? layoutManager.getSpanCount() : 1;
//        int returnSize = adapter.listDetailsModel.size() - 1 == position ? layoutManager.getSpanCount() : 1;
        //Log.e(TAG, "getSpanSize: " + returnSize + "  // layoutManager.getSpanCount(): " + layoutManager.getSpanCount());
        return returnSize;
    }
}