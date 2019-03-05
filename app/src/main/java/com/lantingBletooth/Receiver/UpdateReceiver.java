package com.lantingBletooth.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.lantingBletooth.Application.BleToothApplication;
import com.lantingBletooth.Entity.UpdateInfoModel;
import com.lantingBletooth.R;
import com.lantingBletooth.Service.UpdateService;
import com.lantingBletooth.Utils.UpdateInformation;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * 版本更新升级 广播接受者
 *
 */
public class UpdateReceiver extends BroadcastReceiver {
    private AlertDialog.Builder mDialog;
    public static final String UPDATE_ACTION = "wuyinlei_aixinwen";
    private boolean isShowDialog;

    public UpdateReceiver() {
    }

    public UpdateReceiver(boolean isShowDialog) {
        super();
        this.isShowDialog = isShowDialog;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //当然了，这里也可以直接new处hashmap
//        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        UpdateInfoModel model = new UpdateInfoModel();
        try {

            /**
             * 获取到当前的本地版本
             */
            UpdateInformation.localVersion = BleToothApplication
                    .instance()
                    //包管理独享
                    .getPackageManager()
                    //包信息
                    .getPackageInfo(
                            BleToothApplication.instance()
                                    .getPackageName(), 0).versionCode;
            /**
             * 获取到当前的版本名字
             */
            UpdateInformation.versionName = BleToothApplication
                    .instance()
                    .getPackageManager()
                    .getPackageInfo(
                            BleToothApplication.instance()
                                    .getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //app名字
        UpdateInformation.appname = BleToothApplication.instance()
                .getResources().getString(R.string.app_name);
        //服务器版本
        UpdateInformation.serverVersion = Integer.parseInt(model
                .getServerVersion());
        //服务器标志
        UpdateInformation.serverFlag = Integer.parseInt(model.getServerFlag());
        //强制升级
        UpdateInformation.lastForce = Integer.parseInt(model.getLastForce());
        //升级地址
        UpdateInformation.updateurl = model.getUpdateurl();
        //升级信息
        UpdateInformation.upgradeinfo = model.getUpgradeinfo();

        //检查版本
        checkVersion(context);

    }

    /**
     * 检查版本更新
     *
     * @param context
     */
    public void checkVersion(Context context) {
        if (UpdateInformation.localVersion < UpdateInformation.serverVersion) {
            // 需要进行更新
//            mSharedPreferencesHelper.putIntValue(
//                    //有新版本
//                    SharedPreferencesTag.IS_HAVE_NEW_VERSION, 1);
            //更新
            update(context);
        } else {
//            mSharedPreferencesHelper.putIntValue(
//                    SharedPreferencesTag.IS_HAVE_NEW_VERSION, 0);
            if (isShowDialog) {
                //没有最新版本，不用升级
                noNewVersion(context);
            }
            clearUpateFile(context);
        }
    }

    /**
     * 进行升级
     *
     * @param context
     */
    private void update(Context context) {
        if (UpdateInformation.serverFlag == 1) {
            // 官方推荐升级
            if (UpdateInformation.localVersion < UpdateInformation.lastForce) {
                //强制升级
                forceUpdate(context);
            } else {
                //正常升级
                normalUpdate(context);
            }

        } else if (UpdateInformation.serverFlag == 2) {
            // 官方强制升级
            forceUpdate(context);
        }
    }

    /**
     * 没有新版本
     * @param context
     */
    private void noNewVersion(final Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新");
        mDialog.setMessage("当前为最新版本");
        mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 强制升级 ，如果不点击确定升级，直接退出应用
     *
     * @param context
     */
    private void forceUpdate(final Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新");
        mDialog.setMessage(UpdateInformation.upgradeinfo);

        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent mIntent = new Intent(context, UpdateService.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtra("appname", UpdateInformation.appname);
                mIntent.putExtra("appurl", UpdateInformation.updateurl);
                //启动服务
                context.startService(mIntent);
            }
        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 直接退出应用
                //ManagerActivity.getInstance().finishActivity();
                System.exit(0);
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 正常升级，用户可以选择是否取消升级
     *
     * @param context
     */
    private void normalUpdate(final Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新");
        mDialog.setMessage(UpdateInformation.upgradeinfo);
        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent mIntent = new Intent(context, UpdateService.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //传递数据
                mIntent.putExtra("appname", UpdateInformation.appname);
                mIntent.putExtra("appurl", UpdateInformation.updateurl);
                context.startService(mIntent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 清理升级文件
     *
     * @param context
     */
    private void clearUpateFile(final Context context) {
        File updateDir;
        File updateFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            updateDir = new File(Environment.getExternalStorageDirectory(),
                    UpdateInformation.downloadDir);
        } else {
            updateDir = context.getFilesDir();
        }
        updateFile = new File(updateDir.getPath(), context.getResources()
                .getString(R.string.app_name) + ".apk");
        if (updateFile.exists()) {
            Log.d("update", "升级包存在，删除升级包");
            updateFile.delete();
        } else {
            Log.d("update", "升级包不存在，不用删除升级包");
        }
    }
}
