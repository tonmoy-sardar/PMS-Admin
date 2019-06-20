package com.pmsadmin.survey.coordinates.raw_material_adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.DocumentDetail;
import com.pmsadmin.survey.coordinates.external_mapping_pojo.MappingDocumentDetail;
import com.pmsadmin.survey.resource.hydrological_data.HydroAdapter;

import org.json.JSONException;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialsDocumentsAdapter extends RecyclerView.Adapter<MaterialsDocumentsAdapter.MyViewHolder> {

    Activity activity;
    List<MappingDocumentDetail> documentDetails;

    String uri = "";
    String extension = "";

    public MaterialsDocumentsAdapter(Activity activity, List<MappingDocumentDetail> documentDetails) {

        this.activity = activity;
        this.documentDetails = documentDetails;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.documents_ites_row, parent, false);

        return new MaterialsDocumentsAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {




        holder.tvDoc.setText(documentDetails.get(position).getDocumentName());

        if (documentDetails.get(position).getDocument()!= null){

             uri = documentDetails.get(position).getDocument();
             extension = uri.substring(uri.lastIndexOf(".") + 1);

            System.out.println("extension: "+extension);

            if (extension.equals("pdf")){

                //holder.ivDoc.setVisibility(View.VISIBLE);
                holder.ivDoc.setImageResource(R.drawable.ic_picture_as_pdf_blue_24dp);
                //holder.ivImage.setVisibility(View.GONE);

            }else {
                holder.ivDoc.setImageResource(R.drawable.ic_image_blue_24dp);
                //holder.ivDoc.setVisibility(View.GONE);
                //holder.ivImage.setVisibility(View.VISIBLE);
            }
        }






        holder.rlDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = null;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentDetails.get(position).getDocument()));
                    activity.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoc;
        RelativeLayout rlDoc;
        ImageView ivDoc,ivImage;
        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            tvDoc = itemView.findViewById(R.id.tvDoc);
            tvDoc.setTypeface(MethodUtils.getNormalFont(activity));
            rlDoc = itemView.findViewById(R.id.rlDoc);
            ivDoc = itemView.findViewById(R.id.ivDoc);
            //ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
