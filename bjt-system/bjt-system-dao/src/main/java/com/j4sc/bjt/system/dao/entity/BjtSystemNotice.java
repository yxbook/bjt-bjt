package com.j4sc.bjt.system.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 系统公告表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_system_notice")
public class BjtSystemNotice extends Model<BjtSystemNotice> {

    private static final long serialVersionUID = 1L;

    /**
     * 公告编号
     */
    @TableId(value = "s_notice_id", type = IdType.AUTO)
    private Integer sNoticeId;
    /**
     * 发布用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 发布者姓名
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Long ctime;
    /**
     * 修改时间
     */
    private Long utime;
    /**
     * 额外备注
     */
    private String source;
    /**
     * 公告类型
     */
    private Integer type;


    public Integer getsNoticeId() {
        return sNoticeId;
    }

    public void setsNoticeId(Integer sNoticeId) {
        this.sNoticeId = sNoticeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.sNoticeId;
    }

    @Override
    public String toString() {
        return "BjtSystemNotice{" +
        "sNoticeId=" + sNoticeId +
        ", userId=" + userId +
        ", name=" + name +
        ", content=" + content +
        ", ctime=" + ctime +
        ", utime=" + utime +
        ", source=" + source +
        ", type=" + type +
        "}";
    }
}
