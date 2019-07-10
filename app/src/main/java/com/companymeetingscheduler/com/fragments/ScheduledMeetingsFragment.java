package com.companymeetingscheduler.com.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.companymeetingscheduler.com.R;
import com.companymeetingscheduler.com.activity.MainActivity;
import com.companymeetingscheduler.com.adapters.ScheduledMeetingsAdapter;
import com.companymeetingscheduler.com.interfaces.Apis;
import com.companymeetingscheduler.com.interfaces.HitRequestCallBack;
import com.companymeetingscheduler.com.server.HitRequest;
import com.companymeetingscheduler.com.utility.Constants;
import com.companymeetingscheduler.com.utility.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduledMeetingsFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private ListView listView;
    private ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private ScheduledMeetingsAdapter adapter;

    private FloatingActionButton next, prev;
    private String today_date = "";
    Calendar calendar = Calendar.getInstance();
    Date new_date;
    ProgressDialog progressDialog;
    private TextView selected_date;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.menu.setText(Constants.TitleConstants.ScheduledMeetings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_scheduled_meetings, container, false);
            listView = mView.findViewById(R.id.listview);
            prev = mView.findViewById(R.id.prev);
            next = mView.findViewById(R.id.next);
            selected_date = mView.findViewById(R.id.selected_date);

            adapter = new ScheduledMeetingsAdapter(list);
            listView.setAdapter(adapter);

            prev.setOnClickListener(this);
            next.setOnClickListener(this);

            today_date = MainActivity.currentDate;
            selected_date.setText(today_date);

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading..");
            getDataFromApi(today_date);

            MainActivity.add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!UtilityMethods.dateFormat.parse(today_date).before(UtilityMethods.dateFormat.parse(UtilityMethods.dateFormat.format(new Date())))) {
                            ScheduleMeetingFragment fragment = new ScheduleMeetingFragment();
                            Bundle b = new Bundle();

                            b.putString("date", today_date);
                            b.putSerializable("list", list);
                            fragment.setArguments(b);
                            ((MainActivity) MainActivity.context).replaceFragment(fragment, true, Constants.FragmentTags.ScheduleMeeting, Constants.FragmentTags.ScheduleMeeting);
                        }else {
                            UtilityMethods.customToast("You cannot schedule meeting for past dates");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        return mView;

    }

    private void getDataFromApi(String date) {
       progressDialog.show();
        HitRequest.forGetRequestOnly(Apis.LIST_URL + date, new HitRequestCallBack() {
            @Override
            public void onFailure(Request request, IOException e) {
                ((MainActivity) (MainActivity.context)).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilityMethods.customToast("Server error,Try Again");
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(final String response) {
                Log.e("Ticket response", "" + response);

                if (response != null && !(response.isEmpty())) {

                    try {

                        list.clear();

                        JSONArray data_arr = new JSONArray(response);
                        for (int i = 0; i < data_arr.length(); i++) {
                            JSONObject obj = data_arr.getJSONObject(i);
                            HashMap hashMap = new HashMap();
                            hashMap.put("start_time", obj.getString("start_time"));
                            hashMap.put("end_time", obj.getString("end_time"));
                            hashMap.put("desc", obj.getString("description"));

                            JSONArray participant_arr = obj.getJSONArray("participants");
                            StringBuffer participant_names = new StringBuffer();

                            for (int j = 0; j < participant_arr.length(); j++) {
                                if (j != participant_arr.length() - 1)
                                    participant_names.append(participant_arr.getString(j) + ",\n");
                                else
                                    participant_names.append(participant_arr.getString(j));


                            }
                            hashMap.put("participant_names", participant_names.toString());
                            list.add(hashMap);
                        }
                        Collections.sort(list, new Comparator<HashMap<String, String>>() {

                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                                try {
                                    return UtilityMethods.timeFormat.parse(o1.get("start_time")).compareTo(UtilityMethods.timeFormat.parse(o2.get("start_time")));
                                } catch (ParseException e) {
                                    return 0;
                                }
                            }
                        });
                        ((MainActivity) MainActivity.context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        ((MainActivity) MainActivity.context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilityMethods.customToast("Something went wrong,please try again");
                                progressDialog.dismiss();

                            }
                        });
                    }
                } else {
                    list.clear();
                    ((MainActivity) MainActivity.context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev:
                try {
                    new_date = UtilityMethods.dateFormat.parse(today_date);

                    calendar.setTime(new_date);
                    calendar.add(Calendar.DATE, -1);
                    today_date = UtilityMethods.dateFormat.format(calendar.getTime());
                    selected_date.setText(today_date);
                    getDataFromApi(today_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.next:
                try {
                    new_date = UtilityMethods.dateFormat.parse(today_date);

                    calendar.setTime(new_date);
                    calendar.add(Calendar.DATE, +1);
                    today_date = UtilityMethods.dateFormat.format(calendar.getTime());
                    selected_date.setText(today_date);
                    getDataFromApi(today_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.add_iv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.add_iv.setVisibility(View.GONE);
    }
}


