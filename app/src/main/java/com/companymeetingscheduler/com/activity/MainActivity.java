package com.companymeetingscheduler.com.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.companymeetingscheduler.com.R;
import com.companymeetingscheduler.com.fragments.ScheduledMeetingsFragment;
import com.companymeetingscheduler.com.interfaces.AddFragmentCallBack;
import com.companymeetingscheduler.com.utility.Constants;
import com.companymeetingscheduler.com.utility.UtilityMethods;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AddFragmentCallBack {
    public static Context context;
    public static boolean isNetworkConnected = true;
    public static FrameLayout mFragmentContainer;
    public static TextView toast_tv,menu;
    public static ImageView add_iv;
    public static String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        mFragmentContainer = (FrameLayout) findViewById(R.id.mFragmentContainer);
        toast_tv = findViewById(R.id.toast_tv);
        menu=findViewById(R.id.menu);
        add_iv=findViewById(R.id.add_iv);

        currentDate = UtilityMethods.dateFormat.format(new Date());

        replaceFragment(new ScheduledMeetingsFragment(), false, Constants.FragmentTags.ScheduledMeetings, Constants.FragmentTags.ScheduledMeetings);
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBackStack, String transactionName, String tag) {
        try {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(mFragmentContainer.getId(), fragment, tag);
            if (addToBackStack)
                fragmentTransaction.addToBackStack(transactionName);

            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();


            if (isConnected) {

                isNetworkConnected = true;
            } else {

                isNetworkConnected = false;
                Toast.makeText(getApplicationContext(), "Check your internet !", Toast.LENGTH_LONG).show();
            }
        }
    };
}
