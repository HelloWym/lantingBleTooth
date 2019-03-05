package com.lantingBletooth.Utils;

import com.lantingBletooth.Application.BleToothApplication;
import com.lantingBletooth.R;

/**
 * Created by wym on 2018/9/24.
 */

public class UpdateInformation {
    public static String appname = BleToothApplication.instance()
            .getResources().getString(R.string.app_name);
    public static int localVersion = 1;// 本地版本
    public static String versionName = ""; // 本地版本名
    public static int serverVersion = 1;// 服务器版本
    public static int serverFlag = 0;// 服务器标志
    public static int lastForce = 0;// 之前强制升级版本
    public static String updateurl = "";// 升级包获取地址
    public static String upgradeinfo = "";// 升级信息

    public static String downloadDir = "wuyinlei";// 下载目录
}
