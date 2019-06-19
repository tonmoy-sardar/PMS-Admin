package com.pmsadmin.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.pmsadmin.MethodUtils.errorMsg;

public class LabourFragment extends Fragment {

    SpotsDialog loader;
    SearchableSpinner spnsitelocation;
    ArrayList<String> siteListname;
    int site_location=0;
    int sitetype=0;
    int project_id=0;
    ImageView save;
    public static  EditText edt_date,edt_completion_date,edt_start_time,edt_end_time;
    EditText edt_weather,edt_contractor_name,edt_activity,edt_remarks,edt_no_skilled_labour,edt_no_unskilled_labour,edt_milestone_completed;
    String weather="",contractors_name="",activity="",remarks="",skilledlabour="",unskilledlabour="",milestone_completed="";
    public static  String dt="",dt_completion="",planned_start_time="",planned_end_time="";

    private TextView tvLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.labour_frag_layout, container, false);
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
        save=(ImageView)view.findViewById(R.id.save);
        edt_date=(EditText)view.findViewById(R.id.edt_date);
        edt_weather=(EditText)view.findViewById(R.id.edt_weather);
        edt_contractor_name=(EditText)view.findViewById(R.id.edt_contractor_name);
        edt_milestone_completed=(EditText)view.findViewById(R.id.edt_milestone_completed);

        edt_completion_date=(EditText)view.findViewById(R.id.edt_completion_date);
        edt_activity=(EditText)view.findViewById(R.id.edt_activity);
        edt_remarks=(EditText)view.findViewById(R.id.edt_remarks);
        edt_no_skilled_labour=(EditText)view.findViewById(R.id.edt_no_skilled_labour);
        edt_no_unskilled_labour=(EditText)view.findViewById(R.id.edt_no_unskilled_labour);
        edt_start_time=(EditText)view.findViewById(R.id.edt_start_time);
        edt_end_time=(EditText)view.findViewById(R.id.edt_end_time);

        tvLabel = view.findViewById(R.id.tvLabel);

        edt_date.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_completion_date.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_start_time.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_end_time.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_weather.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_contractor_name.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_activity.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_remarks.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_no_skilled_labour.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_no_unskilled_labour.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_milestone_completed.setTypeface(MethodUtils.getNormalFont(getActivity()));
        tvLabel.setTypeface(MethodUtils.getNormalFont(getActivity()));



        if (!ConnectionDetector.isConnectingToInternet(getActivity())) {
            errorMsg(getActivity(), getActivity().getString(R.string.no_internet));
        }else {
            callSiteListApi();
        }
        edt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new StartTimeFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        edt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new EndTimeFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDate();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        edt_completion_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateCompletion();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weather=edt_weather.getText().toString().trim();
                activity=edt_activity.getText().toString().trim();
                contractors_name=edt_contractor_name.getText().toString().trim();
                remarks=edt_remarks.getText().toString().trim();
                skilledlabour=edt_no_skilled_labour.getText().toString().trim();
                unskilledlabour=edt_no_unskilled_labour.getText().toString().trim();
                milestone_completed=edt_milestone_completed.getText().toString().trim();

                if(site_location!=0)
                {
                    if(!dt.equals(""))

                    {


                        if(!weather.equals(""))
                        {
                            if(!dt_completion.equals(""))
                            {
                                if(!milestone_completed.equals(""))
                                {
                                    if(!contractors_name.equals(""))
                                    {
                                        if(!activity.equals(""))
                                        {
                                            if(!skilledlabour.equals(""))
                                            {
                                                if(!unskilledlabour.equals(""))
                                                {
                                                    if(!planned_start_time.equals(""))
                                                    {
                                                        if(!planned_end_time.equals(""))
                                                        {
                                                            if (!ConnectionDetector.isConnectingToInternet(getActivity()))
                                                            {
                                                                errorMsg(getActivity(), getActivity().getString(R.string.no_internet));
                                                            }
                                                            else {
                                                                SendData();
                                                            }
                                                        }
                                                        else {
                                                            Toast.makeText(getActivity(),"Select End Time",Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                    else {
                                                        Toast.makeText(getActivity(),"Select Start Time",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(getActivity(),"Enter Number of UnSkilled Labour",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            else {
                                                Toast.makeText(getActivity(),"Enter Number of Skilled Labour",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(getActivity(),"Enter Activity Details",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"Enter Contractor Name",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(),"Enter Milestone",Toast.LENGTH_SHORT).show();
                                }



                            }
                            else {
                                Toast.makeText(getActivity(),"Select Date of Completion",Toast.LENGTH_SHORT).show();
                            }
                        }

                        else{
                               Toast.makeText(getActivity(),"Enter Weather",Toast.LENGTH_SHORT).show();
                           }


                    }
                    else {
                        Toast.makeText(getActivity(),"Select Date",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),"Select Site",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void SendData()
    {

   //     loader.show_with_label("Loading");

        loader.show();

        JsonObject object = new JsonObject();
        object.addProperty("type_of_report", "2");
        object.addProperty("project_id", project_id);
        object.addProperty("site_location", site_location);
        object.addProperty("date_entry", dt+"T"+formattedDate);
        object.addProperty("weather", weather);
        object.addProperty("date_of_completion", dt_completion+"T"+formattedDate2);
        object.addProperty("milestone_to_be_completed", milestone_completed);

        JsonArray jsonArray=new JsonArray();

        JsonObject object2 = new JsonObject();
        object2.addProperty("name_of_contractor",contractors_name);
        object2.addProperty("number_skilled",skilledlabour);
        object2.addProperty("number_unskilled",unskilledlabour);
        object2.addProperty("start_time",planned_start_time);
        object2.addProperty("end_time",planned_end_time);
        object2.addProperty("remarks",remarks);
        object2.addProperty("details_activity",activity);

        jsonArray.add(object2);
        object.add("labour_data",jsonArray);

        Gson gson = new Gson();
        // convert your list to json
        final String bodyjson = gson.toJson(object);
        Log.d("sendbody", "" + bodyjson);

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.pms_execution_labour_progress_add("Token "
                + LoginShared.getLoginDataModel(getActivity()).getToken(),"application/json", object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try{
                    if (response.code() == 200 || response.code()==201)
                    {

                        String responseString = response.body().string();
                        Log.d("responssavelabourdata",responseString);
                        clearvalue();




                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    //errorMsg(getActivity(), getActivity().getString(R.string.error_occurred));
                }


                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });






    }
    public void clearvalue()
    {
        activity="";
        edt_activity.setText("");
        planned_start_time="";
        edt_start_time.setText("");
        planned_end_time="";
        edt_end_time.setText("");
        skilledlabour="";
        edt_no_skilled_labour.setText("");
        unskilledlabour="";
        edt_no_unskilled_labour.setText("");
        contractors_name="";
        edt_contractor_name.setText("");
        remarks="";
        edt_remarks.setText("");


    }
    public static class EndTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        int second;
        String totalsecond,totalhour,totalminitue;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            second=c.get(Calendar.SECOND);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(),this, hour, minute,true);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){


            if(second<=9)
            {
                totalsecond ="0"+second;
            }
            else {
                totalsecond= String.valueOf(second);
            }
            if(hourOfDay<=9)
            {
                totalhour="0"+hourOfDay;
            }
            else {
                totalhour= String.valueOf(hourOfDay);
            }

            if(minute<=9)
            {
                totalminitue="0"+minute;
            }
            else {
                totalminitue= String.valueOf(minute);
            }
            planned_end_time=totalhour+":"+totalminitue+":"+second;
            edt_end_time.setText(planned_end_time);
        }
    }

    public static class StartTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        int second;
        String totalsecond,totalhour,totalminitue;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            second=c.get(Calendar.SECOND);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(),this, hour, minute,true);
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){


            if(second<=9)
            {
                totalsecond ="0"+second;
            }
            else {
                totalsecond= String.valueOf(second);
            }
            if(hourOfDay<=9)
            {
                totalhour="0"+hourOfDay;
            }
            else {
                totalhour= String.valueOf(hourOfDay);
            }

            if(minute<=9)
            {
                totalminitue="0"+minute;
            }
            else {
                totalminitue= String.valueOf(minute);
            }
            planned_start_time=totalhour+":"+totalminitue+":"+second;
            edt_start_time.setText(planned_start_time);
        }
    }
    public static class SelectDateCompletion extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());


            //mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            //edt_dob.setText(day+"/"+month+"/"+year);


            if (month < 10) {

                if (day < 10) {
                    //edt_date.setText("0" + day + "/" + "0" + month + "/" + year);
                    dt_completion=year + "-" + "0" + month + "-" + "0" + day;
                    edt_completion_date.setText(dt_completion);
                }
                else {
                    dt_completion=year+"-"+"0"+month+"-"+day;
                    edt_completion_date.setText(dt_completion);
                }

            } else {
                if (day < 10) {
                    dt_completion=year+"-"+month+"-"+"0"+day;
                    edt_completion_date.setText(dt_completion);
                }
                else {
                    dt_completion=year+"-"+month+"-"+day;
                    edt_completion_date.setText(dt_completion);
                }

            }
            getCurrentTimeUsingCalendarCompletion();
        }

    }
    static  String formattedDate2="";
    public static  void getCurrentTimeUsingCalendarCompletion()
    {

        Calendar cal = Calendar.getInstance();

        Date date=cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        formattedDate2=dateFormat.format(date);

        Log.d("formateddate",formattedDate2);

    }
    public static class SelectDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());


            //mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            //edt_dob.setText(day+"/"+month+"/"+year);


            if (month < 10) {

                if (day < 10) {
                    //edt_date.setText("0" + day + "/" + "0" + month + "/" + year);
                    dt=year + "-" + "0" + month + "-" + "0" + day;
                    edt_date.setText(dt);
                }
                else {
                    dt=year+"-"+"0"+month+"-"+day;
                    edt_date.setText(dt);
                }

            } else {
                if (day < 10) {
                    dt=year+"-"+month+"-"+"0"+day;
                    edt_date.setText(dt);
                }
                else {
                    dt=year+"-"+month+"-"+day;
                    edt_date.setText(dt);
                }

            }
            getCurrentTimeUsingCalendar();
        }

    }
    static  String formattedDate="";

    public static void getCurrentTimeUsingCalendar() {

        Calendar cal = Calendar.getInstance();

        Date date=cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        formattedDate=dateFormat.format(date);

        Log.d("formateddate",formattedDate);
//        System.out.println("Current time of the day using Calendar - 24 hour format: "+ formattedDate);

    }
    ArrayList<SiteList> siteLists;
    public void callSiteListApi()
    {

        //loader.show_with_label("Loading");
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
                    //errorMsg(getActivity(),getActivity().getString(R.string.error_occurred));
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

       // loader.show_with_label("Loading");
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
                   // errorMsg(getActivity(), getActivity().getString(R.string.error_occurred));
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
