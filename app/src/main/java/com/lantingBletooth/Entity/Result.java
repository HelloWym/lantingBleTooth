package com.lantingBletooth.Entity;

import java.util.List;

/**
 * Created by wym on 2017/11/24.
 */

public class Result {

    /**
     * 处理结果信息
     */
    private String info;
    /**
     * 处理结果状态
     */
    private int status;
    /**
     * 处理结果列表
     */
    private List list;

    /**
     * 处理结果-实体类
     */
    private Object obj;

    /**
     * 总记录数
     */
    private long total;
    /**
     * 获取 处理结果信息
     * @return info处理结果信息
     */
    public String getInfo() {
        return info;
    }
    /**
     * 设置 处理结果信息
     * @param info处理结果信息
     */
    public void setInfo(String info) {
        this.info = info;
    }
    /**
     * 获取 处理结果状态
     * @return status处理结果状态
     */
    public int getStatus() {
        return status;
    }
    /**
     * 设置 处理结果状态
     * @param status处理结果状态
     */
    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * 获取 处理结果列表
     * @return list处理结果列表
     */
    @SuppressWarnings("rawtypes")
    public List getList() {
        return list;
    }
    /**
     * 设置 处理结果列表
     * @param list处理结果列表
     */
    @SuppressWarnings("rawtypes")
    public void setList(List list) {
        this.list = list;
    }
    /**
     * 获取 总记录数
     * @return total总记录数
     */
    public long getTotal() {
        return total;
    }
    /**
     * 设置 总记录数
     * @param total总记录数
     */
    public void setTotal(long total) {
        this.total = total;
    }
    /**
     * 获取 处理结果-实体类
     * @return obj处理结果-实体类
     */
    public Object getObj() {
        return obj;
    }
    /**
     * 设置 处理结果-实体类
     * @param obj处理结果-实体类
     */
    public void setObj(Object obj) {
        this.obj = obj;
    }
}
