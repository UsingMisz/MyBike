package com.example.administrator.mybike;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;


public class BikeApp  extends Application{
    public static BikeApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        SDKInitializer.initialize(getApplicationContext());



        //初始化Leak内存泄露检测工具
          //调试工具
    }

    public static BikeApp getInstance() {
        return mInstance;
    }
}
