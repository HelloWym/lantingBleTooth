package com.lantingBletooth.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wym on 2018/4/20.
 */

public class MyUtils {
    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //Byte数组转十六进制
    public static String byte2HexString(byte[] bytes) {
        String hex= "";
        if (bytes != null) {
            for (Byte b : bytes) {
                hex += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return hex;
    }
    /**
     * dp转为px
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    public static int dip2px(Context context, float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());

    }
    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
    /**
     * 检查字符串对象是否为空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input){
        if(null==input || "".equals(input))
            return true;
        return false;
    }
    //十六进制转Byte数组
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
        } catch (Exception e) {
            //Log.d("", "Argument(s) for hexStringToByteArray(String s)"+ "was not a hex string");
        }
        return data;
    }
    //判断收到的指令是否完整，不完整则丢掉
    public static boolean messageReceived(byte[] value ) {//接收到信息
        byte[] messages = value;
        int length = messages.length;
        if (messages.length >= 13 && messages[0] == (byte) 0xab && messages[1] == (byte) 0xef &&
                messages[length - 4] == (byte) 0xff && messages[length - 3] == (byte) 0xfc &&
                messages[length - 2] == (byte) 0xff && messages[length - 1] == (byte) 0xff) {//当前接收数据为N条完整指令
            Log.d("messageReceived", "本次指令完整,直接处理");
            return true;
        }
        return false;
    }
}
