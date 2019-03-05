package com.lantingBletooth.Utils;

import com.lantingBletooth.Entity.User;

/**
 * Created by wym on 2017/11/10.
 */

public class Params {
//    public final static String host = "http://60.191.162.182:6070/lantingChild/";
     public final static String host = "http://192.168.242.135:8080/lantingChild/";
     public static String doctorRolename = "认证医师";
    /**
     * 状态-失败：-1
     */
    public static final int status_failed=-1;
    /**
     * 状态-成功：1
     */
    public static final int status_success=1;
    public static int isLogin = 0;//是否登入0未登入 ，1已登入
    public static User user = null;
    public static String userName = "";
    public static String password = "";
    public static long userId = 0;
    public static String roleName = "";
    public static String roleId = "";
    public static String realName = "";
    public static String companyName = "";
    public static long acuId = 0;
    public static AppHttpUtils appHttpUtils= null;
    public static final String split_comma = ",";
    /**
     * 列表获取全部
     */
    public static final int getListAll = -99;

    /**
     * 提示信息:参数不完整
     */
    public static final String info_err_notFull="参数不完整";


}
