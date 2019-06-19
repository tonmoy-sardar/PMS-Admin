package com.pmsadmin.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.login.model.SiteList;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.planreport.PlanningReports;
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
import java.util.List;

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

public class Progressfrag extends Fragment {
    SpotsDialog loader;
    SearchableSpinner spnsitelocation;
    ArrayList<String> siteListname;
    int site_location=0;
    int sitetype=0;
    public static EditText edt_date,edt_completion_date,edt_planed_start_time,edt_planed_end_time,edt_actual_start_time,edt_actual_end_time;
    public static  String dt="",dt_completion="";
    EditText edt_weather,edt_milestone_completed,edt_activity,edt_description,edt_uom,edt_planned_quantity,edt_achieved_quantity,edt_asigned_to,edt_contractor_name,edt_remarks,edt_major_achievements;
    public static String planned_start_time="",planned_end_time="",actual_end_time="",actual_start_time="";
    int project_id=0;
    ImageView save;
    ArrayList<String> itemValues =new ArrayList<String>();
    String weather="",milestone_completed="",activity="",description="",uom="",planned_quantity="",archieved_quantity="",assigned_to="",contractors_name="",remarks="",major_achivements="";
    private TextView tvLabelCompletionPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.progress_frag_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loader = new SpotsDialog(getActivity());
        spnsitelocation=(SearchableSpinner)view.findViewById(R.id.spnsitelocation);
        edt_date=(EditText)view.findViewById(R.id.edt_date);
        edt_weather=(EditText)view.findViewById(R.id.edt_weather);
        tvLabelCompletionPeriod = view.findViewById(R.id.tvLabelCompletionPeriod);

        /////////////////////////////////////////////////////////////


        edt_completion_date=(EditText)view.findViewById(R.id.edt_completion_date);
        edt_milestone_completed=(EditText)view.findViewById(R.id.edt_milestone_completed);
        edt_activity=(EditText)view.findViewById(R.id.edt_activity);
        edt_description=(EditText)view.findViewById(R.id.edt_description);
        edt_planed_start_time=(EditText)view.findViewById(R.id.edt_planed_start_time);
        edt_planed_end_time=(EditText)view.findViewById(R.id.edt_planed_end_time);
        edt_uom=(EditText)view.findViewById(R.id.edt_uom);
        edt_planned_quantity=(EditText)view.findViewById(R.id.edt_planned_quantity);
        edt_achieved_quantity=(EditText)view.findViewById(R.id.edt_achieved_quantity);
        edt_actual_start_time=(EditText)view.findViewById(R.id.edt_actual_start_time);
        edt_actual_end_time=(EditText)view.findViewById(R.id.edt_actual_end_time);
        edt_asigned_to=(EditText)view.findViewById(R.id.edt_asigned_to);
        edt_contractor_name=(EditText)view.findViewById(R.id.edt_contractor_name);
        edt_remarks=(EditText)view.findViewById(R.id.edt_remarks);
        edt_major_achievements=(EditText)view.findViewById(R.id.edt_major_achievements);
        save=(ImageView)view.findViewById(R.id.save);

        edt_date.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_weather.setTypeface(MethodUtils.getNormalFont(getActivity()));
        tvLabelCompletionPeriod.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_completion_date.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_milestone_completed.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_activity.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_description.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_planed_start_time.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_planed_end_time.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_uom.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_actual_start_time.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_actual_end_time.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_planned_quantity.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_achieved_quantity.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_asigned_to.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_contractor_name.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_remarks.setTypeface(MethodUtils.getNormalFont(getActivity()));
        edt_major_achievements.setTypeface(MethodUtils.getNormalFont(getActivity()));







        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weather=edt_weather.getText().toString().trim();
                milestone_completed=edt_milestone_completed.getText().toString().trim();
                activity=edt_activity.getText().toString().trim();
                description=edt_description.getText().toString().trim();
                uom=edt_uom.getText().toString().trim();
                planned_quantity=edt_planned_quantity.getText().toString().trim();
                archieved_quantity=edt_achieved_quantity.getText().toString().trim();
                assigned_to=edt_asigned_to.getText().toString().trim();
                contractors_name=edt_contractor_name.getText().toString().trim();
                remarks=edt_remarks.getText().toString().trim();
                major_achivements=edt_major_achievements.getText().toString().trim();

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
    if(!activity.equals(""))
    {
        if(!description.equals(""))
        {
            if(!planned_start_time.equals(""))
            {
                if(!planned_end_time.equals(""))
                {
                    if(!uom.equals(""))
                    {
                        if(!actual_start_time.equals(""))
                        {
                            if(!actual_end_time.equals(""))
                            {
                                if(!planned_quantity.equals(""))
                                {
                                    if(!archieved_quantity.equals(""))
                                    {
                                       if(!assigned_to.equals(""))
                                       {
                                           if(!contractors_name.equals(""))
                                           {
                                               //if(!remarks.equals(""))
                                              // {
                                                  // if(!major_achivements.equals(""))
                                                  // {

                                                       if (!ConnectionDetector.isConnectingToInternet(getActivity()))
                                                       {
                                                           errorMsg(getActivity(), getActivity().getString(R.string.no_internet));
                                                       }
                                                       else {
                                                           SendData();
                                                       }
                                                  // }
                                                 //  else {
                                                 //      Toast.makeText(getActivity(),"Enter Major Achievements",Toast.LENGTH_SHORT).show();
                                                  // }
                                              // }
                                              // else{
                                              //     Toast.makeText(getActivity(),"Enter Remarks",Toast.LENGTH_SHORT).show();
                                              // }

                                           }
                                           else {
                                               Toast.makeText(getActivity(),"Enter Contractor Name",Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                       else {
                                           Toast.makeText(getActivity(),"Enter the name to whom the project is assigned",Toast.LENGTH_SHORT).show();
                                       }
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"Enter Achieved Quantity",Toast.LENGTH_SHORT).show();
                                    }

                                }
else{
                                    Toast.makeText(getActivity(),"Enter Planned Quantity",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(),"Select Actual End Time",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            Toast.makeText(getActivity(),"Select Actual Start Time",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(getActivity(),"Enter UOM",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(),"Select Plnned End Time",Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(getActivity(),"Select Plnned Start Time",Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(getActivity(),"Enter Description",Toast.LENGTH_SHORT).show();
        }

    }
    else {
        Toast.makeText(getActivity(),"Enter Activity",Toast.LENGTH_SHORT).show();
    }

}else {
    Toast.makeText(getActivity(),"Enter Milestone",Toast.LENGTH_SHORT).show();
}
    }
    else {
        Toast.makeText(getActivity(),"Select Date of Completion",Toast.LENGTH_SHORT).show();
    }

}else{
    Toast.makeText(getActivity(),"Enter Weather",Toast.LENGTH_SHORT).show();
}

                    }
                    else {
                        Toast.makeText(getActivity(),"Select Date",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getActivity(),"Select Site",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!ConnectionDetector.isConnectingToInternet(getActivity())) {
            errorMsg(getActivity(), getActivity().getString(R.string.no_internet));
        }else {
            callSiteListApi();
        }
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

        edt_planed_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new PlanStartTimeFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        edt_planed_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new PlanEndTimeFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        edt_actual_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new ActualStartTimeFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        edt_actual_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new ActualEndTimeFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
    }
    public static class ActualStartTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
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
            actual_start_time=totalhour+":"+totalminitue+":"+totalsecond;
            edt_actual_start_time.setText(actual_start_time);
        }
    }


    public static class ActualEndTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
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
            actual_end_time=totalhour+":"+totalminitue+":"+second;
            edt_actual_end_time.setText(actual_end_time);
        }
    }

    public static class PlanEndTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
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
            planned_end_time=totalhour+":"+totalminitue+":"+totalsecond;
            edt_planed_end_time.setText(planned_end_time);
        }
    }

    public static class PlanStartTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
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
            edt_planed_start_time.setText(planned_start_time);
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
                                        if (position == 0) {
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
                    //errorMsg(getActivity(), getActivity().getString(R.string.error_occurred));
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });



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
public void SendData()
{

   // loader.show_with_label("Loading");
loader.show();


    JsonObject object = new JsonObject();
    object.addProperty("type_of_report", "1");
    object.addProperty("project_id", project_id);
    object.addProperty("site_location", site_location);
    object.addProperty("date_entry", dt+"T"+formattedDate);
    object.addProperty("weather", weather);
    object.addProperty("date_of_completion", dt_completion+"T"+formattedDate2);
    object.addProperty("milestone_to_be_completed", milestone_completed);

    JsonArray jsonArray=new JsonArray();

    JsonObject object2 = new JsonObject();
    object2.addProperty("activity",activity);
    object2.addProperty("description",description);
    object2.addProperty("uom",uom);
    object2.addProperty("planned_start_time",planned_start_time);
    object2.addProperty("planned_end_time",planned_end_time);
    object2.addProperty("actual_start_time",actual_start_time);
    object2.addProperty("actual_end_time",actual_end_time);
    object2.addProperty("planned_quantity",planned_quantity);
    object2.addProperty("archieved_quantity",archieved_quantity);
    object2.addProperty("assigned_to",assigned_to);
    object2.addProperty("contractors_name",contractors_name);
    object2.addProperty("remarks",remarks);
    object2.addProperty("major_achivements",major_achivements);

    jsonArray.add(object2);
    object.add("progress_data",jsonArray);

    Gson gson = new Gson();
    // convert your list to json
    final String bodyjson = gson.toJson(object);
    Log.d("sendbody", "" + bodyjson);

    Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    final Call<ResponseBody> register = apiInterface.call_ppms_execution_daily_progress_add("Token "
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
                    Log.d("responssavedata",responseString);
                    clearvalue();




                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
               // errorMsg(getActivity(), getActivity().getString(R.string.error_occurred));
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
    description="";
    edt_description.setText("");
    planned_start_time="";
    edt_planed_start_time.setText("");
    planned_end_time="";
    edt_planed_end_time.setText("");
    uom="";
    edt_uom.setText("");
    actual_start_time="";
    edt_actual_start_time.setText("");
    actual_end_time="";
    edt_actual_end_time.setText("");
    planned_quantity="";
    edt_planned_quantity.setText("");
    archieved_quantity="";
    edt_achieved_quantity.setText("");
    assigned_to="";
    edt_asigned_to.setText("");
    contractors_name="";
    edt_contractor_name.setText("");
    remarks="";
    edt_remarks.setText("");
    major_achivements="";
    edt_major_achievements.setText("");

}

}
