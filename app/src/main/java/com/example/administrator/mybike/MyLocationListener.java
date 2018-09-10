package com.example.administrator.mybike;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import org.greenrobot.eventbus.EventBus;


public class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f

        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

        int errorCode = location.getLocType();
        Log.e("infolocation-----",latitude+"??"+"?"+longitude+"?"+coorType);
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        MapLocationInfo info = new MapLocationInfo();
        info.setLatitude(latitude);
        info.setLongitude(longitude);
        info.setRadius(radius);
        info.setCoorType(coorType);
        info.setErrorCode(errorCode);

        EventBus.getDefault().post(new MessageLocationEvent(info));
        //PerferenceUtil.putObj(ConstantUtil.MAP_LOCATION,info);
       // PerferenceUtil.put(ConstantUtil.TEST,coorType);
    }
/*    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f

        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

        int errorCode = location.getLocType();
        Log.e("infolocation-----",latitude+"??"+"?"+longitude);
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
    MapLocationInfo info = new MapLocationInfo();
    info.setLatitude(latitude);
    info.setLongitude(longitude);
    info.setRadius(radius);
    info.setCoorType(coorType);
    info.setErrorCode(errorCode);

        PerferenceUtil.putObj(ConstantUtil.MAP_LOCATION,info);
    }*/
}