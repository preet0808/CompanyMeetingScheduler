package com.companymeetingscheduler.com.server;



import android.widget.Toast;

import com.companymeetingscheduler.com.activity.MainActivity;
import com.companymeetingscheduler.com.interfaces.HitRequestCallBack;
import com.companymeetingscheduler.com.utility.UtilityMethods;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HitRequest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();


    public static void forGetRequestOnly(String url,final HitRequestCallBack cb){

        final Request request = new Request.Builder()
                //    .header("Cache-control", "public, max-stale=1800")
                .header("Accept", "application/json")
                .url(url)
                .build();


        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        cb.onFailure(request,e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=   response.body().string();

                        if (response.isSuccessful()) {

                                cb.onResponse(result);

                        }

                    }
                });
    }

}