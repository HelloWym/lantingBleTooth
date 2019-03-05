package com.lantingBletooth.Utils;

import com.alibaba.fastjson.JSON;
import com.lantingBletooth.Application.BleToothApplication;
import com.lantingBletooth.Entity.User;

/**
 * Created by wym on 2018/5/22.
 */

public class AccountKit {
    private final String USER = "user";
    private final String device_name = "deviceName";
    private final String device_address = "deviceAddress";

    private static AccountKit instance = new AccountKit();

    public static AccountKit getInstance() {
        return instance;
    }

    public AccountKit setUser(String user) {
        BleToothApplication.instance().getSharedPreferences().edit().putString(USER, user).commit();
        return this;
    }
    public User getUser() {
        User user = JSON.parseObject(BleToothApplication.instance().getSharedPreferences().getString(USER, ""), User.class);
        return  user;
    }
    public AccountKit setDeviceName(String deviceName){
        BleToothApplication.instance().getSharedPreferences().edit().putString(device_name, deviceName).commit();
        return this;
    }
    public String getDeviceName() {
        String name = BleToothApplication.instance().getSharedPreferences().getString(device_name, "");
        return  name;
    }
    public AccountKit setDeviceAddress(String deviceAddress){
        BleToothApplication.instance().getSharedPreferences().edit().putString(device_address, deviceAddress).commit();
        return this;
    }
    public String getDeviceAddress() {
        String address = BleToothApplication.instance().getSharedPreferences().getString(device_address, "");
        return  address;
    }

}
