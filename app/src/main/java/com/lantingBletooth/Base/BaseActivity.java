package com.lantingBletooth.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.lantingBletooth.Utils.AppManager;

import butterknife.ButterKnife;

/**
 * Created by wym on 2017/12/25.
 */

public abstract class BaseActivity extends FragmentActivity {
    public abstract int getContentViewId();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initAllMembersView(savedInstanceState);
    }
    protected abstract void initAllMembersView(Bundle savedInstanceState);
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();//解除绑定，官方文档只对fragment做了解绑
    }
}
