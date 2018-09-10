package com.example.administrator.mybike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.mybike.content.UserCode;
import com.example.administrator.mybike.content.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * 用户主页
 *
 */
public class UserFagment extends RxLazyFragment{

    @BindView(R.id.user_avatar_view)
    CircleImageView mAvatarImage;
    @BindView(R.id.user_name)
    TextView mUserNameText;
    @BindView(R.id.user_desc)
    TextView mDescriptionText;
    @BindView(R.id.tv_follow_users)
    TextView mFollowNumText;
    @BindView(R.id.tv_fans)
    TextView mFansNumText;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.user_lv)
    ImageView mUserLv;
    @BindView(R.id.user_sex)
    ImageView mUserSex;
    @BindView(R.id.author_verified_layout)
    LinearLayout mAuthorVerifiedLayout;
    @BindView(R.id.author_verified_text)
    TextView mAuthorVerifiedText;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.line)
    View mLineView;

    private int mid;
    private String name = "";
    private String avatar_url;

    public static UserFagment newInstance() {
        return new UserFagment();
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_user;
    }

    @Override
    public void finishCreateView(Bundle state) {
           //得到token数据
        String token = PerferenceUtil.getString(ConstantUtil.USER_INFO,"ss");
          //网络请求数据
        Log.e("token2222222",token);
        get(token);

    }

    private void get(String token) {
        String url = Constants.userInfo+"?Authorization="+token;
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
             //   Log.d(TAG, "onResponse: " + response.body().string());
                //得到值
              if(response.code()==200){
                  //解析
                  Gson gson = new Gson();
                  final UserInfo userInfo=  gson.fromJson(response.body().string(), UserInfo.class);
                    //填数据 切换线程

                 getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          setToAdapter(userInfo);
                      }
                  });

                  //Log.e("name",userInfo.getData().getUsername());

              }
            }



                //设置数据
            private void setToAdapter(UserInfo userInfo) {
                //加载头像
                Glide.with(getActivity()).load(userInfo.getData().getAvatar()).into(mAvatarImage);
               //
                mUserNameText.setText(userInfo.getData().getUsername());
                mDescriptionText.setText(userInfo.getData().getEmail());


            }
        });
        }





}
