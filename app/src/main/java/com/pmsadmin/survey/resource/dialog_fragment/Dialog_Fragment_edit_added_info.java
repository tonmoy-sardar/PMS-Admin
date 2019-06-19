package com.pmsadmin.survey.resource.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;
import com.pmsadmin.survey.resource.adpater.DesignationWiseContactLisEdittAdapter;
import com.pmsadmin.survey.resource.adpater.DesignationWiseMainContactListAdapter;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by USER on 28-Oct-16.
 */

public class Dialog_Fragment_edit_added_info extends DialogFragment implements DesignationWiseContactLisEdittAdapter.OnItemClickListener {

    Dialog dialog;
    View v;
    Animation animation_zoom_in;
    Animation slide_out_buttom;
    TextView tv_save;
    private LoadingData loader;
    String designation_id = "", tender_id = "", contact_id = "";
    OnItemClickDialog itemClickDialog;
    JSONArray field_details;
    RecyclerView rv_edit_info_list;
    DesignationWiseContactLisEdittAdapter designationWiseContactLisEdittAdapter;
    ArrayList<JSONObject> arrayList;


    public void setData(String id, String tender_id, String contact_id, JSONArray field_details) {
        designation_id = id;
        this.tender_id = tender_id;
        this.contact_id = contact_id;
        this.field_details = field_details;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.DialogCustomTheme_image);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogCustomTheme_image;
        System.out.println("Dialog ONcreate============>");

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_edit_added_info, container, false);
        animation_zoom_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
        slide_out_buttom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_bottom);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());
        System.out.println("field_details=====>>>" + field_details);
        System.out.println("token=====>>>" + LoginShared.getLoginDataModel(getActivity()).getToken());

        loader = new LoadingData(getActivity());

        rv_edit_info_list = v.findViewById(R.id.rv_edit_info_list);
        tv_save = v.findViewById(R.id.tv_save);


        try {
            arrayList = new ArrayList<JSONObject>();
            for (int i = 0; i < field_details.length(); i++) {
                arrayList.add(field_details.getJSONObject(i));
            }
            designationWiseContactLisEdittAdapter = new DesignationWiseContactLisEdittAdapter(getActivity(), arrayList);
            designationWiseContactLisEdittAdapter.setOnItemClickListener(this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rv_edit_info_list.setLayoutManager(layoutManager);
            rv_edit_info_list.setHasFixedSize(true);
            rv_edit_info_list.setAdapter(designationWiseContactLisEdittAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_info();
            }
        });


        return v;
    }


    private void edit_info() {
        try {
            loader.show_with_label("Please wait");

            JSONObject object = new JSONObject();
            object.put("tender", tender_id);
            object.put("designation", designation_id);
            object.put("prev_field_details_exist", "yes");
            JSONArray field_detail = new JSONArray(arrayList);
            object.put("field_details", field_detail);
            System.out.println("object======>>>>" + object.toString());
            JsonObject gson = new JsonParser().parse(object.toString()).getAsJsonObject();
            System.out.println("gson object======>>>>" + object.toString());


            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_put_resource_contact_details_edit("Token " +
                    LoginShared.getLoginDataModel(getActivity()).getToken(), "application/json",Integer.valueOf(contact_id), gson);


            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();

                    if (response.code() == 201 || response.code() == 200) {
                        System.out.println("Service URL==>" + response.raw().request().url());
                        try {
                            String responseString = response.body().string();
                            System.out.println("response output==========>>>" + responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (itemClickDialog != null) {
                                itemClickDialog.onItemClick();
                            }
                            Toast.makeText(getActivity(), "Information Added successfully.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setOnDialogListener(OnItemClickDialog itemClickDialog) {
        this.itemClickDialog = itemClickDialog;
    }

    public interface OnItemClickDialog {
        void onItemClick();
    }


    @Override
    public void OnItemClick(int position) {

    }

}
