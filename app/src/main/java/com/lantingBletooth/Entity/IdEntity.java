package com.lantingBletooth.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wym .
 * 主键-实体类
 *  *
 */
public class IdEntity implements Serializable {

    private long id;
    //创建人
    private String author;
    //记录创建人ID
    private long authorId;
    //记录创建日期
    private Date dateCreater;

    /**
     * 获取 id
     */
    public long getId() {
        return id;
    }

    /**
     * 设置 id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 返回 创建人
     */
    public String getAuthor() {
        return author;
    }


    /**
     * 设置 创建人
     * <p>
     * *            创建人
     */
    public void setAuthor(String author) {
        this.author = author;
    }


    /**
     * 返回 记录创建人ID
     */
    public long getAuthorId() {
        return authorId;
    }


    /**
     * 设置 记录创建人ID
     * <p>
     * *            记录创建人ID
     */
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    /**
     * 返回 记录创建日期
     */
    public Date getDateCreater() {
        return dateCreater;
    }


    /**
     * 设置 记录创建日期
     *记录创建日期
     */
    public void setDateCreater(Date dateCreater) {
        this.dateCreater = dateCreater;
    }

}