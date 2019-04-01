package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.attendancelist.approvallistmodel.Result;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.util.List;

public class AttendanceApprovalListAdapter extends RecyclerView.Adapter<ApprovalViewHolder> {
    Activity activity;
    List<Result> approvalList;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private LoadingData loader;

    public AttendanceApprovalListAdapter(Activity activity, List<Result> approvalList) {
        this.activity = activity;
        this.approvalList = approvalList;
        loader = new LoadingData(activity);
    }

    @NonNull
    @Override
    public ApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ApprovalViewHolder vh = null;
        if (i == VIEW_ITEM) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_approval_item, viewGroup, false);

            vh = new ApprovalViewHolder(activity, itemView);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup, false);

            vh = new ProgressViewHolder(v, activity, true);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ApprovalViewHolder approvalViewHolder, int i) {
        if (approvalViewHolder instanceof ProgressViewHolder) {
            return;
        }
        if (approvalList.get(i).getEmployeeDetails().size() > 0) {

            if (approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName() != null ||
                    !approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName().equalsIgnoreCase("null") ||
                    !approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName().equals("")) {
                approvalViewHolder.tv_name.setText(approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName() + " " +
                        approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getLastName());
            } else {
                approvalViewHolder.tv_name.setText("No Name");
            }

            if (approvalList.get(i).getLoginTime() != null || !approvalList.get(i).getLoginTime().equals("") ||
                    !approvalList.get(i).getLoginTime().equalsIgnoreCase("null") || approvalList.get(i).getLogoutTime() != null || !approvalList.get(i).getLogoutTime().equals("") ||
                    !approvalList.get(i).getLogoutTime().equalsIgnoreCase("null")) {
                approvalViewHolder.tv_form.setText("Form: " + approvalList.get(i).getLoginTime() + " To: " +
                        approvalList.get(i).getLogoutTime());
            } else {
                approvalViewHolder.tv_form.setText("");
            }

            if (!approvalList.isEmpty()||approvalList.get(i).getJustification() != null || !approvalList.get(i).getJustification().equals("") ||
                    !approvalList.get(i).getJustification().equalsIgnoreCase("null")) {
                approvalViewHolder.tv_reason.setText("Reason: " + approvalList.get(i).getJustification());
            } else {
                approvalViewHolder.tv_reason.setText("");
            }
        }

        approvalViewHolder.btn_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionDetector.isConnectingToInternet(activity)) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
                }else {
                    attendanceEditApi(2);
                }
            }
        });

        approvalViewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionDetector.isConnectingToInternet(activity)) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
                }else {
                    attendanceEditApi(3);
                }
            }
        });

        approvalViewHolder.btn_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionDetector.isConnectingToInternet(activity)) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
                }else {
                    attendanceEditApi(3);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return approvalList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return approvalList.size();
    }

    public static class ProgressViewHolder extends ApprovalViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view, Activity activity, boolean tempStatus) {
            super(view, activity, tempStatus);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }

    private void attendanceEditApi(int status) {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("approved_status", status);
        object.addProperty("justification", "");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceLogoutApi("Token "
                        + LoginShared.getLoginDataModel(activity).getToken(),
                LoginShared.getAttendanceAddDataModel(activity).getResult().getId().toString(),
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code()==200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        AttendanceAddModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(activity, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(activity, activity.getString(R.string.error_occurred));
            }
        });

    }
}
