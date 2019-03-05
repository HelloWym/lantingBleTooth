package com.lantingBletooth.Activity;

import java.util.ArrayList;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lantingBletooth.Entity.EntityDevice;
import com.lantingBletooth.R;
import com.lantingBletooth.Receiver.UpdateReceiver;
import com.lantingBletooth.Service.BLEService;
import com.lantingBletooth.Utils.BluetoothController;
import com.lantingBletooth.Utils.ConstantUtils;
import com.lantingBletooth.Utils.MyUtils;
import com.lantingBletooth.Utils.ProgressDialog;
import com.lantingBletooth.Utils.ToastUtils;
import com.lantingBletooth.adapter.DeviceAdapter;

/**
 * 主界面
 */
public class MainActivity extends Activity {
    private Button search;
    private RecyclerView recyclerView;
    private ArrayList<EntityDevice> list = new ArrayList<EntityDevice>();
    private DeviceAdapter adapter;
    private Intent intentService;
    private MsgReceiver receiver;
    private UpdateReceiver updateReceiver;
    private Context context;

    private TextView connectedDevice;
    private TextView receivedMessage;

    private EditText editSend;
    private Button btnSend;
    BluetoothController controller=BluetoothController.getInstance();
    String str="";
    byte[] bytes = new byte[13];
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);
        context = this;
        initView();
        initService();//初始化服务
        initData();
        addListener();
        registerReceiver();//初始化动态广播

    }

    private void addListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BluetoothController.getInstance().connect(list.get(position));
            }
        });
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                list.clear();//清空上次的搜索结果
                connectedDevice.setText("");
                adapter.notifyDataSetChanged();
                if(!BluetoothController.getInstance().initBLE()){//手机不支持蓝牙
                    ToastUtils.showShort("您的手机不支持蓝牙");
                    return;//手机不支持蓝牙就啥也不用干了，关电脑睡觉去吧
                }
                if (!BluetoothController.getInstance().isBleOpen()) {// 如果蓝牙还没有打开
                    ToastUtils.showShort("请打开蓝牙");
                    return;
                }
                progressDialog = new ProgressDialog(context,"正在搜索");
                progressDialog.show();
//                new GetDataTask().execute();// 搜索任务
                BluetoothController.getInstance().startScanBLE();

            }
        });

        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String on = "ABEF0000000103000101FFFCFFFF";
                String off = "ABEF0000000103000100FFFCFFFF";
                if(str==null||str.equals("")||str.equals(off)){
                    str = on;
                }else{
                    str = off;
                }

                if(str!=null&&str.length()>0){
                    controller.write(MyUtils.hexStringToByteArray(str));
                }
                else {
                    toast("请填上要发送的内容");
                }

            }
        });
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DeviceAdapter(R.layout.item_device, list,context);
        recyclerView.setAdapter(adapter);
        //开启动画（默认为渐显效果）
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }

    /**
     * 开始服务, 初始化蓝牙
     */
    private void initService() {
        //开始服务
        intentService = new Intent(MainActivity.this,BLEService.class);
        startService(intentService);
        // 初始化蓝牙
        BluetoothController.getInstance().initBLE();
    }

    /**
     * findViewById
     */
    private void initView() {
        connectedDevice=(TextView) findViewById(R.id.connected_device);
        receivedMessage=(TextView) findViewById(R.id.received_message);
        recyclerView = (RecyclerView) findViewById(R.id.list_devices);
        editSend=(EditText) findViewById(R.id.edit_send);
        btnSend=(Button) findViewById(R.id.btn_send);
        search = (Button) findViewById(R.id.btn_search);
    }

    private void registerReceiver() {
        updateReceiver = new UpdateReceiver();
        receiver=new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
        intentFilter.addAction(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_CONNECT);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_SCAN);
        registerReceiver(receiver, intentFilter);

        IntentFilter mIntentFilter = new IntentFilter(UpdateReceiver.UPDATE_ACTION);
        registerReceiver(updateReceiver, mIntentFilter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            if(BluetoothController.getInstance().isBleOpen()){
                BluetoothController.getInstance().startScanBLE();
            };// 开始扫描
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

    /**
     * 广播接收器
     *
     */
    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(
                    ConstantUtils.ACTION_UPDATE_DEVICE_LIST)) {
                String name = intent.getStringExtra("name");
                String address = intent.getStringExtra("address");
                boolean found=false;//记录该条记录是否已在list中，
                for(EntityDevice device:list){
                    if(device.getAddress().equals(address)){
                        found=true;
                        break;
                    }
                }// for
                if(!found){
                    EntityDevice temp = new EntityDevice();
                    temp.setName(name);
                    temp.setAddress(address);
                    list.add(temp);
                    adapter.notifyDataSetChanged();
                }
            }
            else if(intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_STOP_SCAN)){//停止扫描
                BluetoothController.getInstance().stopScanBLE();
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                intent = new Intent(context,DeviceActivity.class);
                intent.putExtra("deviceList",list);
                startActivity(intent);
            }
            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE)){//连接蓝牙
                connectedDevice.setText("连接的蓝牙是："+intent.getStringExtra("address"));
            }

            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_STOP_CONNECT)){//断开连接
                connectedDevice.setText("");
                toast("连接已断开");
            }

            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE)){//接收设备信息
                receivedMessage.append("\n\r"+intent.getStringExtra("message"));
            }
        }
    }


    private void toast(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentService);
        unregisterReceiver(receiver);
        unregisterReceiver(updateReceiver);
    }

}
