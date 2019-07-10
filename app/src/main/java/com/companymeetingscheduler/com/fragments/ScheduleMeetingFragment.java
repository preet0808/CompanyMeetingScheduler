package com.companymeetingscheduler.com.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.companymeetingscheduler.com.R;
import com.companymeetingscheduler.com.activity.MainActivity;
import com.companymeetingscheduler.com.utility.Constants;
import com.companymeetingscheduler.com.utility.UtilityMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleMeetingFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private TextView date_tv, start_time, end_time, submit;
    Calendar c = Calendar.getInstance();
    private int hour;
    private int minute;
    private String selected_text = "";
    private String start_time_str = "", end_time_str = "";
    private ArrayList<HashMap<String, String>> time_list = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> comp_time_list = new ArrayList<>();

    public ScheduleMeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.menu.setText(Constants.TitleConstants.ScheduleMeeting);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_schedule_meeting, container, false);
            start_time = mView.findViewById(R.id.start_time);
            end_time = mView.findViewById(R.id.end_time);
            date_tv = mView.findViewById(R.id.date);
            submit = mView.findViewById(R.id.submit);

            start_time.setOnClickListener(this);
            end_time.setOnClickListener(this);
            date_tv.setOnClickListener(this);
            submit.setOnClickListener(this);

            Bundle b = getArguments();
            if (b != null) {
                date_tv.setText(b.getString("date"));
                time_list = (ArrayList<HashMap<String, String>>) b.getSerializable("list");
                for (int i = 0; i < time_list.size(); i++) {
                    comp_time_list.add(time_list.get(i).get("start_time"));
                    comp_time_list.add(time_list.get(i).get("end_time"));
                }
            }
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                showFromDatePicker();
                break;

            case R.id.start_time:
                selected_text = "start";
                // Current Hour
                hour = c.get(Calendar.HOUR_OF_DAY);
                // Current Minute
                minute = c.get(Calendar.MINUTE);
                // set current time into output textview
                new TimePickerDialog(MainActivity.context, timePickerListener, hour, minute, true).show();
                break;

            case R.id.end_time:
                selected_text = "end";

                hour = c.get(Calendar.HOUR_OF_DAY);
                // Current Minute
                minute = c.get(Calendar.MINUTE);
                // set current time into output textview
                new TimePickerDialog(MainActivity.context, timePickerListener, hour, minute, true).show();
                break;

            case R.id.submit:
                try {
                    boolean flag = false;
                    for (int i = 0; i < time_list.size(); i++) {
                        Date time1 = UtilityMethods.timeFormat.parse(time_list.get(i).get("start_time"));
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);

                        Date time2 = UtilityMethods.timeFormat.parse(time_list.get(i).get("end_time"));
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(time2);


                        Date d1 = UtilityMethods.timeFormat.parse(start_time_str);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(d1);
                        Date x1 = calendar3.getTime();

                        Date d2 = UtilityMethods.timeFormat.parse(end_time_str);
                        Calendar calendar4 = Calendar.getInstance();
                        calendar4.setTime(d2);
                        Date x2 = calendar4.getTime();

                       if (x1.equals(time1)&&x2.equals(time2)){
                           flag=true;
                           break;
                       }
                    }
                    if (flag) {
                        UtilityMethods.customToast("This slot is not available");
                    } else {
                        UtilityMethods.customToast("Meeting scheduled successfully");
                        if (getFragmentManager().getBackStackEntryCount() > 1) {
                            getFragmentManager().popBackStack();
                        }
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

        }

    };

    private void updateTime(int hours, int mins) {

        try {


            String minutes = "";
            if (mins < 10)
                minutes = "0" + mins;
            else
                minutes = String.valueOf(mins);

            // Append in a StringBuilder
            String aTime = new StringBuilder().append(hours).append(':')
                    .append(minutes).append(" ").toString();

            if (selected_text.equals("start")) {
                start_time_str = aTime;
                start_time.setText(aTime);
            } else if (selected_text.equals("end")) {
                if (UtilityMethods.timeFormat.parse(aTime).after(UtilityMethods.timeFormat.parse(start_time_str))) {
                    end_time_str = aTime;
                    end_time.setText(aTime);
                }else {
                    UtilityMethods.customToast("End time should be after start time");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFromDatePicker() {
//        final Date date = new Date(MainActivity.currentDate);
        Calendar fromCalendar = Calendar.getInstance();
        final SimpleDateFormat mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog mFromDatePickerDialog = new DatePickerDialog(MainActivity.context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                long selectedMilli = calendar.getTimeInMillis();
                Date datePickerDate = new Date(selectedMilli);
                if (datePickerDate.before(new Date())) {
                    UtilityMethods.customToast("You can not select past date.");
                    return;
                }

                Calendar fromDate = Calendar.getInstance();
                fromDate.set(year, monthOfYear, dayOfMonth);
                date_tv.setText(mDateFormatter.format(fromDate.getTime()));
            }

        }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DATE));
        // fromCalendar.add(Calendar.DATE,1);
        mFromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        mFromDatePickerDialog.show();
    }
}
