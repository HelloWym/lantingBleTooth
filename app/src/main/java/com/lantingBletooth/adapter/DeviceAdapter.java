package com.lantingBletooth.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lantingBletooth.Entity.EntityDevice;
import com.lantingBletooth.R;

import java.util.List;

/**
 *  ¿∂—¿…Ë±∏
 * Created by wym on 2018/9/10.
 */

public class DeviceAdapter extends BaseQuickAdapter<EntityDevice, BaseViewHolder>
{
    public Context context;
    public DeviceAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }
    @Override
    protected void convert(final BaseViewHolder helper, EntityDevice item) {
        helper.setText(R.id.device_name,item.getName());
    }
}