package com.pmsadmin.survey.resource.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DesignationWiseContactListAdapter extends RecyclerView.Adapter<DesignationWiseContactListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList_info = new ArrayList<JSONObject>();
    OnItemClickListener itemClickListener;


    public DesignationWiseContactListAdapter(Context context, ArrayList<JSONObject> arrayList_info) {
        this.context = context;
        this.arrayList_info = arrayList_info;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_designation_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_field_info.setText(arrayList_info.get(position).getString("field_label")+" : "+arrayList_info.get(position).getString("field_value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return arrayList_info.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_field_info;
        LinearLayout ll_adcl_total_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_field_info = itemView.findViewById(R.id.tv_field_info);
            ll_adcl_total_cell = itemView.findViewById(R.id.ll_adcl_total_cell);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
