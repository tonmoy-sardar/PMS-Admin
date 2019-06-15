package com.pmsadmin.survey.coordinates.coordinate_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.CrusherActivity;
import com.pmsadmin.survey.coordinates.CrusherDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignedCrusherListAdapter extends RecyclerView.Adapter<AssignedCrusherListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrayList_crusher_doc_list;
    AssignedCrusherDocListAdapter assignedCrusherDocListAdapter;

    public AssignedCrusherListAdapter(CrusherDetailsActivity context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_assigned_crusher_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_crusher_name.setText(arrayList.get(position).getString("external_user_name"));
            holder.tv_crusher_no.setText("Contact No. "+arrayList.get(position).getString("external_user_contact"));

            arrayList_crusher_doc_list = new ArrayList<JSONObject>();
            JSONArray jsonArray = arrayList.get(position).getJSONArray("mapping_document_details");
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList_crusher_doc_list.add(jsonArray.getJSONObject(i));
            }
            assignedCrusherDocListAdapter = new AssignedCrusherDocListAdapter(context, arrayList_crusher_doc_list);
            holder.rv_assigned_crusher_document_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.rv_assigned_crusher_document_list.setHasFixedSize(true);
            holder.rv_assigned_crusher_document_list.setAdapter(assignedCrusherDocListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_crusher_name,tv_crusher_no;
        RecyclerView rv_assigned_crusher_document_list;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_crusher_name = itemView.findViewById(R.id.tv_crusher_name);
            tv_crusher_no = itemView.findViewById(R.id.tv_crusher_no);
            rv_assigned_crusher_document_list = itemView.findViewById(R.id.rv_assigned_crusher_document_list);
        }
    }
}
