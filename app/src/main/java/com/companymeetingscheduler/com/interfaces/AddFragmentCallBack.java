package com.companymeetingscheduler.com.interfaces;

import android.support.v4.app.Fragment;

public interface AddFragmentCallBack {
   void replaceFragment(Fragment fragment, boolean addToBackStack, String transactionName, String tag);
}
