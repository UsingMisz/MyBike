package com.example.administrator.mybike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bumptech.glide.Glide;
import com.example.administrator.mybike.content.UserInfo;
import com.example.administrator.mybike.wiget.CommonDialogs;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.example.administrator.mybike.Constants.bikeLocation;
import static com.example.administrator.mybike.Constants.userInfo;

/**
 * 地图-------------------------------
 */
public class HomePageFragment extends RxLazyFragment{


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private RoutePlanSearch mSearch;

    @BindView(R.id.toolbar_user_avatar)
    CircleImageView mCircleImageView;

    //顶部时间信息
    @BindView(R.id.nav_bar_up_time)
    RelativeLayout mNavBarTime;

    @BindView(R.id.nav_up_time)
    Chronometer mNavTime;
    //private MapLocationInfo location;
     private  boolean isOpenBike = false;

    private CountDownTimer mTimer;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_pager;
    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void finishCreateView(Bundle state) {
        setHasOptionsMenu(true);
        //EventBus 注册
        EventBus.getDefault().register(this);
        mMapView = (MapView) getActivity().findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();


        initToolBar();
        //加载坐标点

        //加载地图
        initMap();





    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessage(MessageLocationEvent event) {
        Log.e("fragment location",event.getMessage().getLatitude()+"????");
       // event.getMessage().getLatitude();
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(event.getMessage().getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(event.getMessage().getLatitude())
                .longitude(event.getMessage().getLongitude()).build();

        mBaiduMap.setMyLocationData(locData);
        //缩放
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomBy(4);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
// 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        //设置定位的图标还有一些信息
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, BitmapDescriptorFactory .fromResource(R.mipmap.ic_launcher),
                0xAAFFFF88, 0xAA00FF00));

         //得到数据以后开始网络获取
        getBike();




        //多个点定位  批量定位
         setOverlayOption(event);



        //Log.e("addr","有问题");
    }

    /**
     * 得到车辆
     */
            private void getBike() {
              String token =  PerferenceUtil.getString(ConstantUtil.USER_INFO,"");
                String url = bikeLocation+"?Authorization="+token;
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(url)
                        .get()//默认就是GET请求，可以不写
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                          Log.d(TAG, "onResponse: getBike " + response.body().string());
                        //得到值
                        if (response.code() == 200) {
                            //解析

                            //填数据 切换线程





                        }
                    }

                });


            }


    /**
     * 绘制地图上的点
     * @param event
     */
    private void setOverlayOption(final MessageLocationEvent event) {


        //创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        //假的坐标点
        //获取坐标---本地位置
        final LatLng point = new LatLng(event.getMessage().getLongitude(),event.getMessage().getLatitude());
       //设置坐标点
        LatLng point1 = new LatLng(event.getMessage().getLongitude()+0.02, event.getMessage().getLatitude()+0.02);
        LatLng point2 = new LatLng(event.getMessage().getLongitude()+0.07, event.getMessage().getLatitude()+0.07);
        LatLng point3 = new LatLng(event.getMessage().getLongitude()+0.08, event.getMessage().getLatitude()+0.08);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.stable_cluster_marker_one_normal);
//创建OverlayOptions属性
        OverlayOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bitmap);
        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icon(bitmap);
        OverlayOptions option3=  new MarkerOptions()
                .position(point3)
                .icon(bitmap);
//将OverlayOptions添加到list
        options.add(option1);
        options.add(option2);
        options.add(option3);
        //在地图上批量添加
        mBaiduMap.addOverlays(options);

         //双击点击图标的时候------或长按


        //点击图标时候
         mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
             @Override
             public boolean onMarkerClick(Marker marker) {
                 //信息窗口
                 InfoWindow  infowindow=null;
                 //从marker中获取经纬度的信息来转化成屏幕的坐标
                 LatLng ll=marker.getPosition();
                 Point p=mBaiduMap.getProjection().toScreenLocation(ll);
                 p.y-=47;
                 LatLng llinfo=mBaiduMap.getProjection().fromScreenLocation(p);
                 //点击图标的时候更新路线
                 initRountin(point,llinfo);

                   View view = getLayoutInflater().inflate(R.layout.info_window,null);
                   //？？？？？？？？？？？？？？循环更新每一个点的具体位置------------------
                 //生成信息窗口
                 infowindow=new InfoWindow(view,llinfo,-47);
                 view.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Log.e("infoWindowsClickview--","true");
                         //查看标志位是否是 已经取车了 不然失效
                        // isOpenBike
                   //  PerferenceUtil.put(ConstantUtil.IS_OPEN_BIKE,false);
                  boolean isOpenBike =     PerferenceUtil.getBoolean(ConstantUtil.IS_OPEN_BIKE,true);
                        if(isOpenBike) {
                            //点击是否要取车
                            new CommonDialogs(getActivity(), R.style.dialog, "单车是否要解锁", new CommonDialogs.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, boolean confirm) {
                                    if (confirm) {
                                        ToastUtil.ShortToast("取车成功");

                                        //取车成功
                                        GetCarSuccess();
                                        boolean isOpenBike1 = false;
                                        PerferenceUtil.put(ConstantUtil.IS_OPEN_BIKE,isOpenBike1);

                                        //锁车成功
                                        CloseCarSuccess();
                                        dialog.dismiss();


                                    }
                                }
                            }).setTitle("提示").show();
                        }else {
                            ToastUtil.ShortToast("您已经取车了，请不要重复取车");
                        }





                     }


                     //隐藏消息窗口

                 });

                 new InfoWindow.OnInfoWindowClickListener() {
                     @Override
                     public void onInfoWindowClick() {
                          Log.e("infoWindowsClick----","true");

                            //隐藏消息窗口
                         mBaiduMap.hideInfoWindow();

                         //搜索 路线
                         mSearch.destroy();
                     }
                 };
                 //将信息窗口显示出来
                 mBaiduMap.showInfoWindow(infowindow);


                 ///将布局显示出来用来显示具体的内容
                // layout.setVisibility(View.VISIBLE);

                 return true;
             }
         });

        /*
         * 百度地图点击事件将InfoWindow隐藏
         */

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

                @Override
                public boolean onMapPoiClick(MapPoi arg0) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public void onMapClick(LatLng arg0) {
                    //layout.setVisibility(View.GONE);
                    mBaiduMap.hideInfoWindow();

                }
            });
    }
         //锁车成功
    private void CloseCarSuccess() {
        mNavBarTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonDialogs(getActivity(), R.style.dialog, "单车是否要归还", new CommonDialogs.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            ToastUtil.ShortToast("还车成功");

                            boolean isOpenBike = true;
                            PerferenceUtil.put(ConstantUtil.IS_OPEN_BIKE,isOpenBike);

                            mNavBarTime.setVisibility(View.GONE);
                            mNavTime.stop();


                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();
            }
        });
    }


    //取车成功
    private void GetCarSuccess() {
        mNavBarTime.setVisibility(View.VISIBLE);
        //显示车辆信息在标题并，开始计时
        mNavTime.setBase(SystemClock.elapsedRealtime());
        int hour = (int) ((SystemClock.elapsedRealtime() - mNavTime.getBase()) / 1000 / 60);
        mNavTime.setFormat("0"+String.valueOf(hour)+":%s");
        mNavTime.start();

    }

    //更新路线
    private void initRountin(LatLng loc_start ,LatLng loc_end) {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(myRouteListener);
        PlanNode stNode = PlanNode.withLocation(loc_start);
        PlanNode enNode = PlanNode.withLocation(loc_end);
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));

    }


    OnGetRoutePlanResultListener myRouteListener = new OnGetRoutePlanResultListener() {
        //获取步行线路规划结果
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
          //  Log.e("walkto---------",result.getSuggestAddrInfo()+"?????");
        }
        //获取综合公共交通线路规划结果
        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        }
        //获取**跨城**综合公共交通线路规划结果
        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
           // Log.e("walkto---------",massTransitRouteResult.getSuggestAddrInfo().toString()+"?????");
        }
        //获取驾车线路规划结果
        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        }
        //室内路线规划结果
        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
        }
        //获取普通骑行路规划结果
        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        }
    };



    //加载地图
    private void initMap() {



    }

    private void initToolBar() {

        mToolbar.setTitle("");
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        mCircleImageView.setImageResource(R.drawable.ico_user_default);
    }

 //读取数据点
    private void initLocation() {
       getLocation();
    }

    private void getLocation() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // activity 销毁时同时销毁地图控件

        EventBus.getDefault().unregister(this);

        mMapView.onDestroy();
        //模拟真正------xxx
        boolean isOpenBike = true;
        PerferenceUtil.put(ConstantUtil.IS_OPEN_BIKE,isOpenBike);
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件

        mMapView.onPause();
    }
}
