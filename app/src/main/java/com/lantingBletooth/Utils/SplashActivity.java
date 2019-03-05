package com.lantingBletooth.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lantingBletooth.Base.BaseActivity;
import com.lantingBletooth.MainActivity;
import com.lantingBletooth.R;

/**
 * 欢迎界面
 * Created by wym on 2018/3/15.
 */

public class SplashActivity extends BaseActivity implements Runnable {
    final Handler mHandler = new Handler();
    private String usernamestr;
    private String passwordstr;
    private String isRememberPsd;//是否记住密码
    private String isAutoLogin;//是否自动登入
    private Context context;
    private ImageView img;
    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }
    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        context = this;
        img = findViewById(R.id.img);
        Glide.with(this).load(R.drawable.welcome).into(img);
        mHandler.postDelayed(this, 2000);

    }
    @Override
    public void run() {
        Intent intent = new Intent(context,MainActivity.class);
        startActivity(intent);
        // 此处可以不需要调用finish()了, 因为已经设置了noHistory属性, 从而使得系统接管finish操作
    }


}