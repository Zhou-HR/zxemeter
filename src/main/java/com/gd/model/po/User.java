package com.gd.model.po;

import com.gd.common.Update;
import com.gd.model.annotation.Crud;
import com.gd.model.annotation.Exclude;

import lombok.Data;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;

/**
 * 管理员
 *
 */
@Data
@Crud
public class User {

    @Min(value = 1, groups = Update.class)
    private int id;

    @NotBlank
    private String name;
    
    private String realname;

    @Length(min = 6)
    private String password;

    @Pattern(regexp = "^\\d+[\\d-]+\\d+$", message = "手机号不符合要求")
    private String phone;

    private Date createTime;
    
    private String dept;
    
    private String title;
    
    private Integer status;
    
    private String hour;
    
    private String everyHour;
    
    private String email;
    
    private String ddUserid;
}
