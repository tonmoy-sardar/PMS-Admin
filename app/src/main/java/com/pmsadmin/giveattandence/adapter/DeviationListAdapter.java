package com.pmsadmin.giveattandence.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.giveattandence.JustificationActivity;
import com.pmsadmin.giveattandence.deviation_list_model.Result;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeviationListAdapter extends RecyclerView.Adapter<DeviationListAdapter.ViewHolder> {


    Activity activity;
    List<Result> deviationResultList;
    private int lastSelectedPosition = -1;
    private LoadingData loader;

    public DeviationListAdapter(Activity activity, List<Result> deviationResultList) {

        this.activity = activity;
         this.deviationResultList = deviationResultList;
    }

    @NonNull
    @Override
    public DeviationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_justification_attendence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviationListAdapter.ViewHolder holder, final int position) {

        if (deviationResultList.get(position).getDeviationTime()!= null){

            holder.tvDeviationValue.setText(deviationResultList.get(position).getDeviationTime());
        }


        if (deviationResultList.get(position).getJustification()!= null){
            holder.etJustification.setText(deviationResultList.get(position).getJustification().toString());
            holder.btSubmit.setVisibility(View.GONE);
        }else {
            holder.etJustification.setText("");
            holder.btSubmit.setVisibility(View.VISIBLE);
        }


        if (deviationResultList.get(position).getDeviationType().equals("OD")){
            holder.rdOD.setChecked(true);
        }else if (deviationResultList.get(position).getDeviationType().equals("FD")){
            holder.rdFD.setChecked(true);
        }else if (deviationResultList.get(position).getDeviationType().equals("HD")){
            holder.rdHD.setChecked(true);
        }


        holder.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitDeviation(holder, position);
            }
        });
    }

    private void submitDeviation(final ViewHolder holder, int position) {


        String deviation_type = "";
        int radioButtonID = holder.rgLeaveType.getCheckedRadioButtonId();

        if (radioButtonID == R.id.rdOD){
            deviation_type = "OD";
            lastSelectedPosition = position;

        }else if (radioButtonID == R.id.rdFD){
            deviation_type = "FD";
            lastSelectedPosition = position;
        }else if (radioButtonID == R.id.rdHD){
            deviation_type = "HD";
            lastSelectedPosition = position;
        }


        if (deviation_type.equals("")){

            MethodUtils.errorMsg(activity, "Please select Deviation type.");
        }else if (holder.etJustification.getText().toString().trim().equals("")){
            MethodUtils.errorMsg(activity, "Please enter justification.");
        }else {

            loader = new LoadingData(activity);
            loader.show_with_label("Loading");
            JsonObject object = new JsonObject();
            object.addProperty("justification", holder.etJustification.getText().toString().trim());
            object.addProperty("deviation_type", deviation_type);

            System.out.println("object: " + object.toString());


            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_deviation_justification("Token "
                            + LoginShared.getLoginDataModel(activity).getToken(),
                    deviationResultList.get(position).getId().toString(), object);

            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (loader != null && loader.isShowing())
                        loader.dismiss();

                    if (response.code() == 201 || response.code() == 200) {
                        try {
                            String responseString = response.body().string();
                            System.out.println("resPost: " + responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject.optString("msg").equals("Success")) {

                                MethodUtils.errorMsg(activity, "Your Deviation justification submitted successfully.");
                                holder.btSubmit.setBackgroundColor(holder.btSubmit
                                        .getContext().getResources().getColor(R.color.opacity_blue));
                                //holder.btSubmit.setClickable(false);
                                holder.btSubmit.setVisibility(View.GONE);
                            } else {
                                MethodUtils.errorMsg(activity, "Something went wrong!! Please try again");
                            }

                            System.out.println("dev_provide: " + responseString);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }


        /*holder.rgLeaveType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //since only one package is allowed to be selected
                //this logic clears previous selection
                //it checks state of last radiogroup and
                // clears it if it meets conditions
                if (lastCheckedRadioGroup != null
                        && lastCheckedRadioGroup.getCheckedRadioButtonId()
                        != radioGroup.getCheckedRadioButtonId()
                        && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
                    lastCheckedRadioGroup.clearCheck();

                    Toast.makeText(PackageRecyclerViewAdapter.this.context,
                            "Radio button clicked " + radioGroup.getCheckedRadioButtonId(),
                            Toast.LENGTH_SHORT).show();

                }
                lastCheckedRadioGroup = radioGroup;

            }
        });*/




        //notifyDataSetChanged();

        Toast.makeText(activity,deviation_type,Toast.LENGTH_SHORT).show();

        /*JsonObject object = new JsonObject();
        object.addProperty("approved_status", status);*/

    }

    @Override
    public int getItemCount() {
        return deviationResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDeviationValue;
        private Button btSubmit;
        private RadioGroup rgLeaveType;
        private RadioButton rdOD,rdHD,rdFD;
        private EditText etJustification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviationValue = (TextView) itemView.findViewById(R.id.tvDeviationValue);
            tvDeviationValue.setTypeface(MethodUtils.getNormalFont(activity));

            btSubmit = (Button) itemView.findViewById(R.id.btSubmit);
            btSubmit.setTypeface(MethodUtils.getNormalFont(activity));

            rdOD = (RadioButton) itemView.findViewById(R.id.rdOD);
            rdHD = (RadioButton) itemView.findViewById(R.id.rdHD);
            rdFD = (RadioButton) itemView.findViewById(R.id.rdFD);
            rdOD.setTypeface(MethodUtils.getNormalFont(activity));
            rdHD.setTypeface(MethodUtils.getNormalFont(activity));
            rdFD.setTypeface(MethodUtils.getNormalFont(activity));

            etJustification = (EditText) itemView.findViewById(R.id.etJustification);
            etJustification.setTypeface(MethodUtils.getNormalFont(activity));

            rgLeaveType = (RadioGroup) itemView.findViewById(R.id.rgLeaveType);

        }
    }
}
