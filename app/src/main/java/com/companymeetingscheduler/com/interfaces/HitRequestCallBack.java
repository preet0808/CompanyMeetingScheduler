package com.companymeetingscheduler.com.interfaces;



import java.io.IOException;

import okhttp3.Request;

public interface HitRequestCallBack {

    void onFailure(Request request, IOException e) ;
    void onResponse(String response)  ;


}
