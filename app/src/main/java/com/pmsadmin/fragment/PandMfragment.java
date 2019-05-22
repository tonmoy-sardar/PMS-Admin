package com.pmsadmin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.login.model.SiteList;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.pmsadmin.MethodUtils.errorMsg;

public class PandMfragment extends Fragment {
    SpotsDialog loader;
    SearchableSpinner spnsitelocation;
    ArrayList<String> siteListname;
    int site_location=0;
    int sitetype=0;
    int project_id=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pandm_frag_layout, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {

            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {

                    getActivity().finish();

                    return true;
                }
                return false;
            }
        } );
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        loader = new SpotsDialog(getActivity());
        spnsitelocation=(SearchableSpinner)view.findViewById(R.id.spnsitelocation);

        if (!ConnectionDetector.isConnectingToInternet(getActivity())) {
            errorMsg(getActivity(), getActivity().getString(R.string.no_internet));
        }else {
            callSiteListApi();
        }
    }


    ArrayList<SiteList> siteLists;
    public void callSiteListApi()
    {

       // loader.show_with_label("Loading");
        //  }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_projectlistApi("Token "
                + LoginShared.getLoginDataModel(getActivity()).getToken());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try{
                    if (response.code() == 200)
                    {

                        String responseString = response.body().string();
                        Log.d("responsestring",responseString);

                        JSONObject jsonObject=new JSONObject(responseString);
                        int request_status= jsonObject.getInt("request_status");
                        if(request_status==1)
                        {
                            siteListname=new ArrayList<String>();
                            siteLists=new ArrayList<SiteList>();
                            JSONArray result= jsonObject.getJSONArray("result");
                            if(result.length()>0)
                            {

                                for(int i=0;i<result.length();i++)
                                {
                                    JSONObject jsonObject1=result.getJSONObject(i);
                                    int id=jsonObject1.getInt("id");
                                    String name=jsonObject1.getString("name");
                                    int type=jsonObject1.getInt("type");



                                    SiteList siteList = new SiteList();
                                    siteList.setSitename(name);
                                    siteList.setId(id);
                                    siteList.setType(type);

                                    siteListname.add(name);
                                    siteLists.add(siteList);

                                }


                                siteListname.add(0, "-Select Site-");
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, R.id.item, siteListname);
                                spnsitelocation.setAdapter(dataAdapter);
                                //setApdater();
                                spnsitelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String referredBy = siteListname.get(position);
                                        if (position == 0)
                                        {
                                            site_location =0;
                                            sitetype=0;
                                            project_id=0;
                                            Log.d("siteID", "::::::::::::::::" + site_location+"::::"+sitetype);
                                            return;
                                        } else {
                                            if (referredBy.equals(siteLists.get(position - 1).getSitename()))
                                            {
                                                site_location = siteLists.get(position - 1).getId();
                                                sitetype=siteLists.get(position-1).getType();
                                                Log.d("siteID", "::::::::::::::::" + site_location+"::::"+sitetype);
                                                projectidfetch();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }

                        }

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    errorMsg(getActivity(),getActivity().getString(R.string.error_occurred));
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });


    }
    public void projectidfetch()
    {

        //loader.show_with_label("Loading");
        //  }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_projectid("Token "
                + LoginShared.getLoginDataModel(getActivity()).getToken(),"application/json", String.valueOf(site_location));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try{
                    if (response.code() == 200)
                    {

                        String responseString = response.body().string();
                        Log.d("responsestring",responseString);

                        JSONObject jsonObject=new JSONObject(responseString);
                        JSONArray result= jsonObject.getJSONArray("result");
                        if(result.length()>0)
                        {
                            for(int i=0;i<result.length();i++)
                            {
                                JSONObject jsonObject1=result.getJSONObject(i);
                                project_id=jsonObject1.getInt("id");
                                Log.d("project_id", ":::::::::::::::::"+String.valueOf(project_id));


                            }
                        }


                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    errorMsg(getActivity(), getActivity().getString(R.string.error_occurred));
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });



    }
}
