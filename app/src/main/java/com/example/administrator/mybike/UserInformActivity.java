package com.example.administrator.mybike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.mybike.content.UserInfo;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.example.administrator.mybike.Constants.userInfo;

public class UserInformActivity extends RxBaseActivity {
      @BindView(R.id.profile_image)
      CircleImageView imageView;
        @BindView(R.id.tv_phone)
    TextView textView;
        @BindView(R.id.tv_name)
        TextView name;


    @Override
    public int getLayoutId() {
        return R.layout.activity_user_inform;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //得到token数据
        String token = PerferenceUtil.getString(ConstantUtil.USER_INFO,"ss");
        //加载用户
        get(token);

        //修改用户 不存在的
       // update(token,);
    }


    private void get(String token) {
        String url = userInfo+"?Authorization="+token;
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
                if (response.code() == 200) {
                    //解析
                    Gson gson = new Gson();
                    final UserInfo userInfo = gson.fromJson(response.body().string(), UserInfo.class);
                    //填数据 切换线程

                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setToAdapter(userInfo);
                        }
                    });

                    //Log.e("name",userInfo.getData().getUsername());

                }
            }

            private void setToAdapter(UserInfo userInfo) { //设置数据
                    //加载头像
                    Glide.with(getApplicationContext()).load(userInfo.getData().getAvatar()).into(imageView);
                    //
                   name.setText(userInfo.getData().getUsername());
                   textView.setText(userInfo.getData().getEmail());



            }
        });

    }

    @Override
    public void initToolBar() {

    }
}
