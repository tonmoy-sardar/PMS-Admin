package com.pmsadmin.giveattandence.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.giveattandence.AttendanceJustification;
import com.pmsadmin.giveattandence.JustificationActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceViewHolder> {
    Activity activity;
    List<com.pmsadmin.giveattandence.updatedattandenceListModel.Result> results;

    public AttendanceHistoryAdapter(Activity activity, List<com.pmsadmin.giveattandence.updatedattandenceListModel.Result> results) {
        this.activity = activity;
        this.results = results;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_attendance_list, viewGroup, false);


        return new AttendanceHistoryAdapter.AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceViewHolder attendanceViewHolder, final int i) {


        /*Arghya---------------------------------(10.05.2019)-------------------------------------------*/

        if (results.get(i).getHoliday() != 1) {
            attendanceViewHolder.tvDateValue.setText(results.get(i).getDate());
            if (results.get(i).getLoginTime().equals("")) {
                attendanceViewHolder.tvLoginValue.setText("N/A");
            } else {
                //attendanceViewHolder.tvLoginValue.setText(results.get(i).getLoginTime());
                String loginTime = results.get(i).getLoginTime();
                String[] separated = loginTime.split("T");

                /*String sep1 = separated[0];
                String sep2 = separated[1];*/

                String timeLogIn = separated[1];
                String[] sperated2 = timeLogIn.split(":");

                int time = Integer.parseInt(sperated2[0]);

                if (time < 12){
                    attendanceViewHolder.tvLoginValue.setText(separated[1]+ " AM");
                }else {
                    attendanceViewHolder.tvLoginValue.setText(separated[1]+ " PM");
                }




            }

            if (results.get(i).getLogoutTime().equals("")) {
                attendanceViewHolder.tvLogoutValue.setText("N/A");
            } else {

                String logoutTime = results.get(i).getLogoutTime();
                //String[] separated = logoutTime.split("T");

                if (logoutTime.equals("0") ) {


                    attendanceViewHolder.tvLogoutValue.setText(results.get(i).getLogoutTime());

                    //attendanceViewHolder.tvLogoutValue.setText(results.get(i).getLogoutTime());
                    //attendanceViewHolder.tvLogoutValue.setText(separated[1]);
                }else {
                    //attendanceViewHolder.tvLogoutValue.setText(results.get(i).getLogoutTime());

                    String[] separated = logoutTime.split("T");

                /*String sep1 = separated[0];
                String sep2 = separated[1];*/

                    String timeLogOut = separated[1];
                    String[] sperated2 = timeLogOut.split(":");

                    int time = Integer.parseInt(sperated2[0]);

                    if (time < 12){
                        attendanceViewHolder.tvLogoutValue.setText(separated[1]+ " AM");
                    }else {
                        attendanceViewHolder.tvLogoutValue.setText(separated[1]+ " PM");
                    }
                }
            }

            if (results.get(i).getPresent() == 0) {
                attendanceViewHolder.tvStatusValue.setText("Absent");
            } else {
                attendanceViewHolder.tvStatusValue.setText("Present"+" "/*+ String.valueOf(results.get(i).getId())*/);
            }


            if (results.get(i).getPresent() == 1){

                if (results.get(i).getIs_deviation() == 1){

                    attendanceViewHolder.btJustify.setVisibility(View.VISIBLE);
                    /*attendanceViewHolder.btJustify.setBackgroundColor(attendanceViewHolder.btJustify
                            .getContext().getResources().getColor(R.color.deviation_button_blue));*/

                    attendanceViewHolder.btJustify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, JustificationActivity.class);
                            intent.putExtra("attendence_id",results.get(i).getId());
                            activity.startActivity(intent);
                        }
                    });

                }else {

                    attendanceViewHolder.btJustify.setVisibility(View.GONE);
                }


               /* if (results.get(i).getIs_ten_hrs() < 1){
                    //attendanceViewHolder.btJustify.setVisibility(View.VISIBLE);
                    attendanceViewHolder.btJustify.setVisibility(View.VISIBLE);


                    setPosition(attendanceViewHolder, i);

                    *//*attendanceViewHolder.btJustify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, AttendanceJustification.class);
                            intent.putExtra("attendence_id",results.get(i).getId());
                            activity.startActivity(intent);
                        }
                    });*//*


                    //attendanceViewHolder.btJustify.setVisibility(View.GONE);
                }else {
                    attendanceViewHolder.btJustify.setVisibility(View.GONE);
                }*/



            }else {

                //attendanceViewHolder.btJustify.setVisibility(View.GONE);

                attendanceViewHolder.btJustify.setVisibility(View.GONE);
                attendanceViewHolder.btJustify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, AttendanceJustification.class);
                        intent.putExtra("attendence_id",results.get(i).getId());
                        activity.startActivity(intent);
                    }
                });


                /*attendanceViewHolder.btJustify.setVisibility(View.VISIBLE);
                attendanceViewHolder.btJustify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, JustificationActivity.class);
                        intent.putExtra("attendence_id",results.get(i).getId());
                        activity.startActivity(intent);
                    }
                });*/
            }



        }else {

            attendanceViewHolder.btJustify.setVisibility(View.GONE);
        }




        attendanceViewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsMenu(attendanceViewHolder.menu, attendanceViewHolder, i);
            }
        });


        /*attendanceViewHolder.btJustify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, JustificationActivity.class);
                activity.startActivity(intent);
            }
        });*/

        /*Arghya---------------------------------(10.05.2019)-------------------------------------------*/

        /*if (results.get(i).getHoliday() == 1) {
            attendanceViewHolder.tv_date_value.setVisibility(View.GONE);
            attendanceViewHolder.tv_login.setVisibility(View.GONE);
            attendanceViewHolder.tv_logout.setVisibility(View.GONE);
            attendanceViewHolder.tv_holiday.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_holiday.setText("Status: Holiday");
            if (results.get(i).getDate() == null || results.get(i).getDate().equalsIgnoreCase("null") ||
                    results.get(i).getDate().equals("")) {
                attendanceViewHolder.tv_date.setText("");
            } else {
                attendanceViewHolder.tv_date.setText("Date: " + results.get(i).getDate());
            }

            if (results.get(i).getJustification() == null || results.get(i).getJustification().equalsIgnoreCase("null")
                    || results.get(i).getJustification().equals("")) {
                //attendanceViewHolder.view3.setVisibility(View.GONE);
                attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                attendanceViewHolder.tv_logout.setText("");
            } else {
                //attendanceViewHolder.view3.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_logout.setText("Justification: " + results.get(i).getJustification());
            }
        } else {
            if (results.get(i).getPresent() == 1) {
                attendanceViewHolder.tv_date_value.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_login.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_holiday.setVisibility(View.GONE);
                if (results.get(i).getDate() == null || results.get(i).getDate().equalsIgnoreCase("null") ||
                        results.get(i).getDate().equals("")) {
                    attendanceViewHolder.tv_date.setText("");
                } else {
                    attendanceViewHolder.tv_date.setText("Date: " + results.get(i).getDate());
                }

                if (results.get(i).getLoginTime() == null || results.get(i).getLoginTime().equalsIgnoreCase("null")
                        || results.get(i).getLoginTime().equals("")) {
                    attendanceViewHolder.tv_date_value.setText("");
                } else {
                    attendanceViewHolder.tv_date_value.setText("Log In Time: " + MethodUtils.profileDate(results.get(i).getLoginTime()));
                }

                if (results.get(i).getLogoutTime() == null || results.get(i).getLogoutTime().equalsIgnoreCase("null")
                        || results.get(i).getLogoutTime().equals("")) {
                    attendanceViewHolder.tv_login.setText("");
                } else {
                    attendanceViewHolder.tv_login.setText("Log Out Time: " + MethodUtils.profileDate(results.get(i).getLogoutTime()));
                }

                if (results.get(i).getJustification() == null || results.get(i).getJustification().equalsIgnoreCase("null")
                        || results.get(i).getJustification().equals("")) {
                    //attendanceViewHolder.view3.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setText("");
                } else {
                    //attendanceViewHolder.view3.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setText("Justification: " + results.get(i).getJustification());
                }
            } else {
                attendanceViewHolder.tv_date_value.setVisibility(View.GONE);
                attendanceViewHolder.tv_login.setVisibility(View.GONE);
                attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                attendanceViewHolder.tv_holiday.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_holiday.setText("Status: Absent");
                if (results.get(i).getDate() == null || results.get(i).getDate().equalsIgnoreCase("null") ||
                        results.get(i).getDate().equals("")) {
                    attendanceViewHolder.tv_date.setText("");
                } else {
                    attendanceViewHolder.tv_date.setText("Date: " + results.get(i).getDate());
                }

                if (results.get(i).getJustification() == null || results.get(i).getJustification().equalsIgnoreCase("null")
                        || results.get(i).getJustification().equals("")) {
                    //attendanceViewHolder.view3.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setText("");
                } else {
                    //attendanceViewHolder.view3.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setText("Justification: " + results.get(i).getJustification());
                }
            }
        }*/





        /*if(result.get(i).getDate()==null||result.get(i).getDate().equalsIgnoreCase("null")||
                result.get(i).getDate().equals("")){
            attendanceViewHolder.tv_date_value.setText("");
        }else{
            attendanceViewHolder.tv_date_value.setText(result.get(i).getDate());
        }

        if (result.get(i).getLoginTime()==null||result.get(i).getLoginTime().equalsIgnoreCase("null")
                ||result.get(i).getLoginTime().equals("")){
            attendanceViewHolder.tv_login_value.setText("");
        }else{
            attendanceViewHolder.tv_login_value.setText(result.get(i).getLoginTime());
        }

        if (result.get(i).getLogoutTime()==null||result.get(i).getLogoutTime().equalsIgnoreCase("null")
                ||result.get(i).getLogoutTime().equals("")){
            attendanceViewHolder.tv_logout_value.setText("");
        }else{
            attendanceViewHolder.tv_logout_value.setText(result.get(i).getLogoutTime());
        }

        if (result.get(i).getJustification()==null||result.get(i).getJustification().equalsIgnoreCase("null")
                ||result.get(i).getJustification().equals("")){
            attendanceViewHolder.view3.setVisibility(View.GONE);
            attendanceViewHolder.tv_justification.setVisibility(View.GONE);
            attendanceViewHolder.tv_justification.setText("");
        }else{
            attendanceViewHolder.view3.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_justification.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_justification.setText("Justification: "+result.get(i).getJustification());
        }*/
    }

    private void showOptionsMenu(ImageView menu, AttendanceViewHolder attendanceViewHolder, int i) {

        PopupMenu popup = new PopupMenu(activity, menu);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_attendance, popup.getMenu());
        popup.setOnMenuItemClickListener(new PizzaMenuItemClickListener(i));
        popup.show();
    }


    private class PizzaMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int i;

        public PizzaMenuItemClickListener(int i) {
            this.i = i;
        }

        /**
         * Display Toast message on click of the options in the menu
         *
         * @param item
         * @return
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deviation:
                    Toast.makeText(activity, "Add to favourite", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, JustificationActivity.class);
                    intent.putExtra("attendence_id",results.get(i).getId());
                    activity.startActivity(intent);
                    return true;
                case R.id.ten_hrs:
                    Toast.makeText(activity, "Order Now", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(activity, AttendanceJustification.class);
                    myintent.putExtra("attendence_id",results.get(i).getId());
                    activity.startActivity(myintent);
                    return true;
            }
            return false;
        }
    }



    private void setPosition(AttendanceViewHolder attendanceViewHolder, final int i) {

        attendanceViewHolder.btJustify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AttendanceJustification.class);
                intent.putExtra("attendence_id",results.get(i).getId());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_login, tv_logout, tv_date_value, tv_login_value, tv_logout_value, tv_justification, tv_holiday;
        TextView tvDateValue;
        TextView tvLoginValue;
        TextView tvStatusValue;
        TextView tvLogoutValue;
        TextView tvLogoutLabel;
        TextView tvStatusLabel;
        Button btJustify,btJustifyAttendance;

        ImageView menu;

        //MaterialButton btJustify;
        //TextView tvLoginValue;
        View view3;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvDateValue = itemView.findViewById(R.id.tvDateValue);
            tvLoginValue = itemView.findViewById(R.id.tvLoginValue);
            tvStatusValue = itemView.findViewById(R.id.tvStatusValue);
            tvLogoutValue = itemView.findViewById(R.id.tvLogoutValue);
            tvLogoutLabel = itemView.findViewById(R.id.tvLogoutLabel);
            tvStatusLabel = itemView.findViewById(R.id.tvStatusLabel);
            btJustify = itemView.findViewById(R.id.btJustify);
            btJustifyAttendance = itemView.findViewById(R.id.btJustifyAttendance);

            menu = itemView.findViewById(R.id.menu);

            tvLogoutLabel.setTypeface(MethodUtils.getNormalFont(activity));
            tvStatusLabel.setTypeface(MethodUtils.getNormalFont(activity));
            btJustify.setTypeface(MethodUtils.getNormalFont(activity));

            //btJustify = itemView.findViewById(R.id.btJustify);


            tv_login = itemView.findViewById(R.id.tv_login);
            tv_logout = itemView.findViewById(R.id.tv_logout);
            tv_date_value = itemView.findViewById(R.id.tv_date_value);
            tv_login_value = itemView.findViewById(R.id.tv_login_value);
            tv_logout_value = itemView.findViewById(R.id.tv_logout_value);
            tv_justification = itemView.findViewById(R.id.tv_justification);
            tv_holiday = itemView.findViewById(R.id.tv_holiday);
            view3 = itemView.findViewById(R.id.view3);

            tv_date.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout.setTypeface(MethodUtils.getNormalFont(activity));
            tv_date_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_justification.setTypeface(MethodUtils.getNormalFont(activity));
            tv_holiday.setTypeface(MethodUtils.getNormalFont(activity));
            //btJustify.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
