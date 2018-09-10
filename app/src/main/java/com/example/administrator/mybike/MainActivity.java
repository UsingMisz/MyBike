package com.example.administrator.mybike;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends RxBaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private Fragment[] fragments;
    private int currentTabIndex;
    private int index;
    private long exitTime;
    private HomePageFragment mHomePageFragment;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();


    @Override
    public int getLayoutId() {
        SDKInitializer.initialize(getApplicationContext());

        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {


        //初始化Fragment
        initFragments();
        //初始化侧滑菜单
        initNavigationView();
        initLocation();
    }

    private void initLocation() {

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setWifiCacheTimeOut(5*60*1000);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);
        mLocationClient.start();//启动服务
    }


    private void initFragments() {
        mHomePageFragment = HomePageFragment.newInstance();
        UserFagment mUserFragment= UserFagment.newInstance();


        fragments = new Fragment[]{
                mHomePageFragment,
                mUserFragment,

        };
        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mHomePageFragment)
                .show(mHomePageFragment).commit();
    }
    private void initNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        View headerView = mNavigationView.getHeaderView(0);
        CircleImageView mUserAvatarView = (CircleImageView) headerView.findViewById(R.id.user_avatar_view);
        TextView mUserName = (TextView) headerView.findViewById(R.id.user_name);
        TextView mUserSign = (TextView) headerView.findViewById(R.id.user_other_info);
        //设置头像
        mUserAvatarView.setImageResource(R.drawable.ico_user_default);
        mUserAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserInformActivity.class);
                startActivity(intent);
            }
        });
        //设置用户名 签名
        mUserName.setText(getResources().getText(R.string.username));
        mUserSign.setText(getResources().getText(R.string.about_user_head_layout));

    }
    @Override
    public void initToolBar() {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.item_home:
                // 主页
                changeFragmentIndex(menuItem, 0);
                return true;
            case R.id.item_user:
                // 用户
                changeFragmentIndex(menuItem, 1);
                return true;
            case R.id.item_exit:
                // 退出
               clear();
                return true;

        }
        return false;
    }

    private void clear() {
        PerferenceUtil.removeAll();
        finish();
    }


    /**
     * Fragment切换
     */
    private void switchFragment() {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTabIndex]);
        if (!fragments[index].isAdded()) {
            trx.add(R.id.container, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
    }


    /**
     * 切换Fragment的下标
     */
    private void changeFragmentIndex(MenuItem item, int currentIndex) {
        index = currentIndex;
        switchFragment();
        item.setChecked(true);
    }


    /**
     * DrawerLayout侧滑菜单开关
     */
    public void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }



    /**
     * 监听back键处理DrawerLayout和SearchView
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLayout.getChildAt(1))) {
                mDrawerLayout.closeDrawers();
            } else {
                if (mHomePageFragment != null) {
                        exitApp();
                } else {
                    exitApp();
                }
            }
        }
        return true;
    }


    /**
     * 双击退出App
     */
    private void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.ShortToast("再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            PerferenceUtil.remove(ConstantUtil.SWITCH_MODE_KEY);
            finish();
        }
    }





}
