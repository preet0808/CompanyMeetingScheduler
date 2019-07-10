package com.companymeetingscheduler.com.utility;



import android.view.View;

import com.companymeetingscheduler.com.activity.MainActivity;

import java.text.SimpleDateFormat;

public class UtilityMethods {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static void customToast(String msg) {

        MainActivity.toast_tv.setVisibility(View.VISIBLE);
        MainActivity.toast_tv.setText(msg);
        MainActivity.toast_tv.postDelayed(new Runnable() {
            public void run() {
                MainActivity.toast_tv.setVisibility(View.GONE);
            }
        }, 3000);
    }

}
