package com.j4sc.bjt.system.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 下载信息表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_system_download")
public class BjtSystemDownload extends Model<BjtSystemDownload> {

    private static final long serialVersionUID = 1L;

    /**
     * 链接key
     */
    @TableId("key_id")
    private String keyId;
    /**
     * 链接地址
     */
    private String value;


    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    protected Serializable pkVal() {
        return this.keyId;
    }

    @Override
    public String toString() {
        return "BjtSystemDownload{" +
        "keyId=" + keyId +
        ", value=" + value +
        "}";
    }
}
