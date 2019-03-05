package com.lantingBletooth.Utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lantingBletooth.Entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public abstract class ResultCallback extends Callback<Result>
{

    @Override
    public Result parseNetworkResponse(Response response, int id) throws Exception
    {

        String string = response.body().string();
        Result result = JSON.parseObject(string, Result.class);
        Log.i("BRK", "parseNetworkResponse: "+Ut.json(string));
        return result;
    }

    @Override
    public void onError(Call call, Exception e, int id)
    {
        if(!NetworkUtils.isConnected()){
            ToastUtils.showShort("网络异常！");
        }else{
//            ToastUtils.showShort("出错！"+e.toString());
                ToastUtils.showShort("服务器跑丢了,请稍后再试");
        }

    }
}