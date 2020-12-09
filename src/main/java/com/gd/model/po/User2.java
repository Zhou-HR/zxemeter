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
public class User2 {

    private int id;

    private String name;
    
    private String parentid;
    
}
