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

import com.example.administrator.mybike.content.UserCode;
import com.google.gson.Gson;
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

public class RegisterActivity extends RxBaseActivity {

@BindView(R.id.et_username)
    EditText et_username;
@BindView(R.id.et_password)
    EditText et_password;
@BindView(R.id.et_enter_password)
    EditText et_enter_Password;
@BindView(R.id.et_email)
    EditText et_email;
    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {


        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 如果用户名清空了 清空密码 清空记住密码选项
                et_password.setText("");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

     @OnClick(R.id.btn_register)
        void onClick(View view){
         switch (view.getId()) {
             case R.id.btn_register:
                 //登录
                 boolean isNetConnected = CommonUtil.isNetworkAvailable(this);
                 if (!isNetConnected) {
                     ToastUtil.ShortToast("当前网络不可用,请检查网络设置");
                     return;
                 }
                 login();
                 break;
         }
        }

    private void login() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String email = et_email.getText().toString();
        String ent_password = et_enter_Password.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.ShortToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.ShortToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(ent_password)){
            ToastUtil.ShortToast("确认密码不能为空");
            return;
        }
        if(!password.equals(ent_password)){
            ToastUtil.ShortToast("密码要相同");
            return;
        }
       post(name,password,email);

    }

    private void post(String username, String password, String email) {
//用户登陆
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
        //字符串
        //转为json格式
        String value = changeJson(username,password ,email);
        // String value = "{"+"username"+username+",password:"+password+"}";
        // String value={"username":"admin","password":"admin123"};
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过RequestBody.create 创建requestBody对象
        RequestBody requestBody =RequestBody.create(mediaType, value);
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(Constants.register).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.ShortToast("注册失败，请重新登陆");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  Log.e("printssssssss",response.toString()+response.code());
                //判断code
                Gson gson = new Gson();
                //UserCode userCode=  gson.fromJson(response.toString(), UserCode.class);

                UserCode userCode=  gson.fromJson(response.body().string(), UserCode.class);
                if(userCode.getCode()==200){
                    //注册成功以后
                    PerferenceUtil.putBoolean(ConstantUtil.KEY, true);
                    PerferenceUtil.put(ConstantUtil.USER_INFO,userCode.getData());
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }else if (response.code()==400) {

                    ToastUtil.ShortToast("注册失败，可能密码长度不够长吧，也有可能用户重复");
                }else
                {
                    ToastUtil.ShortToast("注册失败，请重新注册");
                }
            }
        });

    }

    private String changeJson(String username, String password, String email) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("email", email);
        System.out.println(jsonObject+"?????????????");
        return jsonObject.toString();


    }


    @Override
    public void initToolBar() {

    }
}
