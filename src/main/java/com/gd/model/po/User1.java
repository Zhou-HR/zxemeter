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
import java.util.Map;

/**
 * 管理员
 *
 * @author ZhouHR
 */
@Data
@Crud
public class User1 {

    @Min(value = 1, groups = Update.class)
    private int id;

    @NotBlank
    private String name;

    @Pattern(regexp = "^\\d+[\\d-]+\\d+$", message = "手机号不符合要求")
    private String phone;

    private String dept;

    @Exclude
    private Map permission;
}
