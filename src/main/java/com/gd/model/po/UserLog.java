package com.gd.model.po;

import java.io.Serializable;
import java.util.Date;

import com.gd.model.annotation.Crud;
import com.gd.model.annotation.Id;

import lombok.Data;

/**
 * <p>
 * 用户操作日志
 * </p>
 *
 * @author tian
 * @since 2017-03-09
 */
@Data
@Crud(table = "t_user_log")
public class UserLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * IP
     */
    private String ip;
    /**
     * 操作记录
     */
    private String summary;
    /**
     * 记录时间
     */
    private Date createTime;
    private String remark1;
    private String remark2;
}
