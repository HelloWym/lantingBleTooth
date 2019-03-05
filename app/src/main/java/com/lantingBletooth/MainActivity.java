package com.lantingBletooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.lantingBletooth.Activity.DeviceActivity;
import com.lantingBletooth.Base.BaseActivity;
import com.lantingBletooth.Entity.EntityDevice;
import com.lantingBletooth.Service.BLEService;
import com.lantingBletooth.Utils.AccountKit;
import com.lantingBletooth.Utils.BluetoothController;
import com.lantingBletooth.Utils.ConstantUtils;
import com.lantingBletooth.Utils.MyUtils;
import com.lantingBletooth.Utils.ProgressDialog;
import com.lantingBletooth.Utils.ToastUtils;
import com.lantingBletooth.adapter.DeviceAdapter;
import com.lantingBletooth.views.FanImage;

import java.util.ArrayList;

import butterknife.BindView;

import static android.R.id.progress;
import static com.lantingBletooth.Utils.MyUtils.hexStringToByteArray;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.on_offbtn)
    ImageView onOffbtn;
    @BindView(R.id.count_downbtn)
    ImageView countDownbtn;
    @BindView(R.id.yaw_btn)
    ImageView yawBtn;
    @BindView(R.id.block_btn)
    ImageView blockBtn;
    @BindView(R.id.pattern_btn)
    ImageView patternBtn;
    @BindView(R.id.made_cold_btn)
    ImageView madeColdBtn;
    @BindView(R.id.TV1)
    TextView TV1;
    @BindView(R.id.add_menue)
    LinearLayout addMenue;
    @BindView(R.id.countdowntext)
    TextView countdowntext;
    @BindView(R.id.model)
    TextView model;
    @BindView(R.id.fanimg)
    FanImage fanimg;
    @BindView(R.id.temptext)
    TextView temptext;
    @BindView(R.id.tempvalue)
    TextView tempvalue;

    byte[] bytes = new byte[13];
    //各个按钮状态
    private int onOffbtn_status = 0, countDownbtn_status = 0, yawBtn_status = 0, blockBtn_status = 0, pattern = 0, cold = 0;
    private String[] itemStrings = new String[]{"关闭计时", "1小时", "2小时", "3小时", "4小时", "5小时", "6小时", "7小时", "8小时"};
    private CustomPopWindow popWindow;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private ArrayList<EntityDevice> list = new ArrayList<EntityDevice>();
    private DeviceAdapter adapter;
    private Intent intentService;
    private MsgReceiver receiver;
    private Context context;
    BluetoothController controller=BluetoothController.getInstance();
    private ProgressDialog progressDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private EntityDevice device;
    private int isConnect = 0;//是否连接
    private static final int REQUEST_COARSE_LOCATION = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        context = this;
        mayRequestLocation();
        //使用蓝牙进行相关操作之前，需要先获取蓝牙适配器
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //判断蓝牙是否可用或者是否开启，如果蓝牙关闭，那么开启蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        initView();
        initEvent();
        initService();
        registerReceiver();
        String name = AccountKit.getInstance().getDeviceName();
        String address = AccountKit.getInstance().getDeviceAddress();
        if(!MyUtils.isEmpty(address)){
            device = new EntityDevice();
            device.setName(name);
            device.setAddress(address);
            progressDialog = new ProgressDialog(context,"正在连接蓝牙");
            progressDialog.show();
            BluetoothController.getInstance().connect(device);
        }
    }
    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this, "动态请求权限", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
                return;
            }
        }
    }
    private void initView() {
        onOffbtn_status = 0;
        countDownbtn_status = 0;
        yawBtn_status = 0;
        blockBtn_status = 0;
        pattern = 0;
        cold = 0;
        if(isConnect==0){
            TV1.setText("智能冷风扇(未连接)");
        }else{
            if(device!=null){
                TV1.setText(device.getName()+"(已连接)");
            }
        }
        countdowntext.setVisibility(View.INVISIBLE);
        model.setVisibility(View.INVISIBLE);
        temptext.setVisibility(View.INVISIBLE);
        tempvalue.setVisibility(View.INVISIBLE);

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

    private void registerReceiver() {
        receiver=new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
        intentFilter.addAction(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_CONNECT);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_SCAN);
        registerReceiver(receiver, intentFilter);
    }

    private void initEvent() {
        onOffbtn.setOnClickListener(this);
        countDownbtn.setOnClickListener(this);
        yawBtn.setOnClickListener(this);
        blockBtn.setOnClickListener(this);
        patternBtn.setOnClickListener(this);
        madeColdBtn.setOnClickListener(this);
        addMenue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_menue:
                View contentView = LayoutInflater.from(context).inflate(R.layout.pop_menu, null);
                //处理popWindow 显示内容
                handleLogic(contentView);
                popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                        .setView(contentView)
                        .size(MyUtils.dip2px(context,150),MyUtils.dip2px(context,130)) //设置显示的大小，不设置就默认包裹内容
                        .setFocusable(true)
                        .setOutsideTouchable(true)
                        .create()
                        .showAsDropDown(addMenue, 0, 10);
                break;
            case R.id.on_offbtn://开关
                if(BluetoothController.getInstance().isConnect()==false){
                    new MaterialDialog.Builder(context)
                            .title("系统提示")
                            .content("请重新连接蓝牙")
                            .positiveText("确定")
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                }
                            })
                            .show();
                }else{
                    if (onOffbtn_status == 0) {//当前状态关
                        onOffbtn.setImageResource(R.mipmap.offon_on);
                        onOffbtn_status = 1;
                        //ABEF0000000103000101FFFCFFFF
                        //ABEF00000001010000FFFCFFFF//心跳
                        bytes = hexStringToByteArray("ABEF0000000103000101FFFCFFFF");
                        controller.write(bytes);
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);//开机默认一般模式
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        tempvalue.setVisibility(View.VISIBLE);
                        temptext.setVisibility(View.VISIBLE);
                        fanimg.startAnimation(1);
                    } else if (onOffbtn_status == 1) {//当前状态开
                        bytes = hexStringToByteArray("ABEF0000000103000100FFFCFFFF");
                        controller.write(bytes);
                        fanimg.stopAnimation();
                        closeAll();
                    }
                }

                break;
            case R.id.count_downbtn://倒计时
                if (onOffbtn_status == 0) {
                } else {
                    new MaterialDialog.Builder(this)
                            .items(itemStrings)
                            .contentColor(getResources().getColor(R.color.black))
                            //单选
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {//0 表示第一个选中 -1 不选
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    if (position == 0) { //关闭计时
                                        countDownbtn.setImageResource(R.mipmap.cutdown_off);
                                        countdowntext.setVisibility(View.INVISIBLE);
                                        countDownbtn_status = 0;
                                        bytes = hexStringToByteArray("ABEF0000000104000100FFFCFFFF");
                                        controller.write(bytes);
                                    } else {
                                        countdowntext.setVisibility(View.VISIBLE);
                                        countDownbtn.setImageResource(R.mipmap.cutdown_on);
                                        countDownbtn_status = 1;
                                        countdowntext.setText("倒计时:"+position+"小时");
                                        String s = "ABEF00000001040001";
                                        s = s + "0" + position + "FFFCFFFF";
                                        bytes = hexStringToByteArray(s);
                                        controller.write(bytes);
                                    }
                                    return true;
                                }
                            })
                            .show();
                }
                break;
            case R.id.yaw_btn://摆头
                if (onOffbtn_status == 0) {

                } else {
                    if (yawBtn_status == 0) {
                        yawBtn.setImageResource(R.mipmap.yaw_on);
                        yawBtn_status = 1;
                        bytes = hexStringToByteArray("ABEF0000000106000101FFFCFFFF");
                        controller.write(bytes);
                    } else if (yawBtn_status == 1) {
                        yawBtn.setImageResource(R.mipmap.yaw_down);
                        yawBtn_status = 0;
                        bytes = hexStringToByteArray("ABEF0000000106000100FFFCFFFF");
                        controller.write(bytes);
                    }
                }
                break;
            case R.id.block_btn://挡位
                if (onOffbtn_status == 0) {

                } else {
                    if(pattern==1){
                        if (blockBtn_status == 0) {
                            blockBtn.setImageResource(R.mipmap.one_block);
                            blockBtn_status = 1;
                            bytes = hexStringToByteArray("ABEF0000000102000101FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(1);
                        } else if (blockBtn_status == 1) {
                            blockBtn.setImageResource(R.mipmap.two_block);
                            blockBtn_status = 2;
                            bytes = hexStringToByteArray("ABEF0000000102000102FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(2);
                        } else if (blockBtn_status == 2) {
                            blockBtn.setImageResource(R.mipmap.three_block);
                            blockBtn_status = 3;
                            bytes = hexStringToByteArray("ABEF0000000102000103FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(3);
                        } else if (blockBtn_status == 3) {
                            blockBtn.setImageResource(R.mipmap.four_block);
                            blockBtn_status = 4;
                            bytes = hexStringToByteArray("ABEF0000000102000104FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(4);
                        } else if (blockBtn_status == 4) {
                            blockBtn.setImageResource(R.mipmap.five_block);
                            blockBtn_status = 5;
                            bytes = hexStringToByteArray("ABEF0000000102000105FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(5);
                        }else if (blockBtn_status == 5) {
                            blockBtn.setImageResource(R.mipmap.six_block);
                            blockBtn_status = 6;
                            bytes = hexStringToByteArray("ABEF0000000102000106FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(6);
                        }else if (blockBtn_status == 6) {
                            blockBtn.setImageResource(R.mipmap.seven_block);
                            blockBtn_status = 7;
                            bytes = hexStringToByteArray("ABEF0000000102000107FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(7);
                        }else if (blockBtn_status == 7) {
                            blockBtn.setImageResource(R.mipmap.eight_block);
                            blockBtn_status = 8;
                            bytes = hexStringToByteArray("ABEF0000000102000108FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(8);
                        }else if (blockBtn_status ==8) {
                            blockBtn.setImageResource(R.mipmap.one_block);
                            blockBtn_status = 1;
                            bytes = hexStringToByteArray("ABEF0000000102000101FFFCFFFF");
                            controller.write(bytes);
                            fanimg.startAnimation(1);
                        }

                    }else{

                    }
                }
                break;
            case R.id.pattern_btn:
                if (onOffbtn_status == 0) {
                } else {
                    if (pattern == 0) {//一般模式
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        bytes = hexStringToByteArray("ABEF0000000107000102FFFCFFFF");
                        controller.write(bytes);
                    } else if (pattern == 1) {//经济模式（ECO）
                        model.setText("模式:ECO模式");
                        patternBtn.setImageResource(R.mipmap.energy_pattern);
                        pattern = 2;
                        bytes = hexStringToByteArray("ABEF0000000107000101FFFCFFFF");
                        controller.write(bytes);
                        blockBtn_status = 0;
                        blockBtn.setImageResource(R.mipmap.no_block);
                    } else if (pattern == 2) {//自然模式
                        model.setText("模式:自然模式");
                        patternBtn.setImageResource(R.mipmap.nature_pattern);
                        pattern = 3;
                        bytes = hexStringToByteArray("ABEF0000000107000103FFFCFFFF");
                        controller.write(bytes);
                        blockBtn_status = 0;
                        blockBtn.setImageResource(R.mipmap.no_block);
                    } else if (pattern == 3) {
                        model.setText("模式:一般模式");
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        bytes = hexStringToByteArray("ABEF0000000107000102FFFCFFFF");
                        controller.write(bytes);
                        blockBtn_status = 0;
                        blockBtn.setImageResource(R.mipmap.no_block);
                    }
                }
                break;
            case R.id.made_cold_btn:
                if (onOffbtn_status == 0) {

                } else {
                    if (cold == 0) {
                        madeColdBtn.setImageResource(R.mipmap.made_cold_on);
                        cold = 1;
                        bytes = hexStringToByteArray("ABEF0000000105000101FFFCFFFF");
                        controller.write(bytes);
                    } else if (cold == 1) {
                        madeColdBtn.setImageResource(R.mipmap.made_cold_off);
                        cold = 0;
                        bytes = hexStringToByteArray("ABEF0000000105000100FFFCFFFF");
                        controller.write(bytes);
                    }
                }
                break;
        }
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null) {
                    popWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.menu1://扫描
                        progressDialog = new ProgressDialog(context,"正在搜索");
                        progressDialog.show();
                        BluetoothController.getInstance().startScanBLE();
                        break;
                    case R.id.menu3://断开
                        BluetoothController.getInstance().stopConnect();
                        isConnect = 0;
                        TV1.setText("智能冷风扇(未连接)");
                        AccountKit.getInstance().setDeviceAddress("");
                        AccountKit.getInstance().setDeviceName("");
                        break;
                }
            }
        };
        contentView.findViewById(R.id.menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.menu3).setOnClickListener(listener);
    }
    //总开关关闭
    private void closeAll() {
        onOffbtn_status = 0;
        countDownbtn_status = 0;
        yawBtn_status = 0;
        blockBtn_status = 0;
        pattern = 0;
        cold = 0;
        onOffbtn.setImageResource(R.mipmap.offon_off);
        countDownbtn.setImageResource(R.mipmap.cutdown_off);
        yawBtn.setImageResource(R.mipmap.yaw_down);
        blockBtn.setImageResource(R.mipmap.no_block);
        patternBtn.setImageResource(R.mipmap.common_pattern_off);
        madeColdBtn.setImageResource(R.mipmap.made_cold_off);
        countdowntext.setVisibility(View.INVISIBLE);
        model.setVisibility(View.INVISIBLE);
    }

    //根据返回的指令改变界面状态
    private void changeView(final byte[] value){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                if(value[6]==(byte)0x08){//下位机反馈温度
                    tempvalue.setText(value[9]+"℃");
                }else if(value[6]==(byte)0x01&&value[8]==(byte)0x05){//下位机反馈所有按钮状态
                    if(value[9]==(byte)0x01){//总开关开
                        if(onOffbtn_status==0){
                            bytes = hexStringToByteArray("ABEF00000001010000FFFCFFFF");
                            controller.write(bytes);
                            onOffbtn_status = 1;
                            onOffbtn.setImageResource(R.mipmap.offon_on);
                            fanimg.startAnimation(1);
                        }
                    }else if(value[9]==(byte)0x00){//总开关关
                        if(onOffbtn_status==1){
                            fanimg.stopAnimation();
                            closeAll();
                        }
                    }
                    if(value[10]==(byte)0x21&&value[9]==(byte)0x01){//eco模式
                        patternBtn.setImageResource(R.mipmap.energy_pattern);
                        pattern = 2;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:ECO模式");
                    }else if(value[10]==(byte)0x22&&value[9]==(byte)0x01){//自然模式
                        patternBtn.setImageResource(R.mipmap.nature_pattern);
                        pattern = 3;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:自然模式");
                    }else if(value[10]==(byte)0x01&&value[9]==(byte)0x01){//一般模式1档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        blockBtn.setImageResource(R.mipmap.one_block);
                        blockBtn_status = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        fanimg.startAnimation(1);
                    }else if(value[10]==(byte)0x02&&value[9]==(byte)0x01){//一般模式2档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.two_block);
                        blockBtn_status = 2;
                        fanimg.startAnimation(2);
                    }else if(value[10]==(byte)0x03&&value[9]==(byte)0x01){//一般模式3档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.three_block);
                        blockBtn_status = 3;
                        fanimg.startAnimation(3);
                    }else if(value[10]==(byte)0x04&&value[9]==(byte)0x01){//一般模式4档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.four_block);
                        blockBtn_status = 4;
                        fanimg.startAnimation(4);
                    }else if(value[10]==(byte)0x05&&value[9]==(byte)0x01){//一般模式5档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.five_block);
                        blockBtn_status = 5;
                        fanimg.startAnimation(5);
                    }else if(value[10]==(byte)0x06&&value[9]==(byte)0x01){//一般模式6档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.six_block);
                        blockBtn_status = 6;
                        fanimg.startAnimation(6);
                    }else if(value[10]==(byte)0x07&&value[9]==(byte)0x01){//一般模式7档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.seven_block);
                        blockBtn_status = 7;
                        fanimg.startAnimation(7);
                    }else if(value[10]==(byte)0x08&&value[9]==(byte)0x01){//一般模式8档
                        patternBtn.setImageResource(R.mipmap.common_pattern_on);
                        pattern = 1;
                        model.setVisibility(View.VISIBLE);
                        model.setText("模式:一般模式");
                        blockBtn.setImageResource(R.mipmap.eight_block);
                        blockBtn_status = 8;
                        fanimg.startAnimation(8);
                    }

                    if(value[11]==(byte)0x01&&value[9]==(byte)0x01){//制冷开
                        madeColdBtn.setImageResource(R.mipmap.made_cold_on);
                        cold = 1;
                    }else if(value[11] ==(byte)0x00&&value[9]==(byte)0x01){//制冷关
                        madeColdBtn.setImageResource(R.mipmap.made_cold_off);
                        cold = 0;
                    }
                    if(value[12]==(byte)0x01&&value[9]==(byte)0x01){//摆头开
                        yawBtn.setImageResource(R.mipmap.yaw_on);
                        yawBtn_status = 1;
                    }else if(value[12] ==(byte)0x00&&value[9]==(byte)0x01){//摆头关
                        yawBtn.setImageResource(R.mipmap.yaw_down);
                        yawBtn_status = 0;
                    }
                }
            }
        });
    }
    private static BluetoothController bluetoothController;
    /**
     * 广播接收器
     *
     */
    public class MsgReceiver extends BroadcastReceiver {
        private  BluetoothController bluetoothController;
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(
                    ConstantUtils.ACTION_UPDATE_DEVICE_LIST)) {
                String name = intent.getStringExtra("name");
                String address = intent.getStringExtra("address");
                String devicetype = intent.getStringExtra("deviceType");
                boolean found=false;//记录该条记录是否已在list中，
                for(EntityDevice device:list){
                    if(device.getAddress().equals(address)){
                        found=true;
                        break;
                    }
                }// for
                if(!found){
                    EntityDevice temp = new EntityDevice();
                    if(!MyUtils.isEmpty(name)){
                        temp.setName(name);
                    }
                    if(!MyUtils.isEmpty(address)){
                        temp.setAddress(address);
                    }
                    if(!MyUtils.isEmpty(devicetype)){
                        temp.setDeviceType(Integer.parseInt(devicetype));
                    }
                    list.add(temp);
                }
            }
            else if(intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_STOP_SCAN)){//停止扫描
                BluetoothController.getInstance().stopScanBLE();
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                intent = new Intent(context,DeviceActivity.class);
                intent.putExtra("deviceList",list);
                startActivityForResult(intent,1);
            }
            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE)){//已连接蓝牙
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                AccountKit.getInstance().setDeviceName(device.getName());
                AccountKit.getInstance().setDeviceAddress(device.getAddress());
                TV1.setText(device.getName()+"(已连接)");
                isConnect = 1;
            }
            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_STOP_CONNECT)){//断开连接或未连接成功
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                isConnect = 0;
                TV1.setText("智能冷风扇(未连接)");
            }
            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE)){//接收设备信息
                String msg = intent.getStringExtra("message");
                changeView(MyUtils.hexStringToByteArray(msg));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) { //连接蓝牙设备
            device = (EntityDevice) data.getSerializableExtra("device");

            if(device!=null){
                progressDialog = new ProgressDialog(context,"正在连接蓝牙");
                progressDialog.show();
                BluetoothController.getInstance().connect(device);
            }


        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("222222","onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("1111111","Destroy");
        isConnect = 0;
        BluetoothController.getInstance().stopConnect();
        stopService(intentService);
        unregisterReceiver(receiver);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0) {
            switch (requestCode) {
                case REQUEST_COARSE_LOCATION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //权限被允许
                    } else {
                        // 权限被拒绝
                        Toast.makeText(MainActivity.this, "定位权限未开启，请手动去开启！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
