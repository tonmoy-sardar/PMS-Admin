package com.pmsadmin.survey.resource.adpater;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.EstablishmentDetails;
import com.pmsadmin.survey.resource.contractor_vendor.ContractorsDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContractorsDocumentListAdapter extends RecyclerView.Adapter<ContractorsDocumentListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    public ContractorsDocumentListAdapter(ContractorsDetails context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_documents_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_doc_name.setText(arrayList.get(position).getString("document_name"));

            holder.ll_doc_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).getString("document")));
                        context.startActivity(browserIntent);
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_doc_name;
        LinearLayout ll_doc_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_doc_name = itemView.findViewById(R.id.tv_doc_name);
            ll_doc_view = itemView.findViewById(R.id.ll_doc_view);
        }
    }
}
