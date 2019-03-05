package com.lantingBletooth.Entity;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * 蓝牙实体类
 * @author wangdandan
 *
 */
public class EntityDevice implements Serializable {

    private String name;
    private String address;
    private int deviceType;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
