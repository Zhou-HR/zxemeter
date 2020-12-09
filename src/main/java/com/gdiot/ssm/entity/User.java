package com.gdiot.ssm.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String realname;

    private String password;

    private String phone;

    private String dept;

    private String title;

    private Integer status;

    private Date createTime;

    private String deptCode;

    private String ddUserId;
}
