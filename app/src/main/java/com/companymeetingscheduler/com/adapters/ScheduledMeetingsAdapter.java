package com.companymeetingscheduler.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.companymeetingscheduler.com.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.companymeetingscheduler.com.activity.MainActivity.context;

public class ScheduledMeetingsAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> objects;
    private LayoutInflater inflater;


    public ScheduledMeetingsAdapter(ArrayList<HashMap<String, String>> objects) {

        this.objects = objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub

        if (objects.size() > position) {
            return objects.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder();

            holder.main_ll=view.findViewById(R.id.main_ll);
            holder.start_time = view.findViewById(R.id.start_time);
            holder.end_time = view.findViewById(R.id.end_time);
            holder.desc = view.findViewById(R.id.desc);
            holder.participants = view.findViewById(R.id.participants);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if (objects.size() > position) {

            if (position%2==0){
                holder.main_ll.setBackgroundColor(context.getResources().getColor(R.color.white));
            }else {
                holder.main_ll.setBackgroundColor(context.getResources().getColor(R.color.glass));
            }

            HashMap<String, String> object = (HashMap<String, String>) objects.get(position);

            holder.start_time.setText(object.get("start_time"));
            holder.end_time.setText(object.get("end_time"));
            holder.desc.setText(object.get("desc"));
            holder.participants.setText(object.get("participant_names"));

        }

        return view;
    }

    public static class ViewHolder {
        TextView start_time, end_time, desc, participants;
        LinearLayout main_ll;

    }

}
