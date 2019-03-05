package com.lantingBletooth.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.LayoutInflater;

import com.lantingBletooth.Entity.User;
import com.lantingBletooth.MainActivity;
import com.lantingBletooth.Utils.CrashHandler;
import com.lantingBletooth.Utils.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wym on 2018/4/8.
 */

public class BleToothApplication extends Application {
    private static BleToothApplication instance;
    private static List<Activity> activityList = new ArrayList<>();
    private User user;
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//               .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Utils.init(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());

        Beta.canNotShowUpgradeActs.add(MainActivity.class);
        Bugly.init(this,"9d037c6d70", false);
    }

    public BleToothApplication() {
        instance = this;
    }

    public static BleToothApplication instance() {
        if (instance != null)
            return instance;
//        throw new IllegalStateException("Application has not been created");
        return null;
    }

    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("LantingBleTooth", Context.MODE_PRIVATE);
    }


    public static List<Activity> getActivityList() {
        return activityList;
    }

    public static void clearActivityList() {
        if (activityList != null)
            activityList.clear();
    }
}
