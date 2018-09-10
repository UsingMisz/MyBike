package com.example.administrator.mybike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mybike.content.UserCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends RxBaseActivity {

    @BindView(R.id.delete_username)
    ImageView mDeleteUserName;
  //  @BindView(R.id.et_username)
   // EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.register)
    TextView register;
     private EditText et_username;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        boolean login = PerferenceUtil.getBoolean(ConstantUtil.KEY,false);
         if(login){
             startActivity(new Intent(LoginActivity.this, MainActivity.class));
             finish();
         }



         et_username = findViewById(R.id.et_username);
     /*   et_username.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus && et_username.getText().length() > 0) {
                    mDeleteUserName.setVisibility(View.VISIBLE);
                } else {
                    mDeleteUserName.setVisibility(View.GONE);
                }
            }*/

           et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
               @Override
               public void onFocusChange(View view, boolean hasFocus) {
                   if (hasFocus && et_username.getText().length() > 0) {
                       mDeleteUserName.setVisibility(View.VISIBLE);
                   } else {
                       mDeleteUserName.setVisibility(View.GONE);
                   }
               }
           });


        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 如果用户名清空了 清空密码 清空记住密码选项
                et_password.setText("");
                if (s.length() > 0) {
                    // 如果用户名有内容时候 显示删除按钮
                    mDeleteUserName.setVisibility(View.VISIBLE);
                } else {
                    // 如果用户名有内容时候 显示删除按钮
                    mDeleteUserName.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册跳转
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();

            }
        });
    }

    @Override
    public void initToolBar() {

    }


    @OnClick({R.id.btn_login, R.id.delete_username})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                boolean isNetConnected = CommonUtil.isNetworkAvailable(this);
                if (!isNetConnected) {
                    ToastUtil.ShortToast("当前网络不可用,请检查网络设置");
                    return;
                }
                login();
                break;
            case R.id.delete_username:
                // 清空用户名以及密码
                et_username.setText("");
                et_password.setText("");
                mDeleteUserName.setVisibility(View.GONE);
                et_username.setFocusable(true);
                et_username.setFocusableInTouchMode(true);
                et_username.requestFocus();
                break;
        }
    }


    private void login() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.ShortToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.ShortToast("密码不能为空");
            return;
        }

        post(name,password);

    }

    private void post(String username,String password) {
        //用户登陆
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
        //字符串
        //转为json格式
         String value = changeJson(username,password);
       // String value = "{"+"username"+username+",password:"+password+"}";
       // String value={"username":"admin","password":"admin123"};
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过RequestBody.create 创建requestBody对象
        RequestBody requestBody =RequestBody.create(mediaType, value);
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(Constants.login).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.ShortToast("登陆失败，请重新登陆");
             e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

              //  Log.e("printssssssss", response.body().string()+"???????");

                //判断code
                Gson gson = new Gson();
                UserCode userCode=  gson.fromJson(response.body().string(), UserCode.class);
                if(userCode.getCode()==200){
                    //用户登陆成功以后
                     PerferenceUtil.putBoolean(ConstantUtil.KEY, true);
                     PerferenceUtil.put(ConstantUtil.USER_INFO,userCode.getData());
                     startActivity(new Intent(LoginActivity.this, MainActivity.class));
                     finish();
                }else {
                    ToastUtil.ShortToast("登陆失败，请重新登陆");
                }
            }


        });


    }

    private String  changeJson(String username, String password) {
      //  Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        System.out.println(jsonObject);
        return jsonObject.toString();
    }


}
