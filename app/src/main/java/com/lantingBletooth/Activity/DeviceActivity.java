package com.lantingBletooth.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lantingBletooth.Base.BaseActivity;
import com.lantingBletooth.Entity.EntityDevice;
import com.lantingBletooth.R;
import com.lantingBletooth.adapter.DeviceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wym on 2018/9/10.
 */

public class DeviceActivity extends BaseActivity {
    @BindView(R.id.backLL)
    LinearLayout backLL;
    @BindView(R.id.TV1)
    TextView TV1;
    RecyclerView rvMain;
    private Context context;
    private List<EntityDevice> deviceList = new ArrayList<EntityDevice>();
    private DeviceAdapter adapter;
    private Intent intent;
    @Override
    public int getContentViewId() {
        return R.layout.activity_device;
    }
    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        intent = getIntent();
        deviceList = (List<EntityDevice>) intent.getSerializableExtra("deviceList");
        InitView();
    }
    private void InitView(){
        rvMain = findViewById(R.id.rv_main);
        rvMain.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DeviceAdapter(R.layout.item_device, deviceList,context);
        rvMain.setAdapter(adapter);
        //开启动画（默认为渐显效果）
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                BluetoothController.getInstance().connect(list.get(position));
//                BluetoothController.getInstance().connect(deviceList.get(position));
                intent = new Intent();
                intent.putExtra("device",deviceList.get(position));
                setResult(1,intent);
                finish();
            }
        });
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
