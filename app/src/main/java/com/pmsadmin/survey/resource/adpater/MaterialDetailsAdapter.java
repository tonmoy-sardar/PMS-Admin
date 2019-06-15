package com.pmsadmin.survey.resource.adpater;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.MaterialDetails;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.Result;
import com.pmsadmin.survey.coordinates.raw_material_adapter.MaterialsDocumentsAdapter;
import com.pmsadmin.survey.resource.hydrological_data.HydroAdapter;
import com.pmsadmin.utils.ItemOffsetDecoration;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.zelory.compressor.FileUtil;

import static android.app.Activity.RESULT_OK;

public class MaterialDetailsAdapter extends RecyclerView.Adapter<MaterialDetailsAdapter.MyViewHolder> {

    Activity activity;
    List<Result> resultList;
    MaterialsDocumentsAdapter materialsDocumentsAdapter;

    private int PICK_PDF_REQUEST = 1;

    MaterialDetailsAdapter.OnItemClickListener itemClickListener;


    public MaterialDetailsAdapter(Activity activity, List<Result> resultList) {
        this.activity = activity;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_material_details, parent, false);

        return new MaterialDetailsAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if (resultList.get(position).getExternalUserName()!= null) {
            holder.tvVendorName.setText(resultList.get(position).getExternalUserName());
        }



        holder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFile(position, holder.etDocumentName.getText().toString());
            }
        });



        if (resultList.get(position).getMappingDocumentDetails().size() > 0){

            materialsDocumentsAdapter = new MaterialsDocumentsAdapter(activity,
                    resultList.get(position).getMappingDocumentDetails());
            holder.rvDocuments.setAdapter(materialsDocumentsAdapter);
            holder.rvDocuments.setItemAnimator(new DefaultItemAnimator());


            LinearLayoutManager horizontalLayoutManager =
                    new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);

            holder.rvDocuments.setLayoutManager(horizontalLayoutManager);

            ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(activity, 2);
            holder.rvDocuments.addItemDecoration(itemOffset);
        }

    }

    private void showFile(int position, String s) {

        itemClickListener.OnItemClick(position,s);
    }





    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvVendorName;
        TextView tvRateValue;
        RecyclerView rvDocuments;
        ImageView ivSelect;
        EditText etDocumentName;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            tvVendorName = itemView.findViewById(R.id.tvVendorName);
            tvVendorName.setTypeface(MethodUtils.getNormalFont(activity));
            rvDocuments = itemView.findViewById(R.id.rvDocuments);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            etDocumentName = itemView.findViewById(R.id.etDocumentName);
            etDocumentName.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }

    public void setOnItemClickListener(MaterialDetailsAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position, String s);
    }



}
