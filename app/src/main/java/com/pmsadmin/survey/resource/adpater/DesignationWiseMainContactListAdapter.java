package com.pmsadmin.survey.resource.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;
import com.pmsadmin.survey.resource.dialog_fragment.Dialog_Fragment_add_more_info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DesignationWiseMainContactListAdapter extends RecyclerView.Adapter<DesignationWiseMainContactListAdapter.ViewHolder>
        implements DesignationWiseContactListAdapter.OnItemClickListener {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayList_info = new ArrayList<JSONObject>();
    DesignationWiseContactListAdapter designationWiseContactListAdapter;
    OnItemClickListener itemClickListener;

    public DesignationWiseMainContactListAdapter(DesignationWiseContactListActivity context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_designation_main_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        arrayList_info = new ArrayList<JSONObject>();
        try {
            JSONArray jsonArray = arrayList.get(position).getJSONArray("field_details");
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList_info.add(jsonArray.getJSONObject(i));
            }
            designationWiseContactListAdapter = new DesignationWiseContactListAdapter(context, arrayList_info);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            holder.rv_main_contact_list.setLayoutManager(layoutManager);
            holder.rv_main_contact_list.setHasFixedSize(true);
            holder.rv_main_contact_list.setAdapter(designationWiseContactListAdapter);


            holder.iv_add_more_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        try {
                            if (itemClickListener != null) {
                            itemClickListener.OnItemClick(position,arrayList.get(position).getString("id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            });


            holder.iv_edit_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        try {
                            if (itemClickListener != null) {
                            itemClickListener.OnItemClickDetails(position,arrayList.get(position).getString("id"),
                                    arrayList.get(position).getJSONArray("field_details"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void OnItemClick(int position) {
        System.out.println("position===========>>>"+position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_main_contact_list;
        LinearLayout ll_admcl_total_cell;
        ImageView iv_add_more_info,iv_edit_info;

        public ViewHolder(View itemView) {
            super(itemView);
            rv_main_contact_list = itemView.findViewById(R.id.rv_main_contact_list);
            ll_admcl_total_cell = itemView.findViewById(R.id.ll_admcl_total_cell);
            iv_add_more_info = itemView.findViewById(R.id.iv_add_more_info);
            iv_edit_info = itemView.findViewById(R.id.iv_edit_info);
        }
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position,String contact);
        void OnItemClickDetails(int position,String contact,JSONArray field_details);
    }
}
